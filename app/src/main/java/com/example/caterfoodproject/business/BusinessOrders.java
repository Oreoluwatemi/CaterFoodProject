package com.example.caterfoodproject.business;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Order;
import com.example.caterfoodproject.model.OrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusinessOrders#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessOrders extends Fragment {
String userEmail, rName;
    String resName;
    String price;
    String date;
    String email;
    String quantity;
    double p, q, tPrice;
    String totalPrice;
    List<Order> orderList = new ArrayList<>();
    List<Order> custOrderList = new ArrayList<>();
    ListView listView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BusinessOrders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessOrders.
     */
    // TODO: Rename and change types and number of parameters
    public static BusinessOrders newInstance(String param1, String param2) {
        BusinessOrders fragment = new BusinessOrders();
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
        // Inflate the layout for this fragment
        var rootView = inflater.inflate(R.layout.fragment_business_orders, container, false);
        BHomeActivity activity = (BHomeActivity)getActivity();
        Bundle results = activity.getMyData();
        userEmail = results.getString("userEmail");
        rName = results.getString("resName");
        TextView tv = rootView.findViewById(R.id.orderEmpty);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("order");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot res : dataSnapshot.getChildren()){
                    email = res.child("email").getValue(String.class);
                    resName = res.child("restaurantName").getValue(String.class);
                    date = res.child("orderDate").getValue(String.class);
                    Long prodCount = res.child("product_order").getChildrenCount();
                    for(int i = 0; i < prodCount; i++){
                        price = res.child("product_order").child(String.valueOf(i)).child("price").getValue(String.class);
                        quantity = res.child("product_order").child(String.valueOf(i)).child("quantity").getValue(String.class);
                        p = Double.parseDouble(price);
                        q = Double.parseDouble(quantity);
                        tPrice += (p * q);
                    }

                    orderList.add(new Order(resName, String.valueOf(tPrice), date,email));
                    tPrice = 0;
                }

                for(int i =0 ; i < orderList.size(); i++){
                    if(rName.equals(orderList.get(i).name)){
                        custOrderList.add(orderList.get(i));
                    }
                }

                if(custOrderList.size() == 0){
                    tv.setText("No order has been placed");
                }
                if(getActivity() != null) {
                    OrderAdapter androidListAdapter = new OrderAdapter(getActivity(), custOrderList);
                    listView = (ListView) rootView.findViewById(R.id.businessorder_list);
                    listView.setAdapter(androidListAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        public void onItemClick(AdapterView<?> parent, View view,
                                                final int position, long id) {
                            String rName = ((TextView) view.findViewById(R.id.orders)).getText().toString();
                            String rDate = ((TextView) view.findViewById(R.id.date)).getText().toString();
                            Intent intent1 = new Intent(getActivity(), BOrderDetailActivity.class);
                            intent1.putExtra("resName", rName);
                            intent1.putExtra("date", rDate);
                            intent1.putExtra("userEmail", userEmail);
                            startActivity(intent1);
                            // startIntent(name);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
}