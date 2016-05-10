package me.lists5.lists;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import me.lists5.lists.AppDB;
import me.lists5.lists.User;

public class UserLogin extends AppCompatActivity implements View.OnClickListener {

    Button iLogin;
    EditText iUsername, iPassword;
    AppDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        db = new AppDB(this);
        iUsername = (EditText) findViewById(R.id.iUsername);
        iPassword = (EditText) findViewById(R.id.iPassword);
        iLogin = (Button) findViewById(R.id.iLogin);

        iLogin.setOnClickListener(this);

    }

    @Override
    public void  onClick(View v) {
        switch(v.getId()) {
            case R.id.iLogin:
                if (db.authuser()) {
                    Intent newAct = new Intent(UserLogin.this, ListBase.class);
                    //Create the bundle
                    Bundle bundle = new Bundle();

                    bundle.putString("user", iUsername.getText().toString());

                    newAct.putExtras(bundle);

                    startActivity(newAct);
                }
        }
    }

}
