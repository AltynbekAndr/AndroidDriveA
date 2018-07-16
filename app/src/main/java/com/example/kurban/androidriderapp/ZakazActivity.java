package com.example.kurban.androidriderapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ZakazActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    EditText input_phone = null;
    EditText input_adres = null;
    String phone = null;
    String address = null;
    JSONArray jsonArrayToShared = null;
    String jsonArrayFromShared = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zakaz);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Заказ такси");
        sharedPreferences = getSharedPreferences("ALFA_TAXI", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        input_phone = (EditText) findViewById(R.id.input_phone);
        input_adres = (EditText) findViewById(R.id.input_adres);
        try {
            String intentPhone = getIntent().getStringExtra("phone");
            String intentAddress = getIntent().getStringExtra("address");
            input_phone.setText(intentPhone);
            input_adres.setText(intentAddress);
        }catch (Exception e){}
        Button btn_orders = (Button) findViewById(R.id.btn_orders);
        btn_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hiddenKeyword();
                phone = input_phone.getText().toString();
                address = input_adres.getText().toString();
                if((phone!=null)&&(!phone.equals(""))&&(phone.length()>8)){
                    if((address!=null)&&(!address.equals(""))){
                         if(isOnline()){
                             new MyAsynTask().execute();
                         }else{
                             Toast.makeText(ZakazActivity.this,"Включите моб.интернет или WI-FI!",Toast.LENGTH_LONG).show();
                         }
                    }else{
                        Toast.makeText(ZakazActivity.this,"Слишком короткий адрес введен не правильно!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(ZakazActivity.this,"Номер телефона введен не правильно!",Toast.LENGTH_LONG).show();
                }
            }
        });
        jsonArrayFromShared = sharedPreferences.getString("JSONARR","");
        if(!jsonArrayFromShared.equals("")){
            try {
                jsonArrayToShared = new JSONArray(jsonArrayFromShared);
            } catch (JSONException e) {}
        }else{
                jsonArrayToShared = new JSONArray();
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
    public void hiddenKeyword() {
        View vi = this.getCurrentFocus();
        if(vi!=null){
            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(vi.getWindowToken(),0);
        }
    }
    class MyAsynTask extends AsyncTask<Void, Void, Void> {
        OkHttpClient client = null;
        Response response = null;
        Request request = null;
        private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(ZakazActivity.this);
            dialog.setMessage("Отправка заказа...");
            dialog.setCancelable(false);
            dialog.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {

            client = OkHttpUtils.getInstance();
            JSONObject obb = new JSONObject();

            try {

                obb.put("address", address);
                obb.put("phone", phone);
                int lengthJsonArrayToShared = jsonArrayToShared.length();
                if(lengthJsonArrayToShared==0){
                    jsonArrayToShared.put(0,obb);
                }else{
                    jsonArrayToShared.put(lengthJsonArrayToShared,obb);
                }
            } catch (JSONException e) {

            }
            editor.putString("JSONARR",jsonArrayToShared.toString());
            editor.apply();
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), obb.toString());
            request = new Request.Builder()
                    .url("http://alfataxi.kg/request.php")
                    .post(body)
                    .build();
            try {
                response = client.newCall(request).execute();
                Log.e("Информация о Запросе", response.toString());
                Log.e("Ответ Из Сервера", response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            Toast.makeText(ZakazActivity.this,"Заявка отправлена.\nПожалуйста ожидайте свое такси",Toast.LENGTH_LONG).show();
            onSupportNavigateUp();
        }
    }
}
