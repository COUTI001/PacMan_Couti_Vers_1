package com.coutigames.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import com.coutigames.entities.Player;
import com.coutigames.main.Game;

public class Ui {

	public void render(Graphics g) {
   g.setColor(Color.red);
	g.fillRect(30,15,150,15);
	g.setColor(Color.green);//tem que colocar int aqui e no play double para ler corretamete
	g.fillRect(30,15,(int)((Game.player.life/Game.player.maxLife)*150),15);
	g.setColor(Color.red);
	g.setFont(new Font("arial",Font.PLAIN,20));
	g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife,68,30);
	
  g.setColor(Color.white);
  g.setFont(new Font("arial",Font.PLAIN,30));
  g.drawString("Maçãs: " + Game.MacaPontos_Atual +"/"+ Game.MacaPontos_Contagem, 425, 26);
  // Exibir o level atual
  g.setFont(new Font("arial", Font.BOLD, 25));
  g.setColor(Color.yellow);
  g.drawString("Level: " + Game.CUR_LEVEL, 290, 25);
		
		
	}
}
