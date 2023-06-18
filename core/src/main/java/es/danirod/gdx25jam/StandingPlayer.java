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

    private final TextureRegionDrawable[] regions;

    private TextureRegionDrawable drawable;

    private int frame = 0;

    private float timer = 0f;

    public StandingPlayer() {
        texture = new Texture(Gdx.files.internal("stand.png"));
        regions = getRegions();
        setDrawable(regions[frame]);
        setSize(texture.getWidth() / 2f, texture.getHeight());
        setAlign(Align.center);
        setScaling(Scaling.fit);
        // setOrigin(50, 115);
        setOrigin(50, 80);
    }

    @Override
    public void act(float delta) {
        updateFrame(delta);
    }

    void updateFrame(float delta) {
        timer += delta;
        while (timer > 0.5f) {
            frame = (frame + 1) % 2;
            timer -= 0.5f;
            setDrawable(regions[frame]);
        }
    }

    TextureRegionDrawable[] getRegions() {
        TextureRegion[][] regions = TextureRegion.split(texture, texture.getWidth() / 2, texture.getHeight());
        return new TextureRegionDrawable[] {
            new TextureRegionDrawable(regions[0][0]),
            new TextureRegionDrawable(regions[0][1])
        };
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
