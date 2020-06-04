package com.norbertoledo.petportal.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapTask {

    /**
     * CREATE BITMAP METHOD
     */
    public static Bitmap createBitmap(Context context, Uri selectedImageUri){
        InputStream is;
        Bitmap bitmap = null;
        try {
            is = context.getContentResolver().openInputStream(selectedImageUri);
            bitmap = BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            Log.d("createBitmap -> ", "Error: "+e.getMessage() );
        }

        return bitmap;

    }

    /**
     * RESIZE METHODS
     */
    public static Bitmap resizeStaticWidth( Bitmap originalBitmap, int maxWidthImageSize, boolean filter ){

        int percent = maxWidthImageSize * 100 / originalBitmap.getWidth();
        int height = originalBitmap.getHeight() * percent / 100;

        Bitmap newBitmap = Bitmap.createScaledBitmap(originalBitmap, maxWidthImageSize, height, filter);
        return newBitmap;
    }

    public static Bitmap resizeStaticHeight( Bitmap originalBitmap, int maxHeightImageSize, boolean filter ){

        int percent = maxHeightImageSize * 100 / originalBitmap.getWidth();
        int width = originalBitmap.getHeight() * percent / 100;

        Bitmap newBitmap = Bitmap.createScaledBitmap(originalBitmap, width, maxHeightImageSize, filter);
        return newBitmap;
    }

    public static Bitmap resizeDynamicRatio( Bitmap originalBitmap, float maxImageSize, boolean filter ){

        // Devuelve el minimo ratio y ese lado lo lleva a maxImageSize. El minimo ratio siempre es el lado mas grande
        // El resultado es que si la foto es  vertical, el alto será el maxImageSize. Sino, será el ancho el maxImageSize.
        float ratio = Math.min(
                maxImageSize / originalBitmap.getWidth(),
                maxImageSize / originalBitmap.getHeight());
        int width = Math.round( ratio * originalBitmap.getWidth());
        int height = Math.round( ratio * originalBitmap.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, filter);
        return newBitmap;
    }

    /**
     * COMPRESS METHOD
     */
    public static File compressToFile(Context context, String filename, Bitmap bitmap, int quality){
        File filesDir = null;
        File imageFile = null;
        try{
            filesDir = context.getFilesDir();
            imageFile = new File(filesDir, filename);
            OutputStream os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.WEBP, quality, os);
            os.flush();
            os.close();

        } catch (Exception e) {
                Log.d("compressToFile -> ", "Error: "+e.getMessage() );
        }
        return imageFile;
    }







}
