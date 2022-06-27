package com.example.caterfoodproject.user;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddCartActivity extends AppCompatActivity {
    TextView tv;
    String n;
    String resName;
    String price;
    String resNameFromDb;
    Boolean isValid = true;
    Button minus;
    Button add;
    Button cancel;
    Button addToCart;
    EditText value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtocart);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv = findViewById(R.id.productName);
        minus = findViewById(R.id.minus);
        add = findViewById(R.id.add);
        cancel = findViewById(R.id.cancel);
        addToCart = findViewById(R.id.addToCart);
        value = findViewById(R.id.quantity);


        Bundle bundle = getIntent().getExtras();
        n = bundle.getString("name");
        price = bundle.getString("price");
        resName = bundle.getString("resName");
        tv.setText(n);
        Log.d("price", price);
    }

    public void subtractQuantity(View view) {
        int quantity = Integer.parseInt(value.getText().toString());
        Log.d("quantity", String.valueOf(quantity));
        quantity--;
        value.setText(String.valueOf(quantity));
    }

    public void addQuantity(View view) {
        int quantity = Integer.parseInt(value.getText().toString());
        quantity++;
        value.setText(String.valueOf(quantity));
    }

    public void cancelButton(View view) {

        goToSelectActivity(resName);
    }

    public void addButton(View view) {
        String productName = tv.getText().toString();
        String productPrice = price;
        String quantity = value.getText().toString();
        String restaurantName = resName;
        double price = Double.parseDouble(productPrice) * Double.parseDouble(quantity);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("cart");

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("name", dataSnapshot.child("name").getValue(String.class));
                for (DataSnapshot res : dataSnapshot.getChildren()) {
                    resNameFromDb = res.child("restaurantName").getValue(String.class);
                }

                if (restaurantName.equals(resNameFromDb) || resNameFromDb == null) {
                    String key = myRef.push().getKey();
                    myRef.child(key).setValue(new Cart(productName, productPrice, quantity, restaurantName))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddCartActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                                    goToSelectActivity(restaurantName);
                                }
                            });

                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddCartActivity.this);
                    builder.setCancelable(true);
                    builder.setTitle("Title");
                    builder.setMessage("Your cart contains an item from " + resNameFromDb + ". Would you like to clear the cart and add a new one");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Log.d("restaurant name", restaurantName);
                                                String key = myRef.push().getKey();
                                                myRef.child(key).setValue(new Cart(productName, productPrice, quantity, restaurantName))
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused) {
                                                                Toast.makeText(AddCartActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                                                                goToSelectActivity(restaurantName);
                                                            }
                                                        });
                                            }
                                        }
                                    });
                                }
                            });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void goToSelectActivity(String resNameFromDb) {
        AddCartActivity.this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }
}
