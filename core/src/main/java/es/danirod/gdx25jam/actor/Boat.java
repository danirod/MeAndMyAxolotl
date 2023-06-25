package es.danirod.gdx25jam.actor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import es.danirod.gdx25jam.JamGame;
import es.danirod.gdx25jam.actions.ScrollAction;

public class Boat extends Group {
	
	static final String[] names = {
			"ckmu32",
			"codesectarian",
			"ivantheragingpython",
			"Klairm_",
			"messer199",
			"mata649",
			"pacho901",
			"altaskur",
			"leo_develop",
			"cdecompilador",
			"e4yttuh",
			"dannywolfmx2",
			"PachinnWeb",
			"jemma1202",
			"ssmatiuri",
			"likendero",
			"ExtrEmi_C",
			"Niv3k_El_Pato",
			"DHardySD",
			"RothioTome",
			"Carlos4raujo",
			"amstrad05",
			"hellsing2030",
			"CursosDeDesarrollo",
			"MirosoHD",
			"Urdet",
			"Azzer_41",
			"mikelego3ds",
			"ccsavlad",
			"TheRealBridge",
			"ratamalvada",
			"a1t0rmenta",
			"ChrisVDev",
			"viciostv",
			"Raupulus",
			"nMarulo",
			"pythonesa",
			"lucionsuag",
			"programacion_es",
			"Forrest1789",
			"Gordon_F_",
			"intercambioneuronal",
			"LuisLlamas_es",
			"Elmerelmerelmer",
			"the_suisse",
			"omgslinux",
			"monetelab",
			
			"KEEP OCEANS CLEAN",
			"PRESERVE THE AXOLOTS",
	};
	
	static Set<String> pendingNames = new HashSet<>();

	static Texture getBoatTexture() {
		String asset = MathUtils.randomBoolean() ? "trajinera1.png" : "trajinera2.png";
		return JamGame.assets.get(asset);
	}
	
	static String getFollowerName() {
		if (pendingNames.isEmpty()) {
			pendingNames.addAll(Arrays.asList(names));
		}
		
		List<String> array = pendingNames.stream().toList();
		String random = array.get(MathUtils.random(array.size() - 1));
		pendingNames.remove(random);
		return random;
	}
	
	String name;
	
	public Boat() {
		var name = getFollowerName();
		
		var image = new Image(getBoatTexture());
		
		var font = JamGame.assets.get("silkscreen.fnt", BitmapFont.class);
		var layout = new GlyphLayout(font, name);
		
		float x = image.getWidth() / 2;
		float y = 79;
		x -= 2 * (layout.width / 2);
		y -= 2 * (layout.height / 2);
	
		var label = new Label(name, JamGame.skin, "silk");
		label.setPosition(x, y);
		label.setScale(2);
		label.setFontScale(2);
		
		addActor(image);
		addActor(label);
		
		setSize(image.getWidth(), image.getHeight());
		
		addAction(ScrollAction.scrollAndDelete(-240f));
	}
	
}
