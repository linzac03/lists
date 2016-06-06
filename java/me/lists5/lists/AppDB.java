package me.lists5.lists;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by porko on 2/28/16.
 */
public class AppDB {
    static HttpURLConnection conn = null;
    static private String SERVER_ADDR = null;
    static ProgressDialog progressDialog;
    static Context superContext;
    static public String response;

    public AppDB(Context context) {
        superContext = context;
        progressDialog = new ProgressDialog(context);
        if (conn != null) {
            conn.disconnect();
        }
        SERVER_ADDR = "http://lists5.me/lists/db";

    }

    public String testDB(User user, GetUserCallback getUserCallback) {
        HashMap<String, String> myargs = new HashMap<String, String>();
        AppTask at = new AppTask(user, getUserCallback, myargs);
        at.execute();
        return response;
    }

    public String testPost(User user, GetUserCallback getUserCallback) {
        HashMap<String, String> myargs = new HashMap<>();
        myargs.put("data", "this text went from client to server to client");
        AppTask at = new AppTask(user, getUserCallback, myargs);
        at.execute();
        return response;
    }

    public String get(User user, GetUserCallback getUserCallback, HashMap<String,String> myargs) {
        myargs.put("action", "get");
        AppTask at = new AppTask(user, getUserCallback, myargs);
        at.execute();
        return response;
    }

    public String post(User user, GetUserCallback getUserCallback, HashMap<String,String> myargs) {
        myargs.put("action", "post");
        AppTask at = new AppTask(user, getUserCallback, myargs);
        at.execute();
        return response;
    }

    public boolean authuser() { return true; }

    public class AppTask extends AsyncTask<Void, Void, Void> {
        User user;
        GetUserCallback userCallback;
        HashMap<String,String> taskParams;
        String resp = "";

        public AppTask(User user1, GetUserCallback userCallback1, HashMap<String,String> params) {
            this.user = user1;
            this.userCallback = userCallback1;
            this.taskParams = params;
        }

        public void setProgress() {
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Checking Database");
            progressDialog.setMessage("Please wait...");
        }

        public String getResponseMessage() {
            if (resp != null) {
                return resp;
            }
            return "Empty Response: -1";
        }

        //get Data from a specific url
        public void _get(HashMap<String, String> params) {
            setProgress();
            String urlparams = "/";
            if (!params.isEmpty()) {
                for (HashMap.Entry<String, String> entry : params.entrySet()) {
                    urlparams += entry.getValue() + "/";
                }
            }

            String request = SERVER_ADDR;
            int status;

            try {
                conn = null;
                URL url = new URL( request );
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Content-length", "0");
                conn.setUseCaches(false);
                conn.setAllowUserInteraction(false);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);
                conn.connect();

                status = conn.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        resp = sb.toString();
                        break;
                    default:
                        resp = "No Response";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resp = "Failed Url";
            } catch (IOException e) {
                e.printStackTrace();
                resp = "Failed IO";
            } finally {
                conn.disconnect();
            }
        }

        //Post to a url
        public void _post(HashMap<String, String> params) {
            setProgress();
            String outparams = "";
            String request = SERVER_ADDR + "/post";
            int status;
            try {
                //set connection properties
                conn = null; // this is just in case I have a lingering connection
                URL url = new URL(request);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                //start write to outputstream
                if (!params.isEmpty()) {
                    OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
                    for (HashMap.Entry<String, String> entry : params.entrySet()) {
                        out.write(entry.getKey() + "='" + entry.getValue() + "'&");
                    }
                    out.close();
                }

                //parse response
                status = conn.getResponseCode();
                switch (status) {
                    case 200:
                    case 201:
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        resp = sb.toString();
                        break;
                    default:
                        resp = "No Response";
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                resp = "Failed Url";
            } catch (IOException e) {
                e.printStackTrace();
                resp = "Failed IO";
            } finally {
                conn.disconnect();
            }
            progressDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {
            //request(taskParams);
            switch (this.taskParams.get("action")) {
                case "post":
                    _post(taskParams);
                    break;
                case "get":
                    _get(taskParams);
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            response = resp != null ? resp : "NULL RETURNED";
            userCallback.done(response);
            progressDialog.dismiss();

            super.onPostExecute(aVoid);
        }

        protected void onPreExecute() {

        }
    }

}
