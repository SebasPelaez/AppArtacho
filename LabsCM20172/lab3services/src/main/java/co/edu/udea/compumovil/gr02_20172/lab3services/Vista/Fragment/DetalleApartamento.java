package co.edu.udea.compumovil.gr02_20172.lab3services.Vista.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab3services.Adapter.BannerAdapter;
import co.edu.udea.compumovil.gr02_20172.lab3services.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab3services.R;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.Apartament;
import co.edu.udea.compumovil.gr02_20172.lab3services.entities.Resource;
import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleApartamento extends Fragment{

    private RecyclerView recyclerView;
    private BannerAdapter adapter;
    private List<String> photoList;

    private TextView lblNombreApartamento;
    private TextView lblTipoApartamento;
    private TextView lblValorApartamento;
    private TextView lblNumeroHabitaciones;
    private TextView lblArea;
    private TextView lblDescripcion;

    private View rootView;
    private String ubicacion;

    public DetalleApartamento() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detalle_apartamento, container, false);
        inicializarComponentes();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_viewbanner);
        recyclerView.setHasFixedSize(true);

        photoList = new ArrayList<>();
        adapter = new BannerAdapter(rootView.getContext(), photoList);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);

        Button button = (Button) rootView.findViewById(R.id.btnUbicacion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse(ubicacion);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }

        });

        Bundle objetoApartamento=getArguments();
        Apartament apto=null;
        if (objetoApartamento != null) {
            apto= (Apartament) objetoApartamento.getSerializable("objeto");
            asignarInformacion(apto);
            //prepareApartamentos(apto);
        }

        return rootView;
    }

    private void inicializarComponentes() {
        lblNombreApartamento = (TextView)rootView.findViewById(R.id.lblNombreApartamento);
        lblTipoApartamento= (TextView) rootView.findViewById(R.id.lblTipoApartamento);
        lblValorApartamento = (TextView)rootView.findViewById(R.id.lblValorApartamento);
        lblNumeroHabitaciones = (TextView)rootView.findViewById(R.id.lblNumeroHabitaciones);
        lblArea = (TextView)rootView.findViewById(R.id.lblArea);
        lblDescripcion = (TextView) rootView.findViewById(R.id.lblDescripcion);
    }

    private void asignarInformacion(Apartament apto) {
        lblNombreApartamento.setText(lblNombreApartamento.getText()+": "+apto.getName());
        lblTipoApartamento.setText(lblTipoApartamento.getText()+": "+apto.getType());
        lblValorApartamento.setText(lblValorApartamento.getText()+": $"+apto.getValue());
        lblNumeroHabitaciones.setText(lblNumeroHabitaciones.getText()+": "+apto.getNumRooms());
        lblArea.setText(lblArea.getText()+": "+apto.getArea());;
        lblDescripcion.setText(lblDescripcion.getText()+": "+apto.getDescription());
        ubicacion = "geo:0,0?q="+apto.getLocation();
    }


    private void prepareApartamentos(Apartament apto) {
        RestClient restClient = RestClient.retrofit.create(RestClient.class);
        Call<Resource> call = restClient.getResource(apto.getId());
        Resource currentResource = null;
        try {
            currentResource = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        photoList.add(currentResource.getPathResource());
        adapter.notifyDataSetChanged();
    }

}
