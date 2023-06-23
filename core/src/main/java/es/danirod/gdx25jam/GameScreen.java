package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import es.danirod.gdx25jam.actions.CommonActions;
import es.danirod.gdx25jam.actor.Axolotl;
import es.danirod.gdx25jam.actor.Egg;
import es.danirod.gdx25jam.actor.PendingEggs;
import es.danirod.gdx25jam.actor.RepeatingSolid;
import es.danirod.gdx25jam.actor.Turtle;
import es.danirod.gdx25jam.spawner.BubbleSpawner;
import es.danirod.gdx25jam.spawner.CoralSpawner;
import es.danirod.gdx25jam.spawner.EggSpawner;
import es.danirod.gdx25jam.spawner.TrashSpawner;
import es.danirod.gdx25jam.spawner.UbootSpawner;

public class GameScreen implements Screen {
	
	public static GameScreen INSTANCE;
	
	private Stage stage;
	
	Group bubbles = new Group();
	
	private int score;
	
	PendingEggs eggsHUD;
	
	public int getScore() {
		return score;
	}
	
	public void pickEgg(Egg egg) {
		egg.clearActions();
		egg.addAction(
				Actions.sequence(
						Actions.scaleTo(1.1f, 1.1f),
						Actions.moveTo(eggsHUD.getX(), eggsHUD.getY(), 0.25f),
						Actions.run(() -> {
							score++;
							updateEggsCounter();
							if (score == 15) {
								triggerFinalSequence();
							}
						}),
						Actions.removeActor()
				)
		);
	}
	
	void triggerFinalSequence() {
		Action forward = Actions.parallel(
			Actions.moveBy(Gdx.graphics.getWidth() / 3, 0, 1f, Interpolation.sineOut),
			CommonActions.verticalWave(10f, 1f)
		);
		Action moveBack = Actions.scaleTo(-1f, 1f);
		Action grow = Actions.scaleTo(-3f, 3f, 1f, Interpolation.sineIn);
		Action goBack = Actions.moveBy(-Gdx.graphics.getWidth(), 0, 1f, Interpolation.sineIn);
		Action wave = CommonActions.verticalWave(20f, 1f);
		Action growAndGoBack = Actions.parallel(grow, goBack, wave);
		Action animation = Actions.sequence(forward, moveBack, growAndGoBack);
		xo.addAction(animation);
	}
	
	void updateEggsCounter() {
		eggsHUD.update(15 - score);
	}
	
	private Axolotl xo;
	
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
		water.getColor().a = 0.5f;
		stage.addActor(water);
		
		var floor = new RepeatingSolid(JamGame.assets.get("floor.png"), 200);
		floor.setY(0);
		stage.addActor(floor);
		
		stage.addActor(bubbles);
		
		var sky = new RepeatingSolid(JamGame.assets.get("sky.png"), 200);
		sky.setAlign(Align.bottom);
		sky.setY(Gdx.graphics.getHeight() - 80);
		sky.getColor().a = 0.5f;
		stage.addActor(sky);
		
		var algasBack = new Group();
		stage.addActor(algasBack);
		
		// This group is used by far away entities that are swimming (turtles, boats...)
		var swimBack = new Group();
		stage.addActor(swimBack);
		
		xo = new Axolotl();
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
		
		turtle = new Turtle(xo);
		turtle.switchToCalm();
		stage.addActor(turtle);
		
		BubbleSpawner spawner = new BubbleSpawner(bubbles, xo, 86, 7);
		stage.addActor(spawner);
		CoralSpawner algaspawn = new CoralSpawner(algasBack, algasFront, floor.getHeight());
		stage.addActor(algaspawn);
		EggSpawner eggSpawn = new EggSpawner(eggsGroup, xo);
		stage.addActor(eggSpawn);
		UbootSpawner ubootSpawn = new UbootSpawner(swimBack);
		stage.addActor(ubootSpawn);
		
		eggsHUD = new PendingEggs();
		eggsHUD.setPosition(5, Gdx.graphics.getHeight() - eggsHUD.getHeight() - 5);
		stage.addActor(eggsHUD);
		updateEggsCounter();
	}
	
	Turtle turtle;

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
			if (keycode == Input.Keys.F1) {
				xo.setScale(1);
				xo.setPosition(20, 20);
			}
			if (keycode == Input.Keys.F2) {
				triggerFinalSequence();
			}
			if (keycode == Input.Keys.F3) {
				debug = !debug;
				stage.setDebugAll(debug);
				return true;
			}
			if (keycode == Input.Keys.F5) {
				show();
				return true;
			}
			return false;
		}
		
	}
}
