/*
 * Copyright (c)
 * This Code
 * Author Sreekanth K R
 * Name Ilahianz
 * Github  https://github.com/sreekanthblackdevil/Ilahianz
 */

package com.sreekanth.dev.ilahianz.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sreekanth.dev.ilahianz.R;


public class StoryFragment extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_story, container, false);
    }
}
