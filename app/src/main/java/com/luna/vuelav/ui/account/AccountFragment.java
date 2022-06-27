package com.luna.vuelav.ui.account;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.luna.vuelav.R;
import com.luna.vuelav.authui.AuthActivity;
import com.luna.vuelav.databinding.FragmentAccountBinding;
import com.luna.vuelav.retrofit.ApiClient;
import com.luna.vuelav.retrofit.models.Pasajero;
import com.luna.vuelav.retrofit.models.UsuarioCorreo;
import com.luna.vuelav.sqlite.daos.UsuarioDaoImpl;
import com.luna.vuelav.sqlite.models.UsuarioM;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountFragment extends Fragment {

    private ApiClient client;

    private AccountViewModel mViewModel;
    private UsuarioDaoImpl usuarioDao;
    private UsuarioM usuarioM;

    private Pasajero pasajero;

    private FragmentAccountBinding binding;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);
        client = ApiClient.getInstance();

        init();

        binding.layouButtons.setVisibility(View.INVISIBLE);
        editMode(false);

        binding.buttonCancel.setOnClickListener(v -> editMode(false));
        binding.buttonSave.setOnClickListener(v -> {
            updateUser();
            editMode(false);
            loadUserInfo();
        });

        binding.topAppBar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.closeSession:
                    logout();
                    return true;
                case R.id.edit:
                    editMode(true);
                    return true;
            }
            return false;
        });
        return binding.getRoot();
    }

    private void updateUser() {
        pasajero.setCedula(Objects.requireNonNull(binding.textFieldCedula.getEditText()).getText().toString());
        pasajero.setApellido(Objects.requireNonNull(binding.textFieldLastName.getEditText()).getText().toString());
        pasajero.setNombre(Objects.requireNonNull(binding.textFieldName.getEditText()).getText().toString());
        pasajero.getUsuario().setPassword(
                Objects.requireNonNull(binding.textFieldPassword.getEditText()).getText().toString()
        );
        String email = binding.textViewEmail.getText().toString();
        pasajero.getUsuario().setEmail(email);
        pasajero.getUsuario().setCorreo(email);

        client.pasajeroService().update(pasajero.getId(), pasajero)
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Pasajero> call, Response<Pasajero> response) {
                        if (response.isSuccessful()) {
                            loadUserInfo();
                            saveUser(response.body().getUsuario());
                            editMode(false);
                        } else {
                            showToast("No se puede actualizar en este momento, intenta más tarde");
                        }
                    }

                    @Override
                    public void onFailure(Call<Pasajero> call, Throwable t) {
                        showToast("No se puede actualizar en este momento, intenta más tarde");
                    }
                });
    }

    private void init() {
        usuarioDao = new UsuarioDaoImpl(getContext());

        if (isUserLogged()) {
            loadUserInfo();
        } else {
            Intent intent = new Intent(getContext(), AuthActivity.class);
            startActivity(intent);
            requireActivity().finish();
        }
    }

    private void logout() {
        usuarioDao.deleteAll();
        requireActivity().recreate();
    }

    private void editMode(boolean edit) {
        if (edit) {
            binding.layouButtons.setVisibility(View.VISIBLE);
        } else {
            binding.layouButtons.setVisibility(View.GONE);
        }
        binding.textFieldLastName.getEditText().setEnabled(edit);
        binding.textFieldName.getEditText().setEnabled(edit);
        binding.textFieldCedula.getEditText().setEnabled(edit);
        binding.textFieldPassword.getEditText().setEnabled(edit);
    }

    private void loadUserInfo() {
        client.pasajeroService().findPasajeroByUserId(usuarioM.getId())
                .enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<Pasajero> call, Response<Pasajero> response) {
                        if (response.isSuccessful()) {
                            Pasajero p = response.body();
                            pasajero = response.body();
                            binding.textView.setText(p.getNombre().substring(0, 1));
                            binding.textFieldName.getEditText().setText(p.getNombre());
                            binding.textFieldLastName.getEditText().setText(p.getApellido());
                            binding.textViewEmail.setText(p.getUsuario().getEmail());
                            binding.textFieldCedula.getEditText().setText(p.getCedula());
                        } else {
                            showToast("Ha ocurrido un error al cargar tus datos");
                        }
                    }

                    @Override
                    public void onFailure(Call<Pasajero> call, Throwable t) {
                        showToast("Ha ocurrido un error, intenta más tarde");
                    }
                });

    }

    private void saveUser(UsuarioCorreo usuario) {
        usuarioDao.deleteAll();
        UsuarioM usuarioM = new UsuarioM();
        usuarioM.setId(usuario.getId());
        usuarioM.setPassword(usuario.getPassword());
        usuarioM.setUsername(usuario.getEmail());
        usuarioDao.save(usuarioM);
    }

    private void showToast(String text) {
        Toast.makeText(getContext(), text, Toast.LENGTH_LONG).show();
    }

    private boolean isUserLogged() {
        usuarioM = usuarioDao.getUser();
        return usuarioM != null;
    }

}