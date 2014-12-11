package com.hyperion.arcadia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.TextureManager;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.EntityManager;

public class MainMenuScreen extends Screen {
	private OrthoCamera camera;
	private OrthographicCamera cameraGUI;
	public Sound gameLoopSound;
	private Sprite sprite;
	private EntityManager entityManager;
	private Sprite mapSprite;
//	Button bt;
	private TextButton textButton;

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

		gameLoopSound = Gdx.audio.newSound(Gdx.files
				.internal("sounds/gameloopsound.mp3"));
		gameLoopSound.loop();

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

	private void addUI() {
		// TODO crear botones
		// TODO reeditar parametros
//		bt = new Button();
//		bt.setColor(Color.OLIVE);
//		Skin btSkin = new Skin();
//		String up = "up";
//		String down = "down";
//		btSkin.add(up, TextureManager.instance.atlas.findRegion("badlogic")
//				.getTexture());
//		btSkin.add(down, TextureManager.instance.atlas.findRegion("badlogic")
//				.getTexture());
//		ButtonStyle bs = new ButtonStyle(btSkin.getDrawable(up),
//				btSkin.getDrawable(down), btSkin.getDrawable(up));
//		bt.setPosition(250, 50);
//		// bt.setSkin(btSkin);
//		bt.setStyle(bs);
//		bt.setSize(150f, 150f);
//
//		bt.addListener(new EventListener() {
//
//			@Override
//			public boolean handle(Event event) {
//				// TODO Auto-generated method stub
//				if (bt.isPressed() || bt.isChecked()) {
//					System.out.println("boton pulsado");
//					ScreenManager.setScreen(new AbstractLevel("level_1_1"));
//				}
//				return true;
//			}
//		});
//
//		entityManager.addActor(bt);
//
//		Gdx.input.setInputProcessor(EntityManager.em);

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
//		bt.draw(sb, 1);
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
		gameLoopSound.dispose();
		this.camera = null;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		gameLoopSound.stop();
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		gameLoopSound.loop();
	}

}
