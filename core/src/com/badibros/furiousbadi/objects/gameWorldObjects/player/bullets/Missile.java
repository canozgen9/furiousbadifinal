package com.badibros.furiousbadi.objects.gameWorldObjects.player.bullets;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.player.BulletModel;
import com.badibros.furiousbadi.models.player.GunModel;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/6/17.
 */

public class Missile extends BulletModel {
    public Missile(FuriousBadi game, World world, GunModel gun, float width, float height, float textureWidth, float textureHeight, String texturePath, float speed) {
        super(game, world, gun, width, height, textureWidth, textureHeight, texturePath, speed);
        setDamage(200f);
        type = 3;
        Sound sound = Gdx.audio.newSound(Gdx.files.internal("sound/missile.wav"));
        sound.play(.3f);
    }
}
