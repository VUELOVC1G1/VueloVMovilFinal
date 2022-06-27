package com.luna.vuelav.ui.trips;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.luna.vuelav.R;
import com.luna.vuelav.reserva.BoletoViewActivity;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.sqlite.models.PromocionM;
import com.luna.vuelav.webview.WebViewActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class TripsRVAdapter extends RecyclerView.Adapter<TripsRVAdapter.ViewHolder> {

    private List<Boleto> data;
    private LayoutInflater inflater;
    private Context context;

    public TripsRVAdapter(List<Boleto> data, Context context) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public TripsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.trip_item_list, parent, false);
        return new TripsRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripsRVAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<Boleto> data) {
        this.data = data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView from, to, codeTo, codeFrom, asiento, numVuelo, date;
        MaterialButton button;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            from = itemView.findViewById(R.id.textFrom);
            to = itemView.findViewById(R.id.textTo);
            codeFrom = itemView.findViewById(R.id.textFromCode);
            codeTo = itemView.findViewById(R.id.textToCode);
            asiento = itemView.findViewById(R.id.textAsiento);
            date = itemView.findViewById(R.id.textFechaVuelo);
            numVuelo = itemView.findViewById(R.id.textNumVuelo);
            button = itemView.findViewById(R.id.buttonSeeMore);
        }

        void bindData(Boleto m) {
            from.setText(m.getVuelo().getRutaResponse().getOrigen());
            to.setText(m.getVuelo().getRutaResponse().getDestino());
            codeFrom.setText(m.getVuelo().getRutaResponse().getOrigen().substring(0, 3).toUpperCase(Locale.ROOT));
            codeTo.setText(m.getVuelo().getRutaResponse().getDestino().substring(0, 3).toUpperCase(Locale.ROOT));
            asiento.setText(m.getAsientos().get(0).getNombre() + " | " + m.getAsientos().get(0).getTipoAsiento().getNombre());
            numVuelo.setText("Num Vuelo: " + m.getVuelo().getId());
            date.setText(dateToStr(m.getVuelo().getFechaVuelo()));
            button.setOnClickListener(v -> openBoletoWebView(m));
        }

        private void openBoletoWebView(Boleto b) {
            Intent intent = new Intent(context, BoletoViewActivity.class);
            intent.putExtra("boleto", b);
            context.startActivity(intent);
        }

        private String dateToStr(Date fecha) {
            String pattern = "d MMM yyyy  HH:mm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("es", "EC"));
            return simpleDateFormat.format(fecha);
        }
    }
}
