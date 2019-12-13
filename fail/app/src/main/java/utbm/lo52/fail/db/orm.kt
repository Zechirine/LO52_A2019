package utbm.lo52.fail.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.graphics.ColorSpace
import android.util.Log
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.reflect.*
import kotlin.reflect.full.memberProperties

@ExperimentalStdlibApi
fun Cursor.getType(type: KType, name: String): Any? = when (type) {
    typeOf<Int?>() -> this.getInt(this.getColumnIndex(name))
    typeOf<Int>() -> this.getInt(this.getColumnIndex(name))
    typeOf<String>() -> this.getString(this.getColumnIndex(name))
    typeOf<ForeignKey>() -> ForeignKey(
        DBHelper.classFromAttributeName(name) as KClass<ModelClass>,
        this.getInt(this.getColumnIndex("${name}_id"))
    )
    else -> null
}

abstract class ModelClass(open val id: Int? = null) {
    companion object {
        @ExperimentalStdlibApi
        fun modelClassFromDBFactory(klass: KClass<*>, cursor: Cursor): ModelClass {
            val kwargs = HashMap<KParameter, Any?>()
            val constructor = klass.constructors.first()
            for (arg in constructor.parameters) {
                kwargs[arg] = cursor.getType(arg.type, arg.name!!)
            }
            return klass.constructors.first().callBy(kwargs) as ModelClass
        }

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

        @ExperimentalStdlibApi
        fun modelClassToContentValuesExceptID(obj: ModelClass): ContentValues {
            val properties = ContentValues()
            for (property in obj::class.memberProperties) {
                when (property.returnType) {
                    typeOf<ForeignKey>() -> properties.put("${property.name}_id", (property.getter.call(obj) as ForeignKey).id)
                    else -> properties.put(property.name, property.getter.call(obj).toString())
                }
            }
            properties.remove("id")
            return properties
        }
    }
}

data class ForeignKey(val klass: KClass<out ModelClass>, var id: Int?){

    @ExperimentalStdlibApi
    private var related: ModelClass? = null
        set(value) { related = value; id = related?.id }

    @ExperimentalStdlibApi
    fun getRelated(helper: DBHelper) = if (id == null) null else helper.request(klass).get("id", id!!)
}

fun typeSQLConverter(type: Any) = when (type) {
    is Int -> "INTEGER"
    is String -> "TEXT"
    is ForeignKey -> "INTEGER"
    else -> ""
}


@ExperimentalStdlibApi
class DBHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {
    @ExperimentalStdlibApi
    override fun onCreate(db: SQLiteDatabase) {
        Log.v("INFO", "CREATING DATABASE")
        for (model in MODELS) {
            var table = "CREATE TABLE ${model.simpleName?.toLowerCase()} ("
            for (member in model.members) {
                if (member !is KProperty<*>) {
                    continue
                }
                if (member.name == "id") {
                    table += "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    continue
                }
                table += when (member.returnType) {
                    typeOf<Int>() -> " ${member.name} ${typeSQLConverter(17)} NOT NULL,"
                    typeOf<String>() -> " ${member.name} ${typeSQLConverter("")} NOT NULL,"
                    typeOf<ForeignKey>() -> " ${member.name}_id ${typeSQLConverter(17)} NOT NULL,"
                    else -> ""
                }
            }
            table = "${table.substring(0, table.length - 1)})"
            Log.v("INFO", "Creating table with command : `$table`")
            db.execSQL(table)
        }
    }

    inner class SQLRequest(val db: SQLiteDatabase, val klass: KClass<*>, val request: String?) {

        private val lastRequest: String
            get() = if (request == null) "SELECT * FROM ${klass.simpleName} WHERE" else "$request AND"

        @ExperimentalStdlibApi
        fun get(attribute: String, value: Any): ModelClass? {
            val cursor = db.rawQuery("$lastRequest ${attribute}='${value.toString()}'", null)
            if (cursor!!.count == 0)
                return null
            cursor.moveToFirst()
            return ModelClass.modelClassFromDBFactory(klass, cursor)
        }

        @ExperimentalStdlibApi
        fun all(): List<ModelClass> {
            val list = ArrayList<ModelClass>()
            // Remove last word from last query (remove WHERE and AND)
            val request = lastRequest.subSequence(0, lastRequest.length - 4).toString()
            val cursor = db.rawQuery(request, null)
            if (cursor!!.count == 0)
                return list
            cursor.moveToFirst()
            list.add(ModelClass.modelClassFromDBFactory(klass, cursor))
            while (cursor.moveToNext())
                list.add(ModelClass.modelClassFromDBFactory(klass, cursor))
            return list
        }

        fun filter(attribute: String, value: Any): SQLRequest =
            SQLRequest(db, klass, "$lastRequest ${attribute}='${value.toString()}'")

    }

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

    private fun update(obj: ModelClass): ModelClass {
        this.writableDatabase.update(
            obj::class.simpleName,
            ModelClass.modelClassToContentValuesExceptID(obj),
            "id=${obj.id}",
            null
        )
        return obj
    }

    fun save(obj: ModelClass?): ModelClass? {
        if (obj == null)
            return null
        // Save all related foreign keys
        for (property in obj::class.memberProperties){
            when (property.returnType){
                typeOf<ForeignKey>() -> save((property.getter?.call(obj) as ForeignKey).getRelated(this))
            }
        }
        if (obj.id == null) {
            // that's an insert
            return insert(obj)
        }
        // that's an update
        return update(obj)
    }

    fun delete(obj: ModelClass) {
        this.writableDatabase.delete(obj::class.simpleName, "id=${obj.id}", null)
    }

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
            listOf(Player::class, Team::class, Race::class, Lap::class, Type::class)
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "fail.db"
        fun classFromAttributeName(name: String) =
            MODELS.find { it.simpleName == name.capitalize() }
    }
}

