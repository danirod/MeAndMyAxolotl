package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;

public class Algae extends AnimatedImage implements Disposable {

    private Pool<Rectangle> rectangles = Pools.get(Rectangle.class);

    public Algae() {
        super(
            Orientation.HORIZONTAL,
            new Texture(Gdx.files.internal("algae.png")),
            1f
        );
    }

    public boolean overlaps(Actor a) {
        Rectangle mine = rectangles.obtain();
        Rectangle other = rectangles.obtain();
        mine.set(getX(), getY(), getWidth(), getHeight());
        other.set(a.getX(), a.getY(), a.getWidth(), a.getHeight());
        boolean overlaps = mine.overlaps(other);
        rectangles.free(mine);
        rectangles.free(other);
        return overlaps;
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
