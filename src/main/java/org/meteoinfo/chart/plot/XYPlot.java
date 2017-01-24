/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.chart.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.meteoinfo.chart.ChartLegend;
import org.meteoinfo.chart.ChartText;
import org.meteoinfo.chart.ChartWindArrow;
import org.meteoinfo.chart.LegendPosition;
import org.meteoinfo.chart.Location;
import org.meteoinfo.chart.Margin;
import org.meteoinfo.chart.axis.Axis;
import org.meteoinfo.chart.axis.LogAxis;
import org.meteoinfo.chart.axis.TimeAxis;
import static org.meteoinfo.chart.plot.Plot.MINIMUM_HEIGHT_TO_DRAW;
import static org.meteoinfo.chart.plot.Plot.MINIMUM_WIDTH_TO_DRAW;
import org.meteoinfo.drawing.Draw;
import static org.meteoinfo.drawing.Draw.getDashPattern;
import org.meteoinfo.global.Extent;
import org.meteoinfo.global.PointF;
import org.meteoinfo.layer.VectorLayer;
import org.meteoinfo.shape.GraphicCollection;
import org.meteoinfo.shape.WindArrow;

/**
 *
 * @author wyq
 */
public abstract class XYPlot extends Plot {

    // <editor-fold desc="Variables">
    private Color background;
    private boolean drawBackground;
    private Color selectColor = Color.yellow;
    private Extent extent;
    private Extent drawExtent;
    private final Map<Location, Axis> axises;
    //private Axis xAxis;
    //private Axis yAxis;
    private Location xAxisLocation;
    private Location yAxisLocation;
    private PlotOrientation orientation;
    private final GridLine gridLine;
    private boolean drawTopAxis;
    private boolean drawRightAxis;
    private boolean drawNeatLine;
    private ChartText title;
    private ChartText subTitle;
    private ChartLegend legend;
    private boolean drawLegend;
    private List<ChartText> texts;
    private ChartWindArrow windArrow;
    private boolean autoAspect = true;
    private double aspect = 1;

    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public XYPlot() {
        super();
        this.background = Color.white;
        this.drawBackground = false;
        this.drawExtent = new Extent(0, 1, 0, 1);
        //this.xAxis = new Axis("X", true);
        //this.yAxis = new Axis("Y", false);
        this.axises = new HashMap<>();
        this.axises.put(Location.BOTTOM, new Axis("X", true, Location.BOTTOM));
        this.axises.put(Location.LEFT, new Axis("Y", false, Location.LEFT));
        this.axises.put(Location.TOP, new Axis("X", true, Location.TOP, false));
        this.axises.put(Location.RIGHT, new Axis("Y", false, Location.RIGHT, false));
        this.xAxisLocation = Location.BOTTOM;
        this.yAxisLocation = Location.RIGHT;
        this.orientation = PlotOrientation.VERTICAL;
        this.gridLine = new GridLine();
        this.drawTopAxis = true;
        this.drawRightAxis = true;
        this.drawNeatLine = false;
        this.texts = new ArrayList<>();
    }
    // </editor-fold>

    // <editor-fold desc="Get Set Methods">
    /**
     * Get title
     *
     * @return Title
     */
    public ChartText getTitle() {
        return this.title;
    }

    /**
     * Set title
     *
     * @param value Title
     */
    public void setTitle(ChartText value) {
        this.title = value;
    }

    /**
     * Set title
     *
     * @param text Title text
     */
    public void setTitle(String text) {
        if (this.title == null) {
            this.title = new ChartText(text);
        } else {
            this.title.setText(text);
        }
    }

    /**
     * Get selected color
     *
     * @return Selected color
     */
    public Color getSelectedColor() {
        return this.selectColor;
    }

    /**
     * Set selected color
     *
     * @param value Selected color
     */
    public void setSelectedColor(Color value) {
        this.selectColor = value;
    }

    /**
     * Get sub title
     *
     * @return Sub title
     */
    public ChartText getSubTitle() {
        return subTitle;
    }

    /**
     * Set sub title
     *
     * @param value Sub title
     */
    public void setSubTitle(ChartText value) {
        subTitle = value;
    }

    /**
     * Get chart legend
     *
     * @return Chart legend
     */
    public ChartLegend getLegend() {
        return this.legend;
    }

    /**
     * Set chart legend
     *
     * @param value Legend
     */
    public void setLegend(ChartLegend value) {
        this.legend = value;
    }

    /**
     * Get if draw legend
     *
     * @return If draw legend
     */
    public boolean isDrawLegend() {
        return this.drawLegend;
    }

    /**
     * Set if draw legend
     *
     * @param value Boolean
     */
    public void setDrawLegend(boolean value) {
        this.drawLegend = value;
        //this.updateLegendScheme();
    }

    /**
     * Get draw extent
     *
     * @return Draw extent
     */
    public Extent getDrawExtent() {
        return this.drawExtent;
    }

    /**
     * Set draw extent
     *
     * @param extent Extent
     */
    public void setDrawExtent(Extent extent) {
        this.drawExtent = extent;
        this.getAxis(Location.BOTTOM).setMinMaxValue(extent.minX, extent.maxX);
        this.getAxis(Location.TOP).setMinMaxValue(extent.minX, extent.maxX);
        this.getAxis(Location.LEFT).setMinMaxValue(extent.minY, extent.maxY);
        this.getAxis(Location.RIGHT).setMinMaxValue(extent.minY, extent.maxY);
    }

    /**
     * Get extent
     *
     * @return Extent
     */
    public Extent getExtent() {
        return this.extent;
    }

    /**
     * Set extent
     *
     * @param extent Extent
     */
    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    /**
     * Update draw extent
     */
    public void updateDrawExtent() {
        this.getAxis(Location.BOTTOM).setMinMaxValue(drawExtent.minX, drawExtent.maxX);
        this.getAxis(Location.TOP).setMinMaxValue(drawExtent.minX, drawExtent.maxX);
        this.getAxis(Location.LEFT).setMinMaxValue(drawExtent.minY, drawExtent.maxY);
        this.getAxis(Location.RIGHT).setMinMaxValue(drawExtent.minY, drawExtent.maxY);
    }

    /**
     * Get background
     *
     * @return Background
     */
    public Color getBackground() {
        return this.background;
    }

    /**
     * Set background
     *
     * @param value Background
     */
    public void setBackground(Color value) {
        this.background = value;
    }

    /**
     * Get if draw background
     *
     * @return Boolean
     */
    public boolean isDrawBackground() {
        return this.drawBackground;
    }

    /**
     * Set if draw background
     *
     * @param value Boolean
     */
    public void setDrawBackground(boolean value) {
        this.drawBackground = value;
    }

    @Override
    public PlotType getPlotType() {
        return PlotType.XY;
    }

    /**
     * Get bottom x axis
     *
     * @return Bottom x aixs
     */
    public Axis getXAxis() {
        return this.axises.get(Location.BOTTOM);
    }

    /**
     * Set x axis
     *
     * @param axis Axis
     */
    public void setXAxis(Axis axis) {
        axis.setLocation(Location.BOTTOM);
        this.axises.put(Location.BOTTOM, axis);
        Axis topAxis = (Axis) axis.clone();
        topAxis.setLocation(Location.TOP);
        this.axises.put(Location.TOP, topAxis);
    }

    /**
     * Get left y axis
     *
     * @return Left y axis
     */
    public Axis getYAxis() {
        return this.axises.get(Location.LEFT);
    }

    /**
     * Set y axis
     *
     * @param axis Axis
     */
    public void setYAxis(Axis axis) {
        axis.setLocation(Location.LEFT);
        this.axises.put(Location.LEFT, axis);
        Axis rightAxis = (Axis) axis.clone();
        rightAxis.setLocation(Location.RIGHT);
        this.axises.put(Location.RIGHT, rightAxis);
    }

    /**
     * Get axis
     *
     * @param loc Axis location
     * @return Axis
     */
    public Axis getAxis(Location loc) {
        return this.axises.get(loc);
    }

    /**
     * Get x axis location
     *
     * @return X axis location
     */
    public Location getXAxisLocation() {
        return this.xAxisLocation;
    }

    /**
     * Set x axis location
     *
     * @param value X axis location
     */
    public void setXAxisLocation(Location value) {
        this.xAxisLocation = value;
    }

    /**
     * Get y axis location
     *
     * @return Y axis location
     */
    public Location getYAxisLocation() {
        return this.yAxisLocation;
    }

    /**
     * Set y axis location
     *
     * @param value Y axis location
     */
    public void setYAxisLocation(Location value) {
        this.yAxisLocation = value;
    }

    /**
     * Get plot orientation
     *
     * @return Plot orientation
     */
    public PlotOrientation getPlotOrientation() {
        return this.orientation;
    }

    /**
     * Set plot orientation
     *
     * @param value Plot orientation
     */
    public void setPlotOrientation(PlotOrientation value) {
        this.orientation = value;
    }

    /**
     * Get grid line
     *
     * @return Grid line
     */
    public GridLine getGridLine() {
        return this.gridLine;
    }

    /**
     * get if draw top axis
     *
     * @return Boolean
     */
    public boolean isDrawTopAxis() {
        return this.drawTopAxis;
    }

    /**
     * Set if draw top right axis
     *
     * @param value Boolean
     */
    public void setDrawTopAxis(boolean value) {
        this.drawTopAxis = value;
    }

    /**
     * get if draw right axis
     *
     * @return Boolean
     */
    public boolean isDrawRightAxis() {
        return this.drawRightAxis;
    }

    /**
     * Set if draw right axis
     *
     * @param value Boolean
     */
    public void setDrawRightAxis(boolean value) {
        this.drawRightAxis = value;
    }

    /**
     * Get if draw neat line
     *
     * @return Boolean
     */
    public boolean isDrawNeatLine() {
        return this.drawNeatLine;
    }

    /**
     * Set if draw neat line
     *
     * @param value Boolean
     */
    public void setDrawNeatLine(boolean value) {
        this.drawNeatLine = value;
    }

    /**
     * Get texts
     *
     * @return Texts
     */
    public List<ChartText> getTexts() {
        return this.texts;
    }

    /**
     * Set texts
     *
     * @param value texts
     */
    public void setTexts(List<ChartText> value) {
        this.texts = value;
    }

    /**
     * Get wind arrow
     *
     * @return Wind arrow
     */
    public ChartWindArrow getWindArrow() {
        return this.windArrow;
    }

    /**
     * Set wind arrow
     *
     * @param value Wind arrow
     */
    public void setWindArrow(ChartWindArrow value) {
        this.windArrow = value;
    }

    /**
     * Get x axis is log or not
     *
     * @return Boolean
     */
    public boolean isLogX() {
        Axis xAxis = this.getXAxis();
        return xAxis instanceof LogAxis;
    }

    /**
     * Get y axis is log or not
     *
     * @return Boolean
     */
    public boolean isLogY() {
        Axis yAxis = this.getYAxis();
        return yAxis instanceof LogAxis;
    }

    /**
     * Get is auto aspect or not
     *
     * @return Boolean
     */
    public boolean isAutoAspect() {
        return this.autoAspect;
    }

    /**
     * Set is auto aspect or not
     *
     * @param value Boolean
     */
    public void setAutoAspect(boolean value) {
        this.autoAspect = value;
    }

    /**
     * Get aspect - scaling from data to plot units for x and y
     *
     * @return Aspect
     */
    public double getAspect() {
        return this.aspect;
    }

    /**
     * Set aspect
     *
     * @param value Aspect
     */
    public void setAspect(double value) {
        this.aspect = value;
    }
    
    /**
     * Get if y axis is reverse or not
     * @return Boolean
     */
    public boolean isYReverse(){
        return this.getYAxis().isInverse();
    }
    
     /**
     * Get if x axis is reverse or not
     * @return Boolean
     */
    public boolean isXReverse(){
        return this.getXAxis().isInverse();
    }

    // </editor-fold>
    // <editor-fold desc="Method">
    /**
     * Set axis
     *
     * @param axis The axis
     * @param loc Axis location
     */
    public void setAxis(Axis axis, Location loc) {
        this.axises.put(loc, axis);
    }

    /**
     * Set axis label font
     *
     * @param font Font
     */
    public void setAxisLabelFont(Font font) {
        for (Axis axis : this.axises.values()) {
            axis.setTickLabelFont(font);
        }
    }

    /**
     * Set all axis visible or not
     *
     * @param value Boolean
     */
    public void setAxisOn(boolean value) {
        for (Axis axis : this.axises.values()) {
            axis.setVisible(value);
        }
    }

    /**
     * Set axis tick line inside box or not
     *
     * @param isInside Inside box ot not
     */
    public void setInsideTick(boolean isInside) {
        this.getAxis(Location.LEFT).setInsideTick(isInside);
        this.getAxis(Location.RIGHT).setInsideTick(isInside);
        this.getAxis(Location.TOP).setInsideTick(isInside);
        this.getAxis(Location.BOTTOM).setInsideTick(isInside);
    }

    /**
     * Get is inside tick line or not
     *
     * @return Is inside or not
     */
    public boolean isInsideTick() {
        return this.getAxis(Location.BOTTOM).isInsideTick();
    }

    /**
     * Draw plot
     *
     * @param g Graphics2D
     * @param area Drawing area
     */
    @Override
    public void draw(Graphics2D g, Rectangle2D area) {
        // if the plot area is too small, just return...
        boolean b1 = (area.getWidth() <= MINIMUM_WIDTH_TO_DRAW);
        boolean b2 = (area.getHeight() <= MINIMUM_HEIGHT_TO_DRAW);
        if (b1 || b2) {
            return;
        }

//        AffineTransform oldMatrix = g.getTransform();
//        Rectangle oldRegion = g.getClipBounds();
//        g.setClip(area);
//        g.translate(area.getX(), area.getY());
        //Get graphic area
        //graphArea = this.getGraphArea(g, area);
        //graphArea = this.getPositionArea(g, area);
        Rectangle2D graphArea;
        if (this.isAutoPosition()) {
            graphArea = this.getGraphArea(g, area);
        } else {
            //graphArea = this.getPositionArea(this.getPositionAreaZoom());
            graphArea = this.getPositionArea(g, area);
        }
        this.setGraphArea(graphArea);

        //Draw title
        //float y = 5;
        float y = (float) graphArea.getY();
        if (title != null) {
            g.setColor(title.getColor());
            g.setFont(title.getFont());
            //float x = (float) area.getWidth() / 2;
            float x = (float) (graphArea.getX() + graphArea.getWidth() / 2);
            //FontMetrics metrics = g.getFontMetrics(title.getFont());
            //y += metrics.getHeight();
            y -= this.title.getHeight(g) - 5;
            //y -= dim.height * 2 / 3;
            //g.drawString(title.getText(), x, y);
            for (String text : title.getTexts()) {
                Dimension dim = Draw.getStringDimension(text, g);
                Draw.drawString(g, text, x - dim.width / 2, y);
                g.setFont(title.getFont());
                y += dim.height + title.getLineSpace();
            }
        }

//        //Update legend scheme
//        this.updateLegendScheme();
        //Draw grid lines        
        if (graphArea.getWidth() < 10 || graphArea.getHeight() < 10) {
//            g.setTransform(oldMatrix);
//            g.setClip(oldRegion);
            return;
        }

        //Draw graph        
        this.drawGraph(g, graphArea);

        //Draw grid line
        this.drawGridLine(g, graphArea);

        //Draw neat line
        if (this.drawNeatLine) {
            g.setStroke(new BasicStroke(1.0f));
            g.setColor(Color.black);
            g.draw(graphArea);
        }

        //Draw axis
        this.drawAxis(g, graphArea);

        //Draw text
        this.drawText(g, graphArea);

        //Draw legend
        if (this.drawLegend && this.getLegend() != null) {
            Object rendering = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//            switch (this.legend.getPosition()) {
//                case UPPER_CENTER_OUTSIDE:
//                case LOWER_CENTER_OUTSIDE:
//                    this.legend.setPlotOrientation(PlotOrientation.HORIZONTAL);
//                    break;
//                default:
//                    this.legend.setPlotOrientation(PlotOrientation.VERTICAL);
//                    break;
//            }
            if (this.legend.isColorbar()) {
                if (this.legend.getPlotOrientation() == PlotOrientation.VERTICAL) {
                    this.legend.setHeight((int) (graphArea.getHeight() * this.legend.getShrink()));
                } else {
                    this.legend.setWidth((int) (graphArea.getWidth() * this.legend.getShrink()));
                }
            }
            if (this.legend.getPosition() == LegendPosition.CUSTOM) {
                this.legend.getLegendDimension(g, new Dimension((int) area.getWidth(), (int) area.getHeight()));
                float x = (float) (area.getWidth() * this.legend.getX());
                y = (float) (area.getHeight() * (1 - (this.getLegend().getHeight() / area.getHeight())
                        - this.getLegend().getY()));
                this.legend.draw(g, new PointF(x, y));
            } else {
                this.drawLegendScheme(g, graphArea, y);
            }
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, rendering);
        }

        //Draw wind arrow - quiverkey
        if (this.getWindArrow() != null) {
            Object rendering = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ChartWindArrow wa = this.getWindArrow();
            float zoom = 1.0f;
            if (wa.getLayer() != null) {
                if (wa.getLayer() instanceof VectorLayer) {
                    zoom = ((VectorLayer) wa.getLayer()).getDrawingZoom();
                } else if (wa.getLayer() instanceof GraphicCollection) {
                    zoom = ((GraphicCollection) wa.getLayer()).getArrowZoom();
                }
            }
            float x = (float) (area.getWidth() * wa.getX());
            y = (float) (area.getHeight() * (1 - wa.getY()));
            WindArrow aArraw = wa.getWindArrow();
            Font drawFont = wa.getFont();
            g.setFont(drawFont);
            String drawStr = wa.getLabel();
            Dimension dim = Draw.getStringDimension(drawStr, g);
            if (wa.isFill() || wa.isDrawNeatline()) {
                Rectangle2D rect = Draw.getArrawBorder(new PointF(x, y), aArraw, g, zoom);
                double gap = 5;
                double width = Math.max(rect.getWidth(), dim.getWidth());
                rect.setRect(rect.getX() - gap, rect.getY() - gap, width + gap * 2,
                        rect.getHeight() + dim.height + gap * 2);
                if (wa.isFill()) {
                    g.setColor(wa.getBackground());
                    g.fill(rect);
                }
                if (wa.isDrawNeatline()) {
                    g.setColor(wa.getNeatlineColor());
                    g.draw(rect);
                }
            }
            Draw.drawArraw(wa.getColor(), new PointF(x, y), aArraw, g, zoom);
            g.setColor(wa.getLabelColor());
            Draw.drawString(g, drawStr, x, y + dim.height);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, rendering);
        }

//        g.setTransform(oldMatrix);
//        g.setClip(oldRegion);
    }

    /**
     * Get tight inset area
     *
     * @param g Graphics2D
     * @param positionArea Position area
     * @return Tight inset area
     */
    @Override
    public Margin getTightInset(Graphics2D g, Rectangle2D positionArea) {
        int left = 2, bottom = 2, right = 2, top = 5;
        int space = 2;

        if (this.title != null) {
            top += this.title.getHeight(g) + 10;
        }

        if (this.drawLegend && this.getLegend() != null) {
            Dimension dim = this.legend.getLegendDimension(g, new Dimension((int) positionArea.getWidth(),
                    (int) positionArea.getHeight()));
            switch (this.legend.getPosition()) {
                case UPPER_CENTER_OUTSIDE:
                    top += dim.height + 10;
                    break;
                case LOWER_CENTER_OUTSIDE:
                    bottom += dim.height + 10;
                    break;
                case LEFT_OUTSIDE:
                    left += dim.width + 10;
                    break;
                case RIGHT_OUTSIDE:
                    right += dim.width + 10;
                    break;
            }
        }

        //Get x axis space
        bottom += this.getXAxisHeight(g, space);

        //Get y axis space
        left += this.getYAxisWidth(g, space);

        //Set right space
        if (this.getXAxis().isVisible()) {
            if (this.getXAxis().isDrawTickLabel()) {
                right += this.getXAxis().getMaxLabelLength(g) / 2;
            }
        }

        return new Margin(left, right, top, bottom);
    }

    /**
     * Get tight inset area
     *
     * @param g Graphics2D
     * @param positionArea Position area
     * @return Tight inset area
     */
    public Rectangle2D getTightInsetArea(Graphics2D g, Rectangle2D positionArea) {
        int left = 0, bottom = 0, right = 5, top = 5;
        int space = 1;

        if (this.title != null) {
            g.setFont(this.title.getFont());
            Dimension dim = Draw.getStringDimension(this.title.getText(), g);
            top += dim.getHeight() + 10;
        }

        if (this.drawLegend && this.getLegend() != null) {
            Dimension dim = this.legend.getLegendDimension(g, new Dimension((int) positionArea.getWidth(),
                    (int) positionArea.getHeight()));
            switch (this.legend.getPosition()) {
                case UPPER_CENTER_OUTSIDE:
                    top += dim.height + 10;
                    break;
                case LOWER_CENTER_OUTSIDE:
                    bottom += dim.height + 10;
                    break;
                case LEFT_OUTSIDE:
                    left += dim.width + 10;
                    break;
                case RIGHT_OUTSIDE:
                    right += dim.width + 10;
                    break;
            }
        }

        //Get x axis space
        bottom += this.getXAxisHeight(g, space);

        //Get y axis space
        left += this.getYAxisWidth(g, space);

        //Set right space
        if (this.getXAxis().isVisible()) {
            if (this.getXAxis().isDrawTickLabel()) {
                right += this.getXAxis().getMaxLabelLength(g) / 2;
            }
        }

        double x = positionArea.getX() - left;
        double y = positionArea.getY() - top;
        double w = positionArea.getWidth() + left + right;
        double h = positionArea.getHeight() + top + bottom;

        return new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Get position area
     *
     * @param g Graphic2D
     * @param area Whole area
     * @return Position area
     */
    @Override
    public Rectangle2D getPositionArea(Graphics2D g, Rectangle2D area) {
        if (this.autoAspect) {
            return this.getPositionArea();
        } else {
            Rectangle2D plotArea = this.getPositionArea();
            double width = this.drawExtent.getWidth();
            double height = this.drawExtent.getHeight();
            if (width / height / aspect > plotArea.getWidth() / plotArea.getHeight()) {
                double h = plotArea.getWidth() * height * aspect / width;
                double delta = plotArea.getHeight() - h;
                plotArea.setRect(plotArea.getX(), plotArea.getY() + delta / 2, plotArea.getWidth(), h);
            } else {
                double w = width * plotArea.getHeight() / height / aspect;
                double delta = plotArea.getWidth() - w;
                plotArea.setRect(plotArea.getX() + delta / 2, plotArea.getY(), w, plotArea.getHeight());
            }

            return plotArea;
        }
    }

    /**
     * Get position area
     *
     * @param g Graphic2D
     * @param area Whole area
     * @return Position area
     */
    @Override
    public Rectangle2D getPositionAreaOrigin(Graphics2D g, Rectangle2D area) {
        double x = area.getWidth() * this.getPosition().getX() + area.getX();
        double y = area.getHeight() * (1 - this.getPosition().getHeight() - this.getPosition().getY()) + area.getY();
        double w = area.getWidth() * this.getPosition().getWidth();
        double h = area.getHeight() * this.getPosition().getHeight();
        return new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Get graphic area
     *
     * @param g Graphic2D
     * @param area Whole area
     * @return Graphic area
     */
    public Rectangle2D getGraphArea(Graphics2D g, Rectangle2D area) {
        int left = 5, bottom = 5, right = 5, top = 5;
        int space = 5;

        if (this.title != null) {
            top += this.title.getHeight(g) + 10;
        }

        if (this.drawLegend && this.getLegend() != null) {
            Dimension dim = this.legend.getLegendDimension(g, new Dimension((int) area.getWidth(), (int) area.getHeight()));
            switch (this.legend.getPosition()) {
                case UPPER_CENTER_OUTSIDE:
                    top += dim.height + 10;
                    break;
                case LOWER_CENTER_OUTSIDE:
                    bottom += dim.height + 10;
                    break;
                case LEFT_OUTSIDE:
                    left += dim.width + 10;
                    break;
                case RIGHT_OUTSIDE:
                    right += dim.width + 10;
                    break;
            }
        }

        //Get x axis space
        bottom += this.getXAxisHeight(g, space);

        //Get y axis space
        left += this.getYAxisWidth(g, space);

        //Set right space
        if (this.getXAxis().isVisible()) {
            if (this.getXAxis().isDrawTickLabel()) {
                right += this.getXAxis().getMaxLabelLength(g) / 2;
            }
        }

        //Set area
        Rectangle2D plotArea = new Rectangle2D.Double(left, top,
                area.getWidth() - left - right, area.getHeight() - top - bottom);
        return plotArea;
    }

    int getXAxisHeight(Graphics2D g, int space) {
        Axis xAxis = this.getXAxis();
        if (!xAxis.isVisible()) {
            return 0;
        }

        int height = space;
        if (xAxis.isDrawTickLabel()) {
            g.setFont(xAxis.getTickLabelFont());
            String maxLabel = xAxis.getMaxLenLable();
            Dimension dim = Draw.getStringDimension(maxLabel, g);
            if (xAxis.getTickLabelAngle() == 0) {
                height += dim.height + space;
            } else {
                height += dim.height + space + (int) (dim.getWidth()
                        * Math.sin(xAxis.getTickLabelAngle() * Math.PI / 180));
            }
            if (xAxis instanceof TimeAxis) {
                height += dim.height + space;
            }
        }
        if (!xAxis.isInsideTick()) {
            height += xAxis.getTickLength();
        }
        if (xAxis.isDrawLabel()) {
            g.setFont(xAxis.getLabelFont());
            Dimension dim = Draw.getStringDimension(xAxis.getLabel(), g);
            height += dim.height + space;
        }

        return height;
    }

    int getYAxisWidth(Graphics2D g, int space) {
        Axis yAxis = this.getYAxis();
        if (!yAxis.isVisible()) {
            return 0;
        }

        int width = space;
        if (yAxis.isDrawTickLabel()) {
            width += yAxis.getMaxLabelLength(g) + space + space;
        }
        if (!yAxis.isInsideTick()) {
            width += this.getYAxis().getTickLength();
        }
        if (yAxis.isDrawLabel()) {
            g.setFont(yAxis.getLabelFont());
            Dimension dim = Draw.getStringDimension(yAxis.getLabel(), g);
            width += dim.height + 10 - space;
        }

        return width;
    }

    void drawGridLine(Graphics2D g, Rectangle2D area) {
        if (!this.gridLine.isDrawXLine() && !this.gridLine.isDrawYLine()) {
            return;
        }

        double[] xy;
        double x, y;
        double miny = area.getY();
        double minx = area.getX();
        double maxx = area.getX() + area.getWidth();
        double maxy = area.getY() + area.getHeight();

        float[] dashPattern = getDashPattern(this.gridLine.getStyle());
        g.setColor(this.gridLine.getColor());
        g.setStroke(new BasicStroke(this.gridLine.getSize(), BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                10.0f, dashPattern, 0.0f));

        //Draw x grid lines
        if (this.gridLine.isDrawXLine()) {
            this.getXAxis().updateLabelGap(g, area);
            int n = 0;
            while (n < this.getXAxis().getTickValues().length) {
                double value = this.getXAxis().getTickValues()[n];
                xy = this.projToScreen(value, this.drawExtent.minY, area);
                x = xy[0];
                if (this.getXAxis().isInverse()) {
                    x = area.getWidth() - x;
                }
                x += minx;
                g.draw(new Line2D.Double(x, maxy, x, miny));

                n += this.getXAxis().getTickLabelGap();
            }
        }

        //Draw y grid lines
        if (this.gridLine.isDrawYLine()) {
            this.getYAxis().updateLabelGap(g, area);
            int n = 0;
            while (n < this.getYAxis().getTickValues().length) {
                double value = this.getYAxis().getTickValues()[n];
                xy = this.projToScreen(this.drawExtent.minX, value, area);
                y = xy[1];
                if (this.getYAxis().isInverse()) {
                    y = area.getHeight() - y;
                }
                y += area.getY();
                g.draw(new Line2D.Double(minx, y, maxx, y));

                n += this.getYAxis().getTickLabelGap();
            }
        }
    }

    abstract void drawGraph(Graphics2D g, Rectangle2D area);

    void drawAxis(Graphics2D g, Rectangle2D area) {
        for (Location loc : this.axises.keySet()) {
            Axis axis = this.axises.get(loc);
            if (axis.isVisible()) {
                axis.updateLabelGap(g, area);
                axis.draw(g, area, this);
            }
        }
    }

    void drawText(Graphics2D g, Rectangle2D area) {
        for (ChartText text : this.getTexts()) {
            drawText(text, g, area);
        }
    }

    void drawText(ChartText text, Graphics2D g, Rectangle2D area) {
        float x, y;
        switch (text.getCoordinates()) {
            case AXES:
                AffineTransform oldMatrix = g.getTransform();
                Rectangle oldRegion = g.getClipBounds();
                g.setClip(area);
                g.translate(area.getX(), area.getY());
                x = (float) (area.getWidth() * text.getX());
                y = (float) (area.getHeight() * (1 - text.getY()));
                g.setFont(text.getFont());
                g.setColor(text.getColor());
                Draw.drawString(g, text.getText(), x, y);
                g.setTransform(oldMatrix);
                g.setClip(oldRegion);
                break;
            case FIGURE:
                x = (float) (area.getWidth() * text.getX());
                y = (float) (area.getHeight() * (1 - text.getY()));
                g.setFont(text.getFont());
                g.setColor(text.getColor());
                Draw.drawString(g, text.getText(), x, y);
                break;
            case DATA:
                oldMatrix = g.getTransform();
                oldRegion = g.getClipBounds();
                g.setClip(area);
                g.translate(area.getX(), area.getY());
                double[] xy = this.projToScreen(text.getX(), text.getY(), area);
                x = (float) xy[0];
                y = (float) xy[1];
                g.setFont(text.getFont());
                g.setColor(text.getColor());
                Draw.drawString(g, text.getText(), x, y);
                g.setTransform(oldMatrix);
                g.setClip(oldRegion);
                break;
        }
    }

    void drawLegendScheme(Graphics2D g, Rectangle2D area, float y) {
        g.setFont(this.legend.getTickFont());
        Dimension dim = this.legend.getLegendDimension(g, new Dimension((int) area.getWidth(), (int) area.getHeight()));
        float x = 0;
        //Rectangle2D graphArea = this.getPositionArea();
        switch (this.legend.getPosition()) {
            case UPPER_CENTER_OUTSIDE:
                x = (float) (area.getX() + area.getWidth() / 2 - dim.width / 2);
                y += 5;
                break;
            case LOWER_CENTER_OUTSIDE:
                x = (float) (area.getX() + area.getWidth() / 2 - dim.width / 2);
                y = (float) (area.getY() + area.getHeight() + this.getXAxisHeight(g, 1) + 10);
                break;
            case LEFT_OUTSIDE:
                x = 10;
                y = (float) area.getHeight() / 2 - dim.height / 2;
                break;
            case RIGHT_OUTSIDE:
                x = (float) area.getX() + (float) area.getWidth() + 10;
                y = (float) area.getY() + (float) area.getHeight() / 2 - dim.height / 2;
                break;
            case UPPER_CENTER:
                x = (float) (area.getX() + area.getWidth() / 2 - dim.width / 2);
                y = (float) area.getY() + 10;
                break;
            case UPPER_RIGHT:
                x = (float) (area.getX() + area.getWidth()) - dim.width - 10;
                y = (float) area.getY() + 10;
                break;
            case LOWER_CENTER:
                x = (float) (area.getX() + area.getWidth() / 2 - dim.width / 2);
                y = (float) (area.getY() + area.getHeight()) - dim.height - 10;
                break;
            case LOWER_RIGHT:
                x = (float) (area.getX() + area.getWidth()) - dim.width - 10;
                y = (float) (area.getY() + area.getHeight()) - dim.height - 10;
                break;
            case UPPER_LEFT:
                x = (float) area.getX() + 10;
                y = (float) area.getY() + 10;
                break;
            case LOWER_LEFT:
                x = (float) area.getX() + 10;
                y = (float) (area.getY() + area.getHeight()) - dim.height - 10;
                break;
        }
        this.legend.draw(g, new PointF(x, y));
    }

    /**
     * Convert coordinate from map to screen
     *
     * @param projX Map X
     * @param projY Map Y
     * @param area Drawing area
     * @return Screen X/Y array
     */
    public double[] projToScreen(double projX, double projY, Rectangle2D area) {
        double width = drawExtent.getWidth();
        double height = drawExtent.getHeight();
        if (this.isLogY()) {
            height = Math.log10(drawExtent.maxY) - Math.log10(drawExtent.minY);
        }
        if (this.isLogX()) {
            width = Math.log10(drawExtent.maxX) - Math.log10(drawExtent.minX);
        }
        double scaleX = area.getWidth() / width;
        double scaleY = area.getHeight() / height;
        double screenX = (projX - drawExtent.minX) * scaleX;
        double screenY = (drawExtent.maxY - projY) * scaleY;
        if (this.isLogY()) {
            screenY = (Math.log10(drawExtent.maxY) - Math.log10(projY)) * scaleY;
        }
        if (this.isLogX()) {
            screenX = (Math.log10(projX) - Math.log10(drawExtent.minX)) * scaleX;
        }
        if (this.isYReverse()){
            screenY = area.getHeight() - screenY;
        }
        if (this.isXReverse()){
            screenX = area.getWidth() - screenX;
        }

        return new double[]{screenX, screenY};
    }

    /**
     * Convert data length to screen length in x direction
     *
     * @param len data length
     * @param area Drawing area
     * @return Screen length
     */
    public double projXLength(double len, Rectangle2D area) {
        double scaleX = area.getWidth() / drawExtent.getWidth();
        return len * scaleX;
    }

    /**
     * Convert data length to screen length in y direction
     *
     * @param len data length
     * @param area Drawing area
     * @return Screen length
     */
    public double projYLength(double len, Rectangle2D area) {
        double scaleY = area.getHeight() / drawExtent.getHeight();
        return len * scaleY;
    }

    /**
     * Convert coordinate from screen to map
     *
     * @param screenX Screen X
     * @param screenY Screen Y
     * @param area Area
     * @return Projected X/Y
     */
    public double[] screenToProj(double screenX, double screenY, Rectangle2D area) {
        double width = drawExtent.getWidth();
        double height = drawExtent.getHeight();
        if (this.isLogY()) {
            height = Math.log10(drawExtent.maxY) - Math.log10(drawExtent.minY);
        }
        if (this.isLogX()) {
            width = Math.log10(drawExtent.maxX) - Math.log10(drawExtent.minX);
        }
        if (this.isYReverse()){
            screenY = area.getHeight() - screenY;
        }
        if (this.isXReverse()){
            screenX = area.getWidth() - screenX;
        }
        double scaleX = area.getWidth() / width;
        double scaleY = area.getHeight() / height;
        double projX = screenX / scaleX + drawExtent.minX;
        double projY = drawExtent.maxY - screenY / scaleY;
        if (this.isLogY()) {
            projY = Math.pow(10, Math.log10(drawExtent.maxY) - screenY / scaleY);
        }
        if (this.isLogX()) {
            projX = Math.pow(10, screenX / scaleX + Math.log10(drawExtent.minX));
        }

        return new double[]{projX, projY};
    }

    abstract Extent getAutoExtent();

    public abstract void setAutoExtent();

    public abstract void updateLegendScheme();

    /**
     * Zoom to screen extent
     *
     * @param minX Minimum x
     * @param maxX Maximum x
     * @param minY Minimum y
     * @param maxY Maximum y
     */
    public void zoomToExtentScreen(double minX, double maxX, double minY, double maxY) {
        double[] pMin = screenToProj(minX, maxY, this.getGraphArea());
        double[] pMax = screenToProj(maxX, minY, this.getGraphArea());
        this.setDrawExtent(new Extent(pMin[0], pMax[0], pMin[1], pMax[1]));
    }

    /**
     * Add text
     *
     * @param text Chart text
     */
    public void addText(ChartText text) {
        this.getTexts().add(text);
    }
    // </editor-fold>
}
