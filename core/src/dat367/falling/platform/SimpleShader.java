package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import dat367.falling.platform_abstraction.Quad;

public class SimpleShader implements Shader {

    private ShaderProgram shaderProgram;

    private boolean hasBegun = false;
    private RenderContext renderContext;

    @Override
    public void init() {
        ShaderProgram.pedantic = true;

        shaderProgram = new ShaderProgram(Gdx.files.internal("simple.vertex.glsl"), Gdx.files.internal("simple.fragment.glsl"));
        if (!shaderProgram.isCompiled()) {
            throw new RuntimeException("simple shader doesn't compile: " + shaderProgram.getLog());
        }

        shaderProgram.enableVertexAttribute("a_position");
        shaderProgram.enableVertexAttribute("a_texCoord0");
    }

    @Override
    public int compareTo(Shader other) {
        return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
        // Just assume we can render any given object
        return true;
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
        shaderProgram.begin();

        context.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        context.setDepthTest(GL20.GL_LEQUAL);

        shaderProgram.setUniformMatrix("u_projViewTrans", camera.combined);

        hasBegun = true;
        renderContext = context;
    }

    @Override
    public void render(Renderable renderable) {
        if (!hasBegun) {
            throw new IllegalStateException("All render(..) calls must be issued after begin(..) and before end()");
        }

        shaderProgram.setUniformMatrix("u_worldTrans", renderable.worldTransform);

        if (renderable.material.has(TextureAttribute.Diffuse)) {
            TextureAttribute textureAttribute = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);

            // Bind the texture
            int boundPosition = renderContext.textureBinder.bind(textureAttribute.textureDescription);
            shaderProgram.setUniformi("u_diffuseTexture", boundPosition);
        }

        if (renderable.userData instanceof Quad) {
            Quad quad = (Quad) renderable.userData;

            // Depth mask if quad is opaque
            renderContext.setDepthMask(quad.isOpaque());

            shaderProgram.setUniformf("u_uvScale", new Vector2(quad.getUvXScale(), quad.getUvYScale()));
        }

        else /* if it's a normal model */ {

            renderContext.setDepthMask(true);
            shaderProgram.setUniformf("u_uvScale", new Vector2(1, 1));
        }

        // Perform actual render
        renderable.meshPart.render(shaderProgram, true);
    }

    @Override
    public void end() {
        hasBegun = false;
        renderContext = null;

        shaderProgram.end();
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }

}
