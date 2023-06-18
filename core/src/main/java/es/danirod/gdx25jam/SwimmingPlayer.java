package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.Scaling;

public class SwimmingPlayer extends Image implements Disposable {

    public static final float OFFSET_X = 265f;
    public static final float OFFSET_Y = 40f;

    private Texture texture;

    private TextureRegion[] regions;

    private int current = 0;

    private float timeFrame = 0f;

    public SwimmingPlayer() {
        texture = new Texture(Gdx.files.internal("swimming.png"));
        TextureRegion[][] regions = TextureRegion.split(texture, texture.getWidth(), texture.getHeight() / 2);
        this.regions = new TextureRegion[] { regions[0][0], regions[1][0] };
        updateRegion();
        setOrigin(OFFSET_X, OFFSET_Y);
    }

    void updateRegion() {
        setDrawable(new TextureRegionDrawable(regions[current]));
        setAlign(Align.center);
        setScaling(Scaling.fit);
        setSize(regions[current].getRegionWidth(), regions[current].getRegionHeight());
    }

    @Override
    public void act(float delta) {
        timeFrame += delta;
        while (timeFrame > 0.5f) {
            current = (current + 1) % 2;
            timeFrame -= 0.5f;
            updateRegion();
        }
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}
