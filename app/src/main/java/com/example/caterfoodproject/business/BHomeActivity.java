package com.example.caterfoodproject.business;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.user.AccountFragment;
import com.example.caterfoodproject.user.CartFragment;
import com.example.caterfoodproject.user.HomeFragment;
import com.example.caterfoodproject.user.OrderFragment;
import com.example.caterfoodproject.user.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class BHomeActivity extends AppCompatActivity {
    String userN, resName;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesshome);

        Bundle bundle = getIntent().getExtras();
        userN = bundle.getString("userEmail");
        resName  = bundle.getString("resName");
        bottomNavigationView = findViewById(R.id.bottom_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.home_view, new BusinessHome()).commit();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        // set Fragmentclass Arguments

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()){
                    case R.id.nav_home:
                        fragment = new BusinessHome();
                        //fragment.setArguments(bundle1);
                        break;
                    case R.id.nav_orders:
                        fragment = new BusinessOrders();
                        break;
                    case R.id.nav_account:
                        fragment = new BusinessAccount();
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
        hm.putString("resName", resName);
        return hm;
    }
}
