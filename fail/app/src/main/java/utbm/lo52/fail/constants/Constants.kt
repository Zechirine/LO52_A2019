package utbm.lo52.fail.constants

import android.content.Context
import utbm.lo52.fail.R

const val MAX_TEAM_PER_RACE = 12
const val LAP_PER_RACE = 3
const val MAX_ORDERING = 3
const val MIN_ORDERING = 1

enum class LapType(val id: Int, private val resId: Int) {
    SPRINT(1, R.string.lap_type_sprint),
    SPLIT_UP(2, R.string.lap_type_split_up),
    PIT_STOP(3, R.string.lap_type_pit_stop);

    fun getString(context: Context) = context.resources.getString(resId)!!

    companion object {
        val LAP_TYPE_ORDER =
            listOf(
                SPRINT,
                SPLIT_UP,
                PIT_STOP,
                SPRINT,
                SPLIT_UP
            )
    }
}

