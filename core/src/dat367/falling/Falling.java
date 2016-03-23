package dat367.falling;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.vrtoolkit.cardboard.HeadTransform;

public class Falling implements CardBoardApplicationListener {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void onNewFrame(com.google.vrtoolkit.cardboard.HeadTransform paramHeadTransform) {

	}

	@Override
	public void onDrawEye(com.google.vrtoolkit.cardboard.Eye paramEye) {

	}

	@Override
	public void onFinishFrame(com.google.vrtoolkit.cardboard.Viewport paramViewport) {

	}

	@Override
	public void onRendererShutdown() {

	}

	@Override
	public void onCardboardTrigger() {

	}
}
