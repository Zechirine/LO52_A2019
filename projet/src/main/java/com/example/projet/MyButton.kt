package com.example.projet

import io.flic.lib.FlicButton

class MyButton {
    val flicButton: FlicButton
    val roomId: Int

    constructor(flicButton: FlicButton, roomId: Int) {
        this.flicButton = flicButton
        this.roomId = roomId
    }

}