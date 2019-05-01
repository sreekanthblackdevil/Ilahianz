package com.sreekanth.dev.ilahianz.Supports;

import android.graphics.Color;

import java.util.Random;

public class Graphics {
    private int getRandomColor() {
        Random rand = new Random();
        return Color.argb(100, rand.nextInt(256), rand.nextInt(256), rand.nextInt(256));
    }
}
