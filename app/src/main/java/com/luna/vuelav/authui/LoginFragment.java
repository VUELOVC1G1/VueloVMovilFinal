package com.luna.vuelav.authui;

import static android.content.ContentValues.TAG;

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

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.messaging.FirebaseMessaging;
import com.luna.vuelav.MainActivity;
import com.luna.vuelav.R;
import com.luna.vuelav.databinding.FragmentLoginBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.LoginRequest;
import com.luna.vuelav.retrofit.models.Usuario;
import com.luna.vuelav.retrofit.models.UsuarioToken;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    private FragmentLoginBinding binding;

    private UsuarioDaoImpl usuarioDao;
    private ApiClient client;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        client = ApiClient.getInstance();

        usuarioDao = new UsuarioDaoImpl(getContext());

        binding.buttonToSignup.setOnClickListener(v -> Navigation.findNavController(root).navigate(R.id.action_navigation_login_to_navigation_signup));

        binding.buttonLogin.setOnClickListener(v -> {
            if (isNetworkAvailable())
                iniciarSesion();
            else showSnack("No estás conectado a internet", false);
        });

        return root;
    }

    private void iniciarSesion() {

        LoginRequest request = new LoginRequest(
                Objects.requireNonNull(binding.textFieldName.getEditText()).getText().toString(),
                Objects.requireNonNull(binding.textFieldPassword.getEditText()).getText().toString()
        );

        Call<Usuario> call = client.authService().login(request);

        call.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if (response.isSuccessful()) {
                    saveUser(Objects.requireNonNull(response.body()));
                    getToken(response.body().getId());
                    showSnack("Bienvenido/a", true);
                } else {
                    showSnack("Su contraseña o correo son incorrectas", false);
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                showSnack("¡Ha ocurrido un error!, intenta de nuevo más tarde", false);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
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


    private void saveUser(Usuario usuario) {
        UsuarioM usuarioM = new UsuarioM();
        usuarioM.setId(usuario.getId());
        usuarioM.setPassword(usuario.getPassword());
        usuarioM.setUsername(usuario.getEmail());
        usuarioDao.save(usuarioM);
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