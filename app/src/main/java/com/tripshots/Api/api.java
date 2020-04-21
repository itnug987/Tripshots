package com.tripshots.Api;

import com.tripshots.response.blog_item;
import com.tripshots.response.blog_response;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface api {
    String url = "https://www.googleapis.com/blogger/v3/";

    @GET("blogs/2949655844739228374/posts/")
    Call<blog_response> getBlogPosts(@Query("key") String key);

    @GET("blogs/2949655844739228374/posts/")
    Call<blog_item> getSpecificBlogPost(@Path("postId") Long postId, @Query("key") String key);
}
