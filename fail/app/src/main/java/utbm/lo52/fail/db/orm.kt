package utbm.lo52.fail.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.KType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.typeOf

/**
 * Choose automatically the right method from a given type to query data
 *
 * @param type
 * @param name
 * @return Any?
 */
@ExperimentalStdlibApi
fun Cursor.getType(type: KType, name: String): Any? = when (type) {
    typeOf<Int?>() -> this.getInt(this.getColumnIndex(name))
    typeOf<Int>() -> this.getInt(this.getColumnIndex(name))
    typeOf<String>() -> this.getString(this.getColumnIndex(name))
    typeOf<ForeignKey>() -> {
        val className = name.split("__").last().capitalize()
        ForeignKey(
            DBHelper.classFromAttributeName(className) as KClass<out ModelClass>,
            this.getInt(this.getColumnIndex("${name}_id"))
        )
    }
    else -> null
}

/**
 * Abstract class to inherit to be usable by the ORM
 * All properties from a ModelClass are created as entry in a dedicated table
 *
 * @property id if null, means that the object is not created
 */
abstract class ModelClass(open val id: Int? = null) {
    companion object {
        /**
         * Create a new ModelClass from a db cursor at the right location
         *
         * @param klass Type to create
         * @param cursor Positioned a the right place on the database
         * @return A new ModelClass from the klass type
         */
        @ExperimentalStdlibApi
        fun modelClassFromDBFactory(klass: KClass<*>, cursor: Cursor): ModelClass {
            val kwargs = HashMap<KParameter, Any?>()
            val constructor = klass.constructors.first()
            for (arg in constructor.parameters) {
                kwargs[arg] = cursor.getType(arg.type, "${klass.simpleName}__${arg.name!!}")
            }
            return klass.constructors.first().callBy(kwargs) as ModelClass
        }

        /**
         * Copy a given ModelClass instance only changing the id for a new one
         *
         * @param obj to copy
         * @param id to override with
         * @return a new ModelClass from the obj type
         */
        fun modelClassCopyWithNewIDFactory(obj: ModelClass, id: Int): ModelClass {
            val kwargs = HashMap<KParameter, Any?>()
            val constructor = obj::class.constructors.first()
            for (arg in constructor.parameters) {
                if (arg.name!! == "id")
                    kwargs[arg] = id
                else
                    kwargs[arg] =
                        obj::class.memberProperties.find { it.name == arg.name }?.getter?.call(obj)
            }
            return constructor.callBy(kwargs) as ModelClass
        }

        /**
         * Get all properties of a given ModelCLass instance except its id
         *
         * @param obj a ModelClass instance
         * @return a ContentValues dict
         */
        @ExperimentalStdlibApi
        fun modelClassToContentValuesExceptID(obj: ModelClass): ContentValues {
            val properties = ContentValues()
            for (property in obj::class.memberProperties) {
                when (property.returnType) {
                    typeOf<ForeignKey>() -> properties.put(
                        "${property.name}_id",
                        (property.getter.call(obj) as ForeignKey).id
                    )
                    else -> properties.put(property.name, property.getter.call(obj).toString())
                }
            }
            properties.remove("id")
            return properties
        }
    }
}

/**
 * A Foreign key class to create a relation between two models in database
 *
 * @property klass related into the database
 * @property id foreign id on the target model table
 */
data class ForeignKey(val klass: KClass<out ModelClass>, var id: Int?) {

    @ExperimentalStdlibApi
    private var related: ModelClass? = null
        set(value) {
            related = value; id = related?.id
        }

    /**
     * Query the related object into the database
     * Don't return anything if no id is given
     *
     * @param helper
     */
    @ExperimentalStdlibApi
    fun getRelated(helper: DBHelper) =
        if (id == null) null else helper.request(klass).get("id", id!!)
}

/**
 * Dumb conversion from a Kotlin type into a SQL type
 *
 * @param type Any
 * @return string
 */
fun typeSQLConverter(type: Any) = when (type) {
    is Int -> "INTEGER"
    is String -> "TEXT"
    is ForeignKey -> "INTEGER"
    else -> ""
}


/**
 * Handle the database from creation to deletion
 * Handle request via an ORM
 *
 * @constructor
 *
 * @param context
 * @param factory
 */
@ExperimentalStdlibApi
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    /**
     * Create the database from class listed in MODELS of the companion object
     *
     * @param db
     */
    @ExperimentalStdlibApi
    override fun onCreate(db: SQLiteDatabase) {
        Log.v("INFO", "CREATING DATABASE")
        // Create a table for each class
        db.execSQL("PRAGMA foreign_keys = ON;")
        for (model in MODELS) {
            var table = "CREATE TABLE ${model.simpleName?.toLowerCase()} ("
            for (member in model.memberProperties) {
                if (member.name == "id") {
                    table += "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    continue
                }
                table += when (member.returnType) {
                    typeOf<Int>() -> " ${member.name} ${typeSQLConverter(17)} NOT NULL,"
                    typeOf<String>() -> " ${member.name} ${typeSQLConverter("")} NOT NULL,"
                    // Only works for constraints because we named foreign key property like the table name
                    // Also only work because table name are only one word so we can just capitalize the first letter
                    typeOf<ForeignKey>() -> " ${member.name}_id ${typeSQLConverter(17)} NOT NULL " +
                            "REFERENCES ${member.name.capitalize()}(id) " +
                            "ON UPDATE CASCADE " +
                            "ON DELETE CASCADE,"
                    else -> ""
                }
            }
            table = "${table.substring(0, table.length - 1)})"
            Log.v("INFO", "Creating table with command : `$table`")
            db.execSQL(table)
        }
    }

    /**
     * SQL request builder
     *
     * @property db connection to the database
     * @property klass instance type of the object to query
     * @property request builded request
     */
    inner class SQLRequest(val db: SQLiteDatabase, val klass: KClass<*>, val request: String?) {

        private val lastRequest: String
            get() = if (request == null) "SELECT ${craftSelect(klass)} FROM ${klass.simpleName} ${joinKeys()} WHERE" else "$request AND "

        /**
         * Add a systematic left join on every foreign key
         * It's slow but with this data size it's not really important
         * Enable an easy query on foreign key properties
         *
         * @return join for the lastRequest
         */
        private fun joinKeys(): String {
            var join = ""
            for (member in klass.memberProperties) {
                join += when (member.returnType) {
                    typeOf<ForeignKey>() -> "left join ${member.name.capitalize()} on " +
                            "${klass.simpleName}.${member.name}_id=${member.name.capitalize()}.id "
                    else -> ""
                }
            }
            return join
        }

        /**
         * Enumerate all select possibilities to avoid clash with left joins
         * Recursively get all foreign classes to also name them
         * The format is always Table__attribute
         *
         * @return select to apply to lastRequest
         */
        private fun craftSelect(klass: KClass<*>, recurse: Boolean = true): String {
            var select = ""
            for (member in klass.memberProperties) {
                select += when (member.returnType) {
                    typeOf<ForeignKey>() -> "${klass.simpleName}.${member.name}_id AS ${klass.simpleName}__${member.name}_id," +
                            if (recurse) {
                                craftSelect(
                                    classFromAttributeName(member.name)!!,
                                    false
                                ) + ","
                            } else ""
                    else -> "${klass.simpleName}.${member.name} AS ${klass.simpleName}__${member.name},"
                }
            }

            return select.subSequence(0, select.length - 1).toString()
        }

        /**
         * Get the first object matching the search
         *
         * @param attribute to filter
         * @param value to filter
         * @return a ModelClass from the klass type
         */
        @ExperimentalStdlibApi
        fun get(attribute: String, value: Any) = filter(attribute, value).first()

        /**
         * Convert the request to SQL
         *
         * @return String
         */
        fun toSQL() = lastRequest.subSequence(0, lastRequest.length - 5).toString()

        /**
         * Get the first element of the query
         *
         * @return a ModelCLass from the klass type
         */
        fun first(): ModelClass? {
            val cursor = db.rawQuery("${toSQL()} LIMIT 1", null)
            if (cursor!!.count == 0)
                return null
            cursor.moveToFirst()
            return ModelClass.modelClassFromDBFactory(klass, cursor)
        }

        /**
         * Get a list of object matching the previously builded request
         *
         * @return List<ModelClass as klass>
         */
        @ExperimentalStdlibApi
        fun all(): List<ModelClass> {
            val list = ArrayList<ModelClass>()
            // Execute request
            val request = toSQL()
            val cursor = db.rawQuery(request, null)
            if (cursor!!.count == 0)
                return list
            cursor.moveToFirst()
            list.add(ModelClass.modelClassFromDBFactory(klass, cursor))
            while (cursor.moveToNext())
                list.add(ModelClass.modelClassFromDBFactory(klass, cursor))
            return list
        }

        /**
         * Apply a filter onto the request
         *
         * @param attribute to filter
         * @param value to filter
         * @return a newly generated SQLRequest
         */
        fun filter(attribute: String, value: Any): SQLRequest =
            filterRelated(klass, attribute, value)

        /**
         * Apply a filter searching on the specified related content of a foreign key
         *
         * @param relatedClass
         * @param attribute
         * @param value
         * @return a newly generated SQLRequest
         */
        fun filterRelated(relatedClass: KClass<*>, attribute: String, value: Any): SQLRequest =
            SQLRequest(
                db,
                klass,
                "$lastRequest ${relatedClass.simpleName}__${attribute}='${value.toString()}'"
            )

        /**
         * Check if an object exists for this request and return a boolean
         * WARNING : This is not optimized
         *
         * @return Boolean
         */
        fun exists(): Boolean {
            return first() != null
        }
    }

    /**
     * Insert a new object into the database
     *
     * @param obj to insert
     * @return a copy of obj with a new id
     */
    private fun insert(obj: ModelClass): ModelClass {
        this.writableDatabase.insert(
            obj::class.simpleName,
            null,
            ModelClass.modelClassToContentValuesExceptID((obj))
        )
        val cursor = this.writableDatabase.rawQuery(
            "SELECT last_insert_rowid() AS last_id FROM ${obj::class.simpleName}",
            null
        )
        cursor.moveToFirst()
        return ModelClass.modelClassCopyWithNewIDFactory(
            obj,
            cursor.getInt(cursor.getColumnIndex("last_id"))
        )
    }

    /**
     * Update an object into the database
     *
     * @param obj to update into the database
     * @return the given obj
     */
    private fun update(obj: ModelClass): ModelClass {
        this.writableDatabase.update(
            obj::class.simpleName,
            ModelClass.modelClassToContentValuesExceptID(obj),
            "id=${obj.id}",
            null
        )
        return obj
    }

    /**
     * Save an object into the database
     * If exists, update into the database
     * Otherwise, create a new entry and return a new instance with the corresponding id
     *
     * @param obj
     * @return the obj with a matching id
     */
    fun save(obj: ModelClass?): ModelClass? {
        if (obj == null)
            return null
        // Save all related foreign keys
        for (property in obj::class.memberProperties) {
            when (property.returnType) {
                typeOf<ForeignKey>() -> save(
                    (property.getter?.call(obj) as ForeignKey).getRelated(
                        this
                    )
                )
            }
        }
        if (obj.id == null) {
            // that's an insert
            return insert(obj)
        }
        // that's an update
        return update(obj)
    }

    /**
     * Delete an object from the database
     *
     * @param obj to delete
     */
    fun delete(obj: ModelClass) {
        this.writableDatabase.delete(obj::class.simpleName, "id=${obj.id}", null)
    }

    /**
     * Request an object onto the database
     *
     * @param klass type to query into the database
     * @return SQLRequest
     */
    fun request(klass: KClass<*>) = SQLRequest(this.writableDatabase, klass, null)

    @ExperimentalStdlibApi
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.v("INFO", "Upgrading Database")
        for (model in MODELS) {
            db.execSQL("DROP TABLE IF EXISTS ${model.simpleName}")
            Log.v("INFO", "Droping table ${model.simpleName}")
        }
        onCreate(db)
    }

    companion object {
        private val MODELS =
            listOf(Player::class, Team::class, Race::class, Lap::class)
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "fail.db"
        fun classFromAttributeName(name: String) =
            MODELS.find { it.simpleName == name.capitalize() }
    }
}

