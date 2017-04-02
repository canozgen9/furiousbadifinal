package com.badibros.furiousbadi.models;

import com.badibros.furiousbadi.FuriousBadi;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;


public class GameScreen implements Screen {

    protected FuriousBadi game;
    protected Batch batch;

    public GameScreen(FuriousBadi game) {
        this.game = game;
        batch = game.getBatch();
    }

    @Override
    public void show() {

    }

    public void getInputs(float delta) {

    }

    public void update(float delta) {

    }

    @Override
    public void render(float delta) {
        getInputs(delta);
        update(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
