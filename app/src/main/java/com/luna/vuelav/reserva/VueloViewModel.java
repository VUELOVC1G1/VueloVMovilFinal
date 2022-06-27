package com.luna.vuelav.reserva;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.luna.vuelav.retrofit.models.Asiento;
import com.luna.vuelav.retrofit.models.Boleto;
import com.luna.vuelav.retrofit.models.Promocion;

public class VueloViewModel extends ViewModel {

    private MutableLiveData<Long> vueloId = new MutableLiveData<>();
    private MutableLiveData<Asiento> asiento = new MutableLiveData<>();
    private MutableLiveData<Boleto> boleto = new MutableLiveData<>();
    private MutableLiveData<Promocion> promocion = new MutableLiveData<>();

    public void setPromocion(Promocion promocion) {
        this.promocion.setValue(promocion);
    }

    public LiveData<Promocion> getPromocion() {
        return promocion;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto.setValue(boleto);
    }

    public LiveData<Boleto> getBoleto() {
        return boleto;
    }

    public void setAsiento(Asiento asiento) {
        this.asiento.setValue(asiento);
    }

    public LiveData<Asiento> getAsiento() {
        return asiento;
    }

    public void setVueloId(long vueloId) {
        this.vueloId.setValue(vueloId);
    }

    public LiveData<Long> getVueloId() {
        return vueloId;
    }

}