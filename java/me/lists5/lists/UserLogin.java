package me.lists5.lists;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    Button iLogin;
    EditText iUsername, iPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        iUsername = (EditText) findViewById(R.id.iUsername);
        iPassword = (EditText) findViewById(R.id.iPassword);
        iLogin = (Button) findViewById(R.id.iLogin);

        iLogin.setOnClickListener(this);

    }

    @Override
    public void  onClick(View v) {
        switch(v.getId()) {
            case R.id.iLogin:

                break;
        }
    }

}
