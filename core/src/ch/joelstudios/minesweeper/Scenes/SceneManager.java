package ch.joelstudios.minesweeper.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ch.joelstudios.minesweeper.Main;
import ch.joelstudios.minesweeper.Timer;

public class SceneManager implements IScene {
    public static final int MAINMENU_SCENE = 0;
    public static final int GAMEPLAY_SCENE = 1;
    public static final int STATS_SCENE = 2;
    public static final int SETTINGS_SCENE = 3;

    private int width, height;

    private IScene currentScene;
    private IScene newScene;

    private boolean changingScene;

    private Timer timer;
    private final int ANIMATION_TIME = 250;
    private int dx;
    private double delta;
    private int currentX;

    private Main main;

    // 0 : Mainmenu Scene, 1 : Gameplay Scene, 2 : Stats Scene, 3 : Settings Scene
    private IScene[] scenes;

    public SceneManager(Main main, Preferences preferences, int width, int height) {
        this.main = main;

        this.width = width;
        this.height = height;

        dx = width/ANIMATION_TIME;
        delta = Math.PI/ANIMATION_TIME/2;
        currentX = 0;

        timer = new Timer();

        scenes = new IScene[] {
                new MainMenuScene(main, preferences, width, height),
                new Game(main, preferences, width, height),
                new StatsScene(main, preferences, width, height),
                new SettingsScene(main, preferences, width, height)
        };

        changingScene = false;

        currentScene = scenes[0];
        newScene = scenes[0];
    }

    public void changeScene(int scene) {
        Gdx.graphics.setContinuousRendering(true);

        newScene = scenes[scene];

        if (newScene != currentScene) {
            changingScene = true;

            timer.start();
        }
        else {
            lostFocus();
            currentScene = newScene;
        }
        gainedFocus();
    }

    public void update() {
        if (!changingScene) {
            currentScene.update();
        }
        else {
            if (timer.isRunning() && timer.getTime() > ANIMATION_TIME) {
                timer.stop();

                currentScene.moveTo(0, 0);
                currentX = 0;

                changingScene = false;
                lostFocus();
                currentScene = newScene;

                Gdx.graphics.setContinuousRendering(false);
                Gdx.graphics.requestRendering();
            }
            else {
                currentX = (int)((1 - Math.cos(delta*timer.getTime()))*width);
                currentScene.moveTo(currentX, 0);
            }
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer renderer, BitmapFont font) {
        if (!changingScene) {
            currentScene.render(batch, renderer, font);
        }
        else {
            newScene.render(batch, renderer, font);
            renderer.begin(ShapeRenderer.ShapeType.Filled);
            renderer.setColor(Color.WHITE);
            renderer.rect(currentX, 0, width, height);
            renderer.end();
            currentScene.render(batch, renderer, font);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        dx = width/ANIMATION_TIME;
        currentX = 0;

        for (IScene scene : scenes)
            scene.resize(width, height);
    }

    public void moveTo(int x, int y) {

    }

    public void touchDown(int screenX, int screenY) {
        if (!changingScene) {
            currentScene.touchDown(screenX, screenY);
        }
    }

    public void touchUp(int screenX, int screenY) {
        if (!changingScene) {
            currentScene.touchUp(screenX, screenY);
        }
    }

    public void touchDragged(int screenX, int screenY) {
        if (!changingScene) {
            currentScene.touchDragged(screenX, screenY);
        }
    }

    public void lostFocus() {
        currentScene.lostFocus();
    }

    public void gainedFocus() {
        newScene.gainedFocus();
    }

    public void dispose() {
        for (IScene scene : scenes)
            scene.dispose();
    }
}
