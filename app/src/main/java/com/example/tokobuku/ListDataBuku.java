package com.example.tokobuku;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tokobuku.adapter.AdapterBuku;
import com.example.tokobuku.data.DataBuku;
import com.example.tokobuku.server.AppController;
import com.example.tokobuku.server.Config_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListDataBuku extends AppCompatActivity {

    ListView list;

    ArrayList<DataBuku> newsList = new ArrayList<DataBuku>();
    AdapterBuku adapter;

    int socketTimeout = 30000;
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    ProgressDialog pDialog;

    @BindView(R.id.cariBuku)
    EditText edtCari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_data_buku);
        getSupportActionBar().hide();

        ButterKnife.bind(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        list = (ListView) findViewById(R.id.array_list);
        newsList.clear();

        adapter = new AdapterBuku(ListDataBuku.this, newsList);
        list.setAdapter(adapter);
        list.setEmptyView(findViewById(R.id.textNodata));

        getDataBuku();
        searchData();
    }

    // Fungsi get JSON Mahasiswa
    private void getDataBuku() {

        pDialog.setMessage("Loading.....");
        showDialog();

        String tag_json_obj = "json_obj_req";
        StringRequest strReq = new StringRequest(Request.Method.GET,
                Config_URL.dataBuku,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Log.e("Response: ", response.toString());
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean status = jObj.getBoolean("success");

                            if(status == true){

                                String getObject = jObj.getString("message");
                                JSONArray jsonArray = new JSONArray(getObject);

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    final JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    final DataBuku news = new DataBuku();
                                    news.setKodeBuku(jsonObject.getString("kodebuku"));
                                    news.setJudulBuku(jsonObject.getString("judulbuku"));
                                    news.setSinopsis(jsonObject.getString("sinopsis"));
                                    news.setPengarang(jsonObject.getString("pengarang"));
                                    news.setHarga(jsonObject.getString("harga"));

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                            // TODO Auto-generated method stub
                                            Intent a = new Intent(ListDataBuku.this, UpdateBuku.class);
                                            a.putExtra("kode", newsList.get(position).getKodeBuku());
                                            a.putExtra("judul", newsList.get(position).getJudulBuku());
                                            a.putExtra("sinopsis", newsList.get(position).getSinopsis());
                                            a.putExtra("pengarang", newsList.get(position).getPengarang());
                                            a.putExtra("harga", newsList.get(position).getHarga());
                                            startActivity(a);
                                        }
                                    });

                                    newsList.add(news);
                                }
                            }

                        } catch (JSONException e) {
                            // JSON error
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(String.valueOf(getApplication()), "Error : " + error.getMessage());
                error.printStackTrace();
                hideDialog();
            }
        });

        AppController.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(ListDataBuku.this, MainActivity.class);
        startActivity(a);
        finish();
    }

    public void searchData(){
        edtCari.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence query, int start, int before, int count) {

                query = query.toString().toLowerCase();

                final List<DataBuku> filteredList = new ArrayList<DataBuku>();

                for (int i = 0; i < newsList.size(); i++) {

                    final String text = newsList.get(i).getJudulBuku().toLowerCase();
                    if (text.contains(query)) {

                        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                // TODO Auto-generated method stub
                                Intent a = new Intent(ListDataBuku.this, UpdateBuku.class);
                                a.putExtra("kode", filteredList.get(position).getKodeBuku());
                                a.putExtra("judul", filteredList.get(position).getJudulBuku());
                                a.putExtra("sinopsis", filteredList.get(position).getSinopsis());
                                a.putExtra("pengarang", filteredList.get(position).getPengarang());
                                a.putExtra("harga", filteredList.get(position).getHarga());
                                startActivity(a);
                            }
                        });
                        filteredList.add(newsList.get(i));
                    }
                }

                adapter = new AdapterBuku(ListDataBuku.this, filteredList);
                list.setAdapter(adapter);
            }
        });
    }
}
