package com.mailerdaemon.app.attendance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.attendance.database.Subject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.Holder> {
  private List<Subject> list=new ArrayList<>();
  private UpdateDatabse databse;
  private int required=75,red,green;
  private Context context;

  AttendanceAdapter(UpdateDatabse updateDatabse, Context context){
    this.databse=updateDatabse;
    this.context=context;
  }

  @NonNull
  @Override
  public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_subject,parent,false);
    red=parent.getResources().getColor(R.color.red);
    green=parent.getResources().getColor(R.color.green);
    return new Holder(view);
  }

  @SuppressLint("DefaultLocale")
  @Override
  public void onBindViewHolder(@NonNull Holder holder, int position) {
    Subject subject=list.get(position);
    holder.name.setText(subject.name);
    holder.number.setText(String.format("%d/%d", subject.present, subject.total));
    int per;
    if(subject.total>0)
    per=subject.present*100/subject.total;
    else per=0;
    holder.percent.setText(String.format("%d%%", per));
    holder.progressBar.setProgress(per);
    int stat=0,stat2=1;
    if(required!=100)
    stat=(required*subject.total-100*subject.present)/(100-required);
    if(required!=0)
    stat2=(100*subject.present-required*subject.total)/required;
    if(subject.total>0){
    if(stat>0){
        holder.status.setText(String.format("Attend %d more classes to get on\ntrack.", stat));
      holder.status.setTextColor(red);
    }
    else if(stat==0||stat2==0){
    holder.status.setText("On track. You can't miss next class\n");
      holder.status.setTextColor(green);}
    else {
        holder.status.setText("On track.\n");
        holder.status.setTextColor(green);
    }}
    else holder.status.setText("--\n");

    holder.increase.setOnClickListener(v->{
      list.get(position).present++;
      list.get(position).total++;
      list.get(position).date=Calendar.getInstance();
      databse.update(list.get(position));
    });
    holder.decrease.setOnClickListener(v->{
      list.get(position).total++;
      list.get(position).date=Calendar.getInstance();
      databse.update(list.get(position));
    });
    holder.options.setOnClickListener(v-> showOptions(position));
    Calendar calendar=Calendar.getInstance();
    long diff= (calendar.getTimeInMillis()-subject.date.getTimeInMillis());
    long diffDays = diff / (24 * 60 * 60 * 1000);
    if(diffDays>30)
    holder.lastUpdated.setText("long ago");
    else {
      if(diffDays==0)
        holder.lastUpdated.setText("today");
      else if(diffDays==1)
      holder.lastUpdated.setText(String.format("%dday ago", diffDays));
      else holder.lastUpdated.setText(String.format("%ddays ago", diffDays));
    }
  }

  @Override
  public int getItemCount() {
    return list.size();
  }

  void setData(List<Subject> list,int required){
    this.list=list;
    this.required=required;
  }

  private void showOptions(int position){
    BottomSheetDialog bottomSheet= new BottomSheetDialog(context);
    bottomSheet.setContentView(R.layout.attendance_options);
    View bt_edit=bottomSheet.findViewById(R.id.option_edit);
    View bt_delete=bottomSheet.findViewById(R.id.option_delete);
    assert bt_delete != null;
    bt_delete.setOnClickListener(v->{
      databse.delete(list.get(position));
      list.remove(position);
      bottomSheet.dismiss();
    });
    assert bt_edit != null;
    bt_edit.setOnClickListener(v->{
      bottomSheet.dismiss();
      Dialog dialog= new Dialog(context);
      dialog.setContentView(R.layout.dialog_edit_attendance);
      dialog.setTitle("Add Subject");
      EditText present=dialog.findViewById(R.id.present);
      EditText total=dialog.findViewById(R.id.total);
      Button ok=dialog.findViewById(R.id.bt_ok);
      Button cancel=dialog.findViewById(R.id.bt_cancel);
      ok.setOnClickListener(v1->{
        String stotal=total.getText().toString().trim();
        String spresent=present.getText().toString().trim();
        if (!stotal.isEmpty()){
          if (!spresent.isEmpty())
          {
            list.get(position).total = Integer.parseInt(stotal);
            list.get(position).present = Integer.parseInt(spresent);
            databse.update(list.get(position));
          }
          else present.setError("Cannot be empty");
        }else total.setError("Cannot be empty");
        dialog.dismiss();
        });
      cancel.setOnClickListener(v1-> dialog.dismiss());
      dialog.show();

    });
    bottomSheet.show();
  }

  public static class Holder extends RecyclerView.ViewHolder {
    TextView name,status;
    TextView number,percent,lastUpdated;
    ProgressBar progressBar;
    FloatingActionButton increase,decrease;
    ImageView options;


    public Holder(@NonNull View itemView) {
      super(itemView);
      name=itemView.findViewById(R.id.subject_name);
      number=itemView.findViewById(R.id.sub_attendance_num);
      percent=itemView.findViewById(R.id.sub_percent);
      progressBar=itemView.findViewById(R.id.sub_progress);
      increase=itemView.findViewById(R.id.sub_increase);
      decrease=itemView.findViewById(R.id.sub_decrease);
      options=itemView.findViewById(R.id.sub_options);
      lastUpdated=itemView.findViewById(R.id.sub_last_update);
      status=itemView.findViewById(R.id.sub_status);
    }
  }

}
