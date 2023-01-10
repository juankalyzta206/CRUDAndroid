package id.ac.budiluhur.labict.baranghilang;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static id.ac.budiluhur.labict.baranghilang.R.id.id;
import static id.ac.budiluhur.labict.baranghilang.R.id.tgl_msk;


public class TampilSemuaBrg extends AppCompatActivity implements ListView.OnItemClickListener{

    private ListView listView;

    private String JSON_STRING;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_semua_brg);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(this);
        getJSON();
    }


    private void showItem(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(konfigurasi.TAG_ID);
                String nmbrg = jo.getString(konfigurasi.TAG_NAMA);
                String desk = jo.getString(konfigurasi.TAG_DEKSRIPSI);
                String lab = jo.getString(konfigurasi.TAG_LAB);
                String kategori = jo.getString(konfigurasi.TAG_KATEGORI);
                String tgl_msk = jo.getString(konfigurasi.TAG_TANGGAL);
                String nim_ast = jo.getString(konfigurasi.TAG_NIMAST);
                String nama_ast = jo.getString(konfigurasi.TAG_ASISTEN);




                HashMap<String,String> items = new HashMap<>();
                items.put(konfigurasi.TAG_ID,id);
                items.put(konfigurasi.TAG_NAMA,nmbrg);
                items.put(konfigurasi.TAG_DEKSRIPSI,desk);
                items.put(konfigurasi.TAG_KATEGORI,kategori);
                items.put(konfigurasi.TAG_LAB,lab);
                items.put(konfigurasi.TAG_TANGGAL,tgl_msk);
                items.put(konfigurasi.TAG_NIMAST,nim_ast);
                items.put(konfigurasi.TAG_ASISTEN,nama_ast);



                list.add(items);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ListAdapter adapter = new SimpleAdapter(
                TampilSemuaBrg.this, list, R.layout.list_item,
                new String[]{konfigurasi.TAG_ID,konfigurasi.TAG_NAMA ,konfigurasi.TAG_DEKSRIPSI, konfigurasi.TAG_LAB, konfigurasi.TAG_KATEGORI, konfigurasi.TAG_TANGGAL, konfigurasi.TAG_NIMAST, konfigurasi.TAG_ASISTEN},
                new int[]{R.id.id, R.id.nama, R.id.deskripsi, R.id.lab, R.id.kategori, R.id.tgl_msk, R.id.nim_ast, R.id.nama_ast});

        listView.setAdapter(adapter);
    }

    private void getJSON(){
        class GetJSON extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(TampilSemuaBrg.this,"Mengambil Data","Mohon Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                JSON_STRING = s;
                showItem();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequest(konfigurasi.URL_GET_ALL);
                return s;
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, tampilBarang.class);
        HashMap<String,String> map =(HashMap)parent.getItemAtPosition(position);
        String brgId = map.get(konfigurasi.TAG_ID).toString();
        intent.putExtra(konfigurasi.BRG_ID,brgId);
        startActivity(intent);
    }
}