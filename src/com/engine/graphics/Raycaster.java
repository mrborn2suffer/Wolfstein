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
            // For Calculating ray position and direction
            double cameraX = 2 * x / (double) width - 1; //X-Cordinates
            double rayDirX = player.dirX + player.planeX * cameraX;
            double rayDirY = player.dirY + player.planeY * cameraX;

            int mapX = (int) player.x;
            int mapY = (int) player.y;

            double sideDistX; //For calculating distance to next X or Y plane/wall from current position
            double sideDistY;

            double deltaDistX = Math.abs(1 / rayDirX);
            double deltaDistY = Math.abs(1 / rayDirY);
            double perpWallDist;

            int stepX; //Direction (+1 or -1)
            int stepY;

            int hit = 0; // Detects wall
            int side = 0; // Which side of wall was hit i.e. direction.
            
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

            while (hit == 0) {  // Digital Differential Analyzer
                if (sideDistX < sideDistY) {
                    sideDistX += deltaDistX;
                    mapX += stepX;
                    side = 0;
                } else {
                    sideDistY += deltaDistY;
                    mapY += stepY;
                    side = 1;
                }
               
                if (map.data[mapX][mapY] > 0) hit = 1;
            }

            
            if (side == 0)
                perpWallDist = (mapX - player.x + (1 - stepX) / 2.0) / rayDirX; //Calculating distance
            else 
                perpWallDist = (mapY - player.y + (1 - stepY) / 2.0) / rayDirY;

            
            int lineHeight = (int) (height / perpWallDist); //Height (z-axis for 3D)

            int drawStart = -lineHeight / 2 + height / 2;
            if (drawStart < 0) drawStart = 0;
            int drawEnd = lineHeight / 2 + height / 2;
            if (drawEnd >= height) drawEnd = height - 1;

            int color = 0;
            switch (map.data[mapX][mapY]) 
            { 
                //Different wall, different colors... might add textures and shades later
                case 1: color = 0xFF0000; break; // Red
                case 2: color = 0x00FF00; break; // Green
                case 3: color = 0x0000FF; break; // Blue
                case 4: color = 0xFFFF00; break; // Yellow
                default: color = 0xFFFFFF; break; // White
            }

            if (side == 1) {
                color = (color >> 1) & 8355711; 
            }

            for (int y = drawStart; y < drawEnd; y++) 
            {
                screen.pixels[x + y * width] = color;
            }
        }
    }
}
