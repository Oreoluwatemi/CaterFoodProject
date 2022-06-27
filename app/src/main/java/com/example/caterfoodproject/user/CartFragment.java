package com.example.caterfoodproject.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Cart;
import com.example.caterfoodproject.model.CartAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {
String productName;
String productPrice;
String productQuantity;
String restaurantName;
String userEmail;
String userName;
ArrayList<Cart> cartList = new ArrayList<>();
ListView listView;
double price = 0;
Button button;
Date date = new Date();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
                HomeActivity activity = (HomeActivity)getActivity();
        Bundle results = activity.getMyData();
        userEmail = results.getString("userEmail");
        userName = results.getString("userName");
        Log.d("restaurant email", userEmail);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("cart");
        button = (Button) rootView.findViewById(R.id.buy);
        TextView tv = rootView.findViewById(R.id.cartEmpty);

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.d("name", dataSnapshot.child("name").getValue(String.class));
                for(DataSnapshot res : dataSnapshot.getChildren()){

                    productName = res.child("name").getValue(String.class);
                    productQuantity = res.child("quantity").getValue(String.class);
                    productPrice = res.child("price").getValue(String.class);
                    restaurantName = res.child("restaurantName").getValue(String.class);
                    Log.d("price1", productPrice);
                    price += (Double.parseDouble(productPrice) * Double.parseDouble(productQuantity));
                    cartList.add(new Cart(productName, productPrice, productQuantity, restaurantName));

                }
                if(cartList.size() != 0){
                    button.setVisibility(View.VISIBLE);
                    button.setText("Proceed to checkout (" + String.valueOf(price)+ ")");
                    tv.setText("");
                }
                else{
                    tv.setText("Cart Empty");
                }
                if(getActivity() != null) {
                    CartAdapter androidListAdapter = new CartAdapter(getActivity(), cartList);
                    listView = (ListView) rootView.findViewById(R.id.cart_list);
                    listView.setAdapter(androidListAdapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckoutActivity.class);
                startActivityForResult(intent, 1000);
            }

        });

        // Inflate the layout for this fragment
        return rootView;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode==2)
        {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference resRef = database.getReference().child("cart");
            DatabaseReference myRef = database.getReference().child("order");

            String key = myRef.push().getKey();
            myRef.child(key).child("name").setValue(userName);
            myRef.child(key).child("email").setValue(userEmail);
            myRef.child(key).child("orderDate").setValue(date.toString());
            myRef.child(key).child("restaurantName").setValue(restaurantName);
            for(int i = 0; i < cartList.size(); i++){
                Log.d("price1", cartList.get(i).price);
                myRef.child(key).child("product_order").child(String.valueOf(i)).child("name").setValue(cartList.get(i).name);
                myRef.child(key).child("product_order").child(String.valueOf(i)).child("quantity").setValue(cartList.get(i).quantity);
                myRef.child(key).child("product_order").child(String.valueOf(i)).child("price").setValue(cartList.get(i).price);
            }
            Toast.makeText(getActivity(), "Congrats! you have placed your order.", Toast.LENGTH_SHORT).show();

            resRef.removeValue();
            listView.setAdapter(null);
            button.setVisibility(View.INVISIBLE);
        }
    }
}
