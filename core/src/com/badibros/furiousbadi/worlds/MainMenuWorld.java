package com.badibros.furiousbadi.worlds;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameWorld;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.Clickable;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuButton;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.screens.MainScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

public class MainMenuWorld extends GameWorld {

    int i = 0;
    //Objects
    private MenuPlayer menuPlayer;
    private Texture background;
    private Texture header;
    private ArrayList<MenuButton> buttons;

    public MainMenuWorld(FuriousBadi game, Viewport viewport, OrthographicCamera gameCamera) {
        super(game,viewport,gameCamera);
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        gameCamera.zoom = 1;

        buttons = new ArrayList<MenuButton>();

        //Contact Listener
        world.setContactListener(new MainMenuWorldContactListener());

        createBox(0,0, GameVariables.scale(50),GameVariables.scale(600));
        createBox(0,0,GameVariables.scale(2500),GameVariables.scale(50));
        createBox(GameVariables.scale(2500),0,GameVariables.scale(50),GameVariables.scale(600));
        createBox(0,GameVariables.scale(600),GameVariables.scale(2500),GameVariables.scale(50));

        //Objects
        menuPlayer = new MenuPlayer(game,world,80,400);

        MenuButton playButton = new MenuButton(game, world, 500, 250, "img/mainmenu/buttons/play_button.png", "img/mainmenu/buttons/play_button_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainScreen) game.getScreen()).currentWorld = new StagesWorld(game, ((MainScreen) game.getScreen()).viewport, ((MainScreen) game.getScreen()).gameCamera);
            }
        });
        MenuButton optionsButton = new MenuButton(game, world, 1000, 250, "img/mainmenu/buttons/options_button.png", "img/mainmenu/buttons/options_button_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainScreen) game.getScreen()).currentWorld = new StagesWorld(game, ((MainScreen) game.getScreen()).viewport, ((MainScreen) game.getScreen()).gameCamera);
            }
        });
        MenuButton creditsButton = new MenuButton(game, world, 1500, 250, "img/mainmenu/buttons/credits_button.png", "img/mainmenu/buttons/credits_button_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainScreen) game.getScreen()).currentWorld = new CreditsWorld(game, ((MainScreen) game.getScreen()).viewport, ((MainScreen) game.getScreen()).gameCamera);
            }
        });
        MenuButton exitButton = new MenuButton(game, world, 2000, 250, "img/mainmenu/buttons/exit_button.png", "img/mainmenu/buttons/exit_button_selected.png", new Clickable() {
            @Override
            public void onClick() {
                Gdx.app.exit();
            }
        });

        buttons.add(playButton);
        buttons.add(optionsButton);
        buttons.add(creditsButton);
        buttons.add(exitButton);

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
        game.getBatch().draw(background,GameVariables.scale(40),GameVariables.scale(40),GameVariables.scale(2420),GameVariables.scale(520));
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
        for(MenuButton button:buttons){
            button.destroyBody();
        }
        menuPlayer.destroyBody();
        world.dispose();
    }


}
