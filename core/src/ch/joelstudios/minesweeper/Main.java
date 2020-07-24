package ch.joelstudios.minesweeper;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import ch.joelstudios.minesweeper.Scenes.SceneManager;

public class Main extends ApplicationAdapter {
	private int width, height;

	private OrthographicCamera camera;
	private SpriteBatch batch;
	private BitmapFont font;
	private ShapeRenderer shapeRenderer;

	private SceneManager sceneManager;

	private MyInputProcessor inputProcessor;

	private Preferences preferences;

	private Timer timer;

	public void create () {
		Gdx.graphics.setResizable(false);

		this.width = Gdx.graphics.getWidth();
		this.height = Gdx.graphics.getHeight();

		preferences = Gdx.app.getPreferences("prefs");

		camera = new OrthographicCamera();
		batch = new SpriteBatch();

		shapeRenderer = new ShapeRenderer();

		sceneManager = new SceneManager(this, preferences, width, height);

		inputProcessor = new MyInputProcessor(this);
		Gdx.input.setInputProcessor(inputProcessor);

		init();

		timer = new Timer();
		timer.start();
	}

	private void init() {
		camera.setToOrtho(false, width, height);
		batch.setProjectionMatrix(camera.combined);
		shapeRenderer.setProjectionMatrix(camera.combined);

		createFont(width/18);
	}

	public void changeScene(int scene) {
		sceneManager.changeScene(scene);
	}

	public void touchDown(int screenX, int screenY, int pointer, int button) {
		sceneManager.touchDown(screenX, screenY);
	}

	public void touchUp(int screenX, int screenY, int pointer, int button) {
		sceneManager.touchUp(screenX, screenY);
	}

	public void touchDragged(int screenX, int screenY, int pointer) {
		sceneManager.touchDragged(screenX, screenY);
	}

	public void render () {
		sceneManager.update();

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		sceneManager.render(batch, shapeRenderer, font);
	}

	public void dispose () {
		batch.dispose();
		shapeRenderer.dispose();
		font.dispose();
	}

	private void createFont(int size) {
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("OpenSans-Bold.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = (size);
		parameter.borderColor = Color.BLACK;
		parameter.color = Color.BLACK;
		font = generator.generateFont(parameter);
		generator.dispose();
	}

	public void resize(int width, int height) {
		if (width != this.width || height != this.height) {
			this.width = width;
			this.height = height;
			init();
			sceneManager.resize(width, height);
		}
	}

	public void pause() {
		long time = timer.getTime();
		long totalTime = preferences.getLong(Prefs.PLAY_TIME.key(), Prefs.PLAY_TIME.defaultValue()) + time;
		preferences.putLong(Prefs.PLAY_TIME.key(), totalTime);
		timer.stop();

		preferences.flush();
	}

	public void resume() {
		timer.start();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
}
