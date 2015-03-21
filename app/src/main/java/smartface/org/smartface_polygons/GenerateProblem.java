package smartface.org.smartface_polygons;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Button;

import smartface.org.smartface_polygons.geometry.Problem;
import smartface.org.smartface_polygons.geometry.LineSegment;
import smartface.org.smartface_polygons.geometry.Point;

public class GenerateProblem extends Activity {
  String TAG = "GenerateProblem";
  public class ShowProblem extends SurfaceView implements SurfaceHolder.Callback, Runnable {
    SurfaceHolder holder;
    boolean isDrawing;
    Thread drawingThread;
    Problem problem;

    Paint lines;
    Paint points;
    Paint onPolyPoints;
    Paint outsidePoints;
    Paint insidePoints;
    float point_radius = 20f;

    boolean showSolution = false;

    public ShowProblem (Context context, Problem p) {
      super(context);
      holder = getHolder();
      isDrawing = true;
      holder.addCallback(this);
      this.problem = p;

      lines = new Paint();
      lines.setColor(0xff000000);
      lines.setStyle(Paint.Style.STROKE);
      lines.setStrokeWidth(3);

      points = new Paint();
      points.setColor(0xff000000);
      points.setStyle (Paint.Style.FILL);

      onPolyPoints = new Paint(points);
      onPolyPoints.setColor(0xffffff00);
      outsidePoints = new Paint(points);
      outsidePoints.setColor(0xffff0000);
      insidePoints = new Paint(points);
      insidePoints.setColor(0xff00ff00);
    }

    int width, height;

    boolean drawing = false;
    public void run () {
      while (drawing) {
        Canvas c = holder.lockCanvas();
        if (c != null) {
          onDraw(c);
          holder.unlockCanvasAndPost(c);
        }
      }
    }

    @Override
    public void onDraw (Canvas c) {
      c.drawRGB(0xff, 0xff, 0xff);

      for (LineSegment l : problem.poly) {
        c.drawLine (
                (float)(l.start.x) * width, (float)(l.start.y) * height,
                (float)(l.end.x) * width, (float)(l.end.y) * height,
                lines);
      }

      if (showSolution) {
        problem.solve();

        for (Point p : problem.outside){
          c.drawCircle((float) p.x * width, (float) p.y * height, point_radius, outsidePoints);
        }

        for (Point p : problem.inside) {
          c.drawCircle((float) p.x * width, (float) p.y * height, point_radius, insidePoints);

        }

        for (Point p : problem.onPoly) {
          c.drawCircle((float) p.x * width, (float) p.y * height, point_radius, onPolyPoints);
        }
      } else {
        for (Point p : problem.points) {
          c.drawCircle((float) p.x * width, (float) p.y * height, point_radius, points);
        }
      }

      c.save();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      drawingThread = new Thread (this);
      drawing = true;
      drawingThread.start ();
      invalidate ();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
      this.width = width;
      this.height = height;
      invalidate();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
      boolean joined = false;
      drawing = false;
      while (!joined) {
        try {
          drawingThread.join ();
          joined = true;
        } catch (InterruptedException e) {}
      }
    }
  }

  ShowProblem canvas;
  Problem p;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    int points = Integer.parseInt ((String)(getIntent().getExtras().get("points")));
    int lines = Integer.parseInt ((String)(getIntent().getExtras().get("lines")));
    p = new Problem (lines, points);

    canvas = new ShowProblem (this, p);

    View v = View.inflate(this, R.layout.activity_generate_problem, null);
    FrameLayout canvasFrame = (FrameLayout)(v.findViewById(R.id.showProblem));

    if (canvasFrame == null) Log.e(TAG, "WHAT");
    canvas.setLayoutParams(
            new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
    );
    canvasFrame.addView(canvas);
    setContentView(v);
  }

  public void solveProblem(View v) {
    if (canvas.showSolution) {
      canvas.showSolution = false;
      ((Button)v).setText ("Solve!");
    } else {
      canvas.showSolution = true;
      ((Button)v).setText ("Hide");
    }
  }
}
