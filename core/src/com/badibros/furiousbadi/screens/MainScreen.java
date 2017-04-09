package com.badibros.furiousbadi.screens;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameScreen;
import com.badibros.furiousbadi.models.GameWorld;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.MainMenuWorld;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MainScreen extends GameScreen{

    //Current values
    public int currentScreenWidth = Gdx.graphics.getWidth();
    public int currentScreenHeight = Gdx.graphics.getHeight();
    public float scaleRatioX = currentScreenWidth*1f / GameVariables.WIDTH;
    public float scaleRatioY = currentScreenHeight*1f / GameVariables.HEIGHT;

    //Camera and viewport
    public OrthographicCamera gameCamera;
    public Viewport viewport;

    //World
    public GameWorld currentWorld;

    public MainScreen(FuriousBadi game){
        super(game);
    }

    @Override
    public void show(){

        //Initialize camera and viewport
        gameCamera = new OrthographicCamera();
        viewport = new FitViewport(GameVariables.scale(GameVariables.WIDTH),GameVariables.scale(GameVariables.HEIGHT),gameCamera);
        gameCamera.setToOrtho(false,viewport.getWorldWidth(),viewport.getWorldHeight());

        //Create World
        currentWorld = new MainMenuWorld(game, viewport, gameCamera);

        //To see debugging logs
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
    }

    @Override
    public void getInputs(float delta){
        currentWorld.getInputs(delta);
    }

    @Override
    public void update(float delta) {
        currentWorld.update(delta);
    }

    @Override
    public void render(float delta) {
        //Adds update method in superclass
        super.render(delta);

        //Clear screen
        Gdx.gl.glClearColor(1f,1f,1f,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Render world
        currentWorld.render(delta);

        //Set projection matrix to batch
        batch.setProjectionMatrix(gameCamera.combined);
        //Draw screen
        batch.begin();
        //Todo: draw components
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

        //Update viewport
        viewport.update(width,height,false);

        //Update gameCamera
        gameCamera.setToOrtho(false,viewport.getWorldWidth(),viewport.getWorldHeight());

        //Update screen values
        currentScreenWidth = Gdx.graphics.getWidth();
        currentScreenHeight = Gdx.graphics.getHeight();
        scaleRatioX = currentScreenWidth*1f / GameVariables.WIDTH;
        scaleRatioY = currentScreenHeight*1f / GameVariables.HEIGHT;

    }

    @Override
    public void dispose() {
        super.dispose();
        currentWorld.dispose();
    }
}
