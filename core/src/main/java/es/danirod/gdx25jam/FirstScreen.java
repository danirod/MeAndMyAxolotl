package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private Player player;

    private Stage stage;

    private ShapeRenderer shaper;

    @Override
    public void show() {
        shaper = new ShapeRenderer();
        shaper.setAutoShapeType(true);
        stage = new Stage();
        stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        player = new Player();
        stage.addActor(player);
        stage.setDebugAll(true);
        player.setScale(0.5f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act();
        stage.draw();

        shaper.setProjectionMatrix(stage.getCamera().combined);

        shaper.begin();
        shaper.setColor(Color.WHITE);
        shaper.line(-1000, 0, 1000, 0);
        shaper.line(0, -1000, 0, 1000);
        shaper.end();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void hide() {
        // This method is called when another screen replaces this one.
    }

    @Override
    public void dispose() {
        stage.dispose();
        player.dispose();
    }
}
