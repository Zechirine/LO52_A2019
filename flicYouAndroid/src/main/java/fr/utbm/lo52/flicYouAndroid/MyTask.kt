package fr.utbm.lo52.flicYouAndroid

import kotlin.random.Random

class MyTask(_name: String, _state: Int) {
    private var id = Random.nextLong()
    private var name = _name
    private var state = _state

    fun getId():Long{
        return id
    }

    fun getName(): String {
        return name
    }

    fun getState(): Int {
        return state
    }
}

