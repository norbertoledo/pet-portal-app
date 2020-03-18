package com.norbertoledo.petportal.ui.links;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.norbertoledo.petportal.api.IApiRequest;
import com.norbertoledo.petportal.api.ProviderBuilder;
import com.norbertoledo.petportal.models.Links;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LinksFragment extends Fragment {

    private User userStore;
    private String userToken;
    private TextView jsonTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_links, container, false);
        jsonTextView = view.findViewById(R.id.jsonText);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        userStore = User.getInstance();
        userToken = userStore.getToken();

        if(userToken != ""){
            getLinks(userToken);
        }
    }

    private void getLinks(String idToken){

        jsonTextView.setText("");

        IApiRequest iApi = ProviderBuilder.getInstance().create(IApiRequest.class);

        Call<List<Links>> call = iApi.getLinks(idToken);

        call.enqueue(new Callback<List<Links>>() {
            @Override
            public void onResponse(Call<List<Links>> call, Response<List<Links>> response) {

                if(!response.isSuccessful()){
                    // No está autenticado por ejemplo. No exitoso pero no es un error
                    jsonTextView.setText("Código de error: "+response.code());
                    return;
                }

                List<Links> linksList = response.body();


                for( Links post: linksList){
                    String content = "";
                    content += "nombre: " + post.getNombre() + "\n";
                    content += "link: " + post.getLink() + "\n\n\n";

                    jsonTextView.append(content);
                }

            }

            @Override
            public void onFailure(Call<List<Links>> call, Throwable t) {
                jsonTextView.setText(t.getMessage());
            }
        });


    }


}
