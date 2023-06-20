package es.danirod.gdx25jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pools;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JamGame extends Game {

	public static AssetManager assets;
	
	private static final TextureParameter repeatX;
	
	static {
		repeatX = new TextureParameter();
		repeatX.wrapU = TextureWrap.MirroredRepeat;
		repeatX.wrapV = TextureWrap.MirroredRepeat;
	}

    @Override
    public void create() {
    	Pools.get(Vector2.class);
    	Pools.get(Rectangle.class);
    	
    	assets = new AssetManager();
    	assets.load("axolotl.png", Texture.class);
    	assets.load("bubble.png", Texture.class);
    	assets.load("floor.png", Texture.class, repeatX);
    	assets.load("sky.png", Texture.class, repeatX);
    	assets.load("water.png", Texture.class);
    	assets.load("algae.png", Texture.class);
    	assets.finishLoading();
    	
        setScreen(new GameScreen());
    }
	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		assets.dispose();
	}
}
