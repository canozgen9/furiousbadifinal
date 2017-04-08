package com.badibros.furiousbadi.screens;

import com.badibros.furiousbadi.objects.gameWorldObjects.player.Player;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.guns.Bow;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.guns.MissileLauncher;
import com.badibros.furiousbadi.objects.gameWorldObjects.player.guns.SpaceGun;
import com.badibros.furiousbadi.utils.GameVariables;
import com.badibros.furiousbadi.worlds.LevelWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Hud {
    public float levelUpTimer = 0;
    public float killTimer = 0;
    public boolean willPause = false;
    public boolean paused = false;
    ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private BitmapFont smallFont;
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
    private Texture bleedingTexture;
    private Texture hud;
    private SpriteBatch batch;
    private Integer worldTimer;
    private float timeCount;
    private Integer score;
    private float finishGameTimer = 0;
    private float gameOverTimer = 0;
    private float bleedingTimer = 0;
    private Player player;

    public Hud(SpriteBatch sb, Player player) {
        this.batch = sb;
        this.player = player;
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
        font = generator.generateFont(parameter); // font size 12 pixels
        parameter.size = 16;
        smallFont = generator.generateFont(parameter); // font size 12 pixels
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
        bleedingTexture = new Texture("spritesheets/bleeding.png");
        hud = new Texture("img/hud.png");

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

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

    public void updateInfo(Player player) {
        score = player.experience;
        nameLabel.setText("Furious Badi  Level:" + player.level);
        scoreLabel.setText(String.format("Exp: %04d", score));
    }

    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //NAME
        shapeRenderer.setColor(Color.valueOf("212121"));
        shapeRenderer.rect(80, GameVariables.HEIGHT - 52, 180, 26);
        //HEALTH
        shapeRenderer.setColor(Color.valueOf("b71c1c"));
        shapeRenderer.rect(80, GameVariables.HEIGHT - 74, 180, 24);
        shapeRenderer.setColor(Color.valueOf("f44336"));
        shapeRenderer.rect(80, GameVariables.HEIGHT - 74, 180 * player.health / player.maxHealth, 24);
        //EXP
        shapeRenderer.setColor(Color.valueOf("f57f17"));
        shapeRenderer.rect(80, GameVariables.HEIGHT - 86, 160, 14);
        shapeRenderer.setColor(Color.valueOf("ffc107"));
        shapeRenderer.rect(80, GameVariables.HEIGHT - 86, 160 * player.experience / 1000, 14);
        shapeRenderer.end();
        //stage.draw();
        batch.begin();
        font.draw(batch, "Stage " + ((LevelWorld) player.gameWorld).level + "", GameVariables.WIDTH - 120, GameVariables.HEIGHT - 15);
        batch.draw(hud, 10, GameVariables.HEIGHT - 100);
        String gunName = "";
        if (player.selectedGun instanceof Bow) gunName = "EnemyBow";
        else if (player.selectedGun instanceof MissileLauncher) gunName = "Missile Launcher";
        else if (player.selectedGun instanceof SpaceGun) gunName = "Space Gun";
        font.draw(batch, gunName, 15, 45);
        if (levelUpTimer > 0) {
            levelUpTimer -= delta;
            batch.draw(levelUpTexture, viewport.getScreenWidth() / 2 - levelUpTexture.getWidth() / 2, viewport.getScreenHeight() / 2 - levelUpTexture.getHeight() / 2);
        }

        if (killTimer > 0) {
            killTimer -= delta;
            batch.draw(killTexture, 50, 50, viewport.getScreenWidth() / 4, viewport.getScreenWidth() / 4 * 2 / 7);
        }

        if (finishGameTimer > 0) {
            finishGameTimer -= delta;
            font.draw(batch, "Succesfull!!", viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2);
        }

        if (finishGameTimer < 0) {
            ((MainScreen) player.getGame().getScreen()).currentWorld = new LevelWorld(player.getGame(), ((MainScreen) player.getGame().getScreen()).viewport, ((MainScreen) player.getGame().getScreen()).gameCamera, 1);
        }

        if (gameOverTimer > 0) {
            gameOverTimer -= delta;
            font.draw(batch, "Game Over!!", viewport.getScreenWidth() / 2, viewport.getScreenHeight() / 2);
        }
        if (gameOverTimer < 0) {
            ((MainScreen) player.getGame().getScreen()).currentWorld = new LevelWorld(player.getGame(), ((MainScreen) player.getGame().getScreen()).viewport, ((MainScreen) player.getGame().getScreen()).gameCamera, ((LevelWorld) player.gameWorld).level);
        }

        if (bleedingTimer > 0) {
            bleedingTimer -= delta;
            batch.draw(bleedingTexture, 0, 0, viewport.getScreenWidth(), viewport.getScreenHeight());
        }
        font.draw(batch, player.level + "", 53, GameVariables.HEIGHT - 45);
        if (willPause) {
            font.draw(batch, "PAUSED", 450, 500);
            if (!paused) {
                ((LevelWorld) player.gameWorld).running = (!((LevelWorld) player.gameWorld).running);
                paused = true;
            }
        }
        batch.end();
    }

    public void finishGame(int result) {
        if (result == 1) {
            finishGameTimer = 1;
        } else {
            gameOverTimer = 1;
        }
    }

    public void addBleedingTimer(float time) {
        bleedingTimer += time;
    }

    public void pause() {
        willPause = true;
    }

}
