package com.example.fawad.bingochat.message;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.fawad.bingochat.R;
import com.example.fawad.bingochat.chat.ChatActivity;

/**
 * Created by Fawad on 11/17/2017.
 */

public class MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mAvatar;
    private final TextView mName;
    private final TextView mLastMessage;

    public MessageHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mAvatar=itemView.findViewById(R.id.contact_avatar);
        mName=itemView.findViewById(R.id.msg_contact_name);
        mLastMessage=itemView.findViewById(R.id.msg_last_message);
    }

    public void Bind(Message message){
        //passing data on click
        itemView.setTag(message);

        mName.setText(message.getName());
        mLastMessage.setText(message.getLastMessage());
        Glide.with(itemView.getContext()).load(message.getAvatar()).into(mAvatar);

    }

    @Override
    public void onClick(View view) {
        Message message =(Message)itemView.getTag();
        Intent intent =new Intent(itemView.getContext(), ChatActivity.class);
        intent.putExtra("AVATAR", message.getAvatar());
        intent.putExtra("UID", message.getUid());
        intent.putExtra("NAME",message.getName());
        itemView.getContext().startActivity(intent);
    }
}
