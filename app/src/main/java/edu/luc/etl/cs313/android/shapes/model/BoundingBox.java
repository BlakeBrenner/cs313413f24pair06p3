package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    // TODO entirely your job (except onCircle)

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        Shape shape = f.getShape();
        return shape.accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        // Calculate the bounding box for each shape in the group
        for (Shape shape : g.getShapes()) {
            Location shapeBBox = shape.accept(this);
            Shape currentShape = shapeBBox.getShape();

            int x = shapeBBox.getX(); int y = shapeBBox.getY(); int width = 0; int height= 0;

            if (currentShape instanceof Rectangle) {
                Rectangle rect = (Rectangle) currentShape;
                width = rect.getWidth();
                height = rect.getHeight();

            }else if (currentShape instanceof Circle){
                Circle circle = (Circle) currentShape;
                int radius = circle.getRadius();
                width = radius;
                height = radius;

                // Adjust x and y for the bounding box (top-left corner of the circle)
                x = shapeBBox.getX() - radius;
                y = shapeBBox.getY() - radius;
            } else if (currentShape instanceof Polygon) {
                Polygon polygon = (Polygon) currentShape;
                for (Point vertex : polygon.getPoints()) {
                    int vertexX = vertex.getX();
                    int vertexY = vertex.getY();

                    minX = Math.min(minX, vertexX);
                    minY = Math.min(minY, vertexY);
                    maxX = Math.max(maxX, vertexX);
                    maxY = Math.max(maxY, vertexY);

                }

                x = minX;
                y = minY;
                width = maxX - minX;
                height = maxY - minY;
            }

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x + width);
            maxY = Math.max(maxY, y + height);
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));

    }

    @Override
    public Location onLocation(final Location l) {
        return l;
    }

    @Override

    public Location onRectangle(final Rectangle r) {
        final int width = r.getWidth();
        final int height = r.getHeight();
        return new Location(0, 0, new Rectangle(width, height));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        Shape shape = c.getShape();
        return shape.accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        Shape shape = o.getShape();
        return shape.accept(this);
    }

    @Override
    public Location onPolygon(final Polygon s) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        // Compute bounding box by checking all vertices
        for (Point vertex : s.getPoints()) {
            int x = vertex.getX();
            int y = vertex.getY();

            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }

        return new Location(minX, minY, new Rectangle(maxX - minX, maxY - minY));
    }
}
