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
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import dat367.falling.core.FallingGame;
import dat367.falling.math.Matrix;
import dat367.falling.math.Rotation;
import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.*;

public class RenderHandler {


    private boolean platformIsAndroid;
    private final boolean USING_DEBUG_CAMERA = false;

    private ModelBatch modelBatch;
    private Environment environment;

    private ResourceHandler resourceHandler;
    private FallingGame game;

    private SpriteBatch spriteBatch;
    private BitmapFont font;


    public RenderHandler(ResourceHandler resourceHandler, FallingGame game, boolean platformIsAndroid){
        this.resourceHandler = resourceHandler;
        this.game = game;
        this.platformIsAndroid = platformIsAndroid;

        // Create a new model batch that uses our custom shader provider
        modelBatch = new ModelBatch(new DefaultShaderProvider() {
            @Override
            protected Shader createShader(Renderable renderable) {
                return new FallingShader(renderable, config);
            }
        });

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 1.0f, 1.0f, 1.0f, 1.0f));

        spriteBatch = new SpriteBatch();
        font = new BitmapFont();
    }

    public void renderScene(Camera camera) {

        Gdx.gl.glClearColor(165/255f, 215/255f, 250/255f, 1.0f); // (Bright desaturated sky blue)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        render3D(camera);
        render2D(camera);

    }

    private void render3D(Camera camera){
        modelBatch.begin(camera);
        {
            Iterable<RenderTask> tasks = RenderQueue.getTasks();
            for (RenderTask task : tasks) {

                if (task instanceof ModelRenderTask) {
                    ModelRenderTask modelTask = (ModelRenderTask) task;
                    String modelFileName = modelTask.getModel().getModelFileName();

                    // Fetch model instance
                    if (resourceHandler.getModels().containsKey(modelFileName)) {
                        ModelInstance instance = resourceHandler.getModels().get(modelFileName);
                        instance.transform = new Matrix4()
                                .translate(libGdxVector(task.getPosition()))
                                //.setFromEulerAngles(task.getRotation().getX(), task.getRotation().getY(), task.getRotation().getZ())
                                .mul(libGdxRotationMatrix(task.getRotation().relativeTo(new Rotation())))
                                .scale(task.getScale().getX(), task.getScale().getY(), task.getScale().getZ());

                        instance.materials.first().set(IntAttribute.createCullFace(modelTask.getModel().getShouldCullFaces() ? GL20.GL_CW : GL20.GL_NONE));

                        modelBatch.render(instance, environment);
                    }
                }

                else if (task instanceof QuadRenderTask) {
                    QuadRenderTask quadTask = (QuadRenderTask) task;
                    String textureFileName = quadTask.getQuad().getTextureFileName();

                    // Fetch quad
                    if (resourceHandler.getQuadTextureAttributes().containsKey(textureFileName)) {
                        TextureAttribute currentTextureAttribute = resourceHandler.getQuadTextureAttributes().get(textureFileName);

                        // Render using the shared quadModel
                        ModelInstance sharedInstance = this.resourceHandler.getQuadModel();

                        // NOTE: Will overwrite previous attribute of the same type!
                        sharedInstance.materials.get(0).set(currentTextureAttribute);

                        sharedInstance.transform = new Matrix4();

                        // Scale for aspect ratio
                        if (quadTask.getQuad().shouldAspectRatioAdjust()) {
                            Float aspectRatio = quadTask.getQuad().getAspectRatio();
                            if (aspectRatio != null && aspectRatio != 0.0f) {
                                sharedInstance.transform.scl(aspectRatio, 1, 1);
                            }
                        }

                        // NOTE: No rotation (for now?)!
                        sharedInstance.transform = sharedInstance.transform
                                .translate(libGdxVector(task.getPosition()))
                                .scale(task.getScale().getX(), task.getScale().getY(), task.getScale().getZ());

                        // Make copy, since stuff like transform and materials would be shared if not
                        ModelInstance modelInstanceCopy = new ModelInstance(sharedInstance);

                        // Set quad as user data so that the shader can use its properties
                        modelInstanceCopy.userData = quadTask.getQuad();
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
            Iterable<GUITask> tasks = RenderQueue.getGUITasks();
            for(GUITask guiTask : tasks){
                if(guiTask instanceof GUITextTask){
                    GUITextTask textTask = (GUITextTask)guiTask;
                    float posX = textTask.getPosition().getX()*Gdx.graphics.getBackBufferWidth();
                    float posZ = textTask.getPosition().getZ()*Gdx.graphics.getBackBufferHeight();
                    if(textTask.shouldCenterHorizontal()){
                        final GlyphLayout layout = new GlyphLayout(font, textTask.getText());
                        posX -= layout.width/2f;
                    }

                    font.setColor(textTask.getColour().getX(), textTask.getColour().getY(), textTask.getColour().getZ(), 1);
                    font.draw(spriteBatch, textTask.getText(), posX, posZ);//, Gdx.graphics.getWidth(), Align.center, true );
                }
            }

            font.setColor(Color.CHARTREUSE);

            //TODO this should be removed at finish
            String debugText =
                    getFallStateString() + "\n" +
                            "Camera pos: " + gameVector(camera.position) + "\n" +
                            "Look dir: " + gameVector(camera.direction) + "\n\n" +
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
