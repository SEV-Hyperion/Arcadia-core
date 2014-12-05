package com.hyperion.arcadia;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;

public class TextureManager implements Disposable, AssetErrorListener {

	// public static Texture PLAYER = new
	// Texture(Gdx.files.internal("player.png"));
	// public static Texture PLAYER = new Texture(
	// Gdx.files.internal("CharacterSinFondo.png"));
	// public static Texture MISSILE = new Texture(
	// Gdx.files.internal("missile.png"));
	// public static Texture ENEMY0 = new
	// Texture(Gdx.files.internal("enemy0.png"));
	// public static Texture ENEMY1 = new
	// Texture(Gdx.files.internal("enemy1.png"));
	// public static Texture ENEMY2 = new
	// Texture(Gdx.files.internal("enemy2.png"));
	// public static Texture ENEMY3 = new
	// Texture(Gdx.files.internal("enemy3.png"));

	// public static final Texture FIREBALL = null;// new
	// Texture(Gdx.files.internal("fireball.png"));

	// ==========================================================================
	// Texture atlas Management

	public AssetManager assetManager; // Texture Atlas Management

	public TextureAtlas atlas;

	// Singleton Class for Texture atlas Management
	// ==========================================================================
	public static final TextureManager instance = new TextureManager();

	private TextureManager() {

	}

	public void init(AssetManager assetManager) {
		// assetManager = new AssetManager();

		this.assetManager = assetManager;
		assetManager.setErrorListener(this);
		assetManager.load("images/arcadia_alpha.pack", TextureAtlas.class);

		assetManager.finishLoading();
		// System.out.println("Texture Atlas Correctly Loaded");
		atlas = assetManager.get("images/arcadia_alpha.pack");

		Texture.setAssetManager(assetManager);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("rawtypes")
	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {

	}

	// ==========================================================================

}
