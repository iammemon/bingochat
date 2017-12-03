package com.example.fawad.bingochat.contact;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fawad.bingochat.R;
import com.example.fawad.bingochat.chat.ChatActivity;
import com.example.fawad.bingochat.contact.Contact;

/**
 * Created by Fawad on 11/17/2017.
 */

public class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private final ImageView mAvatar;
    private final TextView mName;
    private final TextView mEmail;

    public ContactHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        mAvatar=itemView.findViewById(R.id.contact_avatar);
        mName=itemView.findViewById(R.id.contact_name);
        mEmail=itemView.findViewById(R.id.email);
    }

    public void Bind(Contact contact){
            //passing data on click
            itemView.setTag(contact);

            mName.setText(contact.getName());
            mEmail.setText(contact.getEmail());
            Glide.with(itemView.getContext()).load(contact.getAvatar()).into(mAvatar);

    }

    @Override
    public void onClick(View view) {
        Toast.makeText(itemView.getContext(),"Contact Clicked",Toast.LENGTH_SHORT).show();
        Contact contact=(Contact) itemView.getTag();
        Intent intent=new Intent(itemView.getContext(), ChatActivity.class);
        intent.putExtra("AVATAR",contact.getAvatar());
        intent.putExtra("UID",contact.getUid());
        intent.putExtra("NAME",contact.getName());
        itemView.getContext().startActivity(intent);
//        Message message =(Message)itemView.getTag();
//        Intent intent =new Intent(itemView.getContext(), ChatActivity.class);
//        intent.putExtra("AVATAR", message.getAvatar());
//        intent.putExtra("UID", message.getUid());
//        itemView.getContext().startActivity(intent);
    }
}
