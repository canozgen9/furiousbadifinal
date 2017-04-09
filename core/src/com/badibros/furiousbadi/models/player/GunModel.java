package com.badibros.furiousbadi.models.player;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/3/17.
 */

public class GunModel extends GameObject {

    public Player player;

    public boolean isFiring = false;
    public boolean isFacingRight = false;

    public float angle = 0;
    public float bulletTimer = 0;
    public int clipCount;
    public int clipCapacity;
    public float attackSpeed = .5f;
    private Texture gunTexture;
    private BulletModel bullet;
    private float gunWidth;
    private float gunHeight;
    private float standingXOffset;
    private float standingYOffset;
    private float crouchingXOffset;
    private float crouchingYOffset;

    public GunModel(FuriousBadi game, World world, float x, float y, Player player, java.lang.String texturePath) {
        super(game, world, x, y);
        this.player = player;
        isFacingRight = player.runningRight;
        angle = player.angle;
        gunTexture = new Texture(texturePath);
    }

    public void setPosition(float standingXOffset, float standingYOffset, float crouchingXOffset, float crouchingYOffset) {
        this.standingXOffset = standingXOffset;
        this.standingYOffset = standingYOffset;
        this.crouchingXOffset = crouchingXOffset;
        this.crouchingYOffset = crouchingYOffset;
    }

    public void setGunSize(float width, float height) {
        this.gunWidth = width;
        this.gunHeight = height;
    }

    public void setAttributes(int clipCount, int clipCapacity) {
        this.clipCount = clipCount;
        this.clipCapacity = clipCapacity;
    }

    public void setBullet(BulletModel bullet) {
        this.bullet = bullet;
    }

    public void prepareObject() {
        setSize(GameVariables.scale(gunWidth), GameVariables.scale(gunHeight));
        setRegion(gunTexture);
        setOrigin(getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void createBody() {

    }

    @Override
    public void getInputs(float delta) {

    }

    @Override
    public void update(float delta) {
        isFacingRight = player.runningRight;
        angle = player.angle;
        if (isFacingRight) {
            setSize(GameVariables.scale(gunWidth), GameVariables.scale(gunHeight));
            if (player.isCrouching) {
                setPosition(player.getB2d().getPosition().x + GameVariables.scale(crouchingXOffset), player.getB2d().getPosition().y + GameVariables.scale(crouchingYOffset));
            } else {
                setPosition(player.getB2d().getPosition().x + GameVariables.scale(standingXOffset), player.getB2d().getPosition().y + GameVariables.scale(standingYOffset));
            }
            setRotation(angle);
        } else {
            setSize(-GameVariables.scale(gunWidth), GameVariables.scale(gunHeight));
            if (player.isCrouching) {
                setPosition(player.getB2d().getPosition().x - GameVariables.scale(crouchingXOffset), player.getB2d().getPosition().y + GameVariables.scale(crouchingYOffset));
            } else {
                setPosition(player.getB2d().getPosition().x - GameVariables.scale(standingXOffset), player.getB2d().getPosition().y + GameVariables.scale(standingYOffset));
            }
            setRotation(-angle);
        }
        setRegion(getTexture());
    }

    @Override
    public void render(float delta) {
        draw(getGame().getBatch());
    }

    @Override
    public void afterDestroyedBody() {

    }

    public boolean canFire() {
        return !(clipCount <= 0 && clipCapacity <= 0);
    }

    public void launchBullet() {
        clipCapacity--;
        if (clipCapacity <= 0) {
            if (clipCount > 0) {
                clipCount--;
                clipCapacity = 5;
            }
        }
    }
}
