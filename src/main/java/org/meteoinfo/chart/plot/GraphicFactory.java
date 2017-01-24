/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.chart.plot;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.meteoinfo.data.ArrayMath;
import org.meteoinfo.data.ArrayUtil;
import org.meteoinfo.data.GridArray;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.XYListDataset;
import org.meteoinfo.data.analysis.Statistics;
import org.meteoinfo.drawing.ContourDraw;
import org.meteoinfo.drawing.Draw;
import org.meteoinfo.drawing.PointStyle;
import org.meteoinfo.geoprocess.GeoComputation;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.PointD;
import org.meteoinfo.legend.BarBreak;
import org.meteoinfo.legend.ColorBreak;
import org.meteoinfo.legend.LegendManage;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.LegendType;
import org.meteoinfo.legend.LineStyles;
import org.meteoinfo.legend.PointBreak;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.legend.PolylineBreak;
import org.meteoinfo.shape.ArcShape;
import org.meteoinfo.shape.BarShape;
import org.meteoinfo.shape.Graphic;
import org.meteoinfo.shape.GraphicCollection;
import org.meteoinfo.shape.ImageShape;
import org.meteoinfo.shape.PointShape;
import org.meteoinfo.shape.PolygonShape;
import org.meteoinfo.shape.PolylineErrorShape;
import org.meteoinfo.shape.PolylineShape;
import org.meteoinfo.shape.ShapeTypes;
import org.meteoinfo.shape.WindArrow;
import org.meteoinfo.shape.WindBarb;
import ucar.ma2.Array;
import wContour.Global.PolyLine;

/**
 *
 * @author Yaqiang Wang
 */
public class GraphicFactory {

    /**
     * Create LineString graphic
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param cb Color break
     * @return LineString graphic
     */
    public static GraphicCollection createLineString(Array xdata, Array ydata, ColorBreak cb) {
        GraphicCollection gc = new GraphicCollection();
        PolylineShape pls;
        List<PointD> points = new ArrayList<>();
        double x, y;
        for (int i = 0; i < xdata.getSize(); i++) {
            x = xdata.getDouble(i);
            y = ydata.getDouble(i);
            if (Double.isNaN(y) || Double.isNaN(x)) {
                if (points.isEmpty()) {
                    continue;
                }
                if (points.size() == 1) {
                    points.add((PointD) points.get(0).clone());
                }
                pls = new PolylineShape();
                pls.setPoints(points);
                gc.add(new Graphic(pls, cb));
                points = new ArrayList<>();
            } else {
                points.add(new PointD(x, y));
            }
        }
        if (points.size() == 1) {
            points.add((PointD) points.get(0).clone());
        }
        pls = new PolylineShape();
        pls.setPoints(points);
        gc.add(new Graphic(pls, cb));

        return gc;
    }

    /**
     * Create LineString graphic
     *
     * @param data Y data array
     * @param cbs Color breaks
     * @return LineString graphic
     */
    public static GraphicCollection createLineString(XYListDataset data, List<ColorBreak> cbs) {
        GraphicCollection gc = new GraphicCollection();
        PolylineShape pls;
        List<PointD> points;
        double x, y;
        for (int i = 0; i < data.getSeriesCount(); i++) {
            points = new ArrayList<>();
            for (int j = 0; j < data.getItemCount(i); j++) {
                x = data.getX(i, j);
                y = data.getY(i, j);
                points.add(new PointD(x, y));
            }
            pls = new PolylineShape();
            pls.setPoints(points);
            gc.add(new Graphic(pls, cbs.get(i)));
        }
        gc.setSingleLegend(false);

        return gc;
    }

    /**
     * Create error LineString graphic
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param xError X error array
     * @param yError Y error array
     * @param cb Color break
     * @return LineString graphic
     */
    public static GraphicCollection createErrorLineString(Array xdata, Array ydata, Array xError, Array yError, ColorBreak cb) {
        GraphicCollection gc = new GraphicCollection();
        PolylineErrorShape pls;
        List<PointD> points = new ArrayList<>();
        List<Number> xerrors = new ArrayList<>();
        List<Number> yerrors = new ArrayList<>();
        double x, y;
        for (int i = 0; i < xdata.getSize(); i++) {
            x = xdata.getDouble(i);
            y = ydata.getDouble(i);
            if (Double.isNaN(y) || Double.isNaN(x)) {
                if (points.isEmpty()) {
                    continue;
                }
                if (points.size() == 1) {
                    points.add((PointD) points.get(0).clone());
                }
                pls = new PolylineErrorShape();
                pls.setPoints(points);
                if (xError != null) {
                    pls.setXerror(xerrors);
                }
                if (yError != null) {
                    pls.setYerror(yerrors);
                }
                pls.updateExtent();
                gc.add(new Graphic(pls, cb));
                points = new ArrayList<>();
                xerrors = new ArrayList<>();
                yerrors = new ArrayList<>();
            } else {
                points.add(new PointD(x, y));
                if (xError != null) {
                    xerrors.add(xError.getDouble(i));
                }
                if (yError != null) {
                    yerrors.add(yError.getDouble(i));
                }
            }
        }
        if (points.size() == 1) {
            points.add((PointD) points.get(0).clone());
        }
        pls = new PolylineErrorShape();
        pls.setPoints(points);
        if (xError != null) {
            pls.setXerror(xerrors);
        }
        if (yError != null) {
            pls.setYerror(yerrors);
        }
        pls.updateExtent();
        gc.add(new Graphic(pls, cb));

        return gc;
    }

    /**
     * Create graphics
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param cb Color break
     * @return LineString graphic
     */
    public static GraphicCollection createGraphics(Array xdata, Array ydata, ColorBreak cb) {
        GraphicCollection graphics = new GraphicCollection();
        if (cb instanceof PolylineBreak) {
            graphics.add(createLineString(xdata, ydata, cb));
        } else {
            PointShape ps;
            for (int i = 0; i < xdata.getSize(); i++) {
                ps = new PointShape();
                ps.setPoint(new PointD(xdata.getDouble(i), ydata.getDouble(i)));
                graphics.add(new Graphic(ps, cb));
            }
        }
        return graphics;
    }

    /**
     * Create graphics
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param cbs Color breaks
     * @return LineString graphic
     */
    public static GraphicCollection createPoints(Array xdata, Array ydata, List<ColorBreak> cbs) {
        GraphicCollection graphics = new GraphicCollection();
        PointShape ps;
        if (cbs.size() == xdata.getSize()) {
            for (int i = 0; i < xdata.getSize(); i++) {
                ps = new PointShape();
                ps.setPoint(new PointD(xdata.getDouble(i), ydata.getDouble(i)));
                graphics.add(new Graphic(ps, cbs.get(i)));
            }
            graphics.setSingleLegend(false);
        } else {
            for (int i = 0; i < xdata.getSize(); i++) {
                ps = new PointShape();
                ps.setPoint(new PointD(xdata.getDouble(i), ydata.getDouble(i)));
                graphics.add(new Graphic(ps, cbs.get(0)));
            }
        }
        return graphics;
    }

    /**
     * Create bar graphics
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param autoWidth Is auto width or not
     * @param width Width
     * @param drawError Is draw error or not
     * @param error Error
     * @param drawBottom Is draw bottom or not
     * @param bottom Bottom
     * @param bbs Bar breaks
     * @return Bar graphics
     */
    public static GraphicCollection createBars(Array xdata, Array ydata, boolean autoWidth,
            double width, boolean drawError, Array error, boolean drawBottom, Array bottom,
            List<BarBreak> bbs) {
        GraphicCollection graphics = new GraphicCollection();
        int n = (int) xdata.getSize();
        double x, y;
        BarBreak bb = bbs.get(0);
        for (int i = 0; i < n; i++) {
            x = xdata.getDouble(i);
            y = ydata.getDouble(i);
            BarShape bs = new BarShape();
            bs.setPoint(new PointD(x, y));
            bs.setAutoWidth(autoWidth);
            bs.setWidth(width);
            bs.setDrawError(drawError);
            if (drawError) {
                bs.setError(error.getDouble(i));
            }
            bs.setDrawBottom(drawBottom);
            if (drawBottom) {
                bs.setBottom(bottom.getDouble(i));
            }
            if (bbs.size() > i) {
                bb = bbs.get(i);
            }
            graphics.add(new Graphic(bs, bb));
        }
        if (bbs.size() == 1) {
            graphics.setSingleLegend(true);
        } else {
            graphics.setSingleLegend(false);
        }

        return graphics;
    }

    /**
     * Create histogram bar graphics
     *
     * @param data The data array
     * @param nbin Bin number
     * @param bbs Bar breaks
     * @return Bar graphics
     */
    public static GraphicCollection createHistBars(Array data, int nbin,
            List<BarBreak> bbs) {
        List<Array> r = ArrayUtil.histogram(data, nbin);
        Array xdata = r.get(0);
        Array ydata = r.get(1);
        GraphicCollection graphics = new GraphicCollection();
        int n = (int) ydata.getSize();
        double x, y, width;
        BarBreak bb = bbs.get(0);
        for (int i = 0; i < n; i++) {
            x = (xdata.getDouble(i + 1) + xdata.getDouble(i)) * 0.5;
            width = xdata.getDouble(i + 1) - xdata.getDouble(i);
            y = ydata.getDouble(i);
            BarShape bs = new BarShape();
            bs.setPoint(new PointD(x, y));
            bs.setAutoWidth(false);
            bs.setWidth(width);
            bs.setDrawBottom(false);
            if (bbs.size() > i) {
                bb = bbs.get(i);
            }
            graphics.add(new Graphic(bs, bb));
        }
        if (bbs.size() == 1) {
            graphics.setSingleLegend(true);
        } else {
            graphics.setSingleLegend(false);
        }

        return graphics;
    }

    /**
     * Create image
     *
     * @param x X data array
     * @param y Y data array
     * @param gdata Grid data array
     * @param ls Legend scheme
     * @return Image graphic
     */
    public static Graphic createImage(Array x, Array y, Array gdata, LegendScheme ls) {
        int width, height, breakNum;
        width = (int) x.getSize();
        height = (int) y.getSize();
        breakNum = ls.getBreakNum();
        double[] breakValue = new double[breakNum];
        Color[] breakColor = new Color[breakNum];
        Color undefColor = Color.white;
        for (int i = 0; i < breakNum; i++) {
            breakValue[i] = Double.parseDouble(ls.getLegendBreaks().get(i).getEndValue().toString());
            breakColor[i] = ls.getLegendBreaks().get(i).getColor();
            if (ls.getLegendBreaks().get(i).isNoData()) {
                undefColor = ls.getLegendBreaks().get(i).getColor();
            }
        }
        Color defaultColor = breakColor[breakNum - 1];    //默认颜色为最后一个颜色
        BufferedImage aImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double oneValue;
        Color oneColor;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //oneValue = gdata.data[i][j];
                oneValue = gdata.getDouble(i * width + j);
                if (Double.isNaN(oneValue)) {
                    oneColor = undefColor;
                } else {
                    oneColor = defaultColor;
                    if (ls.getLegendType() == LegendType.GraduatedColor) {
                        //循环只到breakNum-1 是因为最后一个LegendBreaks的EndValue和StartValue是一样的
                        for (int k = 0; k < breakNum - 1; k++) {
                            if (oneValue < breakValue[k]) {
                                oneColor = breakColor[k];
                                break;
                            }
                        }
                    } else {
                        for (int k = 0; k < breakNum - 1; k++) {
                            if (oneValue == breakValue[k]) {
                                oneColor = breakColor[k];
                                break;
                            }
                        }
                    }
                }
                aImage.setRGB(j, height - i - 1, oneColor.getRGB());
            }
        }

        ImageShape ishape = new ImageShape();
        ishape.setPoint(new PointD(x.getDouble(0), y.getDouble(0)));
        ishape.setImage(aImage);
        return new Graphic(ishape, new ColorBreak());
    }

    /**
     * Create image
     *
     * @param gdata Grid data array
     * @param ls Legend scheme
     * @return Image graphic
     */
    public static Graphic createImage(GridArray gdata, LegendScheme ls) {
        int width, height, breakNum;
        width = gdata.getXNum();
        height = gdata.getYNum();
        breakNum = ls.getBreakNum();
        double[] breakValue = new double[breakNum];
        Color[] breakColor = new Color[breakNum];
        Color undefColor = Color.white;
        for (int i = 0; i < breakNum; i++) {
            breakValue[i] = Double.parseDouble(ls.getLegendBreaks().get(i).getEndValue().toString());
            breakColor[i] = ls.getLegendBreaks().get(i).getColor();
            if (ls.getLegendBreaks().get(i).isNoData()) {
                undefColor = ls.getLegendBreaks().get(i).getColor();
            }
        }
        Color defaultColor = breakColor[breakNum - 1];    //默认颜色为最后一个颜色
        BufferedImage aImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        double oneValue;
        Color oneColor;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                //oneValue = gdata.data[i][j];
                oneValue = gdata.getDoubleValue(i, j);
                if (Double.isNaN(oneValue) || MIMath.doubleEquals(oneValue, gdata.missingValue)) {
                    oneColor = undefColor;
                } else {
                    oneColor = defaultColor;
                    if (ls.getLegendType() == LegendType.GraduatedColor) {
                        //循环只到breakNum-1 是因为最后一个LegendBreaks的EndValue和StartValue是一样的
                        for (int k = 0; k < breakNum - 1; k++) {
                            if (oneValue < breakValue[k]) {
                                oneColor = breakColor[k];
                                break;
                            }
                        }
                    } else {
                        for (int k = 0; k < breakNum - 1; k++) {
                            if (oneValue == breakValue[k]) {
                                oneColor = breakColor[k];
                                break;
                            }
                        }
                    }
                }
                aImage.setRGB(j, height - i - 1, oneColor.getRGB());
            }
        }

        ImageShape ishape = new ImageShape();
        ishape.setPoint(new PointD(gdata.xArray[0], gdata.yArray[0]));
        ishape.setImage(aImage);
        return new Graphic(ishape, new ColorBreak());
    }

    /**
     * Create contour lines
     *
     * @param gridData Grid data
     * @param ls Legend scheme
     * @param isSmooth Is smooth or not
     * @return Contour lines
     */
    public static GraphicCollection createContourLines(GridData gridData, LegendScheme ls, boolean isSmooth) {
        ls = ls.convertTo(ShapeTypes.Polyline);
        Object[] ccs = LegendManage.getContoursAndColors(ls);
        double[] cValues = (double[]) ccs[0];

        int[][] S1 = new int[gridData.data.length][gridData.data[0].length];
        Object[] cbs = ContourDraw.tracingContourLines(gridData.data,
                cValues, gridData.xArray, gridData.yArray, gridData.missingValue, S1);
        List<wContour.Global.PolyLine> ContourLines = (List<wContour.Global.PolyLine>) cbs[0];

        if (ContourLines.isEmpty()) {
            return null;
        }

        if (isSmooth) {
            ContourLines = wContour.Contour.smoothLines(ContourLines);
        }

        wContour.Global.PolyLine aLine;
        double v;
        ColorBreak cbb = ls.getLegenBreak(0);
        GraphicCollection graphics = new GraphicCollection();
        for (int i = 0; i < ContourLines.size(); i++) {
            aLine = ContourLines.get(i);
            v = aLine.Value;

            PolylineShape aPolyline = new PolylineShape();
            PointD aPoint;
            List<PointD> pList = new ArrayList<>();
            for (int j = 0; j < aLine.PointList.size(); j++) {
                aPoint = new PointD();
                aPoint.X = aLine.PointList.get(j).X;
                aPoint.Y = aLine.PointList.get(j).Y;
                pList.add(aPoint);
            }
            aPolyline.setPoints(pList);
            aPolyline.setValue(v);
            aPolyline.setExtent(MIMath.getPointsExtent(pList));

            switch (ls.getLegendType()) {
                case UniqueValue:
                    for (int j = 0; j < ls.getBreakNum(); j++) {
                        ColorBreak cb = ls.getLegendBreaks().get(j);
                        if (MIMath.doubleEquals(v, Double.parseDouble(cb.getStartValue().toString()))) {
                            cbb = cb;
                            break;
                        }
                    }
                    break;
                case GraduatedColor:
                    int blNum = 0;
                    for (int j = 0; j < ls.getBreakNum(); j++) {
                        ColorBreak cb = ls.getLegendBreaks().get(j);
                        blNum += 1;
                        if (MIMath.doubleEquals(v, Double.parseDouble(cb.getStartValue().toString()))
                                || (v > Double.parseDouble(cb.getStartValue().toString())
                                && v < Double.parseDouble(cb.getEndValue().toString()))
                                || (blNum == ls.getBreakNum() && v == Double.parseDouble(cb.getEndValue().toString()))) {
                            cbb = cb;
                            break;
                        }
                    }
                    break;
            }
            graphics.add(new Graphic(aPolyline, cbb));
        }
        graphics.setSingleLegend(false);
        graphics.setLegendScheme(ls);

        return graphics;
    }

    /**
     * Create contour polygons
     *
     * @param gridData Grid data
     * @param ls Legend scheme
     * @param isSmooth Is smooth or not
     * @return Contour polygons
     */
    public static GraphicCollection createContourPolygons(GridData gridData, LegendScheme ls, boolean isSmooth) {
        ls = ls.convertTo(ShapeTypes.Polygon);
        Object[] ccs = LegendManage.getContoursAndColors(ls);
        double[] cValues = (double[]) ccs[0];

        double minData;
        double maxData;
        double[] maxmin = new double[2];
        gridData.getMaxMinValue(maxmin);
        maxData = maxmin[0];
        minData = maxmin[1];

        int[][] S1 = new int[gridData.data.length][gridData.data[0].length];
        Object[] cbs = ContourDraw.tracingContourLines(gridData.data,
                cValues, gridData.xArray, gridData.yArray, gridData.missingValue, S1);
        List<wContour.Global.PolyLine> contourLines = (List<wContour.Global.PolyLine>) cbs[0];
        List<wContour.Global.Border> borders = (List<wContour.Global.Border>) cbs[1];

        if (isSmooth) {
            contourLines = wContour.Contour.smoothLines(contourLines);
        }
        List<wContour.Global.Polygon> contourPolygons = ContourDraw.tracingPolygons(gridData.data, contourLines, borders, cValues);

        double v;
        ColorBreak cbb = ls.getLegenBreak(0);
        GraphicCollection graphics = new GraphicCollection();
        for (int i = 0; i < contourPolygons.size(); i++) {
            wContour.Global.Polygon poly = contourPolygons.get(i);
            v = poly.LowValue;
            PointD aPoint;
            List<PointD> pList = new ArrayList<>();
            for (wContour.Global.PointD pointList : poly.OutLine.PointList) {
                aPoint = new PointD();
                aPoint.X = pointList.X;
                aPoint.Y = pointList.Y;
                pList.add(aPoint);
            }
            if (!GeoComputation.isClockwise(pList)) {
                Collections.reverse(pList);
            }
            PolygonShape aPolygonShape = new PolygonShape();
            aPolygonShape.setPoints(pList);
            aPolygonShape.setExtent(MIMath.getPointsExtent(pList));
            aPolygonShape.lowValue = v;
            if (poly.HasHoles()) {
                for (PolyLine holeLine : poly.HoleLines) {
                    pList = new ArrayList<>();
                    for (wContour.Global.PointD pointList : holeLine.PointList) {
                        aPoint = new PointD();
                        aPoint.X = pointList.X;
                        aPoint.Y = pointList.Y;
                        pList.add(aPoint);
                    }
                    aPolygonShape.addHole(pList, 0);
                }
            }
            int valueIdx = Arrays.binarySearch(cValues, v);
            //valueIdx = Arrays.asList(cValues).indexOf(aValue);            
            if (valueIdx == cValues.length - 1) {
                aPolygonShape.highValue = maxData;
            } else {
                aPolygonShape.highValue = cValues[valueIdx + 1];
            }
//            if (!aPolygon.IsBorder) {
//                if (!aPolygon.IsHighCenter) {
//                    aPolygonShape.highValue = aValue;
//                    if (valueIdx == 0) {
//                        aPolygonShape.lowValue = minData;
//                    } else {
//                        aPolygonShape.lowValue = cValues[valueIdx - 1];
//                    }
//                }
//            }
            if (!poly.IsHighCenter && poly.HighValue == poly.LowValue) {
                aPolygonShape.highValue = v;
                if (valueIdx == 0) {
                    aPolygonShape.lowValue = minData;
                } else {
                    aPolygonShape.lowValue = cValues[valueIdx - 1];
                }
            }

            v = aPolygonShape.lowValue;
            switch (ls.getLegendType()) {
                case UniqueValue:
                    for (int j = 0; j < ls.getBreakNum(); j++) {
                        ColorBreak cb = ls.getLegendBreaks().get(j);
                        if (MIMath.doubleEquals(v, Double.parseDouble(cb.getStartValue().toString()))) {
                            cbb = cb;
                            break;
                        }
                    }
                    break;
                case GraduatedColor:
                    int blNum = 0;
                    for (int j = 0; j < ls.getBreakNum(); j++) {
                        ColorBreak cb = ls.getLegendBreaks().get(j);
                        blNum += 1;
                        if (MIMath.doubleEquals(v, Double.parseDouble(cb.getStartValue().toString()))
                                || (v > Double.parseDouble(cb.getStartValue().toString())
                                && v < Double.parseDouble(cb.getEndValue().toString()))
                                || (blNum == ls.getBreakNum() && v == Double.parseDouble(cb.getEndValue().toString()))) {
                            cbb = cb;
                            break;
                        }
                    }
                    break;
            }
            graphics.add(new Graphic(aPolygonShape, cbb));
        }
        graphics.setSingleLegend(false);
        graphics.setLegendScheme(ls);

        return graphics;
    }

    /**
     * Create fill between polygons
     *
     * @param xdata X data array
     * @param y1data Y1 data array
     * @param y2data Y2 data array
     * @param where Where data array
     * @param pb Polygon break
     * @return GraphicCollection
     */
    public static GraphicCollection createFillBetweenPolygons(Array xdata, Array y1data,
            Array y2data, Array where, PolygonBreak pb) {
        GraphicCollection gc = new GraphicCollection();
        int len = (int) xdata.getSize();
        if (where == null) {
            PolygonShape pgs = new PolygonShape();
            List<PointD> points = new ArrayList<>();
            for (int i = 0; i < len; i++) {
                points.add(new PointD(xdata.getDouble(i), y1data.getDouble(i)));
            }
            for (int i = len - 1; i >= 0; i--) {
                points.add(new PointD(xdata.getDouble(i), y2data.getDouble(i)));
            }
            pgs.setPoints(points);
            Graphic graphic = new Graphic(pgs, pb);
            gc.add(graphic);
        } else {
            boolean ob = false;
            List<List<Integer>> idxs = new ArrayList<>();
            List<Integer> idx = new ArrayList<>();
            for (int j = 0; j < len; j++) {
                if (where.getInt(j) == 1) {
                    if (!ob) {
                        idx = new ArrayList<>();
                    }
                    idx.add(j);
                } else if (ob) {
                    idxs.add(idx);
                }
                ob = where.getInt(j) == 1;
            }
            for (List<Integer> index : idxs) {
                int nn = index.size();
                if (nn >= 2) {
                    PolygonShape pgs = new PolygonShape();
                    List<PointD> points = new ArrayList<>();
                    int ii;
                    for (int j = 0; j < nn; j++) {
                        ii = index.get(j);
                        points.add(new PointD(xdata.getDouble(ii), y1data.getDouble(ii)));
                    }
                    for (int j = 0; j < nn; j++) {
                        ii = index.get(nn - j - 1);
                        points.add(new PointD(xdata.getDouble(ii), y2data.getDouble(ii)));
                    }
                    pgs.setPoints(points);
                    Graphic graphic = new Graphic(pgs, pb);
                    gc.add(graphic);
                }
            }
        }

        return gc;
    }

    /**
     * Create wind barbs
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param udata U/WindDirection data array
     * @param vdata V/WindSpeed data array
     * @param cdata Colored data array
     * @param ls Legend scheme
     * @param isUV Is U/V or not
     * @return GraphicCollection
     */
    public static GraphicCollection createBarbs(Array xdata, Array ydata, Array udata, Array vdata,
            Array cdata, LegendScheme ls, boolean isUV) {
        GraphicCollection gc = new GraphicCollection();
        Array windDirData;
        Array windSpeedData;
        if (isUV) {
            Array[] wwData = ArrayMath.uv2ds(udata, vdata);
            windDirData = wwData[0];
            windSpeedData = wwData[1];
        } else {
            windDirData = udata;
            windSpeedData = vdata;
        }

        ShapeTypes sts = ls.getShapeType();
        ls = ls.convertTo(ShapeTypes.Point);
        if (sts != ShapeTypes.Point) {
            for (int i = 0; i < ls.getBreakNum(); i++) {
                ((PointBreak) ls.getLegendBreaks().get(i)).setSize(10);
            }
        }

        int i, j;
        WindBarb aWB;
        double windDir, windSpeed;
        PointD aPoint;
        ColorBreak cb;
        double v;
        int dn = (int) xdata.getSize();
        for (i = 0; i < dn; i++) {
            windDir = windDirData.getDouble(i);
            windSpeed = windSpeedData.getDouble(i);
            if (!Double.isNaN(windDir)) {
                if (!Double.isNaN(windSpeed)) {
                    aPoint = new PointD();
                    aPoint.X = xdata.getDouble(i);
                    aPoint.Y = ydata.getDouble(i);
                    aWB = Draw.calWindBarb((float) windDir, (float) windSpeed, 0, 10, aPoint);
                    if (cdata == null) {
                        cb = ls.getLegendBreaks().get(0);
                    } else {
                        v = cdata.getDouble(i);
                        aWB.setValue(v);
                        cb = ls.getLegenBreak(v);
                    }
                    Graphic graphic = new Graphic(aWB, cb);
                    gc.add(graphic);
                }
            }
        }

        gc.setLegendScheme(ls);
        if (cdata != null) {
            gc.setSingleLegend(false);
        }

        return gc;
    }

    /**
     * Create wind arrows
     *
     * @param xdata X data array
     * @param ydata Y data array
     * @param udata U/WindDirection data array
     * @param vdata V/WindSpeed data array
     * @param cdata Colored data array
     * @param ls Legend scheme
     * @param isUV Is U/V or not
     * @return GraphicCollection
     */
    public static GraphicCollection createArrows(Array xdata, Array ydata, Array udata, Array vdata,
            Array cdata, LegendScheme ls, boolean isUV) {
        GraphicCollection gc = new GraphicCollection();
        Array windDirData;
        Array windSpeedData;
        if (isUV) {
            Array[] wwData = ArrayMath.uv2ds(udata, vdata);
            windDirData = wwData[0];
            windSpeedData = wwData[1];
        } else {
            windDirData = udata;
            windSpeedData = vdata;
        }

        ShapeTypes sts = ls.getShapeType();
        ls = ls.convertTo(ShapeTypes.Point);
        if (sts != ShapeTypes.Point) {
            for (int i = 0; i < ls.getBreakNum(); i++) {
                ((PointBreak) ls.getLegendBreaks().get(i)).setSize(10);
            }
        }

        int i, j;
        WindArrow wa;
        double windDir, windSpeed;
        PointD aPoint;
        ColorBreak cb;
        double v;
        int dn = (int) xdata.getSize();
        float size = 6;
        for (i = 0; i < dn; i++) {
            windDir = windDirData.getDouble(i);
            windSpeed = windSpeedData.getDouble(i);
            if (!Double.isNaN(windDir)) {
                if (!Double.isNaN(windSpeed)) {
                    aPoint = new PointD();
                    aPoint.X = xdata.getDouble(i);
                    aPoint.Y = ydata.getDouble(i);
                    wa = new WindArrow();
                    wa.angle = windDir;
                    wa.length = (float) windSpeed;
                    wa.size = size;
                    wa.setPoint(aPoint);
                    if (cdata == null) {
                        cb = ls.getLegendBreaks().get(0);
                    } else {
                        v = cdata.getDouble(i);
                        wa.setValue(v);
                        cb = ls.getLegenBreak(v);
                    }
                    Graphic graphic = new Graphic(wa, cb);
                    gc.add(graphic);
                }
            }
        }

        gc.setLegendScheme(ls);
        if (cdata != null) {
            gc.setSingleLegend(false);
        }

        return gc;
    }

    /**
     * Create pie arc polygons
     *
     * @param xdata X data array
     * @param colors Colors
     * @param labels Labels
     * @param startAngle Start angle
     * @param explode Explode
     * @param labelFont Label font
     * @param labelColor Label color
     * @param autopct
     * @return GraphicCollection
     */
    public static GraphicCollection createPieArcs(Array xdata, List<Color> colors,
            List<String> labels, float startAngle, List<Number> explode, Font labelFont,
            Color labelColor, String autopct) {
        GraphicCollection gc = new GraphicCollection();
        double sum = ArrayMath.sumDouble(xdata);
        double v;
        int n = (int) xdata.getSize();
        float sweepAngle;
        NumberFormat nf = NumberFormat.getPercentInstance();
        // nf.setMinimumFractionDigits(2);
        for (int i = 0; i < n; i++) {
            v = xdata.getDouble(i);
            if (sum > 1) {
                v = v / sum;
            }
            sweepAngle = (float) (360.0 * v);
            ArcShape aShape = new ArcShape();
            aShape.setStartAngle(startAngle);
            aShape.setSweepAngle(sweepAngle);
            List<PointD> points = new ArrayList<>();
            points.add(new PointD(0, 0));
            points.add(new PointD(0, 1));
            points.add(new PointD(1, 1));
            points.add(new PointD(1, 0));
            points.add(new PointD(0, 0));
            aShape.setPoints(points);
            if (explode != null) {
                aShape.setExplode(explode.get(i).floatValue());
            }
            PolygonBreak pgb = new PolygonBreak();
            pgb.setColor(colors.get(i));
            if (labels == null) {
                if (autopct == null) {
                    pgb.setCaption(nf.format(v));
                } else {
                    pgb.setCaption(String.format(autopct, v * 100));
                }
            } else {
                pgb.setCaption(labels.get(i));
            }
            Graphic graphic = new Graphic(aShape, pgb);
            gc.add(graphic);
            startAngle += sweepAngle;
        }
        gc.setSingleLegend(false);
        gc.getLabelSet().setLabelFont(labelFont);
        gc.getLabelSet().setLabelColor(labelColor);

        return gc;
    }

    /**
     * Create box graphics
     *
     * @param xdata X data array list
     * @param showmeans Show means or not
     * @return GraphicCollection
     */
    public static GraphicCollection createBox(List<Array> xdata, boolean showmeans) {
        GraphicCollection gc = new GraphicCollection();
        int n = xdata.size();
        double v, width = 0.3;
        for (int i = 0; i < n; i++) {
            Array a = xdata.get(i);
            v = i + 1;
            double q1 = Statistics.quantile(a, 1);
            double q3 = Statistics.quantile(a, 3);
            double median = Statistics.quantile(a, 2);
            double mind = ArrayMath.getMinimum(a);
            double maxd = ArrayMath.getMaximum(a);
            double mino = q1 - (q3 - q1) * 1.5;
            double maxo = q3 + (q3 - q1) * 1.5;
            //GraphicCollection gc1 = new GraphicCollection();
            List<PointD> pList = new ArrayList<>();
            pList.add(new PointD(v - width * 0.5, q1));
            pList.add(new PointD(v - width * 0.5, q3));
            pList.add(new PointD(v + width * 0.5, q3));
            pList.add(new PointD(v + width * 0.5, q1));
            pList.add(new PointD(v - width * 0.5, q1));
            PolygonShape pgs = new PolygonShape();
            pgs.setPoints(pList);
            PolygonBreak pgb = new PolygonBreak();
            pgb.setDrawFill(false);
            pgb.setOutlineColor(Color.blue);
            //gc1.add(new Graphic(pgs, pgb));
            gc.add(new Graphic(pgs, pgb));

            pList = new ArrayList<>();
            pList.add(new PointD(v - width * 0.5, median));
            pList.add(new PointD(v + width * 0.5, median));
            PolylineShape pls = new PolylineShape();
            pls.setPoints(pList);
            PolylineBreak plb = new PolylineBreak();
            plb.setColor(Color.red);
            gc.add(new Graphic(pls, plb));

            double min = Math.max(mino, mind);
            pList = new ArrayList<>();
            pList.add(new PointD(v, q1));
            pList.add(new PointD(v, min));
            pls = new PolylineShape();
            pls.setPoints(pList);
            plb = new PolylineBreak();
            plb.setColor(Color.black);
            plb.setStyle(LineStyles.Dash);
            gc.add(new Graphic(pls, plb));
            pList = new ArrayList<>();
            pList.add(new PointD(v - width * 0.25, min));
            pList.add(new PointD(v + width * 0.25, min));
            pls = new PolylineShape();
            pls.setPoints(pList);
            plb = new PolylineBreak();
            plb.setColor(Color.black);
            plb.setStyle(LineStyles.Solid);
            gc.add(new Graphic(pls, plb));
            if (mino > mind) {
                for (int j = 0; j < a.getSize(); j++) {
                    if (a.getDouble(j) < mino) {
                        PointShape ps = new PointShape();
                        ps.setPoint(new PointD(v, a.getDouble(j)));
                        PointBreak pb = new PointBreak();
                        pb.setStyle(PointStyle.Plus);
                        gc.add(new Graphic(ps, pb));
                    }
                }
            }

            double max = Math.min(maxo, maxd);
            pList = new ArrayList<>();
            pList.add(new PointD(v, q3));
            pList.add(new PointD(v, max));
            pls = new PolylineShape();
            pls.setPoints(pList);
            plb = new PolylineBreak();
            plb.setColor(Color.black);
            plb.setStyle(LineStyles.Dash);
            gc.add(new Graphic(pls, plb));
            pList = new ArrayList<>();
            pList.add(new PointD(v - width * 0.25, max));
            pList.add(new PointD(v + width * 0.25, max));
            pls = new PolylineShape();
            pls.setPoints(pList);
            plb = new PolylineBreak();
            plb.setColor(Color.black);
            plb.setStyle(LineStyles.Solid);
            gc.add(new Graphic(pls, plb));
            if (maxo < maxd) {
                for (int j = 0; j < a.getSize(); j++) {
                    if (a.getDouble(j) > maxo) {
                        PointShape ps = new PointShape();
                        ps.setPoint(new PointD(v, a.getDouble(j)));
                        PointBreak pb = new PointBreak();
                        pb.setStyle(PointStyle.Plus);
                        gc.add(new Graphic(ps, pb));
                    }
                }
            }

            if (showmeans) {
                double mean = ArrayMath.mean(a);
                PointShape ps = new PointShape();
                ps.setPoint(new PointD(v, mean));
                PointBreak pb = new PointBreak();
                pb.setStyle(PointStyle.Square);
                pb.setColor(Color.red);
                pb.setOutlineColor(Color.black);
                gc.add(new Graphic(ps, pb));
            }
        }
        gc.setSingleLegend(false);

        return gc;
    }
}
