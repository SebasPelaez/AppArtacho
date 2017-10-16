package co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;

/**
 * Created by jsebastian.pelaez on 5/09/17.
 */

public interface IComunicaFragments {
    public void enviarApartamento(Apartament apto);
    public void generarAccion(String tag);
}
