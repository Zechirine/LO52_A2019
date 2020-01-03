package fr.utbm.lo52.flicYouAndroid.models

import com.j256.ormlite.dao.Dao
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable

@DatabaseTable(tableName = "Message")
data class Message(
    @DatabaseField
    var content:String = "message"
){
    @DatabaseField(generatedId = true)
    private val idMessage:Int? = null
}

class MessageDao{
    companion object{
        lateinit var dao : Dao<Message, Int>
    }
    init {
        dao = DatabaseManager.getDao(Message::class.java)
    }

    //function to call in front-end
    fun add(message: Message) = dao.createOrUpdate(message)

    fun update(message: Message) = dao.update(message)

    fun delete(message: Message) = dao.delete(message)

    fun queryforAll() = dao.queryForAll()

    fun  removeAll() = {
        for(message in queryforAll()){
            dao.delete(message)
        }
    }
}