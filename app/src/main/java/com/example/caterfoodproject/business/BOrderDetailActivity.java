package com.example.caterfoodproject.business;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Cart;
import com.example.caterfoodproject.model.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BOrderDetailActivity extends AppCompatActivity {
    String resName;
    String date;
    String email;
    String emailDb;
    String dateDb;
    String resNameDb;
    long no_of_prod;
    String products;
    String name;
    String price;
    String quantity;
    List<Cart> orders = new ArrayList<>();
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_businessdetail);

    getSupportActionBar().

    setDisplayHomeAsUpEnabled(true);

    Bundle bundle = getIntent().getExtras();
    resName =bundle.getString("resName");
    date =bundle.getString("date");
    email =bundle.getString("userEmail");

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference().child("order");
    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange (DataSnapshot dataSnapshot){
            for (DataSnapshot res : dataSnapshot.getChildren()) {
                emailDb = res.child("email").getValue(String.class);
                dateDb = res.child("orderDate").getValue(String.class);
                resNameDb = res.child("restaurantName").getValue(String.class);
                if (dateDb.equals(date) && resNameDb.equals(resName)) {
                    no_of_prod = res.child("product_order").getChildrenCount();
                    for (int i = 0; i < no_of_prod; i++) {
                        name = res.child("product_order").child(String.valueOf(i)).child("name").getValue(String.class);
                        price = res.child("product_order").child(String.valueOf(i)).child("price").getValue(String.class);
                        quantity = res.child("product_order").child(String.valueOf(i)).child("quantity").getValue(String.class);

                        orders.add(new Cart(name, price, quantity, resNameDb));
                    }
                }
            }

            CartAdapter androidListAdapter = new CartAdapter(BOrderDetailActivity.this, orders);
            listView = (ListView) findViewById(R.id.businessorder_detail);
            listView.setAdapter(androidListAdapter);

            Log.d("details", orders.get(0).name);
        }
        @Override
        public void onCancelled (@NonNull DatabaseError error){

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
