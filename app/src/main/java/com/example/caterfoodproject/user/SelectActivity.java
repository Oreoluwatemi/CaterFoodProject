package com.example.caterfoodproject.user;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Product;
import com.example.caterfoodproject.model.ProductAdapter;
import com.example.caterfoodproject.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SelectActivity extends AppCompatActivity {
    TextView tv;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    ImageView iv;
    String n;
    String style;
    String location;
    String desc;
    String img;
    String name;
    String productImg;
    String productName;
    String productPrice;
    String tag;
    List<Restaurant> restaurantList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectedrestaurant);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv = findViewById(R.id.select_name);
        tv1 = findViewById(R.id.select_style);
        tv2 = findViewById(R.id.select_desc);
        tv3 = findViewById(R.id.select_location);
        iv = findViewById(R.id.select_img);


        Bundle bundle = getIntent().getExtras();
        n = bundle.getString("name");
        Log.d("resName", n);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("restaurants");
        DatabaseReference resProducts = database.getReference().child("restaurantName").child(n);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.child("restaurantOne").child("name").getValue(String.class);
                for (DataSnapshot res : dataSnapshot.getChildren()) {

                    name = res.child("name").getValue(String.class);
                    style = res.child("style").getValue(String.class);
                    location = res.child("location").getValue(String.class);
                    img = res.child("img").getValue(String.class);
                    desc = res.child("desc").getValue(String.class);
                    tag = res.child("tags").getValue(String.class);
                   // Log.d("restaurant name", name);
                    restaurantList.add(new Restaurant(name, style, img, desc, location, tag));

                }
               // Log.d("restaurant", restaurantList.get(0).name);
                for(int i = 0; i < restaurantList.size(); i++){
                    if (restaurantList.get(i).name.equals(n)){
                        tv.setText(restaurantList.get(i).name);
                        tv1.setText(restaurantList.get(i).style);
                        tv2.setText(restaurantList.get(i).description);
                        tv3.setText("Location: " + restaurantList.get(i).location);
                        Picasso.get().load(restaurantList.get(i).image).into(iv);
                        break;
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        resProducts.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot res : dataSnapshot.getChildren()){
                    productName = res.child("name").getValue(String.class);
                    productImg = res.child("img").getValue(String.class);
                    productPrice = res.child("price").getValue(String.class);
                    productList.add(new Product(productName, productImg, productPrice));
                }
                ProductAdapter androidListAdapter = new  ProductAdapter(SelectActivity.this, productList);
                listView = (ListView) findViewById(R.id.product_list);
                listView.setAdapter(androidListAdapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        String pName = ((TextView) view.findViewById(R.id.product_name)).getText().toString();
                        String pPrice = ((TextView) view.findViewById(R.id.product_price)).getText().toString();
                        Intent intent1 = new Intent(SelectActivity.this, AddCartActivity.class);
                        intent1.putExtra("name", pName );
                        intent1.putExtra("price", pPrice );
                        intent1.putExtra("resName", tv.getText().toString() );
                        startActivity(intent1);
                        // startIntent(name);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
