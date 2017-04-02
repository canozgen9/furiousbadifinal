package com.badibros.furiousbadi.worlds;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.models.GameWorld;
import com.badibros.furiousbadi.objects.gameWorldObjects.Box;
import com.badibros.furiousbadi.objects.gameWorldObjects.Enemy;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.screens.Hud;
import com.badibros.furiousbadi.screens.MainMenuScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_COIN;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.PPM;

/**
 * Created by canozgen9 on 3/31/17.
 */

public class Level1World extends GameWorld{

    public ArrayList<GameObject> gameObjects;
    public Hud hud;
    private MenuPlayer player;
    //Tiled map
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    public Level1World(FuriousBadi game, Viewport viewport, OrthographicCamera gameCamera) {
        super(game, viewport, gameCamera);


        world.setContactListener(new Level1WorldContactListener());

        ((MainMenuScreen) game.getScreen()).gameCamera.zoom = 1;
        //tiled map
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("levels/level1/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/ PPM);

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //Ground
        for(MapObject mapObject : tiledMap.getLayers().get(1).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/PPM,(rectangle.getY()+rectangle.getHeight()/2)/PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth()/2/PPM,rectangle.getHeight()/2/PPM);

            fixtureDef.shape = shape;

            fixtureDef.filter.categoryBits = GameVariables.BIT_GAME_GROUND;
            fixtureDef.filter.maskBits = BIT_GAME_COIN | BIT_GAME_GROUND | GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
            body.createFixture(fixtureDef);

        }

        gameObjects = new ArrayList<GameObject>();
        Rectangle rectangle2 = ((RectangleMapObject) tiledMap.getLayers().get(11).getObjects().get(0)).getRectangle();
        player = new MenuPlayer(game,world,(rectangle2.getX()+rectangle2.getWidth()/2),(rectangle2.getY()+rectangle2.getHeight()/2));
        player.gameWorld = this;


        for(MapObject mapObject : tiledMap.getLayers().get(2).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Enemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 200, 50, 1, 70, 70));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(3).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Enemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 300, 50, 2, 80, 80));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(4).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Enemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 400, 50, 3, 90, 90));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(5).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Enemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 500, 50, 4, 100, 100));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(6).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Enemy(game,world,(rectangle.getX()+rectangle.getWidth()/2),(rectangle.getY()+rectangle.getHeight()/2),player,1000,50,5,200,200));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(7).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Box(game,world,(rectangle.getX()+rectangle.getWidth()/2),(rectangle.getY()+rectangle.getHeight()/2),"red"));
        }
        for(MapObject mapObject : tiledMap.getLayers().get(8).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Box(game,world,(rectangle.getX()+rectangle.getWidth()/2),(rectangle.getY()+rectangle.getHeight()/2),"green"));
        }
        for(MapObject mapObject : tiledMap.getLayers().get(9).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Box(game,world,(rectangle.getX()+rectangle.getWidth()/2),(rectangle.getY()+rectangle.getHeight()/2),"yellow"));
        }
        for(MapObject mapObject : tiledMap.getLayers().get(10).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new Box(game,world,(rectangle.getX()+rectangle.getWidth()/2),(rectangle.getY()+rectangle.getHeight()/2),"blue"));
        }


        hud = new Hud(game.getBatch());

    }

    @Override
    public void getInputs(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            FuriousBadi.DEBUGGING = !FuriousBadi.DEBUGGING;
        }
        player.getInputs(delta);
    }

    @Override
    public void update(float delta) {
        world.step(1 / 60f, 6, 2);
        mapRenderer.setView(gameCamera);
        player.update(delta);
        updateGameCamera(delta);
        hud.update(delta);
        for(GameObject gameObject : gameObjects) gameObject.update(delta);

    }

    public void addObject(GameObject o) {
        ArrayList<GameObject> temp = new ArrayList<GameObject>();

        temp.add(o);
        temp.addAll(gameObjects);

        gameObjects = temp;

    }

    @Override
    public void updateGameCamera(float delta) {
        gameCamera.position.x=Math.max(gameCamera.position.x+(player.getB2d().getPosition().x-gameCamera.position.x)*.05f,gameCamera.viewportWidth/2+GameVariables.scale(40));
        gameCamera.position.y=Math.max(gameCamera.position.y+(player.getB2d().getPosition().y-gameCamera.position.y)*.05f,gameCamera.viewportHeight/2+GameVariables.scale(40));
        gameCamera.update();
    }

    @Override
    public void render(float delta) {

        game.getBatch().setProjectionMatrix(gameCamera.combined);

        mapRenderer.render();
        game.getBatch().begin();
        player.render(delta);


        for(GameObject gameObject : gameObjects) gameObject.render(delta);

        game.getBatch().end();

        hud.render(delta);

        if(FuriousBadi.DEBUGGING) debugRenderer.render(world, gameCamera.combined);
    }

    @Override
    public void dispose() {

    }
}