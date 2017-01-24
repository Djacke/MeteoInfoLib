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

import java.util.ArrayList;

/**
 *
 * @author yaqiang
 */
public class RectangleShape extends PolygonShape {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">

    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    @Override
    public ShapeTypes getShapeType(){
        return ShapeTypes.Rectangle;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    /**
     * Clone
     *
     * @return RectangleShape object
     */
    @Override
    public Object clone() {
        RectangleShape aPGS = new RectangleShape();
        aPGS.setExtent(this.getExtent());
        aPGS.setPoints(new ArrayList<>(this.getPoints()));
        aPGS.setVisible(this.isVisible());
        aPGS.setSelected(this.isSelected());

        return aPGS;
    }
    // </editor-fold>
}
