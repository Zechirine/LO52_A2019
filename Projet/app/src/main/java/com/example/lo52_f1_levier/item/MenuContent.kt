package com.example.lo52_f1_levier.item

import java.util.ArrayList
import java.util.HashMap

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 *
 * TODO: Replace all uses of this class before publishing your app.
 */
object MenuContent {

    /**
     * An array of sample (dummy) items.
     */
    val ITEMS: MutableList<MainTab> = ArrayList()

    /**
     * A map of sample (dummy) items, by ID.
     */
    val ITEM_MAP: MutableMap<String, MainTab> = HashMap()

    init {
        // Création des "onglets"
        addItem(MainTab("1", "Participants"))
        addItem(MainTab("2", "Equipes"))
        addItem(MainTab("3", "Courses"))
    }

    private fun addItem(item: MainTab) {
        ITEMS.add(item)
        ITEM_MAP.put(item.id, item)
    }

    /*private fun createDummyItem(position: Int): MainTab {
        return MainTab(position.toString(), "Item " + position, makeDetails(position))
    }*/

    /*private fun makeDetails(position: Int): String {
        val builder = StringBuilder()
        builder.append("Details about Item: ").append(position)
        for (i in 0..position - 1) {
            builder.append("\nMore details information here.")
        }
        return builder.toString()
    }*/

    /**
     * A dummy item representing a piece of content.
     */
    data class MainTab(val id: String, val content: String) {
        override fun toString(): String = content
    }
}
