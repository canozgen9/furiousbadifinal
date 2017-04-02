package com.badibros.furiousbadi.objects.gameWorldObjects;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
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

public class Bullet extends GameObject {

    public float damage = 80f;
    private Array<TextureRegion> explodeFrames;
    private Animation explodeAnimation;
    private TextureRegion bulletInitialFrame;
    private float stateTimer = 0;

    private MenuPlayer player;

    private boolean destroy = false;
    private boolean destroyed = false;
    private boolean hitted = false;
    private boolean dead = false;

    public Bullet(FuriousBadi game, World world, float x, float y, MenuPlayer player) {
        super(game, world, x, y);
        this.player = player;
        createBody();

        stateTimer = 0;

        setBounds(0, 0, GameVariables.scale(50), GameVariables.scale(10));
        bulletInitialFrame = new TextureRegion(new Texture("spritesheets/player/arrow.png"));
        if(!player.runningRight) bulletInitialFrame.flip(true,false);
        setRegion(bulletInitialFrame);
        setOrigin(getWidth()/2,getHeight()/2);
        if(player.runningRight){
            setRotation(player.angle);
        } else {
            setRotation(-player.angle);
        }

        getB2d().applyLinearImpulse((player.runningRight ? 0.3f : -0.3f), player.angle / 100, getB2d().getWorldCenter().x, getB2d().getWorldCenter().y, false);
        System.out.println((float)(Math.sin(player.angle)));

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
        if(player.runningRight){
            if(player.isCrouching){
                bodyDef.position.set(player.getB2d().getPosition().x+GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(10+player.angle/2));
            } else {
                bodyDef.position.set(player.getB2d().getPosition().x+GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(20+player.angle/2));
            }
        }else{
            if(player.isCrouching){
                bodyDef.position.set(player.getB2d().getPosition().x-GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(10+player.angle/2));
            } else {
                bodyDef.position.set(player.getB2d().getPosition().x-GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(20+player.angle/2));
            }
        }
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = 0.05f;
        bodyDef.fixedRotation = false;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameVariables.scale(10),GameVariables.scale(10));
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

        if(!destroyed && destroy){
            getWorld().destroyBody(getB2d());
            destroyed = true;
        }

        if(!hitted)
            setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);

    }


    @Override
    public void render(float delta) {
        stateTimer+=delta;
            if(hitted){
                TextureRegion textureRegion = (TextureRegion) explodeAnimation.getKeyFrame(stateTimer,false);
                setRegion(textureRegion);
                if(stateTimer>.5f){
                    destroy = true;
                    dead = true;
                }
            }
            if(!dead)
            draw(getGame().getBatch());
    }

    public void onHitted(){
        setSize(GameVariables.scale(50),GameVariables.scale(50));
        setPosition(getB2d().getPosition().x- getWidth() / 2,getB2d().getPosition().y- getHeight()/2);
        destroy = true;
        hitted = true;
    }

}
