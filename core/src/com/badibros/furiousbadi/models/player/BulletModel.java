package com.badibros.furiousbadi.models.player;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Created by canozgen9 on 4/3/17.
 */

public class BulletModel extends GameObject {

    private GunModel gun;


    public BulletModel(FuriousBadi game, World world, GunModel gun, float width, float height, float textureWidth, float textureHeight) {
        super(game, world, gun.getB2d().getPosition().x, gun.getB2d().getPosition().y);
    }

    @Override
    public void createBody() {
//        BodyDef bodyDef = new BodyDef();
//        if(player.runningRight){
//            if(player.isCrouching){
//                bodyDef.position.set(player.getB2d().getPosition().x+ GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(10+player.angle/2));
//            } else {
//                bodyDef.position.set(player.getB2d().getPosition().x+GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(20+player.angle/2));
//            }
//        }else{
//            if(player.isCrouching){
//                bodyDef.position.set(player.getB2d().getPosition().x-GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(10+player.angle/2));
//            } else {
//                bodyDef.position.set(player.getB2d().getPosition().x-GameVariables.scale(20),player.getB2d().getPosition().y+GameVariables.scale(20+player.angle/2));
//            }
//        }
//        bodyDef.type = BodyDef.BodyType.DynamicBody;
//        bodyDef.gravityScale = 0.05f;
//        bodyDef.fixedRotation = false;
//        setB2d(getWorld().createBody(bodyDef));
//        FixtureDef fixtureDef = new FixtureDef();
//        PolygonShape shape = new PolygonShape();
//        shape.setAsBox(GameVariables.scale(10),GameVariables.scale(10));
//        fixtureDef.shape = shape;
//        fixtureDef.density = 1f;
//        fixtureDef.friction = 10f;
//        fixtureDef.restitution = 0;
//        fixtureDef.filter.categoryBits = BIT_GAME_BULLET;
//        fixtureDef.filter.maskBits = BIT_MENUBUTTON | BIT_MENUWALLS | BIT_GAME_GROUND | BIT_GAME_BULLET | BIT_GAME_ENEMY | BIT_GAME_BOX;
//        getB2d().createFixture(fixtureDef).setUserData(this);
//        shape.dispose();
    }

    @Override
    public void getInputs(float delta) {

    }

    @Override
    public void update(float delta) {

    }

    @Override
    public void render(float delta) {

    }

    @Override
    public void afterDestroyedBody() {

    }
}
