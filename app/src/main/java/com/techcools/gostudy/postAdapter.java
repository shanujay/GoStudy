package com.techcools.gostudy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class postAdapter extends RecyclerView.Adapter<postAdapter.postViewHolder>{

    private ArrayList<UserPosts> userPostsArrayList;
    private Context context;


    public postAdapter(Context context, ArrayList<UserPosts> userPostsArrayList) {
        this.userPostsArrayList = userPostsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public postViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_post, parent, false);
        return new postViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull postViewHolder holder, int position) {

        UserPosts userPosts = userPostsArrayList.get(position);

        Picasso.get().load(userPosts.getPostUrl()).into(holder.postCommunity);
        holder.userEmail.setText(userPosts.getUserEmail());

    }

    @Override
    public int getItemCount() {
        return userPostsArrayList.size();
    }

    public static class postViewHolder extends RecyclerView.ViewHolder {

        ImageView postCommunity;
        TextView userEmail;

        public postViewHolder(@NonNull View itemView) {
            super(itemView);

            postCommunity = itemView.findViewById(R.id.postCommunity);
            userEmail = itemView.findViewById(R.id.userName_community);
        }
    }

}
