package com.example.caterfoodproject.user;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    String userN;
    String firstName;
    List<Restaurant> restaurantList;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Bundle bundle = getIntent().getExtras();
        userN = bundle.getString("userEmail");
        firstName = bundle.getString("userName");

        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_view, new HomeFragment()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // set Fragmentclass Arguments

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new HomeFragment();
                        //fragment.setArguments(bundle1);
                        break;
                    case R.id.nav_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.nav_orders:
                        fragment = new OrderFragment();
                        break;
                    case R.id.nav_cart:
                        fragment = new CartFragment();
                        break;
                    case R.id.nav_account:
                        fragment = new AccountFragment();
                        break;

                }
                getSupportFragmentManager().beginTransaction().replace(R.id.home_view, fragment).commit();
                return true;
            }
        });

    }
    public Bundle getMyData(){
        Bundle hm = new Bundle();
        hm.putString("userEmail",userN);
        hm.putString("userName",firstName);
        return hm;
    }
}
