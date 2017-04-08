package com.badibros.furiousbadi.models.player;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_MENUBUTTON;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_MENUWALLS;

/**
 * Created by canozgen9 on 4/3/17.
 */

public class EnemyBulletModel extends GameObject {

    public float damage = 80f;
    public float speed = 0.3f;
    public float width = 0, height = 0, textureWith = 0, textureHeight = 0;
    private Array<TextureRegion> explodeFrames;
    private Animation explodeAnimation;
    private TextureRegion bulletInitialFrame;
    private float stateTimer = 0;
    private GunModel gun;
    private boolean hitted = false;
    private boolean dead = false;

    public EnemyBulletModel(FuriousBadi game, World world, GunModel gun, float width, float height, float textureWidth, float textureHeight, String texturePath, float speed) {
        super(game, world, gun.player.getB2d().getPosition().x, gun.player.getB2d().getPosition().y);
        this.speed = speed;
        this.width = width;
        this.height = height;
        this.gun = gun;
        this.textureWith = textureWidth;
        this.textureHeight = textureHeight;
        createBody();
        bulletInitialFrame = new TextureRegion(new Texture(texturePath));
        stateTimer = 0;
        setSize(GameVariables.scale(textureWidth), GameVariables.scale(textureHeight));
        prepareObject();
    }

    public void prepareObject() {
        if (!gun.isFacingRight) bulletInitialFrame.flip(true, false);
        setRegion(bulletInitialFrame);
        setOrigin(getWidth()/2,getHeight()/2);
        if (gun.isFacingRight) {
            setRotation(gun.angle);
        } else {
            setRotation(-gun.angle);
        }

        getB2d().applyLinearImpulse((gun.isFacingRight ? speed : -speed), gun.angle / 100, getB2d().getWorldCenter().x, getB2d().getWorldCenter().y, false);

        explodeFrames = new Array<TextureRegion>();
        Texture texture = new Texture("spritesheets/player/smoke.png");
        for(int i=0;i<10;i++){
            explodeFrames.add(new TextureRegion(texture,i*128,0,128,128));
        }
        explodeAnimation= new Animation(0.05f,explodeFrames);
    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        if (gun.player.runningRight) {
            if (gun.player.isCrouching) {
                bodyDef.position.set(gun.player.getB2d().getPosition().x + GameVariables.scale(20), gun.player.getB2d().getPosition().y + GameVariables.scale(10 + gun.player.angle / 2));
            } else {
                bodyDef.position.set(gun.player.getB2d().getPosition().x + GameVariables.scale(20), gun.player.getB2d().getPosition().y + GameVariables.scale(20 + gun.player.angle / 2));
            }
        }else{
            if (gun.player.isCrouching) {
                bodyDef.position.set(gun.player.getB2d().getPosition().x - GameVariables.scale(20), gun.player.getB2d().getPosition().y + GameVariables.scale(10 + gun.player.angle / 2));
            } else {
                bodyDef.position.set(gun.player.getB2d().getPosition().x - GameVariables.scale(20), gun.player.getB2d().getPosition().y + GameVariables.scale(20 + gun.player.angle / 2));
            }
        }
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0.00f;
        bodyDef.fixedRotation = false;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameVariables.scale(width), GameVariables.scale(height));
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 10f;
        fixtureDef.restitution = 0;
        fixtureDef.filter.categoryBits = BIT_GAME_BULLET;
        fixtureDef.filter.maskBits = BIT_MENUBUTTON | BIT_MENUWALLS | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_BOX;
        getB2d().createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void getInputs(float delta) {

    }

    @Override
    public void update(float delta) {
        checkDestroyFlag();
        if(!hitted)
            setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);

    }

    @Override
    public void render(float delta) {
        if (hitted) {
            stateTimer += delta;
            TextureRegion textureRegion = (TextureRegion) explodeAnimation.getKeyFrame(stateTimer, false);
            setRegion(textureRegion);
            if (stateTimer > .5f) {
                dead = true;
            }
        }
        if (!dead)
            draw(getGame().getBatch());
    }

    @Override
    public void afterDestroyedBody() {

    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public void onHitted(){
        setSize(GameVariables.scale(50),GameVariables.scale(50));
        setPosition(getB2d().getPosition().x- getWidth() / 2,getB2d().getPosition().y- getHeight()/2);
        destroyBody();
        hitted = true;
    }
}
