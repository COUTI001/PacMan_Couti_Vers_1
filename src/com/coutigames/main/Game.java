/** Produzido por André Luiz Coutinho
 * 
 */

package com.coutigames.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.WindowConstants;


import com.coutigames.entities.Entity;
import com.coutigames.entities.Player;
import com.coutigames.graficos.Spritesheet;
import com.coutigames.graficos.Ui;
import com.coutigames.world.World;

public class Game extends Canvas implements Runnable, KeyListener {

	private static final long serialVersionUID = 1L;
	public static JFrame jf;
	private Thread th;
	private boolean isRunning = true;
	public static final int WIDTH = 320;
	public static final int HEIGHT = 320;
	public static final int SCALE = 2;
	private BufferedImage image;

	public static List<Entity> entities;

	public static int CUR_LEVEL = 1;
	private int MAX_LEVEL = 15;
	public static Spritesheet spritesheet;
	public static Player player;
	public static World world;
	
	public static int MacaPontos_Contagem = 0;
    public static int MacaPontos_Atual= 0;
	
	public static Ui ui;
	public static String gameState = "MENU";
	private static boolean showMessageGameOver = true;
	private double framesGameOver = 0;
	
	public static boolean restartGame = false;
	
	private static final String[] menuOptions = {"Iniciar Jogo", "Instruções", "Créditos", "Sair"};
	private int currentMenuOption = 0;
	private boolean showInstructions = false;
	private boolean showCredits = false;
	
	public Game() {
        //Sound.musicBackground.loop();

		addKeyListener(this);
		setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		initFrame();
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		// inicialização de objetos
		spritesheet = new Spritesheet("/spritesheet.png");
		entities = new ArrayList<Entity>();
		
		player = new Player(0, 0, 16, 15, 2, spritesheet.getSprite(0, 0, 16, 15));
		world = new World("/level1.png");
		ui = new Ui();
		

		entities.add(player);

	}

	private void initFrame() {
		jf = new JFrame("Pac_Couti");
		jf.add(this);
		jf.setResizable(false);
		jf.pack();
		jf.setLocationRelativeTo(null);
		jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		jf.setVisible(true);

	}

	public synchronized void start() {
		th = new Thread(this);
		isRunning = true;
		th.start();
	}

	public synchronized void stop() {

	}

	public static void main(String args[]) {
		Game game = new Game();
		game.start();

	}

	public void tick() {
	    if (gameState.equals("MENU") || gameState.equals("PAUSE")) {
	        // Não faz nada no menu ou pause
	        return;
	    }
	    if (gameState.equals("NORMAL")) {
	        this.restartGame = false;

	        // Atualiza todas as entidades
	        for (int i = 0; i < entities.size(); i++) {
	            Entity e = entities.get(i);
	            e.tick();
	        }

	        // Verifica se o jogador coletou todos os pontos no nível
	        if (Game.MacaPontos_Atual == Game.MacaPontos_Contagem) {
	            CUR_LEVEL++;
	            if (CUR_LEVEL > MAX_LEVEL) {
	                CUR_LEVEL = 1;
	            }
	            String newWorld = "level" + CUR_LEVEL + ".png";
	            World.restartGame(newWorld);
	        }
	    } else if (gameState.equals("GAME OVER")) {
	        this.framesGameOver++;

	        // Alterna mensagem piscante de GAME OVER
	        if (this.framesGameOver == 30) {
	            this.framesGameOver = 0;
	            Game.showMessageGameOver = !Game.showMessageGameOver;
	        }

	        // Reinicia o jogo ao pressionar ENTER
	        if (restartGame) {
	            Game.restartGame = false;
	            Game.gameState = "NORMAL";
	            CUR_LEVEL = 1;
	            String newWorld = "level" + CUR_LEVEL + ".png";
	            World.restartGame(newWorld);
	            Player.life = Player.maxLife; // Restaura a vida do jogador
	        }
	    }

	    // Detecta quando o jogador perde toda a vida e muda para GAME OVER
	    if (Player.life <= 0 && !gameState.equals("GAME OVER")) {
	        Game.gameState = "GAME OVER";
	        this.framesGameOver = 0; // Reinicia o contador de frames para o efeito
	    }
	}

	public void render() {
	    BufferStrategy bs = this.getBufferStrategy();
	    if (bs == null) {
	        this.createBufferStrategy(3);
	        return;
	    }

	    Graphics g = image.getGraphics();
	    g.setColor(new Color(0, 0, 0));
	    g.fillRect(0, 0, WIDTH, HEIGHT);
	    if (gameState.equals("MENU")) {
	        g.setColor(Color.yellow);
	        g.setFont(new Font("Arial", Font.BOLD, 30));
	        g.drawString("PAC-MAN COUTI", 38, 70);
	        g.setFont(new Font("Arial", Font.PLAIN, 24));
	        if (showInstructions) {
	            g.setColor(Color.white);
	            g.setFont(new Font("Arial", Font.PLAIN, 16));
	            g.drawString("INSTRUÇÕES:", 90, 120);
	            g.drawString("- Use as setas ou WASD para mover.", 30, 160);
	            g.drawString("- Colete todos os pontos para avançar.", 30, 190);
	            g.drawString("- Evite os inimigos!", 30, 220);
	            g.drawString("- Pressione ESC para voltar.", 30, 260);
	        } else if (showCredits) {
	            g.setColor(Color.white);
	            g.setFont(new Font("Arial", Font.PLAIN, 16));
	            g.drawString("CRÉDITOS:", 110, 120);
	            g.drawString("Desenvolvido por André Luiz Coutinho", 30, 160);
	            g.drawString("Baseado no clássico Pac-Man.", 30, 190);
	            g.drawString("Pressione ESC para voltar.", 30, 260);
	        } else {
	            for (int i = 0; i < menuOptions.length; i++) {
	                if (i == currentMenuOption) {
	                    g.setColor(Color.red);
	                } else {
	                    g.setColor(Color.white);
	                }
	                g.drawString(menuOptions[i], 100, 120 + i * 40);
	            }
	            g.setFont(new Font("Arial", Font.PLAIN, 10));
	            g.setColor(Color.gray);
	            g.drawString("Use as setas para navegar e ENTER para selecionar.", 40, 300);
	        }
	        g.dispose();
	        g = bs.getDrawGraphics();
	        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
	        bs.show();
	        return;
	    }
	    if (gameState.equals("PAUSE")) {
	        world.render(g);
	        Collections.sort(entities, Entity.nodeSorter);
	        for (int i = 0; i < entities.size(); i++) {
	            Entity e = entities.get(i);
	            e.render(g);
	        }
	        ui.render(g);
	        g.dispose();
	        g = bs.getDrawGraphics();
	        g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
	        g.setColor(new Color(0,0,0,150));
	        g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
	        g.setFont(new Font("Arial", Font.BOLD, 30));
	        g.setColor(Color.yellow);
	        g.drawString("Jogo Pausado", 60, 110);
	        g.setFont(new Font("Arial", Font.PLAIN, 18));
	        g.setColor(Color.white);
	        g.drawString("Pressione ESC para continuar", 45, 150);
	        bs.show();
	        return;
	    }
	    world.render(g);
	    Collections.sort(entities, Entity.nodeSorter);
	    for (int i = 0; i < entities.size(); i++) {
	        Entity e = entities.get(i);
	        e.render(g);
	    }
	    ui.render(g);
	    g.dispose();
	    g = bs.getDrawGraphics();
	    g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
	    
	    if(gameState == "GAME OVER") {//é sempre melhor usar .equals para comparar refer de Strings
	    	Graphics2D g2 = (Graphics2D) g;
	        g2.setColor(new Color(0,0,0,100));
	        g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
	        g.setFont(new Font("arial", Font.BOLD, 50));
	        g.setColor(Color.red);
	        g.drawString("Game Over", 170, 200);
	        if(showMessageGameOver) {
	          g.drawString("(((Press Enter)))", 100, 300);
	        }          
	    }
	    bs.show();
}


	
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		requestFocus();
		while (isRunning) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if (delta >= 1) {
				tick();
				render();
				frames++;
				delta--;
			}
			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("FPS:" + frames);
				frames = 0;
				timer += 1000;
			}

		}

	}

	public void keyPressed(KeyEvent e) {
	    if (gameState.equals("MENU")) {
	        if (showInstructions || showCredits) {
	            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
	                showInstructions = false;
	                showCredits = false;
	            }
	            return;
	        }
	        if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
	            currentMenuOption--;
	            if (currentMenuOption < 0) currentMenuOption = menuOptions.length - 1;
	        } else if (e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
	            currentMenuOption++;
	            if (currentMenuOption >= menuOptions.length) currentMenuOption = 0;
	        } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	            switch (currentMenuOption) {
	                case 0: // Iniciar Jogo
	                    gameState = "NORMAL";
	                    break;
	                case 1: // Instruções
	                    showInstructions = true;
	                    break;
	                case 2: // Créditos
	                    showCredits = true;
	                    break;
	                case 3: // Sair
	                    System.exit(0);
	                    break;
	            }
	        }
	        return;
	    }
		if (gameState.equals("NORMAL")) {
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        gameState = "PAUSE";
		        return;
		    }
		} else if (gameState.equals("PAUSE")) {
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		        gameState = "NORMAL";
		        return;
		    }
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;

		} else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;

		}
		if (e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {

			player.up = true;

		} else if (e.getKeyCode() == KeyEvent.VK_DOWN || 
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;

		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			this.restartGame = true;
			
			
		}

	}

	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT || 
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
           player.up = false;
		}

		else if (e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
           player.down = false;
		}
		if (e.getKeyCode() == KeyEvent.VK_ENTER){
			this.restartGame = false;
			
		}
		
	}

	public void keyTyped(KeyEvent e) {

	}

}
