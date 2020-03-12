package com.norbertoledo.petportal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.norbertoledo.petportal.Interface.PetPortalApi;
import com.norbertoledo.petportal.Model.Links;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    private static final String TAG = "LOGIN";
    //TextView tComentario;
    private TextView jsonTextView;
    private String apiUrl;
    private String token;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        jsonTextView = findViewById(R.id.jsonText);



        //apiUrl = "https://pet-portal.firebaseapp.com/api/";
        apiUrl = "https://pet-portal.web.app/api/";

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if(currentUser != null) {
            getIdToken(currentUser);
        }else{
            signIn();
        }
    }

    private void signIn(){
        String email = "beto@beto.com";
        String password = "789456";
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        updateUI(null);
    }

    private void getUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            //Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }

    private void getIdToken(FirebaseUser currentUser){
        //FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
            public void onComplete(@NonNull Task<GetTokenResult> task) {
                if (task.isSuccessful()) {
                    String idToken = task.getResult().getToken();
                    // Send token to your backend via HTTPS
                    // ...
                    getLinks(idToken);
                } else {
                    // Handle error -> task.getException();
                }
            }
        });
    }

    private void getLinks(String idToken){


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PetPortalApi iApi = retrofit.create(PetPortalApi.class);

        Call<List<Links>> call = iApi.getPosts(idToken);

        call.enqueue(new Callback<List<Links>>() {
            @Override
            public void onResponse(Call<List<Links>> call, Response<List<Links>> response) {

                if(!response.isSuccessful()){
                    // No está autenticado por ejemplo. No exitoso pero no es un error
                    jsonTextView.setText("Código de error: "+response.code());
                    return;
                }

                List<Links> postsList = response.body();

                /*
                for( Posts post: postsList){
                    String content = "";
                    content += "userId: " + post.getUserId() + "\n";
                    content += "id: " + post.getId() + "\n";
                    content += "title: " + post.getTitle() + "\n";
                    content += "body: " + post.getBody() + "\n\n\n";

                    jsonTextView.append(content);
                }
                */

                for( Links post: postsList){
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
