package com.badibros.furiousbadi.objects.gameWorldObjects.enemies.guns;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.enemy.EnemyGunModel;
import com.badibros.furiousbadi.objects.gameWorldObjects.enemies.FiringEnemy;
import com.badibros.furiousbadi.objects.gameWorldObjects.enemies.bullets.EnemyArrow;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/6/17.
 */

public class EnemyBow extends EnemyGunModel {

    public EnemyBow(FuriousBadi game, World world, float x, float y, FiringEnemy enemy, String texturePath) {
        super(game, world, x, y, enemy, texturePath);
        setGunSize(20, 100);
        setGunPosition(15, -30);
        setAttributes(5, 5);
        prepareObject();
    }

    @Override
    public void getInputs(float delta) {
        super.getInputs(delta);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
    }

    @Override
    public void fire() {
        ((LevelWorld) enemy.player.gameWorld).gameObjectsToAdd.add(new EnemyArrow(getGame(), getWorld(), this, 10, 10, 50, 10, "spritesheets/enemies/" + enemy.type + "/arrow.png", 0.1f));
    }
}
