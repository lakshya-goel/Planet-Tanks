package com.mygdx.gamemech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

public class SelectTankScreen extends ScreenAdapter {
    TankStars game;
    Stage stage;
    Skin skin;
    ShapeRenderer shape;
    HashMap<String, Button> buttons;

    public Button getInstance(String s){
        if(!this.buttons.containsKey(s)){
            this.buttons.put(s,new Button());
        }
        return this.buttons.get(s);
    }
    public SelectTankScreen(TankStars game) {
        this.game = game;
        this.stage = new Stage(new FitViewport(TankStars.vWidth, TankStars.vHeight, game.camera));
        this.shape = new ShapeRenderer();
        this.buttons = new HashMap<>();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("skin/soldier/star-soldier-ui.json"));

        Image bg1 = new Image(new Texture(Gdx.files.internal("img/preview.png")));
        bg1.setHeight(stage.getHeight());
        bg1.setWidth(stage.getWidth());
        bg1.setPosition(0, 0);

        Image tank = new Image(new Texture(Gdx.files.internal("img/tank1.png")));
        tank.setOrigin(0, 0);
        tank.setSize(390, 390);
        tank.setPosition(205, 50);


        Label title = new Label("PLAYER 1", skin, "title");
        title.setOrigin(0, 0);
        title.setPosition(165, 340);
        title.setSize(400, 120);

        Label tank_name = new Label("ABRAMS", skin, "default");
        tank_name.setOrigin(0, 0);
        tank_name.setFontScale(1.6f);
        tank_name.setPosition(310, 50);
        tank_name.setSize(200, 100);

        Table coins = new Table();
        Image coin_img = new Image(new Texture(Gdx.files.internal("img/coin_symbol.png")));
        Label coin_count = new Label("2250", skin);
        coins.add(coin_img, coin_count);
        coins.setOrigin(0, 0);
        coins.setPosition(690, 440);
        coins.setSize(100, 30);

        addActors(bg1,tank,title,tank_name,coins);
        initButtons();
    }

    public void addActors(Image bg1, Image tank, Label title, Label tank_name, Table coins){ //gives idea of general flow of program
        stage.addActor(bg1);
        stage.addActor(tank);
        stage.addActor(title);
        stage.addActor(tank_name);
        stage.addActor(coins);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, .25f, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
        update(delta);
    }

    public void update(float delta) {
        stage.act(delta);
    }

    @Override
    public void dispose() {
        shape.dispose();
        stage.dispose();
    }

    public TextButton genTB(String name, String style, int x, int y, int ht, int wd) {  //Factory
        TextButton bt = new TextButton(name, skin, style);
        bt.getLabel().setFillParent(false);
        bt.getLabel().setOrigin(0, 0);
        bt.setOrigin(0, 0);
        bt.setSize(wd, ht);
        bt.setPosition(x, y);
        bt.getLabel().setPosition(x, y);
        this.buttons.put(name, bt);
        return bt;
    }

    public ImageButton genIB(String name, String style, int x, int y, int ht, int wd) {     //Factory
        ImageButton bt = new ImageButton(skin, style);
        bt.getImage().setFillParent(true);
        bt.setName(name);
        bt.setOrigin(0, 0);
        bt.setSize(wd, ht);
        bt.setPosition(x, y);
        this.buttons.put(name, bt);
        return bt;
    }

    private void initButtons() {
        ImageButton back = genIB("back", "left", 0, 380, 60, 50);
        TextButton select = genTB("select", "default", 180, 0, 80, 200);
        TextButton upgrade = genTB("upgrade", "default", 420, 0, 80, 200);
        ImageButton right = genIB("right", "right", 540, 140, 110, 100);
        ImageButton left = genIB("left", "left", 50, 140, 110, 100);

        Button start = getInstance("start");


        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        select.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new SelectTankScreen2(game));
            }
        });
        stage.addActor(back);
        stage.addActor(select);
        stage.addActor(upgrade);
        stage.addActor(left);
        stage.addActor(right);
    }
}
