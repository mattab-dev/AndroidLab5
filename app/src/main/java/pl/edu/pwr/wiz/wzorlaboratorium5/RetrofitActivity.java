package pl.edu.pwr.wiz.wzorlaboratorium5;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetrofitActivity extends AppCompatActivity {
    static final String TAG = "RetrofitActivity";
    JsonlaceholderInterface jsonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);

        /* Podpinamy się pod ActionBar */
        setupActionBar();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost();
            }
        });

        fetchData();
    }

    /* Funkcja pobiera dane z API */
    private boolean fetchData() {
        /* Ustawiamy widoczność progress baru i chowamy listę */
        ListView listView = (ListView) findViewById(R.id.posts_list_retrofit);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_retrofit);

        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        /* Uruchamiamy zapytanie przy pomocy RetroFit */
        jsonService = JsonlaceholderInterface.retrofit.create(JsonlaceholderInterface.class);
        Call<List<Post>> call = jsonService.getPosts();
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                ListView listView = (ListView) findViewById(R.id.posts_list_retrofit);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_retrofit);

                listView.setAdapter(new PostsAdapter(getApplicationContext(), response.body()));

                progressBar.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Wystąpił problem z połączeniem", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    /* Funkcja generuje pole dialogowe do dodania posta */
    private void addPost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dodawania wpisu");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.post_add, null);

        // Zapamietujemy nasze pola
        final EditText postTitleInput = (EditText) viewInflated.findViewById(R.id.postTitle);
        final EditText postBodyInput = (EditText) viewInflated.findViewById(R.id.postBody);
        final EditText postUserIdInput = (EditText) viewInflated.findViewById(R.id.postUserId);

        // Ustawiamy widok do buildera
        builder.setView(viewInflated);

        // Callback'i dla przycisków
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Integer userId = Integer.parseInt(postUserIdInput.getText().toString());
                String title = postTitleInput.getText().toString();
                String body = postBodyInput.getText().toString();

                Log.v(TAG, "UserId: " + userId.toString());
                Log.v(TAG, "Title: " + title);
                Log.v(TAG, "Body: " + body);

                Call<Post> call = jsonService.addPost(userId, title, body);
                call.enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Toast.makeText(getApplicationContext(), "Dane zapisane pod ID: " + response.body().id.toString(), Toast.LENGTH_LONG).show();
                    }
                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Log.v(TAG, t.toString());
                        Toast.makeText(getApplicationContext(), "Wystąpił problem z połączeniem", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Podpięcie pod ActionBar
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
