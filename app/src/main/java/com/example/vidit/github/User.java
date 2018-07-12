package com.example.vidit.github;

import com.google.gson.annotations.SerializedName;

public class User
{
    String name;
    @SerializedName("avatar_url")
    String image;
    String company;
    @SerializedName("public_repos")
    String publicRepos;
    String location;
}
