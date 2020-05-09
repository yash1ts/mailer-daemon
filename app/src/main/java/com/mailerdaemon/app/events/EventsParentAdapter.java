package com.mailerdaemon.app.events;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.DialogOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventsParentAdapter extends RecyclerView.Adapter<EventsParentAdapter.Holder> {

  private List<DocumentSnapshot> documentReferences=new ArrayList<>();
  private RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
  private DialogOptions options;
  private Context context;
  //private EventsChildAdapter adapter;
  private Boolean access;

  EventsParentAdapter(DialogOptions options, Boolean access, Context context) {

    this.options=options;
    this.context=context;
    this.access=access;
  }

  @NonNull
  @Override
  public EventsParentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_events,viewGroup,false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull EventsParentAdapter.Holder holder, int i) {
    EventModel eventModel=documentReferences.get(i).toObject(EventModel.class);
    DateFormat dateFormat=new SimpleDateFormat("dd/MM", Locale.ENGLISH);
    holder.name.setText(eventModel.getName());
    holder.date.setText(dateFormat.format(eventModel.getDate()));
    RecyclerView recyclerView=holder.recyclerView;
    holder.recyclerView.setRecycledViewPool(viewPool);
    String path=documentReferences.get(i).getReference().getPath();
    if(access){
      holder.deleteEvent.setVisibility(View.VISIBLE);
      holder.addPost.setVisibility(View.VISIBLE);
      holder.addPost.setOnClickListener(v -> options.showDialog(path));
      holder.deleteEvent.setOnClickListener(v ->{
        AlertDialog dialog=new AlertDialog.Builder(context).create();
        dialog.setTitle("Delete Event ?");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", (dialog1, which) -> {
          FirebaseFirestore.getInstance().document(path).delete();
          documentReferences.remove(i);
          notifyItemRemoved(i);
        });
        dialog.show();
      });
    }
    EventsChildAdapter adapter=new EventsChildAdapter(options,context);
    adapter.setData(eventModel.posts,path);
    recyclerView.setAdapter(adapter);
  }

  @Override
  public int getItemCount() {
    return documentReferences.size();
  }

  public void setData(List<DocumentSnapshot> snap) {
    this.documentReferences=snap;
  }

  public static class Holder extends RecyclerView.ViewHolder {
    TextView name;
    TextView date;
    RecyclerView recyclerView;

    View addPost;
    View deleteEvent;

    public Holder(@NonNull View itemView) {
      super(itemView);
      name=itemView.findViewById(R.id.name);
      date=itemView.findViewById(R.id.date_day);
      deleteEvent=itemView.findViewById(R.id.remove_event);
      recyclerView=itemView.findViewById(R.id.rv_event_child);
      addPost=itemView.findViewById(R.id.add_event_post);
      recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(),RecyclerView.VERTICAL,false));
    }
  }
}
