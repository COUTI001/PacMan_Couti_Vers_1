package com.coutigames.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	// public static final Sound musicbg = new Sound("/Music_01.wav");
	 public static final Sound Dano_01 = new Sound("/hurt.wav");
	// public static final Sound DanoE_01 = new Sound("/DanoE_01.wav");
	public static final Sound Colli_01 = new Sound("/Colli_01.wav");
	// public static final Sound Colis_Bul = new Sound("/Colis_Bul.wav");

	@SuppressWarnings({ "deprecation" })
	private Sound(String name) {
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name));

		} catch (Throwable e) {
		}
	}

	public void play() {
		try {
			new Thread() {
				@SuppressWarnings("deprecation")
				public void run() {
					clip.play();
				}
			}.start();

		} catch (Throwable e) {
		}
	}

	public void loop() {
		try {
			new Thread() {
				public void run() {
					clip.loop();
				}
			}.start();

		} catch (Throwable e) {
		}
	}

}
