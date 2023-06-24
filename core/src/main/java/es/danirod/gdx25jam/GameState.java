package es.danirod.gdx25jam;

public class GameState {

	public static GameState instance;
	
	public int score = 0;
	
	public boolean isFinishing = false;
	
	public static void reset() {
		instance = new GameState();
	}
	
}
