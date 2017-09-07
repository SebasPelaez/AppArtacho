package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab2activities.Adapter.ApartamentoAdapter;
import co.edu.udea.compumovil.gr02_20172.lab2activities.Interface.IComunicaFragments;
import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.SQLiteConnectionHelper;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.Apartament;

/**
 * A simple {@link Fragment} subclass.
 */
public class Apartamentos extends Fragment implements SearchView.OnQueryTextListener {

    private RecyclerView recyclerView;
    private ApartamentoAdapter adapter;
    private List<Apartament> apartamentoList;

    private Activity activity;
    private IComunicaFragments interfaceComunicaFragments;

    private View rootView;

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
        adapter = new ApartamentoAdapter(rootView.getContext(), apartamentoList);
        prepareApartamentos();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                interfaceComunicaFragments.enviarApartamento(apartamentoList.get(recyclerView.getChildAdapterPosition(view)));
            }
        });

        return rootView;
    }

    /**
     * Adding few albums for testing
     */

    private void prepareApartamentos() {
        SQLiteConnectionHelper connectionDb = new SQLiteConnectionHelper(rootView.getContext(),"db_lab",null,1);
        SQLiteDatabase db = connectionDb.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM a",null);

        /**
         * Llenar el recycler con todos los datos posibles
         * apartamentoList.add(new Apartament("Casa 1","Casa", 13,"Poblado","Aqui vivo yo", covers[0]));
         */


        adapter.notifyDataSetChanged();
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
    public boolean onQueryTextChange(String newText) {
        newText  = newText.toLowerCase();
        ArrayList<Apartament> nuevaLista =  new ArrayList<>();
        for (Apartament ap: apartamentoList){
            String nombre =  ap.getName().toLowerCase();
            if (nombre.contains(newText))
                nuevaLista.add(ap);
        }
        adapter.setFilter(nuevaLista);
        return true;
    }
}
