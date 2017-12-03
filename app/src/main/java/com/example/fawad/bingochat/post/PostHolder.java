package com.example.fawad.bingochat.post;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.fawad.bingochat.R;

/**
 * Created by Fawad on 11/30/2017.
 */

public class PostHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView postImage;
    private TextView title;
    private TextView name;
    private ImageView avatar;
    private ProgressBar progress;
    public PostHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        postImage=itemView.findViewById(R.id.post_image);
        title=itemView.findViewById(R.id.post_title);
        name=itemView.findViewById(R.id.post_name);
        avatar=itemView.findViewById(R.id.post_avatar);
        progress=itemView.findViewById(R.id.post_progress);
    }

    public void Bind(Post post){
        //passing data on click
        itemView.setTag(post);

        title.setText(post.getTitle());
        name.setText(post.getName());
        //external library  "Glide" to load images from weburl
        Glide.with(itemView.getContext()).load(post.getAvatar()).into(avatar);
        Glide.with(itemView.getContext())
                .load(post.getPostImage())
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(postImage);
    }
    @Override
    public void onClick(View view) {
        Intent intent = new Intent(itemView.getContext(),PostActivity.class);
        Post post =(Post) itemView.getTag();
        intent.putExtra("NAME",post.getName());
        intent.putExtra("AVATAR",post.getAvatar());
        intent.putExtra("TITLE",post.getTitle());
        intent.putExtra("TEXT",post.getSummary());
        intent.putExtra("PIC",post.getPostImage());

        itemView.getContext().startActivity(intent);
    }
}
