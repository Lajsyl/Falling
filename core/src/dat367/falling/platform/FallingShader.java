package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Attributes;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.math.Vector2;
import dat367.falling.platform_abstraction.Quad;

public class FallingShader extends DefaultShader {

    private final static class Uniforms {
        private static int uvScale;
        private static int maxDrawDistance;
        private static int maxOpacityDistance;
    }

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
        super(renderable, config, createFullPrefix(renderable, config), getVertexSource(), getFragmentSource());

        // Register custom uniforms
        Uniforms.uvScale = register("u_uvScale");
        Uniforms.maxDrawDistance = register("u_maxDrawDistance");
        Uniforms.maxOpacityDistance = register("u_maxOpacityDistance");
    }

    private static String createFullPrefix(Renderable renderable, Config config) {
        String prefix = DefaultShader.createPrefix(renderable, config);

        prefix += "#ifndef cameraPositionFlag\n";
        prefix += "  #define cameraPositionFlag\n";
        prefix += "#endif //cameraPositionFlag\n";

        return prefix;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void render(Renderable renderable, Attributes combinedAttributes) {
        Quad quad = null;
        if (renderable != null && renderable.userData != null && renderable.userData instanceof Quad) {
            quad = (Quad) renderable.userData;
        }
        setCustomFallingUniforms(quad);

        super.render(renderable, combinedAttributes);
    }

    private void setCustomFallingUniforms(Quad quad) {
        final Vector2 unitVector = new Vector2(1, 1);

        // UV scale
        Vector2 uvScale = (quad != null)
                ? new Vector2(quad.getUvXScale(), quad.getUvYScale())
                : unitVector;
        set(Uniforms.uvScale, uvScale);

        // Quad distance fading
        if (quad != null && !quad.isOpaque()) {
            float maxOpacityDistance = quad.getMaxDrawDistance() - quad.getFadeOutDistance();
            set(Uniforms.maxOpacityDistance, maxOpacityDistance);
            set(Uniforms.maxDrawDistance, quad.getMaxDrawDistance());
        } else {
            final float drawDistance = 10000000; /* 10 million meters. Some arbitrary big number, so objects that shouldn't fade wont */
            set(Uniforms.maxOpacityDistance, drawDistance);
            set(Uniforms.maxDrawDistance, drawDistance);
        }
    }

}
