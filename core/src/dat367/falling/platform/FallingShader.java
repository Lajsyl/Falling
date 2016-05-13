package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;

public class FallingShader extends DefaultShader {

    private static String getVertexSource() {
        return Gdx.files.internal("falling.vertex.glsl").readString();
    }

    private static String getFragmentSource() {
        return Gdx.files.internal("falling.fragment.glsl").readString();
    }

    public FallingShader(Renderable renderable) {
        this(renderable, new DefaultShader.Config());
    }

    public FallingShader(Renderable renderable, Config config) {
        super(renderable, config, createPrefix(renderable, config), getVertexSource(), getFragmentSource());
    }

    @Override
    public void init() {
        super.init();
    }
}
