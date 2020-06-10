package com.norbertoledo.petportal.ui.app;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.Tip;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.viewmodels.TipsViewModel;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TipFragment extends Fragment {

    private TipsViewModel tipsViewModel;
    private UserViewModel userViewModel;
    private ImageView tipImage;
    private TextView tipTitle;
    private TextView tipDescription;
    private Tip tip;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tip, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tipImage = view.findViewById(R.id.tipImage);
        tipTitle = view.findViewById(R.id.tipTitle);
        tipDescription = view.findViewById(R.id.tipDescription);


        userViewModel = new ViewModelProvider(getActivity()).get(UserViewModel.class);
        tipsViewModel = new ViewModelProvider(getActivity()).get(TipsViewModel.class);

        Loader.show(getActivity(), R.id.tipFragment, R.string.loader_message_load);

        tipsViewModel.getTip(userViewModel.getUserToken().getValue()).observe(getViewLifecycleOwner(), new Observer<Tip>() {
            @Override
            public void onChanged(Tip tip) {
                if(tip!=null && tip.getId().equals(tipsViewModel.getSelectedTip())){
                    Loader.hide();
                    setView();
                }
            }
        });
    }

    public void setView(){

        tip = tipsViewModel.getResponseTip().getValue();

        DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        Glide.with(getContext())
                .load(tip.getImage())
                .transition(withCrossFade(factory))
                .centerCrop()
                .into(tipImage);
        tipTitle.setText( tip.getTitle() );

        // HTML TEXT
        tipDescription.setText(Html.fromHtml(tip.getDescription()) );

    }

}
