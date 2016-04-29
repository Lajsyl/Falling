package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import dat367.falling.platform_abstraction.Quad;

public class QuadShader extends BaseShader {

    // NOTE: These will be invalid after init()!
    private ShaderProgram program;
    private Renderable renderable;

    public QuadShader(Renderable renderable) {
        this.program = new ShaderProgram(
                Gdx.files.internal("quad.vertex.glsl"),
                Gdx.files.internal("quad.fragment.glsl")
        );
        this.renderable = renderable;
    }

    @Override
    public void init() {
        final ShaderProgram program = this.program;
        this.program = null;
        init(program, renderable);
        renderable = null;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        super.begin(camera, context);
    }

    @Override
    public void render(Renderable renderable) {
        Quad quad = (Quad) renderable.userData;

        // TODO: Set shader uniforms from the quad proprties
        //float uvXScale = quad.getUvXScale();

        super.render(renderable);
    }

    @Override
    public void end() {
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
