package com.mygdx.gamemech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

public class LoadGameScreen extends ScreenAdapter {
    private TankStars game;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shape;
    private HashMap<String,Button> buttons;

    public LoadGameScreen(TankStars game){
        this.game = game;
        this.stage = new Stage(new FitViewport(TankStars.vWidth,TankStars.vHeight,game.camera));
        this.shape=new ShapeRenderer();
        this.buttons =new HashMap<>();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("skin/soldier/star-soldier-ui.json"));

        Image bg1 = new Image(new Texture(Gdx.files.internal("img/preview.png")));
        bg1.setHeight(stage.getHeight());
        bg1.setWidth(stage.getWidth());
        bg1.setPosition(0,0);

        Label title = new Label("SAVED GAMES",skin,"title");
        title.setOrigin(0,0);
        title.setPosition((400-(title.getWidth()/2)),240-(title.getHeight()/2)+150);
        title.setSize(400,120);

        Table t1 = genTable("HP_1: 80%","HP_2: 50%","Date: 12/10/22","Time: 15:34",40,0,350,200);
        Table t2 = genTable("HP_1: 60%","HP_2: 90%","Date: 10/10/22","Time: 11:09",280,0,350,200);
        Table t3 = genTable("HP_1: 73%","HP_2: 10%","Date: 05/10/22","Time: 08:55",520,0,350,200);

        stage.addActor(bg1);
        stage.addActor(t1);
        stage.addActor(t2);
        stage.addActor(t3);
        stage.addActor(title);
        initButtons();
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

    @Override
    public void dispose() {
        shape.dispose();
        stage.dispose();
    }

    private Table genTable(String hp1,String hp2,String date,String time,int x,int y,int ht,int wd){
        Table table = new Table();
        Image img = new Image(new Texture(Gdx.files.internal("img/gameplay.jpg")));
        img.setSize(200,150);

        Label lbl1 = genLabel(hp1,100,100,100,50);
        Label lbl2 = genLabel(hp2,100,200,100,50);
        Label lbl3 = genLabel(date,100,300,100,50);
        Label lbl4 = genLabel(time,100,400,100,50);
        table.row();
        table.add(img);
        table.row();
        table.add(lbl1);
        table.row();
        table.add(lbl2);
        table.row();
        table.add(lbl3);
        table.row();
        table.add(lbl4);
        table.setOrigin(0,0);
        table.setPosition(x,y);
        table.setSize(wd,ht);
        return table;
    }

    private Label genLabel(String text,int x,int y,int wd,int ht){
        Label lbl = new Label(text,skin);
        lbl.setOrigin(0,0);
        lbl.setFillParent(true);
        lbl.setPosition(x,y);
        lbl.setSize(wd,ht);
        return lbl;
    }
    public TextButton genTB(String name, String style, int x, int y, int ht, int wd) {
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

    public ImageButton genIB(String name, String style, int x, int y, int ht, int wd) {
        ImageButton bt = new ImageButton(skin, style);
        bt.getImage().setFillParent(true);
        bt.setName(name);
        bt.setOrigin(0, 0);
        bt.setSize(wd, ht);
        bt.setPosition(x, y);
        this.buttons.put(name, bt);
        return bt;
    }
    private void initButtons(){
        TextButton ret = genTB("return","default",300,0,80,200);
        ImageButton right = genIB("right","right",540,140,110,100);
        ret.setColor(Color.GOLD);
        ret.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TitleScreen(game));
            }
        });

        stage.addActor(ret);
        stage.addActor(right);
    }

}
