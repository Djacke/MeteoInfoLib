 /* Copyright 2014 Yaqiang Wang,
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
package org.meteoinfo.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.meteoinfo.data.analysis.Statistics;
import org.meteoinfo.global.MIMath;
import org.meteoinfo.global.util.GlobalUtil;
import org.meteoinfo.table.ColumnData;
import org.meteoinfo.table.DataColumn;
import org.meteoinfo.table.DataRow;
import org.meteoinfo.table.DataTable;

/**
 *
 * @author Yaqiang Wang
 */
public class TableData {

    // <editor-fold desc="Variables">
    protected DataTable dataTable = new DataTable();
    protected double missingValue = -9999.0;

    // </editor-fold>
    // <editor-fold desc="Constructor">
    /**
     * Constructor
     */
    public TableData() {

    }

    /**
     * Constructor
     *
     * @param dataTable The data table
     */
    public TableData(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    // </editor-fold>
    // <editor-fold desc="Get Set Methods">
    /**
     * Get data table
     *
     * @return Data table
     */
    public DataTable getDataTable() {
        return dataTable;
    }

    /**
     * Set data table
     *
     * @param value Data table
     */
    public void setDataTable(DataTable value) {
        dataTable = value;
    }

    /**
     * Get missing value
     *
     * @return Missing value
     */
    public double getMissingValue() {
        return missingValue;
    }

    /**
     * Set missing value
     *
     * @param value Missing value
     */
    public void setMissingValue(double value) {
        missingValue = value;
    }
    // </editor-fold>
    // <editor-fold desc="Methods">

    // <editor-fold desc="Normal">
    /**
     * Add column
     *
     * @param col The column
     */
    public void addColumn(DataColumn col) {
        dataTable.addColumn(col);
    }

    /**
     * Add column
     *
     * @param index The index
     * @param col The column
     */
    public void addColumn(int index, DataColumn col) {
        dataTable.addColumn(index, col);
    }

    /**
     * Add column
     *
     * @param colName Column name
     * @param dataType Data type
     */
    public void addColumn(String colName, DataTypes dataType) {
        try {
            dataTable.addColumn(colName, dataType);
        } catch (Exception ex) {
            Logger.getLogger(TimeTableData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add column
     *
     * @param index The index
     * @param colName Column name
     * @param dataType Data type
     */
    public void addColumn(int index, String colName, DataTypes dataType) {
        try {
            dataTable.addColumn(index, colName, dataType);
        } catch (Exception ex) {
            Logger.getLogger(TimeTableData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add column data
     *
     * @param colName Column name
     * @param dt Data type string
     * @param colData The column data
     * @throws Exception
     */
    public void addColumnData(String colName, String dt, List<Object> colData) throws Exception {
        DataTypes dataType = TableUtil.toDataTypes(dt);
        switch (dataType) {
            case Date:
                if (colData.get(0) instanceof Date) {
                    this.dataTable.addColumnData(colName, dataType, colData);
                } else {
                    String dformat = TableUtil.getDateFormat(dt);
                    this.dataTable.addColumn(new DataColumn(colName, dataType, dformat));
                    this.dataTable.setValues(colName, colData);
                }
                break;
            default:
                this.dataTable.addColumnData(colName, dataType, colData);
                break;
        }
    }

    /**
     * Remove a column
     *
     * @param colName Column name
     */
    public void removeColumn(String colName) {
        this.dataTable.removeColumn(this.dataTable.findColumn(colName));
    }

    /**
     * Add data row
     */
    public void addRow() {
        try {
            dataTable.addRow();
        } catch (Exception ex) {
            Logger.getLogger(TimeTableData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Add data row
     *
     * @param row Data row
     */
    public void addRow(DataRow row) {
        try {
            dataTable.addRow(row);
        } catch (Exception ex) {
            Logger.getLogger(TimeTableData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Add data rows
     * @param rows Data rows
     */
    public void addRows(List<DataRow> rows){
        try {
            dataTable.addRows(rows);
        } catch (Exception ex) {
            Logger.getLogger(TableData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get data row
     * @param idx Index
     * @return Data row
     */
    public DataRow getRow(int idx){
        return this.dataTable.getRows().get(idx);
    }
    
    /**
     * Get data rows
     * @return Data rows
     */
    public List<DataRow> getRows(){
        return this.dataTable.getRows();
    }

    /**
     * Get data columns
     *
     * @return Data columns
     */
    public List<DataColumn> getDataColumns() {
        List<DataColumn> cols = new ArrayList<>();
        for (DataColumn col : dataTable.getColumns()) {
            if (col.getDataType() != DataTypes.Date) {
                cols.add(col);
            }
        }

        return cols;
    }

    /**
     * Get data columns by names
     *
     * @param colNames Data column names
     * @return Data columns
     */
    public List<DataColumn> findColumns(List<String> colNames) {
        List<DataColumn> cols = new ArrayList<>();
        for (DataColumn col : dataTable.getColumns()) {
            for (String colName : colNames) {
                if (col.getColumnName().equals(colName)) {
                    cols.add(col);
                    break;
                }
            }
        }

        return cols;
    }

    /**
     * Get column values
     *
     * @param col The data column
     * @return Value list
     */
    public List<Double> getValidColumnValues(DataColumn col) {
        return this.getValidColumnValues(dataTable.getRows(), col);
    }

    /**
     * Get column values
     *
     * @param rows The data row list
     * @param col The data column
     * @return Column values
     */
    public List<Double> getValidColumnValues(List<DataRow> rows, DataColumn col) {
        List<Double> values = new ArrayList<>();
        String colName = col.getColumnName();
        String vstr;
        double value = Double.NaN;
        for (DataRow row : rows) {
            if (row.getValue(colName) == null) {
                continue;
            }
            switch (col.getDataType()) {
                case Integer:
                    value = (double) (Integer) row.getValue(colName);
                    break;
                case Float:
                    value = (double) (Float) row.getValue(colName);
                    break;
                case Double:
                    value = (Double) row.getValue(colName);
                    break;
                case String:
                    vstr = (String) row.getValue(colName);
                    if (!vstr.isEmpty()) {
                        value = Double.parseDouble(vstr);
                    } else {
                        value = Double.NaN;
                    }
                    break;
            }
            if (!Double.isNaN(value) && !MIMath.doubleEquals(value, this.missingValue)) {
                values.add(value);
            }
        }

        return values;
    }
    
    /**
     * Get the value by row index and column name
     *
     * @param row Row index
     * @param colName Column name
     * @return Object The value
     */
    public Object getValue(int row, String colName) {
        return this.dataTable.getValue(row, colName);
    }

    /**
     * Get the value by row and column index
     *
     * @param row Row index
     * @param col Column index
     * @return Object The value
     */
    public Object getValue(int row, int col) {
        return this.dataTable.getValue(row, col);
    }
    
    /**
     * Set a vlaue by row and column index
     *
     * @param row Row index
     * @param col Column index
     * @param value The value
     */
    public void setValue(int row, int col, Object value) {
        this.dataTable.setValue(row, col, value);
    }

    /**
     * Set a value
     *
     * @param row Row index
     * @param colName Column name
     * @param value The value
     */
    public void setValue(int row, String colName, Object value) {
        this.dataTable.setValue(row, colName, value);
    }

    /**
     * Get column data
     *
     * @param colName The column name
     * @return Column data
     */
    public ColumnData getColumnData(String colName) {
        return dataTable.getColumnData(colName);
    }

    /**
     * Get column data
     *
     * @param col The data column
     * @return Column data
     */
    public ColumnData getColumnData(DataColumn col) {
        return dataTable.getColumnData(col);
    }

    /**
     * Get column data
     *
     * @param rows The data row list
     * @param col The data column
     * @return Column values
     */
    public ColumnData getColumnData(List<DataRow> rows, DataColumn col) {
        return dataTable.getColumnData(rows, null);
    }

    /**
     * Set column data
     *
     * @param colName Column name
     * @param values Values
     */
    public void setColumnData(String colName, List<Object> values) {
        this.dataTable.setValues(colName, values);
    }

    /**
     * Convert a data column to double data type
     *
     * @param colName The data column name
     */
    public void columnToDouble(String colName) {
        DataColumn col = dataTable.findColumn(colName);
        DataTypes oldType = col.getDataType();
        col.setDataType(DataTypes.Double);
        Object value;
        for (DataRow row : dataTable.getRows()) {
            value = row.getValue(colName);
            switch (oldType) {
                case Integer:
                    row.setValue(col, (double) (Integer) value);
                    break;
                case Float:
                    row.setValue(col, (double) (Float) value);
                    break;
                case String:
                    if (MIMath.isNumeric((String) value)) {
                        row.setValue(col, Double.parseDouble((String) value));
                    } else {
                        row.setValue(col, Double.NaN);
                    }
                    break;
            }
        }
    }

    /**
     * Average data
     *
     * @param cols The data columns
     * @return Result data table
     * @throws Exception
     */
    public DataTable average(List<DataColumn> cols) throws Exception {
        DataTable rTable = new DataTable();
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }
        DataRow nRow = rTable.addRow();

        for (DataColumn col : cols) {
            List<Double> values = this.getValidColumnValues(col);
            double mean = Statistics.mean(values);
            nRow.setValue(col, mean);
        }

        return rTable;
    }

    /**
     * Average data and calculate standard deviation
     *
     * @param cols The data columns
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_stdev(List<DataColumn> cols) throws Exception {
        DataTable rTable = new DataTable();
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
            rTable.addColumn(col.getColumnName() + "_sd", DataTypes.Double);
        }
        DataRow nRow = rTable.addRow();

        for (DataColumn col : cols) {
            List<Double> values = this.getValidColumnValues(col);
            double mean = Statistics.mean(values);
            double stdev = Statistics.standardDeviation(values);
            nRow.setValue(col.getColumnName(), mean);
            nRow.setValue(col.getColumnName() + "_sd", stdev);
        }

        return rTable;
    }

    /**
     * Get average data table
     *
     * @param dataColumns
     * @return Average data table
     * @throws java.lang.Exception
     */
    public DataTable statistics(List<DataColumn> dataColumns) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("Type", DataTypes.String);
        rTable.addColumn("Mean", DataTypes.Double);
        rTable.addColumn("Minimum", DataTypes.Double);
        rTable.addColumn("Q1", DataTypes.Double);
        rTable.addColumn("Meadian", DataTypes.Double);
        rTable.addColumn("Q3", DataTypes.Double);
        rTable.addColumn("Maximum", DataTypes.Double);
        rTable.addColumn("StdDev", DataTypes.Double);
        rTable.addColumn("Count", DataTypes.Integer);

        int i = 0;
        for (DataColumn col : dataColumns) {
            List<Double> values = this.getValidColumnValues(col);
            double mean = Statistics.mean(values);
            double min = Statistics.minimum(values);
            double q1 = Statistics.quantile(values, 1);
            double meadian = Statistics.median(values);
            double q3 = Statistics.quantile(values, 3);
            double max = Statistics.maximum(values);
            double sd = Statistics.standardDeviation(values);
            int n = values.size();

            rTable.addRow();
            rTable.setValue(i, 0, col.getColumnName());
            rTable.setValue(i, 1, mean);
            rTable.setValue(i, 2, min);
            rTable.setValue(i, 3, q1);
            rTable.setValue(i, 4, meadian);
            rTable.setValue(i, 5, q3);
            rTable.setValue(i, 6, max);
            rTable.setValue(i, 7, sd);
            rTable.setValue(i, 8, n);

            i++;
        }

        return rTable;
    }

    /**
     * Get average data table
     *
     * @return Average data table
     * @throws java.lang.Exception
     */
    public DataTable statistics() throws Exception {
        return this.statistics(this.getDataColumns());
    }

    /**
     * Read data table from ASCII file
     *
     * @param fileName File name
     * @param formatSpec Format specifiers string
     * @throws java.io.FileNotFoundException
     */
    public void readASCIIFile(String fileName, String formatSpec) throws FileNotFoundException, IOException, Exception {
        BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
        String title = sr.readLine().trim();
        //Determine separator
        String delimiter = GlobalUtil.getDelimiter(title);
        sr.close();
        this.readASCIIFile(fileName, delimiter, 0, formatSpec, "UTF8");
    }

    /**
     * Read data table from ASCII file
     *
     * @param fileName File name
     * @param delimiter Delimiter
     * @param headerLines Number of lines to skip at begining of the file
     * @param formatSpec Format specifiers string
     * @param encoding Fle encoding
     * @throws java.io.FileNotFoundException
     */
    public void readASCIIFile(String fileName, String delimiter, int headerLines, String formatSpec, String encoding) throws FileNotFoundException, IOException, Exception {
        DataTable dTable = new DataTable();

        BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), encoding));
        if (headerLines > 0) {
            for (int i = 0; i < headerLines; i++) {
                sr.readLine();
            }
        }

        String title = sr.readLine().trim();
        String[] titleArray = GlobalUtil.split(title, delimiter);
        int colNum = titleArray.length;
        if (titleArray.length < 2) {
            System.out.println("File Format Error!");
            sr.close();
        } else {
            //Get fields
            String[] colFormats;
            if (formatSpec == null) {
                colFormats = new String[colNum];
                for (int i = 0; i < colNum; i++) {
                    colFormats[i] = "C";
                }
            } else {
                colFormats = formatSpec.split("%");
            }

            int idx = 0;
            for (String colFormat : colFormats) {
                if (colFormat.isEmpty()) {
                    continue;
                }

                switch (colFormat) {
                    case "C":
                    case "s":
                        dTable.addColumn(titleArray[idx], DataTypes.String);
                        break;
                    case "i":
                        dTable.addColumn(titleArray[idx], DataTypes.Integer);
                        break;
                    case "f":
                        dTable.addColumn(titleArray[idx], DataTypes.Float);
                        break;
                    case "d":
                        dTable.addColumn(titleArray[idx], DataTypes.Double);
                        break;
                    case "B":
                        dTable.addColumn(titleArray[idx], DataTypes.Boolean);
                        break;
                    default:
                        if (colFormat.substring(0, 1).equals("{")) {    //Date
                            int eidx = colFormat.indexOf("}");
                            String formatStr = colFormat.substring(1, eidx);
                            dTable.addColumn(new DataColumn(titleArray[idx], DataTypes.Date, formatStr));
                        } else {
                            dTable.addColumn(titleArray[idx], DataTypes.String);
                        }
                        break;
                }

                idx += 1;
            }

            String[] dataArray;
            int rn = 0;
            String line = sr.readLine();
            while (line != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    line = sr.readLine();
                    continue;
                }
                dataArray = GlobalUtil.split(line, delimiter);

                dTable.addRow();
                int cn = 0;
                for (int i = 0; i < dataArray.length; i++) {
                    dTable.setValue(rn, cn, dataArray[i]);
                    cn++;
                }

                rn += 1;
                line = sr.readLine();
            }

            dataTable = dTable;
            sr.close();
        }
    }

    /**
     * Read data table from ASCII file
     *
     * @param fileName File name
     * @param dataColumns Data columns
     * @throws java.io.FileNotFoundException
     */
    public void readASCIIFile(String fileName, List<DataColumn> dataColumns) throws FileNotFoundException, IOException, Exception {
        DataTable dTable = new DataTable();

        BufferedReader sr = new BufferedReader(new FileReader(new File(fileName)));
        String title = sr.readLine().trim();
        //Determine separator
        String separator = GlobalUtil.getDelimiter(title);
        String[] titleArray = GlobalUtil.split(title, separator);
        if (titleArray.length < 2) {
            JOptionPane.showMessageDialog(null, "File Format Error!");
            sr.close();
        } else {
            //Get fields
            for (DataColumn col : dataColumns) {
                dTable.addColumn(col);
            }
            List<Integer> dataIdxs = new ArrayList<>();
            String fieldName;
            for (int i = 0; i < titleArray.length; i++) {
                fieldName = titleArray[i];
                for (DataColumn col : dataColumns) {
                    if (fieldName.equals(col.getColumnName())) {
                        dataIdxs.add(i);
                    }
                }
            }

            String[] dataArray;
            int rn = 0;
            String line = sr.readLine();
            while (line != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                dataArray = GlobalUtil.split(line, separator);
                dTable.addRow();
                int cn = 0;
                for (int idx : dataIdxs) {
                    dTable.setValue(rn, cn, dataArray[idx]);
                    cn++;
                }

                rn += 1;
                line = sr.readLine();
            }

            dataTable = dTable;
            sr.close();
        }
    }

    /**
     * Read data table from ASCII file
     *
     * @param fileName File name
     * @throws java.io.FileNotFoundException
     */
    public void readASCIIFile(String fileName) throws FileNotFoundException, IOException, Exception {
        DataTable dTable = new DataTable();

        BufferedReader sr = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "utf-8"));
        String title = sr.readLine().trim();
        //Determine separator
        String separator = GlobalUtil.getDelimiter(title);
        String[] titleArray = GlobalUtil.split(title, separator);
        if (titleArray.length < 2) {
            JOptionPane.showMessageDialog(null, "File Format Error!");
            sr.close();
        } else {
            //Get fields
            List<Integer> dataIdxs = new ArrayList<>();
            String fieldName;
            for (int i = 0; i < titleArray.length; i++) {
                fieldName = titleArray[i];
                dTable.addColumn(fieldName, DataTypes.String);
                dataIdxs.add(i);
            }

            String[] dataArray;
            int rn = 0;
            String line = sr.readLine();
            while (line != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                dataArray = GlobalUtil.split(line, separator);
                dTable.addRow();
                int cn = 0;
                for (int idx : dataIdxs) {
                    dTable.setValue(rn, cn, dataArray[idx]);
                    cn++;
                }

                rn += 1;
                line = sr.readLine();
            }

            dataTable = dTable;
            sr.close();
        }
    }

    /**
     * Convert a multi rows data table to single row data table
     *
     * @param inTable Input data table - multi rows
     * @param firstColName The new first column name
     * @param firstColValue The new first column value
     * @return Result data table
     * @throws Exception
     */
    public DataTable toSingleRowTable(DataTable inTable, String firstColName, String firstColValue) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn(firstColName, DataTypes.String);
        List<Object> values = new ArrayList<>();

        if (inTable.getColumnCount() == 2) {
            DataColumn col = inTable.getColumns().get(1);
            String colName = col.getColumnName();
            int r = 0;
            for (DataRow row : inTable.getRows()) {
                String rowName = row.getValue(0).toString();
                DataColumn newCol = new DataColumn(rowName, col.getDataType());
                rTable.addColumn(newCol);
                values.add(inTable.getValue(r, colName));
                r++;
            }
        } else {
            int i = 0;
            for (DataColumn col : inTable.getColumns()) {
                if (i > 0) {
                    String colName = col.getColumnName();
                    int r = 0;
                    for (DataRow row : inTable.getRows()) {
                        String rowName = row.getValue(0).toString();
                        DataColumn newCol = new DataColumn(rowName + "_" + colName, col.getDataType());
                        rTable.addColumn(newCol);
                        values.add(inTable.getValue(r, colName));
                        r++;
                    }
                }
                i++;
            }
        }

        rTable.addRow();
        rTable.setValue(0, 0, firstColValue);
        int i = 1;
        for (Object value : values) {
            rTable.setValue(0, i, value);
            i++;
        }

        return rTable;
    }
    
    /**
     * Convert a multi rows data table to single row data table
     *
     * @param inTable Input data table - multi rows
     * @return Result data table
     * @throws Exception
     */
    public DataTable toSingleRowTable(DataTable inTable) throws Exception {
        DataTable rTable = new DataTable();
        List<Object> values = new ArrayList<>();

        if (inTable.getColumnCount() == 2) {
            DataColumn col = inTable.getColumns().get(1);
            String colName = col.getColumnName();
            int r = 0;
            for (DataRow row : inTable.getRows()) {
                String rowName = row.getValue(0).toString();
                DataColumn newCol = new DataColumn(rowName, col.getDataType());
                rTable.addColumn(newCol);
                values.add(inTable.getValue(r, colName));
                r++;
            }
        } else {
            int i = 0;
            for (DataColumn col : inTable.getColumns()) {
                if (i > 0) {
                    String colName = col.getColumnName();
                    int r = 0;
                    for (DataRow row : inTable.getRows()) {
                        String rowName = row.getValue(0).toString();
                        DataColumn newCol = new DataColumn(rowName + "_" + colName, col.getDataType());
                        rTable.addColumn(newCol);
                        values.add(inTable.getValue(r, colName));
                        r++;
                    }
                }
                i++;
            }
        }

        rTable.addRow();
        int i = 0;
        for (Object value : values) {
            rTable.setValue(0, i, value);
            i++;
        }

        return rTable;
    }

    /**
     * Save as csv file
     *
     * @param fileName File name
     * @throws java.io.IOException
     */
    public void saveAsCSVFile(String fileName) throws IOException {
        this.dataTable.saveAsCSVFile(fileName);
    }

    /**
     * Save as ASCII file
     *
     * @param fileName File name
     * @throws java.io.IOException
     */
    public void saveAsASCIIFile(String fileName) throws IOException {
        this.dataTable.saveAsASCIIFile(fileName);
    }

    /**
     * Join data table
     *
     * @param tableData The input table data
     * @param colName The column name for join
     */
    public void join(TableData tableData, String colName) {
        this.dataTable.join(tableData.dataTable, colName, colName, false);
    }

    /**
     * Join data table
     *
     * @param tableData The input table data
     * @param thisColName The column name for join
     * @param otherColName The other column name for join
     */
    public void join(TableData tableData, String thisColName, String otherColName) {
        this.dataTable.join(tableData.dataTable, thisColName, otherColName, false);
    }

    /**
     * Clone
     *
     * @return Cloned TableData object
     */
    @Override
    public Object clone() {
        TableData td = new TableData();
        td.dataTable = (DataTable) this.dataTable.clone();
        td.missingValue = this.missingValue;

        return td;
    }
    // </editor-fold>

    // <editor-fold desc="Time">
    /**
     * Get years
     *
     * @param tColName Time column name
     * @return Year list
     */
    public List<Integer> getYears(String tColName) {
        List<Integer> years = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int year;
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            year = cal.get(Calendar.YEAR);
            if (!years.contains(year)) {
                years.add(year);
            }
        }

        return years;
    }

    /**
     * Get year months
     *
     * @param tColName Time column name
     * @return Year month list
     */
    public List<String> getYearMonths(String tColName) {
        List<String> yms = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
        String ym;
        for (DataRow row : dataTable.getRows()) {
            ym = format.format((Date) row.getValue(tColName));
            if (!yms.contains(ym)) {
                yms.add(ym);
            }
        }

        return yms;
    }

    /**
     * Get data row list by year
     *
     * @param year The year
     * @param tColName Time column name
     * @return Data row list
     */
    public List<DataRow> getDataByYear(int year, String tColName) {
        List<DataRow> rows = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            if (cal.get(Calendar.YEAR) == year) {
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Get data row list by year
     *
     * @param season The season
     * @param tColName Time column name
     * @return Data row list
     */
    public List<DataRow> getDataBySeason(String season, String tColName) {
        List<Integer> months = this.getMonthsBySeason(season);
        List<DataRow> rows = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        int month;
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            month = cal.get(Calendar.MONTH) + 1;
            if (months.contains(month)) {
                rows.add(row);
            }
        }

        return rows;
    }

    private List<Integer> getMonthsBySeason(String season) {
        List<Integer> months = new ArrayList<>();
        if (season.equalsIgnoreCase("spring")) {
            months.add(3);
            months.add(4);
            months.add(5);
        } else if (season.equalsIgnoreCase("summer")) {
            months.add(6);
            months.add(7);
            months.add(8);
        } else if (season.equalsIgnoreCase("autumn")) {
            months.add(9);
            months.add(10);
            months.add(11);
        } else if (season.equalsIgnoreCase("winter")) {
            months.add(12);
            months.add(1);
            months.add(2);
        }

        return months;
    }

    /**
     * Get data row list by year and month
     *
     * @param yearMonth The year and month
     * @param tColName Time column name
     * @return Data row list
     */
    public List<DataRow> getDataByYearMonth(String yearMonth, String tColName) {
        int year = Integer.parseInt(yearMonth.substring(0, 4));
        int month = Integer.parseInt(yearMonth.substring(4));
        return this.getDataByYearMonth(year, month, tColName);
    }

    /**
     * Get data row list by year and month
     *
     * @param year The year
     * @param month The month
     * @param tColName Time column name
     * @return Data row list
     */
    public List<DataRow> getDataByYearMonth(int year, int month, String tColName) {
        List<DataRow> rows = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            if (cal.get(Calendar.YEAR) == year) {
                if (cal.get(Calendar.MONTH) == month - 1) {
                    rows.add(row);
                }
            }
        }

        return rows;
    }

    /**
     * Get data row list by month
     *
     * @param month The month
     * @param tColName Time column name
     * @return Data row list
     */
    public List<DataRow> getDataByMonth(int month, String tColName) {
        List<DataRow> rows = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            if (cal.get(Calendar.MONTH) == month - 1) {
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Get data row list by day of week
     *
     * @param dow Day of week
     * @param tColName Time column name
     * @return Data row list
     */
    public List<DataRow> getDataByDayOfWeek(int dow, String tColName) {
        dow = dow + 1;
        if (dow == 8) {
            dow = 1;
        }

        List<DataRow> rows = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            if (cal.get(Calendar.DAY_OF_WEEK) == dow) {
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Get data row list by hour
     *
     * @param hour The hour
     * @param tColName Time column name
     * @return Result data row list
     */
    public List<DataRow> getDataByHour(int hour, String tColName) {
        List<DataRow> rows = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (DataRow row : dataTable.getRows()) {
            cal.setTime((Date) row.getValue(tColName));
            if (cal.get(Calendar.HOUR_OF_DAY) == hour) {
                rows.add(row);
            }
        }

        return rows;
    }

    /**
     * Average year by year
     *
     * @param cols The data columns
     * @param tColName The time column name
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_Year(List<DataColumn> cols, String tColName) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("Year", DataTypes.Integer);
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }

        List<Integer> years = this.getYears(tColName);
        for (int year : years) {
            DataRow nRow = rTable.addRow();
            nRow.setValue(0, year);
            List<DataRow> rows = this.getDataByYear(year, tColName);
            for (DataColumn col : cols) {
                List<Double> values = this.getValidColumnValues(rows, col);
                nRow.setValue(col.getColumnName(), Statistics.mean(values));
            }
        }

        return rTable;
    }

    /**
     * Average month by month
     *
     * @param cols The data columns
     * @param tColName Time column name
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_Month(List<DataColumn> cols, String tColName) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("YearMonth", DataTypes.String);
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }

        List<String> yms = this.getYearMonths(tColName);
        for (String ym : yms) {
            DataRow nRow = rTable.addRow();
            nRow.setValue(0, ym);
            List<DataRow> rows = this.getDataByYearMonth(ym, tColName);
            for (DataColumn col : cols) {
                List<Double> values = this.getValidColumnValues(rows, col);
                nRow.setValue(col.getColumnName(), Statistics.mean(values));
            }
        }

        return rTable;
    }

    /**
     * Average monthly
     *
     * @param cols The data columns
     * @param tColName Time column name
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_MonthOfYear(List<DataColumn> cols, String tColName) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("Month", DataTypes.String);
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }

        List<String> monthNames = Arrays.asList(new String[]{"Jan", "Feb", "Mar", "Apr", "May",
            "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"});
        List<Integer> months = new ArrayList<>();
        int i;
        for (i = 1; i < 13; i++) {
            months.add(i);
        }

        i = 0;
        for (int month : months) {
            DataRow nRow = rTable.addRow();
            nRow.setValue(0, monthNames.get(i));
            List<DataRow> rows = this.getDataByMonth(month, tColName);
            for (DataColumn col : cols) {
                List<Double> values = this.getValidColumnValues(rows, col);
                nRow.setValue(col.getColumnName(), Statistics.mean(values));
            }
            i++;
        }

        return rTable;
    }

    /**
     * Average seasonal
     *
     * @param cols The data columns
     * @param tColName Time column name
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_SeasonOfYear(List<DataColumn> cols, String tColName) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("Season", DataTypes.String);
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }

        List<String> seasons = Arrays.asList(new String[]{"Spring", "Summer", "Autumn", "Winter"});
        for (String season : seasons) {
            DataRow nRow = rTable.addRow();
            nRow.setValue(0, season);
            List<DataRow> rows = this.getDataBySeason(season, tColName);
            for (DataColumn col : cols) {
                List<Double> values = this.getValidColumnValues(rows, col);
                nRow.setValue(col.getColumnName(), Statistics.mean(values));
            }
        }

        return rTable;
    }

    /**
     * Average by day of week
     *
     * @param cols The data columns
     * @param tColName Time column name
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_DayOfWeek(List<DataColumn> cols, String tColName) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("Day", DataTypes.String);
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }

        List<String> dowNames = Arrays.asList(new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
            "Saturday"});
        List<Integer> dows = new ArrayList<>();
        dows.add(7);
        int i;
        for (i = 1; i < 7; i++) {
            dows.add(i);
        }

        i = 0;
        for (int dow : dows) {
            DataRow nRow = rTable.addRow();
            nRow.setValue(0, dowNames.get(i));
            List<DataRow> rows = this.getDataByDayOfWeek(dow, tColName);
            for (DataColumn col : cols) {
                List<Double> values = this.getValidColumnValues(rows, col);
                nRow.setValue(col.getColumnName(), Statistics.mean(values));
            }
            i++;
        }

        return rTable;
    }

    /**
     * Average by hour of day
     *
     * @param cols The data columns
     * @param tColName Time column name
     * @return Result data table
     * @throws Exception
     */
    public DataTable ave_HourOfDay(List<DataColumn> cols, String tColName) throws Exception {
        DataTable rTable = new DataTable();
        rTable.addColumn("Hour", DataTypes.Integer);
        for (DataColumn col : cols) {
            rTable.addColumn(col.getColumnName(), DataTypes.Double);
        }

        List<Integer> hours = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            hours.add(i);
        }

        for (int hour : hours) {
            DataRow nRow = rTable.addRow();
            nRow.setValue(0, hour);
            List<DataRow> rows = this.getDataByHour(hour, tColName);
            for (DataColumn col : cols) {
                List<Double> values = this.getValidColumnValues(rows, col);
                nRow.setValue(col.getColumnName(), Statistics.mean(values));
            }
        }

        return rTable;
    }
    
    /**
     * SQL select
     * @param expression SQL expression
     * @return Result TableData
     */
    public TableData sqlSelect(String expression){
        DataTable r = this.dataTable.sqlSelect(expression);
        return new TableData(r);
    }

    @Override
    public String toString() {
        return this.dataTable.toString();
    }
    // </editor-fold>

    // </editor-fold>
}
