package com.mygdx.gamemech;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.gamemech.utils.TiledObjectUtil;

import java.util.HashMap;

import static com.mygdx.gamemech.utils.Constants.ppm;

public class GameMech extends ApplicationAdapter implements Screen, com.badlogic.gdx.Screen {
	private boolean debug = false;
	public float dat = 51;
	private TankStars game;
	private Stage stage;
	private Skin skin;
	private ShapeRenderer shape;
	private HashMap<String, Button> buttons;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer tmr;
	private TiledMap map;
	private World world;
	private Body player,platform,ground;
	private Box2DDebugRenderer b2dr;
	private SpriteBatch batch;
	private Texture tex;
	private Texture bgImg;

	public GameMech(TankStars game){
		this.game = game;
		this.stage = new Stage(new FitViewport(TankStars.vWidth,TankStars.vHeight,game.camera));
		this.shape=new ShapeRenderer();
		this.buttons =new HashMap<>();
	}
	
	@Override
	public void show() {
//		float w = Gdx.graphics.getWidth();
//		float h = Gdx.graphics.getHeight();
//
//		camera = new OrthographicCamera();
//		camera.setToOrtho(false,w,h);

		this.camera = game.camera;

		world = new World(new Vector2(0,0f),false);
		b2dr = new Box2DDebugRenderer();
		player = createBody(10,10,32,32,false);
		platform = createBody(0,0,64,32,true);

		batch = new SpriteBatch();
		bgImg = new Texture(Gdx.files.internal("background_1.png"));
		map = new TmxMapLoader().load("Maps/terr.tmx");
		tmr = new OrthogonalTiledMapRenderer(map);
		createGround();
		TiledObjectUtil.parseTiledObjectLayer(world,map.getLayers().get("layer").getObjects());

		Gdx.input.setInputProcessor(stage);
		this.skin = new Skin(Gdx.files.internal("skin/soldier/star-soldier-ui.json"));

		Image bg1 = new Image(new Texture(Gdx.files.internal("img/gameplay.jpg")));
		bg1.setHeight(stage.getHeight());
		bg1.setWidth(stage.getWidth());
		bg1.setPosition(0,0);

//		stage.addActor(bg1);
		initButtons();
	}

	public void createGround(){
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.StaticBody;
		bodyDef.position.set(0,0);

		ChainShape groundShape = new ChainShape();
		groundShape.createChain(new float[]{0,900,0,200,1600,200,1600,900});
		FixtureDef groundF = new FixtureDef();

		groundF.shape=groundShape;
		groundF.friction = 1f;
		groundF.restitution=0;

		ground=world.createBody(bodyDef);
		ground.createFixture(groundShape,1.0f);
	}

	public Body createBody(int x, int y,int width,int height,boolean isStatic){           //implements Factory design pattern
		Body pBody;
		BodyDef def = new BodyDef();

		if(!isStatic){
			def.type = BodyDef.BodyType.DynamicBody;
		}
		else{
			def.type = BodyDef.BodyType.StaticBody;
		}

		def.position.set(x/ppm,y/ppm);
		def.fixedRotation=true;
		pBody = world.createBody(def);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width/2/ppm,height/2/ppm);
		pBody.createFixture(shape,1.0f);
		shape.dispose();
		return pBody;
	}

//	@Override
//	public void show() {
//		Gdx.input.setInputProcessor(stage);
//		this.skin = new Skin(Gdx.files.internal("skin/soldier/star-soldier-ui.json"));
//
//		Image bg1 = new Image(new Texture(Gdx.files.internal("img/gameplay.jpg")));
//		bg1.setHeight(stage.getHeight());
//		bg1.setWidth(stage.getWidth());
//		bg1.setPosition(0,0);
//
//		stage.addActor(bg1);
//		initButtons();
//	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, .25f, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.draw();
		update(delta);
		render();
	}

	@Override
	public void render () {
		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0f,0f,0f,1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		b2dr.render(world,camera.combined.scl(ppm));

		batch.begin();
		batch.draw(bgImg,-Gdx.graphics.getWidth()/2,-Gdx.graphics.getHeight()/2,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		batch.end();
//		tmr.render();

		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
			Gdx.app.exit();
		}
	}

	public void update(float delta){
		world.step(1/60f,6,2);
		inputUpdate(delta);
		cameraUpdate(delta);
		batch.setProjectionMatrix(camera.combined);
		tmr.setView(camera);
		stage.act(delta);
	}

	public float moveLeft(float a){
		return a-=1;
	}

	public float moveRight(float a){
		return a+=1;
	}

	private void inputUpdate(float delta) {
		float horizontalForce = 0f ;
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
			horizontalForce = moveLeft(horizontalForce);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
			horizontalForce = moveRight(horizontalForce);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP)){
			player.applyForceToCenter(0,5,false);
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
			player.applyForceToCenter(0,-5,false);
		}
		player.setLinearVelocity(horizontalForce*5,player.getLinearVelocity().y);
	}


	public void cameraUpdate(float delta){
		Vector3 position  = camera.position;
		position.x = player.getPosition().x*ppm;
		position.y = player.getPosition().y*ppm;
		camera.position.set(position);

		camera.update();
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false,width/2,height/2);
	}

	@Override
	public void dispose () {
		b2dr.dispose();
		batch.dispose();
		tmr.dispose();
		tex.dispose();
		bgImg.dispose();
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
				game.setScreen(new ExitMenuScreen(game));
			}
		});
		stage.addActor(bt);
	}

	private Table genTable(String hp1, int x, int y, int ht, int wd){
		Table table = new Table();
		Label lbl1 = genLabel(hp1,100,100,450,50);
		table.row();
		table.add(lbl1);
		table.row();
		TextButton tb1 = genButton("yes","default",0,0,0,80,200);
		TextButton tb2 = genButton("no","default",0,0,0,80,200);
		table.add(tb1,tb2);
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
		Table table = genTable("Exit game?",100,100,200,450);
		table.setColor(Color.BLACK);
		stage.addActor(table);
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

}
