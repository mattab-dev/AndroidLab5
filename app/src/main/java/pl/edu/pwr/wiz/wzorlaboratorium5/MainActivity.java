package pl.edu.pwr.wiz.wzorlaboratorium5;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.Gson;
import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final String POSTS_URL = "https://jsonplaceholder.typicode.com/posts";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fetchData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_retrofit :     startActivity(new Intent(this, RetrofitActivity.class));
                                            break;
            case R.id.action_webkit :       startActivity(new Intent(this, WebviewActivity.class));
                                            break;
        }


        return super.onOptionsItemSelected(item);
    }

    /* Funkcja pobiera dane z API */
    private boolean fetchData() {
        /* Ustawiamy widoczność progress baru i chowamy listę */
        ListView listView = (ListView) findViewById(R.id.posts_list);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

        listView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        /* Uruchamiamy wątek do ładowania danych przy pomocy HttpsUrlConnection */
        new LoadDataTask().execute(POSTS_URL);

        return true;
    }

    /* AsyncTask do pobierania danych w tle */
    private class LoadDataTask extends AsyncTask<String, Void, PostLoadedData> {
        /* Wątek wykonywany w tle */
        @Override
        protected PostLoadedData doInBackground(String... urls) {
            if(urls.length != 1) return null;   // Sprawdzamy czy przekazano dokładnie jeden URL

            try {
                HttpsURLConnection c =
                        (HttpsURLConnection ) new URL(urls[0]).openConnection();
                try {
                    InputStream in = c.getInputStream();
                    BufferedReader reader =
                            new BufferedReader(new InputStreamReader(in));
                    Post[] posts =
                            new Gson().fromJson(reader, Post[].class);
                    reader.close();

                    return new PostLoadedData(posts);

                } catch (IOException e) {
                    Log.e(getClass().getSimpleName(), "Błąd parsowania formatu JSON", e);
                } finally {
                    c.disconnect();
                }
            }
            catch (Exception e) {
                Log.e(getClass().getSimpleName(), "Błąd parsowania formatu JSON", e);
            }

            return null;
        }

        /* Obsluga powrotu z wątku z danymi */
        @Override
        protected void onPostExecute(PostLoadedData postLoadedData) {
            ListView listView = (ListView) findViewById(R.id.posts_list);
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressbar);

            if(postLoadedData != null) listView.setAdapter(new PostsAdapter(getApplicationContext(), postLoadedData.getPosts()));
             else Toast.makeText(MainActivity.this, R.string.download_error, Toast.LENGTH_LONG).show();

            progressBar.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}
