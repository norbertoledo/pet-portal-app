package com.norbertoledo.petportal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.norbertoledo.petportal.Interface.PetPortalApi;
import com.norbertoledo.petportal.Model.Links;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LinksActivity extends AppCompatActivity {

    private StoreClass store;
    private String apiUrl;
    private String userToken;
    private TextView jsonTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links);


        jsonTextView = findViewById(R.id.jsonText);
        apiUrl = "https://pet-portal.web.app/api/";

        store = (StoreClass) getApplicationContext();

    }

    @Override
    protected void onStart() {
        super.onStart();

        userToken = store.getUserToken();
        if(userToken != ""){
            getLinks(userToken);
        }
    }

    private void getLinks(String idToken){


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
