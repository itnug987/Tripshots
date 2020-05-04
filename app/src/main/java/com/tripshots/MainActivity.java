package com.tripshots;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshots.Adapter.PostAdapter;
import com.tripshots.Adapter.blogAdapter;
import com.tripshots.Api.api;
import com.tripshots.Data.sharedPref;
import com.tripshots.model.Post;
import com.tripshots.response.blog_item;
import com.tripshots.response.blog_response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private Toolbar toolbar;
    private NavigationView nav_view;

    com.tripshots.Data.sharedPref sharedPref;

    RecyclerView blog_recycler_view;
    RecyclerView blog_firebase_recycler_view;
    ProgressBar progressBar;

    TextView drawer_name, drawer_location;

    FloatingActionButton add_post;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(com.tripshots.Api.api.url)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    com.tripshots.Api.api Api = retrofit.create(com.tripshots.Api.api.class);

    List<Post> postList;
    PostAdapter postAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawerMenu();

        sharedPref = new sharedPref(getApplicationContext());

        HashMap<String, String> user = sharedPref.getUserDetails();

        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        blog_recycler_view = findViewById(R.id.blog_items);
        blog_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        blog_firebase_recycler_view = findViewById(R.id.blog_items_firebase);
        blog_firebase_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        add_post = findViewById(R.id.add_post);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getBlogPosts();

        getBlogPostsFirebase();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference().child("users").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String name = dataSnapshot.child("name").getValue().toString();
                String location = dataSnapshot.child("location").getValue().toString();

                try {

                    drawer_name = findViewById(R.id.drawer_name);
                    drawer_location = findViewById(R.id.drawer_location);

                    drawer_name.setText(name);
                    drawer_location.setText(location);
                }
                catch (Exception e){
                    Log.d("drawer exception", e.getMessage());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        add_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ActivityAddPost.class);
                startActivity(i);
            }
        });

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.app_name);
    }

    private void initDrawerMenu() {
        nav_view = (NavigationView) findViewById(R.id.nav_view);
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close) {
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        View header_view = nav_view.getHeaderView(0);

        drawer.setDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem item) {
                onItemSelected(item.getItemId());
                //drawer.closeDrawers();
                return true;
            }
        });

        nav_view.setItemIconTintList(getResources().getColorStateList(R.color.nav_state_list));

    }

    public boolean onItemSelected(int id){
        Intent i;

        switch (id) {
            case R.id.nav_home:
                i = new Intent(MainActivity.this, MainActivity.class);
                startActivity(i);
                break;
            case R.id.nav_logout:
                sharedPref.logoutUser();
                i = new Intent(MainActivity.this, ActivitySignUp.class);
                startActivity(i);
                break;

            case R.id.about_us:
                showDialogAbout();
                break;

            case R.id.support:
                showDialogSupport();
                break;

            default:
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawers();
        return true;
    }

    private void getBlogPosts(){
        Call<blog_response> call = Api.getBlogPosts("AIzaSyBdlfcNJoSNNgbhjnzv2SsB3tPEz7Qkh8Q");

        call.enqueue(new Callback<blog_response>() {
            @Override
            public void onResponse(Call<blog_response> call, Response<blog_response> response) {
                if(response.isSuccessful()){
                    progressBar.setVisibility(View.GONE);
                    Log.d("blog retrieval", "success");
                    blog_response blogResponse = response.body();

                    List<blog_item> result = blogResponse.getItems();
                    blog_recycler_view.setAdapter(new blogAdapter(MainActivity.this, blogResponse.getItems()));

                }
            }

            @Override
            public void onFailure(Call<blog_response> call, Throwable t) {
                Log.d("Blog retrieval failed", t.getCause().toString());
                Log.d("blog retrieval", "failure");


            }
        });
    }

    private void getBlogPostsFirebase(){
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");

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

                    postAdapter = new PostAdapter(MainActivity.this, postList);
                    blog_firebase_recycler_view.setAdapter(postAdapter);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void showDialogAbout() {
        AlertDialog.Builder alertBuilder;
        alertBuilder = new AlertDialog.Builder(MainActivity.this);

        alertBuilder.setMessage(R.string.about_us)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("About Us");
        alertDialog.show();
    }

    public void showDialogSupport(){
        AlertDialog.Builder alertBuilder;

        alertBuilder = new AlertDialog.Builder(MainActivity.this);

        alertBuilder.setMessage(R.string.support)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle("Support");
        alertDialog.show();
    }


}
