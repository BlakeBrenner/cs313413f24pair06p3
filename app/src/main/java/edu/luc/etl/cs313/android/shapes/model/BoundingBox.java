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
        int x = 0; int y = 0; int width = 0; int height= 0;

        // Calculate the bounding box for each shape in the group
        for (Shape shape : g.getShapes()) {
            Location shapeBBox = shape.accept(this);

            if(shape instanceof Rectangle) {
                Rectangle rect = new Rectangle(shapeBBox.getX(), shapeBBox.getY());
                 x = shapeBBox.getX();
                 y = shapeBBox.getY();
                 width = rect.getWidth();
                 height = rect.getHeight();
            }else if(shape instanceof Circle){
                height = ((Circle) shape).getRadius() * 2;
                width = ((Circle) shape).getRadius() * 2;
            }else{
                for (Point vertex : ((Polygon) shape).getPoints()) {
                    x = vertex.getX();
                    y = vertex.getY();
                }
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
        Location shapeBBox = l.getShape().accept(this);
        Rectangle rect = new Rectangle(shapeBBox.getX(), shapeBBox.getY());
        return new Location(l.getX() + shapeBBox.getX(), l.getY() + shapeBBox.getY(), rect);
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
