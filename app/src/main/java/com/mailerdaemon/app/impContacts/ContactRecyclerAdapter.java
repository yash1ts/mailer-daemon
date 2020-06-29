package com.mailerdaemon.app.impContacts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.facebook.drawee.view.SimpleDraweeView;
import com.mailerdaemon.app.R;
import com.mailerdaemon.app.utils.ContactFunction;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactRecyclerAdapter extends RecyclerView.Adapter<ContactRecyclerAdapter.Holder> {
    ContactFunction fun;
    private List<Contact> contact =new ArrayList();
    public ContactRecyclerAdapter( ContactFunction function) {
    this.fun=function;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate( R.layout.rv_contact_detail,viewGroup,false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int i) {
        Contact c=contact.get(i);
        holder.name.setText(c.getName());
        holder.draweeView.setImageURI(c.getImage());
        holder.phone.setText(c.getPhone());
        holder.dept.setText(c.getDept());
        holder.email.setText(c.getEmail());
        holder.call.setOnClickListener(v->fun.makeCall(c.getPhone()));
        holder.send_mail.setOnClickListener(v->fun.sendMail(c.getEmail()));
    }

    @Override
    public int getItemCount() {
        return contact.size();
    }

    List<Contact> getData(){
        return contact;
    }

    void setData(List<Contact> contact){
        this.contact=contact;
    }

    public class Holder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.contact_call)
        ImageView call;
        @BindView(R.id.contact_send_mail)
        ImageView send_mail;

        public Holder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            draweeView.getHierarchy().setProgressBarImage(new CircularProgressDrawable(name.getContext()));
        }
    }
}