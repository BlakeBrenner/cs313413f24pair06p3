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
        //paint.setStyle(Style.STROKE);
    }

    @Override
    public Void onCircle(final Circle c) {
        canvas.drawCircle(0, 0, c.getRadius(), paint);
        return null;
    }

    @Override
    public Void onStrokeColor(final StrokeColor c) {
        paint.setColor(c.getColor());
        paint.setStyle(Style.STROKE);
        return null;
    }

    @Override
    public Void onFill(final Fill f) {
        paint.setStyle(Paint.Style.FILL);

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
        Shape shape = l.getShape();
        canvas.translate(l.getX(), l.getY());
        if (shape instanceof Rectangle) {
            canvas.drawRect(0, 0,  ((Rectangle) shape).getWidth(), ((Rectangle) shape).getHeight(), paint);
        }else if (shape instanceof Circle){
            canvas.drawCircle(0, 0, ((Circle)shape).getRadius(), paint);
        }else if( shape instanceof Polygon){
            float[] pts = new float[((Polygon)shape).getPoints().size() * 2];
            int i = 0;
            for (Point vertex : ((Polygon)shape).getPoints()) {
                pts[i++] = vertex.getX();
                pts[i++] = vertex.getY();
            }
            canvas.drawLines(pts, paint);
        }
        canvas.translate(-l.getX(), -l.getY());
        canvas.restore();
        return null;
    }

    @Override
    public Void onRectangle(final Rectangle r) {
        canvas.drawRect(0,0 , r.getHeight(),r.getWidth(), paint);
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

        float[] pts = new float[s.getPoints().size() * 2];
        int i = 0;
        for (Point vertex : s.getPoints()) {
            pts[i++] = vertex.getX();
            pts[i++] = vertex.getY();
        }
        canvas.drawLines(pts, paint);

        return null;
    }
}
