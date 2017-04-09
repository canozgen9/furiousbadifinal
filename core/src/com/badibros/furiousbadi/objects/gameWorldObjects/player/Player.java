package com.badibros.furiousbadi.objects.gameWorldObjects.player;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.models.player.GunModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.guns.Bow;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.guns.MissileLauncher;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.guns.SpaceGun;
import com.badibros.furiousbadi.screens.MainScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
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

import java.util.ArrayList;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_FINISH_AREA;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR;

public class Player extends GameObject {

    public int experience = 1;
    public int level = 1;
    public int coin;
    public float health = 1000;
    public float maxHealth = 1000;
    public boolean runningRight = true;
    public boolean isCrouching = false;
    public boolean canStandUp = true;
    public boolean canJump = false;
    public float angle = 0;
    public GunModel selectedGun;
    public int topSensorColliding = 0;
    public int bottomSensorColliding = 0;
    private short maskBits = GameVariables.BIT_GAME_GROUND | GameVariables.BIT_MENUWALLS | GameVariables.BIT_MENUBUTTON | GameVariables.BIT_GAME_BOX | GameVariables.BIT_GAME_ENEMY;
    //Impulses
    private Vector2 rightImpulse = new Vector2(0.05f, 0);
    private Vector2 leftImpulse = new Vector2(-0.05f, 0);
    private Vector2 upImpulse = new Vector2(0, 110f);
    private Vector2 downImpulse = new Vector2(0, -0.05f);
    private TextureAtlas runningtextureAtlas;
    private TextureAtlas crouchingTextureAtlas;
    private Texture standing = new Texture("spritesheets/player/standing/s0.png");
    private Texture jumping = new Texture("spritesheets/player/jumping/j0.png");
    private Animation playerRunning;
    private Animation playerCrouching;
    private Array<TextureRegion> runningframes;
    private Array<TextureRegion> crouchingFrames;
    private TextureRegion jumpingFrame;
    private TextureRegion standingFrame;
    private TextureRegion standingWithCrouching;
    private TextureRegion firingFrame;
    private float stateTimer;
    private float cameraTimer = 0;
    private ArrayList<GunModel> guns;
    private int selectedGunIndex = 0;

    public Player(FuriousBadi game, World world, float x, float y) {
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

        playerCrouching = new Animation(0.1f, crouchingFrames);
        standingWithCrouching = new TextureRegion(crouchingTextureAtlas.findRegion("c0"), 0, 0, 250, 400);

        //Others
        jumpingFrame = new TextureRegion(jumping);
        standingFrame = new TextureRegion(standing);
        firingFrame = new TextureRegion(new Texture("spritesheets/player/firing/f0.png"));

        //Set texture
        setRegion(standingFrame);

        //Set dimensions
        setSize(GameVariables.scale(60), GameVariables.scale(92));

        guns = new ArrayList<GunModel>();

        guns.add(new Bow(game, world, getB2d().getPosition().x, getB2d().getPosition().y, this, "spritesheets/player/bow.png"));
        guns.add(new MissileLauncher(game, world, getB2d().getPosition().x, getB2d().getPosition().y, this, "spritesheets/player/missile_launcher.png"));
        guns.add(new SpaceGun(game, world, getB2d().getPosition().x, getB2d().getPosition().y, this, "spritesheets/player/space_gun.png"));

        selectedGun = guns.get(selectedGunIndex);
    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY()));
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = true;
        setB2d(getWorld().createBody(bodyDef));

        //BODY
        FixtureDef bodyFixtureDef = new FixtureDef();
        PolygonShape bodyShape = new PolygonShape();
        bodyShape.setAsBox(GameVariables.scale(20), GameVariables.scale(20));
        bodyFixtureDef.shape = bodyShape;
        bodyFixtureDef.density = 1f;
        bodyFixtureDef.friction = 0f;
        bodyFixtureDef.restitution = 0f;
        bodyFixtureDef.filter.categoryBits = GameVariables.BIT_PLAYER;
        bodyFixtureDef.filter.maskBits = BIT_GAME_ENEMY_BULLET | GameVariables.BIT_GAME_COIN | GameVariables.BIT_MENUBUTTON | GameVariables.BIT_MENUWALLS | GameVariables.BIT_GAME_GROUND | GameVariables.BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_ENEMY_PLAYER_DETECTION_SENSOR | BIT_GAME_BOX | BIT_FINISH_AREA;
        getB2d().createFixture(bodyFixtureDef).setUserData(this);

        //HEAD
        CircleShape headShape = new CircleShape();
        headShape.setRadius(GameVariables.scale(20));
        headShape.setPosition(new Vector2(0, GameVariables.scale(45)));
        bodyFixtureDef.shape = headShape;
        getB2d().createFixture(bodyFixtureDef).setUserData(this);

        //BOTTOM SENSOR
        FixtureDef bottomSensor = new FixtureDef();
        PolygonShape bottomSensorShape = new PolygonShape();
        bottomSensorShape.setAsBox(GameVariables.scale(10), GameVariables.scale(5), new Vector2(GameVariables.scale(0), GameVariables.scale(-25)), 0);
        bottomSensor.isSensor = true;
        bottomSensor.shape = bottomSensorShape;
        bottomSensor.filter.categoryBits = GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
        bottomSensor.filter.maskBits = maskBits;
        getB2d().createFixture(bottomSensor).setUserData(this);

        //TOP SENSOR
        PolygonShape topSensorShape = new PolygonShape();
        topSensorShape.setAsBox(GameVariables.scale(15), GameVariables.scale(10), new Vector2(0, GameVariables.scale(55)), 0);
        FixtureDef topSensorFixtureDef = new FixtureDef();
        topSensorFixtureDef.shape = topSensorShape;
        topSensorFixtureDef.isSensor = true;
        topSensorFixtureDef.filter.categoryBits = GameVariables.BIT_GAME_PLAYER_TOP_SENSOR;
        topSensorFixtureDef.filter.maskBits = GameVariables.BIT_GAME_GROUND;
        getB2d().createFixture(topSensorFixtureDef).setUserData(this);

        headShape.dispose();
        bottomSensorShape.dispose();
        topSensorShape.dispose();
    }

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
                        if (((MainScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                            ((MainScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
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
                        if (((MainScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                            ((MainScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                        }
                    }
                }
            } else {
                cameraTimer = 0;
                if (((MainScreen) getGame().getScreen()).gameCamera.zoom > 1) {
                    ((MainScreen) getGame().getScreen()).gameCamera.zoom -= 0.005;
                }
            }
        }

        if (Gdx.app.getType() == Application.ApplicationType.Desktop) {

            if (Gdx.input.isKeyJustPressed(Input.Keys.W)) {
                if (canJump && !isCrouching) {
                    getB2d().applyForce(upImpulse, center, false);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                isCrouching = true;
                Filter filterData = getB2d().getFixtureList().get(1).getFilterData();
                filterData.maskBits = 0;
                getB2d().getFixtureList().get(1).setFilterData(filterData);

            } else {
                if (canStandUp) {
                    isCrouching = false;
                    Filter filterData = getB2d().getFixtureList().get(1).getFilterData();
                    filterData.maskBits = maskBits;
                    getB2d().getFixtureList().get(1).setFilterData(filterData);
                }
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                getB2d().applyLinearImpulse(downImpulse, center, false);
            }

            if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                selectedGunIndex++;
                selectedGun = guns.get((selectedGunIndex) % guns.size());
            } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                selectedGunIndex--;
                if (selectedGunIndex < 0) {
                    selectedGunIndex = guns.size() - 1;
                }
                selectedGun = guns.get(selectedGunIndex % guns.size());
            }
            selectedGun.getInputs(delta);
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                if (getB2d().getLinearVelocity().x >= -1.5) {
                    getB2d().applyLinearImpulse(leftImpulse, center, false);
                }
                cameraTimer += delta;
                if (cameraTimer >= 1) {
                    if (((MainScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                        ((MainScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                    }
                }

            } else if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                if (getB2d().getLinearVelocity().x <= 1.5) {
                    getB2d().applyLinearImpulse(rightImpulse, center, false);
                }
                cameraTimer += delta;
                if (cameraTimer >= 0.8) {
                    if (((MainScreen) getGame().getScreen()).gameCamera.zoom < 1.2f) {
                        ((MainScreen) getGame().getScreen()).gameCamera.zoom += 0.005f;
                    }
                }

            } else {
                cameraTimer = 0;
                if (((MainScreen) getGame().getScreen()).gameCamera.zoom > 1) {
                    ((MainScreen) getGame().getScreen()).gameCamera.zoom -= 0.005;
                }
            }
        }
    }

    @Override
    public void update(float delta) {
        canStandUp = topSensorColliding == 0;
        canJump = bottomSensorColliding != 0;
        stateTimer += delta;
        setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2 + GameVariables.scale(20));

        if (isFlaggedForDestroy()) {
            if (getB2d() != null && !getWorld().isLocked()) {
                getWorld().destroyBody(getB2d());
                setB2d(null);
            }
        }

        TextureRegion region;
        if (canJump) {
            if (isCrouching) {
                if (getB2d().getLinearVelocity().x <= 0.2f && getB2d().getLinearVelocity().x >= -0.2f) {
                    if (isCrouching) {
                        region = standingWithCrouching;
                    } else {
                        region = jumpingFrame;
                    }
                } else {
                    region = (TextureRegion) playerCrouching.getKeyFrame(stateTimer, true);
                }
            } else {
                if (getB2d().getLinearVelocity().x <= 0.2f && getB2d().getLinearVelocity().x >= -0.2f) {
                    region = standingFrame;
                } else {
                    region = (TextureRegion) playerRunning.getKeyFrame(stateTimer, true);
                }
            }
        } else {
            if (isCrouching) {
                region = standingWithCrouching;
            } else {
                region = jumpingFrame;
            }

        }


        if ((getB2d().getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;

        } else if ((getB2d().getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        setRegion(region);

        selectedGun.update(delta);

        if (getB2d().getLinearVelocity().x != 0) {
            if (runningRight) {
                getB2d().applyLinearImpulse(new Vector2(-getB2d().getLinearVelocity().x / 100, 0), getB2d().getWorldCenter(), false);
            } else {
                getB2d().applyLinearImpulse(new Vector2(-getB2d().getLinearVelocity().x / 100, 0), getB2d().getWorldCenter(), false);
            }
        }
    }


    @Override
    public void render(float delta) {
        draw(getGame().getBatch());
        selectedGun.render(delta);
    }

    @Override
    public void afterDestroyedBody() {

    }

    public void onHitted(float damage) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/hurt.wav"));
        sound.play(.5f);
        health -= damage;
        ((LevelWorld) gameWorld).hud.addBleedingTimer(0.5f);
        if (health <= 0) {
            health = 0;
            ((LevelWorld) gameWorld).finishGame(0);
        }
    }

}
