package es.danirod.gdx25jam.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Pools;

import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.ShakeAction;

public class Axolotl extends AnimatedImage {

    // public static final float OFFSET_X = 265f;
    
    // public static final float OFFSET_Y = 40f;
	
	int stunnedCombo = 0;
	
	Actor stunnedStar;

    public Axolotl() {
        super(
            Orientation.VERTICAL,
            JamGame.assets.get("axolotl.png"),
            0.5f
        );
        // setScale(2f);
        // setOrigin(OFFSET_X, OFFSET_Y);
        setOrigin(91, 19);
    }
    
    public void stun() {
    	addAction(Actions.sequence(
    			Actions.run(() -> startStunEffect()),
    			Actions.delay(3f),
    			Actions.run(() -> stopStunEffect())
		));
    }
    
    void startStunEffect() {
    	if (stunnedCombo++ == 0) {
    		stunnedStar = new Image(JamGame.assets.get("star.png", Texture.class));
    		stunnedStar.setPosition(getX() + getOriginX(), getY() + getHeight());
    		stunnedStar.addAction(
        			Actions.forever(
    					Actions.sequence(
    						Actions.moveBy(-2 * stunnedStar.getWidth(), 0, 0.1f),
    						Actions.moveBy(4 * stunnedStar.getWidth(), 0, 0.2f),
    						Actions.moveBy(-2 * stunnedStar.getWidth(), 0, 0.1f)
    					)
    				)
    		);
        	getStage().addActor(stunnedStar);
    	}
    	addAction(ShakeAction.shake(0.3f));
    }
    
    void stopStunEffect() {
    	if (--stunnedCombo == 0) {
    		stunnedStar.remove();
    	}
    }
    
    @Override
    public void act(float delta) {
    	super.act(delta);
    	if (stunnedCombo == 0) {
    		swimUpToMouse(delta);
    	}
    }
    
    void getHeadBox(Rectangle out) {
    	float cx = getX() + getOriginX(), cy = getY() + getOriginY();
    	out.set(cx - 15, cy - 15, 30, 30);
    }
    
    void swimUpToMouse(float delta) {
    	Vector2 mouse = null;
    	Vector2 axolotl = null;
    	Vector2 distance = null;
    	try {
    		mouse = Pools.obtain(Vector2.class);
    		axolotl = Pools.obtain(Vector2.class);
    		distance = Pools.obtain(Vector2.class);
    		mouse.set(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
    		axolotl.set(getX() + getOriginX(), getY() + getOriginY());
    		distance.set(mouse).sub(axolotl);
    		if (distance.len() < 10) {
    			return;
    		}
    		float angle = (int) distance.angleDeg();
    		setRotation(angle);
    		if (angle >= 90 && angle <= 270) {
    			setScaleX(-1);
    		} else {
    			setScaleX(1);
    		}
    		distance.nor().scl(500f * delta);
    		moveBy(0, distance.y);
    		if (getY() < 40 - getOriginY()) {
    			setY(40 - getOriginY());
    		}
    		if (getY() > Gdx.graphics.getHeight() - getHeight() + getOriginY() - 80) {
    			setY(Gdx.graphics.getHeight() - getHeight() + getOriginY() - 80);
    		}
    	} finally {
    		Pools.free(mouse);
    		Pools.free(axolotl);
    		Pools.free(distance);
    	}
    }
}
