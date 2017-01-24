/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.chart.plot;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.meteoinfo.chart.ChartLegend;
import org.meteoinfo.chart.ChartText;
import org.meteoinfo.chart.Location;
import org.meteoinfo.data.Dataset;
import org.meteoinfo.data.XYListDataset;
import org.meteoinfo.drawing.Draw;
import org.meteoinfo.global.Extent;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.PointF;
import org.meteoinfo.global.colors.ColorUtil;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.shape.ShapeTypes;

/**
 *
 * @author yaqiang
 */
public class BarPlot_Old extends XYPlot {

    // <editor-fold desc="Variables">
    private XYListDataset dataset;
    private List<PolygonBreak> legendBreaks;
    private float barWidth;
    private boolean autoWidth;

    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public BarPlot_Old() {
        this.dataset = new XYListDataset();
        this.legendBreaks = new ArrayList<>();
        barWidth = 0.8f;
        autoWidth = true;
    }

    /**
     * Constructor
     *
     * @param dataset Dataset
     */
    public BarPlot_Old(XYListDataset dataset) {
        this();
        this.dataset = dataset;
    }

    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    @Override
    public XYListDataset getDataset() {
        return this.dataset;
    }

    @Override
    public void setDataset(Dataset value) {
        this.dataset = (XYListDataset) value;
        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
        this.getAxis(Location.BOTTOM).setTickLocations(this.dataset.getXValues(0));
        this.getAxis(Location.TOP).setTickLocations(this.dataset.getXValues(0));
        this.updateSeriesLegend();
    }

    private void updateSeriesLegend() {
        int si = this.legendBreaks.size();
        if (si > dataset.getSeriesCount()) {
            si = 0;
        }
        for (int i = si; i < dataset.getSeriesCount(); i++) {
            PolygonBreak pgb = new PolygonBreak();
            pgb.setColor(ColorUtil.getCommonColor(i));
            pgb.setCaption(dataset.getSeriesKey(i));
            this.legendBreaks.add(pgb);
        }
    }

    /**
     * Get bar width ratio
     *
     * @return Bar width ratio
     */
    public float getBarWidth() {
        return this.barWidth;
    }

    /**
     * Set bar width ratio
     *
     * @param value Bar width ratio
     */
    public void setBarWidth(float value) {
        this.barWidth = value;
    }

    /**
     * Get if automatically decide bar width
     *
     * @return Boolean
     */
    public boolean isAutoWidth() {
        return this.autoWidth;
    }

    /**
     * Set if automatically decide bar height
     *
     * @param value Boolean
     */
    public void setAutoWidth(boolean value) {
        this.autoWidth = value;
    }

    // </editor-fold>
    // <editor-fold desc="Methods">
    /**
     * Add a series data
     *
     * @param seriesKey Series key
     * @param xvs X value array
     * @param yvs Y value array
     */
    public void addSeries(String seriesKey, double[] xvs, double[] yvs) {
        this.dataset.addSeries(seriesKey, xvs, yvs);
        PolygonBreak plb = new PolygonBreak();
        plb.setColor(ColorUtil.getCommonColor(this.dataset.getSeriesCount()));
        plb.setCaption(seriesKey);
        this.legendBreaks.add(plb);

        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
    }

    /**
     * Remove last series
     */
    public void removeLastSeries() {
        XYListDataset ds = (XYListDataset) this.dataset;
        ds.removeSeries(dataset.getSeriesCount() - 1);
        this.legendBreaks.remove(this.legendBreaks.size() - 1);

        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
    }

    /**
     * Set legend break
     *
     * @param seriesIdx Series index
     * @param cb Legend break
     */
    public void setLegendBreak(int seriesIdx, PolygonBreak cb) {
        this.legendBreaks.set(seriesIdx, cb);
    }

    @Override
    void drawGraph(Graphics2D g, Rectangle2D area) {
        AffineTransform oldMatrix = g.getTransform();
        Rectangle oldRegion = g.getClipBounds();
        g.setClip(area);
        g.translate(area.getX(), area.getY());

        //Draw background
        if (this.isDrawBackground()) {
            g.setColor(this.getBackground());
            g.fill(new Rectangle2D.Double(0, 0, area.getWidth(), area.getHeight()));
        }

        double[] xy;
        float width = this.barWidth;
        if (!this.autoWidth) {
            xy = this.projToScreen(this.barWidth, 0, area);
            width = (float) xy[0];
            xy = this.projToScreen(0, 0, area);
            width = width - (float) xy[0];
        }
        for (int i = 0; i < this.dataset.getSeriesCount(); i++) {
            int len = this.dataset.getItemCount(i);
            PointF[] points = new PointF[len];
            List<Integer> mvIdx = this.dataset.getMissingValueIndex(i);
            if (this.getPlotOrientation() == PlotOrientation.VERTICAL) {
                for (int j = 0; j < len; j++) {
                    xy = this.projToScreen(this.dataset.getX(i, j), this.dataset.getY(i, j), area);
                    points[j] = new PointF((float) xy[0], (float) xy[1]);
                }
            } else {
                for (int j = 0; j < len; j++) {
                    xy = this.projToScreen(this.dataset.getY(i, j), this.dataset.getX(i, j), area);
                    points[j] = new PointF((float) xy[0], (float) xy[1]);
                }
            }
            if (this.getYAxis().isInverse()) {
                PointF[] npoints = new PointF[len];
                PointF p;
                float y;
                for (int j = 0; j < len; j++) {
                    p = points[len - j - 1];
                    y = (float) area.getHeight() - p.Y;
                    npoints[j] = new PointF(p.X, y);
                }
                points = npoints;
                if (!mvIdx.isEmpty()) {
                    for (int j = 0; j < mvIdx.size(); j++) {
                        mvIdx.set(j, len - mvIdx.get(j) - 1);
                    }
                }
            }
            if (this.getXAxis().isInverse()) {
                PointF[] npoints = new PointF[len];
                PointF p;
                float x;
                for (int j = 0; j < len; j++) {
                    p = points[len - j - 1];
                    x = (float) area.getWidth() - p.X;
                    npoints[j] = new PointF(x, p.Y);
                }
                points = npoints;
                if (!mvIdx.isEmpty()) {
                    for (int j = 0; j < mvIdx.size(); j++) {
                        mvIdx.set(j, len - mvIdx.get(j) - 1);
                    }
                }
            }

            PolygonBreak slegend = this.legendBreaks.get(i);
            if (this.autoWidth) {
                if (points.length > 1) {
                    width = (float) ((points[1].X - points[0].X) * 0.5) / this.dataset.getSeriesCount();
                } else {
                    width = (float) (area.getWidth() / 10) / this.dataset.getSeriesCount();
                }
                float height;
                for (int j = 0; j < len; j++) {
                    if (!mvIdx.contains(j)) {
                        height = (float) (area.getHeight() - points[j].Y);
                        Draw.drawBar(new PointF(points[j].X - width * this.dataset.getSeriesCount() / 2
                                + i * width, (float) area.getHeight()), width, height, (PolygonBreak) slegend, g, false, 5);
                    }
                }
            } else {
                float height;
                for (int j = 0; j < len; j++) {
                    if (!mvIdx.contains(j)) {
                        height = (float) (area.getHeight() - points[j].Y);
                        Draw.drawBar(new PointF(points[j].X, (float) area.getHeight()), width, height, (PolygonBreak) slegend, g, false, 5);
                    }
                }
            }
        }

        //Draw texts
        for (ChartText text : this.getTexts()) {
            xy = this.projToScreen(text.getX(), text.getY(), area);
            float x = (float) xy[0];
            float y = (float) xy[1];
            g.setFont(text.getFont());
            g.setColor(text.getColor());
            //Dimension dim = Draw.getStringDimension(text.getText(), g);
            //y -= dim.height * 2 / 3;
            Draw.drawString(g, text.getText(), x, y);
        }

        g.setTransform(oldMatrix);
        g.setClip(oldRegion);
    }

    private double getXInterval() {
        double[] xvalues = this.dataset.getXValues(0);
        if (xvalues.length == 1)
            if (xvalues[0] == 0)
                return 1;
            else
                return xvalues[0] / 10;
        else
            return xvalues[1] - xvalues[0];
    }

    @Override
    Extent getAutoExtent() {
        Extent extent = dataset.getDataExtent();
        double dx = getXInterval();
        double xmin = extent.minX - dx;
        double xmax = extent.maxX + dx;
        double[] yValues = (double[])MIMath.getIntervalValues(extent.minY, extent.maxY, true).get(0);
        if (this.getPlotOrientation() == PlotOrientation.VERTICAL) {
            return new Extent(xmin, xmax, 0, yValues[yValues.length - 1]);
        } else {
            return new Extent(0, yValues[yValues.length - 1], xmin, xmax);
        }
    }
    
    @Override
    public void setAutoExtent(){
        
    }

    @Override
    public void updateLegendScheme() {
        if (this.getLegend() == null) {
            this.setLegend(new ChartLegend(this.getLegendScheme()));
        } else {
            this.getLegend().setLegendScheme(this.getLegendScheme());
        }
    }

    /**
     * Get legend scheme
     *
     * @return Legend scheme
     */
    public LegendScheme getLegendScheme() {
        ShapeTypes stype = ShapeTypes.Polygon;
        LegendScheme ls = new LegendScheme(stype);
        for (PolygonBreak slegend : this.legendBreaks) {
            ls.getLegendBreaks().add(slegend);
        }
        return ls;
    }

    // </editor-fold>
}
