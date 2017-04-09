package com.badibros.furiousbadi.objects.gameWorldObjects;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_FIRING_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;

public class JointTest extends GameObject {

    public float health = 1000f;
    public boolean runningRight = true;
    com.badibros.furiousbadi.objects.gameWorldObjects.player.Player player;
    TextureRegion textureA = new TextureRegion(new Texture("spritesheets/enviroment/sphereHolder.png"));
    TextureRegion textureB = new TextureRegion(new Texture("spritesheets/enviroment/sphere.png"));
    Sprite spriteB = new Sprite();
    //Impulses
    Vector2 rightImpulse = new Vector2(0.1f, 0);
    Vector2 leftImpulse = new Vector2(-0.1f, 0);
    Vector2 upImpulse = new Vector2(0, 5f);
    Vector2 downImpulse = new Vector2(0, -0.1f);
    private boolean destroy = false;
    private boolean destroyed = false;
    private Body secondBody;
    public JointTest(FuriousBadi game, World world, float x, float y) {
        super(game, world, x, y);
        createBody();
        setOrigin(getWidth()/2,getHeight()/2);

        //Sprites
        setRegion(textureA);
        spriteB.setRegion(textureB);

        setSize(GameVariables.scale(160),GameVariables.scale(160));
        spriteB.setSize(GameVariables.scale(80),GameVariables.scale(80));
        spriteB.setOrigin(spriteB.getWidth()/2,spriteB.getHeight()/2);

        setPosition(getB2d().getPosition().x-getWidth()/2,getB2d().getPosition().y-getHeight()/2);
    }

    @Override
    public void createBody() {
        //RECTANGLE
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY()));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameVariables.scale(80),GameVariables.scale(80));
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.3f;
        fixtureDef.filter.categoryBits = GameVariables.BIT_GAME_GROUND;
        fixtureDef.filter.maskBits = GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_BULLET | BIT_GAME_FIRING_ENEMY | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;

        getB2d().createFixture(fixtureDef).setUserData(this);

        shape.dispose();

        //CIRCLE
        BodyDef circleBodyDef = new BodyDef();

        circleBodyDef.type = BodyDef.BodyType.DynamicBody;
        circleBodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY() - 50));

        FixtureDef circleFixtureDef = new FixtureDef();

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(GameVariables.scale(40));

        circleFixtureDef.shape = circleShape;
        circleFixtureDef.density = 5f;
        circleFixtureDef.friction = 1f;
        circleFixtureDef.restitution = 0.3f;
        circleFixtureDef.filter.categoryBits = GameVariables.BIT_GAME_GROUND;
        circleFixtureDef.filter.maskBits = BIT_GAME_GROUND | GameVariables.BIT_PLAYER | GameVariables.BIT_GAME_BULLET | BIT_GAME_FIRING_ENEMY | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;


        secondBody = getWorld().createBody(circleBodyDef);

        secondBody.createFixture(circleFixtureDef);

        //JOINT
        RevoluteJointDef rdef = new RevoluteJointDef();
        rdef.bodyA = getB2d();
        rdef.bodyB = secondBody;
        rdef.collideConnected = true;
        rdef.localAnchorA.set(0,GameVariables.scale(-120));
        rdef.localAnchorB.set(GameVariables.scale(-300), 0);
        getWorld().createJoint(rdef);


    }

    @Override
    public void getInputs(float delta) {
    }

    @Override
    public void update(float delta) {

        if(destroy && !destroyed){
            getWorld().destroyBody(getB2d());
            destroyed = true;
        }

        spriteB.setPosition(secondBody.getPosition().x-spriteB.getWidth()/2,secondBody.getPosition().y-spriteB.getHeight()/2);
        spriteB.setRotation(MathUtils.radiansToDegrees*secondBody.getAngle());

    }


    @Override
    public void render(float delta) {
        if(destroyed){

        } else {
            draw(getGame().getBatch());
            spriteB.draw(getGame().getBatch());
        }
    }

    @Override
    public void afterDestroyedBody() {

    }

    public void onHitted(float damage){
        health-=damage;
        if(health<=0){
            destroy = true;
        }
    }

}
