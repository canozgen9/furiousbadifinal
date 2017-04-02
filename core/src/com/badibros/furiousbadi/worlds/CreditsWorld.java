package com.badibros.furiousbadi.worlds;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameWorld;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.Clickable;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuButton;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.screens.MainMenuScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by canozgen9 on 3/13/17.
 */

public class CreditsWorld extends GameWorld {

    //Objects
    public MenuPlayer menuPlayer;
    public Texture background;
    public Texture header;

    private ArrayList<MenuButton> buttons;

    Texture canozgen;
    Texture furkannakip;

    public CreditsWorld(FuriousBadi game, Viewport viewport, OrthographicCamera gameCamera) {
        super(game,viewport,gameCamera);
        ((MainMenuScreen) game.getScreen()).gameCamera.zoom = 1;
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        //textures
        canozgen = new Texture("img/mainmenu/canozgen.png");
        furkannakip = new Texture("img/mainmenu/furkannakip.png");

        buttons = new ArrayList<MenuButton>();

        //Contact Listener
        world.setContactListener(new MainMenuWorldContactListener());

        createBox(0,0, GameVariables.scale(50),GameVariables.scale(600));
        createBox(0,0,GameVariables.scale(2000),GameVariables.scale(50));
        createBox(GameVariables.scale(2000),0,GameVariables.scale(50),GameVariables.scale(600));
        createBox(0,GameVariables.scale(600),GameVariables.scale(2000),GameVariables.scale(50));

        //Objects
        menuPlayer = new MenuPlayer(game,world,80,200);

        MenuButton back_button = new MenuButton(game, world, 500, 250, "img/mainmenu/buttons/back_button.png", "img/mainmenu/buttons/back_button_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainMenuScreen)game.getScreen()).currentWorld = new MainMenuWorld(game,((MainMenuScreen) game.getScreen()).viewport,((MainMenuScreen)game.getScreen()).gameCamera);
            }
        });

        buttons.add(back_button);

        background = new Texture("img/mainmenu/menu_background.jpg");
        header = new Texture("img/header.png");
    }

    public void getInputs(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            FuriousBadi.DEBUGGING = !FuriousBadi.DEBUGGING;
        }
        menuPlayer.getInputs(delta);
        for(MenuButton button:buttons){
            button.getInputs(delta);
        }
    }

    public void update(float delta) {
        //Update world
        world.step(1 / 60f, 6, 2);
        //Update objects
        menuPlayer.update(delta);
        for(MenuButton button:buttons){
            button.update(delta);
        }
        //Update camera
        updateGameCamera(delta);
    }

    public void updateGameCamera(float delta){
        gameCamera.position.x=Math.max(gameCamera.position.x+(menuPlayer.getB2d().getPosition().x-gameCamera.position.x)*.05f,gameCamera.viewportWidth/2+GameVariables.scale(40));
        gameCamera.position.y=Math.max(gameCamera.position.y+(menuPlayer.getB2d().getPosition().y-gameCamera.position.y)*.05f,gameCamera.viewportHeight/2+GameVariables.scale(40));
        gameCamera.update();
    }

    public void render(float delta) {
        //Render background
        game.getBatch().begin();
        game.getBatch().draw(background,GameVariables.scale(40),GameVariables.scale(40),GameVariables.scale(1920),GameVariables.scale(520));
        game.getBatch().draw(canozgen,GameVariables.scale(300),GameVariables.scale(300),GameVariables.scale(190),GameVariables.scale(160));
        game.getBatch().draw(furkannakip,GameVariables.scale(500),GameVariables.scale(300),GameVariables.scale(190),GameVariables.scale(160));
        menuPlayer.draw(game.getBatch());
        game.getBatch().end();
        //Render game objects
        for(MenuButton button:buttons){
            button.render(delta);
        }
        //Debug renderer
        if(FuriousBadi.DEBUGGING) debugRenderer.render(world, gameCamera.combined);
    }

    public void dispose() {
        debugRenderer.dispose();
        menuPlayer.destroyBody();
        for(MenuButton button:buttons){
            button.destroyBody();
        }
        world.dispose();
    }

}