package com.badibros.furiousbadi.worlds.contactlisteners;

import com.badibros.furiousbadi.models.player.BulletModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.Box;
import com.badibros.furiousbadi.objects.gameWorldObjects.Coin;
import com.badibros.furiousbadi.objects.gameWorldObjects.Enemy;
import com.badibros.furiousbadi.objects.gameWorldObjects.Player;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class LevelWorldContactListener implements ContactListener {
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
                    ((Player) fA.getUserData()).isFootContact = true;
                    ((Player) fA.getUserData()).isJumping = false;
                }else{
                    ((Player) fB.getUserData()).isFootContact = true;
                    ((Player) fB.getUserData()).isJumping = false;
                }
                break;
            case GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_BULLET:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_BULLET){
                    ((BulletModel) fA.getUserData()).onHitted();
                }else{
                    ((BulletModel) fB.getUserData()).onHitted();
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
                    ((Enemy) fA.getUserData()).onHitted(((BulletModel) fB.getUserData()).damage);
                    ((BulletModel) fB.getUserData()).onHitted();
                }else{
                    ((Enemy) fB.getUserData()).onHitted(((BulletModel) fA.getUserData()).damage);
                    ((BulletModel) fA.getUserData()).onHitted();
                }
                break;
            case GameVariables.BIT_GAME_BULLET | GameVariables.BIT_GAME_BOX:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_BOX){
                    ((Box) fA.getUserData()).onHitted(((BulletModel) fB.getUserData()).damage);
                    ((BulletModel) fB.getUserData()).onHitted();
                }else{
                    ((Box) fB.getUserData()).onHitted(((BulletModel) fA.getUserData()).damage);
                    ((BulletModel) fA.getUserData()).onHitted();
                }
                break;
            case GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_COIN:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_GAME_COIN) {
                    ((Coin) fA.getUserData()).onHitted();
                } else {
                    ((Coin) fB.getUserData()).onHitted();
                }
                break;
            case GameVariables.BIT_MENUPLAYER | GameVariables.BIT_FINISH_AREA:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_GAME_COIN) {
                    ((LevelWorld) ((Player) fA.getUserData()).gameWorld).finishGame(1);
                } else {
                    ((LevelWorld) ((Player) fB.getUserData()).gameWorld).finishGame(1);
                }

                break;
            case GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_ENEMY:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_MENUPLAYER) {
                    ((Player) fA.getUserData()).onHitted(((Enemy) fB.getUserData()).damage);
                } else {
                    ((Player) fB.getUserData()).onHitted(((Enemy) fA.getUserData()).damage);
                }

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
                    ((Player) fA.getUserData()).isFootContact = false;
                    ((Player) fA.getUserData()).isJumping = true;
                }else{
                    ((Player) fB.getUserData()).isFootContact = false;
                    ((Player) fB.getUserData()).isJumping = true;
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
