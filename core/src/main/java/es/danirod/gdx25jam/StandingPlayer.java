package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;

public class StandingPlayer extends Image implements Disposable {

    private final Texture texture;

    public StandingPlayer() {
        texture = new Texture(Gdx.files.internal("stand.png"));
        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(texture));
        setDrawable(drawable);
        setSize(texture.getWidth(), texture.getHeight());
        setAlign(Align.center);
        setScaling(Scaling.fit);
        setOrigin(30, 60);
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
