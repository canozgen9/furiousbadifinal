package com.badibros.furiousbadi;

import com.badibros.furiousbadi.screens.ExitScreen;
import com.badibros.furiousbadi.screens.MainMenuScreen;
import com.badibros.furiousbadi.screens.SplashScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FuriousBadi extends Game {

	public static boolean DEBUGGING = false;

	public FPSLogger fpsLogger;

	//Batch
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		fpsLogger = new FPSLogger();
//		setScreen(new SplashScreen(this));
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render () {
		super.render();
		fpsLogger.log();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		super.dispose();
	}

	//Get and set methods
	public SpriteBatch getBatch() {
		return batch;
	}

}
