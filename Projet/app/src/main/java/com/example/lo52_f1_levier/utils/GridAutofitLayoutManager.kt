package com.example.lo52_f1_levier.utils

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.TypedValue
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager


class GridAutofitLayoutManager : GridLayoutManager {
    private var columnWidth: Int = 0
    private var isColumnWidthChanged = true
    private var lastWidth: Int = 0
    private var lastHeight: Int = 0

    constructor(context: Context, columnWidth: Int) : super(context, 1) {
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    constructor(context: Context, columnWidth: Int, orientation: Int, reverseLayout: Boolean)
            : super(context, 1, orientation, reverseLayout) {
        setColumnWidth(checkedColumnWidth(context, columnWidth))
    }

    /**
     * Check the consistency of a given column width (meaning > 0)
     * and correct it if necessary
     */
    private fun checkedColumnWidth(context: Context, columnWidth: Int): Int {
        var width = columnWidth
        if (width <= 0) {
            /* Set default columnWidth value (48dp here). It is better to move this constant
            to static constant on top, but we need context to convert it to dp, so can't really
            do so. */
            width = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 48f,
                context.getResources().getDisplayMetrics()
            ).toInt()
        }
        return width
    }

    /**
     * Change the column width value
     */
    private fun setColumnWidth(newColumnWidth: Int) {
        if (newColumnWidth > 0 && newColumnWidth != columnWidth) {
            columnWidth = newColumnWidth
            isColumnWidthChanged = true
        }
    }

    /**
     * Setup the columns depending on the given width and hand over to the GridLayoutManager
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        val width = width
        val height = height
        if (columnWidth > 0 && width > 0 && height > 0 && (isColumnWidthChanged || lastWidth != width || lastHeight != height)) {
            val totalSpace: Int
            if (orientation == LinearLayoutManager.VERTICAL) {
                totalSpace = width - paddingRight - paddingLeft
            } else {
                totalSpace = height - paddingTop - paddingBottom
            }
            val spanCount = Math.max(1, totalSpace / columnWidth)
            setSpanCount(spanCount)
            isColumnWidthChanged = false
        }
        lastWidth = width
        lastHeight = height
        super.onLayoutChildren(recycler, state)
    }
}