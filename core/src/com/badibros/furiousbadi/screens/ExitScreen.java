package com.badibros.furiousbadi.screens;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;


public class ExitScreen extends GameScreen {

    private float screenTimer = 0;
    private float screenTimeout = 1;

    Texture logo;

    private float x,y;

    boolean comingFromLeft = true;
    boolean goingToRight = false;

    public ExitScreen(FuriousBadi game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        logo = new Texture("img/seeyou.png");
        y=GameVariables.HEIGHT/2-logo.getHeight()/2f/2f;
        x=-logo.getWidth();
        Gdx.app.exit();
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

        if(x>=GameVariables.WIDTH/2-logo.getWidth()/2f/2f-50){
            comingFromLeft = false;
        }
        if(x>=GameVariables.WIDTH/2-logo.getWidth()/2f/2f+50){
            goingToRight = true;
        }

        if(x>=GameVariables.WIDTH){

        }
        super.update(delta);
    }

    @Override
    public void render(float delta) {
        Gdx.app.debug("COORDINATES",x+" "+y);
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
