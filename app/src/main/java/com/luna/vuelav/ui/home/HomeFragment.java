package com.luna.vuelav.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.luna.vuelav.ContactActivity;
import com.luna.vuelav.databinding.FragmentHomeBinding;
import com.luna.vuelav.reserva.ReservaActivity;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonSearchDestine.setOnClickListener(l -> toReservaActivity());
        binding.helpButton.setOnClickListener(l -> toContactActivity());

        return root;
    }

    private void toContactActivity() {
        Intent intent = new Intent(getContext(), ContactActivity.class);
        startActivity(intent);
    }

    private void toReservaActivity() {
        Intent intent = new Intent(getContext(), ReservaActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}