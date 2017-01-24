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

import org.meteoinfo.global.colors.ColorUtil;
import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Color break class
 *
 * @author Yaqiang Wang
 */
public class ColorBreak {
    // <editor-fold desc="Variables">

    private BreakTypes _breakType;
    private Object _startValue;
    private Object _endValue;
    private Color _color;
    private String _caption;
    private boolean _isNoData;
    private boolean _drawShape;
    private String tag;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public ColorBreak() {
        _breakType = BreakTypes.ColorBreak;
        _color = Color.red;
        _isNoData = false;
        _drawShape = true;
        _startValue = 0;
        _endValue = 0;
        _caption = "";
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get break type
     *
     * @return Break type
     */
    public BreakTypes getBreakType() {
        return _breakType;
    }

    /**
     * Set break type
     *
     * @param breakType Break type
     */
    public void setBreakType(BreakTypes breakType) {
        _breakType = breakType;
    }

    /**
     * Get start value
     *
     * @return Start value
     */
    public Object getStartValue() {
        return _startValue;
    }

    /**
     * Set start value
     *
     * @param startValue  Start value
     */
    public void setStartValue(Object startValue) {
        _startValue = startValue;
    }

    /**
     * Get end value
     *
     * @return End value
     */
    public Object getEndValue() {
        return _endValue;
    }

    /**
     * Set end value
     *
     * @param endValue End Value
     */
    public void setEndValue(Object endValue) {
        _endValue = endValue;
    }

    /**
     * Get color
     *
     * @return Color
     */
    public Color getColor() {
        return _color;
    }

    /**
     * Set color
     *
     * @param c Color
     */
    public void setColor(Color c) {
        _color = c;
    }

    /**
     * Get caption
     *
     * @return Caption
     */
    public String getCaption() {
        return _caption;
    }

    /**
     * Set caption
     *
     * @param caption Caption
     */
    public void setCaption(String caption) {
        _caption = caption;
    }

    /**
     * Get if is undefine data
     *
     * @return boolean
     */
    public boolean isNoData() {
        return _isNoData;
    }

    /**
     * Set if is undefine data
     *
     * @param isTrue boolean
     */
    public void setNoData(boolean isTrue) {
        _isNoData = isTrue;
    }

    /**
     * Get if draw shape
     *
     * @return boolean
     */
    public boolean isDrawShape() {
        return _drawShape;
    }

    /**
     * Set if draw shape
     *
     * @param isTrue boolean
     */
    public void setDrawShape(boolean isTrue) {
        _drawShape = isTrue;
    }
    
    /**
     * Get tag
     * @return Tag 
     */
    public String getTag(){
        return this.tag;
    }
    
    /**
     * Set tag
     * @param value Tag
     */
    public void setTag(String value){
        this.tag = value;
    }

    // </editor-fold>
    // <editor-fold desc="Methods">
    /**
     * Get property object
     *
     * @return Custom property object
     */
    public Object getPropertyObject() {
        Map objAttr = new HashMap();
        objAttr.put("Color", "Color");
        return objAttr;
    }

    /**
     * Clone
     *
     * @return ColorBreak
     */
    @Override
    public Object clone() {
        //return MemberwiseClone();
        ColorBreak aCB = new ColorBreak();
        aCB.setCaption(_caption);
        aCB.setColor(_color);
        aCB.setDrawShape(_drawShape);
        aCB.setEndValue(_endValue);
        aCB.setNoData(_isNoData);
        aCB.setStartValue(_startValue);
        aCB.setTag(tag);

        return aCB;
    }

    /**
     * Export to XML document
     *
     * @param doc XML document
     * @param parent Parent XML element
     */
    public void exportToXML(Document doc, Element parent) {
        Element brk = doc.createElement("Break");
        Attr caption = doc.createAttribute("Caption");
        Attr startValue = doc.createAttribute("StartValue");
        Attr endValue = doc.createAttribute("EndValue");
        Attr color = doc.createAttribute("Color");
        Attr isNoData = doc.createAttribute("IsNoData");
        Attr tagAttr = doc.createAttribute("Tag");

        caption.setValue(_caption);
        startValue.setValue(String.valueOf(_startValue));
        endValue.setValue(String.valueOf(_endValue));
        color.setValue(ColorUtil.toHexEncoding(_color));
        isNoData.setValue(String.valueOf(_isNoData));
        tagAttr.setValue(tag);

        brk.setAttributeNode(caption);
        brk.setAttributeNode(startValue);
        brk.setAttributeNode(endValue);
        brk.setAttributeNode(color);
        brk.setAttributeNode(isNoData);
        brk.setAttributeNode(tagAttr);

        parent.appendChild(brk);
    }

    /**
     * Get value string
     *
     * @return value string
     */
    public String getValueString() {
        if (String.valueOf(_startValue) == null ? String.valueOf(_endValue) == null : String.valueOf(_startValue).equals(String.valueOf(_endValue))) {
            return String.valueOf(_startValue);
        } else {
            return String.valueOf(_startValue) + " - " + String.valueOf(_endValue);
        }
    }
    // </editor-fold>
}
