package com.example.caterfoodproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Restaurant> {

    public CustomAdapter(Context context, List<Restaurant> restaurants) {
        super(context, 0, restaurants);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Restaurant restaurant = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_list, parent, false);

        }
        TextView name = (TextView) convertView.findViewById(R.id.name);
        TextView style = (TextView) convertView.findViewById(R.id.style);
        ImageView img = (ImageView) convertView.findViewById(R.id.img);


        name.setText(restaurant.name);
        style.setText(restaurant.style);
        Picasso.get().load(restaurant.image).into(img);


        return convertView;
    }
}
