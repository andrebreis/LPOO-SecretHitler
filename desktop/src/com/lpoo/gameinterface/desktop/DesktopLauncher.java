package com.lpoo.gameinterface.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.lpoo.gameinterface.SecretHtiler;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = SecretHtiler.WIDTH;
		config.height = SecretHtiler.HEIGHT;
		config.title = SecretHtiler.TITLE;
		new LwjglApplication(new SecretHtiler(), config);
	}
}
