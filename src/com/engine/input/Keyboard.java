package com.engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    private boolean[] keys = new boolean[65536];
    public boolean up, down, left, right;

    public void update() {
        up = keys[KeyEvent.VK_W] || keys[KeyEvent.VK_UP];
        down = keys[KeyEvent.VK_S] || keys[KeyEvent.VK_DOWN];
        left = keys[KeyEvent.VK_A] || keys[KeyEvent.VK_LEFT];
        right = keys[KeyEvent.VK_D] || keys[KeyEvent.VK_RIGHT];
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = true;
            update();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() < keys.length) {
            keys[e.getKeyCode()] = false;
            update();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {} // Not used but required by interface
}