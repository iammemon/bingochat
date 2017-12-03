package com.example.fawad.bingochat.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fawad.bingochat.R;
import com.example.fawad.bingochat.contact.ContactHolder;
import com.example.fawad.bingochat.message.Message;
import com.example.fawad.bingochat.message.MessageHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Fawad on 11/17/2017.
 */

public class ContactFragment extends Fragment {

    FirebaseUser CurrentUser;
    DividerItemDecoration mDividerItemDecoration;
    LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter mAdapter;
    private static Query mChatQuery;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView =(RecyclerView)inflater.inflate(R.layout.recycler_view,container,false);
        CurrentUser=FirebaseAuth.getInstance().getCurrentUser();
        mChatQuery=FirebaseDatabase.getInstance()
                .getReference()
                .child("contacts")
                .child(CurrentUser.getUid())
                .limitToLast(50);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=createAdapter();
        //horizontal line under each viewholder
        mDividerItemDecoration=new DividerItemDecoration(recyclerView.getContext(),mLayoutManager.getOrientation());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(mLayoutManager);
  //      recyclerView.addItemDecoration(mDividerItemDecoration);
        return recyclerView;
    }


    public FirebaseRecyclerAdapter createAdapter(){
        FirebaseRecyclerOptions<Contact> options=
                new FirebaseRecyclerOptions.Builder<Contact>()
                        .setLifecycleOwner(this)
                        .setQuery(mChatQuery,Contact.class)
                        .build();

        return new FirebaseRecyclerAdapter<Contact, ContactHolder>(options) {
            @Override
            public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.contact_item, parent, false);

                return new ContactHolder(view);
            }

            @Override
            protected void onBindViewHolder(ContactHolder holder, int position, Contact model) {
//                if(CurrentUser.getUid().equals(model.getUid())){
//                    Log.e("Contact holder","inside if ");
//                    holder.itemView.setVisibility(View.GONE);
//
//                    return;
//                }
//                Log.e("userUid",CurrentUser.getUid());
//                Log.e("contactuid",model.getUid());
//                Log.e("Contact holder","outside if ");
                holder.Bind(model);
            }

        };
    }
}
