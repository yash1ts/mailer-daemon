package com.mailerdaemon.app.Events;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mailerdaemon.app.R;

import java.util.ArrayList;
import java.util.List;

import Utils.StringRes;

public class EventsParentAdapter extends RecyclerView.Adapter<EventsParentAdapter.Holder> {

  private Context context;
  private List<DocumentSnapshot> documentReferences=new ArrayList<>();
  private RecyclerView.RecycledViewPool viewPool=new RecyclerView.RecycledViewPool();
  private List<List<DocumentSnapshot>> lists=new ArrayList<>();
  private EventsFragment fragment;

  public EventsParentAdapter(Context context, EventsFragment eventsFragment) {
    this.context=context;
    this.fragment=eventsFragment;
  }

  @NonNull
  @Override
  public EventsParentAdapter.Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
    View view= LayoutInflater.from(context).inflate(R.layout.rv_events,viewGroup,false);
    return new Holder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull EventsParentAdapter.Holder holder, int i) {
    EventModel eventModel=documentReferences.get(i).toObject(EventModel.class);
    holder.name.setText(eventModel.getName());
    holder.date.setText(eventModel.getDay()+"\n  "+eventModel.getDate());
    RecyclerView recyclerView=holder.recyclerView;
    holder.recyclerView.setRecycledViewPool(viewPool);
    String id=documentReferences.get(i).getId();
    holder.deleteEvent.setOnClickListener(v ->FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Event).document(id).delete());
    holder.addPost.setOnClickListener(v -> openDialog(id));
    if(lists.isEmpty()) {
      FirebaseFirestore.getInstance().collection(StringRes.FB_Collec_Event).document(id).collection("posts").get().addOnCompleteListener(task -> {
        if (task.isSuccessful()) {
          holder.adapter.setData(task.getResult().getDocuments());
          recyclerView.setAdapter(holder.adapter);
          lists.add(task.getResult().getDocuments());
        } else {
        }
      });
    }else{
      holder.adapter.setData(lists.get(i));
      recyclerView.setAdapter(holder.adapter);
    }

  }

  private void openDialog(String name) {
    Bundle bundle=new Bundle();
    bundle.putString("name",name);
    AddEventPostFragment dialog=new AddEventPostFragment();
    dialog.setArguments(bundle);
    dialog.show(fragment.getChildFragmentManager(),null);
  }

  @Override
  public int getItemCount() {
    return documentReferences.size();
  }

  public void setData(List<DocumentSnapshot> snap) {
    this.documentReferences=snap;
  }

  public class Holder extends RecyclerView.ViewHolder {
    TextView name;
    TextView date;
    RecyclerView recyclerView;
    EventsChildAdapter adapter=new EventsChildAdapter(context,fragment.getChildFragmentManager());
    View addPost;
    View deleteEvent;

    public Holder(@NonNull View itemView) {
      super(itemView);
      name=itemView.findViewById(R.id.name);
      date=itemView.findViewById(R.id.date_day);
      deleteEvent=itemView.findViewById(R.id.remove_event);
      recyclerView=itemView.findViewById(R.id.rv_event_child);
      addPost=itemView.findViewById(R.id.add_event_post);
      recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(),LinearLayoutManager.VERTICAL,false));
    }
  }
}
