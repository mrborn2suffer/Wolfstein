package com.engine.graphics;

import com.engine.entity.Player;
import com.engine.level.Map;

public class Raycaster {
    private int width, height;

    public Raycaster(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void render(Screen screen, Map map, Player player) {
        for (int x = 0; x < width; x++) {
            // Calculate ray position and direction
            double cameraX = 2 * x / (double) width - 1; // x-coordinate in camera space
            double rayDirX = player.dirX + player.planeX * cameraX;
            double rayDirY = player.dirY + player.planeY * cameraX;

            // Which box of the map we're in
            int mapX = (int) player.x;
            int mapY = (int) player.y;

            // Length of ray from current position to next x or y-side
            double sideDistX;
            double sideDistY;

            // Length of ray from one x or y-side to next x or y-side
            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            // What direction to step in x or y-direction (either +1 or -1)
            int stepX;
            int stepY;

            int hit = 0; // Was there a wall hit?
            int side = 0; // Was a NS or a EW wall hit?

            // Calculate step and initial sideDist
            if (rayDirX < 0) {
                stepX = -1;
                sideDistX = (player.x - mapX) * deltaDistX;
            } else {
                stepX = 1;
                sideDistX = (mapX + 1.0 - player.x) * deltaDistX;
            }
            if (rayDirY < 0) {
                stepY = -1;
                sideDistY = (player.y - mapY) * deltaDistY;
            } else {
                stepY = 1;
                sideDistY = (mapY + 1.0 - player.y) * deltaDistY;
            }

            // Perform DDA (Digital Differential Analyzer)
            while (hit == 0) {
                // Jump to next map square
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
                // Check if ray has hit a wall
                if (map.data[mapX][mapY] > 0) hit = 1;
            }

            // Calculate distance projected on camera direction
            if (side == 0) perpWallDist = (mapX - player.x + (1 - stepX) / 2.0) / rayDirX;
            else perpWallDist = (mapY - player.y + (1 - stepY) / 2.0) / rayDirY;

            // Calculate height of line to draw on screen
            int lineHeight = (int) (height / perpWallDist);

            // Calculate lowest and highest pixel to fill in current stripe
            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height) drawEnd = height - 1;

            // Choose wall color based on map number
            int color = 0;
            switch (map.data[mapX][mapY]) {
                case 1: color = 0xFF0000; break; // Red
                case 2: color = 0x00FF00; break; // Green
                case 3: color = 0x0000FF; break; // Blue
                case 4: color = 0xFFFF00; break; // Yellow
                default: color = 0xFFFFFF; break; // White
            }

            // Give x and y sides different brightness
            if (side == 1) {
                color = (color >> 1) & 8355711; // Divide color by 2 to simulate shadow
            }

            // Draw the pixels of the stripe as a vertical line
            for (int y = drawStart; y < drawEnd; y++) {
                screen.pixels[x + y * width] = color;
            }
        }
    }
}