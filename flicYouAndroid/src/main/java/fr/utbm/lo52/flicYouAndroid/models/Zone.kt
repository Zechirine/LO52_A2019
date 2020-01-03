package fr.utbm.lo52.flicYouAndroid.models

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "Zone")
data class Zone ( @DatabaseField var number: Int = -1){

    @DatabaseField(generatedId = true)
    var idZone: Int? = null

    override fun toString(): String {
        return "Zone : $number and Id : $idZone"
    }
}

class ZoneDao{
    companion object{
        lateinit var  dao : Dao<Zone, Int>
    }

    init {
        dao =  DatabaseManager.getDao(Zone::class.java)

    }

    //function to call in front-end
    fun add(zone: Zone) = dao.createOrUpdate(zone)

    fun update(zone: Zone) = dao.update(zone)

    fun delete(zone: Zone) = dao.delete(zone)

    fun queryForAll() = dao.queryForAll()

    fun removeAll() = {
        for (zone in queryForAll()){
            dao.delete(zone)
        }
    }
}
