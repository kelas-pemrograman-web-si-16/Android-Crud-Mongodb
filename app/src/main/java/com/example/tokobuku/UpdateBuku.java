package com.example.tokobuku;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tokobuku.server.AppController;
import com.example.tokobuku.server.Config_URL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateBuku extends AppCompatActivity {

    @BindView(R.id.kodebuku)
    EditText kodeBuku;
    @BindView(R.id.judulbuku)
    EditText judulBuku;
    @BindView(R.id.sinopsis)
    EditText sinopsis;
    @BindView(R.id.pengarang)
    EditText pengarang;
    @BindView(R.id.harga)
    EditText harga;

    String kodes, juduls, sinopsiss, pengarangs, hargas;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_buku);
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        Intent a    = getIntent();
        kodes       = a.getStringExtra("kode");
        juduls      = a.getStringExtra("judul");
        sinopsiss   = a.getStringExtra("sinopsis");
        pengarangs  = a.getStringExtra("pengarang");
        hargas      = a.getStringExtra("harga");
        kodeBuku.setText(kodes);
        judulBuku.setText(juduls);
        sinopsis.setText(sinopsiss);
        pengarang.setText(pengarangs);
        harga.setText(hargas);
    }

    @OnClick(R.id.submit)
    void submit(){
        final String kodebuku = kodeBuku.getText().toString();
        final String judulbuku= judulBuku.getText().toString();
        final String sinopsiss = sinopsis.getText().toString();
        final String pengarangg = pengarang.getText().toString();
        final String hargaa     = harga.getText().toString();

        if (kodebuku.isEmpty()){
            Toast.makeText(getApplicationContext(), "Kode buku tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(judulbuku.isEmpty()){
            Toast.makeText(getApplicationContext(), "Judul buku tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(sinopsiss.isEmpty()){
            Toast.makeText(getApplicationContext(), "Sinopsis tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if (pengarangg.isEmpty()){
            Toast.makeText(getApplicationContext(), "Pengarang tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else if(hargaa.isEmpty()){
            Toast.makeText(getApplicationContext(), "Harga tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else {
            updateBuku(kodebuku, judulbuku, sinopsiss, pengarangg, hargaa);
        }
    }

    @OnClick(R.id.hapus)
    void hapus(){
        final String kodebuku = kodeBuku.getText().toString();

        if (kodebuku.isEmpty()){
            Toast.makeText(getApplicationContext(), "Kode buku tidak boleh kosong", Toast.LENGTH_LONG).show();
        }else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(Html.fromHtml("<font color='#25c5da'><b>Yakin ingin Menghapus Data ini ?</b></font>"))
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            hapusBuku(kodebuku);
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.setCancelable(true);
                }
            })
                    .show();
        }
    }

    public void updateBuku(final String kodebuku, final String judulbuku, final String sinopsis, final String pengarang, final String harga){

        String tag_string_req = "req_register";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.updateBuku, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("success");

                    if(status == true){
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent a = new Intent(UpdateBuku.this, ListDataBuku.class);
                        startActivity(a);
                        finish();
                    }else {
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Rquest", "Login Error : " + error.getMessage());
                error.printStackTrace();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodebuku", kodebuku);
                params.put("judulbuku", judulbuku);
                params.put("sinopsis", sinopsis);
                params.put("pengarang", pengarang);
                params.put("harga", harga);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void hapusBuku(final String kodebuku){

        String tag_string_req = "req_register";

        pDialog.setMessage("Loading.....");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                Config_URL.hapusBuku, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Request", "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean status = jObj.getBoolean("success");

                    if(status == true){
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                        Intent a = new Intent(UpdateBuku.this, ListDataBuku.class);
                        startActivity(a);
                        finish();
                    }else {
                        String msg = jObj.getString("message");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();

                    }

                }catch (JSONException e){
                    //JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e("Rquest", "Login Error : " + error.getMessage());
                error.printStackTrace();
                hideDialog();
            }
        }){

            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("kodebuku", kodebuku);
                return params;
            }
        };

        strReq.setRetryPolicy(policy);
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
