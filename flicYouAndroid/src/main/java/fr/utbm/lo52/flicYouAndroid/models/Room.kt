package fr.utbm.lo52.flicYouAndroid.models

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "Room")
data class Room (

    @DatabaseField
    var number:Int = -1,

    @DatabaseField
    var nameSufferer:String = "suffUser",

    @DatabaseField
    var macButton:String = "macButton",

    @DatabaseField(
        canBeNull = false,
        foreign = true,
        foreignColumnName = "idZone",
        foreignAutoCreate = true,
        columnDefinition = "INTEGER CONSTRAINT FK_ZONE REFERENCES Zone(idZone) ON DELETE CASCADE")
    private var zone:Zone = Zone(-1),

    @DatabaseField(
        canBeNull = false,
        foreign = true,
        foreignColumnName = "idCaregiver",
        foreignAutoCreate = true,
        columnDefinition = "INTEGER CONSTRAINT FK_CAREGIVER REFERENCES Caregiver(idCaregiver) ON DELETE CASCADE")
    private var caregiver: Caregiver = Caregiver(-1, "user")

){
    @DatabaseField(generatedId = true)
    private val idRoom:Int? = null
}

class RoomDao{
    companion object{
        lateinit var  dao : Dao<Room, Int>
    }
    init {
        dao = DatabaseManager.getDao(Room::class.java)
    }

    //function to call in front-end
    fun add(room : Room) = dao.createOrUpdate(room)

    fun update(room : Room) = dao.update(room)

    fun delete(room : Room) = dao.delete(room)

    fun queryForAll() = dao.queryForAll()

    fun removeAll() = {
        for(room in queryForAll()){
            dao.delete(room)
        }
    }
}