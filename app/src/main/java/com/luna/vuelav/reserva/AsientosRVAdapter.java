package com.luna.vuelav.reserva;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.luna.vuelav.R;
import com.luna.vuelav.retrofit.models.Asiento;
import java.util.ArrayList;
import java.util.List;


public class AsientosRVAdapter extends RecyclerView.Adapter<AsientosRVAdapter.ViewHolder> {

    private List<Asiento> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private List<Chip> selectedButton = new ArrayList<>();

    public static final int COLUMNS = 4;

    private VueloViewModel viewModel;

    private ReservaFragment fragment;

    public AsientosRVAdapter(List<Asiento> data, Context context, Activity activity) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public AsientosRVAdapter(Context context, ReservaFragment activity) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        fragment = activity;
        viewModel = new ViewModelProvider(fragment.requireActivity()).get(VueloViewModel.class);
    }

    @NonNull
    @Override
    public AsientosRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.asiento_item_list, parent, false);
        return new AsientosRVAdapter.ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull AsientosRVAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Asiento> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        Chip button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            button = ((Chip) itemView);
        }

        void bindData(Asiento m) {
            button.setText(m.getNombre().replace("Asiento", "").toUpperCase());
            if (m.isEstado()) {
                button.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) addCheckedButton(button, m);
                });
            } else {
                button.setCheckable(false);
                button.setEnabled(false);
            }
        }
    }

    private void addCheckedButton(Chip button, Asiento asiento) {
        selectedButton.forEach(b -> b.setChecked(false));
        selectedButton.clear();
        selectedButton.add(button);
        viewModel.setAsiento(asiento);
        viewModel.getBoleto().observe(fragment.getViewLifecycleOwner(), b -> {
            b.getAsientos().clear();
            b.getAsientos().add(asiento);
        });
    }

}
