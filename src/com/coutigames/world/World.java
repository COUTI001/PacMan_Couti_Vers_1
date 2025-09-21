package com.coutigames.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import com.coutigames.entities.Enemy;
import com.coutigames.entities.Enemy2;
import com.coutigames.entities.Enemy3;
import com.coutigames.entities.Enemy4;
import com.coutigames.entities.Entity;
import com.coutigames.entities.MacaLife;
import com.coutigames.entities.MacaPontos;
import com.coutigames.entities.Player;
import com.coutigames.graficos.Spritesheet;
import com.coutigames.graficos.Ui;
import com.coutigames.main.Game;

public class World {

	public static Tile[] tiles;
	public static int WIDTH, HEIGHT;
	public static final int TILE_SIZE = 16;

	public World(String path) {// método padrão para visualização do mundo
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth());
			for (int xx = 0; xx < map.getWidth(); xx++) {
				for (int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy * map.getWidth())];
					tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);

	if (pixelAtual == 0xFF000000) { // Floor
	tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
	} else if (pixelAtual == 0xFFFFFFFF) {// Wall
	tiles[xx + (yy * WIDTH)] = new WallTile(xx * 16, yy * 16, Tile.TILE_WALL);
	}

	else if (pixelAtual == 0xFF0026FF) {// Player
	Game.player.setX(xx * 16);
	Game.player.setY(yy * 16);
	} 
	else if (pixelAtual == 0xFFFF0000) {// Enemy  //Aqui posso aumentar a velocidade do inimigo
     Enemy enemy = new Enemy(xx*16, yy*16, 16, 16, 3, Entity.ENEMY1);
      Game.entities.add(enemy);
	}
	
	else if (pixelAtual == 0xFFFF6060) {// Enemy2
	     Enemy2 enemy2 = new Enemy2(xx*16, yy*16, 16, 16, 4, Entity.ENEMY2);
	      Game.entities.add(enemy2);
			
		}
	else if (pixelAtual == 0xFFFF00A1) {// Enemy3
	     Enemy3 enemy3 = new Enemy3(xx*16, yy*16, 16, 16, 5, Entity.ENEMY3);
	      Game.entities.add(enemy3);
			
		}
	else if (pixelAtual == 0xFF91005C) {// Enemy4
	     Enemy4 enemy4 = new Enemy4(xx*16, yy*16, 16, 16, 6, Entity.ENEMY4);
	      Game.entities.add(enemy4);
			
		}
	
	else if (pixelAtual == 0xFFFFD800) {// Maçã_VERDE
		MacaPontos maca = new MacaPontos(xx*16, yy*16,16,16,0,Entity.MACA_PONTOS);
		Game.entities.add(maca);
		Game.MacaPontos_Contagem ++;
	}
	else if (pixelAtual == 0xFF006309) {// Maçã Vermelha
	MacaLife mlife = new MacaLife(xx*16, yy*16, 16, 16, 0, Entity.MACA_LIFE);
	Game.entities.add(mlife);
	}
	
	else {
	tiles[xx + (yy * WIDTH)] = new FloorTile(xx * 16, yy * 16, Tile.TILE_FLOOR);
	}	
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void restartGame(String level) {
	    // Limpa as listas e reseta os pontos
	    Game.entities.clear();
	    Game.MacaPontos_Atual = 0;
	    Game.MacaPontos_Contagem = 0;

	    // Recarrega recursos do jogo
	    Game.spritesheet = new Spritesheet("/spritesheet.png");

	    // Cria um novo jogador e reposiciona
	    Player.life = Player.maxLife; // Redefine a vida do jogador
	    Game.player = new Player(0, 0, 16, 15, 2, Game.spritesheet.getSprite(0, 0, 16, 15));

	    // Recarrega o mundo
	    Game.world = new World("/" + level);

	    // Recarrega entidades e elementos visuais
	    Game.entities.add(Game.player);
	    Game.ui = new Ui();
	}


	public static boolean isFreeDinamico(int xnext, int ynext, int width, int height) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + width - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + height - 1) / TILE_SIZE;

		int x4 = (xnext + width - 1) / TILE_SIZE;
		int y4 = (ynext + height - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

	}

	public static boolean isFree(int xnext, int ynext) {
		int x1 = xnext / TILE_SIZE;
		int y1 = ynext / TILE_SIZE;

		int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y2 = ynext / TILE_SIZE;

		int x3 = xnext / TILE_SIZE;
		int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
		int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;

		return !((tiles[x1 + (y1 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x2 + (y2 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x3 + (y3 * World.WIDTH)] instanceof WallTile)
				|| (tiles[x4 + (y4 * World.WIDTH)] instanceof WallTile));

	}

	public void render(Graphics g) {
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;

		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 3);

		for (int xx = 0; xx <= xfinal; xx++) {
			for (int yy = 0; yy <= yfinal; yy++) {
				if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy * WIDTH)];
				tile.render(g);
			}
		}

	}

}
