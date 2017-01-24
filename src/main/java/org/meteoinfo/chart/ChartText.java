/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.meteoinfo.drawing.Draw;

/**
 *
 * @author yaqiang
 */
public class ChartText {
    // <editor-fold desc="Variables">
    private Font font;
    private List<String> text;
    private Color color;
    private float x;
    private float y;
    private int lineSpace;
    private CoordinateType coordinates;
    // </editor-fold>    
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public ChartText(){
        font = new Font("Arial", Font.BOLD, 14);
        color = Color.black;
        lineSpace = 3;
        coordinates = CoordinateType.DATA;
    }
    
    /**
     * Constructor
     * @param text Text
     */
    public ChartText(String text){
        this();
        this.text = new ArrayList<>();
        String[] lines = text.split("\n");
        this.text.addAll(Arrays.asList(lines));
    }
    
    /**
     * Constructor
     * @param text Text
     */
    public ChartText(List<String> text){
        this();
        this.text = text;
    }
    
    /**
     * Constructor
     * @param text Text
     * @param font Font
     */
    public ChartText(String text, Font font){
        this();
        this.text = new ArrayList<>();
        String[] lines = text.split("\n");
        this.text.addAll(Arrays.asList(lines));
        this.font = font;
    }
    
    /**
     * Constructor
     * @param text Text
     * @param font Font
     */
    public ChartText(List<String> text, Font font){
        this();
        this.text = text;
        this.font = font;
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    /**
     * Get text
     * @return Text
     */
    public String getText(){
        return text.get(0);
    }
    
    /**
     * Set text
     * @param value Text
     */
    public void setText(String value){
        text = new ArrayList<>();
        String[] lines = value.split("\n");
        this.text.addAll(Arrays.asList(lines));
    }
    
    /**
     * Get texts
     * @return Text list
     */
    public List<String> getTexts(){
        return text;
    }
    
    /**
     * Set texts
     * @param value Text list 
     */
    public void setTexts(List<String> value){
        text = value;
    }
    
    /**
     * Get font
     * @return Font
     */
    public Font getFont(){
        return font;
    }
    
    /**
     * Set font
     * @param value Font
     */
    public void setFont(Font value){
        font = value;
    }
    
    /**
     * Get title color
     * @return Title color
     */
    public Color getColor(){
        return color;
    }
    
    /**
     * Set title color
     * @param value Title color
     */
    public void setColor(Color value){
        this.color = value;
    }
    
    /**
     * Get x
     * @return X
     */
    public float getX(){
        return this.x;
    }
    
    /**
     * Set x
     * @param value X
     */
    public void setX(float value){
        this.x = value;
    }
    
    /**
     * Get y
     * @return Y
     */
    public float getY(){
        return this.y;
    }
    
    /**
     * Set y
     * @param value Y
     */
    public void setY(float value) {
        this.y = value;
    }
    
    /**
     * Get line space
     * @return Line space
     */
    public int getLineSpace(){
        return this.lineSpace;
    }
    
    /**
     * Set line space
     * @param value Line space
     */
    public void setLineSpace(int value){
        this.lineSpace = value;
    }
    
    /**
     * Get coordinates
     * @return Coordinates
     */
    public CoordinateType getCoordinates(){
        return this.coordinates;
    }
    
    /**
     * Set coordinates
     * @param value Coordinates
     */
    public void setCoordinates(CoordinateType value){
        this.coordinates = value;
    }
    
    /**
     * Set coordinates
     * @param value Coordinates
     */
    public void setCoordinates(String value){
        switch (value){
            case "axes":
                this.coordinates = CoordinateType.AXES;
                break;
            case "figure":
                this.coordinates = CoordinateType.FIGURE;
                break;
            case "data":
                this.coordinates = CoordinateType.DATA;
                break;
            case "inches":
                this.coordinates = CoordinateType.INCHES;
                break;
        }
    }
    // </editor-fold>
    // <editor-fold desc="Methods">
    /**
     * Get text line number
     * @return Text line number
     */
    public int getLineNum(){
        return this.text.size();
    }
    
    /**
     * Get height
     * @param g Graphics2D
     * @return Height
     */
    public int getHeight(Graphics2D g) {
        g.setFont(this.font);
        int h = 0;
        for (String line : this.text){
            Dimension dim = Draw.getStringDimension(line, g);
            h += dim.height + this.lineSpace;
        }
        return h - this.lineSpace;
    }
    // </editor-fold>
}
