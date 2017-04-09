package com.badibros.furiousbadi.objects.gameWorldObjects.enemies;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.models.enemy.EnemyGunModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.collectables.Coin;
import com.badibros.furiousbadi.objects.gameWorldObjects.enemies.guns.EnemyBow;
import com.badibros.furiousbadi.screens.MainScreen;
import com.badibros.furiousbadi.utils.GameLogic;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BABY_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BOX;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_BULLET;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_FIRING_ENEMY;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_FIRING_ENEMY_PLAYER_DETECTION_SENSOR;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_PLAYER;

public class FiringEnemy extends GameObject {

    private static int killedEnemy = 0;
    //BabyEnemy Attributes
    public boolean runningRight = true;
    public float damage = 50f;
    //Player Optimization
    public com.badibros.furiousbadi.objects.gameWorldObjects.player.Player player;
    public boolean playerDetected = false;
    public String type;
    private int experience;
    private float health;
    private float maxHealth;
    private float width;
    private float height;
    //Disappera animation
    private Array<TextureRegion> disappearFrames;
    private Animation disappearAnimation;
    //BabyEnemy animation
    private TextureAtlas runningtextureAtlas;
    private TextureRegion standingFrame;
    private Array<TextureRegion> runningFrames;
    private Animation runningAnimation;
    private float playerDetectX = GameVariables.scale(300);
    private float playerDetectY = GameVariables.scale(200);
    //Impulses
    private Vector2 rightImpulse;
    private Vector2 leftImpulse;
    private Vector2 upImpulse = new Vector2(0, 5f);
    private Vector2 downImpulse = new Vector2(0, -0.1f);
    //Object variables
    private boolean hitted = false;
    private boolean dead = false;
    //Animation timer
    private float stateTimer;
    //Gun
    private EnemyGunModel gun;

    private boolean isDecayed = false;

    public FiringEnemy(FuriousBadi game, World world, float x, float y, com.badibros.furiousbadi.objects.gameWorldObjects.player.Player player, float health, float damage, String type) {
        super(game, world, x, y);
        this.player = player;
        this.experience = (int) health;
        this.health = health;
        this.maxHealth = health;
        this.damage = damage;
        this.type = type;

        createBody();
        setBounds(0, 0, GameVariables.scale(width), GameVariables.scale(height));

        stateTimer = 0;

        //Disappear Frames
        disappearFrames = new Array<TextureRegion>();
        Texture texture = new Texture("spritesheets/player/smoke.png");
        for (int i = 0; i < 10; i++) {
            disappearFrames.add(new TextureRegion(texture, i * 128, 0, 128, 128));
        }
        disappearAnimation = new Animation(0.1f, disappearFrames);

        //Running Frames
        runningtextureAtlas = new TextureAtlas("spritesheets/enemies/" + type + "/running.pack");

        runningFrames = new Array<TextureRegion>();
        for (int i = 0; i < 5; i++) {
            runningFrames.add(new TextureRegion(runningtextureAtlas.findRegion("r" + i), 0, 0, 250, 400));
            if (i == 0) {
                standingFrame = new TextureRegion(runningtextureAtlas.findRegion("r" + i), 0, 0, 250, 400);
            }
        }

        runningAnimation = new Animation(0.1f, runningFrames);

        setSize(GameVariables.scale(60), GameVariables.scale(92));
        setRegion(standingFrame);

        rightImpulse = new Vector2(0.1f, 0);
        leftImpulse = new Vector2(-0.1f, 0);

        setGun(new EnemyBow(game, world, getB2d().getPosition().x, getB2d().getPosition().y, this, "spritesheets/enemies/" + type + "/bow.png"));

    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY()));
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
        fixtureDef.filter.categoryBits = BIT_GAME_FIRING_ENEMY;
        fixtureDef.filter.maskBits = BIT_GAME_BABY_ENEMY | BIT_PLAYER | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_BOX | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR | BIT_GAME_FIRING_ENEMY;
        getB2d().createFixture(fixtureDef).setUserData(this);

        CircleShape shape = new CircleShape();
        shape.setRadius(GameVariables.scale(20));
        shape.setPosition(new Vector2(0, GameVariables.scale(45)));
        fixtureDef.shape = shape;
        getB2d().createFixture(fixtureDef).setUserData(this);

        PolygonShape detectionShape = new PolygonShape();
        detectionShape.setAsBox(playerDetectX * 2, playerDetectY);

        FixtureDef fixtureDef1 = new FixtureDef();
        fixtureDef1.shape = detectionShape;
        fixtureDef1.isSensor = true;
        fixtureDef1.filter.categoryBits = BIT_GAME_FIRING_ENEMY_PLAYER_DETECTION_SENSOR;
        fixtureDef1.filter.maskBits = BIT_PLAYER;

        getB2d().createFixture(fixtureDef1).setUserData(this);

        shape.dispose();

    }

    @Override
    public void getInputs(float delta) {
    }

    @Override
    public void update(float delta) {
        stateTimer += delta;
        if (!hitted) {
            setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2 + GameVariables.scale(20));
        }

        checkDestroyFlag();

        if (playerDetected) {
            Vector2 center = getB2d().getWorldCenter();
            if (getB2d().getPosition().x > player.getB2d().getPosition().x + 1f) {
                if (getB2d().getLinearVelocity().x >= -0.2)
                    getB2d().applyLinearImpulse(leftImpulse, center, false);
            } else if (getB2d().getPosition().x < player.getB2d().getPosition().x - 1f) {
                if (getB2d().getLinearVelocity().x <= 0.2)
                    getB2d().applyLinearImpulse(rightImpulse, center, false);
            }

        }

        TextureRegion region;
        if (getB2d().getLinearVelocity().x <= 0.2f && getB2d().getLinearVelocity().x >= -0.2f) {
            region = standingFrame;
        } else {
            region = (TextureRegion) runningAnimation.getKeyFrame(stateTimer, true);
        }

        if ((getB2d().getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX()) {
            region.flip(true, false);
            runningRight = false;

        } else if ((getB2d().getLinearVelocity().x > 0 || runningRight) && region.isFlipX()) {
            region.flip(true, false);
            runningRight = true;
        }

        setRegion(region);
        gun.update(delta);
    }


    @Override
    public void render(float delta) {
        if (hitted) {
            stateTimer += delta;
            TextureRegion textureRegion = (TextureRegion) disappearAnimation.getKeyFrame(stateTimer, false);
            setRegion(textureRegion);
            if (stateTimer > 1f) {
                dead = true;
            }
        }
        if (!dead) {
            draw(getGame().getBatch());
            gun.render(delta);
        }
    }

    @Override
    public void afterDestroyedBody() {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/enemy-die.wav"));
        sound.play(.2f);
        for (int i = 0; i < (int) maxHealth / 100; i++) {
            ((LevelWorld) ((MainScreen) getGame().getScreen()).currentWorld).gameObjectsToAdd.add(new Coin(getGame(), getWorld(), getB2d().getPosition().x, getB2d().getPosition().y));
        }
    }

    public void onHitted(float damage, int bulletType) {
        if (!hitted) {
            health -= damage;
            if (!GameLogic.isMatched(type, bulletType)) {
                if (!isDecayed) {
                    ((LevelWorld) ((MainScreen) getGame().getScreen()).currentWorld).addObject("FiringEnemy", type, 3, getB2d().getPosition().x, getB2d().getPosition().y);
                    isDecayed = true;
                }
                setSize(GameVariables.scale(width * 3 / 2), GameVariables.scale(height * 3 / 2));
                setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);
                destroyBody();
                hitted = true;
            } else {
                if (health <= 0) {
                    killedEnemy++;
                    if (killedEnemy % 2 == 1) {
                        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/kill.ogg"));
                        sound.play(.5f);

                    } else {
                        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/double-kill.ogg"));
                        sound.play(.5f);
                    }
                    setSize(GameVariables.scale(width * 3 / 2), GameVariables.scale(height * 3 / 2));
                    setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);
                    destroyBody();
                    hitted = true;
                    player.experience += experience;
                    if (player.experience >= 1000) {
                        player.experience -= 1000;
                        player.level++;
                        ((LevelWorld) ((MainScreen) getGame().getScreen()).currentWorld).hud.levelUpTimer += 1;
                    }
                    ((LevelWorld) ((MainScreen) getGame().getScreen()).currentWorld).hud.killTimer += 1;
                    ((LevelWorld) ((MainScreen) getGame().getScreen()).currentWorld).hud.updateInfo(player);
                }
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
