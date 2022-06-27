package com.luna.vuelav.reserva;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.chip.ChipGroup;
import com.luna.vuelav.databinding.FragmentMaletaBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Asiento;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.BoletoRequest;
import com.luna.vuelav.retrofit.models.Maleta;
import com.luna.vuelav.retrofit.models.Pago;
import com.luna.vuelav.retrofit.models.Pasajero;
import com.luna.vuelav.retrofit.models.Promocion;
import com.luna.vuelav.retrofit.models.Vuelo;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MaletaFragment extends Fragment {

    private FragmentMaletaBinding binding;
    private VueloViewModel viewModel;
    private Boleto boleto;
    private Pago pago;
    private Promocion promocion;

    private ApiClient client;
    private UsuarioDaoImpl usuarioDao;

    private ChipGroup chipGroup;

    private int numMaletas;
    private double peso;
    private double total;
    private long pasajeroId;
    private boolean existsPasajero;

    private final double precioKiloMaleta = .5;

    public static MaletaFragment newInstance() {
        return new MaletaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMaletaBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(VueloViewModel.class);
        client = ApiClient.getInstance();
        usuarioDao = new UsuarioDaoImpl(getContext());
        chipGroup = binding.chipGroupPay;

        pago = new Pago();
        pago.setEstado(true);
        pago.setTipo("VISA");

        viewModel.getBoleto().observe(getViewLifecycleOwner(), b -> {
            boleto = b;
            bindBoletoData(b);
            viewModel.getPromocion().observe(getViewLifecycleOwner(), this::bindPromotion);
        });

        initMaletasInput();
        getPasajeroId();

        binding.buttonPay.setOnClickListener(l -> {
            if (validatePay()) saveBoleto();
            else
                Toast.makeText(getContext(), "Selecciona un método de pago", Toast.LENGTH_LONG).show();
        });

        return binding.getRoot();
    }

    private void getPasajeroId() {
        client.pasajeroService().findPasajeroByUserId(getUserId())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Pasajero> call, Response<Pasajero> response) {
                        if (response.isSuccessful()) {
                            pasajeroId = response.body().getId();
                            existsPasajero = true;
                        } else {
                            Toast.makeText(getContext(), "Debes iniciar sesión para continuar", Toast.LENGTH_LONG).show();
                            binding.buttonPay.setEnabled(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<Pasajero> call, Throwable t) {
                        Toast.makeText(getContext(), "Error al obtener pasajero", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private long getUserId() {
        UsuarioM m = usuarioDao.getUser();
        if (m != null) return m.getId();
        return 0;
    }

    private void bindPromotion(Promocion promocion) {
        if (promocion != null) {
            this.promocion = promocion;
            binding.textPrecio.setText(boleto.getVuelo().getPrecio() + " | " + promocion.getDescuento() + "% de descuento");
        } else {
            binding.textPrecio.setText(boleto.getVuelo().getPrecio() + "");
        }
        calculateTotal();
    }

    private void calculateTotal() {
        total = boleto.getVuelo().getPrecio();
        if (promocion != null) {
            total -= (promocion.getDescuento() / 100.0) * total;
        }
        if (!boleto.getAsientos().isEmpty())
            total += boleto.getAsientos().get(0).getPrecio();
        total += peso * precioKiloMaleta;
        binding.txtTotal.setText("$ " + total);
    }

    private void initMaletasInput() {
        binding.textNumMaletas.setOnKeyListener((v, keyCode, event) -> {
            String numStr = binding.textNumMaletas.getText().toString();
            if (!numStr.isEmpty()) {
                numMaletas = Integer.parseInt(numStr);
            } else {
                numMaletas = 0;
            }
            return false;
        });

        binding.textPeso.setOnKeyListener((v, keyCode, event) -> {
            String numStr = binding.textPeso.getText().toString();
            if (!numStr.isEmpty()) {
                peso = Double.parseDouble(numStr);
                calculateTotal();
            }
            return false;
        });
    }

    private void initChipd() {
        binding.chipVisa.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) pago.setTipo("VISA");
        });

        binding.chipDinnersClub.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) pago.setTipo("DINNERS CLUB");
        });

        binding.chipDiscover.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) pago.setTipo("DISCOVER");
        });

        binding.chipMasterCard.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) pago.setTipo("MASTERCARD");
        });
        boleto.setPago(pago);
    }

    private void saveBoleto() {
        BoletoRequest request = new BoletoRequest();
        request.setMaletas(boleto.getMaletas());
        request.setFecha(new Date());
        pago.setValor(total);
        request.setPago(pago);
        request.setAsientos(boleto.getAsientos());
        request.setVueloId(boleto.getVuelo().getId());
        request.setPasajeroId(pasajeroId);

        if (numMaletas != 0) {
            generateMaleta();
        }
        client.boletoService().save(request)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), "Has reservado un vuelo", Toast.LENGTH_SHORT).show();
                            requireActivity().finish();
                        } else {
                            Toast.makeText(getContext(), "Algo sucedió al reservar, intenta más tarde", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getContext(), "Error reservar, intenta más tarde", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                    }
                });
    }

    private void generateMaleta() {
        int peso = Integer.parseInt(Objects.requireNonNull(binding.textPeso.getText()).toString()) / numMaletas;
        for (int i = 0; i < numMaletas; i++) {
            Maleta m = new Maleta();
            m.setPeso(String.valueOf(peso));
            m.setPrecio(peso * precioKiloMaleta);
            boleto.getMaletas().add(m);
        }
    }

    private boolean validatePay() {
        return binding.chipMasterCard.isChecked()
                || binding.chipVisa.isChecked()
                || binding.chipDinnersClub.isChecked()
                || binding.chipDiscover.isChecked();
    }

    private void bindBoletoData(Boleto boleto) {
        Vuelo vuelo = boleto.getVuelo();
        if (!boleto.getAsientos().isEmpty()) {
            Asiento asiento = boleto.getAsientos().get(0);
            binding.textFrom.setText(vuelo.getRutaResponse().getOrigen());
            binding.textTo.setText(vuelo.getRutaResponse().getDestino());
            binding.textAsiento.setText(asiento.getNombre());
            binding.textClase.setText(asiento.getTipoAsiento().getNombre() + "\n$ " + asiento.getPrecio());
            binding.textFechaVueloS.setText(dateToStr(vuelo.getFechaVuelo()));
        }
        initChipd();
    }

    private String dateToStr(Date fecha) {
        String pattern = "d MMM yyyy  HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
        return simpleDateFormat.format(fecha);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}