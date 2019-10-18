package fr.utbm.lo52.flicYouAndroid

import io.flic.lib.FlicButton

class MyButton(val roomNumber: Int, val flicButton: FlicButton) {
    fun getName(): String {
        return flicButton.name
    }
}