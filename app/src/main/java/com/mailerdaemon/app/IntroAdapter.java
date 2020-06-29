package com.mailerdaemon.app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class IntroAdapter extends PagerAdapter {

    private int[] xml={
            R.layout.intro_1,R.layout.intro_2,R.layout.intro_3,R.layout.intro_4,R.layout.intro_5,R.layout.intro_6
    };
    @Override
    public int getCount() {
        return xml.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return (view==object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=LayoutInflater.from(container.getContext()).inflate(xml[position],container,false);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

        container.removeView((LinearLayout)object);
    }
}