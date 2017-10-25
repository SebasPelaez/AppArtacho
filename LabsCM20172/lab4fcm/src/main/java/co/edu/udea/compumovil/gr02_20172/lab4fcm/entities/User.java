package co.edu.udea.compumovil.gr02_20172.lab4fcm.entities;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Juan on 1/09/2017.
 */

public class User {
    private String id;
    private String username;
    private String password;
    private String name;
    private String last_name;
    private Integer gender;
    private String birthday;
    private String phone;
    private String address;
    private String email;
    private String city;
    private String image;

    public User() {
    }

    public User(String id, String username, String password, String name, String last_name, Integer gender,
                String birthday, String phone, String address, String email, String city, String image) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.last_name = last_name;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.city = city;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return last_name;
    }

    public void setLastname(String last_name) {
        this.last_name = last_name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("username", username);
        result.put("password", password);
        result.put("name", name);
        result.put("last_name", last_name);
        result.put("gender", gender);
        result.put("birthday", birthday);
        result.put("phone", phone);
        result.put("address", address);
        result.put("email", email);
        result.put("city", city);
        result.put("image", image);
        return result;
    }
}
