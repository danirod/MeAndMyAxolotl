package es.danirod.gdx25jam;

public class GameState {

	public static GameState instance;
	
	public int score = 0;
	
	public static void reset() {
		instance = new GameState();
	}
	
}
