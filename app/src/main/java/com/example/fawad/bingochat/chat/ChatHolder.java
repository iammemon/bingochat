package com.example.fawad.bingochat.chat;

import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RotateDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fawad.bingochat.R;
import com.example.fawad.bingochat.message.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

/**
 * Created by Fawad on 11/17/2017.
 */

public class ChatHolder extends RecyclerView.ViewHolder {


    private final TextView mName;
    private final TextView mDate;
    private final FrameLayout mLeftArrow;
    private final FrameLayout mRightArrow;
    private final RelativeLayout mMessageContainer;
    private final LinearLayout mMessage;
    private final int mGreen300;
    private final int mGray300;

    public ChatHolder(View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.message_text);
        mDate = itemView.findViewById(R.id.date_text);
        mLeftArrow = itemView.findViewById(R.id.left_arrow);
        mRightArrow = itemView.findViewById(R.id.right_arrow);
        mMessageContainer = itemView.findViewById(R.id.message_container);
        mMessage = itemView.findViewById(R.id.message);
        mGreen300 = ContextCompat.getColor(itemView.getContext(), R.color.material_green_300);
        mGray300 = ContextCompat.getColor(itemView.getContext(), R.color.material_gray_300);
    }

    public void Bind(Chat chat) {

        Log.i("Messages",chat.getMessage());
        mName.setText(chat.getMessage());
        long timestamp=Long.parseLong(chat.getDate());
        String date=new Date(timestamp).toLocaleString();
        mDate.setText(date);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setIsSender(currentUser != null && chat.getUid().equals(currentUser.getUid()));

//        if(!(currentUser != null && chat.getUid().equals(currentUser.getUid()))){
//            Message msg=new Message(chat.getName(),chat.getMessage(),chat.getUid(),chat.getAvatar());
//            FirebaseDatabase.getInstance()
//                    .getReference("messages")
//                    .child(chat.getUid())
//                    .setValue(msg);
//        }

    }

    private void setIsSender(boolean isSender) {
        final int color;
        if (isSender) {
            color = mGreen300;
            mLeftArrow.setVisibility(View.GONE);
            mRightArrow.setVisibility(View.VISIBLE);
            mMessageContainer.setGravity(Gravity.END);
        } else {
            color = mGray300;
            mLeftArrow.setVisibility(View.VISIBLE);
            mRightArrow.setVisibility(View.GONE);
            mMessageContainer.setGravity(Gravity.START);
        }

        ((GradientDrawable) mMessage.getBackground()).setColor(color);
        ((RotateDrawable) mLeftArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
        ((RotateDrawable) mRightArrow.getBackground()).getDrawable()
                .setColorFilter(color, PorterDuff.Mode.SRC);
    }




}