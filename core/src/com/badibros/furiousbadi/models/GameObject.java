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
    private boolean isDestroyed = false;

    private float initialX, initialY;

    public GameObject(FuriousBadi game, World world, float initialX, float initialY) {
        this.game = game;
        this.world = world;
        this.initialX = initialX;
        this.initialY = initialY;
    }

    public abstract void createBody();

    public abstract void getInputs(float delta);

    public abstract void update(float delta);

    public abstract void render(float delta);

    public abstract void afterDestroyedBody();


    public void checkDestroyFlag() {
        if (!isDestroyed() && isFlaggedForDestroy()) {
            afterDestroyedBody();
            getWorld().destroyBody(getB2d());
            setDestroyed(true);
        }
    }


    public Body getB2d() {
        return b2d;
    }

    public void setB2d(Body b2d) {
        this.b2d = b2d;
    }

    public World getWorld() {
        return world;
    }

    public FuriousBadi getGame() {
        return game;
    }

    public void destroyBody(){
        isFlaggedForDestroy = true;
    }

    public boolean isFlaggedForDestroy(){
        return isFlaggedForDestroy;
    }

    public float getInitialX() {
        return initialX;
    }

    public void setInitialX(float initialX) {
        this.initialX = initialX;
    }

    public float getInitialY() {
        return initialY;
    }

    public void setInitialY(float initialY) {
        this.initialY = initialY;
    }

    public boolean isDestroyed() {
        return isDestroyed;
    }

    public void setDestroyed(boolean destroyed) {
        isDestroyed = destroyed;
    }
}
