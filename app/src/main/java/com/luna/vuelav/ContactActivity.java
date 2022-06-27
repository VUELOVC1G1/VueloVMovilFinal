package com.luna.vuelav;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.luna.vuelav.databinding.ActivityContactBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Contacto;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactActivity extends AppCompatActivity {

    private ActivityContactBinding binding;
    private ApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        client = ApiClient.getInstance();

        binding.buttonContact.setOnClickListener(l -> {
            if (validateForm()) send();
            else Toast.makeText(this, "¡Olvidaste llenar algo!", Toast.LENGTH_LONG).show();
        });
    }

    private Contacto createRequest() {
        Contacto c = new Contacto();
        c.setCedula(binding.textCedula.getText().toString());
        c.setNombres(binding.textNombres.getText().toString());
        c.setApellidos(binding.textApellidos.getText().toString());
        c.setCorreo(binding.textCorreo.getText().toString());
        c.setDescripcion(binding.textDescripcion.getText().toString());
        return c;
    }

    private boolean validateForm() {
        if (Objects.requireNonNull(binding.textCorreo.getText()).toString().isEmpty()) return false;
        if (Objects.requireNonNull(binding.textNombres.getText()).toString().isEmpty())
            return false;
        if (Objects.requireNonNull(binding.textApellidos.getText()).toString().isEmpty())
            return false;
        if (binding.textCorreo.getText().toString().isEmpty()) return false;
        if (Objects.requireNonNull(binding.textDescripcion.getText()).toString().isEmpty())
            return false;
        return true;
    }

    private void send() {
        Contacto c = createRequest();

        client.contactoService().save(c)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Se registró su solicitud", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Algo salió mal, intenta de nuevo más tarde", Toast.LENGTH_SHORT).show();
                        }
                        finishActivity();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "Algo salió mal, intenta de nuevo más tarde", Toast.LENGTH_SHORT).show();
                        finishActivity();
                    }
                });
    }

    private void finishActivity() {
        finish();
    }
}