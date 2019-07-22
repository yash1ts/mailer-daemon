package com.mailerdaemon.app.Events;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import Utils.AccessDatabse;
import Utils.StringRes;
import Utils.ViewUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventFragment extends DialogFragment implements ViewUtils.showProgressBar {


  private EditText heading;
  private ImageButton send;
  private EditText date;
  EditText day;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.bt_close)
  ImageButton close;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view
        =inflater.inflate(R.layout.fragment_add_event,container,false);
    ButterKnife.bind(this,view);
    heading=view.findViewById(R.id.heading);
    date=view.findViewById(R.id.date);
    day=view.findViewById(R.id.day);
    send=view.findViewById(R.id.send);

    send.setOnClickListener(v -> {
      changeProgressBar();
      setDatabase();
    });
    close.setOnClickListener(v->dismiss());
    return view;
  }

  private void setDatabase() {

    EventModel model=new EventModel();
    model.setDate(date.getText().toString());
    model.setDay(day.getText().toString());
    model.setName(heading.getText().toString());
    FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Event).document().set(model);
    changeProgressBar();
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    AccessDatabse method=(AccessDatabse)getTargetFragment();
    method.getDatabase();
  }

  @Override
  public void changeProgressBar() {
    if(progressBar.isShown())
    {
      progressBar.setVisibility(View.GONE);
    }
    else progressBar.setVisibility(View.VISIBLE);
  }
}
