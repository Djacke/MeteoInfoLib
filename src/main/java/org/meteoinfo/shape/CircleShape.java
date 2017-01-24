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
package org.meteoinfo.shape;

/**
 * Circle shape class
 * 
 * @author Yaqiang Wang
 */
public class CircleShape extends PolygonShape {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public CircleShape() {

    }
    // </editor-fold>
    // <editor-fold desc="Methods">
    
    @Override
    public ShapeTypes getShapeType(){
        return ShapeTypes.Circle;
    }

    /**
     * Clone
     * 
     * @return CircleShape
     */
    @Override
    public Object clone() {
        CircleShape aPGS = new CircleShape();
        aPGS.setExtent(this.getExtent());
        aPGS.setPoints(this.getPoints());
        aPGS.setVisible(this.isVisible());
        aPGS.setSelected(this.isSelected());

        return aPGS;
    }
    // </editor-fold>
}
