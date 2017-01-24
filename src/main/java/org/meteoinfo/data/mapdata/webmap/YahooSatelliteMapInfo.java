/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.meteoinfo.data.mapdata.webmap;

/**
 *
 * @author yaqiang
 */
public class YahooSatelliteMapInfo extends TileFactoryInfo {
    // <editor-fold desc="Variables">
    private String version = "1.9";
    // </editor-fold>
    // <editor-fold desc="Constructor">

    /**
     * Constructor
     */
    public YahooSatelliteMapInfo() {
        super("YahooSatelliteMap", 1, 17, 19,
                256, true, true, // tile size is 256 and x/y orientation is normal
                "http://maps%1$d.yimg.com/ae/ximg?v=%2$s&t=a&s=256&.intl=%3$s&x=%4$d&y=%5$d&z=%6$d&r=1",
                "x", "y", "z");
    }
//    // </editor-fold>
//    // <editor-fold desc="Get Set Methods">
//    // </editor-fold>
//    // <editor-fold desc="Methods">

    @Override
    public String getTileUrl(int x, int y, int zoom) {
        zoom = this.getTotalMapZoom() - zoom;
        int serverNum = this.getServerNum(x, y, 2) + 1;
        String url = String.format(this.baseURL, serverNum, version, this.getLanguage(), x, ((1 << zoom) >> 1) - 1 - y, zoom + 1);
        return url;
    }
   
    // </editor-fold>
}
