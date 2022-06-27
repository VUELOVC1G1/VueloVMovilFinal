package com.luna.vuelav.reserva;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.luna.vuelav.R;
import com.luna.vuelav.databinding.FragmentReservaBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Asiento;
import com.luna.vuelav.retrofit.models.AvionAsientos;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.Promocion;
import com.luna.vuelav.retrofit.models.Vuelo;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReservaFragment extends Fragment {

    private VueloViewModel viewModel;
    private Long idVuelo;
    private Vuelo vuelo;
    private AvionAsientos avion;
    private Asiento asiento;
    private Promocion promocion;
    private ApiClient client;
    private AsientosRVAdapter adapter;
    private RecyclerView recyclerView;
    private ChipGroup chipGroup;
    private Boleto boleto;

    private FragmentReservaBinding binding;

    public static ReservaFragment newInstance() {
        return new ReservaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentReservaBinding.inflate(inflater, container, false);

        recyclerView = binding.asientosRecyclerView;
        boleto = new Boleto();
        boleto.setFecha(new Date());

        // chipGroup = binding.chipGroupasientos;
        viewModel = new ViewModelProvider(requireActivity()).get(VueloViewModel.class);
        client = ApiClient.getInstance();

/*        chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            Toast.makeText(getContext(), "ASIENTO CHECKED: " + checkedIds.get(0), Toast.LENGTH_SHORT).show();
        });
*/
        binding.buttonGo.setOnClickListener(l -> {
            if (checkAsiento())
                navigateToMaletaFragment();
            else
                Toast.makeText(getContext(), "¡No has seleccionado tu asiento!", Toast.LENGTH_SHORT).show();
        });

        viewModel.getVueloId().observe(getViewLifecycleOwner(), id -> {
            idVuelo = id;
            System.out.println(id + " VUELOO ID:::");
            loadVuelo(id);
        });

        viewModel.getAsiento().observe(getViewLifecycleOwner(), a -> {
            asiento = a;
            binding.textAsiento.setText(a.getNombre());
            binding.textClase.setText(a.getTipoAsiento().getNombre() + "\n$ " + a.getPrecio());
        });
        initRecycler();

        return binding.getRoot();
    }

    private void initRecycler() {
        adapter = new AsientosRVAdapter(getContext(), this);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), AsientosRVAdapter.COLUMNS));
        recyclerView.setAdapter(adapter);
    }

    private void bindVueloData() {
        binding.textFrom.setText(vuelo.getRutaResponse().getOrigen());
        binding.textTo.setText(vuelo.getRutaResponse().getDestino());
        String precioText = vuelo.getPrecio() + "";
        if (promocion != null)
            precioText += "\t|\t" + promocion.getDescuento() + "% de descuento";
        binding.textPrecio.setText(precioText);
        binding.textFechaVueloS.setText(dateToStr(vuelo.getFechaVuelo()));
        loadAvionData(vuelo.getAvionResponse().getId());
    }

    private void navigateToMaletaFragment() {
        boleto.setVuelo(vuelo);
        viewModel.setBoleto(boleto);
        ((ReservaActivity) requireActivity()).getNavController().navigate(R.id.action_reservaFragment_to_maletaFragment);
    }

    private void loadAvionData(long idAvion) {
        client.avionService().getById(idAvion)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<AvionAsientos> call, Response<AvionAsientos> response) {
                        if (response.isSuccessful()) {
                            avion = response.body();
                            adapter.setData(avion.getAsientos());
                            adapter.notifyDataSetChanged();
                            // loadAsientos(Objects.requireNonNull(avion).getAsientos());
                        }
                    }

                    @Override
                    public void onFailure(Call<AvionAsientos> call, Throwable t) {
                        Toast.makeText(getContext(), "Ha ocurrido un error, intenta más tarde", Toast.LENGTH_SHORT).show();
                    }
                });
    }

   /* private void loadAsientos(List<Asiento> asientos) {
        asientos.forEach(a -> {
            System.out.println(a.getNombre());
            chipGroup.addView(createChip(a));
        });
    }*/

    private boolean checkAsiento() {
        return asiento != null;
    }

    private String dateToStr(Date fecha) {
        String pattern = "d MMM yyyy  HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
        return simpleDateFormat.format(fecha);
    }

    private void loadVuelo(long id) {
        client.vueloService().getById(id)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Vuelo> call, Response<Vuelo> response) {
                        if (response.isSuccessful()) {
                            vuelo = response.body();
                            boleto.setVuelo(vuelo);
                            viewModel.setBoleto(boleto);
                            getPromotions(id);
                        } else {
                            Toast.makeText(getContext(), "Ha ocurrido un error, intenta más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Vuelo> call, Throwable t) {
                        Toast.makeText(getContext(), "Ha ocurrido un error, intenta más tarde", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getPromotions(long vueloId) {
        client.promotionsService().getPromocionesByVuelo(vueloId)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<List<Promocion>> call, Response<List<Promocion>> response) {
                        if (response.isSuccessful()) {
                            List<Promocion> promocions = response.body();
                            if (!promocions.isEmpty()) {
                                promocion = promocions.get(0);
                                viewModel.setPromocion(promocion);
                            } else viewModel.setPromocion(null);
                        }
                        bindVueloData();
                    }

                    @Override
                    public void onFailure(Call<List<Promocion>> call, Throwable t) {
                        Toast.makeText(getContext(), "Ha ocurrido un error, intenta más tarde", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}