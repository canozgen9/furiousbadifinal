package com.badibros.furiousbadi.models.player;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/3/17.
 */

public class GunModel extends GameObject {

    public boolean isFiring = false;
    public float angle = 0;
    private BulletModel bullet;
    private MenuPlayer player;
    private int clipCount;
    private int clipCapacity;
    private boolean isFacingRight = false;
    private Texture gunTexture;

    public GunModel(FuriousBadi game, World world, float x, float y, MenuPlayer player, java.lang.String texturePath) {
        super(game, world, x, y);
        this.player = player;
        isFacingRight = player.runningRight;
        angle = player.angle;

        gunTexture = new Texture(texturePath);
        setSize(GameVariables.scale(20), GameVariables.scale(100));
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
        setRegion(getTexture());

        if (player.runningRight) {
            setSize(GameVariables.scale(20), GameVariables.scale(100));
            if (player.isCrouching) {
                setPosition(player.getB2d().getPosition().x + GameVariables.scale(15), player.getB2d().getPosition().y - GameVariables.scale(40));
            } else {
                setPosition(player.getB2d().getPosition().x + GameVariables.scale(15), player.getB2d().getPosition().y - GameVariables.scale(30));
            }
            setRotation(angle);
        } else {
            setBounds(0, 0, -GameVariables.scale(20), GameVariables.scale(100));
            if (player.isCrouching) {
                setPosition(player.getB2d().getPosition().x - GameVariables.scale(15), player.getB2d().getPosition().y - GameVariables.scale(40));
            } else {
                setPosition(player.getB2d().getPosition().x - GameVariables.scale(15), player.getB2d().getPosition().y - GameVariables.scale(30));
            }
            setRotation(-angle);
        }

    }

    @Override
    public void render(float delta) {
        if (isFiring) {
            draw(getGame().getBatch());
        }
    }

    @Override
    public void afterDestroyedBody() {

    }
}
