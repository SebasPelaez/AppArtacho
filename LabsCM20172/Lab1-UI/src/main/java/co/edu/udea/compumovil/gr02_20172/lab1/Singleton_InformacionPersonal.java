package co.edu.udea.compumovil.gr02_20172.lab1;

/**
 * Created by Sebas on 19/08/2017.
 */

public class Singleton_InformacionPersonal {


    private static Singleton_InformacionPersonal instance;
    private InformacionPersonal data;
    private Singleton_InformacionPersonal(){
        data = new InformacionPersonal();
    }

    public static Singleton_InformacionPersonal getInformacion(){
        if(instance==null){
            instance=new Singleton_InformacionPersonal();
        }
        return instance;
    }

    public InformacionPersonal getData(){
        return data;
    }


}
