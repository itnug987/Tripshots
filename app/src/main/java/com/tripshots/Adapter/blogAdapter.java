package com.tripshots.Adapter;


import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tripshots.ActivityBlogSubPage;
import com.tripshots.R;
import com.tripshots.response.blog_item;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class blogAdapter extends RecyclerView.Adapter<blogAdapter.ViewHolder> {

    private Context context;
    private List<blog_item> items;

    public blogAdapter(Context context, List<blog_item> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public blogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_post, viewGroup, false);
        return new blogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull blogAdapter.ViewHolder viewHolder,final int i) {

        final blog_item result = items.get(i);

        viewHolder.blog_post_title.setText(result.getTitle());

        String s = Html.fromHtml(result.getContent()).toString();
        s = s.substring(0, Math.min(s.length(), 250));

        viewHolder.blog_post_content.setText(s+"...");

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-HH:mm",
                Locale.ENGLISH);
        String s1 = result.getPublished();

        try
        {
            Date d = sdf.parse(s1);
            String s2 = (new SimpleDateFormat("dd MMMM")).format(d);

            viewHolder.blog_post_date.setText(s2);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        viewHolder.blog_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = i;
                Intent i = new Intent(context, ActivityBlogSubPage.class);
                i.putExtra("id",id);
                context.startActivity(i);
            }
        });

        String html = result.getContent();
        Document doc = Jsoup.parse(html);
        Element link = doc.select("img").first();

        String img_url = link.attr("src");

        Glide.with(context)
                .load(img_url)
                //.apply(new RequestOptions().override(140, 140))
                .into(viewHolder.blog_post_image);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView blog_post_image;
        TextView blog_post_title, blog_post_content, blog_post_date;
        Button blog_post_btn;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            blog_post_image = itemView.findViewById(R.id.blog_post_image);

            blog_post_title = itemView.findViewById(R.id.blog_post_title);
            blog_post_content = itemView.findViewById(R.id.blog_post_content);
            blog_post_date = itemView.findViewById(R.id.blog_post_date);

            blog_post_btn = itemView.findViewById(R.id.blog_post_btn);

        }

    }


}

