/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.shape;

import java.awt.image.BufferedImage;
import org.meteoinfo.global.Extent;

/**
 *
 * @author Yaqiang Wang
 */
public class ImageShape extends PointShape {
    // <editor-fold desc="Variables">
    private BufferedImage image;
    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public ImageShape(){
        super();
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    /**
     * Get image
     * @return Image
     */
    public BufferedImage getImage(){
        return this.image;
    }
    
    /**
     * Set image
     * @param value Image
     */
    public void setImage(BufferedImage value){
        this.image = value;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">
    @Override
    public ShapeTypes getShapeType(){
        return ShapeTypes.Image;
    }
    
    @Override
    public Extent getExtent(){
        Extent extent = new Extent();
        extent.minX = this.getPoint().X;
        extent.minY = this.getPoint().Y;
        extent.maxX = extent.minX + this.image.getWidth();
        extent.maxY = extent.minY + this.image.getHeight();
        return extent;
    }
    // </editor-fold>
}
