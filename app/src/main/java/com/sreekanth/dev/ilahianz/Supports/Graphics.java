package com.sreekanth.dev.ilahianz.Supports;

import android.graphics.Color;

import java.util.Random;

public class Graphics {
    public static int getRandomColor() {
        Random rand = new Random();
        return Color.argb(50,
                rand.nextInt(100),
                rand.nextInt(100),
                rand.nextInt(100)
        );
    }

    public static int getRandomColorMaterial() {
        Random rand = new Random();
        return Color.rgb(rand.nextInt(200), rand.nextInt(200), rand.nextInt(200));
    }
}
