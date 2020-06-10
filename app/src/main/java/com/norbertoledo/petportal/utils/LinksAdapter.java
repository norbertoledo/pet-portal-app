package com.norbertoledo.petportal.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Link;

import java.util.List;

public class LinksAdapter extends ArrayAdapter<Link> {

    //Declarar variables

    //Contexto
    private Context context;

    // Resource
    private int resource;

    // ArrayList
    private List<Link> listItems;

    // Constructor
    public LinksAdapter(Context context, int resource, List<Link> listItems) {
        super(context, resource, listItems);
        this.context = context;
        this.resource = resource;
        this.listItems = listItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Instanciar el objeto Link con cada item de la lista
        Link link = (Link) getItem(position);

        // Inflar la vista de cada item de la lista
        convertView = LayoutInflater.from(context).inflate(resource, null);

        // Instanciar cada elemento del item
        //ImageView image = convertView.findViewById(R.id.item_links_image);
        TextView title = convertView.findViewById(R.id.item_links_title);
        TextView description = convertView.findViewById(R.id.item_links_description);
        TextView linkUrl = convertView.findViewById(R.id.item_links_link);


        // Agregar el contendio a cada elemento del item

        // Title
        title.setText(link.getTitle());
        // Description
        description.setText(link.getDescription());
        // linkUrl
        linkUrl.setText(link.getLink());

        // Devolver la vista del item completa
        return convertView;
    }
}
