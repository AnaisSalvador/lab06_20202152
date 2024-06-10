package com.example.lab6anaissalvador;
import com.example.lab6anaissalvador.*;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6anaissalvador.Entity.InCome;
import com.example.lab6anaissalvador.databinding.ItemIncomeBinding;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class InComeActivity extends AppCompatActivity{
    private ItemIncomeBinding binding;
    Timestamp timestamp;
    FirebaseFirestore db;
    String dateCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ItemIncomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.btn1.setOnClickListener(view -> {
            Intent intent = new Intent(InComeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
        binding.btn2.setOnClickListener(v -> {
            String tittle = binding.tittle.getText().toString().trim();
            String description = binding.description.getText().toString().trim();
            if (description.isEmpty()) {
                description = " ";
            }
            String amountText = binding.amount.getText().toString().trim();
            if (tittle.isEmpty() || amountText.isEmpty() ||  dateCalendar == null || dateCalendar.isEmpty()){
                Toast.makeText(InComeActivity.this, "Completar", Toast.LENGTH_SHORT).show();
            }else {
                Double amount = Double.parseDouble(amountText);
                BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
                amount = bd.doubleValue();
                InCome income = new InCome();
                income.setTittle(tittle);
                income.setAmount(amount);
                income.setDescription(description);
                long currentTimeMillis = System.currentTimeMillis();
                Date currentDate = new Date(currentTimeMillis);
                Timestamp timestampCurrent = new Timestamp(currentDate);
                income.setDate(timestamp);
                FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
                String userid = user.getUid();
                income.setUserId(userid);
                long seconds = timestampCurrent.getSeconds();
                String timestampString = String.valueOf(seconds);
                db = FirebaseFirestore.getInstance();
                db.collection("inCome")
                        .document(timestampString)
                        .set(income)
                        .addOnSuccessListener(unused -> {
                            Log. d("msg-test" ,"Guardado");
                        })
                        .addOnFailureListener(e -> e.printStackTrace());

                Intent intent = new Intent(InComeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(this, "Guardado", Toast.LENGTH_LONG).show();
                startActivity(intent);
                finish();
            }


        });
    }

    public void openCalendar (View view){
        Calendar calendar = Calendar.getInstance();
        int yearCalendar = calendar.get(Calendar.YEAR);
        int monthCalendar = calendar.get(Calendar.MONTH);
        int dayCalendar = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(InComeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateCalendar = dayOfMonth + "/" + (month+1) + "/" + year;
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                Date date = selectedDate.getTime();
                timestamp = new Timestamp(date);
            }
        }, yearCalendar, monthCalendar, dayCalendar);
        datePickerDialog.show();
    }
}
