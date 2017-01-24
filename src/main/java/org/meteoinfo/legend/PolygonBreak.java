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

import java.awt.Color;
import java.util.HashMap;

/**
 * Polygon break class
 *
 * @author Yaqiang Wang
 */
public class PolygonBreak extends ColorBreak {
    // <editor-fold desc="Variables">

    private Color _outlineColor;
    private float _outlineSize;
    private boolean _drawOutline;
    private boolean _drawFill;
    //private boolean usingHatchStyle;
    private HatchStyle style;
    private int styleSize;
    private Color _backColor;
    //private int _transparencyPerc;
    private boolean _isMaskout;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    public PolygonBreak() {
        super();
        this.setBreakType(BreakTypes.PolygonBreak);
        _outlineColor = Color.black;
        _outlineSize = 1.0f;
        _drawOutline = true;
        _drawFill = true;
        //usingHatchStyle = false;
        style = HatchStyle.NONE;
        styleSize = 8;
        _backColor = new Color(1.0f, 1.0f, 1.0f, 0.0f);
        //_transparencyPerc = 0;
        _isMaskout = false;
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get outline color
     *
     * @return Outline color
     */
    public Color getOutlineColor() {
        return _outlineColor;
    }

    /**
     * Set outline color
     *
     * @param c Outline color
     */
    public void setOutlineColor(Color c) {
        _outlineColor = c;
    }

    /**
     * Get outline size
     *
     * @return Outline size
     */
    public float getOutlineSize() {
        return _outlineSize;
    }

    /**
     * Set outline size
     *
     * @param size Outline size
     */
    public void setOutlineSize(float size) {
        _outlineSize = size;
    }

    /**
     * Get if draw outline
     *
     * @return Boolean
     */
    public boolean isDrawOutline() {
        return _drawOutline;
    }

    /**
     * Set if draw outline
     *
     * @param isTrue Boolean
     */
    public void setDrawOutline(boolean isTrue) {
        _drawOutline = isTrue;
    }

    /**
     * Get if draw fill
     *
     * @return Boolean
     */
    public boolean isDrawFill() {
        return _drawFill;
    }

    /**
     * Set if draw fill
     *
     * @param isTrue Boolean
     */
    public void setDrawFill(boolean isTrue) {
        _drawFill = isTrue;
    }

    /**
     * Get background color
     *
     * @return Background color
     */
    public Color getBackColor() {
        return _backColor;
    }

    /**
     * Set background color
     *
     * @param c Background color
     */
    public void setBackColor(Color c) {
        _backColor = c;
    }
    
    /**
     * Get if using hatch style
     * @return Boolean
     */
    public boolean isUsingHatchStyle(){
        return this.style != HatchStyle.NONE;
    }
    
    /**
     * Get hatch style
     * @return Hatch style
     */
    public HatchStyle getStyle(){
        return this.style;
    }
    
    /**
     * Set hatch style
     * @param value Hatch style
     */
    public void setStyle(HatchStyle value){
        this.style = value;
    }
    
    /**
     * Get style size
     * @return Style size
     */
    public int getStyleSize(){
        return this.styleSize;
    }
    
    /**
     * Set style size
     * @param value Style size
     */
    public void setStyleSize(int value){
        this.styleSize = value;
    }

    /**
     * Get if maskout
     *
     * @return Boolean
     */
    public boolean isMaskout() {
        return _isMaskout;
    }

    /**
     * Set if maskout
     *
     * @param isTrue Boolean
     */
    public void setMaskout(boolean isTrue) {
        _isMaskout = isTrue;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Get property object
     *
     * @return Custom property object
     */
    @Override
    public Object getPropertyObject() {
        HashMap objAttr = new HashMap();
        objAttr.put("Color", "Color");
        objAttr.put("OutlineColor", "OutlineColor");
        objAttr.put("OutlineSize", "OutlineSize");
        objAttr.put("DrawOutline", "DrawOutline");
        objAttr.put("DrawFill", "DrawFill");
        objAttr.put("DrawPolygon", "DrawPolygon");
        objAttr.put("UsingHatchStyle", "UsingHatchStyle");
        objAttr.put("Style", "Style");
        objAttr.put("SytleSize", "StyleSize");
        objAttr.put("BackColor", "BackColor");
        objAttr.put("TransparencyPercent", "TransparencyPercent");
        //CustomProperty cp = new CustomProperty(this, objAttr);
        return objAttr;
    }

    /**
     * Cloen
     *
     * @return PolygonBreak
     */
    @Override
    public Object clone() {
        PolygonBreak aCB = new PolygonBreak();
        aCB.setCaption(this.getCaption());
        aCB.setColor(this.getColor());
        aCB.setDrawShape(this.isDrawShape());
        aCB.setEndValue(this.getEndValue());
        aCB.setNoData(this.isNoData());
        aCB.setStartValue(this.getStartValue());
        aCB.setOutlineColor(_outlineColor);
        aCB.setOutlineSize(_outlineSize);
        aCB.setDrawOutline(_drawOutline);
        aCB.setDrawFill(_drawFill);
        //aCB.setUsingHatchStyle(usingHatchStyle);
        aCB.setStyle(style);
        aCB.setStyleSize(styleSize);
        aCB.setBackColor(_backColor);
        //aCB.TransparencyPercent = _transparencyPerc;
        aCB.setMaskout(_isMaskout);

        return aCB;
    }
    // </editor-fold>
}
