package com.norbertoledo.petportal.utils;

import android.content.Context;
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
import com.norbertoledo.petportal.models.Tip;

import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.Holder> {

    private Context context;
    private int resource;
    private List<Tip> listItems;
    private OnItemClickListener mOnItemClickListener;

    public TipsAdapter(Context context, int resource, List<Tip> listItems, OnItemClickListener onItemClickListener) {
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

        Tip item = listItems.get(position);

        holder.title.setText( item.getTitle() );
        holder.description.setText( item.getDescription() );
        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        Glide.with(context).load(item.getImage()).transition(withCrossFade(factory)).centerCrop().into(holder.image);

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView image;
        TextView title, description;
        OnItemClickListener onItemClickListener;

        public Holder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);

            image = itemView.findViewById(R.id.item_tips_image);
            title = itemView.findViewById(R.id.item_tips_title);
            description = itemView.findViewById(R.id.item_tips_description);
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


