package dat367.falling;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.platform.GdxPlatformLayer;


public class AndroidLauncher extends CardBoardAndroidApplication {

	private CardboardAudioEngine cardboardAudioEngine; // VR Sound

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		cardboardAudioEngine = new CardboardAudioEngine(this, CardboardAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
//		testCardboardSound();
		GdxPlatformLayer gdxPlatformLayer = new GdxPlatformLayer(true);
		gdxPlatformLayer.setCardboardAudioEngine(cardboardAudioEngine);

		initialize(gdxPlatformLayer, config);
		// Om testCardboardSound() (eller specifikt preloadSoundFile) ligger ha¨r sa' kraschar det
	}

	public void testCardboardSound() {

//		cardboardAudioEngine.preloadSoundFile("wind01.wav");
//		int soundID = cardboardAudioEngine.createSoundObject("wind01.wav");
//		cardboardAudioEngine.setSoundObjectPosition(soundID, 0, 0, 5);
//		cardboardAudioEngine.playSound(soundID, true);
//		System.out.println("isSoundPlaying: "+cardboardAudioEngine.isSoundPlaying(soundID));
//		System.out.println("isSoundPlaying: soundID = " + soundID);

		// TEST CARDBOARD SOUND
//		new Thread(
//				new Runnable() {
//					@Override
//					public void run() {
//						cardboardAudioEngine.preloadSoundFile("wind01.wav");
//						int soundID = cardboardAudioEngine.createSoundObject("wind01.wav");
//						cardboardAudioEngine.setSoundObjectPosition(soundID, 5, 0, 0);
////                    cardboardAudioEngine.
//						cardboardAudioEngine.playSound(soundID, true);
//						System.out.println("isSoundPlaying: "+cardboardAudioEngine.isSoundPlaying(soundID));
//						System.out.println("isSoundPlaying: soundID = " + soundID);
//					}
//				})
//				.start();
		// --------------------
	}
}
