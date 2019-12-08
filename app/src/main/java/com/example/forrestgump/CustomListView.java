package com.example.forrestgump;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class CustomListView extends ArrayAdapter<String> {

    private DatabaseHelper dbHelper;
    private String[] names;
    private String[] counts;
    private String[] prices;
    private int[] images;
    private Activity context;
    private int score;
    SharedPreferences pref = getContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
    SharedPreferences.Editor editor = pref.edit();
    public CustomListView(int score,Activity context, String[] names, String[] counts, String[] prices, int[] images){
        super(context,R.layout.single_item,names);
        this.context=context;
        this.names = names;
        this.counts=counts;
        this.prices = prices;
        this.images = images;
        this.score = score;
        dbHelper = new DatabaseHelper(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        EntryHolder holder = null;
        if(r==null){
            LayoutInflater inflater = context.getLayoutInflater();
            r=inflater.inflate(R.layout.single_item,null,true);
            holder = new EntryHolder(r);
            r.setTag(holder);
        }else{
            holder = (EntryHolder) r.getTag();
        }
        holder.name.setText(names[position]);
        holder.count.setText("Level: " + counts[position]);
        holder.price.setText("Price: " + prices[position]);
        holder.logo.setImageResource(images[position]);
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EntryHolder h = (EntryHolder)view.getTag();
                int price=0;
                try{
                  //   price = Integer.parseInt(((TextView) h.price).getText().toString()); WTF BUG????
                    price = 10000;

                }catch(NullPointerException e) {
                                }

                int cash = score;

                if(cash>=price){
                    cash-=price;
                    int newCount = Integer.parseInt(h.count.getText().toString())+1;
                    h.count.setText("Level: " + String.valueOf(newCount));
                    h.price.setText("Price: " + String.valueOf(newCount*2000));
                    dbHelper.buy(h.name.getText().toString(),newCount);
                    editor.putInt("score", cash);
                    editor.commit();
                    TextView tmp =  view.findViewById(R.id.score);
                    tmp.setText(String.valueOf(cash));
                }else{
                    Toast toast = Toast.makeText(context,"Not enough cash",Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
        return r;
    }

    class EntryHolder {
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
