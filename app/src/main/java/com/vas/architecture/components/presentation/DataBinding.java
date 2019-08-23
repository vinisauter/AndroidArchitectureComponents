package com.vas.architecture.components.presentation;

import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.squareup.picasso.Picasso;
import com.vas.architecture.R;

import java.io.File;

public class DataBinding {
    @BindingAdapter({"android:src"})
    public static void loadImageUrl(ImageView view, Uri uri) {
        Picasso.get()
                .load(uri)
                .placeholder(R.drawable.github)
                .into(view);
    }

    @BindingAdapter({"android:src"})
    public static void loadImageUrl(ImageView view, String path) {
        Picasso.get()
                .load(path)
                .placeholder(R.drawable.github)
                .into(view);
    }

    @BindingAdapter({"android:src"})
    public static void loadImageUrl(ImageView view, File file) {
        Picasso.get()
                .load(file)
                .placeholder(R.drawable.github)
                .into(view);
    }

    @BindingAdapter({"android:text"})
    public static void loadImage(TextView view, Number number) {
        if (number != null)
            view.setText(number.toString());
        else {
            view.setText("0");
        }
    }
}
