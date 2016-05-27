package dat367.falling;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.badlogic.gdx.backends.android.ShakeListener;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.platform.GdxPlatformLayer;


public class AndroidLauncher extends CardBoardAndroidApplication {

	private CardboardAudioEngine cardboardAudioEngine; // VR Sound
	private ShakeListener shakeListener; // Shake events

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 32;

		cardboardAudioEngine = new CardboardAudioEngine(this, CardboardAudioEngine.RenderingMode.BINAURAL_HIGH_QUALITY);
		GdxPlatformLayer gdxPlatformLayer = new GdxPlatformLayer(true);
		gdxPlatformLayer.setCardboardAudioEngine(cardboardAudioEngine);

		initialize(gdxPlatformLayer, config);
	}

}
