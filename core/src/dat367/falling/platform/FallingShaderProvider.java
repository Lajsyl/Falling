package dat367.falling.platform;

import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.utils.BaseShaderProvider;

public class FallingShaderProvider extends BaseShaderProvider {

    @Override
    protected Shader createShader (final Renderable renderable) {
        if (QuadShader.canRenderRenderable(renderable)) {
            return new QuadShader(renderable);
        } else {
            return new DefaultShader(renderable);
        }
    }
}
