package ch.joelstudios.minesweeper.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import ch.joelstudios.minesweeper.Scenes.Game;
import ch.joelstudios.minesweeper.Scenes.SceneManager;

public class EndGame {
    public static final int VICTORY = 0;
    public static final int GAME_OVER = 1;

    private String[] messages;
    private int currentMessage;

    private int mX;
    private int mY;

    // 0 : Menu, 1 : play Again
    private Button[] buttons;

    private int touchButton;

    private int x, y;
    private int relX, relY;
    private int width;
    private int height;

    private BitmapFont fontL;

    private Game game;

    public EndGame(Game game, int x, int y, int width, int height) {
        this.game = game;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        relX = x;
        relY = y;

        init();

        messages = new String[] {"Victory", "Game Over"};
        currentMessage = 0;

    }

    private void init() {
        buttons = new Button[] {
                new Button(x + width/40, y + height/40, 9*width/20, 9*width/80, new Texture("button.png"), new Texture("button_pressed.png"), "Menu"),
                new Button(x + 21*width/40, y + height/40, 9*width/20, 9*width/80, new Texture("button.png"), new Texture("button_pressed.png"), "Play Again")
        };

        mX = x;
        mY = y + height - height/20;

        createFont((width + 2*x)/9);
    }

    public void render(SpriteBatch batch, ShapeRenderer renderer, BitmapFont font) {
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.WHITE);
        renderer.rect(x, y, width, height);
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.BLACK);
        renderer.rect(x, y, width, height);
        renderer.end();

        batch.begin();
        fontL.draw(batch, messages[currentMessage], mX, mY, width, Align.center, false);

        for (Button button : buttons) {
            button.draw(batch);
            button.drawFont(batch, font);
        }
        batch.end();
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

    private void buttonPressed() {
        switch (touchButton) {
            case 0:
                game.endGame(SceneManager.MAINMENU_SCENE);
                break;
            case 1:
                game.endGame(SceneManager.GAMEPLAY_SCENE);
                break;
        }
    }

    public void resize(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        relX = x;
        relY = y;

        init();
    }

    public void moveTo(int x, int y) {
        this.x = x + relX;
        this.y = y + relY;

        mX = this.x;
        mY = this.y + height - height/20;

        for (Button button : buttons)
            button.moveTo(x, y);
    }

    public void dispose() {
        for (Button button : buttons)
            button.dispose();
    }

    public void setCurrentMessage(int currentMessage) {
        this.currentMessage = currentMessage;
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
