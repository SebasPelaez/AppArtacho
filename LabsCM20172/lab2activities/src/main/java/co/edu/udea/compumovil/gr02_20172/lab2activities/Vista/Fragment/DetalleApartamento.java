package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;


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

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab2activities.Adapter.BannerAdapter;
import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.Apartament;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetalleApartamento extends Fragment{

    private RecyclerView recyclerView;
    private BannerAdapter adapter;
    private List<Integer> photoList;

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
        View rootView = inflater.inflate(R.layout.fragment_detalle_apartamento, container, false);
        inicializarComponentes();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_viewbanner);
        recyclerView.setHasFixedSize(true);

        photoList = new ArrayList<>();
        adapter = new BannerAdapter(rootView.getContext(), photoList);
        prepareApartamentos();
        recyclerView.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(llm);

        Button button = (Button) rootView.findViewById(R.id.btnUbicacion);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194");
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
        }

        return rootView;
    }

    private void inicializarComponentes() {

    }

    private void asignarInformacion(Apartament apto) {
    }


    private void prepareApartamentos() {
        int[] covers = new int[]{
                R.drawable.apartamento1,
                R.drawable.apartamento2,
                R.drawable.apartamento3,
                R.drawable.apartamento4,
                R.drawable.apartamento5,
                R.drawable.apartamento6,
                R.drawable.apartamento7,
                R.drawable.apartamento8,
                R.drawable.apartamento9};

        photoList.add(covers[0]);
        photoList.add(covers[1]);
        photoList.add(covers[2]);
        photoList.add(covers[3]);
        photoList.add(covers[4]);
        photoList.add(covers[5]);
        photoList.add(covers[6]);
        photoList.add(covers[7]);
        photoList.add(covers[8]);

        adapter.notifyDataSetChanged();
    }
}
