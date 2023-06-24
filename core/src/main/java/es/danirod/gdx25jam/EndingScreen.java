package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class EndingScreen implements Screen {

    JamGame game;

	Texture tex;

	Stage stage;

	public EndingScreen(JamGame game, Texture tex) {
        this.game = game;
		this.tex = tex;
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(640, 480));
		Gdx.input.setInputProcessor(stage);
		
		// The static image that was given.
		stage.addActor(new Image(tex));

		// Push button to go back to the main menu.
        TextButton btn = new TextButton("BACK TO MAIN MENU", JamGame.skin);
        btn.setPosition(100, 40);
        btn.setSize(stage.getViewport().getWorldWidth() - 200, 70);
        btn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.transition(stage, new MainMenuScreen(game));
            }
        });
        stage.addActor(btn);
        
     // Quick way to go back to the start page (press ENTER or ESCAPE).
        stage.addListener(new InputListener() {
        	@Override
        	public boolean keyDown(InputEvent event, int keycode) {
        		if (keycode == Input.Keys.ENTER || keycode == Input.Keys.ESCAPE) {
        			game.transition(stage, new MainMenuScreen(game));
        			return true;
        		}
        		return super.keyDown(event, keycode);
        	}
        });
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);

		stage.draw();
		stage.act();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height);
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
		stage.dispose();
	}

	@Override
	public void dispose() {

	}

}
