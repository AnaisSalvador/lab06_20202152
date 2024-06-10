package com.example.lab6anaissalvador.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.lab6anaissalvador.MainActivity;
import com.example.lab6anaissalvador.R;
import com.example.lab6anaissalvador.Entity.InCome;
import com.example.lab6anaissalvador.databinding.FragmentFirstBinding;
import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFirstBinding.inflate(inflater,container,false);

        Bundle bundle = getArguments();
        if (bundle!= null){
            String userId = bundle.getString("userId");
            String tittle = bundle.getString("tittle");
            String description = bundle.getString("description");
            Double amount = bundle.getDouble("amount");
            Log.d("msg-test" , "cantidad: " + amount);
            long seconds = bundle.getLong("seconds");
            int nanoseconds = bundle.getInt("nanoseconds");
            Timestamp date = new Timestamp(seconds, nanoseconds);
            Date date1 = date.toDate();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateFormatString = dateFormat.format(date1);

            binding.tittle.getEditText().setText(tittle);
            binding.description.getEditText().setText(description);
            binding.amount.getEditText().setText(String.valueOf(amount));
            binding.date.getEditText().setText(dateFormatString);

            binding.tittle.getEditText().setEnabled(false);
            binding.date.getEditText().setEnabled(false);

            binding.update.setOnClickListener(v -> {
                String updatedTittle = binding.tittle.getEditText().getText().toString().trim();
                String updatedDescription = binding.description.getEditText().getText().toString().trim();
                String updatedAmount = binding.amount.getEditText().getText().toString().trim();

                if (updatedTittle.isEmpty() || updatedAmount.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
                } else {
                    Double updatedAmountDouble = Double.parseDouble(updatedAmount);
                    BigDecimal bd = new BigDecimal(updatedAmountDouble).setScale(2, RoundingMode.DOWN);
                    Double updatedAmountDouble2 = bd.doubleValue();

                    // Actualización
                    Fragment incomeFragment = new InComeFragment();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("income")
                            .document(userId)
                            .update(
                                    "tittle", updatedTittle,
                                    "description", updatedDescription,
                                    "amount", updatedAmountDouble2
                            )
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                                if (getContext() instanceof MainActivity){
                                    ((MainActivity) getContext()).replaceFragment(incomeFragment);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            });
            binding.back.setOnClickListener(v -> {
                if (getContext() instanceof MainActivity){
                    Fragment incomeFragment = new InComeFragment();
                    ((MainActivity) getContext()).replaceFragment(incomeFragment);
                }
            });

        }

        return binding.getRoot();
    }

}