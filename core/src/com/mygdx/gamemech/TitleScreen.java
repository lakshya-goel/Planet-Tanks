package com.mygdx.gamemech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class TitleScreen extends ScreenAdapter {

    private TankStars game;
    private Stage stage;
    private Image bgImg;
    private Texture bgTex;
    private ShapeRenderer shapeRenderer;
    private Skin skin;

    public TitleScreen(TankStars game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(TankStars.vWidth,TankStars.vHeight,game.camera));
        this.shapeRenderer=new ShapeRenderer();
        this.skin = new Skin(Gdx.files.internal("skin/comic-ui/comic-ui.json"));
    }

    private void queueAssets(){
        game.assets.load("img/preview.png",Texture.class);
    }

    @Override
    public void show(){
        queueAssets();
        bgTex = new Texture(Gdx.files.internal("img/preview.png"));
        bgImg = new Image(bgTex);
        bgImg.setSize(stage.getWidth(), stage.getHeight());
        bgImg.setPosition((stage.getWidth()/2)- 400,(stage.getHeight()/2)-240);


        Label label = new Label("PLANET TANKS",skin,"title");
        label.setFontScale(1.2f);
        label.setSize(300,100);
        float ht = label.getHeight();
        float wd = label.getWidth();
        label.setPosition((stage.getWidth()-wd)/2,(stage.getHeight()-ht)-50);

        stage.addActor(bgImg);
        stage.addActor(label);


        initButtons();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.draw();

        update(delta);
    }

    public void update(float delta){
        stage.act(delta);
    }

    private void initButtons(){
        //play-button
        TextButton butPlay = createButton("Play",240);
        butPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectTankScreen(game));
            }
        });
        stage.addActor(butPlay);

        //load-game-button
        TextButton butLoad = createButton("Load game",150);
        butLoad.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new LoadGameScreen(game));
            }
        });
        stage.addActor(butLoad);

        //exit-button
        TextButton butExit = createButton("Exit",60);
        butExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ExitMenuScreen(game));
            }
        });

        stage.addActor(butExit);
    }

    private TextButton createButton(String text, int y){                                  //implements Factory design pattern
        TextButton butExit = new TextButton(text,skin,"default");
        butExit.setSize(200,60);
        butExit.setPosition(300,y);
        butExit.getLabel().setFontScale(2);
        return butExit;
    }

    @Override
    public void dispose() {
        stage.dispose();
        shapeRenderer.dispose();
    }
}