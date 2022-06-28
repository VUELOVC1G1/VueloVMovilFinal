package com.luna.vuelav.authui;


import static android.content.ContentValues.TAG;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.luna.vuelav.MainActivity;
import com.luna.vuelav.R;
import com.luna.vuelav.databinding.FragmentSignupBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Pasajero;
import com.luna.vuelav.retrofit.models.Usuario;
import com.luna.vuelav.retrofit.models.UsuarioCorreo;
import com.luna.vuelav.retrofit.models.UsuarioToken;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private DatePickerDialog pickerDialog;
    private ApiClient client;

    private UsuarioDaoImpl usuarioDao;

    private Date birthDate;
    private String strDate;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentSignupBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        client = ApiClient.getInstance();
        usuarioDao = new UsuarioDaoImpl(getContext());

        binding.buttonToLogin.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.navigation_login);
        });

        binding.buttonSignup.setOnClickListener(v -> {
            if (isNetworkAvailable())
                signup();
            else showSnack("No estás conectado a internet", false);
        });
        binding.textEditBirthDate.setOnClickListener(v -> showDialog());

        pickerDialog = new DatePickerDialog(getContext());
        pickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTime().getTime());
        pickerDialog.setCancelable(false);

        pickerDialog.setOnDateSetListener((view, year1, month1, dayOfMonth) -> {
            strDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
            Objects.requireNonNull(binding.textEditBirthDate).setText(strDate);
            SignupFragment.this.birthDate = parseDate(strDate);
        });

        return root;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("dd/M/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showDialog() {
        pickerDialog.show();
    }

    private void signup() {
        if (!validateFields()) {
            showSnack("Olvidaste llenar algo!", false);
            return;
        }

        Pasajero pasajero = new Pasajero();
        pasajero.setNombre(Objects.requireNonNull(binding.textFieldName.getEditText()).getText().toString());
        pasajero.setApellido(Objects.requireNonNull(binding.textFieldLastName.getEditText()).getText().toString());
        pasajero.setCedula(Objects.requireNonNull(binding.textFieldCedula.getEditText()).getText().toString());
        pasajero.setFechaNacimiento(formatDate(this.birthDate));
        UsuarioCorreo usuario = new UsuarioCorreo();
        usuario.setCorreo(Objects.requireNonNull(binding.textFieldEmail.getEditText()).getText().toString());
        usuario.setPassword(Objects.requireNonNull(binding.textFieldPassword.getEditText()).getText().toString());
        pasajero.setUsuario(usuario);

        Call<Pasajero> call = client.authService().registro(pasajero);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Pasajero> call, Response<Pasajero> response) {

                System.out.println(response.code());
                System.out.println(response.message());
                System.out.println(response.toString());
                if (response.isSuccessful()) {
                    saveUser(Objects.requireNonNull(response.body().getUsuario()));
                    getToken(response.body().getUsuario().getId());
                    showSnack("Bienvenido/a", true);
                } else showSnack("¡Ha ocurrido un error!, intenta de nuevo más tarde", false);
            }

            @Override
            public void onFailure(Call<Pasajero> call, Throwable t) {
                showSnack("¡Ha ocurrido un error!, intenta de nuevo más tarde", false);
            }
        });
    }

    private void saveUser(UsuarioCorreo usuario) {
        UsuarioM usuarioM = new UsuarioM();
        usuarioM.setId(usuario.getId());
        if (usuario.getCorreo() == null || usuario.getCorreo().isEmpty())
            usuarioM.setPassword(usuario.getPassword());
        else usuarioM.setPassword(usuario.getCorreo());
        usuarioM.setUsername(usuario.getEmail());
        usuarioDao.save(usuarioM);
    }

    private boolean validateFields() {

        if (binding.textFieldName.getEditText().getText().toString().isEmpty()) return false;
        if (binding.textFieldLastName.getEditText().getText().toString().isEmpty()) return false;
        if (this.birthDate == null) return false;
        if (binding.textFieldCedula.getEditText().getText().toString().isEmpty()) return false;
        if (binding.textFieldEmail.getEditText().getText().toString().isEmpty()) return false;
        if (binding.textFieldPassword.getEditText().getText().toString().isEmpty()) return false;
        return true;
    }


    private void getToken(long userId) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    Log.d(TAG, "TOKENN: => : " + token);
                    sendToken(userId, token);
                });
    }

    private void sendToken(long userId, String token) {
        UsuarioToken request = new UsuarioToken();
        request.setToken(token);
        request.setUsuarioId(userId);
        System.out.println("CLASSTOKEN ===> " + token);

        Call<ResponseBody> call = client.tokenService().save(request);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.i(TAG, "onResponse: Token saved!");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: Error send token!");
            }
        });
    }


    private Date formatDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        String str = format.format(date);
        SimpleDateFormat formatToDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        try {
            return formatToDate.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showSnack(String text, boolean finish) {
        Snackbar snackbar = Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG);
        snackbar.show();
        if (finish) new Handler().postDelayed(() -> {
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }, 500);
    }

}