package com.engine.graphics;

public class Screen {
    public int width, height;
    public int[] pixels;

    public Screen(int width, int height, int[] pixels) {
        this.width = width;
        this.height = height;
        this.pixels = pixels;
    }

    public void clear() {
        for (int i = 0; i < pixels.length; i++) {
            // Top half is dark grey (ceiling), bottom is light grey (floor)
            if (i < pixels.length / 2) {
                pixels[i] = 0x333333; 
            } else {
                pixels[i] = 0x555555; 
            }
        }
    }
}