package com.mailerdaemon.app.attendance;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.mailerdaemon.app.ApplicationClass;
import com.mailerdaemon.app.ConstantsKt;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.attendance.database.AppDatabase;
import com.mailerdaemon.app.attendance.database.Subject;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class AttendanceFragment extends Fragment implements UpdateDatabse {

  private static AppDatabase database;
  @BindView(R.id.att_add)
  ImageView add;
  @BindView(R.id.card)
  CardView card;
  @BindView(R.id.att_lay_notification)
  LinearLayout bt_notification;
  @BindView(R.id.att_lay_req)
  LinearLayout bt_required;
  @BindView(R.id.shimmer_view_container)
  ShimmerFrameLayout shimmerFrameLayout;
  @BindView(R.id.att_req)
          TextView required;
  @SuppressLint("UseSwitchCompatOrMaterialCode")
  @BindView(R.id.noti_switch)
  Switch notificationSwitch;
  @BindView(R.id.att_notification_time)
          TextView noti_time;
  @BindView(R.id.att_date)
          TextView tv__date;
  @BindView(R.id.att_month)
          TextView tv_month;
  @BindView(R.id.att_year)
          TextView tv_year;
  private RecyclerView recyclerView;
  private AttendanceAdapter adapter;
  private NestedScrollView scrollView;
  private List<Subject> list=new ArrayList<>();
  private int requiredAtt=75;
  private long time;
  private Context context;
  private Calendar notiTime;

  @SuppressLint("DefaultLocale")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    ApplicationClass applicationClass= (ApplicationClass) requireActivity().getApplication();
    database=applicationClass.database;
    View view=inflater.inflate(R.layout.fragment_attandance,container,false);
    ButterKnife.bind(this,view);
    context=getContext();
    recyclerView=view.findViewById(R.id.rv_attendance);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    adapter=new AttendanceAdapter(this,getContext());
    recyclerView.setAdapter(adapter);
    time= getActivity().getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getLong(ConstantsKt.TIME_NOTI,0);
    Calendar current_date=Calendar.getInstance();
    tv__date.setText(String.format("%d", current_date.get(Calendar.DAY_OF_MONTH)));
    DateFormatSymbols dfs = new DateFormatSymbols();
    String[] months = dfs.getMonths();
    tv_month.setText(months[current_date.get(Calendar.MONTH)]);
    tv_year.setText(String.format("%d", current_date.get(Calendar.YEAR)));
    notiTime= Calendar.getInstance();
    notiTime.setTimeInMillis(time);
    updateTimeTv();
    boolean x= getContext().getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getBoolean("Notification",false);
    notificationSwitch.setChecked(x);

    notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if(isChecked){
        cancelNotification();
        setUpNotification();
        updateTimeTv();
      }else {
        cancelNotification();
      }
    });

    scrollView=getActivity().findViewById(R.id.fragment_container);
    shimmerFrameLayout.setVisibility(View.VISIBLE);
    scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
      int scrollY = scrollView.getScrollY(); // For ScrollView
      if(scrollY<150) {
        if (card.getVisibility()==View.INVISIBLE)
          card.setVisibility(View.VISIBLE);
      }else{
        if(card.getVisibility()==View.VISIBLE)
      scrollView.smoothScrollBy(0,100);
        card.setVisibility(View.INVISIBLE);}
    });

    new DataBase().execute();

    adapter.setData(list,requiredAtt);
    adapter.notifyDataSetChanged();
    add.setOnClickListener(v-> showDialog());
    bt_required.setOnClickListener(v-> setRequiredAtt());
    bt_notification.setOnClickListener(v -> setupNotificationTime());

    return view;
  }

  @Override
  public void delete(Subject subject) {
    new Delete().execute(subject);
    adapter.notifyDataSetChanged();
  }

  @Override
  public void update(Subject subject) {
  new Update().execute(subject);
    adapter.notifyDataSetChanged();

  }

  @SuppressLint("DefaultLocale")
  private void setRequiredAtt(){
    AlertDialog dialog= new AlertDialog.Builder(getContext()).create();
    final View view = getLayoutInflater().inflate(R.layout.dialog_change_attendance, new LinearLayout(getContext()));
    dialog.setView(view);
    dialog.setMessage("Required Attendance");
    SeekBar seekBar= view.findViewById(R.id.seekBar);
    final int[] x = new int[1];
    TextView attendance= view.findViewById(R.id.attendance);
    seekBar.setProgress(requiredAtt);
    x[0]=requiredAtt;
    attendance.setText(String.format("%d%%", requiredAtt));
    seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        attendance.setText(String.format("%d%%", progress));
        x[0] = progress;
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    view.findViewById(R.id.bt_ok).setOnClickListener(v->{
      getContext().getSharedPreferences("GENERAL", Context.MODE_PRIVATE).edit().putInt("attendance",x[0]).apply();
      new DataBase().execute();
      dialog.dismiss();
    });
    view.findViewById(R.id.bt_cancel).setOnClickListener(v->{
      dialog.dismiss();
    });
    dialog.show();
  }

  private void showDialog(){
    AlertDialog dialog= new AlertDialog.Builder(getContext()).create();
    View view=getLayoutInflater().inflate(R.layout.dialog_add_subject,new LinearLayout(getContext()));
    dialog.setView(view);
    dialog.setMessage("Add Subject");
    view.findViewById(R.id.bt_ok).setOnClickListener(v->{
      EditText name=view.findViewById(R.id.sub_name);
      String sub=name.getText().toString();
      if(list.size()==0){
        notificationSwitch.setChecked(true);
      }
      new InsertDatabse().execute(sub);
      new DataBase().execute();
      dialog.dismiss();
    });
    view.findViewById(R.id.bt_cancel).setOnClickListener(v->{
      dialog.dismiss();
    });
    dialog.show();
  }

  private void setUpNotification(){
    Calendar calendar=notiTime;
    getContext().getSharedPreferences("GENERAL", Context.MODE_PRIVATE).edit().putBoolean("Notification",true).putLong(ConstantsKt.TIME_NOTI,calendar.getTimeInMillis()).apply();
    time=notiTime.getTimeInMillis();
    AlarmManager alarmMgr = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getContext(), NotificationReceiver.class);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 123, intent,PendingIntent.FLAG_UPDATE_CURRENT );
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    String val1  = sdf.format(new Date(System.currentTimeMillis()));
    String val2  = sdf.format(calendar.getTime());

    Date d1 = null;
    Date d2 = null;

    try {
      d1 = sdf.parse(val1);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    try {
      d2 = sdf.parse(val2);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    assert d1 != null;
    assert d2 != null;
    long elapsed = d2.getTime() - d1.getTime();

    // Determine if the time specified is past already or not. If it is past add 24 hours so it displays the following day.
    if (elapsed < 0)
    {
      alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1) + elapsed, AlarmManager.INTERVAL_DAY,alarmIntent);
    }
    else
    {
      alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + elapsed, AlarmManager.INTERVAL_DAY, alarmIntent);
    }



    ComponentName receiver = new ComponentName(context, OnBootReciever.class);
    PackageManager pm = context.getPackageManager();

    pm.setComponentEnabledSetting(receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP);

  }

  private void cancelNotification(){
    getContext().getSharedPreferences("GENERAL", Context.MODE_PRIVATE).edit().putBoolean("Notification",false).apply();
    AlarmManager alarmMgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
    Intent intent = new Intent(getContext(), NotificationReceiver.class);
    PendingIntent alarmIntent = PendingIntent.getBroadcast(getContext(), 123, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    if (alarmMgr!= null) {
      ComponentName receiver = new ComponentName(context, OnBootReciever.class);
      PackageManager pm = context.getPackageManager();
      pm.setComponentEnabledSetting(receiver,
              PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
              PackageManager.DONT_KILL_APP);
      alarmMgr.cancel(alarmIntent);
    }
  }

  private void setupNotificationTime(){
    Calendar mcurrentTime = Calendar.getInstance();
    mcurrentTime.setTimeInMillis(time);

    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
    int minute = mcurrentTime.get(Calendar.MINUTE);
    TimePickerDialog mTimePicker;
    mTimePicker = new TimePickerDialog(getContext(), (timePicker, selectedHour, selectedMinute) -> {
      notiTime.setTimeInMillis(System.currentTimeMillis());
      notiTime.set(Calendar.HOUR_OF_DAY,selectedHour);
      notiTime.set(Calendar.MINUTE,selectedMinute);
      notificationSwitch.setChecked(false);
      notificationSwitch.setChecked(true);
    }, hour, minute, false);
    mTimePicker.updateTime(hour,minute);
    mTimePicker.setTitle("Select Notification Time");
    mTimePicker.show();
    }

    @SuppressLint("DefaultLocale")
    private void updateTimeTv() {
      int hour = notiTime.get(Calendar.HOUR);
      int minute = notiTime.get(Calendar.MINUTE);
      if (hour == 0)
        hour = 12;
      if (minute < 10) {
        if (notiTime.get(Calendar.AM_PM) == Calendar.AM)
          noti_time.setText(String.format("%d:0%d AM", hour, minute));
        else noti_time.setText(String.format("%d:0%d PM", hour, minute));
      } else {
        if (notiTime.get(Calendar.AM_PM) == Calendar.AM)
          noti_time.setText(String.format("%d:%d AM", hour, minute));
        else noti_time.setText(String.format("%d:%d PM", hour, minute));
      }
    }

  private static class InsertDatabse extends AsyncTask<String,Void,Void>{

    @Override
    protected Void doInBackground(String... strings) {
      Subject subject = new Subject();
      subject.name = strings[0];
      subject.present = 0;
      subject.total = 0;
      subject.date= Calendar.getInstance();
      database.subjectDao().insertAll(subject);
      return null;
    }

  }

  private static class Update extends AsyncTask<Subject,Void,Void>{

    @Override
    protected Void doInBackground(Subject... subjects) {

      database.subjectDao().update(subjects[0]);
      return null;
    }


  }

  private static class Delete extends AsyncTask<Subject,Void,Void>{

    @Override
    protected Void doInBackground(Subject... subjects) {

      database.subjectDao().delete(subjects[0]);

      return null;
    }
  }

  private class DataBase extends AsyncTask<Void,Void,Void>{

    @Override
    protected Void doInBackground(Void... voids) {

      list=database.subjectDao().getAll();

      return null;
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onPostExecute(Void aVoid) {
      super.onPostExecute(aVoid);
      if(context!=null)
      requiredAtt=context.getSharedPreferences("GENERAL", Context.MODE_PRIVATE).getInt("attendance",75);
      adapter.setData(list,requiredAtt);
      required.setText(String.format("%d%%", requiredAtt));
      adapter.notifyDataSetChanged();
      if(shimmerFrameLayout.getVisibility()==View.VISIBLE)
        shimmerFrameLayout.setVisibility(GONE);
    }
  }


}
