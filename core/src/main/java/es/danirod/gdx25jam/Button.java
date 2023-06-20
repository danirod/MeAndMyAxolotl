package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

import java.util.Arrays;

public class Button extends Image {

    Player player;

    private boolean isTouching;

    public Button(Player player) {
        super(new Texture(Gdx.files.internal("button.png")));
        this.player = player;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        checkCollision();
    }

    void checkCollision() {
        Rectangle mine = Pools.Rectangles.obtain();
        Rectangle other = Pools.Rectangles.obtain();
        mine.set(getX(), getY(), getWidth(), getHeight());
        player.getBoundingBox(other);
        System.out.println("mine: " + mine + " - other: " + other);
        if (mine.overlaps(other)) {
            if (!isTouching) {
                addAction(Actions.moveBy(0, -20f, 1f));
                isTouching = true;
            }
        } else {
            if (isTouching) {
                addAction(Actions.moveBy(0, 20f, 1f));
                isTouching = false;
            }
        }
        Pools.Rectangles.freeAll(Array.with(mine, other));
    }
}
