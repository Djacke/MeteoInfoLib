/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.chart.plot;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import org.meteoinfo.chart.ChartText;
import org.meteoinfo.data.Dataset;
import org.meteoinfo.global.Extent;
import org.meteoinfo.global.PointD;
import org.meteoinfo.layer.MapLayer;
import org.meteoinfo.legend.LabelBreak;
import org.meteoinfo.map.MapView;
import org.meteoinfo.shape.Graphic;
import org.meteoinfo.shape.PointShape;

/**
 *
 * @author wyq
 */
public class XY2DPlotBak extends XYPlot {

    // <editor-fold desc="Variables">
    private MapView mapView;
    private boolean antialias;

    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public XY2DPlotBak() {
        super();
        this.antialias = false;
    }

    /**
     * Constructor
     *
     * @param mapView Map view
     */
    public XY2DPlotBak(MapView mapView) {
        this();
        this.setMapView(mapView, false);
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
     * @return Background color
     */
    @Override
    public Color getBackground(){
        return this.mapView.getBackground();
    }
    
    /**
     * Set background color
     * @param value Background color
     */
    @Override
    public void setBackground(Color value){
        this.mapView.setBackground(value);
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
    public void setAutoExtent(){
        
    }

    @Override
    public void updateLegendScheme() {

    }

    @Override
    public void addText(ChartText text) {
        PointShape ps = new PointShape();
        ps.setPoint(new PointD(text.getX(), text.getY()));
        LabelBreak lb = new LabelBreak();
        lb.setText(text.getText());
        lb.setFont(text.getFont());
        lb.setColor(text.getColor());
        Graphic aGraphic = new Graphic(ps, lb);
        this.mapView.addGraphic(aGraphic);
    }
    
    /**
     * Add a graphic
     * @param graphic The graphic
     */
    public void addGraphic(Graphic graphic){
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
    // </editor-fold>            
}
