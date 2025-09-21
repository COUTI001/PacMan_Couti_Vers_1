package com.coutigames.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.coutigames.main.Game;
import com.coutigames.main.Sound;
import com.coutigames.world.Camera;
import com.coutigames.world.World;

public class Player extends Entity {

	private boolean moved = false;
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 3;
	public boolean right, left, up, down;
   
	private BufferedImage[] rp;
	private BufferedImage[] lp;
	public int right_dir = 0, left_dir = 1;
	public int dir = right_dir;
	public static double life = 100, maxLife = 100;//tem que ser double para UI ficar correto
	private BufferedImage plyDmg;
	public static boolean isDmg = false;
	private int DmgFrams = 0;
    
	public Player(double x, double y, int width, int height, int speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);

		plyDmg = Game.spritesheet.getSprite(48, 16, 16, 16);
		rp = new BufferedImage[4];
		lp = new BufferedImage[4];
		
		
		for(int i = 0; i < 4; i++) {
		rp[i] = Game.spritesheet.getSprite(0 +(i*16), 0, 16, 15);	
		}
		for(int i = 0; i < 4; i++) {
		lp[i] = Game.spritesheet.getSprite(112 -(i*16), 0, 16, 15);
		}
		
		//sprite_left = Game.spritesheet.getSprite(80, 0, 16, 16);
		
	}

	public void tick() {
		depht = 1;
		moved = false;
		if (right && World.isFree((int) (x + speed), this.getY())) {
			moved = true;
			dir = right_dir;
			x += speed;

		} else if (left && World.isFree((int) (x - speed), this.getY())) {
			moved = true;
			dir = left_dir;
			x -= speed;

		}
		if (up && World.isFree(this.getX(), (int) (y - speed))) {
			moved = true;
			y -= speed;

		} else if (down && World.isFree(this.getX(), (int) (y + speed))) {
			moved = true;
			y += speed;

		}
		
		
		 if(moved) {
			   frames++;
				if(frames == maxFrames) {
				  frames =0;
				    index++;
				     if(index > maxIndex) 
				      index = 0;
				   }
				if(isDmg) {
					DmgFrams++;
					if(DmgFrams == 8) {
						DmgFrams = 0;
						 isDmg = false;
					}
				}
				CollisionMacaPontos();
				CollisionMacaLife();
				    		 	    	
				 }		
		
	
		 updateCamera();
		 
		
	}
	private void CollisionMacaLife() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual1 = Game.entities.get(i);
			if(atual1 instanceof MacaLife) {
				if(Entity.isColliding(this, atual1)) {
					Sound.Colli_01.play();
					Game.entities.remove(atual1);					
					if(Player.life >= 100) {
						Player.life = 100;
					}	
					else {
						Player.life +=5;	
					}
					return;
				}
			}
			
		}
		
	}

	private void CollisionMacaPontos() {
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof MacaPontos) {
				if(Entity.isColliding(this, atual)) {
					Sound.Colli_01.play();
					Game.MacaPontos_Atual++;					
					Game.entities.remove(atual);
					return;
				}
				
			}
		}
		
	}
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH / 2), 0, World.WIDTH * 16 - Game.WIDTH);
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT / 2), 0, World.HEIGHT * 16 - Game.HEIGHT);
	}

	public void render(Graphics g) {
		if(!isDmg) {
		if(dir == right_dir) {
		g.drawImage(rp[index], this.getX()- Camera.x, this.getY()-Camera.y, null);
		}
		
		else if(dir == left_dir) {
		g.drawImage(lp[index],this.getX()-Camera.x, this.getY()-Camera.y, null);	
		}
		}
		else {
		g.drawImage(plyDmg, this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
	
	}
	}

