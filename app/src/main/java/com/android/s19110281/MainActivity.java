package com.android.s19110281;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>{

    private EditText urlInput;

    private Spinner spinner;
    private String protocol;
    private TextView sourceCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlInput = findViewById(R.id.urlInput);
        spinner = findViewById(R.id.spinner);
        sourceCode = findViewById(R.id.source_code);
        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }
    public void searchBooks(View view) {
        // Get the search string from the input field.
        String queryString = urlInput.getText().toString();
        protocol = spinner.getSelectedItem().toString();
        protocol = protocol.substring(0,protocol.length()-3);

        // Hide the keyboard when the button is pushed.
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a BookLoader AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {

            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);

        }
        // Otherwise update the TextView to tell the user there is no
        // connection, or no search term.
        else {
            if (queryString.length() == 0) {
                Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Please check your network connection and try again.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";

        if (args != null) {
            queryString = args.getString("queryString");
        }

        return new PageSourceLoader(this, queryString,protocol);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        if (data==null)
            sourceCode.setText("No response");
        else {
            sourceCode.setText(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }
}