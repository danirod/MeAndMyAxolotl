package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.utils.viewport.FitViewport;

import java.util.Random;

/** First screen of the application. Displayed after the application is created. */
public class FirstScreen implements Screen {

    private Player player;

    private Stage stage;

    private ShapeRenderer shapeRenderer;

    private Algae[] alga;

    Oxygen oxygen;

    @Override
    public void show() {
        stage = new Stage();
        stage.setViewport(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        player = new Player();
        stage.addActor(player);
        player.setScale(0.5f);

        stage.addListener(new InputListener() {

            private boolean debug = false;

            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.F12) {
                    debug = !debug;
                    stage.setDebugAll(debug);
                    return true;
                }
                return false;
            }
        });

        Button touchButton = new Button(player);
        touchButton.setPosition(50, -Gdx.graphics.getHeight() / 2);
        stage.addActor(touchButton);


        alga = new Algae[3];
        Random rnd = new Random();
        for (int i = 0; i < alga.length; i++) {
            alga[i] = new Algae();
            int minWidth = (int) (-Gdx.graphics.getWidth() / 2 + alga[i].getWidth());
            int maxWidth = (int) (Gdx.graphics.getWidth() / 2 - alga[i].getWidth());
            alga[i].setX(rnd.nextInt(minWidth, maxWidth));
            alga[i].setY(-Gdx.graphics.getHeight() / 2f);
            stage.addActor(alga[i]);
        }

        oxygen = new Oxygen(alga, player);
        oxygen.setBounds(-Gdx.graphics.getWidth() / 2 + 20, Gdx.graphics.getHeight() / 2 - 40, 200, 20);
        stage.addActor(oxygen);

        shapeRenderer = new ShapeRenderer();
        sColorUp = new Color(0.1f, 0.1f, 0.3f, 1f);
        sColorDown = new Color(0.05f, 0.05f, 0.2f, 1f);

        Gdx.input.setInputProcessor(stage);
    }

    private Color sColorUp, sColorDown;

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.3f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(
            -Gdx.graphics.getWidth() / 2f,
            -Gdx.graphics.getHeight() / 2f,
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight(),
            sColorDown, sColorDown,
            sColorUp, sColorUp
            );
        shapeRenderer.end();

        stage.act();
        stage.draw();

        if (oxygen.oxygen <= 0) {
            Gdx.app.log("Game", "Game over");
        }
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
        shapeRenderer.dispose();
        stage.dispose();
        player.dispose();
    }
}
