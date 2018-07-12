package com.example.vidit.github;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailsActivity extends AppCompatActivity implements View.OnClickListener
{
    TextView userNameTextView,companyTextView,reposTextView,locationTextView;
    Button reposButton,followersButton;
    ProgressBar progressBar;
    ImageView imageView;
    ArrayList<String> details;
    Intent intent,intent1;
    ArrayList<String> repoNames;
    ArrayList<String> reposUrl;
    ArrayList<String> followersName;
    String userName="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        imageView=findViewById(R.id.imageView);
        companyTextView=findViewById(R.id.companyTextView);
        reposTextView=findViewById(R.id.reposTextView);
        locationTextView=findViewById(R.id.locationTextView);
        userNameTextView=findViewById(R.id.userNameTextView);
        reposButton=findViewById(R.id.reposButton);
        reposButton.setEnabled(true);
        followersButton=findViewById(R.id.followersButton);
        followersButton.setEnabled(true);
        progressBar=findViewById(R.id.progressBar);
        reposButton.setOnClickListener(DetailsActivity.this);
        followersButton.setOnClickListener(DetailsActivity.this);
        intent=getIntent();
        details=intent.getStringArrayListExtra("Details");
        userName=intent.getStringExtra("UserName");
        String imageUrl=details.get(1);
        Picasso.get().load(imageUrl).into(imageView);
        String name=details.get(0);
        String company=details.get(2);
        String repos=details.get(4);
        String location=details.get(3);
        if(name==null)
        {
            userNameTextView.setText("Name: N/A");
        }
        else
        {
            userNameTextView.setText("Name: "+details.get(0));
        }
        if(company==null)
        {
            companyTextView.setText("Company: N/A");
        }
        else
        {
            companyTextView.setText("Company: "+details.get(2));
        }
        if(repos==null)
        {
            reposTextView.setText("Repositories: N/A");
        }
        else
        {
            reposTextView.setText("Repositories: "+details.get(4));
        }
        if(location==null)
        {
            locationTextView.setText("Location: N/A");
        }
        else
        {
            locationTextView.setText("Location: "+details.get(3));
        }
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.reposButton)
        {
            imageView.setVisibility(View.GONE);
            userNameTextView.setVisibility(View.GONE);
            companyTextView.setVisibility(View.GONE);
            reposTextView.setVisibility(View.GONE);
            locationTextView.setVisibility(View.GONE);
            reposButton.setVisibility(View.GONE);
            reposButton.setEnabled(false);
            followersButton.setEnabled(false);
            followersButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            intent1=new Intent(DetailsActivity.this,RepositoryActivity.class);
            repoNames=new ArrayList<>();
            reposUrl=new ArrayList<>();
            Retrofit.Builder builder=new Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            GithubService service=retrofit.create(GithubService.class);
            Call<ArrayList<Repository>> call=service.getRepos(userName);
            call.enqueue(new Callback<ArrayList<Repository>>() {
                @Override
                public void onResponse(Call<ArrayList<Repository>> call, Response<ArrayList<Repository>> response)
                {
                    ArrayList<Repository> repos=response.body();
                    repoNames.clear();
                    for(int i=0;i<repos.size();i++)
                    {
                        repoNames.add(repos.get(i).name);
                        reposUrl.add(repos.get(i).url);
                    }
                    intent1.putExtra("Repos",repoNames);
                    intent1.putExtra("ReposUrl",reposUrl);
                    intent1.putExtra("UserName",userName);
                    progressBar.setVisibility(View.GONE);
                    startActivity(intent1);
                }
                @Override
                public void onFailure(Call<ArrayList<Repository>> call, Throwable t) {

                }
            });
        }
        else if(id==R.id.followersButton)
        {
            imageView.setVisibility(View.GONE);
            userNameTextView.setVisibility(View.GONE);
            companyTextView.setVisibility(View.GONE);
            reposTextView.setVisibility(View.GONE);
            locationTextView.setVisibility(View.GONE);
            reposButton.setVisibility(View.GONE);
            reposButton.setEnabled(false);
            followersButton.setEnabled(false);
            followersButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            intent1=new Intent(DetailsActivity.this,FollowersActivity.class);
            followersName=new ArrayList<>();
            Retrofit.Builder builder=new Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit=builder.build();
            GithubService service=retrofit.create(GithubService.class);
            Call<ArrayList<Followers>> call=service.getFollowers(userName);
            call.enqueue(new Callback<ArrayList<Followers>>() {
                @Override
                public void onResponse(Call<ArrayList<Followers>> call, Response<ArrayList<Followers>> response)
                {
                    ArrayList<Followers> followers=response.body();
                    followersName.clear();
                    for(int i=0;i<followers.size();i++)
                    {
                        followersName.add(followers.get(i).login);
                    }
                    intent1.putExtra("Followers",followersName);
                    intent1.putExtra("UserName",userName);
                    progressBar.setVisibility(View.GONE);
                    startActivity(intent1);
                }
                @Override
                public void onFailure(Call<ArrayList<Followers>> call, Throwable t) {

                }
            });
        }
    }
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent intent=new Intent(DetailsActivity.this,MainActivity.class);
        finish();
        startActivity(intent);
    }
}
