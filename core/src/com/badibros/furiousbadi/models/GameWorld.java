package com.badibros.furiousbadi.models;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;

public abstract class GameWorld {
    //DEBUGGING

    protected OrthographicCamera gameCamera;
    protected Viewport viewport;
    protected World world;
    protected FuriousBadi game;
    protected Box2DDebugRenderer debugRenderer;

    public GameWorld(FuriousBadi game, Viewport viewport, OrthographicCamera gameCamera) {
        this.game = game;
        this.viewport = viewport;
        this.gameCamera = gameCamera;
        this.world = new World(new Vector2(0,-9.81f),false);
        initializeComponents();
    }

    public Body createBox(float x, float y, float width, float height) {
        Body pBody;
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.StaticBody;
        def.position.set(x, y);
        def.fixedRotation = true;
        pBody = world.createBody(def);

        FixtureDef fixtureDef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameVariables.BIT_MENUWALLS;
        fixtureDef.filter.maskBits = GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;

        pBody.createFixture(fixtureDef);

        shape.dispose();

        return pBody;
    }

    public void initializeComponents(){
        //Debug renderer
        debugRenderer = new Box2DDebugRenderer();
    }
    public abstract void getInputs(float delta);
    public abstract void update(float delta);
    public abstract void updateGameCamera(float delta);
    public abstract void render(float delta);
    public abstract void dispose();
}
