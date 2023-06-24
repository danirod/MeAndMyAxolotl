package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

	JamGame game;

	GameStage stage;

	Viewport viewport;
	
	public GameScreen(JamGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		GameState.reset();
		viewport = new ExtendViewport(640, 480, 960, 480);
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		stage = new GameStage(game, viewport);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		stage.draw();
		stage.act();

		if (!stage.axolotl.isAlive()) {
			if (!stage.getRoot().hasActions()) {
				Action delay = Actions.delay(1f);
				Action fadeOut = Actions.fadeOut(1f);
				Action run = Actions.run(() -> {
					game.setScreen(new EndingScreen(game, JamGame.assets.get("screens/gameover.png")));
				});
				Action script = Actions.sequence(delay, fadeOut, run);
				stage.getRoot().addAction(script);
			}
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
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

}
