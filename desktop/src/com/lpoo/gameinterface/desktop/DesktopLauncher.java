package com.lpoo.gameinterface.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lpoo.gameinterface.SecretHitler;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SecretHitler.WIDTH;
		config.height = SecretHitler.HEIGHT;
		config.title = SecretHitler.TITLE;
		new LwjglApplication(new SecretHitler(), config);
	}
}
