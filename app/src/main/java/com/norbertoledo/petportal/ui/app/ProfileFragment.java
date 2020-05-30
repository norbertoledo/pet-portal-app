package com.norbertoledo.petportal.ui.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;
import com.norbertoledo.petportal.MainActivity;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.Webservice;
import com.norbertoledo.petportal.repositories.webservice.WebserviceBuilder;
import com.norbertoledo.petportal.ui.utils.Loader;
import com.norbertoledo.petportal.viewmodels.UserViewModel;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE FRAGMENT";
    private UserViewModel userViewModel;
    //private ImageButton profileImageButton;
    private ImageView profileImageButton;
    private TextView profileEmail;
    private EditText profileName;
    private Spinner profileSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button profileSave;
    private LiveData<User> user;

    private View view;


    static final int REQUEST_CODE_STORAGE_PERMISSIONS = 1;
    static final int REQUEST_CODE_SELECT_IMAGE = 2;
    //static final int IMG_REQ = 2;
    private Bitmap bitmap;
    private ImageView imageView;



    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_profile, container, false);


        profileImageButton = view.findViewById(R.id.profileImageButton);
        profileEmail = view.findViewById(R.id.profileEmail);
        profileName = view.findViewById(R.id.profileName);
        profileSpinner = view.findViewById(R.id.spinner);
        profileSave = view.findViewById(R.id.profileSave);



        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.cities_array, android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        profileSpinner.setAdapter(adapter);

        // User ViewModel
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);

        final Context context = this.getContext();


        final Observer<String> messageObsever = new Observer<String>() {
            @Override
            public void onChanged(String message) {
                Log.d(TAG, "ESCUCHO MESSAGE: -> "+message);
                if(message != null){
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "CAMBIOOOOOOOOOOOOOOOO MENSAJEEEEEEEE");
                    userViewModel.clearMessage();
                }
            }
        };
        userViewModel.getMessages().observe(getActivity(), messageObsever);



        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ContextCompat.checkSelfPermission(
                        getContext(), Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_STORAGE_PERMISSIONS
                    );
                }else{
                    selectImage();
                }
            }
        });


        profileSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInfo();
            }
        });

        loadUserInfo();



        return view;
    }

    private void loadUserInfo(){

        user = userViewModel.getUserData();

        Log.d(TAG, "LOAD USER: "+user);

        String photoUrl = user.getValue().getPhotoUrl();
        /*
        if(!photoUrl.equals("") && !photoUrl.equals("null") && userViewModel.getImageProfileSignature() != null ) {

            Glide.with(getContext())
                    .load(photoUrl)
                    .signature(userViewModel.getImageProfileSignature())
                    .centerCrop()
                    .error(R.drawable.profile_default_image)
                    .into(profileImageButton);
        }else{
            profileImageButton.setImageResource(R.drawable.profile_default_image);
        }
*/

        Glide.with(getContext())
                .load(photoUrl)
                .signature(userViewModel.getImageProfileSignature())
                .centerCrop()
                .error(R.drawable.profile_default_image)
                .into(profileImageButton);


        profileEmail.setText(user.getValue().getEmail());
        profileName.setText(user.getValue().getName());

        int position = adapter.getPosition(user.getValue().getCity());
        profileSpinner.setSelection(position);
    }


    private void saveUserInfo(){

        User userM = user.getValue();
        userM.setName(profileName.getText().toString());
        userM.setCity(profileSpinner.getSelectedItem().toString());


        Loader.show(getActivity(), R.id.profileFragment, R.string.loader_message_update);

        userViewModel.updateUserData(userM);

        userViewModel.updateUserDataResponse().observe(getActivity(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                Log.d(TAG, "ESCUCHO USER: -> "+user);
                if(user!=null){

                    Loader.hide();

                    Log.d(TAG, "CAMBIOOOOOOOOOOOOOOOO USER: -> "+user);
                    userViewModel.setUserData(user);
                    //userViewModel.setMessage("MODIFICADO CON EXITO!");
                    Snackbar.make(view, "Modificado con éxito", Snackbar.LENGTH_LONG).show();
                    userViewModel.clearUpdateResponse();
                }
            }
        });


    }

    private void uploadUserImage(File selectedImageFile){

        File file = selectedImageFile;

        Retrofit ws = WebserviceBuilder.getInstance();



        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part requestImage = MultipartBody.Part.createFormData("image", file.getName(), requestFile);

        RequestBody imageData = RequestBody.create(MediaType.parse("multipart/form-data"), "This is a new profile image");


        String token = userViewModel.getUserToken().getValue();

        IWebservice iws = ws.create(IWebservice.class);
        Call call = iws.uploadUserImageApi(token, requestImage, imageData);

        Loader.show(getActivity(), R.id.profileFragment, R.string.loader_message_update);

        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                String photo = response.body().getPhotoUrl();
                Log.d("RETORNO->", photo);

                Log.d("CARGADOR ->  ", "ME DESPRETEEEE!!!!!!");

                ObjectKey signature = new ObjectKey(String.valueOf(System.currentTimeMillis()));

                Glide.with(getContext())
                        .load(photo)
                        .signature(signature)
                        .centerCrop()
                        .into(profileImageButton);

                userViewModel.setImageProfileSignature(signature);
                User user = userViewModel.getUserData().getValue();
                user.setPhotoUrl(photo);
                userViewModel.setUserData(user);

                Loader.hide();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_STORAGE_PERMISSIONS && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                selectImage();
            }else{
                Snackbar.make(view, "No ha aceptado el permiso :(", Snackbar.LENGTH_LONG).show();

            }
        }
    }

    private void selectImage(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if( intent.resolveActivity( getContext().getPackageManager() ) != null ){
            startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SELECT_IMAGE && resultCode == getActivity().RESULT_OK){
            if(data != null){
                Uri selectedImageUri = data.getData();
                if(selectedImageUri != null){
                    try {
                        InputStream inputStream = getContext().getContentResolver().openInputStream(selectedImageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        //profileImageButton.setImageBitmap(bitmap);

                        // *************
                        // Imagen seleccionada para enviar al servidor
                        File selectedImageFile = new File(getPathFromUri(selectedImageUri));

                        // *************

                        uploadUserImage(selectedImageFile);
                        // **************

                    }catch (Exception e){
                        Snackbar.make(view, "Error: "+e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private String getPathFromUri(Uri contentUri){
        String filePath = null;
        Cursor cursor = getContext().getContentResolver()
                .query(contentUri, null, null, null, null);

        if(cursor == null ){
            filePath = contentUri.getPath();
        }else{
            cursor.moveToFirst();
            int index = cursor.getColumnIndex("_data");
            filePath = cursor.getString(index);
            cursor.close();
        }

        return filePath;
    }








    // **************
    // OTRO METODO PARA OBTENER IMAGEN DE LA GALERIA
    /*
    private void selectImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_CODE_SELECT_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //if (requestCode == IMG_REQ && resultCode == RESULT_OK && data != null) {
        if (requestCode == REQUEST_CODE_SELECT_IMAGE && data != null) {
            Uri path = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), path);
                profileImageButton.setImageBitmap(bitmap);
                profileImageButton.setVisibility(View.VISIBLE);

                name.setVisibility(View.VISIBLE);
                button.setEnabled(false);
                upload.setEnabled(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String imageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imageByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageByte, Base64.DEFAULT);
    }
    */
    // ******************

}
