package com.example.caterfoodproject.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import java.io.Serializable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.caterfoodproject.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CheckoutActivity extends AppCompatActivity {
    Button button;
    EditText et1, et2, et3, et4, et5, et6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        button = (Button) findViewById(R.id.pay);
        et1 = findViewById(R.id.cardName);
        et2 = findViewById(R.id.cardNumber);
        et3 = findViewById(R.id.expMonth);
        et4 = findViewById(R.id.expYear);
        et5 = findViewById(R.id.cvc);
        et6 = findViewById(R.id.zipCode);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item);
    }

    public void pay(View view) {
        String cardName = et1.getText().toString();
        String cardNumber = et2.getText().toString();
        String expMonth = et3.getText().toString();
        String expYear = et4.getText().toString();
        String cvc = et5.getText().toString();
        String zipCode = et6.getText().toString();
        Long cardN = 0L;
        Calendar calendar = Calendar.getInstance();

        Boolean isValid = true;
        if(cardName.isEmpty()){
            et1.setError("Field Missing!");
            isValid = false;
        }
        if(cardNumber.isEmpty()){
            et2.setError("Field Missing!");
            isValid = false;
        }
        else{
            cardN = Long.parseLong(cardNumber);
            if(checkCard(cardN) == false){
                et2.setError("Invalid Card Number!");
                isValid = false;
            }
        }

        if(expMonth.isEmpty()){
            et3.setError("Field Missing!");
            isValid = false;
        }
        else if(Integer.parseInt(expMonth) < 1 || Integer.parseInt(expMonth) > 12){
            et3.setError("Invalid Month");
            isValid = false;
        }
        if(expYear.isEmpty()){
            et4.setError("Field Missing!");
            isValid = false;
        }
        else if(Integer.parseInt(expYear) < calendar.get(Calendar.YEAR)){
            Log.d("date", String.valueOf(calendar.get(Calendar.YEAR)));
            et4.setError("Invalid Year!");
            isValid = false;
        }
        if(cvc.isEmpty()){
            et5.setError("Field Missing!");
            isValid = false;
        }
        else if(cvc.length() != 3){
            Log.d("cvc", String.valueOf(cvc.length()));
            et5.setError("Invalid CVC!");
            isValid = false;
        }
        if(zipCode.isEmpty()){
            et6.setError("Field Missing!");
            isValid = false;
        }

        if(isValid == true) {
            Intent intent = new Intent();
            intent.putExtra("message", "pay");
            setResult(2, intent);
            finish();//finishing activity
        }
    }

    public static boolean checkCard(long number) {
        return (getSize(number) >= 13 && getSize(number) <= 16) && (prefix(number, 4)
                || prefix(number, 5) || prefix(number, 37) || prefix(number, 6))
                && ((sumEvenNo(number) + sumOddNo(number)) % 10 == 0);
    }

    public static int sumEvenNo(long number) {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 2; i >= 0; i -= 2)
            sum += getNumber(Integer.parseInt(num.charAt(i) + "") * 2);
        return sum;
    }

    public static int getNumber(int number) {
        if (number < 9)
            return number;
        return number / 10 + number % 10;
    }

    public static int sumOddNo(long number) {
        int sum = 0;
        String num = number + "";
        for (int i = getSize(number) - 1; i >= 0; i -= 2)
            sum += Integer.parseInt(num.charAt(i) + "");
        return sum;
    }

    public static boolean prefix(long number, int s) {
        return getPrefix(number, getSize(s)) == s;
    }

    public static int getSize(long s) {
        String num = s + "";
        return num.length();
    }

    public static long getPrefix(long number, int p) {
        if (getSize(number) > p) {
            String num = number + "";
            return Long.parseLong(num.substring(0, p));
        }
        return number;
    }
}
