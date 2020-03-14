package com.norbertoledo.petportal.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.norbertoledo.petportal.Activities.MainActivity;
import com.norbertoledo.petportal.Interfaces.PetPortalApi;
import com.norbertoledo.petportal.Models.Links;
import com.norbertoledo.petportal.Models.Store;
import com.norbertoledo.petportal.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LinksFragment extends Fragment {

    private Store store;
    private String apiUrl;
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);


        apiUrl = "https://pet-portal.web.app/api/";
        store = (Store) context.getApplicationContext();



    }

    @Override
    public void onStart() {
        super.onStart();
        userToken = store.getUserToken();
        if(userToken != ""){
            //jsonTextView.setText(userToken);
            getLinks(userToken);

        }
    }

    private void getLinks(String idToken){

        //Toast.makeText(getContext(), apiUrl, Toast.LENGTH_LONG).show();
        jsonTextView.setText("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetPortalApi iApi = retrofit.create(PetPortalApi.class);

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
