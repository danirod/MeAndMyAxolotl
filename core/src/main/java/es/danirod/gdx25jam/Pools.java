package es.danirod.gdx25jam;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

public class Pools {

    public static final Pool<Rectangle> Rectangles = com.badlogic.gdx.utils.Pools.get(Rectangle.class);

    public static final Pool<Vector2> Vectors = com.badlogic.gdx.utils.Pools.get(Vector2.class);

}
