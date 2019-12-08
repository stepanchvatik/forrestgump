package com.example.forrestgump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class Store extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    int score;
    private ListView itemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        score = pref.getInt("score",-1);
        TextView score_label = findViewById(R.id.score);
        score_label.setText(String.valueOf(score));
        itemsList = (ListView) findViewById(R.id.itemsList);
        databaseHelper = new DatabaseHelper(this);
        populateItems();



    }

    private void populateItems(){
        View r = getLayoutInflater().inflate(R.layout.single_item,null,true);
        EntryHolder holder = new EntryHolder(r);
        r.setTag(holder);


        Cursor data = databaseHelper.getItems();
        ArrayList<String> listData = new ArrayList<>();
        String[] names = new String[2];
        String[] counts =new String[2];
        String[] prices =new String[2];
        int[] images = new int[2];
        while(data.moveToNext()){
            names[0] = "Armor";
            names[1] = "Boots";
            images[0] = R.drawable.armor;
            images[1] = R.drawable.boots;
            counts[0] = data.getString(0);
            counts[1] = data.getString(1);
            prices[0] = String.valueOf(Integer.parseInt(counts[0])*2000);
            prices[1] = String.valueOf(Integer.parseInt(counts[1])*2000);
        }
        CustomListView customListView = new CustomListView(score,this,names,counts,prices,images);
       // ListAdapter adapter = new ArrayAdapter<>(this,R.layout.single_item,listData);
        itemsList.setAdapter(customListView);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
/*

    int layoutResourceId;
    List<Item> data = new ArrayList<Item>();

        EntryHolder holder = new EntryHolder();

            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
          //  holder = new EntryHolder();
            holder.count = (TextView)row.findViewById(R.id.count);
            holder.name = (TextView)row.findViewById(R.id.name);
            holder.price = (TextView)row.findViewById(R.id.price);
            holder.logo = (ImageView)row.findViewById(R.id.itemimage);

            row.setTag(holder);

        final Item entry = data.get(position);
        holder.price.setText(entry.cost);
        holder.count.setText(entry.level);
        holder.name.setText(entry.name);


      //  holder.logo.setImageResource();



        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"BUY",Toast.LENGTH_LONG).show();
            }
        });
*/


    static class EntryHolder {
        ImageView logo;
        TextView name;
        TextView price;
        TextView count;
        EntryHolder(View v){
            logo = v.findViewById(R.id.itemImage);
            name = v.findViewById(R.id.itemName);
            price = v.findViewById(R.id.itemPrice);
            count = v.findViewById(R.id.itemCount);
        }
    }









}
