package com.example.vokal.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.vokal.R;
import com.example.vokal.pojo.Card;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {
    public List<Card> myCardList;
    public Context context;
    String contact, msgBody;

    public ContactAdapter(Context context, List<Card> myCardList, String contact, String msgBody){
        this.context = context;
        this.myCardList = myCardList;
        this.contact = contact;
        this.msgBody = msgBody;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        Card card = myCardList.get(position);
        myViewHolder.tv_time_slot.setText(card.getTime_slot());
        myViewHolder.tv_contact.setText(card.getContact());
        myViewHolder.tv_msg_body.setText(card.getMsg_body());

        if(card.getContact().equals(contact) && card.getMsg_body().equals(msgBody)){
            myViewHolder.itemView.setBackgroundColor(Color.parseColor("#64b5f6"));
        }


    }

    @Override
    public int getItemCount() {
        return myCardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time_slot, tv_contact, tv_msg_body;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_time_slot = itemView.findViewById(R.id.tv_time_slot);
            tv_contact = itemView.findViewById(R.id.tv_contact);
            tv_msg_body = itemView.findViewById(R.id.tv_msg_body);
        }
    }
}
