package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;

public class StandingPlayer extends AnimatedImage implements Disposable {

    public StandingPlayer() {
        super(
            Orientation.HORIZONTAL,
            new Texture(Gdx.files.internal("stand.png")),
            0.5f
        );
        setOrigin(50, 80);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
