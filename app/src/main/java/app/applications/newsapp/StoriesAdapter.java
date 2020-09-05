/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.applications.newsapp.customviews.RoundImageView;

public class StoriesAdapter extends RecyclerView.Adapter<StoriesAdapter.StoryViewHolder> {

    ArrayList<String> images;

    public StoriesAdapter(ArrayList<String> images) {
        this.images = images;
    }

    @NonNull
    @Override
    public StoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v =  LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_item, parent, false);

        StoryViewHolder vh = new StoryViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull StoryViewHolder holder, int position) {
        Picasso.get().load(images.get(position)).into(holder.storyImage);
    }

    @Override
    public int getItemCount() {
        if(images!=null)
            return images.size();
        return 0;
    }

    public class StoryViewHolder extends RecyclerView.ViewHolder {
        RoundImageView storyImage;
        public StoryViewHolder(@NonNull View itemView) {
            super(itemView);
            this.storyImage = itemView.findViewById(R.id.storyImage);
        }
    }
}
