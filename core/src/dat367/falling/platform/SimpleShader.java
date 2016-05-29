package dat367.falling.platform;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.IntAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import dat367.falling.platform_abstraction.Model;
import dat367.falling.platform_abstraction.Quad;

public class SimpleShader implements Shader {

    private ShaderProgram shaderProgram;

    private boolean hasBegun = false;
    private RenderContext renderContext;
    private Camera currentCamera;

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
        renderContext = context;
        currentCamera = camera;

        shaderProgram.begin();
        renderContext.begin();

        renderContext.setCullFace(GL20.GL_BACK);
        renderContext.setBlending(true, GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        renderContext.setDepthTest(GL20.GL_LEQUAL);

        shaderProgram.setUniformMatrix("u_projViewTrans", camera.combined);

        hasBegun = true;
    }

    @Override
    public void render(Renderable renderable) {
        if (!hasBegun) {
            throw new IllegalStateException("All render(..) calls must be issued after begin(..) and before end()");
        }

        Vector3 renderablePosition = new Vector3();
        renderablePosition = renderable.worldTransform.getTranslation(renderablePosition);

        shaderProgram.setUniformMatrix("u_worldTrans", renderable.worldTransform);
        shaderProgram.setUniformf("u_cameraPos", currentCamera.position);
        shaderProgram.setUniformf("u_objectPos", renderablePosition);

        // Assume back face culling (changes are optimized in the context)
        renderContext.setCullFace(GL20.GL_BACK);
        if (renderable.material.has(IntAttribute.CullFace)) {
            IntAttribute cullFace = (IntAttribute) renderable.material.get(IntAttribute.CullFace);
            renderContext.setCullFace(cullFace.value);
        }

        if (renderable.material.has(TextureAttribute.Diffuse)) {
            TextureAttribute textureAttribute = (TextureAttribute) renderable.material.get(TextureAttribute.Diffuse);

            // Bind the texture
            int boundPosition = renderContext.textureBinder.bind(textureAttribute.textureDescription);
            shaderProgram.setUniformi("u_diffuseTexture", boundPosition);
        }

        //
        // Set specific uniforms
        //
        if (renderable.userData instanceof Quad) {
            final Quad quad = (Quad) renderable.userData;
            setQuadUniforms(quad);

        } else if (renderable.userData instanceof Model) {
            final Model model = (Model) renderable.userData;
            setModelUniforms(model, renderablePosition);
        }

        // Perform actual render
        renderable.meshPart.render(shaderProgram, true);
    }

    private void setQuadUniforms(Quad quad) {
        // Depth mask if quad is opaque
        renderContext.setDepthMask(quad.isOpaque());

        shaderProgram.setUniformf("u_uvScale", new Vector2(quad.getUvXScale(), quad.getUvYScale()));

        float maxOpacityDistance = quad.getMaxDrawDistance() - quad.getFadeOutDistance();
        shaderProgram.setUniformf("u_maxDrawDistance", quad.getMaxDrawDistance());
        shaderProgram.setUniformf("u_maxOpacityDistance", maxOpacityDistance);
    }

    private void setModelUniforms(Model model, Vector3 renderablePosition) {
        renderContext.setDepthMask(true);
        shaderProgram.setUniformf("u_uvScale", new Vector2(1, 1));

        float maxDrawDistance;
        float maxOpacityDistance;

        if (model.shouldFadeOut()) {
            maxDrawDistance = model.getMaxDrawDistance();
            maxOpacityDistance = model.getMaxDrawDistance() - model.getFadeOutDistance();
        } else {
            // Some arbitrary big number, so objects that shouldn't fade wont.
            final float drawDistance = 10000000; /* 10 million meters. */

            maxDrawDistance = drawDistance;
            maxOpacityDistance = drawDistance;

            // Do depth mask, since it's completely opaque
            renderContext.setDepthMask(true);
        }

        shaderProgram.setUniformf("u_maxDrawDistance", maxDrawDistance);
        shaderProgram.setUniformf("u_maxOpacityDistance", maxOpacityDistance);
    }

    @Override
    public void end() {
        hasBegun = false;;

        renderContext.end();
        shaderProgram.end();

        currentCamera = null;
        renderContext = null;
    }

    @Override
    public void dispose() {
        shaderProgram.dispose();
    }

}
