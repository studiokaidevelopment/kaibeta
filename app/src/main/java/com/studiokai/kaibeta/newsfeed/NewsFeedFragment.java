package com.studiokai.kaibeta.newsfeed;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studiokai.kaibeta.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment implements NewsFeedListener {

    RecyclerView recyclerView;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_news_feed, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.fb_post_list);

        KaiFacebook kaiFacebook = new KaiFacebook(this);
        kaiFacebook.loadPosts();

        return v;
    }

    @Override
    public void onFBPostsLoaded(List<ModelFBPost> posts) {

        AdapterFBPosts adapter = new AdapterFBPosts(posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onFBPostImagesLoaded(List<ModelFBPost> posts) {

        AdapterFBPosts adapter = new AdapterFBPosts(posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }
}
