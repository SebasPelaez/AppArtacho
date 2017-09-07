package co.edu.udea.compumovil.gr02_20172.lab2activities.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab2activities.R;
import co.edu.udea.compumovil.gr02_20172.lab2activities.entities.Apartament;

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
        holder.nombre.setText(apto.getName());
        holder.valor.setText(" $"+apto.getValue());
        holder.tipo.setText(apto.getType());
        holder.area.setText(apto.getArea());
        holder.descripcion.setText(apto.getDescription());

        // loading album cover using Glide library
        Glide.with(mContext).load(apto.getResource(0).getIdApartment()).into(holder.thumbnail);//aca verificar si existte el path si no, es con la imagen

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
    
}
