package co.edu.udea.compumovil.gr02_20172.lab2activities.Modelo;

import java.io.Serializable;

/**
 * Created by Sebas on 2/09/2017.
 */

public class Apartamento implements Serializable {

    private String nombre;
    private String tipo;
    private int valor;
    private String area;
    private String descripcion;
    private int thumbnail;

    public Apartamento() {
    }

    public Apartamento(String nombre, String tipo, int valor, String area, String descripcion, int thumbnail) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.valor = valor;
        this.area = area;
        this.descripcion = descripcion;
        this.thumbnail = thumbnail;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
