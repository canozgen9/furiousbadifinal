package com.badibros.furiousbadi.desktop;

import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badibros.furiousbadi.FuriousBadi;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = GameVariables.WIDTH;
		config.height = GameVariables.HEIGHT;
		new LwjglApplication(new FuriousBadi(), config);
	}
}
