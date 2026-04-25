package com.engine.entity;

import com.engine.input.Keyboard;
import com.engine.level.Map;

public class Player {
    public double x, y;
    public double dirX = -1, dirY = 0; // Initial direction vector
    public double planeX = 0, planeY = 0.66; // 2D Raycaster camera plane (FOV)
    
    private final double moveSpeed = 0.05;
    private final double rotSpeed = 0.04;

    public Player(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update(Keyboard key, Map map) {
        // Move forward
        if (key.up) {
            if (map.data[(int)(x + dirX * moveSpeed)][(int)y] == 0) x += dirX * moveSpeed;
            if (map.data[(int)x][(int)(y + dirY * moveSpeed)] == 0) y += dirY * moveSpeed;
        }
        // Move backward
        if (key.down) {
            if (map.data[(int)(x - dirX * moveSpeed)][(int)y] == 0) x -= dirX * moveSpeed;
            if (map.data[(int)x][(int)(y - dirY * moveSpeed)] == 0) y -= dirY * moveSpeed;
        }
        // Rotate right
        if (key.right) {
            double oldDirX = dirX;
            dirX = dirX * Math.cos(-rotSpeed) - dirY * Math.sin(-rotSpeed);
            dirY = oldDirX * Math.sin(-rotSpeed) + dirY * Math.cos(-rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(-rotSpeed) - planeY * Math.sin(-rotSpeed);
            planeY = oldPlaneX * Math.sin(-rotSpeed) + planeY * Math.cos(-rotSpeed);
        }
        // Rotate left
        if (key.left) {
            double oldDirX = dirX;
            dirX = dirX * Math.cos(rotSpeed) - dirY * Math.sin(rotSpeed);
            dirY = oldDirX * Math.sin(rotSpeed) + dirY * Math.cos(rotSpeed);
            double oldPlaneX = planeX;
            planeX = planeX * Math.cos(rotSpeed) - planeY * Math.sin(rotSpeed);
            planeY = oldPlaneX * Math.sin(rotSpeed) + planeY * Math.cos(rotSpeed);
        }
    }
}