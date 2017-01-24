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
package org.meteoinfo.data.meteodata.micaps;

import org.meteoinfo.data.meteodata.MeteoDataType;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author yaqiang
 */
public class MICAPSDataInfo {
    // <editor-fold desc="Variables">
    // </editor-fold>
    // <editor-fold desc="Constructor">
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">

    /**
     * Get MICAPS data type
     *
     * @param fileName File name
     * @return Data type
     */
    public static MeteoDataType getDataType(String fileName) {
        BufferedReader sr = null;
        MeteoDataType mdType = null;
        try {
            String dataType;
            sr = new BufferedReader(new FileReader(new File(fileName)));
            String aLine;
            String[] dataArray;

            aLine = sr.readLine().trim();
            dataArray = aLine.split("\\s+");
            dataType = dataArray[0] + " " + dataArray[1];
            dataType = dataType.trim().toLowerCase();
            if (dataType.equals("diamond 1")) {
                mdType = MeteoDataType.MICAPS_1;
            }
            if (dataType.equals("diamond 2")) {
                mdType = MeteoDataType.MICAPS_2;
            }
            if (dataType.equals("diamond 3")) {
                mdType = MeteoDataType.MICAPS_3;
            }
            if (dataType.equals("diamond 4")) {
                mdType = MeteoDataType.MICAPS_4;
            }
            if (dataType.equals("diamond 7")) {
                mdType = MeteoDataType.MICAPS_7;
            }
            if (dataType.equals("diamond 11")) {
                mdType = MeteoDataType.MICAPS_11;
            }
            if (dataType.equals("diamond 13")) {
                mdType = MeteoDataType.MICAPS_13;
            }
            if (dataType.equals("diamond 120")) {
                mdType = MeteoDataType.MICAPS_120;
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MICAPSDataInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MICAPSDataInfo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                sr.close();
                return mdType;
            } catch (IOException ex) {
                Logger.getLogger(MICAPSDataInfo.class.getName()).log(Level.SEVERE, null, ex);
                return null;
            }
        }
    }
    // </editor-fold>
    // <editor-fold desc="Methods">
    // </editor-fold>
}
