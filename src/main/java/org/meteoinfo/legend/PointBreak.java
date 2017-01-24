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

import org.meteoinfo.drawing.MarkerType;
import org.meteoinfo.drawing.PointStyle;
import org.meteoinfo.global.colors.ColorUtil;
import java.awt.Color;
import java.util.HashMap;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author User
 */
public class PointBreak extends ColorBreak {
    // <editor-fold desc="Variables">

    private MarkerType _markerType;
    private Color _outlineColor;
    private float _size;
    private PointStyle _style;
    private boolean _drawOutline;
    private boolean _drawFill;
    private String _fontName;
    private int _charIndex;
    private String _imagePath;
    private float _angle;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public PointBreak() {
        super();
        this.setBreakType(BreakTypes.PointBreak);
        _markerType = MarkerType.Simple;
        _fontName = "Arial";
        _charIndex = 0;
        _outlineColor = Color.black;
        _size = 6.0f;
        _style = PointStyle.Circle;
        _drawOutline = true;
        _drawFill = true;
        _angle = 0;
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get marker type
     *
     * @return Marker type
     */
    public MarkerType getMarkerType() {
        return _markerType;
    }

    /**
     * Set marker type
     *
     * @param markerType Marker type
     */
    public void setMarkerType(MarkerType markerType) {
        _markerType = markerType;
    }

    /**
     * Get font name
     *
     * @return Font name string
     */
    public String getFontName() {
        return _fontName;
    }

    /**
     * Set font name
     *
     * @param name Font name string
     */
    public void setFontName(String name) {
        _fontName = name;
    }

    /**
     * Get character index
     *
     * @return Character index
     */
    public int getCharIndex() {
        return _charIndex;
    }

    /**
     * Set character index
     *
     * @param idx Index
     */
    public void setCharIndex(int idx) {
        _charIndex = idx;
    }

    /**
     * Get image file path
     *
     * @return Image file path
     */
    public String getImagePath() {
        return _imagePath;
    }

    /**
     * Set image file path
     *
     * @param path Image file path
     */
    public void setImagePath(String path) {
        _imagePath = path;
    }

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
     * @param c Color
     */
    public void setOutlineColor(Color c) {
        _outlineColor = c;
    }

    /**
     * Get size
     *
     * @return Size
     */
    public float getSize() {
        return _size;
    }

    /**
     * Set size
     *
     * @param size Size
     */
    public void setSize(float size) {
        _size = size;
    }

    /**
     * Get point style
     *
     * @return Point style
     */
    public PointStyle getStyle() {
        return _style;
    }

    /**
     * Set point style
     *
     * @param style Point style
     */
    public void setStyle(PointStyle style) {
        if (style != null)
            _style = style;
    }

    /**
     * Get if draw outline
     *
     * @return Boolean
     */
    public boolean getDrawOutline() {
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
    public boolean getDrawFill() {
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
     * Get point angle
     *
     * @return Angle
     */
    public float getAngle() {
        return _angle;
    }

    /**
     * Set point angle
     *
     * @param angle Angle
     */
    public void setAngle(float angle) {
        _angle = angle;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Get property object
     *
     * @return Property object
     */
    @Override
    public Object getPropertyObject() {
        HashMap objAttr = new HashMap();
        objAttr.put("Color", "Color");
        objAttr.put("OutlineColor", "OutlineColor");
        objAttr.put("Size", "Size");
        objAttr.put("Style", "Style");
        objAttr.put("DrawOutline", "DrawOutline");
        objAttr.put("DrawFill", "DrawFill");
        objAttr.put("DrawPoint", "DrawPoint");
        objAttr.put("Angle", "Angle");
        return objAttr;
    }

    /**
     * Clone
     *
     * @return PointBreak
     */
    @Override
    public Object clone() {
        PointBreak aCB = new PointBreak();
        aCB.setCaption(this.getCaption());
        aCB.setColor(this.getColor());
        aCB.setDrawShape(this.isDrawShape());
        aCB.setEndValue(this.getEndValue());
        aCB.setNoData(this.isNoData());
        aCB.setStartValue(this.getStartValue());
        aCB.setMarkerType(_markerType);
        aCB.setFontName(_fontName);
        aCB.setCharIndex(_charIndex);
        aCB.setImagePath(_imagePath);
        aCB.setOutlineColor(_outlineColor);
        aCB.setSize(_size);
        aCB.setDrawOutline(_drawOutline);
        aCB.setDrawFill(_drawFill);
        aCB.setStyle(_style);
        aCB.setAngle(_angle);

        return aCB;
    }

    /**
     * Export to xml document
     *
     * @param doc xml document
     * @param parent parent xml element
     */
    @Override
    public void exportToXML(Document doc, Element parent) {
        Element brk = doc.createElement("Break");
        Attr caption = doc.createAttribute("Caption");
        Attr startValue = doc.createAttribute("StartValue");
        Attr endValue = doc.createAttribute("EndValue");
        Attr color = doc.createAttribute("Color");
        Attr isNoData = doc.createAttribute("IsNoData");

        caption.setValue(this.getCaption());
        startValue.setValue(String.valueOf(this.getStartValue()));
        endValue.setValue(String.valueOf(this.getEndValue()));
        color.setValue(ColorUtil.toHexEncoding(this.getColor()));
        isNoData.setValue(String.valueOf(this.isNoData()));

        brk.setAttributeNode(caption);
        brk.setAttributeNode(startValue);
        brk.setAttributeNode(endValue);
        brk.setAttributeNode(color);
        brk.setAttributeNode(isNoData);

        parent.appendChild(brk);
    }
    // </editor-fold>
}
