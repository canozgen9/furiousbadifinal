package com.badibros.furiousbadi;

import com.badibros.furiousbadi.screens.MainScreen;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FuriousBadi extends Game {

	public static boolean DEBUGGING = false;

	private FPSLogger fpsLogger;

	//Batch
	private SpriteBatch batch;

	@Override
	public void create () {
		batch = new SpriteBatch();
		fpsLogger = new FPSLogger();
//		setScreen(new SplashScreen(this));
		setScreen(new MainScreen(this));
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
