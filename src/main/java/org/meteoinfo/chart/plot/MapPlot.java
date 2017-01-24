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
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import org.meteoinfo.chart.ChartText;
import org.meteoinfo.chart.Location;
import org.meteoinfo.chart.axis.LonLatAxis;
import org.meteoinfo.data.Dataset;
import org.meteoinfo.global.Extent;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.PointD;
import org.meteoinfo.global.PointF;
import org.meteoinfo.layer.MapLayer;
import org.meteoinfo.legend.LabelBreak;
import org.meteoinfo.legend.MapFrame;
import org.meteoinfo.legend.PointBreak;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.legend.PolylineBreak;
import org.meteoinfo.map.GridLabel;
import org.meteoinfo.map.MapView;
import org.meteoinfo.projection.KnownCoordinateSystems;
import org.meteoinfo.projection.ProjectionInfo;
import org.meteoinfo.projection.Reproject;
import org.meteoinfo.shape.Graphic;
import org.meteoinfo.shape.PointShape;
import org.meteoinfo.shape.PolygonShape;
import org.meteoinfo.shape.PolylineShape;

/**
 *
 * @author wyq
 */
public class MapPlot extends XYPlot {

    // <editor-fold desc="Variables">
    private MapFrame mapFrame;
    private MapView mapView;
    private boolean antialias;
    private MapLayer selectedLayer;

    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public MapPlot() {
        super();
        this.antialias = false;
        this.setAutoAspect(false);
        this.setXAxis(new LonLatAxis("Longitude", true));
        this.setYAxis(new LonLatAxis("Latitude", false));
        this.getAxis(Location.TOP).setDrawTickLabel(false);
        this.getAxis(Location.RIGHT).setDrawTickLabel(false);
        this.setDrawNeatLine(true);
    }

    /**
     * Constructor
     *
     * @param mapView MapView
     */
    public MapPlot(MapView mapView) {
        this();
        this.setMapView(mapView, true);
        this.mapFrame = new MapFrame();
        this.mapFrame.setMapView(mapView);
    }

    /**
     * Constructor
     *
     * @param mapFrame MapFrame
     */
    public MapPlot(MapFrame mapFrame) {
        this();
        this.mapFrame = mapFrame;
        this.setMapView(mapFrame.getMapView(), true);
    }

    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    @Override
    public Dataset getDataset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDataset(Dataset dataset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Get map view
     *
     * @return Map view
     */
    public MapView getMapView() {
        return this.mapView;
    }

    /**
     * Set map view
     *
     * @param value Map view
     * @param isGeoMap If is geo map
     */
    public void setMapView(MapView value, boolean isGeoMap) {
        this.mapView = value;
        this.mapView.setGeoMap(isGeoMap);
        this.mapView.setMultiGlobalDraw(isGeoMap);
        Extent extent = this.getAutoExtent();
        this.setDrawExtent(extent);
    }

    @Override
    public PlotType getPlotType() {
        return PlotType.XY2D;
    }

    /**
     * Get if is antialias
     *
     * @return Boolean
     */
    public boolean isAntialias() {
        return this.antialias;
    }

    /**
     * Set if is antialias
     *
     * @param value Boolean
     */
    public void setAntialias(boolean value) {
        this.antialias = value;
    }

    /**
     * Get background color
     *
     * @return Background color
     */
    @Override
    public Color getBackground() {
        return this.mapView.getBackground();
    }

    /**
     * Set background color
     *
     * @param value Background color
     */
    @Override
    public void setBackground(Color value) {
        this.mapView.setBackground(value);
    }

    /**
     * Get map frame
     *
     * @return Map frame
     */
    public MapFrame getMapFrame() {
        return this.mapFrame;
    }

    /**
     * Set map frame
     *
     * @param value Map frame
     */
    public void setMapFrame(MapFrame value) {
        this.mapFrame = value;
        this.setMapView(mapFrame.getMapView(), true);
    }

    /**
     * Get projection info
     *
     * @return Projection info
     */
    public ProjectionInfo getProjInfo() {
        return this.getMapView().getProjection().getProjInfo();
    }

    /**
     * Is lon/lat map or not
     *
     * @return Boolean
     */
    public boolean isLonLatMap() {
        return this.getMapView().getProjection().isLonLatMap();
    }

    /**
     * Get selected layer
     *
     * @return Selected layer
     */
    public MapLayer getSelectedLayer() {
        if (this.selectedLayer != null) {
            return this.selectedLayer;
        } else if (this.mapView.getLastAddedLayer() != null) {
            return this.mapView.getLastAddedLayer();
        } else {
            return null;
        }
    }

    /**
     * Set selected layer
     *
     * @param value Selected layer
     */
    public void setSelectedLayer(MapLayer value) {
        this.selectedLayer = value;
    }

    // </editor-fold>
    // <editor-fold desc="Methods">
    @Override
    void drawGraph(Graphics2D g, Rectangle2D area) {
        this.mapView.setAntiAlias(this.antialias);
        this.mapView.setViewExtent((Extent) this.getDrawExtent().clone());
        this.mapView.paintGraphics(g, area);
    }

    /**
     * Get auto extent
     *
     * @return Auto extent
     */
    @Override
    public Extent getAutoExtent() {
        return this.mapView.getLayersWholeExtent();
    }

    @Override
    public void setAutoExtent() {

    }

    @Override
    public void updateLegendScheme() {

    }

    /**
     * Add a graphic
     *
     * @param graphic The graphic
     */
    public void addGraphic(Graphic graphic) {
        this.getMapView().addGraphic(graphic);
    }

    /**
     * Add a layer
     *
     * @param layer The layer
     */
    public void addLayer(MapLayer layer) {
        this.mapView.addLayer(layer);
        this.setDrawExtent(layer.getExtent());
    }

    /**
     * Add a layer
     *
     * @param idx Index
     * @param layer Layer
     */
    public void addLayer(int idx, MapLayer layer) {
        this.mapView.addLayer(idx, layer);
        this.setDrawExtent(layer.getExtent());
    }

    /**
     * Remove last added layer
     */
    public void removeLastLayer() {
        this.mapView.removeLayer(this.mapView.getLastAddedLayer());
    }

    /**
     * Set all axis visible or not
     *
     * @param value Boolean
     */
    @Override
    public void setAxisOn(boolean value) {
        super.setAxisOn(value);
        this.mapFrame.setDrawGridTickLine(value);
        this.mapFrame.setDrawGridLabel(value);
    }

    /**
     * Set longitude/latitude extent
     *
     * @param extent Extent
     */
    public void setLonLatExtent(Extent extent) {
        if (this.getMapView().getProjection().isLonLatMap()) {
            super.setDrawExtent(extent);
        } else {
            this.getMapView().zoomToExtentLonLatEx(extent);
            super.setDrawExtent(this.getMapView().getViewExtent());
        }
    }

    @Override
    public void addText(ChartText text) {
        if (this.getMapView().getProjection().isLonLatMap()) {
            super.addText(text);
        } else {
            PointShape ps = new PointShape();
            PointD lonlatp = new PointD(text.getX(), text.getY());
            PointD xyp = Reproject.reprojectPoint(lonlatp, KnownCoordinateSystems.geographic.world.WGS1984,
                    this.getMapView().getProjection().getProjInfo());
            ps.setPoint(xyp);
            LabelBreak lb = new LabelBreak();
            lb.setText(text.getText());
            lb.setFont(text.getFont());
            lb.setColor(text.getColor());
            Graphic aGraphic = new Graphic(ps, lb);
            this.getMapView().addGraphic(aGraphic);
        }
    }

    /**
     * Add point graphic
     *
     * @param lat Latitude
     * @param lon Lontitude
     * @param pb Point break
     */
    public void addPoint(double lat, double lon, PointBreak pb) {
        PointShape ps = new PointShape();
        PointD lonlatp = new PointD(lon, lat);
        if (this.getMapView().getProjection().isLonLatMap()) {
            ps.setPoint(lonlatp);
        } else {
            PointD xyp = Reproject.reprojectPoint(lonlatp, KnownCoordinateSystems.geographic.world.WGS1984,
                    this.getMapView().getProjection().getProjInfo());
            ps.setPoint(xyp);
        }
        Graphic aGraphic = new Graphic(ps, pb);
        this.getMapView().addGraphic(aGraphic);
    }

    /**
     * Add point graphic
     *
     * @param lat Latitude
     * @param lon Lontitude
     * @param pb Point break
     * @return Graphic
     */
    public Graphic addPoint(List<Number> lat, List<Number> lon, PointBreak pb) {
        double x, y;
        PointShape ps;
        PointD lonlatp, xyp;
        for (int i = 0; i < lat.size(); i++) {
            ps = new PointShape();
            x = lon.get(i).doubleValue();
            y = lat.get(i).doubleValue();
            lonlatp = new PointD(x, y);
            if (this.getMapView().getProjection().isLonLatMap()) {
                ps.setPoint(lonlatp);
            } else {
                xyp = Reproject.reprojectPoint(lonlatp, KnownCoordinateSystems.geographic.world.WGS1984,
                        this.getMapView().getProjection().getProjInfo());
                ps.setPoint(xyp);
            }
            Graphic aGraphic = new Graphic(ps, pb);
            this.getMapView().addGraphic(aGraphic);
            return aGraphic;
        }
        return null;
    }

    /**
     * Add polyline
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param plb PolylineBreak
     * @return Graphic
     */
    public Graphic addPolyline(List<Number> lat, List<Number> lon, PolylineBreak plb) {
        double x, y;
        PolylineShape pls;
        PointD lonlatp;
        List<PointD> points = new ArrayList<>();
        for (int i = 0; i < lat.size(); i++) {
            x = lon.get(i).doubleValue();
            y = lat.get(i).doubleValue();
            if (Double.isNaN(x)) {
                if (points.size() >= 2) {
                    pls = new PolylineShape();
                    pls.setPoints(points);
                    Graphic aGraphic = new Graphic(pls, plb);
                    this.getMapView().addGraphic(aGraphic);
                }
                points = new ArrayList<>();
            } else {
                lonlatp = new PointD(x, y);
                if (!this.getMapView().getProjection().isLonLatMap()) {
                    lonlatp = Reproject.reprojectPoint(lonlatp, KnownCoordinateSystems.geographic.world.WGS1984,
                            this.getMapView().getProjection().getProjInfo());
                }
                points.add(lonlatp);
            }
        }
        if (points.size() >= 2) {
            pls = new PolylineShape();
            pls.setPoints(points);
            Graphic aGraphic = new Graphic(pls, plb);
            this.getMapView().addGraphic(aGraphic);
            return aGraphic;
        }
        return null;
    }

    /**
     * Add polygon
     *
     * @param lat Latitude
     * @param lon Longitude
     * @param pgb PolygonBreak
     * @return Graphic
     */
    public Graphic addPolygon(List<Number> lat, List<Number> lon, PolygonBreak pgb) {
        double x, y;
        PolygonShape pgs;
        PointD lonlatp;
        List<PointD> points = new ArrayList<>();
        for (int i = 0; i < lat.size(); i++) {
            x = lon.get(i).doubleValue();
            y = lat.get(i).doubleValue();
            if (Double.isNaN(x)) {
                if (points.size() > 2) {
                    pgs = new PolygonShape();
                    pgs.setPoints(points);
                    Graphic aGraphic = new Graphic(pgs, pgb);
                    this.getMapView().addGraphic(aGraphic);
                }
                points = new ArrayList<>();
            } else {
                lonlatp = new PointD(x, y);
                if (!this.getMapView().getProjection().isLonLatMap()) {
                    lonlatp = Reproject.reprojectPoint(lonlatp, KnownCoordinateSystems.geographic.world.WGS1984,
                            this.getMapView().getProjection().getProjInfo());
                }
                points.add(lonlatp);
            }
        }
        if (points.size() > 2) {
            pgs = new PolygonShape();
            pgs.setPoints(points);
            Graphic aGraphic = new Graphic(pgs, pgb);
            this.getMapView().addGraphic(aGraphic);
            return aGraphic;
        }
        return null;
    }

//    /**
//     * Add a layer
//     * @param idx Index
//     * @param layer Layer
//     */
//    public void addLayer(int idx, MapLayer layer){
//        this.mapFrame.addLayer(idx, layer);
//    }
//    
//    /**
//     * Add a layer
//     * @param layer Layer 
//     */
//    public void addLayer(MapLayer layer){
//        this.mapFrame.addLayer(layer);
//    }
//    
//    /**
//     * Set extent
//     *
//     * @param extent Extent
//     */
//    public void setExtent(Extent extent) {
//        this.mapFrame.getMapView().setViewExtent(extent);
//    }
    /**
     * Get position area
     *
     * @param g Graphic2D
     * @param area Whole area
     * @return Graphic area
     */
    @Override
    public Rectangle2D getPositionArea(Graphics2D g, Rectangle2D area) {
        Rectangle2D plotArea = super.getPositionArea(g, area);
        if (!this.isAutoAspect()) {
            MapView mv = this.mapFrame.getMapView();
            mv.setViewExtent((Extent) this.getDrawExtent().clone());
            Extent extent = mv.getViewExtent();
            double width = extent.getWidth();
            double height = extent.getHeight();
            double scaleFactor = mv.getXYScaleFactor();
            if (width / height / scaleFactor > plotArea.getWidth() / plotArea.getHeight()) {
                double h = plotArea.getWidth() * height * scaleFactor / width;
                double delta = plotArea.getHeight() - h;
                plotArea.setRect(plotArea.getX(), plotArea.getY() + delta / 2, plotArea.getWidth(), h);
            } else {
                double w = width * plotArea.getHeight() / height / scaleFactor;
                double delta = plotArea.getWidth() - w;
                plotArea.setRect(plotArea.getX() + delta / 2, plotArea.getY(), w, plotArea.getHeight());
            }          
        }
        return plotArea;
    }

//    @Override
//    public void drawGraph(Graphics2D g, Rectangle2D area) {
//        MapView mapView = this.mapFrame.getMapView();
//        mapView.setViewExtent(this.getDrawExtent());
//        Extent extent = mapView.getViewExtent();
//        double width = extent.getWidth();
//        double height = extent.getHeight();
//        double scaleFactor = mapView.getXYScaleFactor();
//        if (width / height / scaleFactor > area.getWidth() / area.getHeight()){
//            double h = area.getWidth() * height * scaleFactor / width;
//            double delta = area.getHeight() - h;
//            area.setRect(area.getX(), area.getY() + delta / 2, area.getWidth(), h);
//        } else {
//            double w = width * area.getHeight() / height / scaleFactor;
//            double delta = area.getWidth() - w;
//            area.setRect(area.getX() + delta / 2, area.getY(), w, area.getHeight());
//        }
//        mapView.paintGraphics(g, area);
//    }
//    /**
//     * Set draw extent
//     *
//     * @param extent Extent
//     */
//    @Override
//    public void setDrawExtent(Extent extent) {
//        if (this.isLonLatMap()){
//            super.updateDrawExtent();
//        } else {            
//            ((ProjLonLatAxis)this.getAxis(Location.BOTTOM)).setX_Y(extent.minY);
//            ((ProjLonLatAxis)this.getAxis(Location.TOP)).setX_Y(extent.maxY);
//            ((ProjLonLatAxis)this.getAxis(Location.LEFT)).setX_Y(extent.minX);
//            ((ProjLonLatAxis)this.getAxis(Location.RIGHT)).setX_Y(extent.maxX);
//            super.setDrawExtent(extent);
//        }
//    }
//    
//    /**
//     * Update draw extent
//     */
//    @Override
//    public void updateDrawExtent(){
//        if (this.isLonLatMap()){
//            super.updateDrawExtent();
//        } else {
//            Extent extent = this.getDrawExtent();
//            ((ProjLonLatAxis)this.getAxis(Location.BOTTOM)).setX_Y(extent.minY);
//            ((ProjLonLatAxis)this.getAxis(Location.TOP)).setX_Y(extent.maxY);
//            ((ProjLonLatAxis)this.getAxis(Location.LEFT)).setX_Y(extent.minX);
//            ((ProjLonLatAxis)this.getAxis(Location.RIGHT)).setX_Y(extent.maxX);
//            super.updateDrawExtent();
//        }
//    }
        @Override
        void drawAxis
        (Graphics2D g, Rectangle2D area
        
            ) {
        if (this.mapFrame.getMapView().getProjection().isLonLatMap()) {
                super.drawAxis(g, area);
                return;
            }

            //Draw lon/lat grid labels
            if (this.mapFrame.isDrawGridLabel()) {
                List<Extent> extentList = new ArrayList<>();
                Extent maxExtent = new Extent();
                Extent aExtent;
                Dimension aSF;
                g.setColor(this.mapFrame.getGridLineColor());
                g.setStroke(new BasicStroke(this.mapFrame.getGridLineSize()));
                String drawStr;
                PointF sP = new PointF(0, 0);
                PointF eP = new PointF(0, 0);
                Font font = new Font(this.mapFrame.getGridFont().getFontName(), this.mapFrame.getGridFont().getStyle(), (int) (this.mapFrame.getGridFont().getSize()));
                g.setFont(font);
                float labX, labY;
                int len = mapFrame.getTickLineLength();
                int space = len + mapFrame.getGridLabelShift();
                if (mapFrame.isInsideTickLine()) {
                    space = mapFrame.getGridLabelShift();
                }

                for (int i = 0; i < mapFrame.getMapView().getGridLabels().size(); i++) {
                    GridLabel aGL = mapFrame.getMapView().getGridLabels().get(i);
                    switch (mapFrame.getGridLabelPosition()) {
                        case LeftBottom:
                            switch (aGL.getLabDirection()) {
                                case East:
                                case North:
                                    continue;
                            }
                            break;
                        case LeftUp:
                            switch (aGL.getLabDirection()) {
                                case East:
                                case South:
                                    continue;
                            }
                            break;
                        case RightBottom:
                            switch (aGL.getLabDirection()) {
                                case Weast:
                                case North:
                                    continue;
                            }
                            break;
                        case RightUp:
                            switch (aGL.getLabDirection()) {
                                case Weast:
                                case South:
                                    continue;
                            }
                            break;
                    }

                    labX = (float) aGL.getLabPoint().X;
                    labY = (float) aGL.getLabPoint().Y;
                    labX = labX + (float) area.getX();
                    labY = labY + (float) area.getY();
                    sP.X = labX;
                    sP.Y = labY;

                    drawStr = aGL.getLabString();
                    //if (this.drawDegreeSymbol) {
                    if (drawStr.endsWith("E") || drawStr.endsWith("W") || drawStr.endsWith("N") || drawStr.endsWith("S")) {
                        drawStr = drawStr.substring(0, drawStr.length() - 1) + String.valueOf((char) 186) + drawStr.substring(drawStr.length() - 1);
                    } else {
                        drawStr = drawStr + String.valueOf((char) 186);
                    }
                    //}
                    FontMetrics metrics = g.getFontMetrics(font);
                    aSF = new Dimension(metrics.stringWidth(drawStr), metrics.getHeight());
                    switch (aGL.getLabDirection()) {
                        case South:
                            labX = labX - aSF.width / 2;
                            labY = labY + aSF.height * 3 / 4 + space;
                            eP.X = sP.X;
                            if (mapFrame.isInsideTickLine()) {
                                eP.Y = sP.Y - len;
                            } else {
                                eP.Y = sP.Y + len;
                            }
                            break;
                        case Weast:
                            labX = labX - aSF.width - space;
                            labY = labY + aSF.height / 3;
                            eP.Y = sP.Y;
                            if (mapFrame.isInsideTickLine()) {
                                eP.X = sP.X + len;
                            } else {
                                eP.X = sP.X - len;
                            }
                            break;
                        case North:
                            labX = labX - aSF.width / 2;
                            //labY = labY - aSF.height / 3 - space;
                            labY = labY - space;
                            eP.X = sP.X;
                            if (mapFrame.isInsideTickLine()) {
                                eP.Y = sP.Y + len;
                            } else {
                                eP.Y = sP.Y - len;
                            }
                            break;
                        case East:
                            labX = labX + space;
                            labY = labY + aSF.height / 3;
                            eP.Y = sP.Y;
                            if (mapFrame.isInsideTickLine()) {
                                eP.X = sP.X - len;
                            } else {
                                eP.X = sP.X + len;
                            }
                            break;
                    }

                    boolean ifDraw = true;
                    aExtent = new Extent();
                    aExtent.minX = labX;
                    aExtent.maxX = labX + aSF.width;
                    aExtent.minY = labY - aSF.height;
                    aExtent.maxY = labY;

                    //Judge extent                                        
                    if (extentList.isEmpty()) {
                        maxExtent = (Extent) aExtent.clone();
                        extentList.add((Extent) aExtent.clone());
                    } else if (!MIMath.isExtentCross(aExtent, maxExtent)) {
                        extentList.add((Extent) aExtent.clone());
                        maxExtent = MIMath.getLagerExtent(maxExtent, aExtent);
                    } else {
                        for (int j = 0; j < extentList.size(); j++) {
                            if (MIMath.isExtentCross(aExtent, extentList.get(j))) {
                                ifDraw = false;
                                break;
                            }
                        }
                        if (ifDraw) {
                            extentList.add(aExtent);
                            maxExtent = MIMath.getLagerExtent(maxExtent, aExtent);
                        }
                    }

                    if (ifDraw) {
                        g.setColor(mapFrame.getGridLineColor());
                        g.draw(new Line2D.Float(sP.X, sP.Y, eP.X, eP.Y));
                        g.setColor(this.mapFrame.getForeColor());
                        g.drawString(drawStr, labX, labY);
                    }
                }
            }
        }

//    @Override
//    public void zoomToExtentScreen(double minX, double maxX, double minY, double maxY){
//        this.mapView.zoomToExtentScreen(minX, maxX, minY, maxY, 1);
//    }
        // </editor-fold>
    }
