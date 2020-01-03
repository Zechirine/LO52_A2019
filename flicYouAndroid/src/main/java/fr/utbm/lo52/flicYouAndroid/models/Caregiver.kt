package fr.utbm.lo52.flicYouAndroid.models

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "Caregiver")
data class Caregiver(  @DatabaseField
                       var number:Int = -1,

                       @DatabaseField
                       var name:String = "user") {

    @DatabaseField(generatedId = true)
    private val idCaregiver: Int? = null

    override fun toString(): String {
        return "caregiver => number : $number and name : $name"
    }
}

class CaregiverDao{
    companion object  {
        lateinit var dao : Dao<Caregiver, Int>
    }

    init {
        dao = DatabaseManager.getDao(Caregiver::class.java)
    }
    //function to call in front-end
    fun add(caregiver: Caregiver) = dao.createOrUpdate(caregiver)

    fun update(caregiver: Caregiver) = dao.update(caregiver)

    fun delete(caregiver: Caregiver) = dao.delete(caregiver)

    fun queryForAll() = dao.queryForAll()

    fun removeAll() = {
        for (caregiver in queryForAll()){
            dao.delete(caregiver)
        }
    }
}