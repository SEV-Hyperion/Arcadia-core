package com.hyperion.arcadia.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.hyperion.arcadia.MainGame;
import com.hyperion.arcadia.TextureManager;
import com.hyperion.arcadia.camera.OrthoCamera;
import com.hyperion.arcadia.entity.EntityManager;

public class GameOverScreen extends Screen {
	private OrthoCamera camera;
	// private Texture texture;
	private TextureRegion texture;
	private boolean won;
	private String nextLevel;
	private TextButton textButtonNext;
	private TextButton textButtonReplay;
	private TextButton textButtonMenu;

	public GameOverScreen(boolean won, String nextLevel) {
		this.won = won;
		this.nextLevel=nextLevel;
		if (won) {
			texture = EntityManager.em.getAnimatedPlayer().getCurrentFrame();

		} else {
			texture = EntityManager.em.getAnimatedPlayer().getCurrentFrame();
			// TODO use texture from the killing enemy or the round boss or
			// anything?

		}
		// MainGame.score = 0;
	}

	@Override
	public void create() {
		camera = new OrthoCamera();
		camera.resize();
		
		/*
		 * TODO we need to add some buttons. NEXT, REPLAY, MAIN_MENU
		 */
		
		addUI();

	}

	private void addUI() {
		BitmapFont font = new BitmapFont();
		Skin skin = new Skin();
		TextureAtlas atlas = new TextureAtlas();
		atlas.addRegion("btNext",
				TextureManager.instance.atlas.findRegion("botonRayo"));
		atlas.addRegion("btReplay", TextureManager.instance.atlas.findRegion("botonRayo"));
		atlas.addRegion("btMenu", TextureManager.instance.atlas.findRegion("botonRayo"));
		skin.addRegions(atlas);
		
		
		addButtonNext(font, skin);
		
		addButtonReplay(font, skin);
		
		addButtonMenu(font, skin);

		Gdx.input.setInputProcessor(EntityManager.em);
		
	}

	private void addButtonNext(BitmapFont font, Skin skin) {
		TextButtonStyle textButtonNextStyle = new TextButtonStyle();
		textButtonNextStyle.font = font;
		textButtonNextStyle.up = skin.getDrawable("btNext");
		textButtonNextStyle.down = skin.getDrawable("btNext");
		textButtonNextStyle.checked = skin.getDrawable("btNext");
		textButtonNext = new TextButton("Next", textButtonNextStyle);
		textButtonNext.setSize(50, 100);
		textButtonNext.setCenterPosition(MainGame.VIEWPORT_GUI_HEIGHT / 2,
				(MainGame.VIEWPORT_GUI_WIDTH / 2)-50);

		textButtonNext.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				if (textButtonNext.isPressed() || textButtonNext.isChecked()) {
					System.out.println("boton pulsado");
					ScreenManager.setScreen(new AbstractLevel(nextLevel));
				}
				return true;
			}
		});
		EntityManager.em.addActor(textButtonNext);
	}

	private void addButtonReplay(BitmapFont font, Skin skin) {
		TextButtonStyle textButtonReplayStyle = new TextButtonStyle();
		textButtonReplayStyle.font = font;
		textButtonReplayStyle.up = skin.getDrawable("btReplay");
		textButtonReplayStyle.down = skin.getDrawable("btReplay");
		textButtonReplayStyle.checked = skin.getDrawable("btReplay");
		textButtonReplay = new TextButton("Replay", textButtonReplayStyle);
		textButtonReplay.setSize(50, 100);
		textButtonReplay.setCenterPosition((MainGame.VIEWPORT_GUI_HEIGHT / 2)-50,
				(MainGame.VIEWPORT_GUI_WIDTH / 2)-50);

		textButtonReplay.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				if (textButtonReplay.isPressed() || textButtonReplay.isChecked()) {
					System.out.println("boton pulsado");
					ScreenManager.setScreen(new AbstractLevel(nextLevel));// TODO find a way to get the current level
				}
				return true;
			}
		});
		EntityManager.em.addActor(textButtonReplay);
	}

	private void addButtonMenu(BitmapFont font, Skin skin) {
		TextButtonStyle textButtonMenuStyle = new TextButtonStyle();
		textButtonMenuStyle.font = font;
		textButtonMenuStyle.up = skin.getDrawable("btReplay");
		textButtonMenuStyle.down = skin.getDrawable("btReplay");
		textButtonMenuStyle.checked = skin.getDrawable("btReplay");
		textButtonMenu = new TextButton("Menu", textButtonMenuStyle);
		textButtonMenu.setSize(50, 100);
		textButtonMenu.setCenterPosition((MainGame.VIEWPORT_GUI_HEIGHT / 2)+50,
				(MainGame.VIEWPORT_GUI_WIDTH / 2)-50);

		textButtonMenu.addListener(new EventListener() {

			@Override
			public boolean handle(Event event) {
				// TODO Auto-generated method stub
				if (textButtonMenu.isPressed() || textButtonMenu.isChecked()) {
					System.out.println("boton pulsado");
					ScreenManager.setScreen(new MainMenuScreen());// TODO find a way to get the current level
				}
				return true;
			}
		});
		EntityManager.em.addActor(textButtonMenu);
	}

	@Override
	public void update() {

		/*
		 * TODO change level with some more logic. If dead, don't let go to NEXT, etc.
		 */
		camera.update();

		if (Gdx.input.isKeyPressed(Keys.R)) {
			ScreenManager.setScreen(new AbstractLevel("level_1_1"));
		}
		else if(Gdx.input.isKeyPressed(Keys.N)||  Gdx.input.justTouched()){
			if(nextLevel!="" && !nextLevel.isEmpty())
				ScreenManager.setScreen(new AbstractLevel(nextLevel));
			else{
				// Back to main menu.
				ScreenManager.setScreen(new MainMenuScreen());
			}
		}
	}

	@Override
	public void render(SpriteBatch sb) {

		if (won) {
			Gdx.gl.glClearColor(0, 1, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		} else {
			Gdx.gl.glClearColor(0, 0, 0, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		}

		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		sb.draw(texture, MainGame.WIDTH / 2, MainGame.HEIGHT / 2);
		textButtonNext.draw(sb, 1);
		textButtonReplay.draw(sb, 1);
		textButtonMenu.draw(sb, 1);
		sb.end();
	}

	@Override
	public void resize(int width, int height) {

		camera.resize();
	}

	@Override
	public void dispose() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

}
