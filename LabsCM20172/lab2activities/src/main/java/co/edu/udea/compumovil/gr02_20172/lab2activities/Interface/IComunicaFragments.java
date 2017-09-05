package co.edu.udea.compumovil.gr02_20172.lab2activities.Interface;

import co.edu.udea.compumovil.gr02_20172.lab2activities.Modelo.Apartamento;

/**
 * Created by jsebastian.pelaez on 5/09/17.
 */

public interface IComunicaFragments {
    public void enviarApartamento(Apartamento apto);
    public void generarAccion(String tag);
}
