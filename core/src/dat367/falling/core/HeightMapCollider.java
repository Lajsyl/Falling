package dat367.falling.core;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;

public class HeightMapCollider extends Collider {
    private BufferedImage image;
    private float[][] pixelBrightness;

    public HeightMapCollider(Positioned positioned, String name, String heightMapFileName) {
        super(positioned, name);
        try {
            image = ImageIO.read(HeightMapCollider.class.getResource(heightMapFileName));
            int width = image.getWidth();
            int height = image.getHeight();

            pixelBrightness = new float[height][width];

//            int[] data = new int[height * width];
            final byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            assert image.getColorModel().getNumComponents() == 1; // Assert grayscale image
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixelBrightness[y][x] = data[y * width + x];
                }
            }
            // TODO: CONTINUE HERE


//            image.getData()
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float getHeight(float x, float z) {
        return 0;//image.getData();
    }

    @Override
    public boolean collidesWith(Collider collider) {
        return Collider.areColliding(this, collider);
    }

}