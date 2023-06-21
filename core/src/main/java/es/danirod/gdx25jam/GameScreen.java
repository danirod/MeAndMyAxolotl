package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.RepeatingSolid;
import es.danirod.gdx25jam.actor.Turtle;
import es.danirod.gdx25jam.spawner.AlgaSpawner;
import es.danirod.gdx25jam.spawner.BubbleSpawner;
import es.danirod.gdx25jam.spawner.EggSpawner;
import es.danirod.gdx25jam.spawner.TrashSpawner;

public class GameScreen implements Screen {
	
	public static GameScreen INSTANCE;
	
	private Stage stage;
	
	Group bubbles = new Group();
	
	private int score;
	
	private Label scoreLabel;
		
	public int getScore() {
		return score;
	}
	
	public void pickEgg() {
		Gdx.app.log("GameScreen", "Yay, you picked an egg");
		score++;
		scoreLabel.setText("SCORE: " + score);
	}
	
	@Override
	public void show() {
		score = 0;
		INSTANCE = this;
		
		stage = new Stage(new ScreenViewport());
		stage.addListener(new DebugInputListener());	
		Gdx.input.setInputProcessor(stage);
		
		var water = new Image(JamGame.assets.get("water.png", Texture.class));
		water.setY(40);
		water.setHeight(Gdx.graphics.getHeight() - 120);
		stage.addActor(water);
		
		var floor = new RepeatingSolid(JamGame.assets.get("floor.png"));
		floor.setY(0);
		stage.addActor(floor);
		
		stage.addActor(bubbles);
		
		var sky = new RepeatingSolid(JamGame.assets.get("sky.png"));
		sky.setAlign(Align.bottom);
		sky.setY(Gdx.graphics.getHeight() - 80);
		stage.addActor(sky);
		
		var algasBack = new Group();
		stage.addActor(algasBack);
		
		var xo = new Axolotl();
		stage.addActor(xo);
		xo.setPosition(20, 20);
		
		var eggsGroup = new Group();
		stage.addActor(eggsGroup);
		
		var algasFront = new Group();
		stage.addActor(algasFront);
		
		var trashGroup = new Group();
		stage.addActor(trashGroup);
		var trashSpawner = new TrashSpawner(trashGroup, xo, this);
		stage.addActor(trashSpawner);
		
		var turtle = new Turtle();
		turtle.setPosition(400, 300);
		stage.addActor(turtle);
		
		BubbleSpawner spawner = new BubbleSpawner(bubbles, xo);
		stage.addActor(spawner);
		AlgaSpawner algaspawn = new AlgaSpawner(algasBack, algasFront);
		stage.addActor(algaspawn);
		EggSpawner eggSpawn = new EggSpawner(eggsGroup, xo);
		stage.addActor(eggSpawn);
		
		scoreLabel = new Label("SCORE: 0", JamGame.labelStyle);
		scoreLabel.setColor(Color.BLACK);
		scoreLabel.setPosition(10, Gdx.graphics.getHeight() - 50);
		stage.addActor(scoreLabel);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		stage.draw();
		stage.act();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		stage.dispose();
	}

	class DebugInputListener extends InputListener {
		
		boolean debug;
		
		@Override
		public boolean keyDown(InputEvent event, int keycode) {
			if (keycode == Input.Keys.F3) {
				debug = !debug;
				stage.setDebugAll(debug);
				return true;
			}
			return false;
		}
		
	}
}
