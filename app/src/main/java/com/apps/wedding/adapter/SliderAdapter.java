package com.apps.wedding.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.viewpager.widget.PagerAdapter;

import com.apps.wedding.R;
import com.apps.wedding.databinding.SliderBinding;
import com.apps.wedding.model.WeddingHallModel;
import com.apps.wedding.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter  extends PagerAdapter {


    List<WeddingHallModel.ServiceImage> IMAGES;
    private LayoutInflater inflater;
    Context context;

    public SliderAdapter(List<WeddingHallModel.ServiceImage> IMAGES,Context context) {
        this.context = context;
        this.IMAGES=IMAGES;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return IMAGES.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        SliderBinding rowBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.slider,view,false);


        Picasso.get().load(Uri.parse(IMAGES.get(position).getImage())).fit().into(rowBinding.image);

        view.addView(rowBinding.getRoot());
        return rowBinding.getRoot();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}