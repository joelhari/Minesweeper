package ch.joelstudios.minesweeper.Entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import ch.joelstudios.minesweeper.Scenes.Game;

public class TopBar {
    private int screenX, screenY, width, height;
    private int relX, relY;

    private String text;
    private int textX, textY;

    private boolean touchDown;

    private Game game;

    public TopBar(Game game, int screenX, int screenY, int width, int height) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = width;
        this.height = height;

        relX = screenX;
        relY = screenY;

        this.game = game;

        text = "Bombs left: ";

        init();
    }

    private void init() {
        textX = screenX + width/10;
        textY = screenY + 9*height/10;
    }

    public void update() {

    }

    public void render(SpriteBatch batch, BitmapFont font, int numBombsLeft) {
        font.draw(batch, text + numBombsLeft, textX, textY);
    }

    public void touchDown(int screenX, int screenY) {
        touchDown = true;
    }

    public void touchUp(int screenX, int screenY) {
        touchDown = false;
    }

    public void resize(int screenX, int screenY, int width, int height) {
        this.screenX = screenX;
        this.screenY = screenY;
        this.width = width;
        this.height = height;

        relX = screenX;
        relY = screenY;

        init();
    }

    public void moveTo(int x, int y) {
        screenX = x + relX;
        screenY = y + relY;

        init();
    }

    public int getScreenY() {
        return screenY;
    }
}
