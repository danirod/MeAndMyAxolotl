package es.danirod.gdx25jam;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

public class Axolotl extends AnimatedImage {

    // public static final float OFFSET_X = 265f;
    
    // public static final float OFFSET_Y = 40f;

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
    
    @Override
    public void act(float delta) {
    	super.act(delta);
    	swimUpToMouse(delta);
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
