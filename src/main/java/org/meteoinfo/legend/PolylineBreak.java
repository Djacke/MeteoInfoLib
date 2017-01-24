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
package org.meteoinfo.legend;

import org.meteoinfo.drawing.PointStyle;
import java.awt.Color;
import java.util.HashMap;

/**
 * Polyline break class
 *
 * @author Yaqiang Wang
 */
public class PolylineBreak extends ColorBreak {
    // <editor-fold desc="Variables">

    private float _size;
    private LineStyles _style;
    private boolean _drawPolyline;
    private boolean _drawSymbol;
    private float _symbolSize;
    private PointStyle _symbolStyle;
    private Color _symbolColor;
    private Color symbolFillColor;
    private boolean fillSymbol;
    private int _symbolInterval;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    public PolylineBreak() {
        super();
        this.setBreakType(BreakTypes.PolylineBreak);
        _size = 1.0f;
        _style = LineStyles.Solid;
        _drawPolyline = true;
        _drawSymbol = false;
        _symbolSize = 8.0f;
        _symbolStyle = PointStyle.UpTriangle;
        _symbolColor = this.getColor();
        symbolFillColor = _symbolColor;
        fillSymbol = false;
        _symbolInterval = 1;
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get line size
     *
     * @return Size
     */
    public float getSize() {
        return _size;
    }

    /**
     * Set line size
     *
     * @param size Size
     */
    public void setSize(float size) {
        _size = size;
    }

    /**
     * Get line style
     *
     * @return Line style
     */
    public LineStyles getStyle() {
        return _style;
    }

    /**
     * Set line style
     *
     * @param style Line style
     */
    public void setStyle(LineStyles style) {
        if (style != null)
            _style = style;
    }

    /**
     * Get if draw polyline
     *
     * @return Boolean
     */
    public boolean getDrawPolyline() {
        return _drawPolyline;
    }

    /**
     * Set if draw polyline
     *
     * @param isTrue Boolean
     */
    public void setDrawPolyline(boolean isTrue) {
        _drawPolyline = isTrue;
    }

    /**
     * Get if draw symbol
     *
     * @return Boolean
     */
    public boolean getDrawSymbol() {
        return _drawSymbol;
    }

    /**
     * Set if draw symbol
     *
     * @param isTrue
     */
    public void setDrawSymbol(boolean isTrue) {
        _drawSymbol = isTrue;
    }

    /**
     * Get symbol size
     *
     * @return Symbol size
     */
    public float getSymbolSize() {
        return _symbolSize;
    }

    /**
     * Set symbol size
     *
     * @param size Symbol size
     */
    public void setSymbolSize(float size) {
        _symbolSize = size;
    }

    /**
     * Get symbol style
     *
     * @return Symbol style
     */
    public PointStyle getSymbolStyle() {
        return _symbolStyle;
    }

    /**
     * Set symbol style
     *
     * @param style Symbol style
     */
    public void setSymbolStyle(PointStyle style) {
        if (style != null)
            _symbolStyle = style;
    }

    /**
     * Get symbol color
     *
     * @return Symbol color
     */
    public Color getSymbolColor() {
        return _symbolColor;
    }

    /**
     * Set symbol color
     *
     * @param c Symbol color
     */
    public void setSymbolColor(Color c) {
        _symbolColor = c;
    }
    
    /**
     * Get symbol fill color
     * @return Symbol fill color
     */
    public Color getSymbolFillColor(){
        return this.symbolFillColor;
    }
    
    /**
     * Set symbol fill color
     * @param value Symbol fill color
     */
    public void setSymbolFillColor(Color value){
        this.symbolFillColor = value;
    }
    
    /**
     * Get if fill symbol
     * @return Boolean
     */
    public boolean isFillSymbol(){
        return this.fillSymbol;
    }
    
    /**
     * Set if fill symbol
     * @param value Boolean
     */
    public void setFillSymbol(boolean value){
        this.fillSymbol = value;
    }

    /**
     * Get symbol interval
     *
     * @return Symbol interval
     */
    public int getSymbolInterval() {
        return _symbolInterval;
    }

    /**
     * Set symbol Interval
     *
     * @param interval Symbol interval
     */
    public void setSymbolInterval(int interval) {
        _symbolInterval = interval;
    }

    /**
     * Get if using dash style
     *
     * @return Boolean
     */
    public boolean isUsingDashStyle() {
        switch (_style) {
            case Solid:
            case Dash:
            case Dot:
            case DashDot:
            case DashDotDot:
                return true;
            default:
                return false;
        }
    }
    // </editor-fold>
    // <editor-fold desc="Methods">
    /**
     * Get property object
     * @return Custom property object
     */
    @Override
        public Object getPropertyObject()
        {
            HashMap objAttr = new HashMap();
            objAttr.put("Color", "Color");
            objAttr.put("Size", "Size");
            objAttr.put("Style", "Style");
            objAttr.put("DrawPolyline", "DrawPolyline");
            objAttr.put("DrawSymbol", "DrawSymbol");
            objAttr.put("SymbolSize", "SymbolSize");
            objAttr.put("SymbolStyle", "SymbolStyle");
            objAttr.put("SymbolColor", "SymbolColor");
            objAttr.put("SymbolInterval", "SymbolInterval");
            return objAttr;
        }

        /**
         * Clone
         * @return PolylineBreak
         */
    @Override
        public Object clone()
        {
            PolylineBreak aCB = new PolylineBreak();
            aCB.setCaption(this.getCaption());
            aCB.setColor(this.getColor());
            aCB.setDrawShape(this.isDrawShape());
            aCB.setEndValue(this.getEndValue());
            aCB.setNoData(this.isNoData());
            aCB.setStartValue(this.getStartValue());            
            aCB.setSize(_size);           
            aCB.setStyle(_style);
            aCB.setDrawPolyline(_drawPolyline);
            aCB.setDrawSymbol( _drawSymbol);
            aCB.setSymbolSize(_symbolSize);
            aCB.setSymbolColor(_symbolColor);
            aCB.setSymbolStyle(_symbolStyle);
            aCB.setSymbolInterval(_symbolInterval);

            return aCB;
        }
    // </editor-fold>
}
