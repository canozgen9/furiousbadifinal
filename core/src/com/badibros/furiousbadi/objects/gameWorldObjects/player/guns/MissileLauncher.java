package com.badibros.furiousbadi.objects.gameWorldObjects.player.guns;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.player.GunModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.bullets.Missile;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/6/17.
 */

public class MissileLauncher extends GunModel {

    public MissileLauncher(FuriousBadi game, World world, float x, float y, Player player, String texturePath) {
        super(game, world, x, y, player, texturePath);
        setGunSize(120, 40);
        setPosition(-63, -5, -63, -20);
        setAttributes(5, 5);
        prepareObject();
    }

    @Override
    public void getInputs(float delta) {
        super.getInputs(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            isFiring = true;
            if (bulletTimer == 0) {
                ((LevelWorld) player.gameWorld).gameObjects.add(new Missile(getGame(), getWorld(), this, 25, 10, 50, 10, "spritesheets/player/missile.png", 0.5f));
            } else {
                if (bulletTimer > 0.5) {
                    ((LevelWorld) player.gameWorld).gameObjects.add(new Missile(getGame(), getWorld(), this, 25, 10, 50, 10, "spritesheets/player/missile.png", 0.3f));
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
