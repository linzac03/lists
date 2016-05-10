package me.lists5.lists;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import me.lists5.lists.AppDB;

public class ListBase extends AppCompatActivity implements View.OnClickListener {
    static AppDB db;
    static User user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent thisIntent = getIntent();

        if (thisIntent.getExtras() != null) {
            user = new User(thisIntent.getExtras().getString("user"));
        } else {
            user = new User("I'm a tester");
        }

        //Do DB tests
        db = new AppDB(this);
        String test = "post";
        ArrayList<String> testList = new ArrayList<String>();
        JSONArray testJ, testJ2;
        String testjstring = "{'beans':'got none'}";
        switch(test) {
            case "fetch":

                String result = db.testDB(user, new GetUserCallback() {
                    @Override
                    public void done(String returnedString) {
                        if (returnedString == null) {
                            showError("Null Response");
                        }
                    }
                });

                try {
                    testJ = new JSONArray(result != null ? result : "");
                    for (int i = 0; i < testJ.length(); i++) {
                        JSONObject obj = testJ.getJSONObject(i);
                        String name = obj.getString("name");
                        String price = obj.getString("price");
                        testList.add(name + " \t" + price);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case "post":
                String result2 = db.testPost(user, new GetUserCallback() {
                    @Override
                    public void done(String returnedResponse) {
                        if (returnedResponse == null) {
                            showError("Null Response");
                        }
                    }
                });

                try {
                    if(result2 != null) {
                        JSONObject obj = new JSONObject(result2);
                        testList.add(obj.getString("data"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
        setContentView(R.layout.activity_list_base);
        ListAdapter myAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                testList);

        ListView myListView = (ListView) findViewById(R.id.myLists);

        myListView.setAdapter(myAdapter);

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemSelected = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(ListBase.this, itemSelected, Toast.LENGTH_SHORT).show();
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add New List", Snackbar.LENGTH_LONG)
                        .setAction("Add", ListBase.this).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            default:
                if (db.authuser()) {
                    Intent newAct = new Intent(ListBase.this, ListEdit.class);
                    //Create the bundle
                    Bundle bundle = new Bundle();

                    bundle.putString("user", user.toString());

                    newAct.putExtras(bundle);

                    startActivity(newAct);
                }
        }
    }

    private void showError(String str) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListBase.this);
        dialogBuilder.setMessage(str);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
