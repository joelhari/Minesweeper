package ch.joelstudios.minesweeper;

public enum Prefs {
    NUM_GAMES_PLAYED    ("numGamesPlayed", 0),
    NUM_GAMES_WON       ("numGamesWon", 0),
    DIFFICULTY          ("difficulty", 7),
    FIELD_SIZE          ("fieldSize", 10),
    FIELD_COLOR         ("fieldColor", 0),
    PLAY_TIME           ("playTime", 0);

    private final String key;
    private final int defaultValue;

    Prefs(String key, int defaultValue) {
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public String key() {
        return key;
    }

    public int defaultValue() {
        return defaultValue;
    }
}
