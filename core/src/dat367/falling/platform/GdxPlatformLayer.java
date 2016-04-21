package dat367.falling.platform;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import dat367.falling.core.FallingGame;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

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
	private static final float Z_FAR = 10000.0f;

	private UBJsonReader jsonReader = new UBJsonReader();
	private G3dModelLoader modelLoader = new G3dModelLoader(jsonReader);

	private ModelBatch modelBatch;
	private Map<String, ModelInstance> models = new HashMap<String, ModelInstance>();
	private Map<String, TextureAttribute> quadTextureAttributes = new HashMap<String, TextureAttribute>();
	private ModelInstance quadModel;
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

		// Create the quad model if quads are required
		if (resourceRequirements.getQuads().size() > 0) {

			ModelBuilder modelBuilder = new ModelBuilder();
			final long attributes = VertexAttributes.Usage.Position |
					VertexAttributes.Usage.Normal |
					VertexAttributes.Usage.TextureCoordinates;

			com.badlogic.gdx.graphics.g3d.Model quadModel = modelBuilder.createRect(
					// Corners
					-1, 0, -1,
					-1, 0, +1,
					+1, 0, +1,
					+1, 0, -1,

					// Normal
					0, 1, 0,

					// Material
					new Material(
							// (Just add a blend & cull-face attribute, the texture attribute will be set for every render)
							new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA),
							IntAttribute.createCullFace(GL20.GL_NONE)
					),

					// Attributes
					attributes
			);
			this.quadModel = new ModelInstance(quadModel);
		}

		for (Quad quad : resourceRequirements.getQuads()) {
			String textureFileName = quad.getTextureFileName();
			if (!quadTextureAttributes.containsKey(textureFileName)) {
				FileHandle fileHandle = Gdx.files.internal(textureFileName);
				Texture quadTexture = new Texture(fileHandle, quad.shouldUseMipMaps());
				TextureAttribute quadTextureAttribute = TextureAttribute.createDiffuse(quadTexture);
				quadTextureAttributes.put(textureFileName, quadTextureAttribute);

				// Report aspect ratio back to Quad
				quad.setAspectRatio((float) quadTexture.getWidth() / (float) quadTexture.getHeight());
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
		Gdx.gl.glClearColor(165/255f, 215/255f, 250/255f, 1.0f); // (Bright desaturated sky blue)
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		// Render 3D
		modelBatch.begin(camera);
		{
			for (RenderTask task : RenderQueue.getTasks()) {

				if (task instanceof ModelRenderTask) {
					ModelRenderTask modelTask = (ModelRenderTask) task;
					String modelFileName = modelTask.getModel().getModelFileName();

					// Fetch model instance
					if (models.containsKey(modelFileName)) {
						ModelInstance instance = models.get(modelFileName);

						instance.transform = new Matrix4()
								.setFromEulerAngles(task.getOrientation().getX(), task.getOrientation().getY(), task.getOrientation().getZ())
								.scale(task.getScale().getX(), task.getScale().getY(), task.getScale().getZ())
								.translate(libGdxVector(task.getPosition()));


						modelBatch.render(instance, environment);
					}
				}

				else if (task instanceof QuadRenderTask) {
					QuadRenderTask quadTask = (QuadRenderTask) task;
					String textureFileName = quadTask.getQuad().getTextureFileName();

					// Fetch quad
					if (quadTextureAttributes.containsKey(textureFileName)) {
						TextureAttribute currentTextureAttribute = quadTextureAttributes.get(textureFileName);

						// Render using the shared quadModel
						ModelInstance sharedInstance = this.quadModel;

						// NOTE: Will overwrite previous attribute of the same type!
						sharedInstance.materials.get(0).set(currentTextureAttribute);

						// NOTE: No rotation (for now?)!
						sharedInstance.transform = new Matrix4()
								.scale(task.getScale().getX(), task.getScale().getY(), task.getScale().getZ())
								.translate(libGdxVector(task.getPosition()));

						// Scale for aspect ratio
						if (quadTask.getQuad().shouldAspectRatioAdjust()) {
							Float aspectRatio = quadTask.getQuad().getAspectRatio();
							if (aspectRatio != null && aspectRatio != 0.0f) {
								sharedInstance.transform.scl(aspectRatio, 1, 1);
							}
						}

						modelBatch.render(new ModelInstance(sharedInstance), environment);
					}
				}

			}
		}
		modelBatch.end();

		// Render 2D
		spriteBatch.begin();
		{
			String debugText =
					"Camera pos: " + camera.position + "\n" +
					"Look dir: " + new Vector(camera.direction.x, camera.direction.y, camera.direction.z) + "\n\n" +
					"Acceleration: " + game.getCurrentJump().getJumper().getAcceleration() + "\n" +
					"Speed: " + game.getCurrentJump().getJumper().getVelocity();

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

		game.setLookDirection(getVRLookDirection(paramHeadTransform));

		// Update game logic
		updateGame();

		// Set camera position depending on jumper position
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));
	}

	private Vector getVRLookDirection(com.google.vrtoolkit.cardboard.HeadTransform paramHeadTransform) {
		// Since the look vector provided by getForwardVector() seems inaccurate,
		// compute it from the Euler angles instead
		float[] eulerAngles = new float[3];
		paramHeadTransform.getEulerAngles(eulerAngles, 0);
		double yaw = eulerAngles[1];
		double pitch = eulerAngles[0];
		float x = (float)(Math.cos(yaw)*Math.cos(pitch));
		float y = (float)(Math.sin(pitch));
		float z = -(float)(Math.sin(yaw)*Math.cos(pitch));
		return new Vector(x, y, z);
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
