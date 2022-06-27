package com.example.caterfoodproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Order;

import java.util.List;

public class OrderAdapter extends ArrayAdapter<Order> {
    public OrderAdapter(Context context, List<Order> orders) {
        super(context, 0, orders);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Order order = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_orderlist, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.orders);
        TextView date = (TextView) convertView.findViewById(R.id.date);
        TextView price = (TextView) convertView.findViewById(R.id.price);


        name.setText(order.name);
        date.setText(order.date);
        price.setText(order.price);

        return convertView;
    }
}
