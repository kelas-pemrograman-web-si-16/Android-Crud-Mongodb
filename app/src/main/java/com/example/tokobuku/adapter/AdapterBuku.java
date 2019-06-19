package com.example.tokobuku.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.tokobuku.R;
import com.example.tokobuku.data.DataBuku;

import java.util.List;

public class AdapterBuku extends BaseAdapter {

    private Activity activity;
    private LayoutInflater inflater;
    private List<DataBuku> item;

    public AdapterBuku(Activity activity, List<DataBuku> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int location) {
        return item.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_buku, null);


        TextView kode         = (TextView) convertView.findViewById(R.id.txtKode);
        TextView judul        = (TextView) convertView.findViewById(R.id.txtJudul);
        TextView sinopsis     = (TextView) convertView.findViewById(R.id.txtSinopsis);
        TextView pengarang    = (TextView) convertView.findViewById(R.id.txtpengarang);
        TextView harga        = (TextView) convertView.findViewById(R.id.txtHarga);

        kode.setText(": "+item.get(position).getKodeBuku());
        judul.setText(": "+item.get(position).getJudulBuku());
        sinopsis.setText(": "+item.get(position).getSinopsis());
        pengarang.setText(": "+item.get(position).getPengarang());
        harga.setText(": "+item.get(position).getHarga());

        return convertView;
    }
}
