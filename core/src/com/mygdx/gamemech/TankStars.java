package com.mygdx.gamemech;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class TankStars extends Game {
	public static final int vWidth = 800;
	public static final int vHeight = 480;
	public AssetManager assets;
	public SpriteBatch batch;
	public BitmapFont font;
	public OrthographicCamera camera;

	@Override
	public void create () {
		assets = new AssetManager();
		batch = new SpriteBatch();
		font = new BitmapFont();
		camera = new OrthographicCamera();
		initFont();
		camera.setToOrtho(false,800,480);
		setScreen(new TitleScreen(this));
	}

	@Override
	public void render(){
		super.render();
	}

	public void initFont(){
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}
}