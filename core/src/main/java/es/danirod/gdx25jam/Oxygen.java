package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

import java.util.Arrays;
import java.util.stream.StreamSupport;

public class Oxygen extends Actor implements Disposable {

    float oxygen = 1f;

    Algae[] algas;

    Player player;

    ShapeRenderer renderer = new ShapeRenderer();

    private Pool<Rectangle> rectangles = Pools.get(Rectangle.class);

    public Oxygen(Algae[] algas, Player player) {
        this.algas = algas;
        this.player = player;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());
        renderer.begin(ShapeRenderer.ShapeType.Line);
        renderer.setColor(Color.WHITE);
        renderer.rect(getX(), getY(), getWidth(), getHeight());
        renderer.end();
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.rect(getX() + 4, getY() + 4, (getWidth() - 8) * oxygen ,getHeight() - 8);
        renderer.end();
        batch.begin();
    }

    @Override
    public void act(float delta) {
        oxygen -= delta * 0.025f;
        if (oxygen < 0) {
            oxygen = 0;
        }
        if (checkAlga()) {
            oxygen += delta * 0.4f;
            if (oxygen > 1) {
                oxygen = 1;
            }
        }
    }

    boolean checkAlga() {
        Rectangle mine = rectangles.obtain();
        Rectangle other = rectangles.obtain();
        other.set(player.getX(), player.getY(), player.getWidth(), player.getHeight());
        return Arrays.stream(algas)
                .anyMatch(alga -> {
                    mine.set(alga.getX(), alga.getY(), alga.getWidth(), alga.getHeight());
                    return mine.overlaps(other);
                });
    }

    @Override
    public void dispose() {
        renderer.dispose();
    }
}
