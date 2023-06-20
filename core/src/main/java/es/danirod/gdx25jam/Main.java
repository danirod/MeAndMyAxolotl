package es.danirod.gdx25jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {



    @Override
    public void create() {
    	Pools.get(Vector2.class);
    	Pools.get(Rectangle.class);
        setScreen(new FirstScreen());
    }
}
