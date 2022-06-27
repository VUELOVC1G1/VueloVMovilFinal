package com.luna.vuelav.reserva;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.luna.vuelav.R;
import com.luna.vuelav.databinding.ActivityBoletoViewBinding;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.webview.WebViewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BoletoViewActivity extends AppCompatActivity {

    private ActivityBoletoViewBinding binding;
    private final String URL = "https://vuelovc1g1.github.io/VuelaVG1C1fFront/inicio/buscarboleto/";
    private Boleto boleto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityBoletoViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialToolbar toolbar = findViewById(R.id.topAppBarQr);
        toolbar.setNavigationOnClickListener(v -> finish());

        boleto = (Boleto) getIntent().getSerializableExtra("boleto");
        if (boleto != null) {
            loadData();
        }
        binding.viewQRCode.setOnClickListener(l -> startWebActivity());
    }

    private void startWebActivity() {
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", URL + boleto.getId());
        startActivity(intent);
    }

    private void loadData() {
        String from = boleto.getVuelo().getRutaResponse().getOrigen();
        String to = boleto.getVuelo().getRutaResponse().getDestino();
        binding.textFrom.setText(from);
        binding.textTo.setText(to);
        binding.textFromCode.setText(from.substring(0, 3).toUpperCase());
        binding.textToCode.setText(to.substring(0, 3).toUpperCase());
        binding.textAsiento.setText(boleto.getAsientos().get(0).getNombre());
        binding.textNumVuelo.setText(boleto.getVuelo().getId() + "");
        binding.textSala.setText(boleto.getVuelo().getSalaEspera());
        binding.textFecha.setText(dateToStr(boleto.getVuelo().getFechaVuelo()));
        binding.textPasajero.setText(boleto.getPasajero().getNombre() + " " + boleto.getPasajero().getApellido() + " | " + boleto.getPasajero().getCedula());
        generateQR(URL + boleto.getId());
    }

    private String dateToStr(Date fecha) {
        String pattern = "d MMM yyyy  HH:mm";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
        return simpleDateFormat.format(fecha);
    }

    private void generateQR(String text) {
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.QR_CODE, 250, 250);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            binding.viewQRCode.setImageBitmap(bitmap);

            /*InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(binding.)*/
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}