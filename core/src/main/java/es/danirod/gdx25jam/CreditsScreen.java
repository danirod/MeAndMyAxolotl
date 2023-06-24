package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CreditsScreen implements Screen {
	
	JamGame game;
	
	Stage stage;
	
	ScrollPane credits;
	
	TextButton backButton;
	
	public CreditsScreen(JamGame game) {
		this.game = game;
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(640, 480));
		Gdx.input.setInputProcessor(stage);
		
		// Background
		Texture texture = JamGame.assets.get("background.png");
		var background = new Image(texture);
		background.setBounds(0, 0, 640, 480);
		background.getColor().a = 0.5f;
		stage.addActor(background);
		
		// Credits.
		String creditsText = Gdx.files.internal("credits.txt").readString();
		var creditsLabel = new Label(creditsText, JamGame.skin);
		creditsLabel.setWrap(true);
		var creditsBox = new VerticalGroup();
		creditsBox.addActor(creditsLabel);
		creditsBox.grow().pad(0, 0, 0, 10);
		credits = new ScrollPane(creditsBox, JamGame.skin);
		credits.setBounds(20, 100, stage.getViewport().getWorldWidth() - 40, stage.getViewport().getWorldHeight() - 120);
		credits.setFadeScrollBars(false);
		stage.addActor(credits);
		
		// Back button.
		backButton = new TextButton("BACK", JamGame.skin);
		backButton.setPosition(stage.getViewport().getWorldWidth() / 2 - 200, 20);
		backButton.setSize(400, 50);
		backButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				JamGame.assets.get("sounds/pick.ogg", Sound.class).play(1.0f, 1.0f, 0.0f);
				game.transition(stage, new MainMenuScreen(game));
			}
		});
		stage.addActor(backButton);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL30.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		stage.draw();
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
