package ch.joelstudios.minesweeper.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Random;

import ch.joelstudios.minesweeper.Scenes.Game;
import ch.joelstudios.minesweeper.Timer;

public class Board {

    public static int FIELD_COLOR_GREY = 0;
    public static int FIELD_COLOR_BLUE = 1;
    public static int FIELD_COLOR_GREEN = 2;
    public static int FIELD_COLOR_ORANGE = 3;
    public static int FIELD_COLOR_LILA = 4;
    public static int FIELD_COLOR_PINK = 5;

    private final int TOUCH_TIME = 300;
    private int color = 1;

    private int screenX, screenY;
    private int width, height;

    private Field[][] fields;
    private int numFieldsX, numFieldsY;
    private int fieldSize;
    private int spacing;
    private int numBombs;
    private int numBombsLeft;

    private int difficulty;

    private int touchX, touchY;

    private Sprite[] sprites;
    private Sprite[] numbres;
    private Random random;
    private Timer timer;

    private boolean gameOver;
    private boolean isVictory;

    private boolean touchDown;
    private Field touchField;

    private Game game;

    public Board(Game game, int screenX, int screenY, int width, int hieght, int numFieldsX, int difficulty, int color) {
        this.game = game;
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = width;
        this.height = hieght;
        this.numFieldsX = numFieldsX;
        this.difficulty = difficulty;
        this.color = color;

        random = new Random();

        init();
    }

    public void init() {

        gameOver = false;
        touchDown = false;
        isVictory = false;

        timer = new Timer();

        switch (color) {
            case 0:
                sprites = new Sprite[] {
                        new Sprite(new Texture("field.png")),
                        new Sprite(new Texture("field_pressed.png")),
                        new Sprite(new Texture("field_uncovered.png")),
                        new Sprite(new Texture("flag.png")),
                        new Sprite(new Texture("bomb.png")),
                        new Sprite(new Texture("flag_pressed.png")),
                };
                break;
            case 1:
                sprites = new Sprite[] {
                        new Sprite(new Texture("field_blue.png")),
                        new Sprite(new Texture("field_pressed_blue.png")),
                        new Sprite(new Texture("field_uncovered_blue.png")),
                        new Sprite(new Texture("flag_blue.png")),
                        new Sprite(new Texture("bomb_blue.png")),
                        new Sprite(new Texture("flag_pressed_blue.png")),
                };
                break;
            case 2:
                sprites = new Sprite[] {
                        new Sprite(new Texture("field_green.png")),
                        new Sprite(new Texture("field_pressed_green.png")),
                        new Sprite(new Texture("field_uncovered_green.png")),
                        new Sprite(new Texture("flag_green.png")),
                        new Sprite(new Texture("bomb_green.png")),
                        new Sprite(new Texture("flag_pressed_green.png")),
                };
                break;
            case 3:
                sprites = new Sprite[] {
                        new Sprite(new Texture("field_orange.png")),
                        new Sprite(new Texture("field_pressed_orange.png")),
                        new Sprite(new Texture("field_uncovered_orange.png")),
                        new Sprite(new Texture("flag_orange.png")),
                        new Sprite(new Texture("bomb_orange.png")),
                        new Sprite(new Texture("flag_pressed_orange.png")),
                };
                break;
            case 4:
                sprites = new Sprite[] {
                        new Sprite(new Texture("field_lila.png")),
                        new Sprite(new Texture("field_pressed_lila.png")),
                        new Sprite(new Texture("field_uncovered_lila.png")),
                        new Sprite(new Texture("flag_lila.png")),
                        new Sprite(new Texture("bomb_lila.png")),
                        new Sprite(new Texture("flag_pressed_lila.png")),
                };
                break;
            case 5:
                sprites = new Sprite[] {
                        new Sprite(new Texture("field_pink.png")),
                        new Sprite(new Texture("field_pressed_pink.png")),
                        new Sprite(new Texture("field_uncovered_pink.png")),
                        new Sprite(new Texture("flag_pink.png")),
                        new Sprite(new Texture("bomb_pink.png")),
                        new Sprite(new Texture("flag_pressed_pink.png")),
                };
                break;
        }

        numbres = new Sprite[] {
                new Sprite(new Texture("1.png")),
                new Sprite(new Texture("2.png")),
                new Sprite(new Texture("3.png")),
                new Sprite(new Texture("4.png")),
                new Sprite(new Texture("5.png")),
                new Sprite(new Texture("6.png")),
                new Sprite(new Texture("7.png")),
                new Sprite(new Texture("8.png")),
        };

        createBoard();
    }

    private void createBoard() {
        fieldSize = width/numFieldsX;
        numFieldsY = height/fieldSize;
        spacing = fieldSize/40;
        numBombs = (numFieldsX*numFieldsY)/difficulty;

        numBombsLeft = numBombs;

        fields = new Field[numFieldsX][numFieldsY];

        // initialise Field
        for (int x = 0; x < numFieldsX; x++) {
            for (int y = 0; y < numFieldsY; y++) {
                fields[x][y] = new Field(x*fieldSize, y*fieldSize, fieldSize, spacing);
            }
        }

        // set Bombs
        for (int i = 0; i < numBombs; i++) {
            int x = random.nextInt(numFieldsX);
            int y = random.nextInt(numFieldsY);

            if (!fields[x][y].isBomb()) {
                fields[x][y].setBomb(true);
            } else {
                i--;
            }
        }

        setNeighbors();
        calculateValues();
    }

    private void setNeighbors() {
        for (int x = 0; x < numFieldsX; x++) {
            for (int y = 0; y < numFieldsY; y++) {
                ArrayList<Field> neighborsA = new ArrayList<Field>();

                for (int w = x - 1; w < x + 2; w++) {
                    for (int h = y - 1; h < y + 2; h++) {
                        if (!(w == x && h == y)) {
                            try {
                                neighborsA.add(fields[w][h]);
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                }
                Field[] neighbors = neighborsA.toArray(new Field[0]);
                fields[x][y].setNeighbors(neighbors);
            }
        }
    }

    private void calculateValues() {
        for (int x = 0; x < numFieldsX; x++) {
            for (int y = 0; y < numFieldsY; y++) {
                fields[x][y].calculateValue();
            }
        }
    }

    private void uncoverAll() {
        for (int x = 0; x < numFieldsX; x++) {
            for (int y = 0; y < numFieldsY; y++) {
                fields[x][y].setCovered(false);
            }
        }
    }

    private void checkForVictory() {
        int fieldsLeft = 0;
        for (int x = 0; x < numFieldsX; x++) {
            for (int y = 0; y < numFieldsY; y++) {
                if (fields[x][y].isCovered()) fieldsLeft++;
            }
        }
        if (fieldsLeft == numBombs) {
            gameOver = true;
            isVictory = true;
        }
    }

    public void update() {
        if (timer.isRunning() && timer.getTime() >= TOUCH_TIME) {
            timer.stop();
            Gdx.input.vibrate(50);
        }
    }

    public void render(SpriteBatch batch, BitmapFont font) {
        for (int x = 0; x < numFieldsX; x++) {
            for (int y = 0; y < numFieldsY; y++) {
                fields[x][y].render(batch, font, sprites, numbres);
            }
        }
    }

    public void resize(int screenX, int screenY, int width, int height, int numFieldsX) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = width;
        this.height = height;
        this.numFieldsX = numFieldsX;

        init();
    }

    public void moveTo(int x, int y) {
        this.screenX = x;
        this.screenY = y;

        for (Field[] f : fields) {
            for (Field field : f) {
                field.moveTo(x, y);
            }
        }
    }

    public void touchDown(int screenX, int screenY) {
        touchX = screenX / fieldSize;
        touchY = screenY / fieldSize;
        touchDown = true;
        timer.start();

        if (touchX >= 0 && touchX < numFieldsX && touchY >= 0 && touchY < numFieldsY) {
            touchField = fields[touchX][touchY];
            if (touchField.isCovered())
                touchField.setPressed(true);
        }
    }

    public void touchUp(int screenX, int screenY) {
        if (touchField != null)
            touchField.setPressed(false);
        if (touchDown) {
            int x = screenX / fieldSize;
            int y = screenY / fieldSize;

            if (touchX != x || touchY != y) {
                timer.stop();
                return;
            }

            if (x >= 0 && x < numFieldsX && y >= 0 && y < numFieldsY) {
                Field f = fields[x][y];
                if (timer.getTime() >= TOUCH_TIME && f.isCovered()) {
                    if (timer.isRunning()) {
                        timer.stop();
                        Gdx.input.vibrate(50);
                    }
                    f.setFlag(!f.isFlag());
                    numBombsLeft = f.isFlag() ? --numBombsLeft : ++numBombsLeft;
                } else if (f.isBomb() && !f.isFlag()) {
                    uncoverAll();
                    gameOver = true;
                } else if (!f.isFlag() && !f.isCovered() && f.getValue() == f.getNumNeighboringFlags()) {
                    if (!f.uncoverNeighbours()) {
                        uncoverAll();
                        gameOver = true;
                    }
                    checkForVictory();
                } else {
                    f.uncover();
                    checkForVictory();
                }
            }
        }
        touchDown = false;
        timer.stop();
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public boolean isVictory() {
        return isVictory;
    }

    public int getNumBombsLeft() {
        return numBombsLeft;
    }

    public void dispose() {
        for (Sprite sprite : sprites)
            sprite.getTexture().dispose();


        for (Sprite sprite : numbres)
            sprite.getTexture().dispose();
    }

    public int getFieldSize() {
        return numFieldsX;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getColor() {
        return color;
    }
}
