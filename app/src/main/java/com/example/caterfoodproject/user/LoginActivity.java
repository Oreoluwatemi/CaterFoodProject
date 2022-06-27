package com.example.caterfoodproject.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caterfoodproject.R;
import com.example.caterfoodproject.business.BHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText e;
    EditText p;
    String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String sharedProfile = "com.example.caterfoodproject";
    String emailFromDb, resNameDb;
    String userNameFromDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        e = findViewById(R.id.login_email);
        p = findViewById(R.id.login_password);
        Intent intent = getIntent();
        progressDialog = new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        SharedPreferences sharedPreferences = getSharedPreferences(sharedProfile, MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String password = sharedPreferences.getString("password", "");
        if(username != "" && password != ""){
            e.setText(username);
            p.setText(password);
        }
    }

    public void login(View view) {

        String email= e.getText().toString();
        String password =p.getText().toString();
        Boolean isValid = true;

        if(email.isEmpty()){
            e.setError("Field Empty!");
            isValid = false;
        }else if(password.isEmpty()){
            p.setError("Field Empty!");
            isValid = false;
        }
        else{
            progressDialog.setMessage("Wait while we log you in!");
            progressDialog.setTitle("Login User");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        SharedPreferences sharedPreferences = getSharedPreferences(sharedProfile, MODE_PRIVATE);
                        SharedPreferences.Editor preferencesEditor = sharedPreferences.edit();
                        preferencesEditor.putString("username", email);
                        preferencesEditor.putString("password", password);
                        preferencesEditor.apply();

                        progressDialog.dismiss();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("customer");
                        DatabaseReference getAdmin = database.getReference().child("admin");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot res : dataSnapshot.getChildren()) {
                                    userNameFromDb = res.child("firstname").getValue(String.class);
                                    emailFromDb = res.child("email").getValue(String.class);
                                    if(emailFromDb.equals(email)){
                                        goToUserHomePage(emailFromDb, userNameFromDb);
                                    }
                                    // restaurantList.add(new Restaurant(name, style, R.drawable.res1, desc, location, tag));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        getAdmin.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot res : dataSnapshot.getChildren()) {
                                    emailFromDb = res.child("email").getValue(String.class);
                                    resNameDb = res.child("restaurantName").getValue(String.class);
                                    if(emailFromDb.equals(email)){
                                        goToAdminHomePage(emailFromDb, resNameDb);
                                    }
                                    // restaurantList.add(new Restaurant(name, style, R.drawable.res1, desc, location, tag));
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void goToAdminHomePage(String e, String r) {
        Intent intent = new Intent(this, BHomeActivity.class);
        intent.putExtra("userEmail", e);
        intent.putExtra("resName", r);
        startActivity(intent);
        finish();
    }

    private void goToUserHomePage(String e, String u) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("userEmail", e);
        intent.putExtra("userName", u);
        startActivity(intent);
        finish();
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }
}
