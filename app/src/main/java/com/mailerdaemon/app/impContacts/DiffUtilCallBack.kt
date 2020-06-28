package com.mailerdaemon.app.impContacts

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallBack(private val mOldEmployeeList: List<Contact>, private val mNewEmployeeList: List<Contact>) : DiffUtil.Callback() {

    override fun getOldListSize() = mOldEmployeeList.size

    override fun getNewListSize() = mNewEmployeeList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int)
            = (mOldEmployeeList[oldItemPosition].name == mNewEmployeeList[newItemPosition].name)

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (_, _, _, name) = mOldEmployeeList[oldItemPosition]
        val (_, _, _, name1) = mNewEmployeeList[newItemPosition]
        return name == name1
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int) = super.getChangePayload(oldItemPosition, newItemPosition)
}
