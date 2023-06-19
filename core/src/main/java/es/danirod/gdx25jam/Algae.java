package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class Algae extends AnimatedImage implements Disposable {

    public Algae() {
        super(
            Orientation.HORIZONTAL,
            new Texture(Gdx.files.internal("algae.png")),
            1f
        );
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
