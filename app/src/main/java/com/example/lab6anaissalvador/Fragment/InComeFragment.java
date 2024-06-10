package com.example.lab6anaissalvador.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lab6anaissalvador.Adapter.InComeAdapter;
import com.example.lab6anaissalvador.Entity.InCome;
import com.example.lab6anaissalvador.InComeActivity;
import com.example.lab6anaissalvador.R;
import com.example.lab6anaissalvador.databinding.FragmentIncomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
public class InComeFragment extends Fragment{
    private FragmentIncomeBinding binding;
    List<InCome> inComeList;
    InComeAdapter inComeAdapter;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle savedInstanceState){
        binding = FragmentIncomeBinding.inflate(layoutInflater,container,false);
        inComeList = new ArrayList<>();
        inComeAdapter = new InComeAdapter(getContext(),inComeList);
        binding.incomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.incomeRecyclerView.setAdapter(inComeAdapter);
        //llamado a bd
        FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("income")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            InCome income = document.toObject(InCome.class);
                            if (income != null){
                                income.setUserId(document.getId());
                                inComeList.add(income);
                            }
                        }
                        Log.d("msg-test", "Se mandó la lista");
                        inComeAdapter.notifyDataSetChanged();
                    }else {
                        Log.e("msg-test","Error getting documents: ", task.getException());
                    }
                });

        //botón flotante para ir a Nuevo Ingreso
        FloatingActionButton floatingAdd = binding.getRoot().findViewById(R.id.floatingAdd);
        floatingAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), InComeActivity.class); // Reemplaza con tu actividad
            startActivity(intent);
        });


        return binding.getRoot();
    }
}
