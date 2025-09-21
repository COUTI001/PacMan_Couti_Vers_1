package com.coutigames.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.coutigames.main.Game;
import com.coutigames.main.Sound;
import com.coutigames.world.AStar;
import com.coutigames.world.Camera;
import com.coutigames.world.Vector2i;

public class Enemy3 extends Entity{

	
	private boolean modoFantasma = false;
	private int frameFantasma = 0;
	private int nextTime = Entity.rand.nextInt(60*4 - 60*2) + 60*2;
	private int maskx = 7,masky = 7,maskw = 9,maskh = 9;

	public Enemy3(double x, double y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, null);

	}

	public void tick() {
		depht = 0;
		if(!isColiddingWithPlayer()) {
		if(modoFantasma == false) {
       if(path == null || path.size() == 0) { 
      Vector2i start = new Vector2i(((int)(x/16)), ((int)(y/16))); 
     Vector2i end = new Vector2i((int)(Game.player.x/16), (int)(Game.player.y/16)); 
      path = AStar.findPath(Game.world, start, end);  
       }
       followPath(path);
		 }
		}else { 
    	   if(isColiddingWithPlayer() == true) {
    		   if(new Random().nextInt(100) < 25)
    	        Player.life -=new Random().nextInt(3);
    		      Sound.Dano_01.play();
    		      Player.isDmg = true;
    		      return;
    		
	}
	 
		 else {
			 modoFantasma = true; 
		 }
		}
		frameFantasma++;
		if(frameFantasma == nextTime) {
			nextTime = Entity.rand.nextInt(60*4 - 60*2) + 60*2;
			frameFantasma = 0;
			if(modoFantasma == false) {
				modoFantasma = true;
			
			}else {
				modoFantasma = false;
				
			}
		  }
		
		}
		
	private  boolean isColiddingWithPlayer(){
	Rectangle enemyCurrent = new Rectangle(this.getX()+maskx-Camera.x,this.getY()+masky-2-Camera.y,maskw,maskh);
	Rectangle player = new Rectangle(Game.player.getX()-Camera.x,Game.player.getY()-Camera.y,16,16);
		
		return enemyCurrent.intersects(player);
	}
		
	

	public void render(Graphics g) {
		if(modoFantasma == false) {
	 g.drawImage(ENEMY3, getX()-Camera.x, getY()-Camera.y, null);
		
	}else {
	 g.drawImage(ENEMYDG, getX()-Camera.x, getY()-Camera.y, null);
	}
	
	}
}
