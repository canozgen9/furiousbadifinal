package com.badibros.furiousbadi.objects.gameWorldObjects.collectables;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_COIN;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_GAME_GROUND;
import static com.badibros.furiousbadi.utils.GameVariables.BIT_PLAYER;

public class Coin extends GameObject {

    public float damage = 80f;
    private Array<TextureRegion> animationFrames;
    private Animation animation;
    private TextureRegion bulletInitialFrame;
    private float stateTimer = 0;

    private boolean destroy = false;
    private boolean destroyed = false;
    private boolean hitted = false;
    private boolean dead = false;

    public Coin(FuriousBadi game, World world, float x, float y) {
        super(game, world, x, y);
        createBody();

        stateTimer = 0;

        setOrigin(getWidth() / 2, getHeight() / 2);

        getB2d().applyLinearImpulse(.1f, 5, getB2d().getWorldCenter().x, getB2d().getWorldCenter().y, false);

        animationFrames = new Array<TextureRegion>();
        Texture texture = new Texture("spritesheets/enviroment/coin.png");
        for (int i = 0; i < 10; i++) {
            animationFrames.add(new TextureRegion(texture, i * 100, 0, 100, 100));
        }
        animation = new Animation(0.05f, animationFrames);

        setSize(GameVariables.scale(30), GameVariables.scale(30));

    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();

        bodyDef.position.set(getInitialX(), getInitialY());

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.gravityScale = .5f;
        bodyDef.fixedRotation = false;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(GameVariables.scale(15));
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.1f;
        fixtureDef.restitution = .3f;
        fixtureDef.filter.categoryBits = BIT_GAME_COIN;
        fixtureDef.filter.maskBits = BIT_GAME_GROUND | BIT_PLAYER | BIT_GAME_COIN;
        getB2d().createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    @Override
    public void getInputs(float delta) {


    }

    @Override
    public void update(float delta) {
        setPosition(getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2);
        if (!destroyed && destroy) {
            getWorld().destroyBody(getB2d());
            destroyed = true;
        }

    }


    @Override
    public void render(float delta) {
        stateTimer += delta;
        TextureRegion textureRegion = (TextureRegion) animation.getKeyFrame(stateTimer, true);
        setRegion(textureRegion);
        if (!destroyed)
            draw(getGame().getBatch());
    }

    @Override
    public void afterDestroyedBody() {

    }

    public void onHitted() {
        destroy = true;
        hitted = true;
    }

}
