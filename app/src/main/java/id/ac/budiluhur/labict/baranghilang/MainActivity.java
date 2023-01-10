package id.ac.budiluhur.labict.baranghilang;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.DatePicker;


import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextNama;
    private EditText editTextDesk;
    private Spinner spinnerLab;
    private Spinner spinnerKategori;
    private EditText editTextNamaAsisten;
    private EditText editTextNimAsisten ;
    private DatePickerDialog datePickerDialog;
    private EditText date;
    private Button buttonAdd;
    private Button buttonView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNama = (EditText) findViewById(R.id.editTextNama);
        editTextDesk = (EditText) findViewById(R.id.editTextDesk);
        spinnerLab = (Spinner) findViewById(R.id.spinnerLab);
        spinnerKategori = (Spinner) findViewById(R.id.spinnerKategori);
        date = (EditText) findViewById(R.id.date);
        editTextNimAsisten = (EditText)findViewById(R.id.editTextNimAsisten);
        editTextNamaAsisten = (EditText) findViewById(R.id.editTextNamaAsisten);
        buttonAdd = (Button) findViewById(R.id.buttonAdd);
        buttonView = (Button) findViewById(R.id.buttonView);

        //Setting listeners to button
        buttonAdd.setOnClickListener(this);
        buttonView.setOnClickListener(this);

        date = (EditText) findViewById(R.id.date);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                date.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
    }


    //Dibawah ini merupakan perintah untuk Menambahkan Data Barang Hilang (CREATE)
    private void addItem(){

        final String nama = editTextNama.getText().toString();
        final String desk = editTextDesk.getText().toString();
        final String lab = spinnerLab.getSelectedItem().toString();
        final String kategori = spinnerKategori.getSelectedItem().toString();
        final String nim_ast = editTextNimAsisten.getText().toString().trim();
        final String nama_ast = editTextNamaAsisten.getText().toString();
        final String tgl_msk = date.getText().toString();

        class AddItem extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MainActivity.this,"Menambahkan...","Tunggu...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(MainActivity.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put(konfigurasi.KEY_BRG_NAMA,nama);
                params.put(konfigurasi.KEY_BRG_DESKRIPSI,desk);
                params.put(konfigurasi.KEY_BRG_LAB,lab);
                params.put(konfigurasi.KEY_BRG_KATEGORI,kategori);
                params.put(konfigurasi.KEY_BRG_NIMAST,nim_ast);
                params.put(konfigurasi.KEY_BRG_ASISTEN,nama_ast);
                params.put(konfigurasi.KEY_BRG_TANGGAL,tgl_msk);


                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(konfigurasi.URL_ADD, params);
                return res;
            }
        }

        AddItem ae = new AddItem();
        ae.execute();
    }

    @Override
    public void onClick(View v) {
        if(v == buttonAdd){
           if(editTextNama.getText().toString().equals("")){
               editTextNama.setError("Harap isi Nama Barang");
           }if(editTextDesk.getText().toString().equals("")){
               editTextDesk.setError("Harap Isi Field Ini");
            }if(editTextNimAsisten.getText().toString().equals("")){
                editTextNimAsisten.setError("Harap Isi Field Ini");
            }if(editTextNamaAsisten.getText().toString().equals("")){
                editTextNama.setError("Harap Isi Field Ini");
            }
            else{
            addItem();
        }}

        if(v == buttonView){
            startActivity(new Intent(this,TampilSemuaBrg.class));
        }
    }
}