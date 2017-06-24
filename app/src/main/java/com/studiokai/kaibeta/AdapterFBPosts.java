package com.studiokai.kaibeta;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

public class AdapterFBPosts extends RecyclerView.Adapter<AdapterFBPosts.ViewHolder> {

    List<ModelFBPost> fbPosts;

    AdapterFBPosts(List<ModelFBPost> posts) {
        fbPosts = posts;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newsfeed_list, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        ModelFBPost post = fbPosts.get(position);

        holder.story.setText(post.story);
        holder.msg.setText(post.message);

        if (!post.images.isEmpty()) {

            if (post.images.size() == 1)
                holder.img1.setImageBitmap(post.images.get(0));
            if (post.images.size() == 2) {
                holder.img1.setImageBitmap(post.images.get(0));
                holder.img2.setImageBitmap(post.images.get(1));
            }
            if (post.images.size() == 3) {
                holder.img1.setImageBitmap(post.images.get(0));
                holder.img2.setImageBitmap(post.images.get(1));
                holder.img3.setImageBitmap(post.images.get(2));
            }
        }

    }

    @Override
    public int getItemCount() {
        return fbPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView story, time, msg;
        ImageView img1, img2, img3;

        ViewHolder(View itemView) {

            super(itemView);

            story = (TextView) itemView.findViewById(R.id.fb_post_story);
            time = (TextView) itemView.findViewById(R.id.fb_post_time);
            msg = (TextView) itemView.findViewById(R.id.fb_post_msg);
            img1 = (ImageView) itemView.findViewById(R.id.fb_post_img1);
            img2 = (ImageView) itemView.findViewById(R.id.fb_post_img2);
            img3 = (ImageView) itemView.findViewById(R.id.fb_post_img3);
        }
    }
}
