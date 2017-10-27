package co.edu.udea.compumovil.gr02_20172.lab4fcm.Vista.Fragment;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.Adapter.ApartamentoAdapter;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.IComunicaFragments;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Resource;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class Apartamentos extends Fragment implements SearchView.OnQueryTextListener,IComunicaFragments{

    private RecyclerView recyclerView;
    private ApartamentoAdapter adapter;
    private List<Apartament> apartamentoList;

    private Activity activity;
    private IComunicaFragments interfaceComunicaFragments;

    private View rootView;

    private DatabaseReference databaseReference;//REFERENCIAS A LA BASE DE DATOS DE FIREBASE
    private DatabaseReference apartamentosReference;//REFERENCIA A UN HIJO EN LA BASE DE DATOS.

    public Apartamentos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_apartamentos, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference();//INSTANCIA LA BASE DE DATOS DE FIREBASE
        apartamentosReference = databaseReference.child("Apartamentos");//SE PARA EN EL HIJO USUARIO

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceComunicaFragments.generarAccion("add_Apartamento");
            }
        });

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_viewApartamentos);
        recyclerView.setHasFixedSize(true);

        apartamentoList = new ArrayList<>();
        prepareApartamentos();

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        return rootView;
    }

    /**
     * Adding few albums for testing
     */

    private void prepareApartamentos() {
        apartamentosReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Apartament a = dataSnapshot.getValue(Apartament.class);
                //Toast.makeText(getActivity(), apartment.getUbication(), Toast.LENGTH_SHORT).show();
                apartamentoList.add(a);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        adapter = new ApartamentoAdapter(rootView.getContext(), apartamentoList);
        recyclerView.setAdapter(adapter);
        handlerClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof Activity){
            this.activity= (Activity) context;
            this.interfaceComunicaFragments = (IComunicaFragments) this.activity;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String filtro) {
        filtro  = filtro.toLowerCase();
        ArrayList<Apartament> nuevaLista =  new ArrayList<>();
        for (Apartament ap: apartamentoList){
            String tipoInmueble =  ap.getType().toLowerCase();
            String numeroCuartos = String.valueOf(ap.getNumRooms());
            String valor = String.valueOf(ap.getValue());
            if (tipoInmueble.contains(filtro) || numeroCuartos.contains(filtro) || valor.contains(filtro))
                nuevaLista.add(ap);
        }
        adapter.setFilter(nuevaLista);
        return true;
    }


    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    public void enviarApartamento(Apartament apto) {

    }

    @Override
    public void generarAccion(String tag) {
        switch (tag){
            case "Actualizar_Aptos":
                apartamentoList.clear();
                adapter.notifyDataSetChanged();
                prepareApartamentos();
                adapter.notifyDataSetChanged();
                Toast.makeText(rootView.getContext(),"Se actualizaron los apartamentos",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void handlerClick(){
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceComunicaFragments.enviarApartamento(apartamentoList.get(recyclerView.getChildAdapterPosition(view)));
            }
        });
    }
}
