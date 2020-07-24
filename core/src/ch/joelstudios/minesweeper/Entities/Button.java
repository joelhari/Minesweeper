package ch.joelstudios.minesweeper.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;

public class Button {
    private int x, y;
    private int relX, relY;
    private int width, height;

    private int textX, textY;

    private boolean pressed;

    private Texture texture;
    private Texture texturePressed;

    private String text;

    public Button(int x, int y, int width, int height, Texture texture, Texture texturePressed) {
        this.relX = x;
        this.relY = y;
        this.width = width;
        this.height = height;

        this.x = x;
        this.y = y;

        this.textX = x;
        this.textY = y + height - height/4;

        this.texture = texture;
        this.texturePressed = texturePressed;

        this.text = "";

        this.pressed = false;
    }

    public Button(int x, int y, int width, int height, Texture texture, Texture texturePressed, String text) {
        this.relX = x;
        this.relY = y;
        this.width = width;
        this.height = height;

        this.x = x;
        this.y = y;

        this.textX = x;
        this.textY = y + height - height/4;

        this.texture = texture;
        this.texturePressed = texturePressed;

        this.text = text;

        this.pressed = false;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
        if (pressed)
            batch.draw(texturePressed, x, y, width, height);
    }

    public void drawFont(SpriteBatch batch, BitmapFont font) {
        font.draw(batch, text, textX, textY, width, Align.center, false);
    }

    public void moveTo(int x, int y) {
        this.x = x + relX;
        this.y = y + relY;

        this.textX = this.x;
        this.textY = this.y + height - height/4;
    }

    public boolean onButton(int pointX, int pointY) {
        if (pointX >= x && pointX < x + width &&
            pointY >= y && pointY < y + height)
            return true;
        else
            return false;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public void dispose() {
        texture.dispose();
        texturePressed.dispose();
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
