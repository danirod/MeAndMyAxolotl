package es.danirod.gdx25jam;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bubble extends Image {

    public Bubble() {
        super(JamGame.assets.get("bubble.png", Texture.class));
        float offsetX = 200f * (float) (Math.random() - 0.5);
        Action moving = Actions.parallel(Actions.moveBy(offsetX, 150f, 0.5f), Actions.alpha(0, 0.5f));
        addAction(Actions.sequence(moving, Actions.removeActor()));
    }
}
