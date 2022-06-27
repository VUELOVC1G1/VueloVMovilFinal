package com.luna.vuelav.reserva;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.luna.vuelav.R;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.Vuelo;
import com.luna.vuelav.webview.WebViewActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TripsSearchRVAdapter extends RecyclerView.Adapter<TripsSearchRVAdapter.ViewHolder> {

    private List<Vuelo> data = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    private VueloViewModel viewModel;

    private SearchFragment fragment;

    public TripsSearchRVAdapter(List<Vuelo> data, Context context, Activity activity) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    public TripsSearchRVAdapter(Context context, SearchFragment activity) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        fragment = activity;
        viewModel = new ViewModelProvider(fragment.requireActivity()).get(VueloViewModel.class);
    }

    @NonNull
    @Override
    public TripsSearchRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.trip_search_item_list, parent, false);
        return new TripsSearchRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsSearchRVAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Vuelo> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView from, to, precio, date;
        MaterialButton button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            from = itemView.findViewById(R.id.textFrom);
            to = itemView.findViewById(R.id.textTo);
            precio = itemView.findViewById(R.id.textPrecio);
            date = itemView.findViewById(R.id.textFechaVueloS);
            button = itemView.findViewById(R.id.buttonReservarS);
        }

        void bindData(Vuelo m) {
            from.setText(m.getRutaResponse().getOrigen());
            to.setText(m.getRutaResponse().getDestino());
            precio.setText(m.getPrecio() + "");
            date.setText(dateToStr(m.getFechaVuelo()));
            button.setOnClickListener(v -> toReservarFragment(m.getId()));
        }

        private String dateToStr(Date fecha) {
            String pattern = "d MMM yyyy  HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
            return simpleDateFormat.format(fecha);
        }
    }

    private void toReservarFragment(long id) {
        viewModel.setVueloId(id);
        ((ReservaActivity) fragment.requireActivity()).getNavController().navigate(R.id.action_searchFragment_to_reservaFragment);
    }
}
