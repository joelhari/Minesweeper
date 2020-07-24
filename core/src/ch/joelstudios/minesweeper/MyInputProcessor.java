package ch.joelstudios.minesweeper;

import com.badlogic.gdx.InputProcessor;

public class MyInputProcessor implements InputProcessor {
    private Main main;
    private int currentPointer;
    private boolean touching;

    public MyInputProcessor(Main main) {
        assert (main != null);
        this.main = main;
        currentPointer = -1;
        touching = false;
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (!touching) {
            touching = true;
            currentPointer = pointer;
            screenY = main.getHeight() - screenY;
            main.touchDown(screenX, screenY, pointer, button);
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (touching && currentPointer == pointer) {
            screenY = main.getHeight() - screenY;
            main.touchUp(screenX, screenY, pointer, button);
            touching = false;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (touching && currentPointer == pointer) {
            screenY = main.getHeight() - screenY;
            main.touchDragged(screenX, screenY, pointer);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
