package com.badibros.furiousbadi.models.gui.buttons;

import com.badibros.furiousbadi.FuriousBadi;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class ButtonModel {
    private float x;
    private float y;

    private float width;
    private float height;

    private Texture backgroundImage;
    private Texture hoverImage;
    private Texture pressedImage;

    private boolean hovered = false;
    private boolean pressed = false;

    SpriteBatch batch;

    public ButtonModel(FuriousBadi game, float x, float y, float width, float height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
        batch = game.getBatch();
    }

    public void render(float delta){
        if(isPressed()){
            batch.draw(getPressedImage(), x, y, width, height);
        } else if(isHovered()){
            batch.draw(getHoverImage(), x, y, width, height);
        } else {
            batch.draw(getBackgroundImage(), x, y, width, height);
        }
    }

    public void onHover(float screenX,float screenY){
        screenY = Gdx.graphics.getHeight() - screenY;
        if(screenX>= getX() && screenX <= getX()+getWidth() && screenY >= getY() && screenY <= getY()+getHeight()){
            setHovered(true);
        }else{
            setHovered(false);
            setPressed(false);
        }
    }

    public void onClick(float screenX,float screenY){
        screenY = Gdx.graphics.getHeight() - screenY;
        if(screenX>= getX() && screenX <= getX()+getWidth() && screenY >= getY() && screenY <= getY()+getHeight()){
            setPressed(true);
        }else{
            setPressed(false);
            setHovered(false);
        }
    }

    public void onTouchUp(){
        setPressed(false);
    }


    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public Texture getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Texture backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public Texture getHoverImage() {
        return hoverImage;
    }

    public void setHoverImage(Texture hoverImage) {
        this.hoverImage = hoverImage;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public Texture getPressedImage() {
        return pressedImage;
    }

    public void setPressedImage(Texture pressedImage) {
        this.pressedImage = pressedImage;
    }
}
