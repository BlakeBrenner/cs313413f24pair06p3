package edu.luc.etl.cs313.android.shapes.android;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.luc.etl.cs313.android.shapes.model.*;

/**
 * A Visitor for drawing a shape to an Android canvas.
 */
public class Draw implements Visitor<Void> {

    // TODO entirely your job (except onCircle)

    private final Canvas canvas;

    private final Paint paint;

    public Draw(final Canvas canvas, final Paint paint) {
        this.canvas = canvas; // FIXME
        this.paint = paint; // FIXME
        paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        //int oldColor = paint.getColor();
       // Paint.Style oldStyle = paint.getStyle();

        if (c.getColor() != 0) {
            paint.setColor(c.getColor());
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(c.getColor());
        }
        c.getShape().accept(this);
        paint.setStyle(null);

        if (c.getColor() != 0) {
            paint.setColor(c.getColor());
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setColor(c.getColor());
        }

        //paint.setColor(oldColor); // Restore previous color
        //paint.setStyle(oldStyle);  // Restore previous style
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        //Paint.Style oldStyle = paint.getStyle();
        paint.setStyle(Paint.Style.FILL);
        f.getShape().accept(this);
        //paint.setStyle(oldStyle);// restores style
        return null;
    }

    @Override
    public Void onGroup(final Group g) {

        for(Shape shape : g.getShapes()){
            shape.accept(this);
        }
        return null;
    }

    @Override
    public Void onLocation(final Location l) {
        canvas.save(); // Save the current canvas state
        l.getShape().accept(this);
        canvas.translate(l.getX(), l.getY());
        l.getShape().accept(this);
        canvas.save(); // Save the current canvas state
        canvas.translate(-l.getX(),-l.getY());
        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0,0 , r.getWidth(),r.getHeight(), paint);
        return null;
    }

    @Override
    public Void onOutline(Outline o) {
        paint.setStyle(Style.STROKE);
        o.getShape().accept(this);
        return null;
    }

    @Override
    public Void onPolygon(final Polygon s) {
        int pointCount = s.getPoints().size(); // Assuming getPoints() returns a List<Point>
        float[] pts = new float[pointCount * 2];

        int i = 0;
        for (Point vertex : s.getPoints()) {
            pts[i++] = vertex.getX(); // X coordinate of the vertex
            pts[i++] = vertex.getY(); // Y coordinate of the vertex
        }

        // Draw the polygon using a path to connect the points
        if (pointCount > 0) {
            canvas.drawLines(pts, paint); // Draw lines connecting the points

            // Optionally close the polygon by drawing a line from the last point to the first
            canvas.drawLine(pts[0], pts[1], pts[pointCount * 2 - 2], pts[pointCount * 2 - 1], paint);
        }
        return null;
    }
}
