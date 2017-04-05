package com.badibros.furiousbadi.objects.mainMenuWorldObjects;

import com.badibros.furiousbadi.FuriousBadi;
import com.badibros.furiousbadi.models.GameObject;
import com.badibros.furiousbadi.screens.MainScreen;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;


public class MenuButton extends GameObject{


    private String defaultImage, selectedImage;
    private Clickable clickable;
    private boolean doWork = false;
    private float doWorkTimer = 0;
    private int doWorkTimeout = 1;

    public MenuButton(FuriousBadi game, World world, float x, float y, String defaultImage, String selectedImage, Clickable clickable) {
        super(game, world,x,y);
        createBody();

        this.clickable = clickable;

        this.defaultImage = defaultImage;
        this.selectedImage = selectedImage;

        //Set texture
        setTexture(new Texture(defaultImage));

        //Set dimensions
        setSize(GameVariables.scale(300), GameVariables.scale(80));

    }

    @Override
    public void createBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(GameVariables.scale(getInitialX()), GameVariables.scale(getInitialY()));
        bodyDef.type = BodyDef.BodyType.StaticBody;
        setB2d(getWorld().createBody(bodyDef));
        FixtureDef fixtureDef = new FixtureDef();
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(GameVariables.scale(150),GameVariables.scale(40));
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.1f;
        fixtureDef.filter.categoryBits = GameVariables.BIT_MENUBUTTON;
        fixtureDef.filter.maskBits = GameVariables.BIT_MENUPLAYER | GameVariables.BIT_GAME_PLAYER_BOTTOM_SENSOR;
        getB2d().createFixture(fixtureDef).setUserData(this);
        shape.dispose();
    }

    public void hitted(){
        setTexture(new Texture(selectedImage));
        doWork = true;
    }

    @Override
    public void getInputs(float delta) {

    }

    @Override
    public void update(float delta) {
        if(isFlaggedForDestroy()){
            if(getB2d()!=null&&!getWorld().isLocked()){
                getWorld().destroyBody(getB2d());
                setB2d(null);
            }
        }
        if(doWork){
            doWorkTimer += delta;
            if (((MainScreen) getGame().getScreen()).gameCamera.zoom >= 0.700) {
                ((MainScreen) getGame().getScreen()).gameCamera.zoom -= 0.005;
            }
            if(doWorkTimer >= doWorkTimeout *1f){
                clickable.onClick();
            }
        }

    }

    @Override
    public void render(float delta) {
        if(getB2d()!=null){
            getGame().getBatch().begin();
            getGame().getBatch().draw(getTexture(), getB2d().getPosition().x - getWidth() / 2, getB2d().getPosition().y - getHeight() / 2, getWidth(), getHeight());
            getGame().getBatch().end();
        }
    }

    @Override
    public void afterDestroyedBody() {

    }
}

