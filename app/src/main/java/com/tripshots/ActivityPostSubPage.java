package com.tripshots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshots.Adapter.PostAdapter;
import com.tripshots.model.Post;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ActivityPostSubPage extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;

    ImageView post_img;
    TextView post_title, post_date, post_content;

    List<Post> postList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_sub_page);

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
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");

        postList = new ArrayList<>();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();

                Log.d("firebase posts", "message");

                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {

                    Log.d("dataSnapshot key", FirebaseAuth.getInstance().getUid());

                    Iterable<DataSnapshot> postChildren = snapshot.getChildren();

                    for (DataSnapshot postSnap : postChildren) {

                        Post post = postSnap.getValue(Post.class);

                        Log.d("post title", post.getTitle());

                        postList.add(post);
                    }

                    post_title.setText(postList.get(id).getTitle());
                    post_content.setText(postList.get(id).getTravel_story());


                    Glide.with(getApplicationContext())
                            .load(postList.get(id).getImage_url())
                            .into(post_img);

                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss-HH:mm",
                            Locale.ENGLISH);
                    Date d = new Date();
                    String s1 = d.toString();

                    try
                    {
                        Date d1 = sdf.parse(s1);
                        String s2 = (new SimpleDateFormat("dd MMMM YYYY")).format(d1);

                        post_date.setText(s2);
                    }
                    catch (Exception e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
