package com.badibros.furiousbadi.models;

import com.badibros.furiousbadi.FuriousBadi;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;


public abstract class GameObject extends Sprite {

    public GameWorld gameWorld;

    private Body b2d;
    private World world;
    private FuriousBadi game;

    private boolean isFlaggedForDestroy = false;

    private float textureWidth;
    private float textureHeight;

    private float x,y;

    public GameObject(FuriousBadi game, World world, float x,float y){
        this.game = game;
        this.world = world;
        this.x = x;
        this.y = y;
    }

    public abstract void createBody();

    public abstract void getInputs(float delta);

    public abstract void update(float delta);

    public abstract void render(float delta);

    public Body getB2d() {
        return b2d;
    }

    public World getWorld() {
        return world;
    }

    public FuriousBadi getGame() {
        return game;
    }

    public void setB2d(Body b2d) {
        this.b2d = b2d;
    }

    public float getTextureWidth() {
        return textureWidth;
    }

    public void setTextureWidth(float textureWidth) {
        this.textureWidth = textureWidth;
    }

    public float getTextureHeight() {
        return textureHeight;
    }

    public void setTextureHeight(float textureHeight) {
        this.textureHeight = textureHeight;
    }

    public void destroyBody(){
        isFlaggedForDestroy = true;
    }

    public boolean isFlaggedForDestroy(){

        return isFlaggedForDestroy;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }
}
