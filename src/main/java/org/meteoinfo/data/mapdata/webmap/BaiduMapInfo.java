/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.data.mapdata.webmap;

/**
 *
 * @author yaqiang
 */
public class BaiduMapInfo extends TileFactoryInfo {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public BaiduMapInfo() {
        super("BaiduMap", 3, 18, 19,
                256, true, true, // tile size is 256 and x/y orientation is normal
                "http://api0.map.bdimg.com/customimage/tile?&x=%1$d&y=%2$d&z=%3$d&udt=20141112&customid=googlelite",
                "x", "y", "z");
    }
//    // </editor-fold>
//    // <editor-fold desc="Get Set Methods">
    
//    // </editor-fold>
//    // <editor-fold desc="Methods">
    
    @Override
    public String getTileUrl(int x, int y, int zoom) {
        zoom = this.getTotalMapZoom() - zoom;
        String url = String.format(this.baseURL, x, y, zoom);
        return url;
    }
    // </editor-fold>
}
