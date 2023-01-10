package id.ac.budiluhur.labict.baranghilang;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;


public class tampilBarang extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextId;
    private EditText editTextNama;
    private EditText editTextDesk;
    private EditText editTextLab;
    private EditText tanggal;
    private DatePickerDialog datePickerDialog;

    private Button buttonUpdate;
    private Button buttonDelete;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tampil_barang);

        Intent intent = getIntent();

        id = intent.getStringExtra(konfigurasi.BRG_ID);

        editTextId = (EditText) findViewById(R.id.editTextId);
        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextDesk = (EditText) findViewById(R.id.editTextDesk);
        editTextLab = (EditText) findViewById(R.id.editTextLab);
        tanggal = (EditText) findViewById(R.id.date);


        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonDelete = (Button) findViewById(R.id.buttonDelete);

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        editTextId.setText(id);

        getItems();
    }

    private void getItems(){
        class GetItems extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tampilBarang.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                showItems(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_GET_BRG,id);
                return s;
            }
        }
        GetItems ge = new GetItems();
        ge.execute();
    }
    private void showItems(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(konfigurasi.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);
            String nama = c.getString(konfigurasi.TAG_NAMA);
            String desk = c.getString(konfigurasi.TAG_DEKSRIPSI);
            String lab = c.getString(konfigurasi.TAG_LAB);
            String date = c.getString(konfigurasi.TAG_TANGGAL);

            editTextNama.setText(nama);
            editTextDesk.setText(desk);
            editTextLab.setText(lab);
            tanggal.setText(date);



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void updateItems(){
        final String nama = editTextNama.getText().toString().trim();
        final String desk = editTextDesk.getText().toString().trim();
        final String lab = editTextLab.getText().toString().trim();
        final String date = tanggal.getText().toString();

        class UpdateItems extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tampilBarang.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(tampilBarang.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put(konfigurasi.KEY_BRG_ID,id);
                hashMap.put(konfigurasi.KEY_BRG_NAMA,nama);
                hashMap.put(konfigurasi.KEY_BRG_DESKRIPSI,desk);
                hashMap.put(konfigurasi.KEY_BRG_LAB,lab);
                hashMap.put(konfigurasi.KEY_BRG_TANGGAL,date);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(konfigurasi.URL_UPDATE_BRG,hashMap);

                return s;
            }
        }

        UpdateItems ue = new UpdateItems();
        ue.execute();
    }

    private void deleteItems(){
        class DeleteItems extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(tampilBarang.this, "Updating...", "Tunggu...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(tampilBarang.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s = rh.sendGetRequestParam(konfigurasi.URL_DELETE_BRG, id);
                return s;
            }
        }

        DeleteItems de = new DeleteItems();
        de.execute();
    }

    private void confirmDeleteItems(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Apakah Kamu Yakin Ingin Menghapus Barang ini?");

        alertDialogBuilder.setPositiveButton("Ya",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteItems();
                        startActivity(new Intent(tampilBarang.this,TampilSemuaBrg.class));
                    }
                });

        alertDialogBuilder.setNegativeButton("Tidak",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonUpdate){
            updateItems();
            startActivity(new Intent(tampilBarang.this,TampilSemuaBrg.class));
        }

        if(v == buttonDelete){
            confirmDeleteItems();
        }
    }
}