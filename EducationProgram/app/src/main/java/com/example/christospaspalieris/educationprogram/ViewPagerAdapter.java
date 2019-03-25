package com.example.christospaspalieris.educationprogram;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by papac on 6/7/2017.
 */

public class ViewPagerAdapter extends PagerAdapter {
    List<Integer> lstImages;
    Context context;
    LayoutInflater inflater;

    public ViewPagerAdapter(List<Integer> lstImages, Context context) {
        this.lstImages = lstImages;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.list_item,container,false);
        ImageView imageView = (ImageView)view.findViewById(R.id.imageView);
        imageView.setImageResource(lstImages.get(position));

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return lstImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
}
