package pl.edu.pwr.wiz.wzorlaboratorium5;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {
    static final String TAG = "CommentActivity";
    JsonlaceholderInterface jsonService;

    Integer parentPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit_comments);

        Intent passedIntent = getIntent();
        parentPostId = passedIntent.getExtras().getInt("postId");

        /* Podpinamy się pod ActionBar */
        setupActionBar();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment();
            }
        });

        fetchData();
    }

    /* Funkcja pobiera dane z API */
    private boolean fetchData() {
        /* Ustawiamy widoczność progress baru i chowamy listę */
        ListView listView = (ListView) findViewById(R.id.comments_list_retrofit);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_retrofit_comments);

        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        /* Uruchamiamy zapytanie przy pomocy RetroFit */
        jsonService = JsonlaceholderInterface.retrofit.create(JsonlaceholderInterface.class);
        Call<List<Comment>> call = jsonService.getCommentsByPostId(parentPostId);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                ListView listView = (ListView) findViewById(R.id.comments_list_retrofit);
                ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar_retrofit_comments);

                listView.setAdapter(new CommentsAdapter(getApplicationContext(), response.body()));

                progressBar.setVisibility(View.INVISIBLE);
                listView.setVisibility(View.VISIBLE);
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Wystąpił problem z połączeniem", Toast.LENGTH_SHORT).show();
            }
        });

        return true;
    }

    /* Funkcja generuje pole dialogowe do dodania posta */
    private void addComment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Dodawanie komentarza");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.comment_add, null);

        // Zapamietujemy nasze pola
        final EditText commentIdInput = (EditText) viewInflated.findViewById(R.id.commentId);
        final EditText commentNameInput = (EditText) viewInflated.findViewById(R.id.commentName);
        final EditText commentEmailInput = (EditText) viewInflated.findViewById(R.id.commentEmail);
        final EditText commentBodyInput = viewInflated.findViewById(R.id.commentInsertBody);

        // Ustawiamy widok do buildera
        builder.setView(viewInflated);

        // Callback'i dla przycisków
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                Integer commentId = Integer.parseInt(commentIdInput.getText().toString());
                String name = commentNameInput.getText().toString();
                String email = commentEmailInput.getText().toString();
                String body = commentBodyInput.getText().toString();

                Log.v(TAG, "CommentId: " + commentId.toString());
                Log.v(TAG, "Name: " + name);
                Log.v(TAG, "Email: " + email);
                Log.v(TAG, "Body: " + body);

                Call<Post> call = jsonService.addComment(commentId, name, email, body);
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
