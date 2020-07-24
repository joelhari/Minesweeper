package ch.joelstudios.minesweeper.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.Random;

import ch.joelstudios.minesweeper.Entities.Button;
import ch.joelstudios.minesweeper.Main;

public class MainMenuScene implements IScene {
    private int x, y;
    private int width, height;

    private Main main;

    // 0 : play Button, 1 : stats Button, 2 : settings Button
    private Button[] buttons;

    private int touchButton;

    private BitmapFont fontL;

    private Random random;

    private String[] messages = {
            "have fun!",
            "Minesweeper forever!",
            "press Play!",
            "hi!",
            "how are you?",
            "sälü"
    };
    private int curM;
    private int mX, mY;
    private int relmX, relmY;

    public MainMenuScene(Main main, Preferences preferences, int width, int height) {
        this.main = main;
        this.width = width;
        this.height = height;

        random = new Random();

        init();
    }

    private void init() {
        x = 0;
        y = 0;

        buttons = new Button[] {
                new Button(width/6, height/2 + width/4, 2*width/3, width/6, new Texture("button.png"), new Texture("button_pressed.png"), "Play"),
                new Button(width/6, height/2, 2*width/3, width/6, new Texture("button.png"), new Texture("button_pressed.png"), "Stats"),
                new Button(width/6, height/2 - width/4, 2*width/3, width/6, new Texture("button.png"), new Texture("button_pressed.png"), "Settings")
        };

        touchButton = -1;

        mX = width/8;
        mY = height/6;

        relmX = mX;
        relmY = mY;

        createFont(width/9);
    }

    public void update() {

    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font) {
        batch.begin();
        for (Button button : buttons) {
            button.draw(batch);
            button.drawFont(batch, fontL);
        }
        font.draw(batch, messages[curM], mX, mY);
        batch.end();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        init();
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;

        mX = x + relmX;
        mY = y + relmY;

        for (Button button : buttons)
            button.moveTo(x, y);
    }

    public void touchDown(int screenX, int screenY) {
        boolean onButton = false;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].onButton(screenX, screenY)) {
                touchButton = i;
                buttons[i].setPressed(true);
                onButton = true;
            }
        }
        if (!onButton) {
            touchButton = -1;
        }
    }

    public void touchUp(int screenX, int screenY) {
        if (touchButton >= 0 && touchButton < buttons.length) {
            if (buttons[touchButton].onButton(screenX, screenY)) {
                buttonPressed();
            }
            buttons[touchButton].setPressed(false);
        }
    }

    public void touchDragged(int screenX, int screenY) {

    }

    private void buttonPressed() {
        switch (touchButton) {
            case 0:
                main.changeScene(SceneManager.GAMEPLAY_SCENE);
                break;
            case 1:
                main.changeScene(SceneManager.STATS_SCENE);
                break;
            case 2:
                main.changeScene(SceneManager.SETTINGS_SCENE);
                break;
        }
    }

    public void lostFocus() {

    }

    public void gainedFocus() {
        curM = random.nextInt(messages.length - 1);
    }

    public void dispose() {
        for (Button button : buttons)
            button.dispose();
    }

    private void createFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = (size);
        parameter.borderColor = Color.BLACK;
        parameter.color = Color.BLACK;
        fontL = generator.generateFont(parameter);
        generator.dispose();
    }
}
