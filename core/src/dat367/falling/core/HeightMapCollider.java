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
        float imageX = ((heightMap.getImageWidth()-1) * ((x - (baseCenterPosition.getX() - xDimension/2)) / xDimension));
        float imageY = ((heightMap.getImageHeight()-1) * ((z - (baseCenterPosition.getZ() - zDimension / 2)) / zDimension));
        return baseCenterPosition.getY() + heightMap.getInterpolatedBrightnessAt(imageX, imageY) * maxHeight;
    }

    public boolean pointIsInsideXZBoundary(Vector point) {
        return point.getX() >= baseCenterPosition.getX() - xDimension/2
            && point.getX() <  baseCenterPosition.getX() + xDimension/2
            && point.getZ() >= baseCenterPosition.getZ() - zDimension/2
            && point.getZ() <  baseCenterPosition.getZ() + zDimension/2;
    }

    public Vector getBasePosition() {
        return baseCenterPosition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeightMapCollider that = (HeightMapCollider) o;

        if (Float.compare(that.xDimension, xDimension) != 0) return false;
        if (Float.compare(that.zDimension, zDimension) != 0) return false;
        if (Float.compare(that.maxHeight, maxHeight) != 0) return false;
        if (heightMap != null ? !heightMap.equals(that.heightMap) : that.heightMap != null) return false;
        return baseCenterPosition != null ? baseCenterPosition.equals(that.baseCenterPosition) : that.baseCenterPosition == null;

    }

    @Override
    public int hashCode() {
        int result = heightMap != null ? heightMap.hashCode() : 0;
        result = 31 * result + (baseCenterPosition != null ? baseCenterPosition.hashCode() : 0);
        result = 31 * result + (xDimension != +0.0f ? Float.floatToIntBits(xDimension) : 0);
        result = 31 * result + (zDimension != +0.0f ? Float.floatToIntBits(zDimension) : 0);
        result = 31 * result + (maxHeight != +0.0f ? Float.floatToIntBits(maxHeight) : 0);
        return result;
    }
}