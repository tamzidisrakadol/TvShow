package com.example.tvshow.utilities;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindAdapters {

    //set to ImageView with animation
    @BindingAdapter("android:imgUrl")
    public static void setImageUrl(ImageView imageView,String URL){
        try {
            imageView.setAlpha(0f);
            Picasso.get().load(URL).into(imageView, new Callback() {
                @Override
                public void onSuccess() {
                    imageView.animate().setDuration(300).alpha(1f).start();
                }

                @Override
                public void onError(Exception e) {

                }
            });
        }catch (Exception e){

        }

    }
}
