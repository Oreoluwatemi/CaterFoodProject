package com.example.caterfoodproject.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.caterfoodproject.R;
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

public class RegisterActivity extends AppCompatActivity {
    EditText et1;
    EditText et2;
    EditText et3;
    EditText et4;
    EditText et5;
    EditText et6;
    String regex = "^[\\w!#$%&’*+/=?`{|}~^-]+(?:\\.[\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    ProgressDialog progressDialog;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        et1 = findViewById(R.id.login_email);
        et2 = findViewById(R.id.login_password);
        et3 = findViewById(R.id.login_cpassword);
        et4 = findViewById(R.id.login_firstName);
        et5 = findViewById(R.id.login_lastName);
        et6 = findViewById(R.id.login_phoneNumber);

        Intent intent = getIntent();
        progressDialog = new ProgressDialog(this);
        firebaseAuth=FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    public void register(View view) {
        String email = et1.getText().toString();
        String password =et2.getText().toString();
        String confirmPassword = et3.getText().toString();
        String firstName = et4.getText().toString();
        String lastName = et5.getText().toString();
        String phoneNumber = et6.getText().toString();
        Boolean isValid = true;

        if(email.isEmpty() || email == null){
            et1.setError("Field Empty!");
            isValid = false;
        }
        if(firstName.isEmpty() || firstName == null){
            et4.setError("Field Empty!");
            isValid = false;
        }
        if(lastName.isEmpty() || lastName == null){
            et5.setError("Field Empty!");
            isValid = false;
        }
        if(phoneNumber.isEmpty() || phoneNumber == null){
            et6.setError("Field Empty!");
            isValid = false;
        }
        if(password.isEmpty() || password == null){
            et2.setError("Field Empty!");
            isValid = false;
        }
        if(confirmPassword.isEmpty() || confirmPassword == null){
            et3.setError("Field Empty!");
            isValid = false;
        }
        if(password.length() <= 6){
            et2.setError("Password should be more than six characters");
            isValid = false;
        }
        if(!email.matches(regex)){
            et1.setError("Invalid Email Address");
            isValid = false;
        }
        if(!password.equals(confirmPassword)){
            et3.setError("Password and Confirm password not matching");
            isValid = false;
        }
        if(isValid == true){
            progressDialog.setMessage("Registering you!!");
            progressDialog.setTitle("Register User");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            
           firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
               @Override
               public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "You have Successfully Registered", Toast.LENGTH_SHORT).show();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference().child("customer");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String key = myRef.push().getKey();
                                myRef.child(key).child("email").setValue(email);
                                myRef.child(key).child("firstname").setValue(firstName);
                                myRef.child(key).child("lastname").setValue(lastName);
                                myRef.child(key).child("phonenumber").setValue(phoneNumber);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        goToLogin();
                    }
                    else{
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
               }
           });
            
        }
    }

    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        finish();
    }

    public void cancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        finish();
    }
}
