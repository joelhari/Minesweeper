package ch.joelstudios.minesweeper.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ch.joelstudios.minesweeper.Entities.Board;
import ch.joelstudios.minesweeper.Entities.EndGame;
import ch.joelstudios.minesweeper.Main;
import ch.joelstudios.minesweeper.Prefs;
import ch.joelstudios.minesweeper.Timer;
import ch.joelstudios.minesweeper.Entities.TopBar;

public class Game implements IScene {
    private final int RESET_TIME = 1000;

    private int x, y;
    private int width, height;

    private Timer resetTimer;

    private Main main;
    private Preferences preferences;

    private TopBar topBar;
    private Board board;

    private EndGame endGame;
    private boolean showEndGame;

    private int tbx, tby, tbwidth, tbheight;
    private int box, boy, bowidth, boheight;

    private int egx, egy, egwidth, egheight;

    private int numFieldsX;
    private int difficulty;
    private int color;

    public Game(Main main, Preferences preferences, int width, int height) {
        this.main = main;
        this.preferences = preferences;
        this.width = width;
        this.height = height;

        x = 0;
        y = 0;

        resetTimer = new Timer();

        numFieldsX = preferences.getInteger(Prefs.FIELD_SIZE.key(), Prefs.FIELD_SIZE.defaultValue());
        difficulty = preferences.getInteger(Prefs.DIFFICULTY.key(), Prefs.DIFFICULTY.defaultValue());
        color = preferences.getInteger(Prefs.FIELD_COLOR.key(), Prefs.FIELD_COLOR.defaultValue());

        init();

        board = new Board(this, box, boy, bowidth, boheight, numFieldsX, difficulty, color);
        topBar = new TopBar(this, tbx, tby, tbwidth, tbheight);
        endGame = new EndGame(this, egx, egy, egwidth, egheight);
    }

    private void init() {
        int fieldSize = width/numFieldsX;
        int numFieldsY = height/fieldSize - 1;

        box = x;
        boy = y;
        bowidth = width;
        boheight = numFieldsY * fieldSize;

        tbx = x;
        tby = y + numFieldsY * fieldSize;
        tbwidth = width;
        tbheight = height - boheight;

        egx = width/8;
        egwidth = 3*width/4;
        egheight = 3*width/4;
        egy = (height - egheight)/2;

        showEndGame = false;
    }

    public void endGame(int nextScene) {
        main.changeScene(nextScene);
    }

    private void updatePrefs() {
        int tmp = preferences.getInteger(Prefs.NUM_GAMES_PLAYED.key(), Prefs.NUM_GAMES_PLAYED.defaultValue()) + 1;
        preferences.putInteger(Prefs.NUM_GAMES_PLAYED.key(), tmp);

        if (board.isVictory()) {
            tmp = preferences.getInteger(Prefs.NUM_GAMES_WON.key(), Prefs.NUM_GAMES_WON.defaultValue()) + 1;
            preferences.putInteger(Prefs.NUM_GAMES_WON.key(), tmp);
        }

        preferences.flush();
    }

    public void update() {
        board.update();
        topBar.update();

        if (board.isGameOver() && !resetTimer.isRunning()) {
            resetTimer.start();
            board.setGameOver(false);

            Gdx.graphics.setContinuousRendering(true);
        }

        if (resetTimer.isRunning() && resetTimer.getTime() >= RESET_TIME) {
            resetTimer.stop();
            showEndGame = true;
            if (board.isVictory())
                endGame.setCurrentMessage(EndGame.VICTORY);
            else
                endGame.setCurrentMessage(EndGame.GAME_OVER);

            Gdx.graphics.setContinuousRendering(false);
            Gdx.graphics.requestRendering();
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer, BitmapFont font) {
        batch.begin();
        board.render(batch, font);
        topBar.render(batch, font, board.getNumBombsLeft());
        batch.end();

        if (showEndGame) {
            endGame.render(batch, shapeRenderer, font);
        }
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        init();

        board.resize(box, boy, bowidth, boheight, numFieldsX);
        topBar.resize(tbx, tby, tbwidth, tbheight);
        endGame.resize(egx, egy, egwidth, egheight);
    }

    public void moveTo(int x, int y) {
        board.moveTo(x, y);
        topBar.moveTo(x, y);
        endGame.moveTo(x, y);
    }

    public void touchDown(int screenX, int screenY) {
        if (!showEndGame && !resetTimer.isRunning()) {
            if (screenY >= 0 && screenY < topBar.getScreenY()) {
                board.touchDown(screenX, screenY);
            } else if (screenY >= topBar.getScreenY() && screenY < height) {
                topBar.touchDown(screenX, screenY);
            }
            Gdx.graphics.setContinuousRendering(true);
        }
        else if (showEndGame) {
            endGame.touchDown(screenX, screenY);
        }
    }

    public void touchUp(int screenX, int screenY) {
        if (!showEndGame && !resetTimer.isRunning()) {
            if (screenY >= 0 && screenY < topBar.getScreenY()) {
                board.touchUp(screenX, screenY);
            } else if (screenY >= topBar.getScreenY() && screenY < height) {
                topBar.touchUp(screenX, screenY);
            }
            Gdx.graphics.setContinuousRendering(false);
        }
        else if (showEndGame) {
            endGame.touchUp(screenX, screenY);
        }
    }

    public void touchDragged(int screenX, int screenY) {

    }

    public void lostFocus() {
        updatePrefs();

        board.init();

        showEndGame = false;
    }

    public void gainedFocus() {
        int fieldSize = preferences.getInteger(Prefs.FIELD_SIZE.key(), Prefs.FIELD_SIZE.defaultValue());
        int dif = preferences.getInteger(Prefs.DIFFICULTY.key(), Prefs.DIFFICULTY.defaultValue());
        int col = preferences.getInteger(Prefs.FIELD_COLOR.key(), Prefs.FIELD_COLOR.defaultValue());

        if (fieldSize != board.getFieldSize() || dif != board.getDifficulty() || col != board.getColor()) {
            numFieldsX = fieldSize;
            difficulty = dif;
            color = col;
            init();
            board = new Board(this, box, boy, bowidth, boheight, numFieldsX, difficulty, color);
            topBar = new TopBar(this, tbx, tby, tbwidth, tbheight);
        }
    }

    public void dispose() {
        board.dispose();
        endGame.dispose();
    }
}
