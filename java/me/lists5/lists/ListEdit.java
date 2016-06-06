package me.lists5.lists;

import android.app.ProgressDialog;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
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
    static ArrayList<String> newList, newListDisplay;
    EditText newItem;
    static ListView myListView;
    static ListAdapter myAdapter;
    ProgressDialog progressDialog;
    static Intent newAct;
    static String listName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (newList == null) {
            newList = new ArrayList<>();
            newListDisplay = new ArrayList<>();
        }
        db = new AppDB(this);
        Intent thisIntent = getIntent();

        if (thisIntent.getExtras() != null) {
            user = new User(thisIntent.getExtras().getString("user"));
        } else {
            user = new User("I'm a tester");
        }

        listName = getIntent().getExtras().getString("name");
        setTitle(listName);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(this);
        setSupportActionBar(toolbar);

        Button addBtn = (Button) findViewById(R.id.AddItem);
        addBtn.setOnClickListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(this);
        String newItem = thisIntent.getExtras().getString("newItem");
        String newItemDisplay = thisIntent.getExtras().getString("newItemDisplay");
        if (newItem != null) {
            newList.add(newItem);
            newListDisplay.add(newItemDisplay);
        }
        myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                newListDisplay);

        myListView = (ListView) findViewById(R.id.myLists);
        if (newList.size() > 0) {
            ViewGroup.LayoutParams listviewps = myListView.getLayoutParams();
            View item = myAdapter.getView(0, null, myListView);
            item.measure(0, 0);
            listviewps.height = newList.size() * item.getMeasuredHeight();
            myListView.setLayoutParams(listviewps);
        }
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
            case R.id.fab:
                newAct = new Intent(ListEdit.this, ListBase.class);

                //Convert newList to HashMap<String,String>
                HashMap<String,String> newArgs = new HashMap<>();
                int i = 1;
                String str = "{";
                str += "'name':'" + listName + "', 'items':{";
                for (String item : newList) {
                    str += item + ( i<newList.size() ? "," : "");
                }
                str += "}}";
                newArgs.put("query", "newlist");
                newArgs.put("data", str);

                newList.clear();
                newListDisplay.clear();
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
                newAct = new Intent(ListEdit.this, ListAddItem.class);
                final Bundle bundle = new Bundle();
                bundle.putString("name",listName);

                newAct.putExtras(bundle);
                startActivity(newAct);
                break;
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
