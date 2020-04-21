package com.tripshots;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tripshots.response.blog_response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ActivityBlogSubPage extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;

    ImageView post_img;
    TextView post_title, post_date, post_content;

    Retrofit.Builder blog_builder = new Retrofit.Builder()
            .baseUrl(com.tripshots.Api.api.url)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit blog_retrofit = blog_builder.build();
    com.tripshots.Api.api blog_Api = blog_retrofit.create(com.tripshots.Api.api.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_sub_page);

        post_img = findViewById(R.id.post_img);
        post_title = findViewById(R.id.post_title);
        post_date = findViewById(R.id.post_date);
        post_content = findViewById(R.id.post_content);

        Intent intent = getIntent();
        //     Long postID = intent.getLongExtra("postID",20001520);

        int id = intent.getIntExtra("id",0);
        getPost(id);

        initToolbar();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle("Back");
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == android.R.id.home) {
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    private void getPost(final int id){
        Call<blog_response> call = blog_Api.getBlogPosts("AIzaSyBdlfcNJoSNNgbhjnzv2SsB3tPEz7Qkh8Q");

        call.enqueue(new Callback<blog_response>() {
            @Override
            public void onResponse(Call<blog_response> call, Response<blog_response> response) {
                blog_response result = response.body();

                post_title.setText(result.getItems().get(id).getTitle());

                post_content.setText(Html.fromHtml(result.getItems().get(id).getContent()).toString());

                DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-HH:mm",
                        Locale.US);
                String s1 = result.getItems().get(id).getPublished();
                Date d;
                try
                {
                    d = sdf.parse(s1);
                    String s2 = (new SimpleDateFormat("dd MMMM")).format(d);

                    post_date.setText(s2);
                }
                catch (Exception e)
                {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                String html = result.getItems().get(id).getContent();
                Document doc = Jsoup.parse(html);
                Element link = doc.select("img").first();

                String img_url = link.attr("src");

                Glide.with(getApplicationContext())
                        .load(img_url)
                        //.apply(new RequestOptions().override(140, 140))
                        .into(post_img);


            }

            @Override
            public void onFailure(Call<blog_response> call, Throwable t) {

            }
        });

    }
}
