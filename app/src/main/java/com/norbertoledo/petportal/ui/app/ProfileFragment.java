package com.norbertoledo.petportal.ui.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.signature.ObjectKey;
import com.google.android.material.snackbar.Snackbar;
import com.norbertoledo.petportal.R;
import com.norbertoledo.petportal.models.User;
import com.norbertoledo.petportal.repositories.webservice.IWebservice;
import com.norbertoledo.petportal.repositories.webservice.WebserviceBuilder;
import com.norbertoledo.petportal.utils.Loader;
import com.norbertoledo.petportal.utils.BitmapTask;
import com.norbertoledo.petportal.viewmodels.UserViewModel;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "PROFILE FRAGMENT";
    private UserViewModel userViewModel;
    private ImageView profileImageButton;
    private TextView profileEmail;
    private EditText profileName;
    private Spinner profileSpinner;
    private ArrayAdapter<CharSequence> adapter;
    private Button profileSave;
    private LiveData<User> user;

    private View view;


    private static final int REQUEST_CODE_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };


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

        profileImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check if we have write permission
                int permission = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // We don't have permission so prompt the user
                    ActivityCompat.requestPermissions(
                            getActivity(),
                            PERMISSIONS_STORAGE,
                            REQUEST_CODE_EXTERNAL_STORAGE
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
                if(user!=null){
                    Loader.hide();
                    userViewModel.setUserData(user);
                    Snackbar.make(view, R.string.profile_data_update_success, Snackbar.LENGTH_LONG).show();
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

                Snackbar.make(view, R.string.profile_image_update_success, Snackbar.LENGTH_LONG).show();

                ObjectKey signature = new ObjectKey(String.valueOf(System.currentTimeMillis()));

                DrawableCrossFadeFactory factory =
                        new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();

                Glide.with(getContext())
                        .load(photo)
                        .transition(withCrossFade(factory))
                        .signature(signature)
                        .placeholder(profileImageButton.getDrawable())
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
                Snackbar.make(view, R.string.profile_image_update_error, Snackbar.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_EXTERNAL_STORAGE && grantResults.length > 0){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Snackbar.make(view, R.string.permission_success, Snackbar.LENGTH_LONG).show();
                selectImage();
            }else{
                Snackbar.make(view, R.string.permission_error, Snackbar.LENGTH_LONG).show();

            }
        }
    }

    private void selectImage(){
        CropImage.activity()
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK) {
                Uri selectedImageUri = result.getUri();

                if(selectedImageUri != null) {

                    // *************
                    // Extract filename from Uri
                    String path = getPathFromUri(selectedImageUri);
                    String filename = path.substring(path.lastIndexOf("/"), path.length());

                    try {

                        // Create Bitmap
                        Bitmap bitmap = BitmapTask.createBitmap(getContext(), selectedImageUri);

                        // Resize Bitmap
                        bitmap = BitmapTask.resizeStaticWidth(bitmap, 500, true);

                        // Compress Bitmap and write File
                        File imageFile = BitmapTask.compressToFile(getContext(), filename, bitmap, 70);

                        //Send File
                        uploadUserImage(imageFile);

                    } catch (Exception e) {

                        Snackbar.make(view, "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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


}
