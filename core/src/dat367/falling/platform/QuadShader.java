package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import dat367.falling.platform_abstraction.Quad;

public class QuadShader extends BaseShader {

    // The renderable used to create this shader, invalid after the call to init
    private Renderable renderable;

    private int textureUniform;
    private int modelMatrixUniform;
    private int viewProjectionMatrixUniform;
    private int uScaleUniform;
    private int vScaleUniform;
    private int maxDrawDistanceUniform;
    private int maxOpacityDistanceUniform;
    private int cameraPositionUniform;

    public QuadShader(Renderable renderable) {
        this.program = new ShaderProgram(
                Gdx.files.internal("quad.vertex.glsl"),
                Gdx.files.internal("quad.fragment.glsl")
        );
        this.renderable = renderable;
    }

    @Override
    public void init() {
        this.textureUniform = register("u_texture");
        this.modelMatrixUniform = register("u_modelMatrix");
        this.viewProjectionMatrixUniform = register("u_viewProjectionMatrix");
        this.uScaleUniform = register("u_uScale");
        this.vScaleUniform = register("u_vScale");
        this.maxDrawDistanceUniform = register("u_maxDrawDistance");
        this.maxOpacityDistanceUniform = register("u_maxOpacityDistance");
        this.cameraPositionUniform = register("u_cameraPosition");

        final ShaderProgram program = this.program;
        this.program = null;
        init(program, renderable);
        renderable = null;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
    }

    @Override
    public void render(Renderable renderable) {
        Quad quad = (Quad) renderable.userData;

        Matrix4 modelMatrix = renderable.worldTransform;
        Matrix4 viewProjectionMatrix = camera.combined;

        set(modelMatrixUniform, modelMatrix);
        set(viewProjectionMatrixUniform, viewProjectionMatrix);

        TextureAttribute textureAttribute = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);
        textureAttribute.textureDescription.texture.bind(0);
        set(textureUniform, 0);

        set(uScaleUniform, quad.getUvXScale());
        set(vScaleUniform, quad.getUvYScale());

        set(maxDrawDistanceUniform, quad.getMaxDrawDistance());
        set(maxOpacityDistanceUniform, quad.getMaxDrawDistance() - quad.getFadeOutDistance());

        set(cameraPositionUniform, camera.position);

        super.render(renderable);
    }

    @Override
    public void end() {
        Gdx.gl.glDisable(GL20.GL_BLEND);
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);
        super.end();
    }

    @Override
    public int compareTo(Shader other) {
        // TODO: Implement!
        return 0;
    }

    @Override
    public boolean canRender(Renderable renderable) {
        return QuadShader.canRenderRenderable(renderable);
    }

    public static boolean canRenderRenderable(Renderable renderable) {
        return renderable != null &&
                renderable.userData != null &&
                renderable.userData instanceof Quad;
    }

}
