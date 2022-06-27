package com.luna.vuelav.ui.promotions;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.luna.vuelav.R;
import com.luna.vuelav.reserva.ReservaActivity;
import com.luna.vuelav.sqlite.models.PromocionM;

import java.util.List;


public class PromotionsRVAdapter extends RecyclerView.Adapter<PromotionsRVAdapter.ViewHolder> {

    private List<PromocionM> data;
    private LayoutInflater inflater;
    private Context context;

    public PromotionsRVAdapter(List<PromocionM> data, Context context) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public PromotionsRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.promotion_item_list, parent, false);
        return new PromotionsRVAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PromotionsRVAdapter.ViewHolder holder, int position) {
        holder.bindData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<PromocionM> data) {
        this.data = data;
    }

    private void gotoReservaActivity(Long idVuelo) {
        System.out.println("VUELOOOID: " + idVuelo);
        Intent intent = new Intent(context, ReservaActivity.class);
        intent.putExtra("vueloId", idVuelo);
        context.startActivity(intent);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView discount, from, to, description, date;
        Button button;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            discount = itemView.findViewById(R.id.textDiscount);
            from = itemView.findViewById(R.id.textFrom);
            to = itemView.findViewById(R.id.textTo);
            description = itemView.findViewById(R.id.textDescription);
            date = itemView.findViewById(R.id.textFechaVuelo);
            button = itemView.findViewById(R.id.buttonBuyTicket);
        }

        void bindData(PromocionM m) {
            discount.setText(m.getDescuento() + "%");
            from.setText(m.getOrigen());
            to.setText(m.getDestino());
            description.setText("Por " + m.getDescripcion());
            date.setText("Viaja el " + m.getFechaVuelo());
            button.setOnClickListener(l -> gotoReservaActivity(m.getVueloId()));
        }

        String dateToString(String fecha) {
            return fecha.replace('T', ' ');
        }
    }
}