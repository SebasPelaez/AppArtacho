package co.edu.udea.compumovil.gr02_20172.lab3services.entities;

/**
 * Created by Sebas on 23/09/2017.
 */

public class User_Singleton {
    private static User instance = null;

    public static User getInstance() {
        if(instance == null) {
            instance = new User();
        }
        return instance;
    }

    public static void destroyInstance(){
        instance = null;
    }

    private User_Singleton() {
    }
}
