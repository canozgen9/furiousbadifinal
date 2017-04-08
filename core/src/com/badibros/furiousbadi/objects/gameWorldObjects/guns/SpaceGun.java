package com.badibros.furiousbadi.objects.gameWorldObjects.guns;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.player.GunModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.Player;
import com.badibros.furiousbadi.objects.gameWorldObjects.bullets.BlueBullet;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/6/17.
 */

public class SpaceGun extends GunModel {

    public SpaceGun(FuriousBadi game, World world, float x, float y, Player player, String texturePath) {
        super(game, world, x, y, player, texturePath);
        setGunSize(120, 30);
        setPosition(-55, -5, -55, -15);
        setAttributes(5, 5);
        prepareObject();
    }

    @Override
    public void getInputs(float delta) {
        super.getInputs(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            isFiring = true;
            if (bulletTimer == 0) {
                ((LevelWorld) player.gameWorld).gameObjects.add(new BlueBullet(getGame(), getWorld(), this, 10, 10, 10, 10, "spritesheets/player/blue_bullet.png", 0.6f));
            } else {
                if (bulletTimer > 0.5) {
                    ((LevelWorld) player.gameWorld).gameObjects.add(new BlueBullet(getGame(), getWorld(), this, 10, 10, 10, 10, "spritesheets/player/blue_bullet.png", 0.6f));
                    bulletTimer = 0;
                }
            }
            bulletTimer += delta;
        } else {
            isFiring = false;
        }
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }
}
