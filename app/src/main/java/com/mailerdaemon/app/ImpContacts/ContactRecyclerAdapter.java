package com.mailerdaemon.app.ImpContacts;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mailerdaemon.app.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.Holder> {
    private List<Contact> contact =new ArrayList();
    public ContactRecyclerAdapter() {

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.rv_contact_detail,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        holder.name.setText(contact.get(i).getName());
        holder.draweeView.setImageURI(contact.get(i).getImage());
        holder.phone.setText(contact.get(i).getPhone());
        holder.dept.setText(contact.get(i).getDept());
        holder.email.setText(contact.get(i).getEmail());
    }

    @Override
    public int getItemCount() {
        return contact.size();
    }

    public static class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.contact_name)
        TextView name;
        @BindView(R.id.contact_img)
        SimpleDraweeView draweeView;
        @BindView(R.id.contact_dept)
        TextView dept;
        @BindView(R.id.contact_phone)
        TextView phone;
        @BindView(R.id.contact_email)
        TextView email;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            draweeView.getHierarchy().setProgressBarImage(new CircularProgressDrawable(name.getContext()));
        }
    }

    void setData(List<Contact> contact){
        this.contact=contact;
    }
    List<Contact> getData(){
        return contact;
    }
}
