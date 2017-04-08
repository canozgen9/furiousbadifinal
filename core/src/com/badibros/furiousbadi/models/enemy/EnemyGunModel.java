package com.badibros.furiousbadi.models.enemy;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.models.player.BulletModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.enemies.FiringEnemy;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/3/17.
 */

public class EnemyGunModel extends GameObject {

    public FiringEnemy enemy;

    public boolean isFiring = false;
    public boolean isFacingRight = false;
    protected float bulletTimer = 0;
    private Texture gunTexture;
    private BulletModel bullet;
    private int clipCount;
    private int clipCapacity;
    private float gunWidth;
    private float gunHeight;
    private float standingXOffset;
    private float standingYOffset;

    public EnemyGunModel(FuriousBadi game, World world, float x, float y, FiringEnemy enemy, String texturePath) {
        super(game, world, x, y);
        this.enemy = enemy;
        isFacingRight = enemy.runningRight;
        gunTexture = new Texture(texturePath);
    }

    public void setGunPosition(float standingXOffset, float standingYOffset) {
        this.standingXOffset = standingXOffset;
        this.standingYOffset = standingYOffset;
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
        isFacingRight = enemy.runningRight;
        if (isFacingRight) {
            setSize(GameVariables.scale(gunWidth), GameVariables.scale(gunHeight));
            setPosition(enemy.getB2d().getPosition().x + GameVariables.scale(standingXOffset), enemy.getB2d().getPosition().y + GameVariables.scale(standingYOffset));
        } else {
            setSize(-GameVariables.scale(gunWidth), GameVariables.scale(gunHeight));
            setPosition(enemy.getB2d().getPosition().x - GameVariables.scale(standingXOffset), enemy.getB2d().getPosition().y + GameVariables.scale(standingYOffset));
        }
        setRegion(getTexture());

        if (enemy.playerDetected) {
            bulletTimer += delta;
            if (bulletTimer > 3) {
                fire();
                bulletTimer = 0;
            }
        }

    }

    @Override
    public void render(float delta) {
        draw(getGame().getBatch());
    }

    @Override
    public void afterDestroyedBody() {

    }

    public void fire() {

    }
}
