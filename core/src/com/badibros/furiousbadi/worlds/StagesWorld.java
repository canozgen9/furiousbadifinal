package com.badibros.furiousbadi.worlds;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameWorld;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.Clickable;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuButton;
import com.badibros.furiousbadi.screens.MainScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;


public class StagesWorld extends GameWorld {

    //Objects
    private Player player;
    private Texture background;
    private Texture header;

    private ArrayList<MenuButton> buttons;


    public StagesWorld(FuriousBadi game, Viewport viewport, OrthographicCamera gameCamera) {
        super(game,viewport,gameCamera);
        ((MainScreen) game.getScreen()).gameCamera.zoom = 1;
    }

    @Override
    public void initializeComponents() {
        super.initializeComponents();

        buttons = new ArrayList<MenuButton>();

        //Contact Listener
        world.setContactListener(new com.badibros.furiousbadi.worlds.contactlisteners.MainMenuWorldContactListener());

        createBox(0,0, GameVariables.scale(50),GameVariables.scale(600));
        createBox(0,0,GameVariables.scale(2000),GameVariables.scale(50));
        createBox(GameVariables.scale(2000),0,GameVariables.scale(50),GameVariables.scale(600));
        createBox(0,GameVariables.scale(600),GameVariables.scale(2000),GameVariables.scale(50));

        //Objects
        player = new Player(game, world, 80, 400);

        MenuButton level1Button = new MenuButton(game, world, 500, 250, "img/mainmenu/buttons/level1.png", "img/mainmenu/buttons/level1_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainScreen) game.getScreen()).currentWorld = new LevelWorld(game, ((MainScreen) game.getScreen()).viewport, ((MainScreen) game.getScreen()).gameCamera, 1);
            }
        });
        MenuButton level2Button = new MenuButton(game, world, 1000, 250, "img/mainmenu/buttons/level2.png", "img/mainmenu/buttons/level2_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainScreen) game.getScreen()).currentWorld = new LevelWorld(game, ((MainScreen) game.getScreen()).viewport, ((MainScreen) game.getScreen()).gameCamera, 2);
            }
        });
        MenuButton backButton = new MenuButton(game, world, 1500, 250, "img/mainmenu/buttons/back_button.png", "img/mainmenu/buttons/back_button_selected.png", new Clickable() {
            @Override
            public void onClick() {
                ((MainScreen) game.getScreen()).currentWorld = new MainMenuWorld(game, ((MainScreen) game.getScreen()).viewport, ((MainScreen) game.getScreen()).gameCamera);
            }
        });

        buttons.add(level1Button);
        buttons.add(level2Button);
        buttons.add(backButton);

        background = new Texture("img/mainmenu/menu_background.jpg");
        header = new Texture("img/header.png");
    }

    public void getInputs(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            FuriousBadi.DEBUGGING = !FuriousBadi.DEBUGGING;
        }
        player.getInputs(delta);
        for(MenuButton button:buttons){
            button.getInputs(delta);
        }
    }

    public void update(float delta) {
        //Update world
        world.step(1 / 60f, 6, 2);
        //Update objects
        player.update(delta);
        for(MenuButton button:buttons){
            button.update(delta);
        }
        //Update camera
        updateGameCamera(delta);
    }

    public void updateGameCamera(float delta){
        gameCamera.position.x = Math.max(gameCamera.position.x + (player.getB2d().getPosition().x - gameCamera.position.x) * .05f, gameCamera.viewportWidth / 2 + GameVariables.scale(40));
        gameCamera.position.y = Math.max(gameCamera.position.y + (player.getB2d().getPosition().y - gameCamera.position.y) * .05f, gameCamera.viewportHeight / 2 + GameVariables.scale(40));
        gameCamera.update();
    }

    public void render(float delta) {
        //Render background
        game.getBatch().begin();
        game.getBatch().draw(background,GameVariables.scale(40),GameVariables.scale(40),GameVariables.scale(1920),GameVariables.scale(520));
        player.draw(game.getBatch());
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
        player.destroyBody();
        for(MenuButton button:buttons){
            button.destroyBody();
        }
        world.dispose();
    }

}
