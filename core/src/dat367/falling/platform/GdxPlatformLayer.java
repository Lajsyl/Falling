package dat367.falling.platform;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.CardBoardApplicationListener;
import com.badlogic.gdx.backends.android.CardboardCamera;
import com.badlogic.gdx.backends.android.ShakeListener;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.*;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.UBJsonReader;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.audio.CardboardAudioEngine;
import dat367.falling.core.*;
import dat367.falling.math.Matrix;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;
import dat367.falling.platform_abstraction.Model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
	private static final float Z_NEAR = 0.15f;//0.1f;
	private static final float Z_FAR = Ground.SCALE;

	private ResourceHandler resourceHandler = new ResourceHandler();
	private RenderHandler renderHandler;

	private Rotation desktopSimulatedHeadTransform;

	private CardboardAudioEngine cardboardAudioEngine;
	private ShakeListener shakeListener;

	public GdxPlatformLayer(boolean platformIsAndroid) {
		this.platformIsAndroid = platformIsAndroid;
	}

	public void setCardboardAudioEngine(CardboardAudioEngine cardboardAudioEngine) {
		this.cardboardAudioEngine = cardboardAudioEngine;
		resourceHandler.setCardboardAudioEngine(cardboardAudioEngine);
	}

	@Override
	public void create() {

		if (platformIsAndroid) {
			mainCamera = new CardboardCamera();
			resourceHandler.setupSoundEventHandling();
			if (PreJumpState.JUMP_IN_REAL_LIFE_TO_JUMP_FROM_PLANE) {
				setupShakeEventHandling();
			}
		} else {
			mainCamera = new PerspectiveCamera(90, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

			// Instead of getting the head transform from a VR device,
			// we simulate this on desktop with a rotation controlled with the mouse
			desktopSimulatedHeadTransform = new Rotation();

			if (USING_DEBUG_CAMERA) {
				// Set position first frame
				setDesktopCameraPosAndOrientation();
			}
		}



		game = new FallingGame();
		resourceHandler.loadResources(game.getCurrentJump().getResourceRequirements(), platformIsAndroid);
		renderHandler = new RenderHandler(resourceHandler, game, platformIsAndroid);

		mainCamera.near = Z_NEAR;
		mainCamera.far = Z_FAR;

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

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
		/*modelBatch.dispose();
		spriteBatch.dispose();
		for (String sound : preloadedSounds) {
			cardboardAudioEngine.unloadSoundFile(sound);
		}
		preloadedSounds.clear();*/
		// TODO: Dispose of all resources!
	}



	private void updateGame() {
		RenderQueue.clear();
		game.update(Gdx.graphics.getDeltaTime());
		resourceHandler.updateAnimations(Gdx.graphics.getDeltaTime());
	}

	private String getFallStateString() {
		return game.getCurrentJump().getJumper().getFallStateDebugString();
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

		if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
			Vector position = game.getCurrentJump().getJumper().getPosition();
			game.getCurrentJump().getJumper().setPosition(position.getX(), position.getY()-20, position.getZ());
		}//FOR DEBUGGING


		if (Gdx.input.isKeyJustPressed(Input.Keys.P)){
			game.screenClicked(true);
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

//			Vector3 currentLookDirection = libGdxVector(game.getLookDirection());
//			mainCamera.normalizeUp();
//			Vector3 lookDirection = currentLookDirection
//					.rotateRad(mainCamera.up, -dX)
//					.rotateRad(mainCamera.up.cpy().nor().crs(mainCamera.direction.cpy().nor()), dY);

			// TODONE: Replace above with rotating simulatedHeadTransform instead, set headRotation to combination of bodyRotation and simulatedHeadTransform,
			// TODONE: set camera to headTransform.

			Vector direction = desktopSimulatedHeadTransform.getDirection();
			Vector up = desktopSimulatedHeadTransform.getUp();
			desktopSimulatedHeadTransform = desktopSimulatedHeadTransform.rotate(up, -dX).rotate(direction.cross(up), -dY);
			direction = desktopSimulatedHeadTransform.getDirection();
			up = desktopSimulatedHeadTransform.getUp();


//			System.out.println(desktopSimulatedHeadTransform.getUp());

//			Vector3 lookAtVector = libGdxVector(game.getCurrentJump().getJumper().getPosition()).add(libGdxVector(newHeadRotation.getDirection()));
//			mainCamera.lookAt(lookAtVector);

//			mainCamera.direction.set(libGdxVector(newHeadRotation.getDirection()));
//			mainCamera.up.set(libGdxVector(newHeadRotation.getUp()));
		}

		Vector simulatedHeadUp = desktopSimulatedHeadTransform.getUp();
		Vector simulatedHeadForward = desktopSimulatedHeadTransform.getDirection();
		Vector simulatedHeadRight = simulatedHeadForward.cross(simulatedHeadUp);
		final float mouseSensitivity = 0.002f;
		final float rollSpeed = 15.0f;
		float rotationZ = 0.0f;
		if (Gdx.input.isKeyPressed(Input.Keys.Q)) rotationZ -= rollSpeed;
		if (Gdx.input.isKeyPressed(Input.Keys.E)) rotationZ += rollSpeed;
		desktopSimulatedHeadTransform = desktopSimulatedHeadTransform.rotate(simulatedHeadForward, rotationZ * mouseSensitivity);

		Rotation bodyRotation = game.getCurrentJump().getJumper().getBodyRotation();
		Rotation newHeadRotation = bodyRotation.rotate(desktopSimulatedHeadTransform);
		game.setJumperHeadRotation(newHeadRotation);

//		Rotation bodyRotation = game.getCurrentJump().getJumper().getBodyRotation();
//		Rotation newHeadRotation = bodyRotation.rotate(desktopSimulatedHeadTransform.rotate(game.getCurrentJump().getJumper().getAdjustmentRotation()));
//		game.setJumperHeadRotation(newHeadRotation);

//		System.out.println(game.getCurrentJump().getJumper().getBodyRotation().getDirection());
	}

	private void setDesktopCameraPosAndOrientation() {
		// Set camera position depending on jumper position
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));

		// Set camera orientation depending on jumper look direction
//		Vector jumperLookDirection = game.getCurrentJump().getJumper().getLookDirection();
//		Vector lookAtPoint = jumperHeadPosition.add(jumperLookDirection);
//		mainCamera.lookAt(libGdxVector(lookAtPoint));
		Rotation bodyRotation = game.getJumperBodyRotation();
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
	public void onNewFrame(HeadTransform paramHeadTransform) {

		// Update things that affect game logic
		game.setJumperHeadRotation(getCurrentHeadRotation(paramHeadTransform));
		if (Gdx.input.justTouched()) {
			game.screenClicked(true);
		}

		// Update game logic
		updateGame();

		// Update things that are affected by game logic
		updateCamera();
		updateHeadPlacementForPositionalSound(paramHeadTransform);
	}

	private void updateCamera() {
		Vector jumperHeadPosition = game.getCurrentJump().getJumper().getPosition();
		mainCamera.position.set(libGdxVector(jumperHeadPosition));
		Rotation bodyRotation = game.getJumperBodyRotation();
		Rotation adjustedBodyRotation = bodyRotation.rotate(game.getCurrentJump().getJumper().getAdjustmentRotation());
		mainCamera.direction.set(libGdxVector(adjustedBodyRotation.getDirection()));
		mainCamera.up.set(libGdxVector(adjustedBodyRotation.getUp()));
	}

	private void updateHeadPlacementForPositionalSound(HeadTransform paramHeadTransform) {
		Vector position = convertToCardboardCoordinateSystem(new Vector(mainCamera.position.x, mainCamera.position.y, mainCamera.position.z));
		cardboardAudioEngine.setHeadPosition(position.getX(), position.getY(), position.getZ());
//		cardboardAudioEngine.setHeadPosition(mainCamera.position.x, mainCamera.position.y, mainCamera.position.z);
		Rotation head = getCurrentHeadRotation(paramHeadTransform);
		Vector xAxis = head.getDirection();
		Vector yAxis = head.getUp();
		Vector zAxis = head.getRight();
//		Vector xAxis = head.getRight().scale(-1);
//		Vector yAxis = head.getUp();
//		Vector zAxis = head.getDirection();
		Quaternion headQuaternion = new Quaternion().setFromAxes(xAxis.getX(), xAxis.getY(), xAxis.getZ(),
				yAxis.getX(), yAxis.getY(), yAxis.getZ(),
				zAxis.getX(), zAxis.getY(), zAxis.getZ());
		cardboardAudioEngine.setHeadRotation(headQuaternion.x, headQuaternion.y, headQuaternion.z, headQuaternion.w);
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
//		return new Vector(x, y, z);

		return bodyRotation.getDirection().scale(x)
				.add(bodyRotation.getUp().scale(y))
				.add(bodyRotation.getDirection().cross(bodyRotation.getUp()).scale(z));
//		Rotation adjustedBodyRotation = bodyRotation.rotate(game.getCurrentJump().getJumper().getAdjustmentRotation());
//		return adjustedBodyRotation.getDirection().scale(x)
//				.add(adjustedBodyRotation.getUp().scale(y))
//				.add(adjustedBodyRotation.getDirection().cross(adjustedBodyRotation.getUp()).scale(z));
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

//		return upDirection;
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

	//screen clicks should release the parachute
	@Override
	public void onCardboardTrigger() {
		game.screenClicked(true);
	}

	// It seems that the cardboard coordinate system swaps x and z???
	private Vector convertToCardboardCoordinateSystem(Vector position) {
		return new Vector(position.getZ(), position.getY(), position.getX());
	}

	private void setupShakeEventHandling() {
		this.shakeListener.setOnShakeListener(new ShakeListener.OnShakeListener() {
			@Override
			public void onShake() {
				game.screenClicked(true);
			}
		});
	}

	public void setShakeListener(ShakeListener shakeListener) {
		this.shakeListener = shakeListener;
	}

	//---- UTILITIES ----//

	private Vector3 libGdxVector(Vector vector) {
		return new Vector3(vector.getX(), vector.getY(), vector.getZ());
	}


	private Vector gameVector(Vector3 vector) {
		return new Vector(vector.x, vector.y, vector.z);
	}
}
