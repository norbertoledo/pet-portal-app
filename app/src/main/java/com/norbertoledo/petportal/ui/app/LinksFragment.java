package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.norbertoledo.petportal.MainActivity;
import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.ui.utils.Loader;
import com.norbertoledo.petportal.ui.utils.LoaderDialog;
import com.norbertoledo.petportal.viewmodels.LinksViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class LinksFragment extends Fragment {

    private static final String TAG = "LINKS";

    private TextView jsonTextView;
    private LinksViewModel linksViewModel;
    private UserViewModel userViewModel;
    private LoaderDialog loader;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_links, container, false);
        jsonTextView = view.findViewById(R.id.jsonText);

        //loader = LoaderDialog.getInstance(getContext());

        linksViewModel = new ViewModelProvider(getActivity()).get(LinksViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linksViewModel.init(userViewModel.getUserToken().getValue());

        //loader.show(R.string.loader_message_load);
        //((MainActivity)getActivity()).showLoader(R.id.linksFragment, R.string.loader_message_load);
        Loader.show(getActivity(), R.id.linksFragment, R.string.loader_message_load);

        linksViewModel.getLinksVM().observe(getActivity(), new Observer<List<Link>>() {
            @Override
            public void onChanged(@Nullable List<Link> links) {
                if(links != null){
                    //((MainActivity)getActivity()).hideLoader();
                    Loader.hide();
                    setView();
                }
            }
        });
    }


    private void setView(){

        jsonTextView.setText("");


            List<Link> items = linksViewModel.getLinksVM().getValue();
            int size = items.size();

            for( int i=0; i<size; i++ ){

                String content = "";
                content += "nombre: " + items.get(i).getNombre() + "\n";
                content += "link: " + items.get(i).getLink() + "\n\n\n";

                jsonTextView.append(content);
            }
            /*
            for( Link post: mLinksViewModel.getLinksVM().getValue()){
                String content = "";
                content += "nombre: " + post.getNombre() + "\n";
                content += "link: " + post.getLink() + "\n\n\n";

                jsonTextView.append(content);
            }
            */



    }

}
