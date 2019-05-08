package com.sreekanth.dev.ilahianz.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sreekanth.dev.ilahianz.R;

public class slideAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public slideAdapter(Context context) {
        this.context = context;
    }

    private int[] slideImages = {
            R.mipmap.welcome,
            R.mipmap.chat_onboard,
            R.mipmap.location,
            R.mipmap.helpline
    };
    private String[] SlideHeading = {
            "Welcome", "Chat", "Location", "Help"
    };
    private String[] slide_contents = {
            "To make better Community. and Friendship",
            "Chat with your friends and loved once. " +
                    "And also you can ask questions to Teachers",
            "Locate your friends.",
            "Use Help if you need Check it out our Help Center"
    };

    @Override
    public int getCount() {
        return SlideHeading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide, container, false);

        ImageView imageView = view.findViewById(R.id.imageView);
        TextView heading = view.findViewById(R.id.heading);
        TextView contents = view.findViewById(R.id.contents);
        heading.setText(SlideHeading[position]);
        imageView.setImageResource(slideImages[position]);
        contents.setText(slide_contents[position]);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}
