package com.badibros.furiousbadi.objects.mainMenuWorldObjects;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.objects.gameWorldObjects.Bullet;
import com.badibros.furiousbadi.screens.MainMenuScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.Level1World;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;

public class MenuPlayer extends GameObject {

    private TextureAtlas runningtextureAtlas;
    private TextureAtlas crouchingTextureAtlas;
    private Texture standing = new Texture("spritesheets/player/standing/s0.png");
    private Texture jumping = new Texture("spritesheets/player/jumping/j0.png");

    TextureRegion bowTexture = new TextureRegion(new Texture("spritesheets/player/bow.png"));
    Sprite bow = new Sprite();

    private Animation playerRunning;
    private Animation playerCrouching;

    private Array<TextureRegion> runningframes;
    private Array<TextureRegion> crouchingFrames;
    private TextureRegion jumpingFrame;
    private TextureRegion standingFrame;
    private TextureRegion standingWithCrouching;
    private TextureRegion firingFrame;

    private float stateTimer;

    short maskBits = GameVariables.BIT_GAME_GROUND | GameVariables.BIT_MENUWALLS | GameVariables.BIT_MENUBUTTON | GameVariables.BIT_GAME_BOX | GameVariables.BIT_GAME_ENEMY;

    public boolean runningRight = true;

    private float cameraTimer = 0;
    private float bulletTimer = 0;

    public boolean isFootContact = false;

    public boolean isJumping = true;
    public boolean isCrouching = false;
    public boolean isFiring = false;

    public float angle = 0;

    public MenuPlayer(FuriousBadi game, World world, float x, float y) {
        super(game, world, x, y);
        createBody();
        //Texture confugiration
        stateTimer = 0;

        //Running
        runningtextureAtlas = new TextureAtlas("spritesheets/player/running/running.pack");

        runningframes = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            runningframes.add(new TextureRegion(runningtextureAtlas.findRegion("r" + i), 0, 0, 250, 400));
        }



        playerRunning = new Animation(0.1f, runningframes);

        //Crouching

        crouchingTextureAtlas = new TextureAtlas("spritesheets/player/crouching/crouching.pack");
        crouchingFrames = new Array<TextureRegion>();

        for (int i = 0; i < 6; i++) {
            crouchingFrames.add(new TextureRegion(crouchingTextureAtlas.findRegion("c" + i), 0, 0, 250, 400));
        }

        playerCrouching = new Animation(0.1f,crouchingFrames);
        standingWithCrouching = new TextureRegion(crouchingTextureAtlas.findRegion("c0"), 0, 0, 250, 400);

        //Others
        jumpingFrame = new TextureRegion(jumping);
        standingFrame = new TextureRegion(standing);
        firingFrame = new TextureRegion(new Texture("spritesheets/player/firing/f0.png"));

        //Set texture
        setRegion(standingFrame);

        //Set dimensions
        setBounds(0, 0, GameVariables.scale(60), GameVariables.scale(92));

        bow.setBounds(0,0,GameVariables.scale(20),GameVariables.scale(100));
        bow.setRegion(bowTexture);
        bow.setOrigin(bow.getWidth()/2,bow.getHeight()/2);

    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getX()), GameVariables.scale(getY()));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape2 = new PolygonShape();
        shape2.setAsBox(GameVariables.scale(20), GameVariables.scale(20));
        fixtureDef.shape = shape2;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution = 0f;
        fixtureDef.filter.categoryBits = GameVariables.BIT_MENUPLAYER;
        fixtureDef.filter.maskBits = GameVariables.BIT_MENUBUTTON | GameVariables.BIT_MENUWALLS | GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR | BIT_GAME_BOX;
        getB2d().createFixture(fixtureDef).setUserData(this);
        CircleShape shape = new CircleShape();
        shape.setRadius(GameVariables.scale(20));
        shape.setPosition(new Vector2(0, GameVariables.scale(45)));
        fixtureDef.shape = shape;
        getB2d().createFixture(fixtureDef).setUserData(this);

        FixtureDef bottomSensor = new FixtureDef();

        PolygonShape shape1 = new PolygonShape();
        shape1.setAsBox(GameVariables.scale(10),GameVariables.scale(5),new Vector2(GameVariables.scale(0),GameVariables.scale(-25)),0);
        bottomSensor.isSensor = true;
        bottomSensor.shape = shape1;
        bottomSensor.filter.categoryBits = GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
        bottomSensor.filter.maskBits = maskBits;

        getB2d().createFixture(bottomSensor).setUserData(this);

        shape.dispose();
        shape1.dispose();
    }

    //Impulses
    Vector2 rightImpulse = new Vector2(0.05f, 0);
    Vector2 leftImpulse = new Vector2(-0.05f, 0);
    Vector2 upImpulse = new Vector2(0, 110f);
    Vector2 downImpulse = new Vector2(0, -0.05f);

    @Override
    public void getInputs(float delta) {
        Vector2 center = getB2d().getWorldCenter();

        if (Gdx.app.getType() == Application.ApplicationType.Android) {
            if (Gdx.input.isTouched()) {
                int pointerX = Gdx.input.getX();
                int pointerY = Gdx.input.getY();
                Gdx.app.debug("TOUCH", pointerX + " " + pointerY);
                if (pointerX <= Gdx.graphics.getWidth() / 3) {
                    if (getB2d().getLinearVelocity().x >= -3) {
                        getB2d().applyLinearImpulse(leftImpulse, center, false);
                    }
                    cameraTimer += delta;
                    if (cameraTimer >= 0.8) {
                        if (((MainMenuScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                            ((MainMenuScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                        }
                    }
                } else if (pointerX <= 2 * Gdx.graphics.getWidth() / 3) {
                    getB2d().applyForce(upImpulse, center, false);

                } else {
                    if (getB2d().getLinearVelocity().x <= 3) {
                        getB2d().applyLinearImpulse(rightImpulse, center, false);
                    }
                    cameraTimer += delta;
                    if (cameraTimer >= 0.8) {
                        if (((MainMenuScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                            ((MainMenuScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                        }
                    }
                }
            } else {
                cameraTimer = 0;
                if (((MainMenuScreen) getGame().getScreen()).gameCamera.zoom > 1) {
                    ((MainMenuScreen) getGame().getScreen()).gameCamera.zoom -= 0.005;
                }
            }
        }

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                if(!isJumping&&!isCrouching){
                    getB2d().applyForce(upImpulse, center, false);
                    isJumping = true;
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.O)) {
                isCrouching = true;
                Filter filterData = getB2d().getFixtureList().get(1).getFilterData();
                filterData.maskBits = 0;
                getB2d().getFixtureList().get(1).setFilterData(filterData);

            }else{
                isCrouching = false;
                Filter filterData = getB2d().getFixtureList().get(1).getFilterData();
                filterData.maskBits = maskBits;
                getB2d().getFixtureList().get(1).setFilterData(filterData);
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                getB2d().applyLinearImpulse(downImpulse, center, false);
            }

            if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
                if(angle<15){
                    angle+=1f;
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                if(angle>-15){
                    angle-=1f;
                }
            }

            if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
                isFiring = true;
                if(bulletTimer == 0){
                    ((Level1World)gameWorld).gameObjects.add(new Bullet(getGame(),getWorld(),0,0,this));
                }else{
                    if(bulletTimer>0.5){
                        ((Level1World)gameWorld).gameObjects.add(new Bullet(getGame(),getWorld(),0,0,this));
                        bulletTimer = 0;
                    }
                }

                bulletTimer+=delta;

            }else{
                bulletTimer = 0;
                isFiring = false;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if(!isFiring){
                    if (getB2d().getLinearVelocity().x >= -1.5) {
                        getB2d().applyLinearImpulse(leftImpulse, center, false);
                    }
                    cameraTimer += delta;
                    if (cameraTimer >= 1) {
                        if (((MainMenuScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                            ((MainMenuScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                        }
                    }
                }
            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if(!isFiring){
                    if (getB2d().getLinearVelocity().x <= 1.5) {
                        getB2d().applyLinearImpulse(rightImpulse, center, false);
                    }
                    cameraTimer += delta;
                    if (cameraTimer >= 0.8) {
                        if (((MainMenuScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                            ((MainMenuScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                        }
                    }
                }
            } else {
                cameraTimer = 0;
                if (((MainMenuScreen) getGame().getScreen()).gameCamera.zoom > 1) {
                    ((MainMenuScreen) getGame().getScreen()).gameCamera.zoom -= 0.005;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        stateTimer += delta;
        setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2 + GameVariables.scale(20));

        if (isFlaggedForDestroy()) {
            if (getB2d() != null && !getWorld().isLocked()) {
                getWorld().destroyBody(getB2d());
                setB2d(null);
            }
        }

        TextureRegion region;
        if(isFiring){
            if(isCrouching){
                if (getB2d().getLinearVelocity().x <= 0.2f && getB2d().getLinearVelocity().x >= -0.2f ) {
                    if(isCrouching){
                        region = standingWithCrouching;
                    }else{
                        region = jumpingFrame;
                    }
                } else {
                    region = (TextureRegion) playerCrouching.getKeyFrame(stateTimer, true);
                }
            }else {
                region = firingFrame;
            }
        } else {
            if(isFootContact){
                if(isCrouching){
                    if (getB2d().getLinearVelocity().x <= 0.2f && getB2d().getLinearVelocity().x >= -0.2f ) {
                        if(isCrouching){
                            region = standingWithCrouching;
                        }else{
                            region = jumpingFrame;
                        }
                    } else {
                        region = (TextureRegion) playerCrouching.getKeyFrame(stateTimer, true);
                    }
                }else{
                    if (getB2d().getLinearVelocity().x <= 0.2f && getB2d().getLinearVelocity().x >= -0.2f ) {
                        region = standingFrame;
                    } else {
                        region = (TextureRegion) playerRunning.getKeyFrame(stateTimer, true);
                    }
                }
            } else {
                if(isCrouching){
                    region = standingWithCrouching;
                }else{
                    region = jumpingFrame;
                }
            }
        }


        if ((getB2d().getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;

        } else if ((getB2d().getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        bow.setRegion(bowTexture);

        if(runningRight){
            bow.setBounds(0,0,GameVariables.scale(20),GameVariables.scale(100));
            if(isCrouching){
                bow.setPosition(getB2d().getPosition().x+GameVariables.scale(15),getB2d().getPosition().y-GameVariables.scale(40));
            } else {
                bow.setPosition(getB2d().getPosition().x+GameVariables.scale(15),getB2d().getPosition().y-GameVariables.scale(30));
            }
            bow.setRotation(angle);
        } else {
            bow.setBounds(0,0,-GameVariables.scale(20),GameVariables.scale(100));
            if(isCrouching) {
                bow.setPosition(getB2d().getPosition().x - GameVariables.scale(15), getB2d().getPosition().y - GameVariables.scale(40));
            } else {
                bow.setPosition(getB2d().getPosition().x-GameVariables.scale(15),getB2d().getPosition().y-GameVariables.scale(30));
            }
            bow.setRotation(-angle);
        }



        setRegion(region);

        if(getB2d().getLinearVelocity().x!=0){
            if(runningRight){
                getB2d().applyLinearImpulse(new Vector2(-getB2d().getLinearVelocity().x/100,0),getB2d().getWorldCenter(),false);
            } else {
                getB2d().applyLinearImpulse(new Vector2(-getB2d().getLinearVelocity().x/100,0),getB2d().getWorldCenter(),false);
            }
        }


    }


    @Override
    public void render(float delta) {
            draw(getGame().getBatch());
        if(isFiring){
            bow.draw(getGame().getBatch());
        }
    }

}
