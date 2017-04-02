package com.badibros.furiousbadi.screens;

import com.badibros.furiousbadi.objects.mainMenuWorldObjects.MenuPlayer;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by canozgen9 on 4/2/17.
 */

public class Hud {
    public float levelUpTimer = 0;
    public float killTimer = 0;
    private Stage stage;
    private Viewport viewport;
    private Label countDownLabel;
    private Label scoreLabel;
    private Label timeLabel;
    private Label levelLevel;
    private Label worldLabel;
    private Label nameLabel;
    private Texture killTexture;
    private Texture levelUpTexture;
    private SpriteBatch batch;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;

    public Hud(SpriteBatch sb) {
        this.batch = sb;
        worldTimer = 300;
        timeCount = 0;
        score = 0;

        viewport = new FitViewport(GameVariables.WIDTH, GameVariables.HEIGHT, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        Table table = new Table();
        table.top();
        table.setFillParent(true);

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 35;
        parameter.borderWidth = 2;
        parameter.borderColor = Color.BLACK;
        parameter.shadowColor = Color.BLACK;
        parameter.shadowOffsetX = 3;
        parameter.shadowOffsetY = 3;
        BitmapFont font = generator.generateFont(parameter); // font size 12 pixels
        generator.dispose();
        countDownLabel = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(font, Color.WHITE));
        countDownLabel.getStyle().background = new SpriteDrawable(new Sprite(new Texture("spritesheets/enviroment/sphereHolder.png")));

        scoreLabel = new Label(String.format("Exp: %04d", score), new Label.LabelStyle(font, Color.WHITE));
        scoreLabel.getStyle().background = new SpriteDrawable(new Sprite(new Texture("spritesheets/enviroment/sphereHolder.png")));
        timeLabel = new Label("TIME", new Label.LabelStyle(font, Color.WHITE));
        timeLabel.getStyle().background = new SpriteDrawable(new Sprite(new Texture("spritesheets/enviroment/sphereHolder.png")));
        levelLevel = new Label("1", new Label.LabelStyle(font, Color.WHITE));
        levelLevel.getStyle().background = new SpriteDrawable(new Sprite(new Texture("spritesheets/enviroment/sphereHolder.png")));
        worldLabel = new Label("Level", new Label.LabelStyle(font, Color.WHITE));
        worldLabel.getStyle().background = new SpriteDrawable(new Sprite(new Texture("spritesheets/enviroment/sphereHolder.png")));
        nameLabel = new Label("Furious Badi  Level: 1", new Label.LabelStyle(font, Color.WHITE));
        nameLabel.getStyle().background = new SpriteDrawable(new Sprite(new Texture("spritesheets/enviroment/sphereHolder.png")));
        levelUpTexture = new Texture("img/levelup.png");

        killTexture = new Texture("img/killed.png");

        table.add(nameLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);

        table.row();

        table.add(scoreLabel).expandX();
        table.add(levelLevel).expandX();
        table.add(countDownLabel).expandX();

        stage.addActor(table);
    }

    public void update(float delta) {
        timeCount += delta;
        if (timeCount >= 1) {
            worldTimer--;
            countDownLabel.setText(String.format("%03d", worldTimer));
            timeCount = 0;
        }
    }

    public void updateInfo(MenuPlayer player) {
        score = player.experience;
        nameLabel.setText("Furious Badi  Level:" + player.level);
        scoreLabel.setText(String.format("Exp: %04d", score));
    }

    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        stage.draw();
        batch.begin();
        if (levelUpTimer >= 0) {
            levelUpTimer -= delta;
            batch.draw(levelUpTexture, viewport.getScreenWidth() / 2 - levelUpTexture.getWidth() / 2, viewport.getScreenHeight() / 2 - levelUpTexture.getHeight() / 2);
        }

        if (killTimer >= 0) {
            killTimer -= delta;
            batch.draw(killTexture, 50, 50, viewport.getScreenWidth() / 4, viewport.getScreenWidth() / 4 * 2 / 7);
        }

        batch.end();
    }

}
