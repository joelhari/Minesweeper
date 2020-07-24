package ch.joelstudios.minesweeper.Scenes;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ch.joelstudios.minesweeper.Entities.Button;
import ch.joelstudios.minesweeper.Main;
import ch.joelstudios.minesweeper.Prefs;

public class StatsScene implements IScene {
    private Main main;
    private Preferences preferences;

    private int x, y;
    private int width;
    private int height;

    private int gamesPlayed;
    private int gamesWon;
    private int playTimeMinutes1;
    private int playTimeMinutes2;
    private int playTimeHours;

    private int textX;
    private int textY;

    private int dY;

    // 0 : back Button
    private Button[] buttons;

    private int touchButton;


    public StatsScene(Main main, Preferences preferences, int width, int height) {
        this.main = main;
        this.preferences = preferences;
        this.width = width;
        this.height = height;

        x = 0;
        y = 0;

        gamesPlayed = preferences.getInteger(Prefs.NUM_GAMES_PLAYED.key(), Prefs.NUM_GAMES_PLAYED.defaultValue());
        gamesWon = preferences.getInteger(Prefs.NUM_GAMES_WON.key(), Prefs.NUM_GAMES_WON.defaultValue());
        long playTime = preferences.getLong(Prefs.PLAY_TIME.key(), Prefs.PLAY_TIME.defaultValue());
        int playTimeMinutes = (int)((playTime/1000l/60)%60);
        playTimeMinutes1 = playTimeMinutes/10;
        playTimeMinutes2 = playTimeMinutes%10;
        playTimeHours = (int)(playTime/1000l/60l/60l);

        init();
        init2();
    }

    private void init() {
        textX = x + width/8;
        textY = y + height - width/8;

        dY = width/8;
    }

    private void init2() {
        buttons = new Button[] {
                new Button(width - width/16 - width/6, width/16, width/6, width/6, new Texture("back_button2.png"), new Texture("back_button_pressed.png"))
        };

        touchButton = -1;
    }

    public void update() {

    }

    public void render(SpriteBatch batch, ShapeRenderer renderer, BitmapFont font) {
        batch.begin();
        font.draw(batch, "Games Played:  " + gamesPlayed, textX, textY);
        font.draw(batch, "Games Won:  " + gamesWon, textX, textY - dY);
        font.draw(batch, "Total Play Time:  " + playTimeHours + ":" + playTimeMinutes1 + "" + playTimeMinutes2, textX, textY - 2*dY);

        for (Button button : buttons) {
            button.draw(batch);
        }
        batch.end();
    }

    public void resize(int width, int height) {
        this.width = width;
        this.height = height;

        init();
        init2();
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;

        init();

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
                main.changeScene(SceneManager.MAINMENU_SCENE);
                break;
        }
    }

    public void lostFocus() {

    }

    public void gainedFocus() {
        gamesPlayed = preferences.getInteger(Prefs.NUM_GAMES_PLAYED.key(), Prefs.NUM_GAMES_PLAYED.defaultValue());
        gamesWon = preferences.getInteger(Prefs.NUM_GAMES_WON.key(), Prefs.NUM_GAMES_WON.defaultValue());
        long playTime = preferences.getLong(Prefs.PLAY_TIME.key(), Prefs.PLAY_TIME.defaultValue());
        int playTimeMinutes = (int)((playTime/1000l/60)%60);
        playTimeMinutes1 = playTimeMinutes/10;
        playTimeMinutes2 = playTimeMinutes%10;
        playTimeHours = (int)(playTime/1000l/60l/60l);
    }

    public void dispose() {
        for (Button button : buttons)
            button.dispose();
    }
}
