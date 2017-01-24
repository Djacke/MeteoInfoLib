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
package org.meteoinfo.data.meteodata.hysplit;

import org.meteoinfo.data.meteodata.ascii.ASCIIGridDataInfo;
import org.meteoinfo.data.GridData;
import org.meteoinfo.data.meteodata.DataInfo;
import org.meteoinfo.data.meteodata.Dimension;
import org.meteoinfo.data.meteodata.DimensionType;
import org.meteoinfo.data.meteodata.IGridDataInfo;
import org.meteoinfo.data.meteodata.Variable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.meteoinfo.data.GridArray;
import org.meteoinfo.data.meteodata.MeteoDataType;
import org.meteoinfo.data.meteodata.arl.ARLDataInfo;
import org.meteoinfo.global.util.DateUtil;
import ucar.ma2.Array;
import ucar.ma2.DataType;
import ucar.ma2.IndexIterator;
import ucar.ma2.InvalidRangeException;
import ucar.ma2.Range;
import ucar.ma2.Section;
import ucar.nc2.Attribute;

/**
 *
 * @author yaqiang
 */
public class HYSPLITConcDataInfo extends DataInfo implements IGridDataInfo {
    // <editor-fold desc="Variables">

    private int _pack_flag;
    private int _loc_num;
    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public HYSPLITConcDataInfo(){
        this.setDataType(MeteoDataType.HYSPLIT_Conc);
    }
    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    // </editor-fold>
    // <editor-fold desc="Methods">

    @Override
    public void readDataInfo(String fileName) {
        try {
            this.setFileName(fileName);

            RandomAccessFile br = new RandomAccessFile(fileName, "r");
            int i, j, hBytes;
            byte[] aBytes;

            //Record #1
            br.skipBytes(4);
            aBytes = new byte[4];
            br.read(aBytes);
            String Ident = new String(aBytes);
            int year = br.readInt();
            int month = br.readInt();
            int day = br.readInt();
            int hour = br.readInt();
            int forecast_hour = br.readInt();
            _loc_num = br.readInt();
            _pack_flag = br.readInt();

            //Record #2
            Object[][] locArray = new Object[8][_loc_num];
            for (i = 0; i < _loc_num; i++) {
                br.skipBytes(8);
                for (j = 0; j < 4; j++) {
                    locArray[j][i] = br.readInt();
                }
                for (j = 4; j < 7; j++) {
                    locArray[j][i] = br.readFloat();
                }
                locArray[7][i] = br.readInt();
            }

            //Record #3
            String fName = new File(fileName).getName().toLowerCase();
            if (fName.contains("gemzint")) {
                br.skipBytes(4);   //For vertical concentration file gemzint
            } else {
                br.skipBytes(8);
            }
            int lat_point_num = br.readInt();
            int lon_point_num = br.readInt();
            float lat_delta = br.readFloat();
            float lon_delta = br.readFloat();
            float lat_LF = br.readFloat();
            float lon_LF = br.readFloat();

            double[] X = new double[lon_point_num];
            double[] Y = new double[lat_point_num];
            for (i = 0; i < lon_point_num; i++) {
                X[i] = lon_LF + i * lon_delta;
            }
            if (X[0] == 0 && X[X.length - 1]
                    + lon_delta == 360) {
                this.setGlobal(true);
            }
            for (i = 0; i < lat_point_num; i++) {
                Y[i] = lat_LF + i * lat_delta;
            }

            Dimension xDim = new Dimension(DimensionType.X);
            xDim.setValues(X);
            this.setXDimension(xDim);
            Dimension yDim = new Dimension(DimensionType.Y);
            yDim.setValues(Y);
            this.setYDimension(yDim);

            //Record #4
            br.skipBytes(8);
            int level_num = br.readInt();
            double[] heights = new double[level_num];
            for (i = 0; i < level_num; i++) {
                heights[i] = br.readInt();
            }
            Dimension zDim = new Dimension(DimensionType.Z);
            zDim.setValues(heights);
            this.setZDimension(zDim);

            //Record #5
            br.skipBytes(8);
            int pollutant_num = br.readInt();
            List<Variable> variables = new ArrayList<>();
            for (i = 0; i < pollutant_num; i++) {
                br.read(aBytes);
                Variable var = new Variable();
                var.setName(new String(aBytes));
                variables.add(var);
            }
            this.setVariables(variables);

            hBytes = 36 + lon_point_num * 40 + 32 + 12 + level_num * 4 + 12
                    + pollutant_num * 4;
            int hByte_num = hBytes;

            //Record Data
            int k, tNum;
            tNum = 0;
            int[] sampleTimes = new int[6];
            String dStr;
            Date aDateTime;
            List<Date> sample_start = new ArrayList<>();
            List<Date> sample_stop = new ArrayList<>();
            do {
                //Record #6
                br.skipBytes(8);
                for (i = 0; i < 6; i++) {
                    sampleTimes[i] = br.readInt();
                }
                year = sampleTimes[0];
                if (year < 50) {
                    year = 2000 + year;
                } else {
                    year = 1900 + year;
                }
                Calendar cal = new GregorianCalendar(year, sampleTimes[1] - 1, sampleTimes[2], sampleTimes[3], 0, 0);
                aDateTime = cal.getTime();
                sample_start.add(aDateTime);

                //Record #7
                br.skipBytes(8);
                for (i = 0; i < 6; i++) {
                    sampleTimes[i] = br.readInt();
                }
                year = sampleTimes[0];
                if (year < 50) {
                    year = 2000 + year;
                } else {
                    year = 1900 + year;
                }
                cal = new GregorianCalendar(year, sampleTimes[1] - 1, sampleTimes[2], sampleTimes[3], 0, 0);
                aDateTime = cal.getTime();
                sample_stop.add(aDateTime);

                //Record 8;
                int aLevel, aN, IP, JP;
                String aType;
                for (i = 0; i < pollutant_num; i++) {
                    for (j = 0; j < level_num; j++) {
                        if (_pack_flag == 1) {
                            br.skipBytes(8);
                            br.read(aBytes);
                            aType = new String(aBytes);
                            aLevel = br.readInt();
                            aN = br.readInt();
                            for (k = 0; k < aN; k++) {
                                if (br.getFilePointer() + 8 > br.length()) {
                                    break;
                                }
                                IP = br.readShort();
                                JP = br.readShort();
                                br.skipBytes(4);
                            }
                        } else {
                            br.skipBytes(8);
                            br.read(aBytes);
                            aType = new String(aBytes);
                            aLevel = br.readInt();
                            for (JP = 0; JP < lat_point_num; JP++) {
                                for (IP = 0; IP < lon_point_num; IP++) {
                                    br.skipBytes(4);
                                }
                            }
                        }
                    }
                }

                tNum += 1;

                if (br.getFilePointer() + 10 > br.length()) {
                    break;
                }
            } while (true);

            List<Double> values = new ArrayList<>();
            for (Date t : sample_start) {
                values.add(DateUtil.toOADate(t));
            }
            Dimension tDim = new Dimension(DimensionType.T);
            tDim.setValues(values);
            this.setTimeDimension(tDim);

            for (Variable var : variables) {                                
                var.setDimension(tDim);
                var.setDimension(zDim);
                var.setDimension(yDim);
                var.setDimension(xDim);
            }
            //this.setTimes(times);
            this.setVariables(variables);

            br.close();
        } catch (IOException ex) {
            Logger.getLogger(ASCIIGridDataInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get global attributes
     * @return Global attributes
     */
    @Override
    public List<Attribute> getGlobalAttributes(){
        return new ArrayList<>();
    }

    @Override
    public String generateInfoText() {
        String dataInfo;
        dataInfo = "File Name: " + this.getFileName();
        dataInfo += System.getProperty("line.separator") + "Pack Flag = " + String.valueOf(_pack_flag);
        dataInfo += System.getProperty("line.separator") + "Xsize = " + String.valueOf(this.getXDimension().getLength())
                + "  Ysize = " + String.valueOf(this.getYDimension().getLength()) + "  Zsize = " + String.valueOf(this.getZDimension().getLength())
                + "  Tsize = " + String.valueOf(this.getTimeDimension().getLength());
        dataInfo += System.getProperty("line.separator") + "Number of Variables = " + String.valueOf(this.getVariableNum());
        for (String v : this.getVariableNames()) {
            dataInfo += System.getProperty("line.separator") + v;
        }

        return dataInfo;
    }
    
    /**
     * Read array data of a variable
     * 
     * @param varName Variable name
     * @return Array data
     */
    @Override
    public Array read(String varName){
        Variable var = this.getVariable(varName);
        int n = var.getDimNumber();
        int[] origin = new int[n];
        int[] size = new int[n];
        int[] stride = new int[n];
        for (int i = 0; i < n; i++){
            origin[i] = 0;
            size[i] = var.getDimLength(i);
            stride[i] = 1;
        }
        
        Array r = read(varName, origin, size, stride);
        
        return r;
    }
    
    /**
     * Read array data of the variable
     *
     * @param varName Variable name
     * @param origin The origin array
     * @param size The size array
     * @param stride The stride array
     * @return Array data
     */
    @Override
    public Array read(String varName, int[] origin, int[] size, int[] stride) {
        try {
            Variable var = this.getVariable(varName);
            Section section = new Section(origin, size, stride);
            Array dataArray = Array.factory(DataType.DOUBLE, section.getShape());
            int rangeIdx = 0;
            Range timeRange = section.getRank() > 2 ? section
                    .getRange(rangeIdx++)
                    : new Range(0, 0);

            Range levRange = var.getLevelNum() > 0 ? section
                    .getRange(rangeIdx++)
                    : new Range(0, 0);

            Range yRange = section.getRange(rangeIdx++);
            Range xRange = section.getRange(rangeIdx);

            IndexIterator ii = dataArray.getIndexIterator();

            for (int timeIdx = timeRange.first(); timeIdx <= timeRange.last();
                    timeIdx += timeRange.stride()) {
                int levelIdx = levRange.first();

                for (; levelIdx <= levRange.last();
                        levelIdx += levRange.stride()) {
                    readXY(varName, timeIdx, levelIdx, yRange, xRange, ii);
                }
            }

            return dataArray;
        } catch (InvalidRangeException ex) {
            Logger.getLogger(HYSPLITConcDataInfo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    private void readXY(String varName, int timeIdx, int levelIdx, Range yRange, Range xRange, IndexIterator ii) {
        try {
            int varIdx = this.getVariableNames().indexOf(varName);
            RandomAccessFile br = new RandomAccessFile(this.getFileName(), "r");
            int i, j, nBytes;
            byte[] aBytes = new byte[4];
            int xNum = this.getXDimension().getLength();
            int yNum = this.getYDimension().getLength();
            double[][] dataArray = new double[xNum][yNum];
            double[] data = new double[yNum * xNum];

            //Record #1            
            br.skipBytes(36);

            //Record #2
            nBytes = (8 * 4 + 8) * _loc_num;
            br.skipBytes(nBytes);

            //Record #3
            String fName = new File(this.getFileName()).getName().toLowerCase();
            if (fName.contains("gemzint")) {
                br.skipBytes(28);   //For vertical concentration file gemzint
            } else {
                br.skipBytes(32);
            }

            //Record #4
            nBytes = 12 + this.getZDimension().getLength() * 4;
            br.skipBytes(nBytes);

            //Record #5
            nBytes = 12 + this.getVariableNum() * 4;
            br.skipBytes(nBytes);

            //Record Data
            int t, k;
            int aLevel, aN, IP, JP;
            String aType;
            double aConc;
            for (t = 0; t < this.getTimeNum(); t++) {
                br.skipBytes(64);

                for (i = 0; i < this.getVariableNum(); i++) {
                    for (j = 0; j < this.getZDimension().getLength(); j++) {
                        if (t == timeIdx && i == varIdx && j == levelIdx) {
                            if (br.getFilePointer() + 28 > br.length()) {
                                break;
                            }
                            if (_pack_flag == 1) {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                aN = br.readInt();
                                for (k = 0; k < aN; k++) {
                                    if (br.getFilePointer() + 8 > br.length()) {
                                        break;
                                    }
                                    IP = br.readShort();
                                    JP = br.readShort();
                                    aConc = br.readFloat();
                                    if (IP >= 0 && IP < xNum && JP >= 0 && JP < yNum) {
                                        dataArray[IP][JP] = aConc;
                                    }
                                }
                            } else {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                for (JP = 0; JP < yNum; JP++) {
                                    for (IP = 0; IP < xNum; IP++) {
                                        aConc = br.readFloat();
                                        dataArray[IP][JP] = aConc;
                                    }
                                }
                            }
                        } else {
                            if (br.getFilePointer() + 28 > br.length()) {
                                break;
                            }
                            if (_pack_flag == 1) {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                aN = br.readInt();
                                for (k = 0; k < aN; k++) {
                                    if (br.getFilePointer() + 8 > br.length()) {
                                        break;
                                    }
                                    IP = br.readShort();
                                    JP = br.readShort();
                                    br.skipBytes(4);
                                }
                            } else {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                for (JP = 0; JP < yNum; JP++) {
                                    for (IP = 0; IP < xNum; IP++) {
                                        br.skipBytes(4);
                                    }
                                }
                            }
                        }
                    }
                }

                if (br.getFilePointer() + 10 > br.length()) {
                    break;
                }
            }

            br.close();

            for (i = 0; i < xNum; i++) {
                for (j = 0; j < yNum; j++) {
                    data[i * yNum + j] = dataArray[j][i];
                }
            }
            
            for (int y = yRange.first(); y <= yRange.last();
                    y += yRange.stride()) {
                for (int x = xRange.first(); x <= xRange.last();
                        x += xRange.stride()) {
                    int index = y * xNum + x;
                    ii.setDoubleNext(data[index]);
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ARLDataInfo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ARLDataInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get grid data
     *
     * @param varName Variable name
     * @return Grid data
     */
    @Override
    public GridArray getGridArray(String varName) {
        return null;    
    }

    @Override
    public GridData getGridData_LonLat(int timeIdx, int varIdx, int levelIdx) {
        try {
            RandomAccessFile br = new RandomAccessFile(this.getFileName(), "r");
            int i, j, nBytes;
            byte[] aBytes = new byte[4];
            int xNum = this.getXDimension().getLength();
            int yNum = this.getYDimension().getLength();
            double[][] dataArray = new double[xNum][yNum];
            double[][] newDataArray = new double[yNum][xNum];

            //Record #1            
            br.skipBytes(36);

            //Record #2
            nBytes = (8 * 4 + 8) * _loc_num;
            br.skipBytes(nBytes);

            //Record #3
            String fName = new File(this.getFileName()).getName().toLowerCase();
            if (fName.contains("gemzint")) {
                br.skipBytes(28);   //For vertical concentration file gemzint
            } else {
                br.skipBytes(32);
            }

            //Record #4
            nBytes = 12 + this.getZDimension().getLength() * 4;
            br.skipBytes(nBytes);

            //Record #5
            nBytes = 12 + this.getVariableNum() * 4;
            br.skipBytes(nBytes);

            //Record Data
            int t, k;
            int aLevel, aN, IP, JP;
            String aType;
            double aConc;
            for (t = 0; t < this.getTimeNum(); t++) {
                br.skipBytes(64);

                for (i = 0; i < this.getVariableNum(); i++) {
                    for (j = 0; j < this.getZDimension().getLength(); j++) {
                        if (t == timeIdx && i == varIdx && j == levelIdx) {
                            if (br.getFilePointer() + 28 > br.length()) {
                                break;
                            }
                            if (_pack_flag == 1) {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                aN = br.readInt();
                                for (k = 0; k < aN; k++) {
                                    if (br.getFilePointer() + 8 > br.length()) {
                                        break;
                                    }
                                    IP = br.readShort();
                                    JP = br.readShort();
                                    aConc = br.readFloat();
                                    if (IP >= 0 && IP < xNum && JP >= 0 && JP < yNum) {
                                        dataArray[IP][JP] = aConc;
                                    }
                                }
                            } else {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                for (JP = 0; JP < yNum; JP++) {
                                    for (IP = 0; IP < xNum; IP++) {
                                        aConc = br.readFloat();
                                        dataArray[IP][JP] = aConc;
                                    }
                                }
                            }
                        } else {
                            if (br.getFilePointer() + 28 > br.length()) {
                                break;
                            }
                            if (_pack_flag == 1) {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                aN = br.readInt();
                                for (k = 0; k < aN; k++) {
                                    if (br.getFilePointer() + 8 > br.length()) {
                                        break;
                                    }
                                    IP = br.readShort();
                                    JP = br.readShort();
                                    br.skipBytes(4);
                                }
                            } else {
                                br.skipBytes(8);
                                br.read(aBytes);
                                aType = new String(aBytes);
                                aLevel = br.readInt();
                                for (JP = 0; JP < yNum; JP++) {
                                    for (IP = 0; IP < xNum; IP++) {
                                        br.skipBytes(4);
                                    }
                                }
                            }
                        }
                    }
                }

                if (br.getFilePointer() + 10 > br.length()) {
                    break;
                }
            }

            br.close();

            double[] newX = this.getXDimension().getValues();
            for (i = 0; i < xNum; i++) {
                for (j = 0; j < yNum; j++) {
                    newDataArray[j][i] = dataArray[i][j];
                }
            }

            GridData gridData = new GridData();
            gridData.data = newDataArray;
            gridData.xArray = newX;
            gridData.yArray = this.getYDimension().getValues();
            gridData.missingValue = this.getMissingValue();

            return gridData;
        } catch (IOException ex) {
            Logger.getLogger(ASCIIGridDataInfo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public GridData getGridData_TimeLat(int lonIdx, int varIdx, int levelIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_TimeLon(int latIdx, int varIdx, int levelIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_LevelLat(int lonIdx, int varIdx, int timeIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_LevelLon(int latIdx, int varIdx, int timeIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_LevelTime(int latIdx, int varIdx, int lonIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_Time(int lonIdx, int latIdx, int varIdx, int levelIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_Level(int lonIdx, int latIdx, int varIdx, int timeIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_Lon(int timeIdx, int latIdx, int varIdx, int levelIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public GridData getGridData_Lat(int timeIdx, int lonIdx, int varIdx, int levelIdx) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    // </editor-fold>
}
