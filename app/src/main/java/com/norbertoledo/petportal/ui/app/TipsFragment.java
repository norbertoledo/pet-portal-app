package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.utils.TipsAdapter;
import com.norbertoledo.petportal.viewmodels.TipsViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.util.List;

public class TipsFragment extends Fragment implements TipsAdapter.OnItemClickListener {

    private UserViewModel userViewModel;
    private TipsViewModel tipsViewModel;
    private List<Tip> tipList;
    private RecyclerView tipsRecyclerView;
    private TipsAdapter adapter;
    private RecyclerView.LayoutManager manager;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tips, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        tipsRecyclerView = view.findViewById(R.id.tipsRecyclerView);

        int gridColumnCount = getResources().getInteger(R.integer.tips_grid_column_count);

        // Grid
        manager = new GridLayoutManager(getActivity(), gridColumnCount);

        tipsRecyclerView.setLayoutManager(manager);


        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        tipsViewModel = new ViewModelProvider(getActivity()).get(TipsViewModel.class);

        tipsViewModel.initTips(userViewModel.getUserToken().getValue());

        Loader.show(getActivity(), R.id.tipsFragment, R.string.loader_message_load);

        tipsViewModel.getTips().observe(getViewLifecycleOwner(), new Observer<List<Tip>>() {
            @Override
            public void onChanged(@Nullable List<Tip> tips) {
                if(tips != null){
                    Loader.hide();
                    setView();
                }
            }
        });

    }

    public void setView(){

        tipList = tipsViewModel.getTips().getValue();

        adapter = new TipsAdapter(getActivity(), R.layout.item_list_tips, tipList, this);

        tipsRecyclerView.setAdapter(adapter);

    }

    @Override
    public void onItemClick(int position) {
        tipsViewModel.setSelectedTip(tipList.get(position).getId());
        navController.navigate(R.id.action_tipsFragment_to_tipFragment);
    }
}
