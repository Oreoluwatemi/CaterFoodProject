package com.example.caterfoodproject.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductAdapter extends ArrayAdapter<Product>{

    public ProductAdapter(Context context, List<Product> products) {
        super(context, 0, products);
    }
        public View getView(int position, View convertView, ViewGroup parent){
        Product product = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_productlist, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.product_name);
        ImageView img = (ImageView) convertView.findViewById(R.id.product_img);
        TextView price = (TextView) convertView.findViewById(R.id.product_price);

        name.setText(product.name);
        Picasso.get().load(product.img).into(img);
        price.setText(product.price);
        return convertView;
    }
}
