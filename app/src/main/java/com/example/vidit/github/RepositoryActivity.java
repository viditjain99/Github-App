package com.example.vidit.github;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RepositoryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
{
    Intent intent;
    ListView reposListView;
    ArrayAdapter<String> adapter;
    ArrayList<String> reposUrl;
    String userName;
    ArrayList<String> details;
    ProgressBar progressBar;
    TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repository);
        textView=findViewById(R.id.textView);
        textView.setVisibility(View.GONE);
        intent=getIntent();
        ArrayList<String> repos=intent.getStringArrayListExtra("Repos");
        textView=findViewById(R.id.textView);
        reposUrl=intent.getStringArrayListExtra("ReposUrl");
        userName=intent.getStringExtra("UserName");
        reposListView=findViewById(R.id.reposListView);
        progressBar=findViewById(R.id.progressBar);
        adapter=new ArrayAdapter(RepositoryActivity.this,android.R.layout.simple_list_item_1,repos);
        reposListView.setOnItemClickListener(RepositoryActivity.this);
        reposListView.setAdapter(adapter);
        details=new ArrayList<>();
        if(repos.size()==0)
        {
            textView.setVisibility(View.VISIBLE);
            return;
        }
    }
    @Override
    public void onBackPressed()
    {
        textView.setVisibility(View.GONE);
        reposListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        GithubService service=retrofit.create(GithubService.class);
        Call<User> call=service.getDetails(userName);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user=response.body();
                details.add(user.name);
                details.add(user.image);
                details.add(user.company);
                details.add(user.location);
                details.add(user.publicRepos);
                intent=new Intent(RepositoryActivity.this,DetailsActivity.class);
                intent.putExtra("Details",details);
                intent.putExtra("UserName",userName);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        String urlString=reposUrl.get(i);
         Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse(urlString));
         startActivity(intent);
    }
}
