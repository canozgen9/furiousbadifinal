package com.badibros.furiousbadi.worlds.contactlisteners;

import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuButton;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;


public class MainMenuWorldContactListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.debug("CONTACT DETECTED",contact.getFixtureA().getUserData()+" - "+contact.getFixtureB().getUserData());
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int bit = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;


        switch (bit) {
            case GameVariables.BIT_MENUBUTTON | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
            case GameVariables.BIT_MENUWALLS | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR){
                    ((Player) fA.getUserData()).bottomSensorColliding++;
                }else{
                    ((Player) fB.getUserData()).bottomSensorColliding++;
                }
                break;
            case GameVariables.BIT_PLAYER | GameVariables.BIT_MENUBUTTON:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_MENUBUTTON){
                    ((MenuButton) fA.getUserData()).hitted();
                }else{
                    ((MenuButton) fB.getUserData()).hitted();
                }
                break;
        }

    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.debug("CONTACT DETECTED",contact.getFixtureA().getUserData()+" - "+contact.getFixtureB().getUserData());
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        int bit = fA.getFilterData().categoryBits | fB.getFilterData().categoryBits;


        switch (bit) {
            case GameVariables.BIT_MENUBUTTON | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
            case GameVariables.BIT_MENUWALLS | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR:
                if(fA.getFilterData().categoryBits==GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR){
                    ((Player) fA.getUserData()).bottomSensorColliding++;
                }else{
                    ((Player) fB.getUserData()).bottomSensorColliding--;
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
