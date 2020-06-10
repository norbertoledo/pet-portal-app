package com.norbertoledo.petportal.ui.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.norbertoledo.petportal.models.Link;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.utils.LinksAdapter;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.viewmodels.LinksViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class LinksFragment extends Fragment {

    private static final String TAG = "LINKS";

    private LinksViewModel linksViewModel;
    private UserViewModel userViewModel;
    private ArrayAdapter<Link> adapter;
    private List<Link> linkList;
    private ListView linksListView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_links, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        linksListView = view.findViewById(R.id.linksListView);

        linksViewModel = new ViewModelProvider(getActivity()).get(LinksViewModel.class);
        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);

        linksViewModel.init(userViewModel.getUserToken().getValue());

        Loader.show(getActivity(), R.id.linksFragment, R.string.loader_message_load);

        linksViewModel.getLinksVM().observe(getViewLifecycleOwner(), new Observer<List<Link>>() {
            @Override
            public void onChanged(@Nullable List<Link> links) {
                if(links != null){
                    Loader.hide();
                    setView();
                }
            }
        });
    }

    private void setView(){


        linkList = linksViewModel.getLinksVM().getValue();

        adapter = new LinksAdapter(getActivity(), R.layout.item_list_links, linkList);

        linksListView.setAdapter(adapter);

        linksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openLink(linkList.get(position).getLink());
            }
        });

    }

    private void openLink(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

}
