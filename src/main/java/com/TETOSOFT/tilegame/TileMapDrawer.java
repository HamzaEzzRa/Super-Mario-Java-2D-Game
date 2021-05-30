package com.TETOSOFT.tilegame;

import java.awt.*;
import java.util.Iterator;

import com.TETOSOFT.graphics.Sprite;
import com.TETOSOFT.tilegame.sprites.Creature;

/**
    The TileMapRenderer class draws a TileMap on the screen.
    It draws all tiles, sprites, and an optional background image
    centered around the position of the player.

    <p>If the width of background image is smaller the width of
    the tile map, the background image will appear to move
    slowly, creating a parallax background effect.

    <p>Also, three static methods are provided to convert pixels
    to tile positions, and vice-versa.

    <p>This TileMapRender uses a tile size of 64.
*/
public class TileMapDrawer {
    private static final int TILE_SIZE = 64;
    // the size in bits of the tile
    // Math.pow(2, TILE_SIZE_BITS) == TILE_SIZE
    private static final int TILE_SIZE_BITS = 6;

    private Image[] backgrounds;
    private int[] backgroundStartPositions;
    private float[] backgroundParallaxFactors;
    
    private Image[] foregrounds;
    private int[] foregroundStartPositions;
    private float[] foregroundParallaxFactors;

    /**
        Converts a pixel position to a tile position.
    */
    public static int pixelsToTiles(float pixels) {
        return pixelsToTiles(Math.round(pixels));
    }

    /**
        Converts a pixel position to a tile position.
    */
    public static int pixelsToTiles(int pixels) {
        // use shifting to get correct values for negative pixels
        return pixels >> TILE_SIZE_BITS;

        // or, for tile sizes that aren't a power of two,
        // use the floor function:
        //return (int)Math.floor((float)pixels / TILE_SIZE);
    }

    /**
        Converts a tile position to a pixel position.
    */
    public static int tilesToPixels(int numTiles) {
        // no real reason to use shifting here.
        // it's slighty faster, but doesn't add up to much
        // on modern processors.
        return numTiles << TILE_SIZE_BITS;

        // use this if the tile size isn't a power of 2:
        //return numTiles * TILE_SIZE;
    }

    /**
        Sets the backgrounds to draw.
    */
    public void setBackgrounds(Image[] backgrounds) {
        if (backgrounds == null)
            return;
        
        this.backgrounds = backgrounds;
        
        this.backgroundStartPositions = new int[backgrounds.length];
        for (int i = 0; i < backgrounds.length; i++)
            this.backgroundStartPositions[i] = 0;
    }
    
    /**
        Sets the foregrounds to draw.
    */
    public void setForegrounds(Image[] foregrounds) {
        if (foregrounds == null)
            return;
        
        this.foregrounds = foregrounds;
        
        this.foregroundStartPositions = new int[foregrounds.length];
        for (int i = 0; i < foregrounds.length; i++)
            this.foregroundStartPositions[i] = 0;
    }
    
    /**
     * Sets a list of parallax factor, one for each background image.
     */
    public void setBackgroundParallaxFactors(float[] parallaxFactors) {
        if (backgrounds == null)
            return;
        
        this.backgroundParallaxFactors = new float[backgrounds.length];
        for (int i = 0; i < backgrounds.length; i++)
            this.backgroundParallaxFactors[i] = 1f;
        
        System.arraycopy(parallaxFactors, 0, this.backgroundParallaxFactors, 0, parallaxFactors.length);
    }
    
    /**
     * Sets a list of parallax factor, one for each foreground image.
     */
    public void setForegroundParallaxFactors(float[] parallaxFactors) {
        if (foregrounds == null)
            return;
        
        this.foregroundParallaxFactors = new float[foregrounds.length];
        for (int i = 0; i < foregrounds.length; i++)
            this.foregroundParallaxFactors[i] = 1f;
        
        System.arraycopy(parallaxFactors, 0, this.foregroundParallaxFactors, 0, parallaxFactors.length);
    }
    
    /**
     * Resets start position to zero for all background / foreground images.
     */
    public void resetStartPositions() {
        if (backgrounds != null) {
            for (int i = 0; i < backgrounds.length; i++)
                this.backgroundStartPositions[i] = 0;
        }
        
        if (foregrounds != null) {
            for (int i = 0; i < foregrounds.length; i++)
                this.foregroundStartPositions[i] = 0;
        }
    }

    /**
     * Apply parallax effect to the image with infinite scrolling
     * @param g
     * @param index
     * @param screenWidth
     * @param screenHeight
     * @param offsetX
     * @param mapWidth 
     */
    private void applyBackgroundParallax(Graphics2D g, int index, int screenWidth, int screenHeight,
        int offsetX, int mapWidth) {
        int temp = ((int)(offsetX * backgroundParallaxFactors[index])) *
            (screenWidth - backgrounds[index].getWidth(null)) /
            (screenWidth - mapWidth);
        if (temp > backgroundStartPositions[index] + backgrounds[index].getWidth(null))
            backgroundStartPositions[index] += backgrounds[index].getWidth(null);
        if (temp - screenWidth < backgroundStartPositions[index] - backgrounds[index].getWidth(null))
            backgroundStartPositions[index] -= backgrounds[index].getWidth(null);

        int x = temp - backgroundStartPositions[index];
        int y = screenHeight - backgrounds[index].getHeight(null);

        g.drawImage(backgrounds[index], x - backgrounds[index].getWidth(null), y, null);
        g.drawImage(backgrounds[index], x, y, null);
    }
    
    private void applyForegroundParallax(Graphics2D g, int index, int screenWidth, int screenHeight,
        int offsetX, int mapWidth) {
        int temp = ((int)(offsetX * foregroundParallaxFactors[index])) *
            (screenWidth - foregrounds[index].getWidth(null)) /
            (screenWidth - mapWidth);
        if (temp > foregroundStartPositions[index] + foregrounds[index].getWidth(null))
            foregroundStartPositions[index] += foregrounds[index].getWidth(null);
        if (temp - screenWidth < foregroundStartPositions[index] - foregrounds[index].getWidth(null))
            foregroundStartPositions[index] -= foregrounds[index].getWidth(null);

        int x = temp - foregroundStartPositions[index];
        int y = screenHeight - foregrounds[index].getHeight(null);

        g.drawImage(foregrounds[index], x - foregrounds[index].getWidth(null), y, null);
        g.drawImage(foregrounds[index], x, y, null);
    }
    
    /**
        Draws the specified TileMap.
    */
    public void draw(Graphics2D g, TileMap map,
        int screenWidth, int screenHeight) {
        Sprite player = map.getPlayer();
        int mapWidth = tilesToPixels(map.getWidth());

        // get the scrolling position of the map
        // based on player's position
        int offsetX = screenWidth / 2 -
            Math.round(player.getX()) - TILE_SIZE;
        offsetX = Math.min(offsetX, 0);
        offsetX = Math.max(offsetX, screenWidth - mapWidth);

        // get the y offset to draw all sprites and tiles
        int offsetY = screenHeight -
            tilesToPixels(map.getHeight());

        // draw black background, if needed
        if (backgrounds == null || backgrounds.length == 0 ||
            screenHeight > backgrounds[0].getHeight(null)) {
            g.setColor(Color.black);
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // draw parallax background images
        if (backgrounds != null) {
            for (int i = 0; i < backgrounds.length; i++)
                applyBackgroundParallax(g, i, screenWidth, screenHeight, offsetX, mapWidth);
        }

        // draw the visible tiles
        int firstTileX = pixelsToTiles(-offsetX);
        int lastTileX = firstTileX + pixelsToTiles(screenWidth) + 1;
        for (int y=0; y<map.getHeight(); y++) {
            for (int x=firstTileX; x <= lastTileX; x++) {
                Image image = map.getTile(x, y);
                if (image != null) {
                    g.drawImage(image,
                        tilesToPixels(x) + offsetX,
                        tilesToPixels(y) + offsetY,
                        null);
                }
            }
        }

        // draw player
        g.drawImage(player.getImage(),
            Math.round(player.getX()) + offsetX,
            Math.round(player.getY()) + offsetY,
            null);

        // draw sprites
        Iterator i = map.getSprites();
        while (i.hasNext()) {
            Sprite sprite = (Sprite)i.next();
            int x = Math.round(sprite.getX()) + offsetX;
            int y = Math.round(sprite.getY()) + offsetY;
            g.drawImage(sprite.getImage(), x, y, null);

            // wake up the creature when it's on screen
            if (sprite instanceof Creature &&
                x >= 0 && x < screenWidth)
            {
                ((Creature)sprite).wakeUp();
            }
        }
        
        // draw parallax foreground images
        if (foregrounds != null) {
            for (int j = 0; j < foregrounds.length; j++)
                applyForegroundParallax(g, j, screenWidth, screenHeight, offsetX, mapWidth);
        }
    }
}
