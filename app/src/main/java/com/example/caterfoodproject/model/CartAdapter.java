package com.example.caterfoodproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Cart;

import java.util.List;

public class CartAdapter extends ArrayAdapter<Cart> {

    public CartAdapter(Context context, List<Cart> carts) {
        super(context, 0, carts);
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Cart cart = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_cartlist, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.product_name);
        TextView quantity = (TextView) convertView.findViewById(R.id.product_quantity);
        TextView price = (TextView) convertView.findViewById(R.id.product_price);


        name.setText(cart.name);
        quantity.setText(cart.quantity);
        price.setText(cart.price);

        return convertView;
    }
}
