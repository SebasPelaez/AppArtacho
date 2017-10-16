package co.edu.udea.compumovil.gr02_20172.lab4fcm.entities;

/**
 * Created by Juan on 1/09/2017.
 */

public class Resource {
    private int id;
    private int idApartment;
    private String pathResource;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdApartment() {
        return idApartment;
    }

    public void setIdApartment(int idApartment) {
        this.idApartment = idApartment;
    }

    public String getPathResource() {
        return pathResource;
    }

    public void setPathResource(String pathResource) {
        this.pathResource = pathResource;
    }
}
