package com.example.lab6anaissalvador.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.lab6anaissalvador.Adapter.OutComeAdapter;
import com.example.lab6anaissalvador.Entity.OutCome;
import com.example.lab6anaissalvador.OutComeActivity;
import com.example.lab6anaissalvador.R;
import com.example.lab6anaissalvador.databinding.FragmentOutcomeBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;
public class OutComeFragment extends Fragment {
    private FragmentOutcomeBinding binding;
    List<OutCome> outComeList;
    OutComeAdapter outComeAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentOutcomeBinding.inflate(inflater,container,false);
        outComeList = new ArrayList<>();
        outComeAdapter = new OutComeAdapter(getContext(),outComeList);
        binding.outcomeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.outcomeRecyclerView.setAdapter(outComeAdapter);
        //llamado a bd
        FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
        String userId = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("outcome")
                .whereEqualTo("userId", userId)
                .orderBy("date", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()){
                            OutCome outcome = document.toObject(OutCome.class);
                            if (outcome != null){
                                outcome.setUserId(document.getId());
                                outComeList.add(outcome);
                            }
                        }
                        Log.d("msg-test", "Se mandó la lista");
                        outComeAdapter.notifyDataSetChanged();
                    }else {
                        Log.e("msg-test","Error getting documents: ", task.getException());
                    }
                });

        //botón flotante para ir a Nuevo egreso
        FloatingActionButton floatingAdd = binding.getRoot().findViewById(R.id.floatingAdd1);
        floatingAdd.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OutComeActivity.class); // Reemplaza con tu actividad
            startActivity(intent);
        });
        return binding.getRoot();
    }
}
