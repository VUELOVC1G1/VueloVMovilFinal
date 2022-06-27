package com.luna.vuelav.reserva;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.luna.vuelav.R;
import com.luna.vuelav.databinding.ActivityReservaBinding;

import java.util.Objects;

public class ReservaActivity extends AppCompatActivity {

    private NavController navController;
    private VueloViewModel vueloViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserva);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_reserva);
        navController = Objects.requireNonNull(navHostFragment).getNavController();

        vueloViewModel = new ViewModelProvider(this).get(VueloViewModel.class);

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> finish());

        if (isNetworkAvailable()) {
            Intent intent = getIntent();
            long vueloId = intent.getLongExtra("vueloId", -1);
            if (vueloId != -1) {
                vueloViewModel.setVueloId(vueloId);
                navController.navigate(R.id.action_searchFragment_to_reservaFragment);
            }
        } else {
            Toast.makeText(this, "No estás conectado a internet", Toast.LENGTH_LONG).show();
        }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public NavController getNavController() {
        return navController;
    }
}