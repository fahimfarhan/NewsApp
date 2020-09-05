/**
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2020 Qazi Fahim Farhan
 */
package app.applications.newsapp.viewholders;

import android.app.Activity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import app.applications.newsapp.R;
import app.applications.newsapp.StoriesAdapter;

public class StoriesViewHolder extends RecyclerView.ViewHolder {
    private RecyclerView stories;
    private Activity activity;

    public StoriesViewHolder(@NonNull View itemView) {
        super(itemView);
        this.stories = itemView.findViewById(R.id.stories);
        this.activity = (Activity) itemView.getContext();
    }

    public void bind(ArrayList<String> images) {
        stories.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this.activity, RecyclerView.HORIZONTAL, false);
        stories.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        StoriesAdapter mAdapter = new StoriesAdapter(images);
        stories.setAdapter(mAdapter);
    }
}
