package com.example.lab6anaissalvador.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.lab6anaissalvador.MainActivity;
import com.example.lab6anaissalvador.R;
import com.example.lab6anaissalvador.databinding.FragmentSecondBinding;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSecondBinding.inflate(inflater,container,false);

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
                    Fragment outcomeFragment = new OutComeFragment();
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    db.collection("outcome")
                            .document(userId)
                            .update(
                                    "tittle", updatedTittle,
                                    "description", updatedDescription,
                                    "amount", updatedAmountDouble2
                            )
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(getContext(), "Actualización exitosa", Toast.LENGTH_SHORT).show();
                                if (getContext() instanceof MainActivity){
                                    ((MainActivity) getContext()).replaceFragment(outcomeFragment);
                                }
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getContext(), "Error al actualizar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
            });
            binding.back.setOnClickListener(v -> {
                if (getContext() instanceof MainActivity){
                    Fragment outcomeFragment = new OutComeFragment();
                    ((MainActivity) getContext()).replaceFragment(outcomeFragment);
                }
            });

        }

        return binding.getRoot();
    }

}