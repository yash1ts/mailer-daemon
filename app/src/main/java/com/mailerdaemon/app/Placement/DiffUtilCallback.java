package com.mailerdaemon.app.Placement;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class DiffUtilCallback extends DiffUtil.Callback {
    private final List<DocumentSnapshot> mOldEmployeeList;
    private final List<DocumentSnapshot> mNewEmployeeList;

    public DiffUtilCallback(List<DocumentSnapshot> oldEmployeeList, List<DocumentSnapshot> newEmployeeList) {
        this.mOldEmployeeList = oldEmployeeList;
        this.mNewEmployeeList = newEmployeeList;
    }

    @Override
    public int getOldListSize() {
        return mOldEmployeeList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewEmployeeList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldEmployeeList.get(oldItemPosition).toObject(PlacementModel.class).getDate() == mNewEmployeeList.get(
                newItemPosition).toObject(PlacementModel.class).getDate();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final DocumentSnapshot oldEmployee = mOldEmployeeList.get(oldItemPosition);
        final DocumentSnapshot newEmployee = mNewEmployeeList.get(newItemPosition);

        return oldEmployee.toObject(PlacementModel.class).getDate().equals(newEmployee.toObject(PlacementModel.class).getDate());
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        // Implement method if you're going to use ItemAnimator
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
