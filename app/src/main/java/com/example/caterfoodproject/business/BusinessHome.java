package com.example.caterfoodproject.business;

import android.os.Bundle;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Product;
import com.example.caterfoodproject.model.ProductAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BusinessHome#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BusinessHome extends Fragment {
    TextView tv;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    ImageView iv;
    String resName;
    String style;
    String location;
    String productImg;
    String productName;
    String productPrice;
    List<Product> productList = new ArrayList<>();
    String desc;
    String img;
    String name;
    String tag;
    ListView listView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BusinessHome() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BusinessHome.
     */
    // TODO: Rename and change types and number of parameters
    public static BusinessHome newInstance(String param1, String param2) {
        BusinessHome fragment = new BusinessHome();
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
        var rootView = inflater.inflate(R.layout.fragment_business_home, container, false);
        tv = rootView.findViewById(R.id.select_name);
        tv1 = rootView.findViewById(R.id.select_style);
        tv2 = rootView.findViewById(R.id.select_desc);
        tv3 = rootView.findViewById(R.id.select_location);
        iv = rootView.findViewById(R.id.select_img);

        BHomeActivity activity = (BHomeActivity)getActivity();
        Bundle results = activity.getMyData();
        String userEmail = results.getString("userEmail");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("admin");
        DatabaseReference sec = database.getReference().child("restaurants");


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot res: snapshot.getChildren()){
                    if(res.child("email").getValue(String.class).equals(userEmail)){
                        resName = res.child("restaurantName").getValue(String.class);
                        tv.setText(resName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sec.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               String rName = tv.getText().toString();
                Log.d("name", rName);
                for (DataSnapshot res: snapshot.getChildren()){
                    if(res.child("name").getValue(String.class).equals(rName)){
                        name = res.child("name").getValue(String.class);
                        style = res.child("style").getValue(String.class);
                        location = res.child("location").getValue(String.class);
                        img = res.child("img").getValue(String.class);
                        desc = res.child("desc").getValue(String.class);
                        tag = res.child("tags").getValue(String.class);

                    }
                }
                tv.setText(name);
                tv1.setText(style);
                tv2.setText(desc);
                tv3.setText(location);
                Picasso.get().load(img).into(iv);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference secName = database.getReference().child("restaurantName");
        secName.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String resNameDb = tv.getText().toString();
                Log.d("name", resNameDb);

                for(DataSnapshot res : dataSnapshot.child(resNameDb).getChildren()){
                    productName = res.child("name").getValue(String.class);
                    productImg = res.child("img").getValue(String.class);
                    productPrice = res.child("price").getValue(String.class);
                    productList.add(new Product(productName, productImg, productPrice));
                }
                ProductAdapter androidListAdapter = new  ProductAdapter(getActivity(), productList);
                listView = (ListView) rootView.findViewById(R.id.business_list);
                listView.setAdapter(androidListAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
}