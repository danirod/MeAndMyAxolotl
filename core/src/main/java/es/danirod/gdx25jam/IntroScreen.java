package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;

import es.danirod.gdx25jam.actions.ShakeAction;

public class IntroScreen implements Screen {
	
	private static final String[] screens = {
			"screens/myeggs.png",
			"screens/flow.png",
			"screens/currents.png",
			"screens/savethem.png",
	};
	
	private static final boolean[] shakes = {
			false,
			true,
			false,
			false,
	};
	
	private JamGame game;
	
	private int screen = 0;
	
	private Stage stage;
	
	private Image image;
	
	private TextButton next, start;
	
	public IntroScreen(JamGame game) {
		this.game = game;
	}
	
	public boolean hasMore() {
		return screen < screens.length - 1;
	}

	@Override
	public void show() {
		stage = new Stage(new FitViewport(640, 480));
		
		image = new Image();
		image.setBounds(0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
		image.setAlign(Align.bottom);
		image.setScaling(Scaling.fit);
		stage.addActor(image);
		Gdx.input.setInputProcessor(stage);
		
		next = new TextButton(">>", JamGame.skin);
		next.setBounds(stage.getViewport().getWorldWidth() - 100, 20, 70, 70);
		next.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				JamGame.assets.get("sounds/pick.ogg", Sound.class).play(1.0f, 1.0f, 0.0f);
				triggerNextImage();
			}
		});
		stage.addActor(next);
		
		start = new TextButton("START", JamGame.skin);
		start.setBounds(stage.getViewport().getWorldWidth() - 140, 20, 120, 70);
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				JamGame.assets.get("sounds/pick.ogg", Sound.class).play(1.0f, 1.0f, 0.0f);
				game.transition(stage, new GameScreen(game));
			}
		});
		start.setVisible(false);
		stage.addActor(start);
		
		updateImage();
	}
	
	void triggerNextImage() {
		screen++;
		updateImage();
	}
	
	void updateImage() {
		Texture text = JamGame.assets.get(screens[screen], Texture.class);
		TextureRegionDrawable drawable = new TextureRegionDrawable(text);
		image.setDrawable(drawable);
		
		next.setVisible(hasMore());
		start.setVisible(!hasMore());
		
		if (shakes[screen]) {
			image.addAction(Actions.forever(ShakeAction.shake(0.5f)));
		} else {
			image.clearActions();
		}
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
		// TODO Auto-generated method stub
		
	}

}
