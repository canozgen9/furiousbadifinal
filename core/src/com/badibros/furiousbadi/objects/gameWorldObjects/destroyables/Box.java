package com.badibros.furiousbadi.objects.gameWorldObjects.destroyables;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_PLAYER;

public class Box extends GameObject {

    private float health = 250f;
    private String color;
    private TextureRegion textureRegion;

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
        bodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY()));
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
        fixtureDef.filter.maskBits = BIT_PLAYER | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
        getB2d().createFixture(fixtureDef).setUserData(this);

        shape.dispose();
    }

    @Override
    public void getInputs(float delta) {
    }

    @Override
    public void update(float delta) {

        checkDestroyFlag();

        float angle = getB2d().getAngle();
        setRotation(angle * MathUtils.radiansToDegrees);
        setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);

    }


    @Override
    public void render(float delta) {
        if (isDestroyed()) {

        } else {
            draw(getGame().getBatch());
        }
    }

    @Override
    public void afterDestroyedBody() {

    }

    public void onHitted(float damage){
        health-=damage;
        if(health<=0){
            destroyBody();
        }
    }

}
