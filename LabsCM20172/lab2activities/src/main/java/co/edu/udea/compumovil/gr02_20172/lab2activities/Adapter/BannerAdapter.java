package co.edu.udea.compumovil.gr02_20172.lab2activities.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import co.edu.udea.compumovil.gr02_20172.lab2activities.Modelo.Apartamento;
import co.edu.udea.compumovil.gr02_20172.lab2activities.R;

/**
 * Created by Sebas on 4/09/2017.
 */

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Integer> photos;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            thumbnail = (ImageView) view.findViewById(R.id.imgBannner);
        }
    }

    public BannerAdapter(Context mContext, List<Integer> photos) {
        this.mContext = mContext;
        this.photos = photos;
    }

    @Override
    public BannerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bannerphotodetail, parent, false);

        return new BannerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final BannerAdapter.MyViewHolder holder, int position) {
        int photo = photos.get(position);
        // loading album cover using Glide library
        Glide.with(mContext).load(photo).into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

}
