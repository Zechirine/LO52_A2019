package utbm.lo52.fail.constants

import android.content.Context
import utbm.lo52.fail.R

const val LAP_PER_RACE = 3
const val MAX_ORDERING = 3
const val MIN_ORDERING = 1

enum class LapType(val id: Int) {
    SPRINT(R.string.lap_type_sprint),
    SPLIT_UP(R.string.lap_type_split_up),
    PIT_STOP(R.string.lap_type_pit_stop);

    fun getString(context: Context) = context.resources.getString(id)!!

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

