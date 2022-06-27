package com.luna.vuelav.reserva;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luna.vuelav.R;
import com.luna.vuelav.databinding.FragmentSearchBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.Vuelo;
import com.luna.vuelav.ui.trips.TripsRVAdapter;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private VueloViewModel vueloViewModel;
    private TripsSearchRVAdapter adapter;
    private RecyclerView recyclerView;

    private ApiClient client;
    private List<Vuelo> vuelos;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        vueloViewModel = new ViewModelProvider(requireActivity()).get(VueloViewModel.class);

        client = ApiClient.getInstance();
        recyclerView = binding.recyclerTrips;

        binding.buttonSearchDestine.setOnClickListener(l -> search());
        initRecycler();

        showToast("Elige un destino o escribe uno");

        return root;
    }

    private void search() {
        String origen = Objects.requireNonNull(binding.menuDesde.getEditText())
                .getText().toString().trim();
        String destino = Objects.requireNonNull(binding.menuHasta.getEditText())
                .getText().toString().trim();

        if (origen.isEmpty() && destino.isEmpty()) return;

        loadTrips(destino, origen, true);
    }

    private void initRecycler() {
        adapter = new TripsSearchRVAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void loadTrips(String destino, String origen, boolean filter) {
        client.vueloService().getAll()
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Vuelo>> call, Response<List<Vuelo>> response) {
                        if (response.isSuccessful()) {
                            vuelos = response.body()
                                    .stream().filter(v -> v.getTipoVueloResponse().getNombre().equalsIgnoreCase("COMERCIAL")
                                            && v.isEstado())
                                    .collect(Collectors.toList());

                            if (filter) {
                                vuelos = vuelos.stream()
                                        .filter(vuelo -> {
                                            if (!origen.isEmpty() && destino.isEmpty()) {
                                                return vuelo.getRutaResponse().getOrigen().equalsIgnoreCase(origen);
                                            }
                                            if (!destino.isEmpty() && origen.isEmpty()) {
                                                return vuelo.getRutaResponse().getDestino().equalsIgnoreCase(destino);
                                            }
                                            return vuelo.getRutaResponse().getDestino().equalsIgnoreCase(destino)
                                                    && vuelo.getRutaResponse().getOrigen().equalsIgnoreCase(origen);
                                        }).collect(Collectors.toList());
                            }
                            if (vuelos.isEmpty())
                                showToast("No hay vuelos para estos destinos por ahora");
                            adapter.setData(vuelos);
                            adapter.notifyDataSetChanged();
                        } else showToast("Ah ocurrido un error, inténtalo de nuevo");
                    }

                    @Override
                    public void onFailure(Call<List<Vuelo>> call, Throwable t) {
                        showToast("Ah ocurrido un error, inténtalo de nuevo");
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}