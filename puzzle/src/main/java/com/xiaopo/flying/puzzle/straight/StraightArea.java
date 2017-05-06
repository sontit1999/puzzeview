package com.xiaopo.flying.puzzle.straight;

import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import com.xiaopo.flying.puzzle.Area;
import com.xiaopo.flying.puzzle.Line;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author wupanjie
 */

public class StraightArea implements Area {
  StraightLine lineLeft;
  StraightLine lineTop;
  StraightLine lineRight;
  StraightLine lineBottom;

  private Path areaPath = new Path();
  private RectF areaRect = new RectF();
  private PointF[] handleBarPoints = new PointF[2];

  public StraightArea(RectF baseRect) {
    setBaseRect(baseRect);

    handleBarPoints[0] = new PointF();
    handleBarPoints[1] = new PointF();
  }

  private void setBaseRect(RectF baseRect) {
    PointF one = new PointF(baseRect.left, baseRect.top);
    PointF two = new PointF(baseRect.right, baseRect.top);
    PointF three = new PointF(baseRect.left, baseRect.bottom);
    PointF four = new PointF(baseRect.right, baseRect.bottom);

    lineLeft = new StraightLine(one, three);
    lineTop = new StraightLine(one, two);
    lineRight = new StraightLine(two, four);
    lineBottom = new StraightLine(three, four);
  }

  public StraightArea(StraightArea src) {
    this.lineLeft = src.lineLeft;
    this.lineTop = src.lineTop;
    this.lineRight = src.lineRight;
    this.lineBottom = src.lineBottom;

    handleBarPoints[0] = new PointF();
    handleBarPoints[1] = new PointF();
  }

  @Override public float left() {
    return lineLeft.minX();
  }

  @Override public float top() {
    return lineTop.minY();
  }

  @Override public float right() {
    return lineRight.maxX();
  }

  @Override public float bottom() {
    return lineBottom.maxY();
  }

  @Override public float centerX() {
    return (left() + right()) / 2;
  }

  @Override public float centerY() {
    return (top() + bottom()) / 2;
  }

  @Override public float width() {
    return right() - left();
  }

  @Override public float height() {
    return bottom() - top();
  }

  @Override public PointF getCenterPoint() {
    return new PointF(centerX(), centerY());
  }

  @Override public boolean contains(PointF point) {
    return false;
  }

  @Override public boolean contains(float x, float y) {
    return getAreaRect().contains(x, y);
  }

  @Override public boolean contains(Line line) {
    return lineLeft == line || lineTop == line || lineRight == line || lineBottom == line;
  }

  @Override public Path getAreaPath() {
    areaPath.reset();
    areaPath.addRect(getAreaRect(), Path.Direction.CCW);
    return areaPath;
  }

  @Override public RectF getAreaRect() {
    areaRect.set(left(), top(), right(), bottom());
    return areaRect;
  }

  @Override public List<Line> getLines() {
    return Arrays.asList((Line) lineLeft, lineTop, lineRight, lineBottom);
  }

  @Override public PointF[] getHandleBarPoints(Line line) {
    if (line == lineLeft) {
      handleBarPoints[0].x = left();
      handleBarPoints[0].y = top() + height() / 4;
      handleBarPoints[1].x = left();
      handleBarPoints[1].y = top() + height() / 4 * 3;
    } else if (line == lineTop) {
      handleBarPoints[0].x = left() + width() / 4;
      handleBarPoints[0].y = top();
      handleBarPoints[1].x = left() + width() / 4 * 3;
      handleBarPoints[1].y = top();
    } else if (line == lineRight) {
      handleBarPoints[0].x = right();
      handleBarPoints[0].y = top() + height() / 4;
      handleBarPoints[1].x = right();
      handleBarPoints[1].y = top() + height() / 4 * 3;
    } else if (line == lineBottom) {
      handleBarPoints[0].x = left() + width() / 4;
      handleBarPoints[0].y = bottom();
      handleBarPoints[1].x = left() + width() / 4 * 3;
      handleBarPoints[1].y = bottom();
    }
    return handleBarPoints;
  }

  public static class AreaComparator implements Comparator<StraightArea> {
    @Override public int compare(StraightArea lhs, StraightArea rhs) {
      if (lhs.top() < rhs.top()) {
        return -1;
      } else if (lhs.top() == rhs.top()) {
        if (lhs.left() < rhs.left()) {
          return -1;
        } else {
          return 1;
        }
      } else {
        return 1;
      }
    }
  }
}