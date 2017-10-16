package co.edu.udea.compumovil.gr02_20172.lab4fcm.entities;

import android.graphics.Point;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Juan on 1/09/2017.
 */

public class Apartament implements Serializable {
    private int id;
    private String name;
    private String type;
    private int value;
    private int idUSer;
    private Double area;
    private String description;
    private String location;
    private int numRooms;

    public Apartament() {
    }

    public Apartament(int id, String name, String type, int value, int idUSer, Double area, String description, String location, int numRooms) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.value = value;
        this.idUSer = idUSer;
        this.area = area;
        this.description = description;
        this.location = location;
        this.numRooms = numRooms;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getIdUSer() {
        return idUSer;
    }

    public void setIdUSer(int idUSer) {
        this.idUSer = idUSer;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getNumRooms() {
        return numRooms;
    }

    public void setNumRooms(int numRooms) {
        this.numRooms = numRooms;
    }
}
