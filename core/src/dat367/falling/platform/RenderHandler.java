package dat367.falling.platform;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.DepthTestAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import dat367.falling.core.CrashedState;
import dat367.falling.core.FallingGame;
import dat367.falling.core.NotificationManager;
import dat367.falling.core.World;
import dat367.falling.math.Matrix;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class RenderHandler {

    private final boolean DEBUG_MODE = false;

    private boolean platformIsAndroid;

    private ModelBatch modelBatch;
    private Environment environment;

    private ResourceHandler resourceHandler;
    private FallingGame game;

    private SpriteBatch spriteBatch;

    private BitmapFont debugFont;
    private BitmapFont smallFont;
    private BitmapFont bigFont;

    private boolean hasCrashed = false;

    public RenderHandler(ResourceHandler resourceHandler, FallingGame game, boolean platformIsAndroid){
        this.resourceHandler = resourceHandler;
        this.game = game;
        this.platformIsAndroid = platformIsAndroid;

        // Create a new model batch that only uses the simple shader (for everything)
        modelBatch = new ModelBatch(new ShaderProvider() {
            SimpleShader simpleShader = null;
            @Override
            public Shader getShader(Renderable renderable) {
                if (simpleShader == null) {
                    simpleShader = new SimpleShader();
                    simpleShader.init();
                }
                return simpleShader;
            }

            @Override
            public void dispose() {
                simpleShader.dispose();
            }
        });

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f));

        spriteBatch = new SpriteBatch();

        debugFont = new BitmapFont();
        smallFont = new BitmapFont(Gdx.files.internal("quadrangle_25.fnt"));
        bigFont = new BitmapFont(Gdx.files.internal("quadrangle_32.fnt"));

       setupCrashedEventHandler();
    }

    public void setupCrashedEventHandler() {
        NotificationManager.getDefault().addObserver(CrashedState.PLAYER_HAS_CRASHED_EVENT_ID, new NotificationManager.EventHandler() {
            @Override
            public void handleEvent(NotificationManager.Event event) {
                hasCrashed = true;
            }
        });
    }

    public void renderScene(Camera camera) {
        if(!hasCrashed){
            Vector c = World.ATMOSPHERE_COLOR;
            Gdx.gl.glClearColor(c.getX(), c.getY(), c.getZ(), 1.0f);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

            render3D(camera);
        } else {
            Gdx.gl.glClearColor(0f, 0f, 0f, 1.0f); // Black
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        }


        render2D(camera);
    }

    private void render3D(Camera camera){
        modelBatch.begin(camera);
        {
            Iterable<RenderTask> tasks = RenderQueue.getDefault().getTasks();
            for (RenderTask task : tasks) {

                if (task instanceof ModelRenderTask) {
                    ModelRenderTask modelTask = (ModelRenderTask) task;
                    String modelFileName = modelTask.getModel().getModelFileName();

                    // Fetch model instance
                    if (resourceHandler.getModels().containsKey(modelFileName)) {
                        ModelInstance instance = resourceHandler.getModels().get(modelFileName);
                        instance.transform = new Matrix4()
                                .translate(libGdxVector(task.getPosition()))
                                .mul(libGdxRotationMatrix(task.getRotation().relativeTo(new Rotation())))
                                .scale(task.getScale().getX(), task.getScale().getY(), task.getScale().getZ());

                        // Set Model as user data so that the shader can use its properties
                        instance.userData = modelTask.getModel();

                        modelBatch.render(instance, environment);
                    }
                }

                else if (task instanceof QuadRenderTask) {
                    QuadRenderTask quadTask = (QuadRenderTask) task;
                    String textureFileName = quadTask.getQuad().getTextureFileName();

                    // Fetch quad
                    if (resourceHandler.getQuadTextureAttributes().containsKey(textureFileName)) {
                        TextureAttribute currentTextureAttribute = resourceHandler.getQuadTextureAttributes().get(textureFileName);

                        // Make copy, since stuff like transform and materials would be shared if not
                        ModelInstance sharedInstance = resourceHandler.getQuadModel();
                        ModelInstance modelInstanceCopy = new ModelInstance(sharedInstance);

                        // Set quad as user data so that the shader can use its properties
                        modelInstanceCopy.userData = quadTask.getQuad();

                        // NOTE: Will overwrite previous attribute of the same type!
                        modelInstanceCopy.materials.get(0).set(currentTextureAttribute);
                        modelInstanceCopy.materials.first().set(new DepthTestAttribute(quadTask.getQuad().isOpaque()));

                        modelInstanceCopy.transform = new Matrix4();

                        // Scale for aspect ratio
                        if (quadTask.getQuad().shouldAspectRatioAdjust()) {
                            Float aspectRatio = quadTask.getQuad().getAspectRatio();
                            if (aspectRatio != null && aspectRatio != 0.0f) {
                                modelInstanceCopy.transform.scl(aspectRatio, 1, 1);
                            }
                        }

                        // NOTE: No rotation!
                        modelInstanceCopy.transform = modelInstanceCopy.transform
                                .translate(libGdxVector(task.getPosition()))
                                .scale(task.getScale().getX(), task.getScale().getY(), task.getScale().getZ());

                        modelBatch.render(modelInstanceCopy, environment);
                    }
                }

            }
        }
        modelBatch.end();
    }

    private void render2D(Camera camera){
        spriteBatch.begin();
        {
            Iterable<GUITask> tasks = RenderQueue.getDefault().getGUITasks();
            for(GUITask guiTask : tasks){
                if(guiTask instanceof GUITextTask){
                    GUITextTask textTask = (GUITextTask)guiTask;
                    BitmapFont font = textTask.isBigSize() ? bigFont : smallFont;
                    float posX = textTask.getPosition().getX()*Gdx.graphics.getWidth();
                    float posZ = textTask.getPosition().getZ()*Gdx.graphics.getHeight();
                    if(textTask.shouldCenterHorizontal()){
                        final GlyphLayout layout = new GlyphLayout(font, textTask.getText());
                        posX -= layout.width/2f;
                    }

                    int edgeThickness = 1;//textTask.isBigSize() ? 2 : 1;
                    // Draw black text edges
                    font.setColor(0,0,0, 1);
                    font.draw(spriteBatch, textTask.getText(), posX+edgeThickness, posZ+edgeThickness);
                    font.draw(spriteBatch, textTask.getText(), posX+edgeThickness, posZ+0);
                    font.draw(spriteBatch, textTask.getText(), posX+edgeThickness, posZ-edgeThickness);
                    font.draw(spriteBatch, textTask.getText(), posX+0, posZ-edgeThickness);
                    font.draw(spriteBatch, textTask.getText(), posX-edgeThickness, posZ-edgeThickness);
                    font.draw(spriteBatch, textTask.getText(), posX-edgeThickness, posZ+0);
                    font.draw(spriteBatch, textTask.getText(), posX-edgeThickness, posZ+edgeThickness);
                    font.draw(spriteBatch, textTask.getText(), posX+0, posZ+edgeThickness);
                    // Draw text fill color
                    font.setColor(textTask.getColour().getX(), textTask.getColour().getY(), textTask.getColour().getZ(), 1);
                    font.draw(spriteBatch, textTask.getText(), posX, posZ);
                }
            }


            if (DEBUG_MODE) {
                String debugText =
                        getFallStateString() + "\n" +
                                "Camera pos: " + gameVector(camera.position) + "\n" +
                                "Look dir: " + gameVector(camera.direction) + "\n\n" +
                                "Acceleration: " + game.getCurrentJump().getJumper().getAcceleration() + "\n" +
                                "Speed: " + game.getCurrentJump().getJumper().getVelocity() + "\n" +
                                "                                                                                                                        " +
                                "FPS: " + Gdx.graphics.getFramesPerSecond();

                debugFont.setColor(Color.CHARTREUSE);
                debugFont.draw(spriteBatch, debugText, 50, Gdx.graphics.getHeight() - 60);
            }

            // Draw crosshair etc. for desktop control (if not using debug camera
            if (!platformIsAndroid && !DEBUG_MODE) {
                Color color = debugFont.getColor().cpy();
                {
                    debugFont.setColor(Color.RED);
                    debugFont.draw(spriteBatch, "o",
                            Gdx.graphics.getBackBufferWidth() / 2.0f - 5,
                            Gdx.graphics.getBackBufferHeight() / 2.0f + 8);

                    debugFont.draw(spriteBatch, "*",
                            Gdx.input.getX() - 4,
                            Gdx.graphics.getBackBufferHeight() - Gdx.input.getY() + 4);
                }
                debugFont.setColor(color);
            }
        }
        spriteBatch.end();
    }

    public void reset() {
        this.hasCrashed = false;
    }

    private String getFallStateString() {
        return game.getCurrentJump().getJumper().getFallStateDebugString();
    }

    private Vector3 libGdxVector(Vector vector) {
        return new Vector3(vector.getX(), vector.getY(), vector.getZ());
    }

    private Vector gameVector(Vector3 vector) {
        return new Vector(vector.x, vector.y, vector.z);
    }

    private Matrix4 libGdxRotationMatrix(Rotation rotation){
        Matrix rotMatrix = rotation.getRotationMatrix();

        Vector col1 = rotMatrix.getColumn1();
        Vector col2 = rotMatrix.getColumn2();
        Vector col3 = rotMatrix.getColumn3();

        float[] values = { col1.getX(), col2.getX(), col3.getX(), 0,
                col1.getY(), col2.getY(), col3.getY(), 0,
                col1.getZ(), col2.getZ(), col3.getZ(), 0,
                0, 0, 0, 1};

        return new Matrix4(values);
    }
}
