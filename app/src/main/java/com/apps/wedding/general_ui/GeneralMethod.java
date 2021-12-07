package com.apps.wedding.general_ui;

import android.net.Uri;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;


import com.apps.wedding.R;

import com.apps.wedding.model.DepartmentModel;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class GeneralMethod {

    @BindingAdapter("error")
    public static void errorValidation(View view, String error) {
        if (view instanceof EditText) {
            EditText ed = (EditText) view;
            ed.setError(error);
        } else if (view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.setError(error);


        }
    }

    @BindingAdapter("image")
    public static void image(View view, String imageUrl) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {

                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {

                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {

                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);
            }
        }

    }

    @BindingAdapter("user_image")
    public static void user_image(View view, String imageUrl) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null) {
                Picasso.get().load(Uri.parse(imageUrl)).placeholder(R.drawable.circle_avatar).into(imageView);

            }

        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null) {
                Picasso.get().load(Uri.parse(imageUrl)).placeholder(R.drawable.circle_avatar).into(imageView);

            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null) {
                Picasso.get().load(Uri.parse(imageUrl)).placeholder(R.drawable.circle_avatar).into(imageView);

            }
        }

    }

    @BindingAdapter("departmentImage")
    public static void department_image(View view, String imageUrl) {
        if (view instanceof CircleImageView) {
            CircleImageView imageView = (CircleImageView) view;
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

            } else {
                imageView.setImageResource(R.drawable.ic_all_icon);
            }

        } else if (view instanceof RoundedImageView) {
            RoundedImageView imageView = (RoundedImageView) view;

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

            } else {
                imageView.setImageResource(R.drawable.ic_all_icon);
            }
        } else if (view instanceof ImageView) {
            ImageView imageView = (ImageView) view;

            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(Uri.parse(imageUrl)).into(imageView);

            } else {
                imageView.setImageResource(R.drawable.ic_all_icon);
            }
        }

    }


    @BindingAdapter("createAt")
    public static void dateCreateAt(TextView textView, String s) {
        if (s != null) {
            try {
                String[] dates = s.split("T");
                textView.setText(dates[0]);
            } catch (Exception e) {

            }

        }

    }


}










