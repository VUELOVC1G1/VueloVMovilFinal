package com.luna.vuelav;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.luna.vuelav.authui.AuthActivity;
import com.luna.vuelav.databinding.ActivityMainBinding;
import com.luna.vuelav.reserva.ReservaActivity;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private UsuarioDaoImpl usuarioDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_promotions, R.id.navigation_trips, R.id.navigation_account)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        usuarioDao = new UsuarioDaoImpl(this);

        binding.floatingActionButton.setOnClickListener(l -> toReservaActivity());

        navController.addOnDestinationChangedListener((navController1, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.navigation_account || navDestination.getId() == R.id.navigation_promotions) {
                binding.floatingActionButton.hide();
            } else {
                binding.floatingActionButton.show();
            }
        });
    }

    private void toReservaActivity() {
        startActivity(new Intent(this, ReservaActivity.class));
    }
}