package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
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

public class MainMenuScreen implements Screen {

    Stage stage;

    JamGame game;

    public MainMenuScreen(JamGame game) {
        this.game = game;
    }



    @Override
    public void show() {
        stage = new Stage(new FitViewport(640, 480));
        Gdx.input.setInputProcessor(stage);

        // Logo.
        Image logo = new Image(JamGame.assets.get("ui/logo.png", Texture.class));
        logo.setPosition(
        		stage.getViewport().getWorldWidth() / 2 - logo.getWidth() / 2,
        		stage.getViewport().getWorldHeight() - logo.getHeight() - 50
        );
        stage.addActor(logo);

        // Play button.
        TextButton button = new TextButton("START GAME", JamGame.skin);
        button.setSize(400, 200);
        button.setPosition(stage.getViewport().getWorldWidth() / 2 - 200, 50);
        button.setColor(Color.valueOf("a8e2ff"));
        button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.transition(stage, new GameScreen(game));
            }
        });
        stage.addActor(button);
        
        // Quick way to start the game (press ENTER).
        stage.addListener(new InputListener() {
        	@Override
        	public boolean keyDown(InputEvent event, int keycode) {
        		if (keycode == Input.Keys.ENTER) {
        			game.transition(stage, new GameScreen(game));
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

        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
    	stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
