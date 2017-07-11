package com.studiokai.kaibeta.newsfeed;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.studiokai.kaibeta.R;

import java.util.List;

/**
 * Created by titusjuocepis on 6/24/17.
 */

class AdapterFBPosts extends RecyclerView.Adapter<AdapterFBPosts.ViewHolder> {

    private List<ModelFBPost> fbPosts;

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
            else if (post.images.size() == 2) {
                holder.img1.setImageBitmap(post.images.get(0));
                holder.img2.setImageBitmap(post.images.get(1));
            }
            else if (post.images.size() == 3) {
                holder.img1.setImageBitmap(post.images.get(0));
                holder.img2.setImageBitmap(post.images.get(1));
                holder.img3.setImageBitmap(post.images.get(2));
            }
            else {
                holder.img1.setImageBitmap(post.images.get(0));
                holder.img2.setImageBitmap(post.images.get(1));
                holder.img3.setImageBitmap(post.images.get(2));
                holder.img4.setImageBitmap(post.images.get(3));
            }
        }

    }

    @Override
    public int getItemCount() {
        return fbPosts.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView story, time, msg;
        ImageView img1, img2, img3, img4;

        ViewHolder(View itemView) {

            super(itemView);

            story = (TextView) itemView.findViewById(R.id.fb_post_story);
            time = (TextView) itemView.findViewById(R.id.fb_post_time);
            msg = (TextView) itemView.findViewById(R.id.fb_post_msg);
            img1 = (ImageView) itemView.findViewById(R.id.fb_post_img1);
            img2 = (ImageView) itemView.findViewById(R.id.fb_post_img2);
            img3 = (ImageView) itemView.findViewById(R.id.fb_post_img3);
            img4 = (ImageView) itemView.findViewById(R.id.fb_post_img4);
        }
    }
}