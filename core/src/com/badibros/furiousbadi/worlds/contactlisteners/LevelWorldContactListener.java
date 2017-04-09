package com.badibros.furiousbadi.worlds.contactlisteners;

import com.badibros.furiousbadi.models.enemy.EnemyBulletModel;
import com.badibros.furiousbadi.models.player.BulletModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.collectables.Coin;
import com.badibros.furiousbadi.objects.gameWorldObjects.destroyables.Box;
import com.badibros.furiousbadi.objects.gameWorldObjects.enemies.FiringEnemy;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class LevelWorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
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
            case GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_ENEMY_BULLET:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_GAME_ENEMY_BULLET) {
                    ((EnemyBulletModel) fA.getUserData()).onHitted();
                } else {
                    ((EnemyBulletModel) fB.getUserData()).onHitted();
                }
                break;

            case GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR){
                    ((FiringEnemy) fA.getUserData()).playerDetected = true;
                }else{
                    ((FiringEnemy) fB.getUserData()).playerDetected = true;
                }
                break;
            case GameVariables.BIT_GAME_BULLET | GameVariables.BIT_GAME_ENEMY:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_ENEMY){
                    ((FiringEnemy) fA.getUserData()).onHitted(((BulletModel) fB.getUserData()).damage);
                    ((BulletModel) fB.getUserData()).onHitted();
                }else{
                    ((FiringEnemy) fB.getUserData()).onHitted(((BulletModel) fA.getUserData()).damage);
                    ((BulletModel) fA.getUserData()).onHitted();
                }
                break;
            case GameVariables.BIT_GAME_ENEMY_BULLET | GameVariables.BIT_PLAYER:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_PLAYER) {
                    ((Player) fA.getUserData()).onHitted(((EnemyBulletModel) fB.getUserData()).damage);
                    ((EnemyBulletModel) fB.getUserData()).onHitted();
                } else {
                    ((Player) fB.getUserData()).onHitted(((EnemyBulletModel) fA.getUserData()).damage);
                    ((EnemyBulletModel) fA.getUserData()).onHitted();
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
            case GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_COIN:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_GAME_COIN) {
                    ((Coin) fA.getUserData()).onHitted();
                    ((Player) fB.getUserData()).coin++;
                } else {
                    ((Coin) fB.getUserData()).onHitted();
                    ((Player) fA.getUserData()).coin++;
                }
                break;
            case GameVariables.BIT_PLAYER | GameVariables.BIT_FINISH_AREA:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_GAME_COIN) {
                    ((LevelWorld) ((Player) fA.getUserData()).gameWorld).finishGame(1);
                } else {
                    ((LevelWorld) ((Player) fB.getUserData()).gameWorld).finishGame(1);
                }

                break;
            case GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_ENEMY:
                if (fA.getFilterData().categoryBits == GameVariables.BIT_PLAYER) {
                    ((Player) fA.getUserData()).onHitted(((FiringEnemy) fB.getUserData()).damage);
                } else {
                    ((Player) fB.getUserData()).onHitted(((FiringEnemy) fA.getUserData()).damage);
                }

            default:

        }

    }

    @Override
    public void endContact(Contact contact) {
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
            case GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR){
                    ((FiringEnemy) fA.getUserData()).playerDetected = false;
                }else{
                    ((FiringEnemy) fB.getUserData()).playerDetected = false;
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
