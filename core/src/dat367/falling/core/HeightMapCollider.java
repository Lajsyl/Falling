package dat367.falling.core;

import dat367.falling.math.Vector;
import dat367.falling.platform_abstraction.HeightMap;

public class HeightMapCollider extends Collider {
    private final HeightMap heightMap;
    private Vector baseCenterPosition;
    private float xDimension;
    private float zDimension;
    private float maxHeight;

    public HeightMapCollider(Positioned positioned, String name, HeightMap heightMap, Vector baseCenterPosition, float xDimension, float zDimension, float maxHeight) {
        super(positioned, name);
        this.heightMap = heightMap;
        this.baseCenterPosition = baseCenterPosition;
        this.xDimension = xDimension;
        this.zDimension = zDimension;
        this.maxHeight = maxHeight;
    }

    @Override
    public boolean collidesWith(Collider collider) {
        return Collider.areColliding(this, collider);
    }

    public boolean collidesWithPoint(Vector point) {
        return pointIsInsideXZBoundary(point) && getHeight(point.getX(), point.getZ()) >= point.getY();
    }

    public float getHeight(float x, float z) {
        int imageX = (int)(heightMap.getImageWidth() * ((x - baseCenterPosition.getX() - xDimension/2) / xDimension));
        int imageY = (int)(heightMap.getImageHeight() * ((z - baseCenterPosition.getZ() - zDimension/2) / zDimension));
        return heightMap.getBrightnessAtPixel(imageX, imageY) * maxHeight;
    }

    private boolean pointIsInsideXZBoundary(Vector point) {
        return point.getX() > baseCenterPosition.getX() - xDimension
            && point.getX() < baseCenterPosition.getX() + xDimension
            && point.getZ() > baseCenterPosition.getZ() - zDimension
            && point.getZ() < baseCenterPosition.getZ() + zDimension;
    }
}