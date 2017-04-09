package com.badibros.furiousbadi.worlds;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.models.GameWorld;
import com.badibros.furiousbadi.objects.gameWorldObjects.JointTest;
import com.badibros.furiousbadi.objects.gameWorldObjects.destroyables.Box;
import com.badibros.furiousbadi.objects.gameWorldObjects.enemies.FiringEnemy;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.screens.Hud;
import com.badibros.furiousbadi.screens.MainScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
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

import org.json.JSONObject;

import java.util.ArrayList;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_COIN;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.PPM;

/**
 * Created by canozgen9 on 3/31/17.
 */

public class LevelWorld extends GameWorld {

    public boolean running;
    public ArrayList<GameObject> gameObjects;
    public ArrayList<GameObject> gameObjectsToAdd;
    public Hud hud;
    //Level
    public int level;
    private Player player;
    //Tiled map
    private TmxMapLoader mapLoader;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer mapRenderer;

    public LevelWorld(FuriousBadi game, Viewport viewport, OrthographicCamera gameCamera, int level) {
        super(game, viewport, gameCamera);

        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/music.wav"));
        long id = sound.play(0.5f);
        sound.setLooping(id, true);
        this.level = level;


        world.setContactListener(new com.badibros.furiousbadi.worlds.contactlisteners.LevelWorldContactListener());

        ((MainScreen) game.getScreen()).gameCamera.zoom = 1;
        //tiled map
        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("levels/level" + level + "/map.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/ PPM);

        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;

        //Finish Point
        Rectangle finishRectangle = ((RectangleMapObject) tiledMap.getLayers().get(13).getObjects().get(0)).getRectangle();
        BodyDef finishRectangleBodyDef = new BodyDef();
        finishRectangleBodyDef.type = BodyDef.BodyType.StaticBody;
        finishRectangleBodyDef.position.set((finishRectangle.getX() + finishRectangle.getWidth() / 2) / PPM, (finishRectangle.getY() + finishRectangle.getHeight() / 2) / PPM);
        FixtureDef finishRectangleFixtureDef = new FixtureDef();
        PolygonShape finishRectangleShape = new PolygonShape();
        finishRectangleShape.setAsBox(finishRectangle.getWidth() / 2 / PPM, finishRectangle.getHeight() / 2 / PPM);
        finishRectangleFixtureDef.shape = finishRectangleShape;
        finishRectangleFixtureDef.filter.categoryBits = GameVariables.BIT_FINISH_AREA;
        finishRectangleFixtureDef.filter.maskBits = GameVariables.BIT_PLAYER;
        finishRectangleFixtureDef.isSensor = true;
        world.createBody(finishRectangleBodyDef).createFixture(finishRectangleFixtureDef);

        //Ground
        for(MapObject mapObject : tiledMap.getLayers().get(1).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX()+rectangle.getWidth()/2)/PPM,(rectangle.getY()+rectangle.getHeight()/2)/PPM);

            body = world.createBody(bodyDef);

            shape.setAsBox(rectangle.getWidth()/2/PPM,rectangle.getHeight()/2/PPM);

            fixtureDef.shape = shape;

            fixtureDef.filter.categoryBits = GameVariables.BIT_GAME_GROUND;
            fixtureDef.filter.maskBits = BIT_GAME_ENEMY_BULLET | BIT_GAME_COIN | BIT_GAME_GROUND | GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
            body.createFixture(fixtureDef);

        }

        gameObjects = new ArrayList<GameObject>();
        gameObjectsToAdd = new ArrayList<GameObject>();

        Rectangle rectangle2 = ((RectangleMapObject) tiledMap.getLayers().get(11).getObjects().get(0)).getRectangle();
        player = new Player(game, world, (rectangle2.getX() + rectangle2.getWidth() / 2), (rectangle2.getY() + rectangle2.getHeight() / 2));
        player.gameWorld = this;


        for (MapObject mapObject : tiledMap.getLayers().get(12).getObjects()) {
            rectangle2 = ((RectangleMapObject) mapObject).getRectangle();
            gameObjects.add(new JointTest(game, world, (rectangle2.getX() + rectangle2.getWidth() / 2), (rectangle2.getY() + rectangle2.getHeight() / 2)));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(2).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new FiringEnemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 200, 50, "green"));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(3).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new FiringEnemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 300, 50, "red"));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(4).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new FiringEnemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 400, 50, "blue"));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(5).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new FiringEnemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 500, 50, "green"));
        }

        for(MapObject mapObject : tiledMap.getLayers().get(6).getObjects()){
            Rectangle rectangle = ((RectangleMapObject)mapObject).getRectangle();
            gameObjects.add(new FiringEnemy(game, world, (rectangle.getX() + rectangle.getWidth() / 2), (rectangle.getY() + rectangle.getHeight() / 2), player, 1000, 50, "red"));
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


        hud = new Hud(game.getBatch(), player);
        ((MainScreen) game.getScreen()).currentWorld = this;

        gameObjects.add(new FiringEnemy(game, world, player.getInitialX() + 500, player.getInitialY() + 300, player, 400, 50, "red"));
        running = true;


        player = updatePlayerData(player);


    }

    public boolean savePlayerData(Player player) {
        JSONObject JSONobject = new JSONObject();
        JSONobject.put("player-level", player.level);
        JSONobject.put("player-experience", player.experience);
        JSONobject.put("player-coin", player.coin);
        saveData(JSONobject.toString());
        return true;
    }

    public Player updatePlayerData(Player player) {
        player.level = getDataArray().getInt("player-level");
        player.experience = getDataArray().getInt("player-experience");
        player.coin = getDataArray().getInt("player-coin");
        return player;
    }

    public JSONObject getDataArray() {
        JSONObject J = new JSONObject(readData());
        return J;
    }

    public void saveData(String s) {
        FileHandle file = Gdx.files.local("data/player.txt");
        file.writeString(s, false);
    }

    public String readData() {
        FileHandle file = Gdx.files.internal("data/player.txt");
        return file.readString();
    }

    @Override
    public void getInputs(float delta) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.F1)){
            FuriousBadi.DEBUGGING = !FuriousBadi.DEBUGGING;
        }
        if (running) {
        player.getInputs(delta);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (hud.paused) {
                hud.paused = false;
                hud.willPause = false;
                running = true;
            } else {
                hud.pause();
            }
        }
    }

    @Override
    public void update(float delta) {
        if (running) {
            for (GameObject g : gameObjectsToAdd) {
                gameObjects.add(g);
            }
            gameObjectsToAdd.clear();

            world.step(1 / 60f, 6, 2);
            mapRenderer.setView(gameCamera);
            player.update(delta);
            updateGameCamera(delta);
            hud.update(delta);
            for (GameObject gameObject : gameObjects) gameObject.update(delta);
        }
    }

    public void addObject(GameObject o) {
        ArrayList<GameObject> temp = new ArrayList<GameObject>();

        temp.add(o);
        temp.addAll(gameObjects);

        gameObjects = temp;

    }

    @Override
    public void updateGameCamera(float delta) {
        if (running) {
            gameCamera.position.x = Math.max(gameCamera.position.x + (player.getB2d().getPosition().x - gameCamera.position.x) * .05f, gameCamera.viewportWidth / 2 + GameVariables.scale(40));
            gameCamera.position.y = Math.max(gameCamera.position.y + (player.getB2d().getPosition().y - gameCamera.position.y) * .05f, gameCamera.viewportHeight / 2 + GameVariables.scale(40));
            gameCamera.update();
        }
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

    public void finishGame(int result) {
        if (result == 1) {
            hud.finishGame(1);
        } else {
            hud.finishGame(0);
        }
    }
}
