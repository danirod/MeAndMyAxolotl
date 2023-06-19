package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;


public class SwimmingPlayer extends AnimatedImage implements Disposable {

    public static final float OFFSET_X = 265f;
    public static final float OFFSET_Y = 40f;

    public SwimmingPlayer() {
        super(
            Orientation.VERTICAL,
            new Texture(Gdx.files.internal("swimming.png")),
            0.5f
        );
        // setScale(2f);
        setOrigin(OFFSET_X, OFFSET_Y);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
