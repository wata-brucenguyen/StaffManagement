package com.example.staffmanagement.View.Ultils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ImageHandler {

    public static void loadImageFromBytes(Context context, byte[] bytesImage, ImageView imageView) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytesImage, 0, bytesImage.length);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ((Activity) context).getWindowManager().getDefaultDisplay().getRealMetrics(displayMetrics);
            imageView.setMinimumHeight(displayMetrics.heightPixels);
            imageView.setMinimumWidth(displayMetrics.widthPixels);
        }
        imageView.setImageBitmap(bitmap);
    }

    public static Bitmap getBitmapFromUriAndShowImage(Context context, Uri imageUri,ImageView imageView){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);
            if (imageView != null)
                imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static byte[] getByteArrayFromBitmap(Bitmap bitmap){
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,bao);
        return bao.toByteArray();
    }
}
