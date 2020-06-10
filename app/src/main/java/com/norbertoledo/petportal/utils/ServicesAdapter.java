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
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Service;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.Holder> {

    private Context context;
    private int resource;
    private List<Service> listItems;
    private OnItemClickListener mOnItemClickListener;

    public ServicesAdapter(Context context, int resource, List<Service> listItems, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.resource = resource;
        this.listItems = listItems;
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View convertView = LayoutInflater.from(context).inflate(resource, null);

        Holder holder = new Holder(convertView, mOnItemClickListener);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {

        Service item = listItems.get(position);

        holder.title.setText( item.getName() );
        holder.caption.setText( item.getDescription() );
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        Glide.with(context).load(item.getThumb()).transition(withCrossFade(factory)).centerCrop().into(holder.image);
        //Log.d("IMAGEN THUMB ->",item.getThumb());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title;
        TextView caption;
        OnItemClickListener onItemClickListener;

        public Holder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            image = itemView.findViewById(R.id.item_services_image);
            title = itemView.findViewById(R.id.item_services_title);
            caption = itemView.findViewById(R.id.item_services_caption);
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
