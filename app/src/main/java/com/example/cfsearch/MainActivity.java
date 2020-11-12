package com.example.cfsearch;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Code>> {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    String CODEFORCES_REQUEST_URL;
    final static String CODEFORCES_BASE_URL = "https://codeforces.com/api/user.info?";
    final static String PARAMS_HANDLES = "handles";

    private static final int EARTHQUAKE_LOADER_ID = 1;

    private CodeAdapter mAdapter;
    MaterialEditText coderName;
    Button btn_search;
    TextView mEmptyStateTextView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        coderName = findViewById(R.id.handle);
        btn_search = findViewById(R.id.btn_search);
        mEmptyStateTextView = findViewById(R.id.empty_view);

        ListView CodeListView = findViewById(R.id.list_view);
        CodeListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new CodeAdapter(this,new ArrayList<Code>());
        CodeListView.setAdapter(mAdapter);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String handle = coderName.getText().toString();
                CODEFORCES_REQUEST_URL = buildUrl(handle);
                getLoaderManager().restartLoader(EARTHQUAKE_LOADER_ID,null, MainActivity.this);
            }
        });

//        CodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                // Find the current earthquake that was clicked on
//                Code currentNews = mAdapter.getItem(position);
//
//                // Convert the String URL into a URI object (to pass into the Intent constructor)
//                Uri newsUri = Uri.parse(currentNews.getmUrl());
//
//                // Create a new intent to view the earthquake URI
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
//
//                // Send the intent to launch a new activity
//                startActivity(websiteIntent);
//            }
//        });

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if(networkInfo!=null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null, MainActivity.this);
        }
        else {
            mEmptyStateTextView.setText("No Internet Connection");
            Toast.makeText(MainActivity.this,"No Internet Connection",Toast.LENGTH_SHORT).show();
        }
    }

    public static String buildUrl(String handle) {
        Uri buildUri = Uri.parse(CODEFORCES_BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_HANDLES,handle)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        assert url != null;
        return url.toString();
    }


    @Override
    public Loader<List<Code>> onCreateLoader(int i, Bundle bundle) {
        return new CodeLoader(this,CODEFORCES_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Code>> loader,List<Code> code){
        mEmptyStateTextView.setText("");
        mAdapter.clear();
        if(code!=null && !code.isEmpty()){
            mAdapter.addAll(code);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Code>> loader){
        mAdapter.clear();
    }


}