package com.example.caterfoodproject.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.CustomAdapter;
import com.example.caterfoodproject.model.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
List<String> allTags = new ArrayList<>();
List<Restaurant> restaurantList = new ArrayList<>();
List<Restaurant> foundList = new ArrayList<>();
EditText searchTitle;
ListView listView;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        //super.onCreateView(inflater,container,savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        HomeActivity activity = (HomeActivity)getActivity();
        Bundle results = activity.getMyData();
        String userEmail = results.getString("userEmail");
        Log.d("restaurant email", userEmail);
        searchTitle = (EditText) rootView.findViewById(R.id.search_restaurant);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference().child("restaurants");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot res : dataSnapshot.getChildren()) {
                    String name = res.child("name").getValue(String.class);
                    String style = res.child("style").getValue(String.class);
                    String location = res.child("location").getValue(String.class);
                    String img = res.child("img").getValue(String.class);
                    String desc = res.child("desc").getValue(String.class);
                    String tag = res.child("tags").getValue(String.class);
                    restaurantList.add(new Restaurant(name, style,img, desc, location, tag));
                    allTags.add(tag);
                }
                // Log.d("restaurant", restaurantList.get(0).name);

                Button button = (Button) rootView.findViewById(R.id.button_search);
                button.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        foundList.clear();
                        String tag = searchTitle.getText().toString();
                        for (int i = 0; i < allTags.size(); i++) {
                            if (allTags.get(i).toLowerCase().contains(tag.toLowerCase())) {
                                foundList.add(restaurantList.get(i));
                            }
                        }
                        CustomAdapter androidListAdapter = new CustomAdapter(getActivity(), foundList);
                        listView = (ListView) rootView.findViewById(R.id.search_list);
                        listView.setAdapter(androidListAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent, View view,
                                                    final int position, long id) {
                                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                                Intent intent1 = new Intent(getActivity(), SelectActivity.class);
                                intent1.putExtra("name", name);
                                startActivity(intent1);
                            }
                        });
                    }

                });

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
}