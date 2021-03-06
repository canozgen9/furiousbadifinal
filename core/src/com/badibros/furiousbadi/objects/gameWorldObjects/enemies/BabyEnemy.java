package com.badibros.furiousbadi.objects.gameWorldObjects.enemies;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.models.enemy.EnemyGunModel;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BABY_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BABY_ENEMY_PLAYER_DETECTION_SENSOR;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_FIRING_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_PLAYER;

public class BabyEnemy extends GameObject {

    public boolean playerDetected = false;
    public float damage = 50f;
    public boolean runningRight = true;
    private int experience;
    private float health;
    private float maxHealth;
    private TextureRegion textureRegion;
    private float width;
    private float height;
    private com.badibros.furiousbadi.objects.gameWorldObjects.player.Player player;
    private Array<TextureRegion> disappearFrames;
    private Animation disappearAnimation;
    private float playerDetectX = GameVariables.scale(300);
    private float playerDetectY = GameVariables.scale(200);
    //Impulses
    private Vector2 rightImpulse;
    private Vector2 leftImpulse;
    private boolean hitted = false;
    private boolean dead = false;
    private float stateTimer;

    private EnemyGunModel gun;

    public BabyEnemy(FuriousBadi game, World world, float x, float y, com.badibros.furiousbadi.objects.gameWorldObjects.player.Player player, float health, float damage, String type, float width, float height) {
        super(game, world, x, y);
        this.player = player;
        this.experience = (int) health;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.width = width;
        this.height = height;
        textureRegion = new TextureRegion(new Texture("spritesheets/enemies/" + type + "/" + type + "_baby.png"));
        createBody();
        setBounds(0, 0, GameVariables.scale(width), GameVariables.scale(height));
        setRegion(textureRegion);

        stateTimer = 0;

        disappearFrames = new Array<TextureRegion>();
        Texture texture = new Texture("spritesheets/player/smoke.png");
        for(int i=0;i<10;i++){
            disappearFrames.add(new TextureRegion(texture,i*128,0,128,128));
        }

        disappearAnimation = new Animation(0.1f,disappearFrames);

        rightImpulse = new Vector2(.01f, 0);
        leftImpulse = new Vector2(-.01f, 0);

    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY()));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(GameVariables.scale(width/2));
        fixtureDef.shape = shape;
        fixtureDef.density = .5f;
        fixtureDef.friction = .5f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = BIT_GAME_BABY_ENEMY;
        fixtureDef.filter.maskBits = BIT_PLAYER | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_BOX | BIT_GAME_PLAYER_BOTTOM_SENSOR | BIT_GAME_FIRING_ENEMY | BIT_GAME_BABY_ENEMY;
        getB2d().createFixture(fixtureDef).setUserData(this);

        PolygonShape detectionShape = new PolygonShape();
        detectionShape.setAsBox(playerDetectX*2,playerDetectY);

        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = detectionShape;
        fixtureDef1.isSensor = true;
        fixtureDef1.filter.categoryBits = BIT_GAME_BABY_ENEMY_PLAYER_DETECTION_SENSOR;
        fixtureDef1.filter.maskBits = BIT_PLAYER;

        getB2d().createFixture(fixtureDef1).setUserData(this);

        shape.dispose();
    }

    @Override
    public void getInputs(float delta) {
    }

    @Override
    public void update(float delta) {

        if(!hitted)
        setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);

        checkDestroyFlag();

        if(playerDetected){

            Vector2 center = getB2d().getWorldCenter();
            if (getB2d().getPosition().x > player.getB2d().getPosition().x) {
                if (getB2d().getLinearVelocity().x >= -0.8)
                getB2d().applyLinearImpulse(leftImpulse,center,false);
            } else if (getB2d().getPosition().x < player.getB2d().getPosition().x) {
                if (getB2d().getLinearVelocity().x <= 0.8)
                    getB2d().applyLinearImpulse(rightImpulse,center,false);
            }

        }

        if ((getB2d().getLinearVelocity().x < 0 || !runningRight) && !textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = false;
        } else if ((getB2d().getLinearVelocity().x > 0 || runningRight) && textureRegion.isFlipX()) {
            textureRegion.flip(true, false);
            runningRight = true;
        }

        setRegion(textureRegion);
        if (gun != null) {
            gun.update(delta);
        }
    }


    @Override
    public void render(float delta) {
        if(hitted){
            stateTimer+=delta;
            TextureRegion textureRegion = (TextureRegion) disappearAnimation.getKeyFrame(stateTimer,false);
            setRegion(textureRegion);
            if(stateTimer>1f){
                dead = true;
            }
        }
        if (!dead) {
            draw(getGame().getBatch());
            if (gun != null) {
                gun.render(delta);
            }
        }
    }

    @Override
    public void afterDestroyedBody() {
    }

    public void onHitted(float damage){
        if(!hitted){
            health-=damage;
            if(health<=0){
                setSize(GameVariables.scale(width*3/2),GameVariables.scale(height*3/2));
                setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);
                destroyBody();
                hitted = true;
            }
        }
    }

    public EnemyGunModel getGun() {
        return gun;
    }

    public void setGun(EnemyGunModel gun) {
        this.gun = gun;
    }
}
