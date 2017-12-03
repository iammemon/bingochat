package com.example.fawad.bingochat.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fawad.bingochat.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

/**
 * Created by Fawad on 11/17/2017.
 */

public class PostFragment extends Fragment {

    LinearLayoutManager mLayoutManager;
    FirebaseRecyclerAdapter mAdapter;
    private static Query mPostQuery;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView =(RecyclerView)inflater.inflate(R.layout.recycler_view,container,false);
        mPostQuery= FirebaseDatabase.getInstance().getReference("posts").limitToLast(50);
        mLayoutManager=new LinearLayoutManager(getActivity());
        mAdapter=createAdapter();
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);
        return  recyclerView;
    }

    public FirebaseRecyclerAdapter createAdapter(){
        FirebaseRecyclerOptions<Post> options=
                new FirebaseRecyclerOptions.Builder<Post>()
                        .setLifecycleOwner(this)
                        .setQuery(mPostQuery,Post.class)
                        .build();

        return new FirebaseRecyclerAdapter<Post, PostHolder>(options) {
            @Override
            public PostHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                // Create a new instance of the ViewHolder
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.post_item_row, parent, false);

                return new PostHolder(view);
            }

            @Override
            protected void onBindViewHolder(PostHolder holder, int position, Post model) {
                holder.Bind(model);
            }

        };
    }
}
