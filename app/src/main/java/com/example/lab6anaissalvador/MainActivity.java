package com.example.lab6anaissalvador;

import android.content.Intent;
import android.os.Bundle;

import com.example.lab6anaissalvador.Fragment.GraficaFragment;
import com.example.lab6anaissalvador.Fragment.InComeFragment;
import com.example.lab6anaissalvador.Fragment.OutComeFragment;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.lab6anaissalvador.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseUser user = FirebaseAuth. getInstance().getCurrentUser() ;
        String userId = user.getUid();
        String userName = user.getDisplayName();

        //fragmentos
        replaceFragment(new InComeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.inCome){
                replaceFragment(new InComeFragment());
            } else if (menuItem.getItemId() == R.id.outCome) {
                replaceFragment(new OutComeFragment());
            } else if (menuItem.getItemId() == R.id.grafica) {
                replaceFragment(new GraficaFragment());
            }
            return true;
        });

    }
    public void  replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.linearLayout,fragment);
        fragmentTransaction.commit();
    }


    //menú cierre de sesión
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return  true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return logOutActionBar(item);
    }

    public boolean logOutActionBar(MenuItem item){
        if (item.getItemId() == R.id.logOut) {
            Toast.makeText(this, "Sesión finalizada", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance().signOut(MainActivity.this)
                    .addOnCompleteListener(task -> {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    });
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}