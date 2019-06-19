package com.example.tokobuku;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tokobuku.session.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    public SharedPreferences prefs;
    public SessionManager session;

    @BindView(R.id.email)
    TextView emails;
    @BindView(R.id.namaLengkap)
    TextView namalengkaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        prefs = getSharedPreferences("UserDetails",
                Context.MODE_PRIVATE);
        isLogin();
    }

    @OnClick(R.id.logout)
    void keluar(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Html.fromHtml("<font color='#25c5da'><b>Yakin ingin Keluar ?</b></font>"))
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        isLogOut();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                builder.setCancelable(true);
            }
        }).show();
    }

    @OnClick(R.id.inputData)
    void inputData(){
        Intent a = new Intent(MainActivity.this, InputBuku.class);
        startActivity(a);
        finish();
    }

    @OnClick(R.id.lihatData)
    void lihatData(){
        Intent a = new Intent(MainActivity.this, ListDataBuku.class);
        startActivity(a);
        finish();
    }

    public void isLogin(){
        // Session manager
        session = new SessionManager(this);

        //Session Login
        if(session.isLoggedIn()){
            String email       = prefs.getString("email", "");
            String namalengkap = prefs.getString("namalengkap", "");
            String username    = prefs.getString("namalengkap", "");
            emails.setText(email);
            namalengkaps.setText(namalengkap);


        }else{
            Intent a = new Intent(getApplicationContext(), Login.class);
            startActivity(a);
            finish();
        }
    }

    public void isLogOut(){
        session.setLogin(false);
        session.setSkip(false);
        session.setSessid(0);
        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}