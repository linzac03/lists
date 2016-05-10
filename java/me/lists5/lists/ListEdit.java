package me.lists5.lists;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class ListEdit extends AppCompatActivity implements View.OnClickListener {
    static AppDB db;
    static User user;
    static ArrayList<String> newList;
    EditText newItem;
    static ListView myListView;
    static ListAdapter myAdapter;
    ProgressDialog progressDialog;
    static Intent newAct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        newList = new ArrayList<>();
        db = new AppDB(this);
        user = new User(getIntent().getExtras().getString("user"));

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        setSupportActionBar(toolbar);
        newItem = (EditText) findViewById(R.id.NewItem);
        Button addBtn = (Button) findViewById(R.id.AddItem);

        addBtn.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //String[] testList = {"This", "Is", "A", "Test"};
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                newList);

        myListView = (ListView) findViewById(R.id.myLists);

        myListView.setAdapter(myAdapter);
    }

    public void refreshList() {
        ViewGroup.LayoutParams listviewps = myListView.getLayoutParams();
        View item = myAdapter.getView(0, null, myListView);
        item.measure(0, 0);
        listviewps.height = newList.size() * item.getMeasuredHeight();
        myListView.setLayoutParams(listviewps);
        myListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                newList));
    }

    public void setProgress(Context context) {
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Adding Item");
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
    }

    public void dismissProgress() {
        progressDialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar:
                newAct = new Intent(ListEdit.this, ListBase.class);
                //Convert newList to HashMap<String,String>
                HashMap<String,String> newArgs = new HashMap<>();
                int i = 0;
                for (String item : newList) {
                    newArgs.put("item" + (i++),item);
                }
                //Post to DB
                db.post(user, new GetUserCallback() {
                    @Override
                    public void done(String returnedResponse) {
                        startActivity(newAct);
                    }
                },newArgs); // the most disgusting code i've ever written.
                // probably broken
                break;

            case R.id.AddItem:
                if (newItem != null) {
                //newList.add(newItem.toString());
                //finish();
                //startActivity(getIntent());
                setProgress(this);
                new EditTask(newItem.getText().toString()).execute();
                break;
            }
        }
    }



    class EditTask extends AsyncTask<Void, Void, Void> {
        String name;


        public EditTask(String name) {
            this.name = name;
        }

        @Override
        protected Void doInBackground(Void... params) {
            newList.add(name);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            dismissProgress();
            refreshList();
        }
    }
}
