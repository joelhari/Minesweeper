package ch.joelstudios.minesweeper.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Align;

import ch.joelstudios.minesweeper.Entities.Board;
import ch.joelstudios.minesweeper.Entities.Button;
import ch.joelstudios.minesweeper.Main;
import ch.joelstudios.minesweeper.Prefs;

public class SettingsScene implements IScene {
    private int x, y;
    private int screenWidth;
    private int screenHeight;
    private int width;

    private Main main;

    private Preferences preferences;

    // 0 : back Button
    private Button[] buttons;

    private int touchButton;

    private BitmapFont fontS;
    private BitmapFont fontL;

    private int textX1;
    private int textY1;

    private int reglerX1;
    private int reglerY1;

    private int reglerBarX1;
    private int reglerBarY1;

    private int textX2;
    private int textY2;

    private int reglerX2;
    private int reglerY2;

    private int reglerBarX2;
    private int reglerBarY2;

    private int textX3;
    private int textY3;

    private int posX3;
    private int posY3;
    private int size3;
    private int spriteSize;

    private int reglerBarWidth;
    private int reglerBarHeight;
    private int reglerSize;

    private int[] reglerX;

    private int reglerBarSlotWidth;
    private int reglerBarSlotHeight;
    private int reglerBarSlotY1;
    private int reglerBarSlotY2;
    private int[] reglerBarSlots;

    private int textX5;
    private int textY5;
    private int textX6;
    private int textY6;
    private int textX7;
    private int textY7;
    private int textX8;
    private int textY8;

    private boolean movingRegler1;
    private boolean movingRegler2;
    private int movingDelta;

    private Texture regler;

    private int difficulty;
    private int fieldSize;

    private int[] difficultyValues;
    private int[] fieldSizeValues;

    public SettingsScene(Main main, Preferences preferences, int width, int height) {
        this.main = main;
        this.preferences = preferences;
        this.width = width;
        this.screenWidth = width;
        this.screenHeight = height;

        x = 0;
        y = 0;

        difficulty = preferences.getInteger(Prefs.DIFFICULTY.key(), Prefs.DIFFICULTY.defaultValue());
        fieldSize = preferences.getInteger(Prefs.FIELD_SIZE.key(), Prefs.FIELD_SIZE.defaultValue());

        regler = new Texture("reglerkopf.png");

        difficultyValues = new int[] {10, 7, 5, 4};
        fieldSizeValues = new int[] {6, 8, 10, 12};

        movingRegler1 = false;
        movingRegler2 = false;
        movingDelta = 0;

        init();
        init2();
    }

    private void init() {
        reglerSize = width/10;
        reglerBarWidth = width - 2*(width/8 + reglerSize/2);
        reglerBarHeight = reglerSize/7;

        textX1 = x + width/8;
        textY1 = y + screenHeight - width/8;

        reglerX1 = x + width/8;
        reglerY1 = textY1 - width/18 - width/36 - reglerSize;

        reglerBarX1 = x + width/8 + reglerSize/2;
        reglerBarY1 = reglerY1 + 3*reglerBarHeight;

        textX2 = x + width/8;
        textY2 = y + textY1 - width/18 - 2*width/36 - reglerSize - width/27 - width/8;

        reglerX2 = x + width/8;
        reglerY2 = textY2 - width/18 - width/36 - reglerSize;

        reglerBarX2 = x + width/8 + reglerSize/2;
        reglerBarY2 = reglerY2 + 3*reglerBarHeight;

        textX3 = x + width/8;
        textY3 = y + textY2 - width/18 - 2*width/36 - reglerSize - width/27 - width/8;

        reglerX = new int[] {x + width/8, x + width/8 + (6*width/8 - reglerSize)/3,  x + width/8 + 2*(6*width/8 - reglerSize)/3, x + width/8 + (6*width/8 - reglerSize)};

        reglerBarSlotWidth = reglerSize/10;
        reglerBarSlotHeight = reglerSize/2;
        reglerBarSlotY1 = reglerBarY1 + reglerBarHeight/2 - reglerBarSlotHeight/2;
        reglerBarSlotY2 = reglerBarY2 + reglerBarHeight/2 - reglerBarSlotHeight/2;
        reglerBarSlots = new int[] {reglerX[0] + reglerSize/2 - reglerBarSlotWidth /2, reglerX[1] + reglerSize/2 - reglerBarSlotWidth /2, reglerX[2] + reglerSize/2 - reglerBarSlotWidth /2, reglerX[3] + reglerSize/2 - reglerBarSlotWidth /2};

        for (int i = 0; i < difficultyValues.length; i++) {
            if (difficultyValues[i] == difficulty) {
                reglerX1 = reglerX[i];
            }
        }
        for (int i = 0; i < fieldSizeValues.length; i++) {
            if (fieldSizeValues[i] == fieldSize) {
                reglerX2 = reglerX[i];
            }
        }

        textX5 = x + width/8;
        textY5 = textY1 - width/18 - 2*width/36 - reglerSize;
        textX6 = x + width - width/8;
        textY6 = textY1 - width/18 - 2*width/36 - reglerSize;

        textX7 = x + width/8;
        textY7 = textY2 - width/18 - 2*width/36 - reglerSize;
        textX8 = x + width - width/8;
        textY8 = textY2 - width/18 - 2*width/36 - reglerSize;
    }

    private void init2() {
        int[][] spritePos = new int[6][2];

        spriteSize = width/8;

        int x1 = x + width/8;
        int y1 = textY3 - width/18 - width/36 - spriteSize;
        int xd = width/12 + width/8;
        int yd = width/18 + spriteSize;

        spritePos[0][0] = x1;
        spritePos[0][1] = y1;
        spritePos[1][0] = x1 + xd;
        spritePos[1][1] = y1;
        spritePos[2][0] = x1 + 2*xd;
        spritePos[2][1] = y1;
        spritePos[3][0] = x1 + 3*xd;
        spritePos[3][1] = y1;

        spritePos[4][0] = x1;
        spritePos[4][1] = y1 - yd;
        spritePos[5][0] = x1 + width/12 + width/8;
        spritePos[5][1] = y1 - yd;

        int backButtonY = y + width/16;

        buttons = new Button[] {
                new Button(x + width - width/16 - width/6, backButtonY, width/6, width/6, new Texture("back_button2.png"), new Texture("back_button_pressed.png")),
                new Button(spritePos[0][0], spritePos[0][1], spriteSize, spriteSize, new Texture("field.png"), new Texture("field_pressed.png")),
                new Button(spritePos[1][0], spritePos[1][1], spriteSize, spriteSize, new Texture("field_blue.png"), new Texture("field_pressed_blue.png")),
                new Button(spritePos[2][0], spritePos[2][1], spriteSize, spriteSize, new Texture("field_green.png"), new Texture("field_pressed_green.png")),
                new Button(spritePos[3][0], spritePos[3][1], spriteSize, spriteSize, new Texture("field_orange.png"), new Texture("field_pressed_orange.png")),
                new Button(spritePos[4][0], spritePos[4][1], spriteSize, spriteSize, new Texture("field_lila.png"), new Texture("field_pressed_lila.png")),
                new Button(spritePos[5][0], spritePos[5][1], spriteSize, spriteSize, new Texture("field_pink.png"), new Texture("field_pressed_pink.png"))
        };

        createFont(width/27);

        update_pressed();

        touchButton = -1;
    }

    private void update_pressed() {
        int color = preferences.getInteger(Prefs.FIELD_COLOR.key(), Prefs.FIELD_COLOR.defaultValue());
        int boarder = spriteSize/24;
        posX3 = buttons[color + 1].getX() - boarder;
        posY3 = buttons[color + 1].getY() - boarder;
        size3 = spriteSize + 2*boarder;
    }

    public void update() {

    }

    public void render(SpriteBatch batch, ShapeRenderer renderer, BitmapFont font) {

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(reglerBarX1, reglerBarY1, reglerBarWidth, reglerBarHeight);
        renderer.rect(reglerBarX2, reglerBarY2, reglerBarWidth, reglerBarHeight);

        for (int x : reglerBarSlots) {
            renderer.rect(x, reglerBarSlotY1, reglerBarSlotWidth, reglerBarSlotHeight);
        }
        for (int x : reglerBarSlots) {
            renderer.rect(x, reglerBarSlotY2, reglerBarSlotWidth, reglerBarSlotHeight);
        }

        renderer.rect(posX3, posY3, size3, size3);
        renderer.end();

        batch.begin();
        fontL.draw(batch, "Difficulty:  ", textX1, textY1);
        fontL.draw(batch, "Field Size:  ", textX2, textY2);
        fontL.draw(batch, "Color:  ", textX3, textY3);

        fontS.draw(batch, "Easy", textX5, textY5);
        fontS.draw(batch, "Hard", textX6, textY6, 0, Align.right, false);

        fontS.draw(batch, "Large", textX7, textY7);
        fontS.draw(batch, "Small", textX8, textY8, 0, Align.right, false);

        for (Button button : buttons) {
            button.draw(batch);
        }

        batch.draw(regler, reglerX1, reglerY1, reglerSize, reglerSize);
        batch.draw(regler, reglerX2, reglerY2, reglerSize, reglerSize);
        batch.end();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.screenWidth = width;
        this.screenHeight = height;

        x = 0;
        y = 0;

        init();
        init2();
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;

        init();

        for (Button button : buttons)
            button.moveTo(x, y);

        update_pressed();
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

        if (screenX >= reglerX1 && screenX < reglerX1 + reglerSize &&
            screenY >= reglerY1 && screenY < reglerY1 + reglerSize) {
            movingRegler1 = true;
            movingDelta = screenX - reglerX1;
        }
        else if (screenX >= reglerX2 && screenX < reglerX2 + reglerSize &&
                screenY >= reglerY2 && screenY < reglerY2 + reglerSize) {
            movingRegler2 = true;
            movingDelta = screenX - reglerX2;
        }
    }

    public void touchUp(int screenX, int screenY) {
        if (touchButton >= 0 && touchButton < buttons.length) {
            if (buttons[touchButton].onButton(screenX, screenY)) {
                buttonPressed();
            }
            buttons[touchButton].setPressed(false);
        }
        if (movingRegler1) {
            reglerX1 = screenX - movingDelta;
            int min = width;
            int newX = 0;
            for (int i = 0; i < reglerX.length; i++) {
                int delta = Math.abs(reglerX1 - reglerX[i]);
                if (delta <= min) {
                    min = delta;
                    newX = i;
                }
            }
            reglerX1 = reglerX[newX];
            difficulty = difficultyValues[newX];
            preferences.putInteger(Prefs.DIFFICULTY.key(), difficulty);
            preferences.flush();
        }
        else if (movingRegler2) {
            reglerX2 = screenX - movingDelta;
            int min = width;
            int newX = 0;
            for (int i = 0; i < reglerX.length; i++) {
                int delta = Math.abs(reglerX2 - reglerX[i]);
                if (delta <= min) {
                    min = delta;
                    newX = i;
                }
            }
            reglerX2 = reglerX[newX];
            fieldSize = fieldSizeValues[newX];
            preferences.putInteger(Prefs.FIELD_SIZE.key(), fieldSize);
            preferences.flush();
        }
        movingRegler1 = false;
        movingRegler2 = false;
    }

    public void touchDragged(int screenX, int screenY) {
        if (movingRegler1) {
            reglerX1 = screenX - movingDelta;
            if (reglerX1 < reglerX[0]) {
                reglerX1 = reglerX[0];
            } else if (reglerX1 > reglerX[reglerX.length - 1]) {
                reglerX1 = reglerX[reglerX.length - 1];
            }
        }
        else if (movingRegler2) {
            reglerX2 = screenX - movingDelta;
            if (reglerX2 < reglerX[0]) {
                reglerX2 = reglerX[0];
            } else if (reglerX2 > reglerX[reglerX.length - 1]) {
                reglerX2 = reglerX[reglerX.length - 1];
            }
        }
    }

    private void buttonPressed() {
        switch (touchButton) {
            case 0:
                main.changeScene(SceneManager.MAINMENU_SCENE);
                break;
            case 1:
                preferences.putInteger(Prefs.FIELD_COLOR.key(), Board.FIELD_COLOR_GREY);
                preferences.flush();
                update_pressed();
                break;
            case 2:
                preferences.putInteger(Prefs.FIELD_COLOR.key(), Board.FIELD_COLOR_BLUE);
                preferences.flush();
                update_pressed();
                break;
            case 3:
                preferences.putInteger(Prefs.FIELD_COLOR.key(), Board.FIELD_COLOR_GREEN);
                preferences.flush();
                update_pressed();
                break;
            case 4:
                preferences.putInteger(Prefs.FIELD_COLOR.key(), Board.FIELD_COLOR_ORANGE);
                preferences.flush();
                update_pressed();
                break;
            case 5:
                preferences.putInteger(Prefs.FIELD_COLOR.key(), Board.FIELD_COLOR_LILA);
                preferences.flush();
                update_pressed();
                break;
            case 6:
                preferences.putInteger(Prefs.FIELD_COLOR.key(), Board.FIELD_COLOR_PINK);
                preferences.flush();
                update_pressed();
                break;
        }
    }

    public void lostFocus() {

    }

    public void gainedFocus() {
        difficulty = preferences.getInteger(Prefs.DIFFICULTY.key(), Prefs.DIFFICULTY.defaultValue());
        fieldSize = preferences.getInteger(Prefs.FIELD_SIZE.key(), Prefs.FIELD_SIZE.defaultValue());
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
        fontS = generator.generateFont(parameter);
        parameter.size = width/18;
        fontL = generator.generateFont(parameter);
        generator.dispose();
    }
}
