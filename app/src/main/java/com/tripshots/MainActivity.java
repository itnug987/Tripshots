package com.tripshots;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.navigation.NavigationView;
import com.tripshots.Adapter.blogAdapter;
import com.tripshots.Api.api;
import com.tripshots.response.blog_item;
import com.tripshots.response.blog_response;

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

    RecyclerView blog_recycler_view;

    Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(com.tripshots.Api.api.url)
            .addConverterFactory(GsonConverterFactory.create());
    Retrofit retrofit = builder.build();
    com.tripshots.Api.api Api = retrofit.create(com.tripshots.Api.api.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        initDrawerMenu();

        blog_recycler_view = findViewById(R.id.blog_items);
        blog_recycler_view.setLayoutManager(new LinearLayoutManager(this));

        getBlogPosts();
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


            case R.id.privacy_policy:
                String inURL = "https://www.freshokartz.com/privacy-policy.html";
                i = new Intent( Intent.ACTION_VIEW , Uri.parse( inURL ) );
                startActivity(i);
                break;



            case R.id.about_us:
               // showDialogAbout();
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
}
