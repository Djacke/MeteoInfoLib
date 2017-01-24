 /* Copyright 2012 Yaqiang Wang,
 * yaqiang.wang@gmail.com
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or (at
 * your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser
 * General Public License for more details.
 */
package org.meteoinfo.chart;

import org.meteoinfo.drawing.Draw;
import org.meteoinfo.global.PointF;
import org.meteoinfo.legend.ColorBreak;
import org.meteoinfo.legend.LegendScheme;
import org.meteoinfo.legend.PointBreak;
import org.meteoinfo.legend.PolygonBreak;
import org.meteoinfo.legend.PolylineBreak;
import com.l2fprod.common.beans.BaseBeanInfo;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import org.meteoinfo.chart.plot.PlotOrientation;
import org.meteoinfo.global.DataConvert;
import org.meteoinfo.legend.LegendType;
import org.meteoinfo.shape.ShapeTypes;

/**
 *
 * @author Yaqiang Wang
 */
public class ChartLegend {
    // <editor-fold desc="Variables">

    //private final XY1DPlot plot;
    private LegendScheme legendScheme;
    private LegendPosition position;
    private float shrink;
    private int aspect;
    private boolean colorBar;
    private float x;
    private float y;
    private PlotOrientation orientation;
    private Color background;
    private boolean drawBackground;
    private int width;
    private int height;
    private String label;
    private Font labelFont;
    private Color labelColor;
    private Font tickFont;
    private Color tickColor;
    private boolean _drawNeatLine;
    private Color _neatLineColor;
    private float _neatLineSize;
    private float _breakSpace;
    private float _topSpace;
    private float _leftSpace;
    private float _vBarWidth;
    private float _hBarHeight;
    private int rowColNum = 1;
    private Dimension symbolDimension;
    private boolean extendRect;
    private boolean autoExtendFrac;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     *
     * @param ls LegendScheme
     */
    public ChartLegend(LegendScheme ls) {
        //this.plot = plot;
        this.legendScheme = ls;
        this.colorBar = false;
        this.position = LegendPosition.LOWER_CENTER_OUTSIDE;
        this.orientation = PlotOrientation.HORIZONTAL;
        this.shrink = 1.0f;
        this.aspect = 20;
        this.background = Color.white;
        this.drawBackground = false;
        _drawNeatLine = true;
        _neatLineColor = Color.black;
        _neatLineSize = 1;
        _breakSpace = 3;
        _topSpace = 5;
        _leftSpace = 5;
        _vBarWidth = 10;
        _hBarHeight = 10;
        this.labelFont = new Font("Arial", Font.PLAIN, 14);
        this.labelColor = Color.black;
        tickFont = new Font("Arial", Font.PLAIN, 14);
        this.tickColor = Color.black;
        this.symbolDimension = new Dimension(16, 10);
        this.extendRect = true;
        this.autoExtendFrac = false;
    }

    // </editor-fold>
    // <editor-fold desc="Events">
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    /**
     * Get legend scheme
     *
     * @return Legend scheme
     */
    public LegendScheme getLegendScheme() {
        return this.legendScheme;
    }

    /**
     * Set legend scheme
     *
     * @param value Legend scheme
     */
    public void setLegendScheme(LegendScheme value) {
        this.legendScheme = value;
    }

    /**
     * Get if is color bar
     *
     * @return Boolean
     */
    public boolean isColorbar() {
        return this.colorBar;
    }

    /**
     * Set if is color bar
     *
     * @param value Boolean
     */
    public void setColorbar(boolean value) {
        this.colorBar = value;
    }

    /**
     * Get legend position
     *
     * @return Legend position
     */
    public LegendPosition getPosition() {
        return this.position;
    }

    /**
     * Set legend position
     *
     * @param value Legend position
     */
    public void setPosition(LegendPosition value) {
        this.position = value;
    }

    /**
     * Get shrink
     *
     * @return Shrink
     */
    public float getShrink() {
        return this.shrink;
    }

    /**
     * Set shrink
     *
     * @param value Shrink
     */
    public void setShrink(float value) {
        this.shrink = value;
    }

    /**
     * Get aspect
     *
     * @return Aspect
     */
    public int getAspect() {
        return this.aspect;
    }

    /**
     * Set aspect
     *
     * @param value Aspect
     */
    public void setAspect(int value) {
        this.aspect = value;
    }

    /**
     * Get X
     *
     * @return X value
     */
    public float getX() {
        return this.x;
    }

    /**
     * Set X
     *
     * @param value X value
     */
    public void setX(float value) {
        x = value;
    }

    /**
     * Get Y
     *
     * @return Y value
     */
    public float getY() {
        return this.y;
    }

    /**
     * Set Y
     *
     * @param value Y value
     */
    public void setY(float value) {
        y = value;
    }

    /**
     * Get width
     *
     * @return Width
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Set width
     *
     * @param value Width
     */
    public void setWidth(int value) {
        this.width = value;
    }

    /**
     * Get height
     *
     * @return Height
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Set height
     *
     * @param value Height
     */
    public void setHeight(int value) {
        this.height = value;
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

    /**
     * Get if draw neat line
     *
     * @return If draw neat line
     */
    public boolean isDrawNeatLine() {
        return _drawNeatLine;
    }

    /**
     * Set if draw neat line
     *
     * @param istrue If draw neat line
     */
    public void setDrawNeatLine(boolean istrue) {
        _drawNeatLine = istrue;
    }

    /**
     * Get neat line color
     *
     * @return Neat line color
     */
    public Color getNeatLineColor() {
        return _neatLineColor;
    }

    /**
     * Set neat line color
     *
     * @param color Neat line color
     */
    public void setNeatLineColor(Color color) {
        _neatLineColor = color;
    }

    /**
     * Get neat line size
     *
     * @return Neat line size
     */
    public float getNeatLineSize() {
        return _neatLineSize;
    }

    /**
     * Set neat line size
     *
     * @param size Neat line size
     */
    public void setNeatLineSize(float size) {
        _neatLineSize = size;
    }
    
    /**
     * Get label
     * @return Label
     */
    public String getLabel(){
        return this.label;
    }
    
    /**
     * Set label
     * @param value Label
     */
    public void setLabel(String value){
        this.label = value;
    }
    
    /**
     * Get label font
     * @return Label font
     */
    public Font getLabelFont(){
        return this.labelFont;
    }
    
    /**
     * Set label font
     * @param value Label font
     */
    public void setLabelFont(Font value){
        this.labelFont = value;
    }
    
    /**
     * Get label color
     * @return Label color
     */
    public Color getLabelColor(){
        return this.labelColor;
    }
    
    /**
     * Set label color
     * @param value Label color
     */
    public void setLabelColor(Color value){
        this.labelColor = value;
    }

    /**
     * Get Tick font
     *
     * @return The Tick font
     */
    public Font getTickFont() {
        return tickFont;
    }

    /**
     * Set Tick font
     *
     * @param font The Tick font
     */
    public void setTickFont(Font font) {
        tickFont = font;
    }
    
    /**
     * Get tick color
     * @return Tick color
     */
    public Color getTickColor(){
        return this.tickColor;
    }
    
    /**
     * Set tick color
     * @param value Tick color
     */
    public void setTickColor(Color value){
        this.tickColor = value;
    }

    /**
     * Get column number
     *
     * @return Column number
     */
    public int getColumnNumber() {
        return rowColNum;
    }

    /**
     * Set column number
     *
     * @param value Column number
     */
    public void setColumnNumber(int value) {
        rowColNum = value;
    }

    /**
     * Get symbol dimension
     *
     * @return Symbol dimension
     */
    public Dimension getSymbolDimension() {
        return this.symbolDimension;
    }

    /**
     * Set symbol dimension
     *
     * @param value Symbol dimension
     */
    public void setSymbolDimension(Dimension value) {
        this.symbolDimension = value;
    }

    /**
     * Get if extend rectangle - or triangle
     *
     * @return Boolean
     */
    public boolean isExtendRect() {
        return this.extendRect;
    }

    /**
     * Set if extend rectangle - or triangle
     *
     * @param value Boolean
     */
    public void setExtendRect(boolean value) {
        this.extendRect = value;
    }
    
    /**
     * Get if auto set extend fraction - extend has save width and height
     * Only valid for colorbar
     * @return Boolean
     */
    public boolean isAutoExtendFrac(){
        return this.autoExtendFrac;
    }
    
    /**
     * Set if auto set extend fraction - extend has save width and height
     * Only valid for colorbar
     * @param value 
     */
    public void setAutoExtendFrac(boolean value){
        this.autoExtendFrac = value;
    }
    
    /**
     * Set tick labels
     * @param value Tick labels
     */
    public void setTickLabels(List<String> value){
        for (int i = 0; i < this.legendScheme.getBreakNum(); i++){
            if (i < value.size()){
                this.legendScheme.getLegendBreaks().get(i).setCaption(value.get(i));
            } else {
                break;
            }
        }
    }

    // </editor-fold>
    // <editor-fold desc="Methods">
    /**
     * Draw legend
     *
     * @param g Graphics2D
     * @param point Start point
     */
    public void draw(Graphics2D g, PointF point) {

        AffineTransform oldMatrix = g.getTransform();
        g.translate(point.X, point.Y);

        //Draw background color
        if (this.drawBackground) {
            g.setColor(this.background);
            g.fill(new Rectangle.Float(0, 0, this.width, this.height));
        }

        //Draw legend
        if (this.colorBar) {
            switch (this.orientation) {
                case HORIZONTAL:
                    this.drawHorizontalBarLegend(g, legendScheme);
                    break;
                case VERTICAL:
                    this.drawVerticalBarLegend(g, legendScheme);
                    break;
            }
        } else {
            switch (this.orientation) {
                case HORIZONTAL:
                    drawHorizontalLegend(g, legendScheme);
                    break;
                case VERTICAL:
                    this.drawVerticalLegend(g, legendScheme);
                    break;
            }
        }

        //Draw neatline
        if (_drawNeatLine) {
            Rectangle.Float mapRect = new Rectangle.Float(0, 0, this.width, this.height);
            g.setColor(_neatLineColor);
            g.setStroke(new BasicStroke(_neatLineSize));
            g.draw(mapRect);
        }

        g.setTransform(oldMatrix);
    }

    private void drawVerticalLegend(Graphics2D g, LegendScheme aLS) {
        String caption = "";
        Dimension aSF;
        float leftSpace = _leftSpace;
        float breakSpace = _breakSpace;
        float breakHeight = this.getBreakHeight(g);
        float symbolHeight = this.symbolDimension.height;
        float symbolWidth = this.symbolDimension.width;
        float colWidth = symbolWidth + getMaxLabelWidth(g) + 10;

        //Set columns
        int[] rowNums = new int[rowColNum];
        int ave = aLS.getVisibleBreakNum() / rowColNum;
        if (ave * rowColNum < aLS.getBreakNum()) {
            ave += 1;
        }
        int num = 0;
        int i;
        for (i = 1; i < rowColNum; i++) {
            rowNums[i] = ave;
            num += ave;
        }
        rowNums[0] = aLS.getVisibleBreakNum() - num;

        //Draw legend                        
        //Font lFont = new Font(this.labelFont.getFontName(), this.labelFont.getStyle(), (int) (this.labelFont.getSize()));
        float x, y;
        i = 0;
        for (int col = 0; col < rowColNum; col++) {
            x = symbolWidth / 2 + leftSpace + col * colWidth;
            y = breakHeight / 2 + breakSpace;
            for (int row = 0; row < rowNums[col]; row++) {
                if (!aLS.getLegendBreaks().get(i).isDrawShape()) {
                    continue;
                }

                //y += breakHeight + breakSpace;
                ColorBreak cb = aLS.getLegendBreaks().get(i);
                if (!cb.isDrawShape()) {
                    continue;
                }
                caption = aLS.getLegendBreaks().get(i).getCaption();
                if (cb instanceof PointBreak) {
                    PointBreak aPB = (PointBreak) cb;
                    Draw.drawPoint(new PointF(x, y), aPB, g);
                } else if (cb instanceof PolylineBreak) {
                    PolylineBreak aPLB = (PolylineBreak) cb;
                    Draw.drawPolylineSymbol_S(new PointF(x, y), symbolWidth, symbolHeight, aPLB, g);
                } else if (cb instanceof PolygonBreak) {
//                    Draw.drawPolygonSymbol(new PointF(x, y), cb.getColor(), Color.black, symbolWidth,
//                            symbolHeight, true, true, g);
                    Draw.drawPolygonSymbol(new PointF(x, y), symbolWidth, symbolHeight, (PolygonBreak)cb, g);
                }
//                switch (aLS.getShapeType()) {
//                    case Point:
//                        PointBreak aPB = (PointBreak) ((PointBreak) aLS.getLegendBreaks().get(i)).clone();
//                        caption = aPB.getCaption();
//                        aPB.setSize(aPB.getSize());
//                        Draw.drawPoint(new PointF(x, y), aPB, g);
//                        break;
//                    case Polyline:
//                    case PolylineZ:
//                        PolylineBreak aPLB = (PolylineBreak) aLS.getLegendBreaks().get(i);
//                        caption = aPLB.getCaption();
//                        Draw.drawPolylineSymbol_S(new PointF(x, y), symbolWidth, symbolHeight, aPLB, g);
//                        break;
//                    case Polygon:
//                        PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(i);
//                        caption = aPGB.getCaption();
//                        Draw.drawPolygonSymbol(new PointF(x, y), symbolHeight * 8 / 10, symbolHeight * 8 / 10, aPGB, g);
//                        break;
//                    case Image:
//                        ColorBreak aCB = aLS.getLegendBreaks().get(i);
//                        caption = aCB.getCaption();
//                        Draw.drawPolygonSymbol(new PointF(x, y), aCB.getColor(), Color.black, symbolWidth,
//                                symbolHeight, true, true, g);
//                        break;
//                }

                PointF sP = new PointF(0, 0);
                sP.X = x + symbolWidth / 2;
                sP.Y = y;
                //FontMetrics metrics = g.getFontMetrics(lFont);
                //aSF = new Dimension(metrics.stringWidth(caption), metrics.getHeight());                
                g.setColor(this.labelColor);
                g.setFont(this.labelFont);
                aSF = Draw.getStringDimension(caption, g);
                //g.drawString(caption, sP.X + 5, sP.Y + aSF.height / 3);
                //g.drawString(caption, sP.X + 5, sP.Y + aSF.height / 4);
                Draw.drawString(g, caption, sP.X + 5, sP.Y + aSF.height / 4);
                y += breakHeight + breakSpace;

                i += 1;
            }
        }
    }

    private void drawHorizontalLegend(Graphics2D g, LegendScheme aLS) {
        String caption;
        float breakHeight = this.getBreakHeight(g);
        float symbolHeight = this.symbolDimension.height;
        float symbolWidth = this.symbolDimension.width;
        FontMetrics metrics = g.getFontMetrics(labelFont);

        //Set columns
        int[] colNums = new int[rowColNum];
        int ave = aLS.getVisibleBreakNum() / rowColNum;
        if (ave * rowColNum < aLS.getBreakNum()) {
            ave += 1;
        }
        int num = 0;
        int i;
        for (i = 0; i < rowColNum - 1; i++) {
            colNums[i] = ave;
            num += ave;
        }
        colNums[rowColNum - 1] = aLS.getVisibleBreakNum() - num;

        //Draw legend                        
        float x, y;
        y = this._breakSpace + breakHeight / 2;
        i = 0;
        for (int row = 0; row < rowColNum; row++) {
            x = this.symbolDimension.width / 2 + 5;
            for (int col = 0; col < colNums[row]; col++) {
                if (i >= aLS.getBreakNum()) {
                    break;
                }

                ColorBreak cb = aLS.getLegendBreaks().get(i);
                if (!cb.isDrawShape()) {
                    continue;
                }
                caption = aLS.getLegendBreaks().get(i).getCaption();
                if (cb instanceof PointBreak) {
                    PointBreak aPB = (PointBreak) cb;
                    Draw.drawPoint(new PointF(x, y), aPB, g);
                } else if (cb instanceof PolylineBreak) {
                    PolylineBreak aPLB = (PolylineBreak) cb;
                    Draw.drawPolylineSymbol_S(new PointF(x, y), symbolWidth, symbolHeight, aPLB, g);
                } else if (cb instanceof PolygonBreak) {
                    Draw.drawPolygonSymbol(new PointF(x, y), cb.getColor(), Color.black, symbolWidth,
                            symbolHeight, true, true, g);
                }
//                switch (aLS.getShapeType()) {
//                    case Point:
//                        PointBreak aPB = (PointBreak) cb;
//                        Draw.drawPoint(new PointF(x, y), aPB, g);
//                        break;
//                    case Polyline:
//                    case PolylineZ:
//                        PolylineBreak aPLB = (PolylineBreak) cb;
//                        Draw.drawPolylineSymbol_S(new PointF(x, y), symbolWidth, symbolHeight, aPLB, g);
//                        break;
//                    case Polygon:
//                        PolygonBreak aPGB = (PolygonBreak) cb;
//                        Draw.drawPolygonSymbol(new PointF(x, y), symbolHeight * 8 / 10, symbolHeight * 8 / 10, aPGB, g);
//                        break;
//                    case Image:
//                        Draw.drawPolygonSymbol(new PointF(x, y), cb.getColor(), Color.black, symbolWidth,
//                                symbolHeight, true, true, g);
//                        break;
//                }

                PointF sP = new PointF(0, 0);
                sP.X = x + symbolWidth / 2;
                sP.Y = y;
                g.setColor(this.labelColor);
                g.setFont(this.labelFont);
                //g.drawString(caption, sP.X + 5, sP.Y + aSF.height / 3);
                //g.drawString(caption, sP.X + 5, sP.Y + metrics.getHeight() / 4);
                Draw.drawString(g, caption, sP.X + 5, sP.Y + metrics.getHeight() / 4);

                x += this.symbolDimension.width + metrics.stringWidth(caption) + 15;
                i += 1;
            }
            y += breakHeight + this._breakSpace * 2;
        }
    }

    private void drawVerticalBarLegend(Graphics2D g, LegendScheme aLS) {
        PointF aP = new PointF(0, 0);
        PointF sP = new PointF(0, 0);
        boolean DrawShape = true, DrawFill = true, DrawOutline = false;
        Color FillColor = Color.red, OutlineColor = Color.black;
        String caption;
        Dimension aSF;

        int bNum = aLS.getBreakNum();
        if (aLS.getLegendBreaks().get(bNum - 1).isNoData()) {
            bNum -= 1;
        }

        int tickGap = this.getTickGap(g);
        List<Integer> labelIdxs = new ArrayList<>();
        int sIdx = (bNum % tickGap) / 2;
        int labNum = bNum - 1;
        if (aLS.getLegendType() == LegendType.UniqueValue) {
            labNum += 1;
        }
        while (sIdx < labNum) {
            labelIdxs.add(sIdx);
            sIdx += tickGap;
        }

        this._vBarWidth = (float) this.height / this.aspect;
        _hBarHeight = (float) this.height / bNum;
        aP.Y = this.height + _hBarHeight / 2;
        int idx;
        for (int i = 0; i < bNum; i++) {
            idx = i;
            switch (aLS.getShapeType()) {
                case Point:
                    PointBreak aPB = (PointBreak) aLS.getLegendBreaks().get(idx);
                    DrawShape = aPB.isDrawShape();
                    DrawFill = aPB.getDrawFill();
                    FillColor = aPB.getColor();
                    break;
                case Polyline:
                    PolylineBreak aPLB = (PolylineBreak) aLS.getLegendBreaks().get(idx);
                    DrawShape = aPLB.getDrawPolyline();
                    FillColor = aPLB.getColor();
                    break;
                case Polygon:
                    PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx);
                    DrawShape = aPGB.isDrawShape();
                    DrawFill = aPGB.isDrawFill();
                    FillColor = aPGB.getColor();
                    break;
                case Image:
                    ColorBreak aCB = aLS.getLegendBreaks().get(idx);
                    DrawShape = true;
                    DrawFill = true;
                    FillColor = aCB.getColor();
                    break;
            }

            aP.X = _vBarWidth / 2;
            aP.Y = aP.Y - _hBarHeight;

            if (DrawShape) {
                if (this.extendRect) {
                    if (aLS.getShapeType() == ShapeTypes.Polygon) {
                        PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                        aPGB.setDrawOutline(false);
                        Draw.drawPolygonSymbol((PointF) aP.clone(), _vBarWidth, _hBarHeight, aPGB, g);
                    } else {
                        Draw.drawPolygonSymbol((PointF) aP.clone(), FillColor, OutlineColor, _vBarWidth,
                                _hBarHeight, DrawFill, DrawOutline, g);
                    }
                } else {
                    if (i == 0) {
                        PointF[] Points = new PointF[4];
                        Points[0] = new PointF();
                        Points[0].X = aP.X;
                        Points[0].Y = this.height;
                        Points[1] = new PointF();
                        Points[1].X = 0;
                        Points[1].Y = aP.Y - _hBarHeight / 2 - 1.0f;
                        Points[2] = new PointF();
                        Points[2].X = _vBarWidth;
                        Points[2].Y = aP.Y - _hBarHeight / 2 - 1.0f;
                        Points[3] = new PointF();
                        Points[3].X = aP.X;
                        Points[3].Y = this.height;
                        if (aLS.getShapeType() == ShapeTypes.Polygon) {
                            PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                            aPGB.setDrawOutline(false);
                            Draw.drawPolygon(Points, aPGB, g);
                        } else {
                            Draw.drawPolygon(Points, FillColor, OutlineColor, DrawFill, DrawOutline, g);
                        }
                    } else if (i == bNum - 1) {
                        PointF[] Points = new PointF[4];
                        Points[0] = new PointF();
                        Points[0].X = 0;
                        Points[0].Y = _hBarHeight;
                        Points[1] = new PointF();
                        Points[1].X = _vBarWidth;
                        Points[1].Y = _hBarHeight;
                        Points[2] = new PointF();
                        Points[2].X = aP.X;
                        Points[2].Y = 0;
                        Points[3] = new PointF();
                        Points[3].X = 0;
                        Points[3].Y = _hBarHeight;
                        if (aLS.getShapeType() == ShapeTypes.Polygon) {
                            PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                            aPGB.setDrawOutline(false);
                            Draw.drawPolygon(Points, aPGB, g);
                        } else {
                            Draw.drawPolygon(Points, FillColor, OutlineColor, DrawFill, DrawOutline, g);
                        }
                    } else {
                        if (aLS.getShapeType() == ShapeTypes.Polygon) {
                            PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                            aPGB.setDrawOutline(false);
                            Draw.drawPolygonSymbol((PointF) aP.clone(), _vBarWidth, _hBarHeight, aPGB, g);
                        } else {
                            Draw.drawPolygonSymbol((PointF) aP.clone(), FillColor, OutlineColor, _vBarWidth,
                                    _hBarHeight, DrawFill, DrawOutline, g);
                        }
                    }
                }
            }
        }
        //Draw neatline
        g.setColor(Color.black);
        if (this.extendRect) {
            g.draw(new Rectangle.Float(0, 0, this._vBarWidth, this._hBarHeight * bNum));
        } else {
            Polygon p = new Polygon();
            p.addPoint((int)(this._vBarWidth / 2), 0);
            p.addPoint(0, (int)this._hBarHeight);
            p.addPoint(0, (int)(this._hBarHeight * (bNum - 1)));
            p.addPoint((int)(this._vBarWidth / 2), (int)(this._hBarHeight * bNum));
            p.addPoint((int)this._vBarWidth, (int)(this._hBarHeight * (bNum - 1)));
            p.addPoint((int)this._vBarWidth, (int)this._hBarHeight);
            g.drawPolygon(p);
        }
        //Draw ticks
        aP.Y = this.height + _hBarHeight / 2;
        int labLen = (int)(this._vBarWidth / 3);
        if (labLen < 5){
            labLen = 5;
            if (this._vBarWidth < 5)
                labLen = (int)this._vBarWidth;
        }
        for (int i = 0; i < bNum; i++) {
            idx = i;
            ColorBreak cb = aLS.getLegendBreaks().get(idx);
            if (aLS.getLegendType() == LegendType.UniqueValue) {
                caption = cb.getCaption();
            } else {
                caption = DataConvert.removeTailingZeros(cb.getEndValue().toString());
            }

            aP.X = _vBarWidth / 2;
            aP.Y = aP.Y - _hBarHeight;

            if (aLS.getLegendType() == LegendType.UniqueValue) {
                if (labelIdxs.contains(idx)) {
                    sP.X = aP.X + _vBarWidth / 2 + 5;
                    sP.Y = aP.Y;
                    FontMetrics metrics = g.getFontMetrics(this.tickFont);
                    aSF = new Dimension(metrics.stringWidth(caption), metrics.getHeight());
                    g.setColor(Color.black);
                    g.setFont(this.tickFont);
                    //g.drawString(caption, sP.X, sP.Y + aSF.height / 4);
                    Draw.drawString(g, caption, sP.X, sP.Y + aSF.height / 4);
                }
            } else {
                if (labelIdxs.contains(idx)) {
                    //g.setColor(Color.black);
                    sP.X = aP.X + _vBarWidth / 2;
                    sP.Y = aP.Y - _hBarHeight / 2;
                    g.draw(new Line2D.Float(sP.X - labLen, sP.Y, sP.X, sP.Y));
                    sP.X = sP.X + 5;
                    if (i < bNum - 1) {
                        FontMetrics metrics = g.getFontMetrics(this.tickFont);
                        aSF = new Dimension(metrics.stringWidth(caption), metrics.getHeight());
                        g.setFont(this.tickFont);
                        //g.drawString(caption, sP.X, sP.Y + aSF.height / 4);
                        Draw.drawString(g, caption, sP.X, sP.Y + aSF.height / 4);
                    }
                }
            }
        }
        //Draw label
        if (this.label != null){
            g.setFont(this.labelFont);
            Dimension dim = Draw.getStringDimension(this.getLabel(), g);
            x = this.width - dim.height * 0.5f - 2;
            y = this.height * 0.5f; 
            Draw.drawLabelPoint_270((float) x, (float) y, this.getLabelFont(), this.label,
                this.getLabelColor(), g, null);
        }
    }

    private void drawHorizontalBarLegend(Graphics2D g, LegendScheme aLS) {
        PointF aP = new PointF(0, 0);
        PointF sP = new PointF(0, 0);
        boolean DrawShape = true, DrawFill = true, DrawOutline = false;
        Color FillColor = Color.red, OutlineColor = Color.black;
        String caption;
        Dimension aSF;

        int bNum = aLS.getBreakNum();
        if (aLS.getLegendBreaks().get(bNum - 1).isNoData()) {
            bNum -= 1;
        }

        int tickGap = this.getTickGap(g);
        List<Integer> labelIdxs = new ArrayList<>();
        int sIdx = (bNum % tickGap) / 2;
        int labNum = bNum - 1;
        if (aLS.getLegendType() == LegendType.UniqueValue) {
            labNum += 1;
        }
        while (sIdx < labNum) {
            labelIdxs.add(sIdx);
            sIdx += tickGap;
        }

        this._hBarHeight = (float) this.width / this.aspect;
        _vBarWidth = (float) this.width / bNum;
        aP.X = -_vBarWidth / 2;
        int idx;
        for (int i = 0; i < bNum; i++) {
            idx = i;
            switch (aLS.getShapeType()) {
                case Point:
                    PointBreak aPB = (PointBreak) aLS.getLegendBreaks().get(idx);
                    DrawShape = aPB.isDrawShape();
                    DrawFill = aPB.getDrawFill();
                    FillColor = aPB.getColor();
                    break;
                case Polyline:
                    PolylineBreak aPLB = (PolylineBreak) aLS.getLegendBreaks().get(idx);
                    DrawShape = aPLB.getDrawPolyline();
                    FillColor = aPLB.getColor();
                    break;
                case Polygon:
                    PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx);
                    DrawShape = aPGB.isDrawShape();
                    DrawFill = aPGB.isDrawFill();
                    FillColor = aPGB.getColor();
                    break;
                case Image:
                    ColorBreak aCB = aLS.getLegendBreaks().get(idx);
                    DrawShape = true;
                    DrawFill = true;
                    FillColor = aCB.getColor();
                    break;
            }

            aP.X += _vBarWidth;
            aP.Y = _hBarHeight / 2;

            if (DrawShape) {
                if (this.extendRect) {
                    if (aLS.getShapeType() == ShapeTypes.Polygon) {
                        PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                        aPGB.setDrawOutline(false);
                        Draw.drawPolygonSymbol((PointF) aP.clone(), _vBarWidth, _hBarHeight, aPGB, g);
                    } else {
                        Draw.drawPolygonSymbol((PointF) aP.clone(), FillColor, OutlineColor, _vBarWidth,
                                _hBarHeight, DrawFill, DrawOutline, g);
                    }
                } else {
                    float extendw = _vBarWidth;
                    if (this.autoExtendFrac)
                        extendw = _hBarHeight;
                    if (i == 0) {
                        PointF[] Points = new PointF[4];
                        Points[0] = new PointF();
                        Points[0].X = _vBarWidth - extendw;
                        Points[0].Y = aP.Y;
                        Points[1] = new PointF();
                        Points[1].X = _vBarWidth;
                        Points[1].Y = 0;
                        Points[2] = new PointF();
                        Points[2].X = _vBarWidth;
                        Points[2].Y = _hBarHeight;
                        Points[3] = new PointF();
                        Points[3].X = _vBarWidth - extendw;
                        Points[3].Y = aP.Y;
                        if (aLS.getShapeType() == ShapeTypes.Polygon) {
                            PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                            aPGB.setDrawOutline(false);
                            Draw.drawPolygon(Points, aPGB, g);
                        } else {
                            Draw.drawPolygon(Points, FillColor, OutlineColor, DrawFill, DrawOutline, g);
                        }
                    } else if (i == bNum - 1) {
                        PointF[] Points = new PointF[4];
                        Points[0] = new PointF();
                        Points[0].X = i * _vBarWidth - 1.0f;
                        Points[0].Y = _hBarHeight;
                        Points[1] = new PointF();
                        Points[1].X = i * _vBarWidth - 1.0f;
                        Points[1].Y = 0;
                        Points[2] = new PointF();
                        Points[2].X = i * _vBarWidth + extendw;
                        Points[2].Y = aP.Y;
                        Points[3] = new PointF();
                        Points[3].X = i * _vBarWidth - 1.0f;
                        Points[3].Y = _hBarHeight;
                        if (aLS.getShapeType() == ShapeTypes.Polygon) {
                            PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                            aPGB.setDrawOutline(false);
                            Draw.drawPolygon(Points, aPGB, g);
                        } else {
                            Draw.drawPolygon(Points, FillColor, OutlineColor, DrawFill, DrawOutline, g);
                        }
                    } else {
                        if (aLS.getShapeType() == ShapeTypes.Polygon) {
                            PolygonBreak aPGB = (PolygonBreak) aLS.getLegendBreaks().get(idx).clone();
                            aPGB.setDrawOutline(false);
                            Draw.drawPolygonSymbol((PointF) aP.clone(), _vBarWidth, _hBarHeight, aPGB, g);
                        } else {
                            Draw.drawPolygonSymbol((PointF) aP.clone(), FillColor, OutlineColor, _vBarWidth,
                                    _hBarHeight, DrawFill, DrawOutline, g);
                        }
                    }
                }
            }
        }
        //Draw neatline
        g.setColor(Color.black);
        if (this.extendRect)
            g.draw(new Rectangle.Float(0, 0, this._vBarWidth * bNum, this._hBarHeight));
        else {
            float extendw = _vBarWidth;
            if (this.autoExtendFrac)
                extendw = _hBarHeight;
            Polygon p = new Polygon();
            p.addPoint((int)(_vBarWidth - extendw), (int)(this._hBarHeight / 2));
            p.addPoint((int)this._vBarWidth, 0);
            p.addPoint((int)(this._vBarWidth * (bNum - 1)), 0);
            p.addPoint((int)(this._vBarWidth * (bNum - 1) + extendw), (int)(this._hBarHeight / 2));
            p.addPoint((int)(this._vBarWidth * (bNum - 1)), (int)this._hBarHeight);
            p.addPoint((int)this._vBarWidth, (int)this._hBarHeight);
            g.drawPolygon(p);
        }
        //Draw tick and label
        aP.X = -_vBarWidth / 2;
         int labLen = (int)(this._hBarHeight / 3);
        if (labLen < 5){
            labLen = 5;
            if (this._hBarHeight < 5)
                labLen = (int)this._hBarHeight;
        }
        for (int i = 0; i < bNum; i++) {
            idx = i;
            ColorBreak cb = aLS.getLegendBreaks().get(idx);
            if (aLS.getLegendType() == LegendType.UniqueValue) {
                caption = cb.getCaption();
            } else {
                caption = DataConvert.removeTailingZeros(cb.getEndValue().toString());
            }

            aP.X += _vBarWidth;
            aP.Y = _hBarHeight / 2;

            if (aLS.getLegendType() == LegendType.UniqueValue) {
                if (labelIdxs.contains(idx)) {
                    sP.X = aP.X;
                    sP.Y = aP.Y + _hBarHeight / 2 + 5;
                    FontMetrics metrics = g.getFontMetrics(this.tickFont);
                    aSF = new Dimension(metrics.stringWidth(caption), metrics.getHeight());
                    g.setColor(Color.black);
                    g.setFont(this.tickFont);
                    //g.drawString(caption, sP.X, sP.Y + aSF.height / 4);
                    Draw.drawString(g, caption, sP.X - aSF.width / 2, sP.Y + aSF.height * 3 / 4);
                }
            } else {
                if (labelIdxs.contains(idx)) {
                    //g.setColor(Color.black);
                    sP.X = aP.X + _vBarWidth / 2;
                    sP.Y = aP.Y + _hBarHeight / 2;
                    g.draw(new Line2D.Float(sP.X, sP.Y, sP.X, sP.Y - labLen));
                    sP.Y = sP.Y + 5;
                    if (i < bNum - 1) {
                        FontMetrics metrics = g.getFontMetrics(this.tickFont);
                        aSF = new Dimension(metrics.stringWidth(caption), metrics.getHeight());
                        g.setFont(this.tickFont);
                        //g.drawString(caption, sP.X, sP.Y + aSF.height / 4);
                        Draw.drawString(g, caption, sP.X - aSF.width / 2, sP.Y + aSF.height * 3 / 4);
                    }
                }
            }
        }
        
        //Draw label
        if (this.label != null){
            g.setFont(this.labelFont);
            g.setColor(this.labelColor);
            Dimension dim = Draw.getStringDimension(this.getLabel(), g);
            x = this.width * 0.5f - dim.width * 0.5f;
            y = this.height - dim.height * 0.25f - 2; 
            Draw.drawString(g, label, x, y);
        }
    }

    private int getMaxLabelWidth(Graphics2D g) {
        String caption;
        Dimension aSF;
        int bNum = legendScheme.getBreakNum();
        //FontMetrics metrics = g.getFontMetrics(tickFont);
        //aSF = new Dimension(metrics.stringWidth(caption), metrics.getHeight());
        int labWidth = 0;
        for (int i = 0; i < bNum; i++) {
            caption = legendScheme.getLegendBreaks().get(i).getCaption();
            boolean isValid = true;
            if (isValid) {
                g.setFont(this.labelFont);
                aSF = Draw.getStringDimension(caption, g);
                int labwidth = aSF.width;
                if (labWidth < labwidth) {
                    labWidth = labwidth;
                }
            }
        }

        return labWidth;
    }

    private int getBreakHeight(Graphics2D g) {
        FontMetrics metrics = g.getFontMetrics(tickFont);
        return Math.max(metrics.getHeight(), this.symbolDimension.height);
    }

    /**
     * Get legend dimension
     *
     * @param g Graphics2D
     * @param limitDim Limit dimension
     * @return Legend dimension
     */
    public Dimension getLegendDimension(Graphics2D g, Dimension limitDim) {
        if (legendScheme != null) {
            if (this.colorBar) {
                switch (this.orientation) {
                    case VERTICAL:
                        this.width = (int) (this.getTickWidth(g) + limitDim.height * this.shrink / this.aspect + 5);
                        if (this.label != null){
                            g.setFont(this.labelFont);
                            this.width += (int)Draw.getStringDimension(label, g).height + 5;
                        }
                        break;
                    default:
                        g.setFont(this.tickFont);
                        this.height = (int) (Draw.getStringDimension("test", g).height + limitDim.width * this.shrink / this.aspect + 5);
                        if (this.label != null){
                            g.setFont(this.labelFont);
                            this.height += (int)Draw.getStringDimension(label, g).height + 5;
                        }
                }
            } else {
                int breakHeight = getBreakHeight(g);
                switch (this.orientation) {
                    case VERTICAL:
                        //Get column number
                        int tHeight = (int) (legendScheme.getBreakNum() * (breakHeight + _breakSpace)
                                + _breakSpace * 2 + breakHeight / 2 + 5);
                        rowColNum = 1;
                        if (tHeight > limitDim.height * 10 / 8) {
                            rowColNum = tHeight / (limitDim.height * 10 / 8) + 1;
                            if (rowColNum == 1) {
                                rowColNum = 2;
                            } else {
                                int n = legendScheme.getBreakNum() / rowColNum;
                                int m = legendScheme.getBreakNum() % rowColNum;
                                if (m != 0) {
                                    if (m <= n) {
                                        rowColNum += 1;
                                    } else {
                                        rowColNum += 2;
                                    }
                                } else {
                                    if (rowColNum * (limitDim.width * 8 / 10) < tHeight) {
                                        rowColNum += 1;
                                    }
                                }
                            }
                        }

                        //Get width
                        int colWidth = this.symbolDimension.width + getMaxLabelWidth(g) + 15;
                        this.width = colWidth * rowColNum;

                        //Get height
                        int[] rowNums = new int[rowColNum];
                        int ave = legendScheme.getBreakNum() / rowColNum;
                        if (ave * rowColNum < legendScheme.getBreakNum()) {
                            ave += 1;
                        }
                        int num = 0;
                        int i;
                        for (i = 0; i < rowColNum - 1; i++) {
                            rowNums[i] = ave;
                            num += ave;
                        }
                        rowNums[rowColNum - 1] = legendScheme.getBreakNum() - num;

//                        this.height = (int) (rowNums[0] * (breakHeight + _breakSpace)
//                                + _breakSpace * 2 + breakHeight / 2 + 5);
                        this.height = (int) (rowNums[0] * (breakHeight + _breakSpace)
                                + _breakSpace * 2);
                        break;
                    case HORIZONTAL:
                        //Get row number
                        int breakWidth = this.symbolDimension.width + this.getMaxLabelWidth(g) + 15;
                        int tWidth = breakWidth * legendScheme.getBreakNum();
                        rowColNum = 1;
                        if (tWidth > limitDim.width * 8 / 10) {
                            rowColNum = tWidth / (limitDim.width * 8 / 10);
                            if (rowColNum == 1) {
                                rowColNum = 2;
                            } else {
                                int n = legendScheme.getBreakNum() / rowColNum;
                                int m = legendScheme.getBreakNum() % rowColNum;
                                if (m != 0) {
                                    if (m <= n) {
                                        rowColNum += 1;
                                    } else {
                                        rowColNum += 2;
                                    }
                                } else {
                                    if (rowColNum * (limitDim.width * 8 / 10) < tWidth) {
                                        rowColNum += 1;
                                    }
                                }
                            }
                        }

                        //Get height
                        this.height = (int) (breakHeight + this._breakSpace * 2) * this.rowColNum;

                        //Get width
                        //FontMetrics metrics = g.getFontMetrics(tickFont);
                        ave = legendScheme.getBreakNum() / rowColNum;
                        if (ave * rowColNum < legendScheme.getBreakNum()) {
                            ave += 1;
                        }
                        num = 0;
                        int maxWidth = 0;
                        int tempWidth = 0;
                        for (i = 0; i < legendScheme.getBreakNum(); i++) {
                            if (num < ave) {
                                //tempWidth += this.symbolDimension.width + 15
                                //        + metrics.stringWidth(legendScheme.getLegendBreaks().get(i).getCaption());
                                tempWidth += this.symbolDimension.width + 15
                                        + Draw.getStringDimension(legendScheme.getLegendBreaks().get(i).getCaption(), g).width;
                                num += 1;
                            } else {
                                if (maxWidth < tempWidth) {
                                    maxWidth = tempWidth;
                                }
                                //tempWidth = metrics.stringWidth(legendScheme.getLegendBreaks().get(i).getCaption()) + 15;
                                tempWidth = Draw.getStringDimension(legendScheme.getLegendBreaks().get(i).getCaption(), g).width;
                                num = 1;
                            }
                        }
                        if (maxWidth < tempWidth) {
                            maxWidth = tempWidth;
                        }
                        if (maxWidth > limitDim.width) {
                            maxWidth = limitDim.width * 8 / 10;
                        }
                        this.width = maxWidth;
                        break;
                }
            }
        }

        return new Dimension(this.width, this.height);
    }

    private int getTickWidth(Graphics2D g) {
        float rwidth = 0;
        String caption = "";
        int bNum = this.legendScheme.getBreakNum();
        //FontMetrics metrics = g.getFontMetrics(this.tickFont);
        if (this.legendScheme.getLegendBreaks().get(bNum - 1).isNoData()) {
            bNum -= 1;
        }
        for (int i = 0; i < bNum; i++) {
            switch (this.legendScheme.getShapeType()) {
                case Point:
                    PointBreak aPB = (PointBreak) legendScheme.getLegendBreaks().get(i);
                    if (legendScheme.getLegendType() == LegendType.GraduatedColor) {
                        caption = DataConvert.removeTailingZeros(aPB.getEndValue().toString());
                    } else {
                        caption = aPB.getCaption();
                    }
                    break;
                case Polyline:
                    PolylineBreak aPLB = (PolylineBreak) legendScheme.getLegendBreaks().get(i);
                    if (legendScheme.getLegendType() == LegendType.GraduatedColor) {
                        caption = DataConvert.removeTailingZeros(aPLB.getEndValue().toString());
                    } else {
                        caption = aPLB.getCaption();
                    }
                    break;
                case Polygon:
                    PolygonBreak aPGB = (PolygonBreak) legendScheme.getLegendBreaks().get(i);
                    if (legendScheme.getLegendType() == LegendType.GraduatedColor) {
                        caption = DataConvert.removeTailingZeros(aPGB.getEndValue().toString());
                    } else {
                        caption = aPGB.getCaption();
                    }
                    break;
                case Image:
                    ColorBreak aCB = legendScheme.getLegendBreaks().get(i);
                    if (legendScheme.getLegendType() == LegendType.GraduatedColor) {
                        caption = DataConvert.removeTailingZeros(aCB.getEndValue().toString());
                    } else {
                        caption = aCB.getCaption();
                    }
                    break;
            }

            boolean isValid = true;
            switch (legendScheme.getLegendType()) {
                case GraduatedColor:
                    if (i == bNum - 1) {
                        isValid = false;
                    }
                    break;
            }
            if (isValid) {
                //float labwidth = metrics.stringWidth(caption);
                float labwidth = Draw.getStringDimension(caption, g).width;
                if (rwidth < labwidth) {
                    rwidth = labwidth;
                }
            }
        }

        return (int) rwidth;
    }

    /**
     * Update tick gap
     *
     * @param g Graphics2D
     */
    private int getTickGap(Graphics2D g) {
        double len;
        int n = this.legendScheme.getBreakNum();
        int nn;
        if (this.orientation == PlotOrientation.HORIZONTAL) {
            len = this.width;
            int labLen = this.getTickWidth(g);
            nn = (int) ((len * 0.8) / labLen);
        } else {
            len = this.height;
            FontMetrics metrics = g.getFontMetrics(tickFont);
            nn = (int) (len / metrics.getHeight());
        }
        if (nn == 0) {
            nn = 1;
        }
        return n / nn + 1;
    }

    // </editor-fold>      
    // <editor-fold desc="BeanInfo">
    public class LayoutLegendBean {

        LayoutLegendBean() {
        }

        // <editor-fold desc="Get Set Methods">        
        /**
         * Get if draw neat line
         *
         * @return If draw neat line
         */
        public boolean isDrawNeatLine() {
            return _drawNeatLine;
        }

        /**
         * Set if draw neat line
         *
         * @param istrue If draw neat line
         */
        public void setDrawNeatLine(boolean istrue) {
            _drawNeatLine = istrue;
        }

        /**
         * Get neat line color
         *
         * @return Neat line color
         */
        public Color getNeatLineColor() {
            return _neatLineColor;
        }

        /**
         * Set neat line color
         *
         * @param color Neat line color
         */
        public void setNeatLineColor(Color color) {
            _neatLineColor = color;
        }

        /**
         * Get neat line size
         *
         * @return Neat line size
         */
        public float getNeatLineSize() {
            return _neatLineSize;
        }

        /**
         * Set neat line size
         *
         * @param size Neat line size
         */
        public void setNeatLineSize(float size) {
            _neatLineSize = size;
        }

        /**
         * Get tick font
         *
         * @return The tick font
         */
        public Font getTickFont() {
            return tickFont;
        }

        /**
         * Set tick font
         *
         * @param font The tick font
         */
        public void setTickFont(Font font) {
            tickFont = font;
        }

        /**
         * Get column number
         *
         * @return Column number
         */
        public int getColumnNumber() {
            return rowColNum;
        }

        /**
         * Set column number
         *
         * @param value Column number
         */
        public void setColumnNumber(int value) {
            rowColNum = value;
        }

        /**
         * Get is draw background
         *
         * @return Boolean
         */
        public boolean isDrawBackground() {
            return drawBackground;
        }

        /**
         * Set is draw background
         *
         * @param value Boolean
         */
        public void setDrawBackground(boolean value) {
            drawBackground = value;
        }

        /**
         * Get background color
         *
         * @return Background color
         */
        public Color getBackground() {
            return background;
        }

        /**
         * Set background color
         *
         * @param c Background color
         */
        public void setBackground(Color c) {
            background = c;
        }

        // </editor-fold>
    }

    public static class LayoutLegendBeanBeanInfo extends BaseBeanInfo {

        public LayoutLegendBeanBeanInfo() {
            super(LayoutLegendBean.class);
            ExtendedPropertyDescriptor e = addProperty("plotOrientation");
            e.setCategory("General").setDisplayName("Plot orientation");
            e.setPropertyEditorClass(PlotOrientationEditor.class);
            addProperty("tickFont").setCategory("General").setDisplayName("Tick Font");
            addProperty("drawBackground").setCategory("General").setDisplayName("Draw Background");
            addProperty("background").setCategory("General").setDisplayName("Background");
            addProperty("columnNumber").setCategory("General").setDisplayName("Column Number");
            addProperty("drawNeatLine").setCategory("Neat Line").setDisplayName("Draw Neat Line");
            addProperty("neatLineColor").setCategory("Neat Line").setDisplayName("Neat Line Color");
            addProperty("neatLineSize").setCategory("Neat Line").setDisplayName("Neat Line Size");
        }
    }

    public static class PlotOrientationEditor extends ComboBoxPropertyEditor {

        public PlotOrientationEditor() {
            super();
            PlotOrientation[] orientations = PlotOrientation.values();
            String[] types = new String[orientations.length];
            int i = 0;
            for (PlotOrientation type : orientations) {
                types[i] = type.toString();
                i += 1;
            }
            setAvailableValues(types);
        }
    }

    // </editor-fold>
}
