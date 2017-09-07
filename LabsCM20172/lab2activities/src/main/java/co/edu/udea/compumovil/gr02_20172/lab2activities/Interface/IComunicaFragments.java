package co.edu.udea.compumovil.gr02_20172.lab2activities.Interface;

import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.Apartament;

/**
 * Created by jsebastian.pelaez on 5/09/17.
 */

public interface IComunicaFragments {
    public void enviarApartamento(Apartament apto);
    public void generarAccion(String tag);
}
