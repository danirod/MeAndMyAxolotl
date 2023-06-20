package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameScreen implements Screen {
	
	private Stage stage;
	
	Group bubbles = new Group();
	
	public GameScreen() {
		
	}
	
	@Override
	public void show() {
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
		
		var algasFront = new Group();
		stage.addActor(algasFront);
		
		BubbleSpawner spawner = new BubbleSpawner(bubbles, xo);
		stage.addActor(spawner);
		AlgaSpawner algaspawn = new AlgaSpawner(algasBack, algasFront);
		stage.addActor(algaspawn);
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
