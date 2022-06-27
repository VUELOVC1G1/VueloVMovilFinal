package com.luna.vuelav.authui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.luna.vuelav.R;
import com.luna.vuelav.databinding.ActivityMainBinding;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_auth);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        if (!isNetworkAvailable()) {
            Toast.makeText(this, "No estás conectado a internet", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}