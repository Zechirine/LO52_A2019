package utbm.lo52.fail.db

data class Player(override val id: Int? = null, var name: String, var ordering: Int, val team: ForeignKey) : ModelClass(id)

data class Team(override val id: Int? = null, var name: String, val race: ForeignKey) : ModelClass(id)

data class Race(override val id: Int? = null, var name: String, var lap: Int) : ModelClass(id)

data class Lap(override val id: Int? = null, var chrono: Int, val type: ForeignKey, val player: ForeignKey) : ModelClass(id)

data class Type(override val id: Int? = null, var label: String) : ModelClass(id)

