package co.edu.udea.compumovil.gr02_20172.lab2activities.Vista.Fragment;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab2activities.Adapter.ApartamentoAdapter;
import co.edu.udea.compumovil.gr02_20172.lab2activities.Modelo.Apartamento;
import co.edu.udea.compumovil.gr02_20172.lab2activities.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Apartamentos extends Fragment {

    private RecyclerView recyclerView;
    private ApartamentoAdapter adapter;
    private List<Apartamento> apartamentoList;

    public Apartamentos() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_apartamentos, container, false);

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.recycler_viewApartamentos);
        rv.setHasFixedSize(true);

        apartamentoList = new ArrayList<>();
        adapter = new ApartamentoAdapter(rootView.getContext(), apartamentoList);
        prepareApartamentos();
        rv.setAdapter(adapter);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rv.setLayoutManager(llm);

        return rootView;
    }

    /**
     * Adding few albums for testing
     */
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

        apartamentoList.add(new Apartamento("Casa 1","Casa", 13,"Poblado","Aqui vivo yo", covers[0]));
        apartamentoList.add(new Apartamento("Casa 2","Apartamento", 3,"Aranjuez","Aqui vivo yo", covers[1]));
        apartamentoList.add(new Apartamento("Casa 3","Hotel", 53,"Castilla", "Aqui vivo yo",covers[2]));
        apartamentoList.add(new Apartamento("Casa 4","Apartamento",6,"La Milagrosa", "Aqui vivo yo",covers[3]));
        apartamentoList.add(new Apartamento("Casa 5","Casa", 8,"Cordoba", "Aqui vivo yo",covers[4]));
        apartamentoList.add(new Apartamento("Casa 6","Apartamento",2,"San Petesburgo", "Aqui vivo yo",covers[5]));
        apartamentoList.add(new Apartamento("Casa 7","Casa", 4,"Tehuatiucan", "Aqui vivo yo",covers[6]));
        apartamentoList.add(new Apartamento("Casa 8","Apartamento", 5, "Springfield","Aqui vivo yo",covers[7]));
        apartamentoList.add(new Apartamento("Casa 9","Hotel",1,"Westeros","Aqui vivo yo",covers[8]));

        adapter.notifyDataSetChanged();
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}
