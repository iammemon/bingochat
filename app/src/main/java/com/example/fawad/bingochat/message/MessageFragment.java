package com.example.fawad.bingochat.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fawad.bingochat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Fawad on 11/17/2017.
 */

public class MessageFragment extends Fragment {

    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter mAdapter;
    private static  Query mChatQuery;
            //FirebaseDatabase.getInstance().getReference().child("messages").limitToLast(50);

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView =(RecyclerView)inflater.inflate(R.layout.recycler_view,container,false);
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid="";
        if(user!=null){
            uid=user.getUid();
        }
        mChatQuery=FirebaseDatabase.getInstance()
                .getReference("messages")
                .child(uid)
                .limitToLast(50);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=createAdapter();
        //horizontal line under each viewholder
        mDividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),mLayoutManager.getOrientation());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        return recyclerView;
    }


    public FirebaseRecyclerAdapter createAdapter(){
        FirebaseRecyclerOptions<Message> options=
                new FirebaseRecyclerOptions.Builder<Message>()
                    .setLifecycleOwner(this)
                    .setQuery(mChatQuery,Message.class)
                    .build();

        return new FirebaseRecyclerAdapter<Message, MessageHolder>(options) {
            @Override
            public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.message_item_row, parent, false);

                return new MessageHolder(view);
            }

            @Override
            protected void onBindViewHolder(MessageHolder holder, int position, Message model) {
                holder.Bind(model);
            }

        };
    }
}
