package com.badibros.furiousbadi.screens;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


public class SplashScreen extends GameScreen {

    private float screenTimer = 0;
    private float screenTimeout = 5;

    Texture logo;

    private float x,y;

    boolean comingFromLeft = true;
    boolean goingToRight = false;

    public SplashScreen(FuriousBadi game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        logo = new Texture("img/company_logo.png");
        y=Gdx.graphics.getHeight()/2-logo.getHeight()/2f/2f;
        x=-logo.getWidth();
    }

    @Override
    public void update(float delta) {
        if(comingFromLeft){
            x+=10;
        } else if(goingToRight){
            x+=10;
        } else {
            x++;
        }

        if(x>=Gdx.graphics.getWidth()/2-logo.getWidth()/2f/2f-50){
            comingFromLeft = false;
        }
        if(x>=Gdx.graphics.getWidth()/2-logo.getWidth()/2f/2f+50){
            goingToRight = true;
        }

        if(x>=Gdx.graphics.getWidth()){
            game.setScreen(new MainMenuScreen(game));
        }
        super.update(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,1,1,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(logo,x,y, logo.getWidth()/2f,logo.getHeight()/2f);
        batch.end();
        super.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
