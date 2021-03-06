package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.core.FallingGame;
import dat367.falling.core.NotificationManager;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.RenderQueue;

/**
 * Responsibilities
 * Create game
 * ask game for resources
 * updateGame
 * when something in game wants to be drawn, draw it
 * sound, etc
 */

public class GdxPlatformLayer implements CardBoardApplicationListener {

	private boolean platformIsAndroid;
	private final boolean USING_DEBUG_CAMERA = false;

	private FallingGame game;
	private Camera mainCamera;
	private static final float Z_NEAR = 0.15f;
	private static final float Z_FAR = 30000.0f;

	private ResourceHandler resourceHandler = new ResourceHandler();
	private AudioHandler audioHandler  = new AudioHandler();
	private RenderHandler renderHandler;

	private Rotation desktopSimulatedHeadTransform;


	public static final float DOUBLEPRESS_TIME_MIN_SECONDS = 0.050f;
	public static final float DOUBLEPRESS_TIME_MAX_SECONDS = 0.500f;
	private float timeSinceLastScreenPress = 0.0f;

	public GdxPlatformLayer(boolean platformIsAndroid) {
		this.platformIsAndroid = platformIsAndroid;
	}

	public void setCardboardAudioEngine(CardboardAudioEngine cardboardAudioEngine) {
		audioHandler.setCardboardAudioEngine(cardboardAudioEngine);
	}

	@Override
	public void create() {
		if (platformIsAndroid) {
			mainCamera = new CardboardCamera();
			audioHandler.setupSoundEventHandling();
		} else {
			mainCamera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			// Instead of getting the head transform from a VR device,
			// we simulate this on desktop with a rotation controlled with the mouse
			desktopSimulatedHeadTransform = new Rotation();
		}

		setupRestartGameEventHandling();

		game = new FallingGame(); // Game must be initialized after soundEventHandling is setup but before sound files are loaded
		resourceHandler.loadResources(game.getCurrentJump().getResourceRequirements(), platformIsAndroid);
		audioHandler.loadAudio(game.getCurrentJump().getResourceRequirements(), platformIsAndroid);
		renderHandler = new RenderHandler(resourceHandler, game, platformIsAndroid);

		mainCamera.near = Z_NEAR;
		mainCamera.far = Z_FAR;

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		if (USING_DEBUG_CAMERA && !platformIsAndroid) {
			// Set position first frame
			setDesktopCameraPosAndOrientation();
		}
	}

	private void setupRestartGameEventHandling() {
		//Listen for restart events
		NotificationManager.getDefault().addObserver(FallingGame.BEFORE_GAME_RESTART_EVENT, new NotificationManager.EventHandler<FallingGame>() {
			@Override
			public void handleEvent(NotificationManager.Event<FallingGame> event) {
				audioHandler.stopAllLoopingSounds();
				NotificationManager.getDefault().clear();
				setupRestartGameEventHandling();
				if (platformIsAndroid) {
					audioHandler.setupSoundEventHandling();
				}
			}
		});
		NotificationManager.getDefault().addObserver(FallingGame.AFTER_GAME_RESTART_EVENT, new NotificationManager.EventHandler<FallingGame>() {
			@Override
			public void handleEvent(NotificationManager.Event<FallingGame> event) {
				resourceHandler.loadResources(game.getCurrentJump().getResourceRequirements(), platformIsAndroid);
				audioHandler.loadAudio(game.getCurrentJump().getResourceRequirements(), platformIsAndroid);
				renderHandler.reset();
				renderHandler.setupCrashedEventHandler();
			}
		});
	}


	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		if (platformIsAndroid) {
			audioHandler.pause();
		}
	}

	@Override
	public void resume() {
		if (platformIsAndroid) {
			audioHandler.resume();
		}
	}

	@Override
	public void dispose() {
	}

	private void updateGame() {
		RenderQueue.getDefault().clear();
		game.update(Gdx.graphics.getDeltaTime());
		resourceHandler.updateAnimations(Gdx.graphics.getDeltaTime());
		timeSinceLastScreenPress += Gdx.graphics.getDeltaTime();
	}

	private void screenClick() {
		NotificationManager.getDefault().registerEvent(FallingGame.SCREEN_TAP_EVENT, null);

		if (timeSinceLastScreenPress >= DOUBLEPRESS_TIME_MIN_SECONDS
				&& timeSinceLastScreenPress <= DOUBLEPRESS_TIME_MAX_SECONDS) {
			NotificationManager.getDefault().registerEvent(FallingGame.SCREEN_DOUBLE_TAP_EVENT, null);
		}
		timeSinceLastScreenPress = 0;
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

			setDesktopCameraPosAndOrientation();
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

		//FOR DEBUGGING
		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			Vector position = game.getCurrentJump().getJumper().getPosition();
			game.getCurrentJump().getJumper().setPosition(position.getX(), position.getY()-20, position.getZ());
		}

		if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
			screenClick();
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


			Vector direction = desktopSimulatedHeadTransform.getDirection();
			Vector up = desktopSimulatedHeadTransform.getUp();
			desktopSimulatedHeadTransform = desktopSimulatedHeadTransform.rotate(up, -dX).rotate(direction.cross(up), -dY);
		}

		Vector simulatedHeadForward = desktopSimulatedHeadTransform.getDirection();
		final float mouseSensitivity = 0.002f;
		final float rollSpeed = 15.0f;
		float rotationZ = 0.0f;
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) rotationZ -= rollSpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.E)) rotationZ += rollSpeed;
		desktopSimulatedHeadTransform = desktopSimulatedHeadTransform.rotate(simulatedHeadForward, rotationZ * mouseSensitivity);

		Rotation bodyRotation = game.getCurrentJump().getJumper().getBodyRotation();
		Rotation newHeadRotation = bodyRotation.rotate(desktopSimulatedHeadTransform);
		game.setJumperHeadRotation(newHeadRotation);
	}

	private void setDesktopCameraPosAndOrientation() {
		// Set camera position depending on jumper position
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));

		// Set camera orientation depending on jumper rotation
		Rotation headRotation = game.getCurrentJump().getJumper().getHeadRotation();

		mainCamera.direction.set(libGdxVector(headRotation.getDirection()));
		mainCamera.up.set(libGdxVector(headRotation.getUp()));
	}

	@Override
	public void render() {
		if (!platformIsAndroid) {
			desktopUpdate();

			Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			mainCamera.update();
			renderHandler.renderScene(mainCamera);
		}
	}

	private void handleDebugCameraControl() {
		final float mouseSensitivity = 0.1f;
		final float speed = 25f;
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

		game.getCurrentJump().getJumper().setPosition(mainCamera.position.x, mainCamera.position.y, mainCamera.position.z);
	}

	//---- ANDROID-VR-SPECIFIC ----//


	@Override
	public void onNewFrame(HeadTransform paramHeadTransform) {
		// Update things that affect game logic
		game.setJumperHeadRotation(getCurrentHeadRotation(paramHeadTransform));
		if (Gdx.input.justTouched()) {
			screenClick();
		}

		// Update game logic
		updateGame();

		// Update things that are affected by game logic
		updateCamera();
		Rotation head = getCurrentHeadRotation(paramHeadTransform);
		audioHandler.updateHeadPlacementForPositionalSound(mainCamera, head);
	}

	private void updateCamera() {
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));
		Rotation bodyRotation = game.getJumperBodyRotation();
		Rotation adjustedBodyRotation = bodyRotation.rotate(game.getCurrentJump().getJumper().getAdjustmentRotation());
		mainCamera.direction.set(libGdxVector(adjustedBodyRotation.getDirection()));
		mainCamera.up.set(libGdxVector(adjustedBodyRotation.getUp()));
	}

	private Rotation getCurrentHeadRotation(HeadTransform paramHeadTransform) {
		Rotation bodyRotation = game.getJumperBodyRotation();

		Rotation adjustedBodyRotation = bodyRotation.rotate(game.getCurrentJump().getJumper().getAdjustmentRotation());
		Rotation adjustedHeadRotation = new Rotation(getVRLookDirection(adjustedBodyRotation, paramHeadTransform),
				getVRUpVector(adjustedBodyRotation, paramHeadTransform));
		return adjustedHeadRotation;
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
		float z = (float)(-Math.sin(yaw)*Math.cos(pitch));
		return new Vector(x, y, z);
	}

	private Vector getVRLookDirection(Rotation bodyRotation, com.google.vrtoolkit.cardboard.HeadTransform paramHeadTransform) {
		// Since the look vector provided by getForwardVector() seems inaccurate,
		// compute it from the Euler angles instead
		float[] eulerAngles = new float[3];
		paramHeadTransform.getEulerAngles(eulerAngles, 0);
		double yaw = eulerAngles[1];
		double pitch = eulerAngles[0];
		float x = (float)(Math.cos(yaw)*Math.cos(pitch));
		float y = (float)(Math.sin(pitch));
		float z = (float)(-Math.sin(yaw)*Math.cos(pitch));

		return bodyRotation.getDirection().scale(x)
				.add(bodyRotation.getUp().scale(y))
				.add(bodyRotation.getDirection().cross(bodyRotation.getUp()).scale(z));
	}

	private Vector getVRUpVector(Rotation bodyRotation, com.google.vrtoolkit.cardboard.HeadTransform paramHeadTransform){
		float[] eulerAngles = new float[3];
		paramHeadTransform.getEulerAngles(eulerAngles, 0);
		double yaw = eulerAngles[1];
		double pitch = eulerAngles[0];
		double roll = eulerAngles[2];

		Vector up = new Vector(0, 1, 0);
		Vector yawLookDirection = new Vector((float) Math.cos(yaw), 0, (float) -Math.sin(yaw));
		Vector yawPitchLookDirection = getVRLookDirection(paramHeadTransform);
		Vector yawPitchUpDirection = yawLookDirection.scale((float)-Math.sin(pitch))
									.add(up.scale((float)Math.cos(pitch)));
		Vector yawPitchLeftDirection = yawPitchUpDirection.cross(yawPitchLookDirection);
		Vector upDirection = yawPitchLeftDirection.scale((float)Math.sin(roll))
							.add(yawPitchUpDirection.scale((float)Math.cos(roll)));

		float x = upDirection.getX();
		float y = upDirection.getY();
		float z = upDirection.getZ();
		return bodyRotation.getDirection().scale(x)
				.add(bodyRotation.getUp().scale(y))
				.add(bodyRotation.getDirection().cross(bodyRotation.getUp()).scale(z));
	}

	@Override
	public void onDrawEye(com.google.vrtoolkit.cardboard.Eye eye) {

		if (mainCamera instanceof CardboardCamera) {
			CardboardCamera cardboardCamera = (CardboardCamera)mainCamera;

			cardboardCamera.setEyeViewAdjustMatrix(new Matrix4(eye.getEyeView()));
			float[] perspective = eye.getPerspective(Z_NEAR, Z_FAR);
			cardboardCamera.setEyeProjection(new Matrix4(perspective));

			cardboardCamera.update();

			renderHandler.renderScene(cardboardCamera);

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
		screenClick();
	}

	//---- UTILITIES ----//

	// It seems that the cardboard coordinate system swaps x and z???
	private Vector convertToCardboardCoordinateSystem(Vector position) {
		return new Vector(position.getZ(), position.getY(), position.getX());
	}

	private Vector3 libGdxVector(Vector vector) {
		return new Vector3(vector.getX(), vector.getY(), vector.getZ());
	}
}
