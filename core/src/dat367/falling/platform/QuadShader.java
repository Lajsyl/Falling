package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.BaseShader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class QuadShader extends BaseShader {

    public static final String QUAD_INDENTIFIER = "renderable-quad";

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
                renderable.userData.equals(QUAD_INDENTIFIER);
    }

}
