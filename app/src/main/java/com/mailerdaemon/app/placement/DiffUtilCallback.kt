package com.mailerdaemon.app.placement

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback(
    private val oldList: List<PlacementModel>,
    private val newList: List<PlacementModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].message == newList[newItemPosition].message
}
