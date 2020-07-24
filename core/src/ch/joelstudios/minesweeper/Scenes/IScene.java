package ch.joelstudios.minesweeper.Scenes;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public interface IScene {
    public void update();
    public void render(SpriteBatch batch, ShapeRenderer renderer, BitmapFont font);
    public void resize(int width, int height);
    public void moveTo(int x, int y);
    public void touchDown(int screenX, int screenY);
    public void touchUp(int screenX, int screenY);
    public void touchDragged(int screenX, int screenY);
    public void lostFocus();
    public void gainedFocus();
    public void dispose();
}
