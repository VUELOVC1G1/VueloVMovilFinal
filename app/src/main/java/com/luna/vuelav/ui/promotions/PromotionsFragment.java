package com.luna.vuelav.ui.promotions;

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

import com.google.android.material.snackbar.Snackbar;
import com.luna.vuelav.databinding.FragmentPromotionsBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Promocion;
import com.luna.vuelav.sqlite.daos.PromocionesDaoImpl;
import com.luna.vuelav.sqlite.models.PromocionM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionsFragment extends Fragment {

    private FragmentPromotionsBinding binding;

    private RecyclerView recyclerView;
    private PromotionsRVAdapter adapter;

    private PromocionesDaoImpl promocionesDao;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        PromotionsViewModel promotionsViewModel =
                new ViewModelProvider(this).get(PromotionsViewModel.class);

        binding = FragmentPromotionsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        promocionesDao = new PromocionesDaoImpl(getContext());

        recyclerView = binding.recyclerPromotions;

        if (isNetworkAvailable()) {
            getPromotions();
        } else {
            showToast("No estás conectado a internet!");
            getSavedPromociones();
        }

        return root;
    }

    private void getSavedPromociones() {
        List<PromocionM> promocions = promocionesDao.getAll();
        initRecycler(promocions);
    }

    private void getPromotions() {
        ApiClient client = ApiClient.getInstance();

        Call<List<Promocion>> call = client.promotionsService().getAllComerciales();

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Promocion>> call, Response<List<Promocion>> response) {
                if (response.isSuccessful()) {
                    promocionesDao.deleteAll();
                    List<PromocionM> promocionMS = toPromocionM(Objects.requireNonNull(response.body()));
                    promocionMS.forEach(p -> {
                        promocionesDao.save(p);
                    });
                    initRecycler(promocionMS);
                } else {
                    showToast("Ah ocurrido un error, intenta de nuevo más tarde");
                }
            }

            @Override
            public void onFailure(Call<List<Promocion>> call, Throwable t) {
                showToast("Ah ocurrido un error, intenta de nuevo más tarde");
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private List<PromocionM> toPromocionM(List<Promocion> list) {
        return list.stream()
                .filter(promocion -> promocion.getVuelo().isEstado())
                .map(p -> {
                    PromocionM m = new PromocionM();
                    m.setId(p.getId());
                    m.setOrigen(p.getVuelo().getRutaResponse().getOrigen());
                    m.setDestino(p.getVuelo().getRutaResponse().getDestino());
                    m.setRutaDescripcion(p.getVuelo().getRutaResponse().getDescripcion());
                    m.setDescripcion(p.getDescripcion());
                    m.setDescuento(p.getDescuento());
                    m.setPrecio(p.getVuelo().getPrecio());
                    m.setFechaVuelo(dateToStr(p.getVuelo().getFechaVuelo()));
                    m.setVueloId(p.getVuelo().getId());
                    return m;
                }).collect(Collectors.toList());
    }

    private String dateToStr(Date fecha) {
        String pattern = "d MMM yyyy  HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
        return simpleDateFormat.format(fecha);
    }

    private void initRecycler(List<PromocionM> list) {
        adapter = new PromotionsRVAdapter(list, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void showSnack(String text) {
        Snackbar snackbar = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}