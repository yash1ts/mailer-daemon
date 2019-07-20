package com.mailerdaemon.app.Events;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.util.ArrayList;
import java.util.List;

public class EventsParentAdapter extends RecyclerView.Adapter<EventsParentAdapter.Holder> {

  private List<DocumentSnapshot> documentReferences=new ArrayList<>();
  private RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
  private List<List<DocumentSnapshot>> lists=new ArrayList<>();
  private static FragmentManager childFM;
  private Boolean access;

  public EventsParentAdapter(FragmentManager childFM, Boolean access) {
    this.childFM=childFM;
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
    holder.name.setText(eventModel.getName());
    holder.date.setText(eventModel.getDay()+"\n"+eventModel.getDate());
    RecyclerView recyclerView=holder.recyclerView;
    holder.recyclerView.setRecycledViewPool(viewPool);
    String id=documentReferences.get(i).getReference().getPath();
    if(access){
      holder.deleteEvent.setVisibility(View.VISIBLE);
      holder.addPost.setVisibility(View.VISIBLE);
      holder.addPost.setOnClickListener(v -> openDialog(id));
      holder.deleteEvent.setOnClickListener(v ->FirebaseFirestore.getInstance().document(id).delete());
    }
    holder.adapter.setData(eventModel.posts,id);
    recyclerView.setAdapter(holder.adapter);
  }

  private void openDialog(String id) {
    Bundle bundle=new Bundle();
    bundle.putString("id",id);
    AddEventPostFragment dialog=new AddEventPostFragment();
    dialog.setArguments(bundle);
    dialog.show(childFM,null);
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
    EventsChildAdapter adapter=new EventsChildAdapter(childFM);
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
