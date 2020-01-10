package fr.utbm.lo52.flicYouAndroid

import kotlin.random.Random
import kotlin.random.nextULong

class MyTask(_name: String, _state: Int) {
    private var id = Random.nextULong()
    private var name = _name
    private var state = _state

    fun getId(): ULong{
        return id
    }

    fun getName(): String {
        return name
    }

    fun getState(): Int {
        return state
    }

    fun setState(_state: Int) {
        state = _state
    }
}

