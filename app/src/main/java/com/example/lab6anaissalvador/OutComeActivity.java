package com.example.lab6anaissalvador;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lab6anaissalvador.Entity.OutCome;
import com.example.lab6anaissalvador.OutComeActivity;
import com.example.lab6anaissalvador.Entity.InCome;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
public class OutComeActivity extends AppCompatActivity{
    ActivityOutComeBinding binding;
    TextView tvCalendar;
    Timestamp timestamp;
    FirebaseFirestore db;
    String dateCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOutComeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tvCalendar = binding.tvCalendar;


        binding.back.setOnClickListener(view -> {
            Intent intent = new Intent(OutComeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });

        binding.create.setOnClickListener(v -> {
            String tittle = binding.tittle.getEditText().getText().toString().trim();
            String description = binding.description.getEditText().getText().toString().trim();
            if (description.isEmpty()) {
                description = " ";
            }
            String amountText = binding.amount.getEditText().getText().toString().trim();


            if (tittle.isEmpty() || amountText.isEmpty() ||  dateCalendar == null || dateCalendar.isEmpty()){
                Toast.makeText(OutComeActivity.this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
            }else {
                Double amount = Double.parseDouble(amountText);
                BigDecimal bd = new BigDecimal(amount).setScale(2, RoundingMode.DOWN);
                amount = bd.doubleValue();

                OutCome outcome = new OutCome();
                outcome.setTittle(tittle);
                outcome.setAmount(amount);
                outcome.setDescription(description);
                //hora actual
                long currentTimeMillis = System.currentTimeMillis();
                Date currentDate = new Date(currentTimeMillis);
                Timestamp timestampCurrent = new Timestamp(currentDate);
                outcome.setDate(timestamp);
                //id
                FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
                String userid = user.getUid();
                outcome.setUserId(userid);

                //id con la fecha
                long seconds = timestampCurrent.getSeconds();
                String timestampString = String.valueOf(seconds);

                db = FirebaseFirestore.getInstance();
                db.collection("outcome")
                        .document(timestampString)
                        .set(outcome)
                        .addOnSuccessListener(unused -> {
                            Log. d("msg-test" ,"Data guardada exitosamente ");
                        })
                        .addOnFailureListener(e -> e.printStackTrace());

                Intent intent = new Intent(OutComeActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(this, "Egreso guardado", Toast.LENGTH_LONG).show();
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

        DatePickerDialog datePickerDialog = new DatePickerDialog(NewOutcomeActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateCalendar = dayOfMonth + "/" + (month+1) + "/" + year;
                tvCalendar.setText(dateCalendar);
                tvCalendar.setVisibility(View.VISIBLE);

                // Convertir la fecha seleccionada a un objeto Date
                Calendar selectedDate = Calendar.getInstance();
                selectedDate.set(year, month, dayOfMonth);
                Date date = selectedDate.getTime();

                // Convertir el objeto Date a un Timestamp
                timestamp = new Timestamp(date);
            }
        }, yearCalendar, monthCalendar, dayCalendar);
        datePickerDialog.show();
    }
}
