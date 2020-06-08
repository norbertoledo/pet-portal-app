package com.norbertoledo.petportal.utils;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.ServicesCategory;

import java.util.List;

public class ServicesCategoryAdapter extends RecyclerView.Adapter<ServicesCategoryAdapter.Holder> {

    private Context context;
    private int resource;
    private List<ServicesCategory> listItems;
    private ServicesCategoryAdapter.OnItemClickListener mOnItemClickListener;

    public ServicesCategoryAdapter(Context context, int resource, List<ServicesCategory> listItems, ServicesCategoryAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.resource = resource;
        this.listItems = listItems;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ServicesCategoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(context).inflate(resource, null);

        ServicesCategoryAdapter.Holder holder = new ServicesCategoryAdapter.Holder(convertView, mOnItemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ServicesCategoryAdapter.Holder holder, int position) {

        ServicesCategory item = listItems.get(position);

        holder.title.setText( item.getName() );
        holder.background.setBackgroundColor( Color.parseColor( item.getColor() ) );
        Glide.with(context).load(item.getImage()).centerCrop().into(holder.image);
        Log.d("IMAGEN THUMB ->",item.getImage());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title;
        View background;
        ServicesCategoryAdapter.OnItemClickListener onItemClickListener;

        public Holder(@NonNull View itemView, ServicesCategoryAdapter.OnItemClickListener onItemClickListener) {
            super(itemView);

            image = itemView.findViewById(R.id.item_services_category_image);
            title = itemView.findViewById(R.id.item_services_category_title);
            background = itemView.findViewById(R.id.item_services_category_background);
            this.onItemClickListener = onItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
