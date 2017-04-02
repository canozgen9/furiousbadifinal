package com.badibros.furiousbadi.worlds;

import com.badibros.furiousbadi.objects.gameWorldObjects.Box;
import com.badibros.furiousbadi.objects.gameWorldObjects.Bullet;
import com.badibros.furiousbadi.objects.gameWorldObjects.Coin;
import com.badibros.furiousbadi.objects.gameWorldObjects.Enemy;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class Level1WorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug("CONTACT DETECTED",contact.getFixtureA().getUserData()+" - "+contact.getFixtureB().getUserData());
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int bit = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;

        switch (bit) {
            case GameVariables.BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
            case GameVariables.BIT_GAME_ENEMY | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
            case GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR){
                    ((MenuPlayer) fA.getUserData()).isFootContact = true;
                    ((MenuPlayer) fA.getUserData()).isJumping = false;
                }else{
                    ((MenuPlayer) fB.getUserData()).isFootContact = true;
                    ((MenuPlayer) fB.getUserData()).isJumping = false;
                }
                break;
            case GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_BULLET:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_BULLET){
                    ((Bullet) fA.getUserData()).onHitted();
                }else{
                    ((Bullet) fB.getUserData()).onHitted();
                }
                break;

            case GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR){
                    ((Enemy) fA.getUserData()).playerDetected = true;
                }else{
                    ((Enemy) fB.getUserData()).playerDetected = true;
                }
                break;
            case GameVariables.BIT_GAME_BULLET | GameVariables.BIT_GAME_ENEMY:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_ENEMY){
                    ((Enemy) fA.getUserData()).onHitted(((Bullet) fB.getUserData()).damage);
                    ((Bullet) fB.getUserData()).onHitted();
                }else{
                    ((Enemy) fB.getUserData()).onHitted(((Bullet) fA.getUserData()).damage);
                    ((Bullet) fA.getUserData()).onHitted();
                }
                break;
            case GameVariables.BIT_GAME_BULLET | GameVariables.BIT_GAME_BOX:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_BOX){
                    ((Box) fA.getUserData()).onHitted(((Bullet) fB.getUserData()).damage);
                    ((Bullet) fB.getUserData()).onHitted();
                }else{
                    ((Box) fB.getUserData()).onHitted(((Bullet) fA.getUserData()).damage);
                    ((Bullet) fA.getUserData()).onHitted();
                }
                break;
            case GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_COIN:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_GAME_COIN) {
                    ((Coin) fA.getUserData()).onHitted();
                } else {
                    ((Coin) fB.getUserData()).onHitted();
                }
                break;
            default:

        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.debug("CONTACT LEAVED",contact.getFixtureA().getUserData()+" - "+contact.getFixtureB().getUserData());
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int bit = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;

        switch (bit) {
            case GameVariables.BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
            case GameVariables.BIT_GAME_ENEMY | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
            case GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR){
                    ((MenuPlayer) fA.getUserData()).isFootContact = false;
                    ((MenuPlayer) fA.getUserData()).isJumping = true;
                }else{
                    ((MenuPlayer) fB.getUserData()).isFootContact = false;
                    ((MenuPlayer) fB.getUserData()).isJumping = true;
                }
                break;
            case GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR){
                    ((Enemy) fA.getUserData()).playerDetected = false;
                }else{
                    ((Enemy) fB.getUserData()).playerDetected = false;
                }
                break;
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
