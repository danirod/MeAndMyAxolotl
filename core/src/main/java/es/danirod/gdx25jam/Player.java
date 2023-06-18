package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Disposable;

public class Player extends Group implements Disposable {

    private final SwimmingPlayer swimmingPlayer = new SwimmingPlayer();
    private final StandingPlayer standingPlayer = new StandingPlayer();

    private Actor currentActor;

    public Player() {
        swimmingPlayer.setPosition(-swimmingPlayer.getOriginX(), -swimmingPlayer.getHeight() + swimmingPlayer.getOriginY());
        standingPlayer.setPosition(-standingPlayer.getOriginX(), -standingPlayer.getHeight() + standingPlayer.getOriginY());
        switchToActor(swimmingPlayer);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        gravitateToCursor(delta);
    }

    void getScreenCenter(Vector2 out) {
        float centerX = currentActor.getX() + currentActor.getOriginX();
        float centerY = currentActor.getY () + (currentActor.getHeight() - currentActor.getOriginY());
        out.set(centerX, centerY);
        localToScreenCoordinates(out);
    }

    Vector2 sCenter = new Vector2();
    Vector2 sSpeedVector = new Vector2();
    Vector2 sAuxSpeedVector = new Vector2();
    Circle sMouseCircle = new Circle();
    Circle sCenterCircle = new Circle();
    int count = 0;
    boolean isCloseToMouse() {
        getScreenCenter(sCenter);

        float cursorX = Gdx.input.getX(), cursorY = Gdx.input.getY();

        sMouseCircle.set(cursorX, cursorY, 5f);
        sCenterCircle.set(sCenter.x, sCenter.y, 5f);
        return sCenterCircle.overlaps(sMouseCircle);
    }

    private void swimToMouse(float delta) {
        getScreenCenter(sAuxSpeedVector);
        sSpeedVector
            .set(Gdx.input.getX(), Gdx.input.getY())
            .sub(sAuxSpeedVector)
            .nor()
            .scl(60f * delta);
        if (sSpeedVector.x < 0) {
            currentActor.setScaleX(-1);
        } else {
            currentActor.setScaleX(1);
        }
        moveBy(sSpeedVector.x, -sSpeedVector.y);
    }

    void gravitateToCursor(float delta) {
        if (isCloseToMouse()) {
            switchToActor(standingPlayer);
        } else {
            switchToActor(swimmingPlayer);
            swimToMouse(delta);
        }
    }

    void switchToActor(Actor actor) {
        clear();
        addActor(actor);
        setSize(actor.getWidth(), actor.getHeight());
        currentActor = actor;
    }

    @Override
    public void dispose() {
        swimmingPlayer.dispose();
        standingPlayer.dispose();
    }
}
