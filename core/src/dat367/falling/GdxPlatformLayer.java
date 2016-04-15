package dat367.falling;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsibilities
 * Create game
 * ask game for resources
 * updateGame
 * when something in game wants to be drawn, draw it
 * sound, etc
 */

public class GdxPlatformLayer implements CardBoardApplicationListener {

	boolean platformIsAndroid;
	final boolean USING_DEBUG_CAMERA = false;

	private FallingGame game;
	private Camera mainCamera;
	private static final float Z_NEAR = 0.1f;
	private static final float Z_FAR = 300f;

	private UBJsonReader jsonReader = new UBJsonReader();
	private G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

	private ModelBatch modelBatch;
	private Map<String, ModelInstance> models = new HashMap<String, ModelInstance>();
	private Environment environment;

	private SpriteBatch spriteBatch;
	private BitmapFont font;

	public GdxPlatformLayer(boolean platformIsAndroid) {
		this.platformIsAndroid = platformIsAndroid;
	}

	@Override
	public void create() {

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		game = new FallingGame();
		loadResources(game.getCurrentJump().getResourceRequirements());

		if (platformIsAndroid) {
			mainCamera = new CardboardCamera();
			setCameraPosAndOrientation();
		} else {
			mainCamera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			if (USING_DEBUG_CAMERA) {
				// Set position first frame
				setCameraPosAndOrientation();
			}
		}
		mainCamera.near = Z_NEAR;
		mainCamera.far = Z_FAR;

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f));

		spriteBatch = new SpriteBatch();
		font = new BitmapFont();
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
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {
		modelBatch.dispose();
		spriteBatch.dispose();
		// TODO: Dispose of all resources!
	}

	private void renderScene(Camera camera) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Render 3D
		modelBatch.begin(camera);
		{
			for (RenderQueue.RenderTask task : RenderQueue.getTasks()) {
				// Fetch model instance
				if (models.containsKey(task.model.getModelFileName())) {
					ModelInstance instance = models.get(task.model.getModelFileName());

					// TODO: Set transform of model instance
					// task.position

					modelBatch.render(instance, environment);
				}
			}
		}
		modelBatch.end();

		// Render 2D
		spriteBatch.begin();
		{
			String debugText =
					"Camera pos: " + camera.position + "\n" +
					"Look dir: " + camera.direction;

			font.draw(spriteBatch, debugText, 50, Gdx.graphics.getHeight() - 60);

			// Draw crosshair etc. for desktop control
			if (!platformIsAndroid && !USING_DEBUG_CAMERA) {
				Color color = font.getColor().cpy();
				{
					font.setColor(Color.RED);
					font.draw(spriteBatch, "o",
							Gdx.graphics.getBackBufferWidth() / 2.0f - 5,
							Gdx.graphics.getBackBufferHeight() / 2.0f + 8);

					font.draw(spriteBatch, "*",
							Gdx.input.getX() - 4,
							Gdx.graphics.getBackBufferHeight() - Gdx.input.getY() + 4);
				}
				font.setColor(color);
			}
		}
		spriteBatch.end();
	}

	private void updateGame() {
		RenderQueue.clear();
		game.update(Gdx.graphics.getDeltaTime());

	}

	//---- DESKTOP-SPECIFIC ----//

	private void desktopUpdate() {

		// Set look direction depending on mouse position
		handleDesktopControls();

		// Desktop FPS camera movement
		if (USING_DEBUG_CAMERA) {
			handleDebugCameraControl();
			updateGame();
		} else {

			// Update game logic
			updateGame();

			setCameraPosAndOrientation();
		}

	}

	private void handleDesktopControls() {
		final float MOUSE_SENSITIVITY = 2000f;

		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT) || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.input.setCursorCatched(false);
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Gdx.input.setCursorCatched(true);

		}

		if (Gdx.input.isCursorCatched()) {

			float dX = Gdx.input.getX() - Gdx.graphics.getBackBufferWidth() / 2.0f;
			float dY = Gdx.input.getY() - Gdx.graphics.getBackBufferHeight() / 2.0f;

			// Discard minor noise
			if (Math.abs(dX) < 15) dX = 0;
			if (Math.abs(dY) < 15) dY = 0;

			// Clamp to range [-30..30]
			dX = Math.min(30, Math.max(dX, -30));
			dY = Math.min(30, Math.max(dY, -30));

			// Scale movement
			dX /= MOUSE_SENSITIVITY;
			dY /= MOUSE_SENSITIVITY;

			Vector3 currentLookDirection = libGdxVector(game.getLookDirection());
			mainCamera.normalizeUp();
			Vector3 lookDirection = currentLookDirection
					.rotateRad(mainCamera.up, -dX)
					.rotateRad(mainCamera.up.cpy().nor().crs(mainCamera.direction.cpy().nor()), dY);

			game.setLookDirection(gameVector(lookDirection));
		}
	}

	private void setCameraPosAndOrientation() {
		// Set camera position depending on jumper position
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));

		// Set camera orientation depending on jumper look direction
		Vector jumperLookDirection = game.getCurrentJump().getJumper().getLookDirection();
		Vector lookAtPoint = jumperHeadPosition.add(jumperLookDirection);
		mainCamera.lookAt(libGdxVector(lookAtPoint));
	}

	@Override
	public void render() {
		if (!platformIsAndroid) {

			desktopUpdate();

			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			mainCamera.update();
			renderScene(mainCamera);
		}
	}

	private void handleDebugCameraControl() {
		final float mouseSensitivity = 0.1f;
		final float speed = 0.05f;
		final float rollSpeed = 15.0f;

		Vector3 up = new Vector3(mainCamera.up).nor();
		Vector3 forward = new Vector3(mainCamera.direction).nor();
		Vector3 right = forward.cpy().crs(up).nor();

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Gdx.input.setCursorCatched(true);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			Gdx.input.setCursorCatched(false);
		}

		// Rotation
		if (Gdx.input.isCursorCatched()) {
			int dX = Gdx.input.getDeltaX();
			int dY = Gdx.input.getDeltaY();

			float rotationZ = 0.0f;
			if (Gdx.input.isKeyPressed(Input.Keys.Q)) rotationZ -= rollSpeed;
			if (Gdx.input.isKeyPressed(Input.Keys.E)) rotationZ += rollSpeed;

			mainCamera.rotate(-dX * mouseSensitivity, up.x, up.y, up.z);
			mainCamera.rotate(-dY * mouseSensitivity, right.x, right.y, right.z);
			mainCamera.rotate(rotationZ * mouseSensitivity, forward.x, forward.y, forward.z);
		}

		// Translation
		Vector3 translation = new Vector3(0, 0, 0);
		if (Gdx.input.isKeyPressed(Input.Keys.W)) translation.add(forward);
		if (Gdx.input.isKeyPressed(Input.Keys.S)) translation.sub(forward);
		if (Gdx.input.isKeyPressed(Input.Keys.D)) translation.add(right);
		if (Gdx.input.isKeyPressed(Input.Keys.A)) translation.sub(right);
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) translation.add(up);
		if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) translation.sub(up);
		translation.nor().scl(speed);
		mainCamera.translate(translation);
	}


	//---- ANDROID-VR-SPECIFIC ----//

	@Override
	public void onNewFrame(com.google.vrtoolkit.cardboard.HeadTransform paramHeadTransform) {

		// Set look direction from actual vr-headset look direction
		float[] vecComponents = new float[3];
		paramHeadTransform.getForwardVector(vecComponents, 0);

		// Invert all axes and swap x and z
		Vector lookDirection = new Vector(-vecComponents[2], -vecComponents[1], -vecComponents[0]);
		game.setLookDirection(lookDirection);
		System.out.println(lookDirection);

		// Update game logic
		updateGame();

		// Set camera position depending on jumper position
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));
	}

	@Override
	public void onDrawEye(com.google.vrtoolkit.cardboard.Eye eye) {

		if (mainCamera instanceof CardboardCamera) {
			CardboardCamera cardboardCamera = (CardboardCamera)mainCamera;

			cardboardCamera.setEyeViewAdjustMatrix(new Matrix4(eye.getEyeView()));
			float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
			cardboardCamera.setEyeProjection(new Matrix4(perspective));

			cardboardCamera.update();

			renderScene(cardboardCamera);

		} else {
			System.err.println("onDrawEye called and camera is not a CardboardCamera instance!");
		}
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

	//---- UTILITIES ----//

	private Vector3 libGdxVector(Vector vector) {
		return new Vector3(vector.getX(), vector.getY(), vector.getZ());
	}

	private Vector gameVector(Vector3 vector) {
		return new Vector(vector.x, vector.y, vector.z);
	}

}
