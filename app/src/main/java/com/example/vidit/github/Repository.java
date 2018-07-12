package com.example.vidit.github;

import com.google.gson.annotations.SerializedName;

public class Repository
{
    String name;
    @SerializedName("html_url")
    String url;
}
