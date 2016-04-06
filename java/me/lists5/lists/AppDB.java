package me.lists5.lists;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import java.net.HttpCookie;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by porko on 2/28/16.
 */
public class AppDB {
    ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 15000;
    public static final String SERVER_ADDR = "http://lists5.me";

    public AppDB(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Retrieving Data..");
    }

    public void testStore() {

    }

    public void testFetch() {

    }

    public class AppTask extends AsyncTask<Void, Void, Void> {
        User user;

        @Override
        protected Void doInBackground(Void... params) {
            HashMap<String, String> dataToSend = new HashMap<>();
            //dataToSend.put("name", user.name) etc.


            return null;
        }
    }

}
