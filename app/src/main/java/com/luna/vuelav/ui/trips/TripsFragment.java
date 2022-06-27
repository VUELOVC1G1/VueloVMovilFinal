package com.luna.vuelav.ui.trips;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luna.vuelav.databinding.FragmentTripsBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.Pasajero;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripsFragment extends Fragment {

    private FragmentTripsBinding binding;
    private RecyclerView recyclerView;

    private TripsRVAdapter adapter;

    private ApiClient client;
    private UsuarioDaoImpl usuarioDao;

    private List<Boleto> boletos;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TripsViewModel tripsViewModel =
                new ViewModelProvider(this).get(TripsViewModel.class);

        binding = FragmentTripsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        client = ApiClient.getInstance();
        usuarioDao = new UsuarioDaoImpl(getContext());

        recyclerView = binding.recyclerPromotions;

        if (isNetworkAvailable()) {
            getPasajeroId();
        } else {
            showToast("No estás conectado a internet");
        }

        return root;
    }

    private void getPasajeroId() {
        UsuarioM m = usuarioDao.getUser();
        if (m != null) {
            loadUserInfo(m.getId());
        } else {
            showToast("Debes iniciar sesión para ver tus viajes");
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


    private void loadUserInfo(long userId) {
        client.pasajeroService().findPasajeroByUserId(userId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Pasajero> call, Response<Pasajero> response) {
                        if (response.isSuccessful()) {
                            long pasajeroId = Objects.requireNonNull(response.body()).getId();
                            getBoletos(pasajeroId);
                        } else {
                            showToast("Ha ocurrido un error");
                        }
                    }

                    @Override
                    public void onFailure(Call<Pasajero> call, Throwable t) {
                        showToast("Ha ocurrido un error, intenta más tarde");
                    }
                });

    }

    private void getBoletos(long pasajeroId) {
        client.boletoService().getBoletosByPasajeroId(pasajeroId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Boleto>> call, Response<List<Boleto>> response) {
                        if (response.isSuccessful()) {
                            boletos = response.body();
                            initRecycler(boletos);
                        } else showToast("Algo sucedió, intenta más tarde!");
                    }

                    @Override
                    public void onFailure(Call<List<Boleto>> call, Throwable t) {
                        showToast("Ha ocurrido un error, intenta más tarde");
                    }
                });
    }

    private void initRecycler(List<Boleto> list) {
        list = list.stream().filter(b -> b.getVuelo().isEstado()).collect(Collectors.toList());
        adapter = new TripsRVAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}