package com.norbertoledo.petportal.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.ReferencePlaces;

import java.lang.reflect.Field;
import java.util.List;

public class PlacesAdapter extends ArrayAdapter<ReferencePlaces> {

    private Context context;
    private int resource;
    private List<ReferencePlaces> listItems;
    private int theColor;
    private int currentWidth;

    // Constructor
    public PlacesAdapter(Context context, int resource, List<ReferencePlaces> listItems) {
        super(context, resource, listItems);
        this.context = context;
        this.resource = resource;
        this.listItems = listItems;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Instanciar el objeto Link con cada item de la lista
        ReferencePlaces reference =  getItem(position);

        // Inflar la vista de cada item de la lista
        convertView = LayoutInflater.from(context).inflate(resource, null);

        // Instanciar cada elemento del item
        //ImageView image = convertView.findViewById(R.id.item_links_image);
        View color = convertView.findViewById(R.id.item_places_color);
        TextView category = convertView.findViewById(R.id.item_places_category);

        // Color
        theColor = Color.parseColor( reference.getColor() );
        color.getBackground().setColorFilter( theColor, PorterDuff.Mode.SRC_ATOP);

        // Title
        category.setText(reference.getCategory());

        // Devolver la vista del item completa
        return convertView;

    }

}
