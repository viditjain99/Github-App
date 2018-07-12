package com.example.vidit.github;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText searchEditText;
    Button searchButton;
    ProgressBar progressBar;
    public static String userName="";
    ArrayList<String> details;
    Intent intent;
    FrameLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        searchEditText=findViewById(R.id.searchEditText);
        searchButton=findViewById(R.id.searchButton);
        searchButton.setEnabled(true);
        progressBar=findViewById(R.id.progressBar);
        mainLayout=findViewById(R.id.mainLayout);
        searchButton.setOnClickListener(this);
        details=new ArrayList<>();
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        if(id==R.id.searchButton)
        {
            userName=searchEditText.getText().toString();
            searchButton.setVisibility(View.GONE);
            searchEditText.setVisibility(View.GONE);
            searchButton.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);
            fetchData();
        }
    }
    public void fetchData()
    {
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl("https://api.github.com").addConverterFactory(GsonConverterFactory.create());
        Retrofit retrofit=builder.build();
        GithubService service=retrofit.create(GithubService.class);
        Call<User> call=service.getDetails(userName);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user=response.body();
                if(user==null)
                {
                    searchEditText.setVisibility(View.VISIBLE);
                    searchButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    searchButton.setEnabled(true);
                    Snackbar.make(mainLayout,"User not found",Snackbar.LENGTH_LONG).show();
                    return;
                }
                details.add(user.name);
                details.add(user.image);
                details.add(user.company);
                details.add(user.location);
                details.add(user.publicRepos);
                intent=new Intent(MainActivity.this,DetailsActivity.class);
                intent.putExtra("Details",details);
                intent.putExtra("UserName",userName);
                progressBar.setVisibility(View.GONE);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t)
            {
                progressBar.setVisibility(View.GONE);
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("An error occured!");
                builder.setCancelable(false);
                builder.setPositiveButton("Retry", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        fetchData();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        searchEditText.setVisibility(View.VISIBLE);
                        searchButton.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        searchButton.setEnabled(true);
                        return;
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();
            }
        });
    }
}
