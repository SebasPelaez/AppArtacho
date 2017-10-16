package co.edu.udea.compumovil.gr02_20172.lab4fcm.Adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.udea.compumovil.gr02_20172.lab4fcm.Interface.RestClient;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.R;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Apartament;
import co.edu.udea.compumovil.gr02_20172.lab4fcm.entities.Resource;
import retrofit2.Call;

/**
 * Created by Sebas on 2/09/2017.
 */

public class ApartamentoAdapter extends RecyclerView.Adapter<ApartamentoAdapter.MyViewHolder> implements View.OnClickListener{

    private Context mContext;
    private List<Apartament> apartamentoList;

    private View.OnClickListener listener;

    @Override
    public void onClick(View v) {
        if (listener!=null){
            listener.onClick(v);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nombre, tipo,valor,area,descripcion;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            nombre = (TextView) view.findViewById(R.id.nombre);
            tipo = (TextView) view.findViewById(R.id.tipo);
            valor = (TextView) view.findViewById(R.id.valor);
            area = (TextView) view.findViewById(R.id.area);
            descripcion = (TextView) view.findViewById(R.id.descripcion);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public ApartamentoAdapter(Context mContext, List<Apartament> apartamentoList) {
        this.mContext = mContext;
        this.apartamentoList = apartamentoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_item, parent, false);

        itemView.setOnClickListener(this);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Apartament apto = apartamentoList.get(position);
        holder.nombre.setText("Nombre: "+apto.getName());
        holder.valor.setText("Valor: "+apto.getValue());
        holder.tipo.setText("Tipo: "+apto.getType());
        holder.area.setText("Área: "+apto.getArea());
        holder.descripcion.setText("Descripción: "+apto.getDescription());

        Glide.with(mContext).load(Uri.parse(findImages(apto.getId()))).into(holder.thumbnail);

    }

    @Override
    public int getItemCount() {
        return apartamentoList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener=listener;
    }

    public void setFilter(List<Apartament> newList){
        apartamentoList= new ArrayList<>();
        apartamentoList.addAll(newList);
        notifyDataSetChanged();
    }

    private String findImages(int aptoId){
        List<Resource> resources = null;
        String path=null;
        RestClient restClient = RestClient.retrofit.create(RestClient.class);
        Call<List<Resource>> call = restClient.getResources();
        try {
            resources = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(Resource r: resources){
            if(r.getIdApartment() == aptoId){
                path=r.getPathResource();
                break;
            }
        }
        return path;
    }
    
}
