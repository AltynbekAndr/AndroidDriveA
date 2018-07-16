package com.example.kurban.androidriderapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences = null;
    SharedPreferences.Editor editor = null;
    JSONArray jsonArrayFromShared = null;
    JSONObject jsonObjectItemFromJsonArray = null;
    String strJsonArrayFromShared = null;
    List<String> phone_list = null;
    List<String> address_list = null;
    List<String> phone_list_Tolko_Perevernutyi = null;
    List<String> address_list_Tolko_Perevernutyi = null;

    ListView listView = null;
    CustomArrayAdapter customArrayAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("История");
        listView = (ListView) findViewById(R.id.listView);
        sharedPreferences = getSharedPreferences("ALFA_TAXI", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        strJsonArrayFromShared = sharedPreferences.getString("JSONARR","");
        if(!strJsonArrayFromShared.equals("")){
            phone_list = new ArrayList<>();
            address_list = new ArrayList<>();
            phone_list_Tolko_Perevernutyi = new ArrayList<>();
            address_list_Tolko_Perevernutyi = new ArrayList<>();
            try {
                jsonArrayFromShared = new JSONArray(strJsonArrayFromShared);
                for(int i =0;i<jsonArrayFromShared.length();i++){
                    jsonObjectItemFromJsonArray = jsonArrayFromShared.getJSONObject(i);
                    phone_list.add(jsonObjectItemFromJsonArray.get("phone").toString());
                    address_list.add(jsonObjectItemFromJsonArray.get("address").toString());
                }
                for(int i =phone_list.size();i>0;i--){
                    phone_list_Tolko_Perevernutyi.add(phone_list.get(i-1));
                    address_list_Tolko_Perevernutyi.add(address_list.get(i-1));
                }
                customArrayAdapter = new CustomArrayAdapter(HistoryActivity.this,address_list_Tolko_Perevernutyi);
                listView.setAdapter(customArrayAdapter);
            } catch (JSONException e) {}
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HistoryActivity.this,ZakazActivity.class);
                startActivity(intent);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HistoryActivity.this,ZakazActivity.class);
                intent.putExtra("address",address_list_Tolko_Perevernutyi.get(i));
                intent.putExtra("phone",phone_list_Tolko_Perevernutyi.get(i));
                startActivity(intent);
            }
        });
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
}
