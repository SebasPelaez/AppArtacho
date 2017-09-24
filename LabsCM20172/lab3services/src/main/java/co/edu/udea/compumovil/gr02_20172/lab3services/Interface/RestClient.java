package co.edu.udea.compumovil.gr02_20172.lab3services.Interface;

import java.util.List;
import java.util.Map;

import co.edu.udea.compumovil.gr02_20172.lab3services.entities.Apartament;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.Resource;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.User;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Sebas on 23/09/2017.
 */

public interface RestClient {

    //Crear usuario
    @POST("users")
    Call<User> createUser(@Body User user);

    //Modificar usuario
    @PUT("users")
    Call <User> editUser(@Body User user);

    //Listar un usuario por id
    @GET("users/{id}")
    Call <User> getUser(@Path("id") Integer id);

    //Loggin
    @GET("users/findOne")
    Call<User> loginUser(@QueryMap Map<String,String> filters);

    //Listar apartamentos
    @GET("apartaments")
    Call<List<Apartament>> getApartaments();

    //Crear Apartamento
    @POST("apartaments")
    Call<Apartament> createApartament(@Body Apartament apartament);

    //Listar un recurso por id
    @GET("resources/{id}")
    Call <Resource> getResource(@Path("id") Integer id);

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://192.168.1.53:3000/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
