package es.danirod.gdx25jam;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pools;

public class Utils {

	/** Copy the bounds of the actor into the given rectangle. */
	public static void transferBounds(Actor actor, Rectangle rect) {
		rect.setPosition(actor.getX(), actor.getY());
		rect.setSize(actor.getWidth(), actor.getHeight());
	}
	
	/** Copy the bounds of the actor, taking into account sub-groups. */
	public static void transferStageBounds(Actor actor, Rectangle rect) {
		Vector2 vec = null;
		try {
			vec = Pools.obtain(Vector2.class);
			
			// The tricky thing is that you have to set to (0,0) in order to
			// query the stage location of the bottom-left corner of the actor.
			vec.set(0, 0);
			actor.localToStageCoordinates(vec);
			rect.set(vec.x, vec.y, actor.getWidth(), actor.getHeight());
		} finally {
			Pools.free(vec);
		}
	}
	
	public static void transferPosition(Actor actor, Vector2 vec) {
		vec.set(actor.getX(), actor.getY());
	}
	
	public static void transferSize(Actor actor, Vector2 vec) {
		vec.set(actor.getWidth(), actor.getHeight());
	}
	
	public static <T> T pick(T[] items) {
		return items[MathUtils.random(items.length - 1)];
	}
	
}
