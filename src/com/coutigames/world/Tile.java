package com.coutigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.coutigames.main.Game;



public class Tile {

	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(0, 64, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(16, 64, 16, 16);
	// public static BufferedImage TILE_MATO1 = Game.spritesheet.getSprite(0, 112,
	// 16, 16);

	private BufferedImage sprite;
	private int x, y;

	public Tile(int x, int y, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}

	public void render(Graphics g) {
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
