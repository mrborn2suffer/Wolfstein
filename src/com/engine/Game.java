package com.engine;

import com.engine.entity.Player;
import com.engine.graphics.Raycaster;
import com.engine.graphics.Screen;
import com.engine.input.Keyboard;
import com.engine.level.Map;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Game extends Canvas implements Runnable {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private boolean running = false;
    private Thread thread;

    // Fast pixel manipulation
    private BufferedImage image;
    private int[] pixels;

    private Screen screen;
    private Keyboard keyboard;
    private Map map;
    private Player player;
    private Raycaster raycaster;

    public Game() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        
        screen = new Screen(WIDTH, HEIGHT, pixels);
        keyboard = new Keyboard();
        addKeyListener(keyboard);
        
        map = new Map();
        player = new Player(4.5, 4.5); // Start in the middle of the map
        raycaster = new Raycaster(WIDTH, HEIGHT);
    }

    public synchronized void start() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; // 60 updates per second
        double delta = 0;
        requestFocus();

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while (delta >= 1) {
                update(); // Physics and input
                delta--;
            }
            render(); // Drawing
        }
    }

    private void update() {
        player.update(keyboard, map);
    }

    private void render() {
        BufferStrategy bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        screen.clear();
        raycaster.render(screen, map, player);

        Graphics g = bs.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, null);
        g.dispose();
        bs.show();
    }
}