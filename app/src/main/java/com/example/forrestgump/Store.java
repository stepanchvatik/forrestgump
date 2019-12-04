package com.example.forrestgump;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class Store extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        int score = pref.getInt("score",-1);
        TextView score_label = findViewById(R.id.score);
        score_label.setText(String.valueOf(score));




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
    }









}
