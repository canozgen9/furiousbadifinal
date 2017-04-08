package com.badibros.furiousbadi.objects.gameWorldObjects.enemies.bullets;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.enemy.EnemyBulletModel;
import com.badibros.furiousbadi.models.enemy.EnemyGunModel;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/6/17.
 */

public class EnemyArrow extends EnemyBulletModel {
    public EnemyArrow(FuriousBadi game, World world, EnemyGunModel gun, float width, float height, float textureWidth, float textureHeight, String texturePath, float speed) {
        super(game, world, gun, width, height, textureWidth, textureHeight, texturePath, speed);
        setDamage(150f);
    }
}
