package ch.joelstudios.minesweeper.Entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Field {
    private int x, y;
    private int relX, relY;
    private int size;
    private int value;
    private int spacing;
    private int width, height;

    private int numX, numY;

    private int numSprite;

    private boolean isBomb;
    private boolean isFlag;
    private boolean isCovered;
    private boolean isPressed;

    private Field[] neighbors;

    public Field(int x, int y, int size, int spacing) {
        this.x = x + spacing;
        this.y = y + spacing;
        this.size = size;
        this.spacing = spacing;

        relX = this.x;
        relY = this.y;

        this.width = size - 2*spacing;
        this.height = size - 2*spacing;

        this.numX = x + 3*size/9;
        this.numY = y + 3*size/4;

        isBomb = false;
        isFlag = false;
        isCovered = true;
        isPressed = false;
        setNumSprite();
    }

    public void render(SpriteBatch batch, BitmapFont font, Sprite[] sprites, Sprite[] numbres) {
        batch.draw(sprites[numSprite], x, y, width, height);
        if (!isCovered && !isBomb && value != 0) {
            batch.draw(numbres[value - 1], x, y, width, height);
        }
    }

    public void moveTo(int x, int y) {
        this.x = x + relX;
        this.y = y + relY;

        this.numX = this.x - spacing + 3*size/9;
        this.numY = this.y - spacing + 3*size/4;
    }

    public void uncover() {
        if (!isFlag) {
            setCovered(false);
            if (value == 0 && !isBomb) {
                for (Field field : neighbors) {
                    if (field.isCovered() && !field.isFlag()) {
                        field.uncover();
                    }
                }
            }
        }
    }

    private void setNumSprite() {
        if (isCovered) {
            if (isFlag) {
                numSprite = isPressed ? 5 : 3;
            } else {
                numSprite = isPressed ? 1 : 0;
            }
        } else {
            numSprite = isBomb ? 4 : 2;
        }
    }

    public void calculateValue() {
        if (neighbors == null)
            return;

        for (int i = 0; i < neighbors.length; i++) {
            if (neighbors[i].isBomb()) {
                value++;
            }
        }
    }

    public boolean uncoverNeighbours() {
        for (Field field : neighbors) {
            if (field.isBomb() && !field.isFlag())
                return false;
            field.uncover();
        }
        return true;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public boolean isFlag() {
        return isFlag;
    }

    public boolean isCovered() {
        return isCovered;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setBomb(boolean isBomb) {
        this.isBomb = isBomb;
        setNumSprite();
    }

    public void setFlag(boolean isFlag) {
        this.isFlag = isFlag;
        setNumSprite();
    }

    public void setCovered(boolean isCovered) {
        this.isCovered = isCovered;
        setNumSprite();
    }

    public void setPressed(boolean isPressed) {
        this.isPressed = isPressed;
        setNumSprite();
    }

    public void setNeighbors(Field[] neighbors) {
        this.neighbors = neighbors;
    }

    public int getValue() {
        return value;
    }

    public int getNumNeighboringFlags() {
        int numFlags = 0;
        for (Field field : neighbors) {
            if (field.isFlag()) {
                numFlags++;
            }
        }
        return numFlags;
    }
}
