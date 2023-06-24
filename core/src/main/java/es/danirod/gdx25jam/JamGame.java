package es.danirod.gdx25jam;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Pools;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class JamGame extends Game {

	public static AssetManager assets;

	public static Skin skin;

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

        assets = prepareAssetManager();
    	assets.finishLoading();

        skin = prepareSkin();

        setScreen(new MainMenuScreen(this));
    }

    AssetManager prepareAssetManager() {
        var assets = new AssetManager();
        assets.load("axolotl.png", Texture.class);
        assets.load("bubble.png", Texture.class);
        assets.load("floor.png", Texture.class, repeatX);
        assets.load("sky.png", Texture.class, repeatX);
        assets.load("water.png", Texture.class);
        assets.load("algae.png", Texture.class);
        assets.load("egg.png", Texture.class);
        assets.load("turtle.png", Texture.class);
        assets.load("trash1.png", Texture.class);
        assets.load("trash2.png", Texture.class);
        assets.load("trash3.png", Texture.class);
        assets.load("star.png", Texture.class);
        assets.load("font.fnt", BitmapFont.class);
        assets.load("large.fnt", BitmapFont.class);
        assets.load("farturtle.png", Texture.class);
        assets.load("uboot.png", Texture.class);
        assets.load("axolotl/body.png", Texture.class);
        assets.load("axolotl/lf.png", Texture.class);
        assets.load("axolotl/lb.png", Texture.class);
        assets.load("axolotl/rf.png", Texture.class);
        assets.load("axolotl/rb.png", Texture.class);
        assets.load("screens/gameover.png", Texture.class);
        assets.load("screens/youwin.png", Texture.class);
        assets.load("ui/button.png", Texture.class);
        assets.load("ui/logo.png", Texture.class);
        return assets;
    }

    Skin prepareSkin() {
        var font = assets.get("font.fnt", BitmapFont.class);
        var large = assets.get("large.fnt", BitmapFont.class);

        var labelStyle = new LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;

        var largeLabelStyle = new LabelStyle();
        largeLabelStyle.font = large;
        largeLabelStyle.fontColor = Color.WHITE;

        Texture button = assets.get("ui/button.png");
        TextureRegion buttonNormal = new TextureRegion(button, 0, 0, 64, 64);
        TextureRegion buttonDown = new TextureRegion(button, 64, 0, 64, 64);
        NinePatch patchNormal = new NinePatch(buttonNormal, 10, 10, 10, 10);
        NinePatch patchDown = new NinePatch(buttonDown, 10, 10, 14, 6);

        var drawableNormal = new NinePatchDrawable(patchNormal);
        var drawableDown = new NinePatchDrawable(patchDown);
        var textButtonStyle = new TextButton.TextButtonStyle(drawableNormal, drawableDown, drawableNormal, large);
        textButtonStyle.fontColor = Color.BLACK;

        var skin = new Skin();
        skin.add("default", labelStyle);
        skin.add("large", largeLabelStyle);
        skin.add("default", textButtonStyle);
        return skin;
    }

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();
		assets.dispose();
	}

    public void transition(Stage stageToFadeOut, Screen nextScreen) {
        Action fadeOut = Actions.fadeOut(1f);
        Action switchScreen = Actions.run(() -> setScreen(nextScreen));
        Action fadeOutThenSwitch = Actions.sequence(fadeOut, switchScreen);
        stageToFadeOut.addAction(fadeOutThenSwitch);
    }
}

