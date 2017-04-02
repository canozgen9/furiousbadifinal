package com.badibros.furiousbadi.objects.gameWorldObjects;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_MENUPLAYER;

public class Box extends GameObject {

    public boolean playerDetected = false;
    MenuPlayer player;
    //Impulses
    Vector2 rightImpulse = new Vector2(0.1f, 0);
    Vector2 leftImpulse = new Vector2(-0.1f, 0);
    Vector2 upImpulse = new Vector2(0, 5f);
    Vector2 downImpulse = new Vector2(0, -0.1f);
    private float health = 250f;
    private String color;
    private boolean runningRight = true;
    private TextureRegion textureRegion;
    private boolean destroy = false;
    private boolean destroyed = false;
    public Box(FuriousBadi game, World world, float x, float y, String color) {
        super(game, world, x, y);
        this.color = color;
        createBody();
        textureRegion = new TextureRegion(new Texture("spritesheets/enviroment/"+color+"_block.png"));
        setBounds(0, 0, GameVariables.scale(40), GameVariables.scale(40));
        setRegion(textureRegion);
        setOrigin(getWidth()/2,getHeight()/2);
    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getX()), GameVariables.scale(getY()));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameVariables.scale(20),GameVariables.scale(20));
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.3f;
        fixtureDef.filter.categoryBits = BIT_GAME_BOX;
        fixtureDef.filter.maskBits = BIT_MENUPLAYER | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
        getB2d().createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void getInputs(float delta) {
    }

    @Override
    public void update(float delta) {
        float angle = getB2d().getAngle();
        setRotation(angle * MathUtils.radiansToDegrees);
        setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);

        if(destroy && !destroyed){
            getWorld().destroyBody(getB2d());
            destroyed = true;
        }

    }


    @Override
    public void render(float delta) {
        if(destroyed){

        } else {
            draw(getGame().getBatch());
        }
    }

    public void onHitted(float damage){
        health-=damage;
        if(health<=0){
            destroy = true;
        }
    }

}
