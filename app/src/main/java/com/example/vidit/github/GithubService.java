package com.example.vidit.github;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubService
{
    @GET("/users/{name}")
    Call<User> getDetails(@Path("name") String name);

    @GET("/users/{name}/repos")
    Call<ArrayList<Repository>> getRepos(@Path("name") String name);

    @GET("/users/{name}/followers")
    Call<ArrayList<Followers>> getFollowers(@Path("name") String name);
}
