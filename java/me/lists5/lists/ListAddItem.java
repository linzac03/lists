package me.lists5.lists;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import me.lists5.lists.BillItem;
import java.util.ArrayList;

public class ListAddItem extends AppCompatActivity implements OnItemSelectedListener {
    LinearLayout containerLayout;
    int[] itemfields = {R.id.billAmount,R.id.billDue,R.id.billInitMonth
                       ,R.id.billInitDay,R.id.billName,R.id.textItem
                       ,R.id.billDueTitle,R.id.billInitTitle};
    static String listname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        containerLayout = (LinearLayout) findViewById(R.id.construct_item);
        setSupportActionBar(toolbar);

        final Spinner spinner = (Spinner)findViewById(R.id.item_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.item_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listname = getIntent().getExtras().getString("name");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newAct = new Intent(ListAddItem.this, ListEdit.class);
                final Bundle bundle = new Bundle();

                switch(spinner.getSelectedItem().toString()) {
                    case "Bill":
                        EditText name = (EditText) findViewById(R.id.billName);
                        EditText amount = (EditText) findViewById(R.id.billAmount);
                        Spinner month = (Spinner) findViewById(R.id.billInitMonth);
                        Spinner day = (Spinner) findViewById(R.id.billInitDay);
                        Spinner freq = (Spinner) findViewById(R.id.billDue);
                        BillItem newBill = new BillItem(name.getText().toString(), amount.getText().toString(),
                                month.getSelectedItem().toString()+"/"+day.getSelectedItem().toString(),freq.getSelectedItem().toString());
                        bundle.putString("newItem", newBill.toString());
                        bundle.putString("newItemDisplay", name.getText().toString() + " \t" + amount.getText().toString());
                        break;
                    case "Text":
                        break;
                    default:
                        break;
                }
                bundle.putString("name",listname);
                newAct.putExtras(bundle);
                startActivity(newAct);
//                Snackbar.make(view, "Add New List", Snackbar.LENGTH_LONG)
//                        .setAction("Add", ListBase.this).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        String itemtype = parent.getItemAtPosition(pos).toString();

        for (int j : itemfields) {
            View v = containerLayout.findViewById(j);
            if (v != null) {
                containerLayout.removeView(v);
            }
        }

        switch (itemtype) {
            case "Bill":
                spawnEditText("Name", R.id.billName);
                spawnEditText("Amount", R.id.billAmount);
                TextView nextDue = new TextView(this);
                nextDue.setText("Next Due");
                nextDue.setId(R.id.billDueTitle);
                containerLayout.addView(nextDue);
                String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Dec"};
                spawnSpinner(months, R.id.billInitMonth);
                String[] days = {"1" ,"2" ,"3" ,"4" ,"5"
                                ,"6" ,"7" ,"8" ,"9" ,"10"
                                ,"11","12","13","14","15"
                                ,"16","17","18","19","20"
                                ,"21","22","23","24","25"
                                ,"26","27","28","29","30","31"}; //I'm aware don't worry, I just don't know what to do about it yet
                spawnSpinner(days, R.id.billInitDay);
                TextView freq = new TextView(this);
                freq.setText("Frequency");
                freq.setId(R.id.billInitTitle);
                containerLayout.addView(freq);
                String[] opts = {"Weekly", "Monthly", "Annually"};
                spawnSpinner(opts, R.id.billDue);
                break;
            default:
                spawnEditText("Name", R.id.textItem);
                break;
        }
    }

    protected void spawnEditText (String name, int id) {
        if (containerLayout.findViewById(id) != null) {
            containerLayout.removeView(containerLayout.findViewById(id)); // haha
        }
        final EditText editText = new EditText(getBaseContext());
        containerLayout.addView(editText);
        LinearLayout.LayoutParams textParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
        textParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        textParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        editText.setLayoutParams(textParams);
        editText.setTag(name);
        editText.setId(id);
        editText.setHint(name + "..");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                editText.setTextColor(Color.parseColor("#ff000000"));
            }
        });
    }

    protected void spawnSpinner(String[] options, int id) {
        if (containerLayout.findViewById(id) != null) {
            containerLayout.removeView(containerLayout.findViewById(id)); // haha
        }
        Spinner spinner = new Spinner(getBaseContext());
        spinner.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,options));
        containerLayout.addView(spinner);
        Spinner.LayoutParams spinnerParams = spinner.getLayoutParams();
        spinnerParams.width = Spinner.LayoutParams.MATCH_PARENT;
        spinnerParams.height = Spinner.LayoutParams.WRAP_CONTENT;
        spinner.setId(id);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
