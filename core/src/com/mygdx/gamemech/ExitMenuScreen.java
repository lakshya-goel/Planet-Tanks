package com.mygdx.gamemech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.HashMap;

public class ExitMenuScreen extends ScreenAdapter {
    private TankStars game;
    private Stage stage;
    private Skin skin;
    private ShapeRenderer shape;
    private HashMap<String,Button> buttons;

    public ExitMenuScreen(TankStars game){
        this.game = game;
        this.stage = new Stage(new FitViewport(TankStars.vWidth,TankStars.vHeight,game.camera));
        this.shape=new ShapeRenderer();
        this.buttons =new HashMap<>();
    }


    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("skin/soldier/star-soldier-ui.json"));

        Texture bgTex = new Texture(Gdx.files.internal("img/preview.png"));
        Image bgImg = new Image(bgTex);
        bgImg.setSize(stage.getWidth(), stage.getHeight());
        bgImg.setPosition((stage.getWidth()/2)- 400,(stage.getHeight()/2)-240);

        stage.addActor(bgImg);
        main_menu();
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


    private void initButtons(){
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("img/hmburger.png"))));
        ImageButton bt = new ImageButton(drawable);
        bt.setSkin(skin);
        bt.getImage().setFillParent(true);
        bt.setOrigin(0,0);
        bt.setSize(50,70);
        bt.setPosition(0,400);

        bt.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                main_menu();
            }
        });
        stage.addActor(bt);
    }

    private Table genTable(String hp1,int x,int y,int ht,int wd){
        Table table = new Table();
        Label lbl1 = genLabel(hp1,x+300,y+100,450,50);
        table.row();
        table.add(lbl1);
        table.row();
        TextButton tb1 = genButton("yes","default",0,x,y,80,200);
        assert tb1 != null;
        tb1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(1);
            }
        });
        TextButton tb2 = genButton("no","default",0,x+220,y,80,200);
        assert tb2 != null;
        tb2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameMech(game));
            }
        });
        table.add(tb1,tb2);
        table.setOrigin(0,0);
        table.setPosition(x,y);
        table.setSize(wd,ht);
        return table;
    }

    private Label genLabel(String text,int x,int y,int wd,int ht){
        Label lbl = new Label(text,skin);
        lbl.setOrigin(0,0);
        lbl.setPosition(x,y);
        lbl.setSize(wd,ht);
        return lbl;
    }
    private TextButton genButton(String name, String style,int type,int x,int y,int ht,int wd){
        if(type==0){
            //text-button
            TextButton bt = new TextButton(name,skin,style);
            bt.getLabel().setFillParent(false);
            bt.getLabel().setOrigin(0,0);
            bt.setOrigin(0,0);
            bt.setSize(wd,ht);
            bt.setPosition(x,y);
            bt.getLabel().setPosition(x,y);
            return bt;
        }
        return null;
    }

    private void main_menu(){
        Table table = genTable("Exit game?",175,140,200,450);
        stage.addActor(table);
    }
}
