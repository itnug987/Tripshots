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
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.tripshots.Adapter.PostAdapter;
import com.tripshots.Data.sharedPref;
import com.tripshots.model.Post;

import java.util.ArrayList;
import java.util.List;

public class ActivityMyArticles extends AppCompatActivity {
    private ActionBar actionBar;
    private Toolbar toolbar;
    private NavigationView nav_view;

    com.tripshots.Data.sharedPref sharedPref;

    RecyclerView blog_recycler_view;
    ProgressBar progressBar;

    TextView drawer_name, drawer_location;

    List<Post> postList;
    PostAdapter postAdapter;

    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_articles);

        initToolbar();
        initDrawerMenu();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        blog_recycler_view = findViewById(R.id.blog_items);

        blog_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        getBlogPosts();

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
                i = new Intent(ActivityMyArticles.this, MainActivity.class);
                startActivity(i);
                break;

            case R.id.nav_my_articles:
                i = new Intent(ActivityMyArticles.this, ActivityMyArticles.class);
                startActivity(i);
                break;

            case R.id.nav_logout:
                sharedPref.logoutUser();
                i = new Intent(ActivityMyArticles.this, ActivitySignUp.class);
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
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("posts");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                progressBar.setVisibility(View.GONE);
                
                postList = new ArrayList<>();

                Iterable<DataSnapshot> postChildren = dataSnapshot.child(uid).getChildren();

                for (DataSnapshot postSnap : postChildren) {

                    Post post = postSnap.getValue(Post.class);

                    Log.d("post title", post.getTitle());

                    postList.add(post);
                }

                postAdapter = new PostAdapter(ActivityMyArticles.this, postList);
                blog_recycler_view.setAdapter(postAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void showDialogAbout() {
        AlertDialog.Builder alertBuilder;
        alertBuilder = new AlertDialog.Builder(ActivityMyArticles.this);

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

        alertBuilder = new AlertDialog.Builder(ActivityMyArticles.this);

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
