package com.mailerdaemon.app.placement

import androidx.recyclerview.widget.DiffUtil
import com.mailerdaemon.app.notices.PostModel

class DiffUtilCallback(
    private val oldList: List<PostModel>,
    private val newList: List<PostModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].message == newList[newItemPosition].message
}
