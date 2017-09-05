package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroApartamento extends Fragment {


    public RegistroApartamento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registro_apartamento, container, false);
    }

}
