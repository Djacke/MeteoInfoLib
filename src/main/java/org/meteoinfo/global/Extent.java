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
package org.meteoinfo.global;

import java.awt.Rectangle;

/**
 * Template
 *
 * @author Yaqiang Wang
 */
public class Extent implements Cloneable{
    // <editor-fold desc="Variables">
    /// <summary>
    /// minimun x
    /// </summary>

    public double minX;
    /// <summary>
    /// maximum x
    /// </summary>
    public double maxX;
    /// <summary>
    /// minimum y
    /// </summary>
    public double minY;
    /// <summary>
    /// maximum y
    /// </summary>
    public double maxY;
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public Extent() {
    }

    /**
     * Constructor
     *
     * @param xMin Minimum X
     * @param xMax Maximum X
     * @param yMin Minimum Y
     * @param yMax Maximum Y
     */
    public Extent(double xMin, double xMax, double yMin, double yMax) {
        minX = xMin;
        maxX = xMax;
        minY = yMin;
        maxY = yMax;
    }

    /**
     * Constructor
     *
     * @param aExtent The extent
     */
    public Extent(Extent aExtent) {
        this.minX = aExtent.minX;
        this.maxX = aExtent.maxX;
        this.minY = aExtent.minY;
        this.maxY = aExtent.maxY;
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    public double getWidth() {
        return maxX - minX;
    }

    public double getHeight() {
        return maxY - minY;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Judge if this extent include another extent
     *
     * @param bExtent extent
     * @return is included
     */
    public boolean include(Extent bExtent) {
        if (minX <= bExtent.minX && maxX >= bExtent.maxX && minY <= bExtent.minY && maxY >= bExtent.maxY) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Convert to rectangle
     *
     * @return rectangel
     */
    public Rectangle convertToRectangle() {
        return new Rectangle((int) minX, (int) minY, (int) getWidth(), (int) getHeight());
    }

    /**
     * Get center point
     *
     * @return Center point
     */
    public PointD getCenterPoint() {
        return new PointD((maxX - minX) / 2 + minX, (maxY - minY) / 2 + minY);
    }

    /**
     * Shift extent
     *
     * @param dx X shift value
     * @param dy Y shift value
     * @return Shifted extent
     */
    public Extent shift(double dx, double dy) {
        return new Extent(minX + dx, maxX + dx, minY + dy, maxY + dy);
    }
    
    /**
     * Extends extent
     * @param dx X delta
     * @param dy Y delta
     * @return Extended extent
     */
    public Extent extend(double dx, double dy){
        return new Extent(minX - dx, maxX + dx, minY - dy, maxY + dy);
    }
    
    /**
     * Get is NaN or not
     * @return Boolean
     */
    public boolean isNaN(){
        return Double.isNaN(minX) || Double.isNaN(maxX) || Double.isNaN(minY) || Double.isNaN(maxY);
    }

    /**
     * Clone
     *
     * @return Extent object
     */
    @Override
    public Object clone() {
        Extent o = null;
        try {
            o = (Extent)super.clone();
        } catch (CloneNotSupportedException ex) {
            ex.printStackTrace();
        }
        
        return o;
    }
    // </editor-fold>
}
