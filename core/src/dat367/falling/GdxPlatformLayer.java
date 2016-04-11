package dat367.falling;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.CardBoardAndroidApplication;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.AnimationController;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.UBJsonReader;
import com.google.vrtoolkit.cardboard.HeadTransform;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsibilites
 * Create game
 * ask game for resources
 * update
 * when something in game wants to be drawn, draw it
 * sound, etc
 */

public class GdxPlatformLayer implements CardBoardApplicationListener {

	private FallingGame game;

	private CardboardCamera cardboardCamera;
	private PerspectiveCamera camera;
	private static final float Z_NEAR = 0.1f;
	private static final float Z_FAR = 300f;

	private UBJsonReader jsonReader = new UBJsonReader();
	private G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

	private ModelBatch modelBatch;
	private Map<String, ModelInstance> models = new HashMap<String, ModelInstance>();
	private Environment environment;


	@Override

	public void create() {

		game = new FallingGame();
		loadResources(game.getCurrentJump().getResourceRequirements());

		camera = new PerspectiveCamera(75, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.position.set(0f, 100f, 100f);
		camera.lookAt(0f,100f,0f);
		camera.near = Z_NEAR;
		camera.far = Z_FAR;

		cardboardCamera = new CardboardCamera();
		cardboardCamera.position.set(0f, 100f, 100f);
		cardboardCamera.lookAt(0f,100f,0f);
		cardboardCamera.near = 0.1f;
		cardboardCamera.far = 300f;


		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.8f, 0.8f, 0.8f, 1.0f));

	}

	public void loadResources(ResourceRequirements resourceRequirements) {
		for (Model model : resourceRequirements.getModels()) {
			String fileName = model.getModelFileName();
			if (!models.containsKey(fileName)) {
				com.badlogic.gdx.graphics.g3d.Model gdxModel = modelLoader.loadModel(Gdx.files.getFileHandle(fileName, Files.FileType.Internal));
				ModelInstance gdxModelInstance = new ModelInstance(gdxModel);
				models.put(fileName, gdxModelInstance);
			}
		}
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		//model.dispose();
	}

	private void renderScene(Camera camera) {
		modelBatch.begin(camera);

		while(RenderQueue.hasMoreTasks()) {
			RenderQueue.RenderTask task = RenderQueue.getNextTask();

			// Fetch model instance
			if (models.containsKey(task.model.getModelFileName())) {
				ModelInstance instance = models.get(task.model.getModelFileName());

				// Set transform of model instance
				//task.position

				modelBatch.render(instance, environment);
			}
		}

		modelBatch.end();
	}

	@Override
	public void render() {
		Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera.update();

		update();

		renderScene(camera);

	}

	private void update() {
		game.update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void onNewFrame(com.google.vrtoolkit.cardboard.HeadTransform paramHeadTransform) {
		update();
	}

	@Override
	public void onDrawEye(com.google.vrtoolkit.cardboard.Eye eye) {
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		cardboardCamera.setEyeViewAdjustMatrix(new Matrix4(eye.getEyeView()));

		float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
		cardboardCamera.setEyeProjection(new Matrix4(perspective));
		cardboardCamera.update();

		renderScene(cardboardCamera);
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
