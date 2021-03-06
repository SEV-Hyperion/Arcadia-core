package com.hyperion.arcadia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.TextureManager;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.EntityManager;

public class MainMenuScreen extends Screen {
	private OrthoCamera camera;
	public Sound gameLoopSound;
	private EntityManager entityManager;
	private Sprite mapSprite;
	private TextButton textButton;
	private Music music;

	@Override
	public void create() {
		// TODO Auto-generated method stub

		Gdx.graphics.setTitle("Main Menu");

		camera = new OrthoCamera();
		camera.setPosition(0.0f, 0.0f);

		Viewport vp = new FitViewport(MainGame.VIEWPORT_GUI_HEIGHT,
				MainGame.VIEWPORT_GUI_WIDTH);
		entityManager = new EntityManager(vp, MainGame.getBatch());
		entityManager.setCamera(camera);
		addUI();
		music = Gdx.audio.newMusic(Gdx.files.internal("sounds/l2outro.ogg"));
		music.play();
//		gameLoopSound = Gdx.audio.newSound(Gdx.files
//				.internal("sounds/gameloopsound.mp3"));
//		gameLoopSound.loop();

		TextureRegion region = TextureManager.instance.atlas.findRegion("map");
		mapSprite = new Sprite(region);
		mapSprite.setSize(MainGame.VIEWPORT_GUI_HEIGHT,
				MainGame.VIEWPORT_GUI_WIDTH);
		if (MainGame.isAndroid) {
			System.out.println(Gdx.graphics.getDesktopDisplayMode());
		} else {
			// mapSprite.rotate90(false);
		}
		mapSprite.setPosition(0, 0);
		mapSprite.setOrigin(0, 0);

		camera.updateViewport();

	}

	/**
	 * Creates and adds the UI elements for the Main Screen. Curretnly only a
	 * Button.
	 */
	private void addUI() {
		// TODO crear botones
		// TODO reeditar parametros
		BitmapFont font = new BitmapFont();
		Skin skin = new Skin();
		TextureAtlas atlas = new TextureAtlas();
		atlas.addRegion("bt",
				TextureManager.instance.atlas.findRegion("botonRayo"));
		skin.addRegions(atlas);
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.font = font;
		textButtonStyle.up = skin.getDrawable("bt");
		textButtonStyle.down = skin.getDrawable("bt");
		textButtonStyle.checked = skin.getDrawable("bt");

		textButton = new TextButton("PLAY", textButtonStyle);
		textButton.setSize(400, 200);
		textButton.setCenterPosition(MainGame.VIEWPORT_GUI_HEIGHT / 2,
				MainGame.VIEWPORT_GUI_WIDTH / 2);

		textButton.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				if (textButton.isPressed() || textButton.isChecked()) {
					System.out.println("boton pulsado");
					ScreenManager.setScreen(new AbstractLevel("level_1_1"));
				}
				return true;
			}
		});
		entityManager.addActor(textButton);

		Gdx.input.setInputProcessor(EntityManager.em);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		camera.update();
		entityManager.act();
	}

	@Override
	public void render(SpriteBatch sb) {

		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		sb.setProjectionMatrix(camera.combined);
		sb.enableBlending(); // Enable blending in the game screen
		sb.begin();
		mapSprite.draw(sb);
		// bt.draw(sb, 1);
		textButton.draw(sb, 1);
		entityManager.render(sb);
		sb.end();

	}

	@Override
	public void resize(int width, int height) {
		camera.resize();

		camera.updateViewport();
		camera.update();
	}

	@Override
	public void dispose() {
//		gameLoopSound.dispose();
		music.dispose();
		this.camera = null;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
//		gameLoopSound.stop();
		music.pause();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
//		gameLoopSound.loop();
		music.play();
	}

}
