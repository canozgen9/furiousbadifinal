package com.badibros.furiousbadi.objects.gameWorldObjects;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.screens.MainMenuScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.Level1World;
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

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_MENUPLAYER;

public class Enemy extends GameObject {

    public boolean playerDetected = false;
    private int experience;
    private float health;
    private float maxHealth;
    private float damage = 50f;
    private boolean runningRight = true;
    private TextureRegion textureRegion;
    private float width;
    private float height;
    private MenuPlayer player;
    private Array<TextureRegion> disappearFrames;
    private Animation disappearAnimation;
    private float playerDetectX = GameVariables.scale(300);
    private float playerDetectY = GameVariables.scale(200);
    //Impulses
    private Vector2 rightImpulse;
    private Vector2 leftImpulse;
    private Vector2 upImpulse = new Vector2(0, 5f);
    private Vector2 downImpulse = new Vector2(0, -0.1f);
    private boolean destroy = false;
    private boolean destroyed = false;
    private boolean hitted = false;
    private boolean dead = false;
    private float stateTimer;
    public Enemy(FuriousBadi game, World world, float x, float y,MenuPlayer player,float health,float damage, int type, float width, float height) {
        super(game, world, x, y);
        this.player = player;
        this.experience = (int) health;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.width = width;
        this.height = height;
        textureRegion = new TextureRegion(new Texture("spritesheets/enviroment/enemies/enemy"+type+".png"));
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

        rightImpulse = new Vector2(0.2f*width/70, 0);
        leftImpulse = new Vector2(-0.2f*width/70, 0);

    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getX()), GameVariables.scale(getY()));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(GameVariables.scale(width/2));
        fixtureDef.shape = shape;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0.1f;
        fixtureDef.filter.categoryBits = BIT_GAME_ENEMY;
        fixtureDef.filter.maskBits = BIT_MENUPLAYER | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR | BIT_GAME_ENEMY;
        getB2d().createFixture(fixtureDef).setUserData(this);


        PolygonShape detectionShape = new PolygonShape();
        detectionShape.setAsBox(playerDetectX*2,playerDetectY);

        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = detectionShape;
        fixtureDef1.isSensor = true;
        fixtureDef1.filter.categoryBits = BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR;
        fixtureDef1.filter.maskBits = BIT_MENUPLAYER;

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

        if(destroy && !destroyed){
            for (int i = 0; i < (int) maxHealth / 100; i++) {
                ((Level1World) ((MainMenuScreen) getGame().getScreen()).currentWorld).addObject(new Coin(getGame(), getWorld(), getB2d().getPosition().x, getB2d().getPosition().y));
            }
            getWorld().destroyBody(getB2d());
            destroyed = true;

        }

        if(playerDetected){
            Vector2 center = getB2d().getWorldCenter();
            if(getB2d().getPosition().x >player.getB2d().getPosition().x +1f ){
                if(getB2d().getLinearVelocity().x>=-0.2)
                getB2d().applyLinearImpulse(leftImpulse,center,false);
            } else  if(getB2d().getPosition().x < player.getB2d().getPosition().x -1f ){
                if(getB2d().getLinearVelocity().x<=0.2)
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
    }


    @Override
    public void render(float delta) {
        if(hitted){
            stateTimer+=delta;
            TextureRegion textureRegion = (TextureRegion) disappearAnimation.getKeyFrame(stateTimer,false);
            setRegion(textureRegion);
            if(stateTimer>1f){
                destroy = true;
                dead = true;
            }
        }
        if(!dead)
            draw(getGame().getBatch());
    }

    public void onHitted(float damage){
        if(!hitted){
            health-=damage;
            if(health<=0){
                setSize(GameVariables.scale(width*3/2),GameVariables.scale(height*3/2));
                setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);
                destroy = true;
                hitted = true;
                player.experience += experience;
                if (player.experience >= 1000) {
                    player.experience -= 1000;
                    player.level++;
                    ((Level1World) ((MainMenuScreen) getGame().getScreen()).currentWorld).hud.levelUpTimer += 1;
                }
                ((Level1World) ((MainMenuScreen) getGame().getScreen()).currentWorld).hud.killTimer += 1;
                ((Level1World) ((MainMenuScreen) getGame().getScreen()).currentWorld).hud.updateInfo(player);
            }
        }
    }

}