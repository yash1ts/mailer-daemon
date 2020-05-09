package com.mailerdaemon.app.events;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.AccessDatabase;
import com.mailerdaemon.app.utils.ViewUtils;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEventFragment extends DialogFragment implements ViewUtils.showProgressBar {


  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  @BindView(R.id.bt_close)
  ImageButton close;
  @BindView(R.id.datePicker)
  DatePicker datePicker;
  private EditText heading;
  private ImageButton send;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view
        =inflater.inflate(R.layout.fragment_add_event,container,false);
    ButterKnife.bind(this,view);
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(System.currentTimeMillis());
    datePicker.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
    heading=view.findViewById(R.id.heading);
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
    Calendar calendar = Calendar.getInstance();
    calendar.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth());
    Date date= new Date(calendar.getTimeInMillis());
    model.setDate(date);
    model.setName(heading.getText().toString());
    FirebaseFirestore.getInstance().collection(ConstantsKt.FB_EVENT).document().set(model);
    changeProgressBar();
    dismiss();
  }

  @Override
  public void onDismiss(@NonNull DialogInterface dialog) {
    super.onDismiss(dialog);
    AccessDatabase method=(AccessDatabase)getActivity();
    assert method != null;
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
