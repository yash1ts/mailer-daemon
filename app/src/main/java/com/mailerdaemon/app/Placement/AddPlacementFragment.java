package com.mailerdaemon.app.Placement;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.util.Date;

import Utils.AccessDatabse;
import Utils.StringRes;
import Utils.ViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AddPlacementFragment extends DialogFragment implements ViewUtils.showProgressBar {
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R.id.bt_close)
    ImageButton close;
    private TextInputEditText detail;
    private ImageButton send;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_add_placement,container,false);
        ButterKnife.bind(this,view);
        detail=view.findViewById(R.id.detail);
        send=view.findViewById(R.id.send);

        send.setOnClickListener(v ->{
            changeProgressBar();
            setDatabase();

        });
        close.setOnClickListener(v->{
            dismiss();
        });


        return view;
    }

    private void setDatabase() {
        Date date=new Date();
        PlacementModel model=new PlacementModel();
        model.setData(detail.getText().toString());
        model.setDate(date);
        FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_PLACEMENT).document().set(model);
        changeProgressBar();
        dismiss();
    }

    @Override
    public void changeProgressBar() {
        if(progressBar.isShown())
        {
            progressBar.setVisibility(View.GONE);
        }
        else progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        AccessDatabse method=(AccessDatabse)getActivity();
        method.getDatabase();
    }
}
