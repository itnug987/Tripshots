package com.tripshots.Adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tripshots.ActivityBlogSubPage;
import com.tripshots.ActivityPostSubPage;
import com.tripshots.R;
import com.tripshots.model.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    Context mContext;
    List<Post> mData;

    public PostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.item_post, parent, false);
        return new PostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.blog_post_title.setText(mData.get(position).getTitle());

        String s = Html.fromHtml(mData.get(position).getDescription()).toString();
        s = s.substring(0, Math.min(s.length(), 80));

        holder.blog_post_content.setText(s+"...");

        Glide.with(mContext).load(mData.get(position).getImage_url()).into(holder.blog_post_image);

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-HH:mm",
                Locale.ENGLISH);
        Date d = new Date();
        String s1 = d.toString();

        try
        {
            Date d1 = sdf.parse(s1);
            String s2 = (new SimpleDateFormat("dd MMMM YYYY")).format(d1);

            holder.blog_post_date.setText(s2);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        holder.btn_read_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = position;
                Intent i = new Intent(mContext, ActivityPostSubPage.class);
                i.putExtra("id",id);
                mContext.startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView blog_post_image;
        TextView blog_post_title, blog_post_content, blog_post_date;
        Button btn_read_more;

        public ViewHolder(View itemView){
            super(itemView);

            blog_post_image = itemView.findViewById(R.id.blog_post_image);

            blog_post_title = itemView.findViewById(R.id.blog_post_title);
            blog_post_content = itemView.findViewById(R.id.blog_post_content);
            blog_post_date = itemView.findViewById(R.id.blog_post_date);

            btn_read_more = itemView.findViewById(R.id.btn_read_more);
        }
    }

}
