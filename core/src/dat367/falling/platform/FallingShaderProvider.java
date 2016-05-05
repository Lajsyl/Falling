package dat367.falling.platform;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.DefaultShaderProvider;

public class FallingShaderProvider extends DefaultShaderProvider {

    public static final DefaultShader.Config FALLING_CONFIG = new DefaultShader.Config();

    public FallingShaderProvider() {
        super(new DefaultShader.Config());
    }

    @Override
    protected Shader createShader (final Renderable renderable) {
        if (QuadShader.canRenderRenderable(renderable)) {
            return new QuadShader(renderable);
        } else {
            return super.createShader(renderable);
        }
    }
}
