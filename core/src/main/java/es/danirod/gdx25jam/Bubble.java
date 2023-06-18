package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

public class Bubble extends Image {

    private static Texture bubbleTexture = null;

    public Bubble() {
        super(bubbleTexture());
        float offsetX = 200f * (float) (Math.random() - 0.5);
        Action moving = Actions.parallel(Actions.moveBy(offsetX, 200f, 0.5f), Actions.alpha(0, 0.5f));
        addAction(Actions.sequence(moving, Actions.removeActor()));
    }

    static Texture bubbleTexture() {
        if (bubbleTexture == null) {
            bubbleTexture = new Texture(Gdx.files.internal("bubble.png"));
        }
        return bubbleTexture;
    }

}
