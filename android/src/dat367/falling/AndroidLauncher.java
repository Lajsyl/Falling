package dat367.falling;

import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import dat367.falling.platform.GdxPlatformLayer;


public class AndroidLauncher extends CardBoardAndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.depth = 32;
		initialize(new GdxPlatformLayer(true), config);
	}
}
