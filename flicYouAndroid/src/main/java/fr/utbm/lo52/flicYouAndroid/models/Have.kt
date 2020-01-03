package fr.utbm.lo52.flicYouAndroid.models

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "Have")
data class Have(
    @DatabaseField(
        canBeNull = false,
        foreign = true,
        foreignColumnName = "idRoom")
    private val room: Room = Room(-1,"suffUser", "macButton",zone = Zone(-1)),
    @DatabaseField(
        canBeNull = false,
        foreign = true,
        foreignColumnName = "idMessage")
    private val message: Message = Message("message")
){
    @DatabaseField(generatedId = true)
    private val idHave : Int? = null
}

class HaveDao{
    companion object{
        lateinit var  dao : Dao<Have, Int>
    }

    init {
        dao = DatabaseManager.getDao(Have::class.java)
    }

    //function to call in frontend
    fun add(have : Have) = dao.createOrUpdate(have)

    fun update(have : Have) =  dao.update(have)

    fun delete(have: Have) =  dao.delete(have)

    fun queryForAll() = dao.queryForAll()

    fun removeAll() = {
        for (have in queryForAll()){
            dao.delete(have)
        }
    }
}