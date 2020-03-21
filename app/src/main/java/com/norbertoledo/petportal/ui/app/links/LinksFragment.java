package com.norbertoledo.petportal.ui.app.links;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.viewmodels.LinksViewModel;

import java.util.List;

public class LinksFragment extends Fragment {

    private static final String TAG = "LINKS";

    private TextView jsonTextView;
    private LinksViewModel mLinksViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_links, container, false);
        jsonTextView = view.findViewById(R.id.jsonText);



        mLinksViewModel = ViewModelProviders.of(getActivity()).get(LinksViewModel.class);
        mLinksViewModel.init();

        mLinksViewModel.getLinksVM().observe(getActivity(), new Observer<List<Link>>() {
            @Override
            public void onChanged(@Nullable List<Link> links) {
                if(links != null){


                    setView();
                }
            }
        });



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //setView();
    }


    private void setView(){

        jsonTextView.setText("");

        //if(mLinksViewModel.getLinksVM().getValue() != null){

            for( Link post: mLinksViewModel.getLinksVM().getValue()){
                String content = "";
                content += "nombre: " + post.getNombre() + "\n";
                content += "link: " + post.getLink() + "\n\n\n";

                jsonTextView.append(content);
            }

        //}


    }

}
