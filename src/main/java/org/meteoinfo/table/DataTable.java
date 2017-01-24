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
package org.meteoinfo.table;

import org.meteoinfo.data.DataTypes;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.meteoinfo.data.TableUtil;
import org.meteoinfo.data.mapdata.Field;
import org.meteoinfo.global.MIMath;

/**
 *
 * @author Yaqiang Wang
 */
public final class DataTable {

    private DataRowCollection rows;
    private DataColumnCollection columns;
    private String tableName;
    private boolean readOnly = false;
    private int nextRowIndex = 0;
//private DataExpression dataExpression;
    private Object tag;

    /**
     * Constructor
     */
    public DataTable() {
        this.columns = new DataColumnCollection();
        this.rows = new DataRowCollection();
        this.rows.setColumns(columns);
        //dataExpression = new DataExpression(this);
    }

    /**
     * Constructor
     *
     * @param dataTableName The data table name
     */
    public DataTable(String dataTableName) {
        this();
        this.tableName = dataTableName;
    }

    /**
     * Get total row count
     *
     * @return Row number
     */
    public int getTotalCount() {
        return rows.size();
    }

    /**
     * Get row count
     *
     * @return Row count
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Get column count
     *
     * @return Column count
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Get if is read only
     *
     * @return Boolean
     */
    public boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * Set if is read only
     *
     * @param readOnly Read only
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    /**
     * Get table name
     *
     * @return Table name
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Set tabel name
     *
     * @param tableName Table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * Get data rows
     *
     * @return DataRowCollection The data rows
     */
    public DataRowCollection getRows() {
        return this.rows;
    }

    /**
     * Get data columns
     *
     * @return The data columns
     */
    public DataColumnCollection getColumns() {
        return this.columns;
    }

    /**
     * Get column names
     *
     * @return Column names
     */
    public List<String> getColumnNames() {
        return this.columns.getColumnNames();
    }

    /**
     * Get the value by row index and column name
     *
     * @param row Row index
     * @param colName Column name
     * @return Object The value
     */
    public Object getValue(int row, String colName) {
        return this.rows.get(row).getValue(colName);
    }

    /**
     * Get the value by row and column index
     *
     * @param row Row index
     * @param col Column index
     * @return Object The value
     */
    public Object getValue(int row, int col) {
        return this.rows.get(row).getValue(col);
    }

    /**
     * Create a new data row
     *
     * @return DataRow The new data row
     * @throws java.lang.Exception
     */
    public DataRow newRow() throws Exception {
        DataRow tempRow = new DataRow(this);
        nextRowIndex = nextRowIndex < this.rows.size() ? this.rows.size()
                : nextRowIndex;
        tempRow.setColumns(this.columns);
        tempRow.setRowIndex(nextRowIndex++);
        for (DataColumn col : columns) {
            switch (col.getDataType()) {
                case String:
                    tempRow.setValue(col.getColumnName(), "");
                    break;
                case Date:
                    tempRow.setValue(col.getColumnName(), new Date());
                    break;
                case Boolean:
                    tempRow.setValue(col.getColumnName(), Boolean.TRUE);
                    break;
                default:
                    tempRow.setValue(col.getColumnName(), 0);
                    break;
            }
        }
        return tempRow;
    }

    /**
     * Set a vlaue by row and column index
     *
     * @param row Row index
     * @param col Column index
     * @param value The value
     */
    public void setValue(int row, int col, Object value) {
        this.rows.get(row).setValue(col, value);
    }

    /**
     * Set a value
     *
     * @param row Row index
     * @param colName Column name
     * @param value The value
     */
    public void setValue(int row, String colName, Object value) {
        this.rows.get(row).setValue(colName, value);
    }

    /**
     * Set values
     *
     * @param colName Column name
     * @param values Values
     */
    public void setValues(String colName, List<Object> values) {
        for (int i = 0; i < values.size(); i++) {
            this.rows.get(i).setValue(colName, values.get(i));
        }
    }

    /**
     * Set tag
     *
     * @param tag The tag
     */
    public void setTag(Object tag) {
        this.tag = tag;
    }

    /**
     * Get tag
     *
     * @return the tag
     */
    public Object getTag() {
        return tag;
    }

    /**
     * Find column by name
     *
     * @param colName The column name
     * @return The data column
     */
    public DataColumn findColumn(String colName) {
        String name;
        for (DataColumn col : this.columns) {
            name = col.getColumnName();
            if (name.equals(colName)) {
                return col;
            }
        }

        return null;
    }

    /**
     * Add a data column
     *
     * @param columnName Data column name
     * @param dataType Data type
     * @return The data column
     * @throws Exception
     */
    public DataColumn addColumn(String columnName, DataTypes dataType) throws Exception {
        DataColumn col = new DataColumn(columnName, dataType);
        addColumn(col);
        return col;
    }

    /**
     * Add a data column
     *
     * @param index The index
     * @param columnName Data column name
     * @param dataType Data type
     * @return The data column
     * @throws Exception
     */
    public DataColumn addColumn(int index, String columnName, DataTypes dataType) throws Exception {
        DataColumn col = new DataColumn(columnName, dataType);
        addColumn(index, col);
        return col;
    }

    /**
     * Add a data column by index
     *
     * @param index The index
     * @param column Data column
     */
    public void addColumn(int index, DataColumn column) {
        this.columns.add(index, column);
        for (DataRow row : this.rows) {
            row.setColumns(columns);
            row.addColumn(column);
        }
    }

    /**
     * Add a data column
     *
     * @param column Data column
     */
    public void addColumn(DataColumn column) {
        this.columns.add(column);
        for (DataRow row : this.rows) {
            row.setColumns(columns);
            row.addColumn(column);
        }
    }

    /**
     * Remove a data column
     *
     * @param column The data column
     */
    public void removeColumn(DataColumn column) {
        this.columns.remove(column);
        for (DataRow row : this.rows) {
            row.setColumns(columns);
            row.removeColumn(column);
        }
    }

    /**
     * Rename column
     *
     * @param column The column
     * @param fieldName The new column name
     */
    public void renameColumn(DataColumn column, String fieldName) {
        String oldName = column.getColumnName();
        this.columns.renameColumn(column, fieldName);
        for (DataRow row : this.rows) {
            row.setColumns(columns);
            row.renameColumn(oldName, fieldName);
        }
    }

    /**
     * Rename column
     *
     * @param colIdx The column index
     * @param fieldName The new column name
     */
    public void renameColumn(int colIdx, String fieldName) {
        DataColumn column = this.columns.get(colIdx);
        this.renameColumn(column, fieldName);
    }

    /**
     * Add a data row
     *
     * @param row The data row
     * @return Boolean
     * @throws Exception
     */
    public boolean addRow(DataRow row) throws Exception {
        nextRowIndex = nextRowIndex < this.rows.size() ? this.rows.size()
                : nextRowIndex;
        row.setColumns(this.columns);
        row.setRowIndex(nextRowIndex++);
        row.setTable(this);
        return this.rows.add(row);
    }

    /**
     * Add data row
     *
     * @return Data row
     * @throws Exception
     */
    public DataRow addRow() throws Exception {
        DataRow row = new DataRow();
        this.addRow(row);
        return row;
    }

    /**
     * Add data rows
     *
     * @param rows Data rows
     * @throws Exception
     */
    public void addRows(List<DataRow> rows) throws Exception {
        for (DataRow row : rows) {
            addRow(row);
        }
    }

    /**
     * Append a data row
     *
     * @param row Data row
     * @return Boolean
     */
    public boolean appendRow(DataRow row) {
        List<String> colNames = row.getColumns().getColumnNames();
        nextRowIndex = nextRowIndex < this.rows.size() ? this.rows.size()
                : nextRowIndex;
        row.setColumns(this.columns);
        row.setRowIndex(nextRowIndex++);
        row.setTable(this);
        for (DataColumn col : this.columns) {
            if (!colNames.contains(col.getColumnName())) {
                row.setValue(col, null);
            }
        }
        return this.rows.add(row);
    }

    /**
     * Remove a row
     *
     * @param rowIdx Row index
     */
    public void removeRow(int rowIdx) {
        this.rows.remove(rowIdx);
    }

    /**
     * Remove a row
     *
     * @param row The row will be removed
     */
    public void removeRow(DataRow row) {
        this.rows.remove(row);
    }

    /**
     * Remove rows
     *
     * @param rows The rows will be removed
     */
    public void removeRows(List<DataRow> rows) {
        this.rows.removeAll(rows);
    }

    /**
     * Set data rows
     *
     * @param rows The data rows
     */
    public void setRows(List<DataRow> rows) {
        this.rows.clear();
        for (DataRow row : rows) {
            this.rows.add(row);
        }
    }

    /**
     * Add column data
     *
     * @param colData The column data
     * @throws Exception
     */
    public void addColumnData(ColumnData colData) throws Exception {
        DataColumn col = this.addColumn(colData.getDataColumn().getColumnName(), colData.getDataType());
        int i = 0;
        for (DataRow row : this.rows) {
            if (i < colData.size()) {
                row.setValue(col, colData.getValue(i));
            }
            i++;
        }
    }

    /**
     * Add column data
     *
     * @param colName Column name
     * @param dataType Data type
     * @param colData The column data
     * @throws Exception
     */
    public void addColumnData(String colName, DataTypes dataType, List<Object> colData) throws Exception {
        DataColumn col = this.addColumn(colName, dataType);
        int i = 0;
        for (DataRow row : this.rows) {
            if (i < colData.size()) {
                row.setValue(col, colData.get(i));
            }
            i++;
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
        this.addColumnData(colName, dataType, colData);
    }

    /**
     * Get column data
     *
     * @param colName The column name
     * @return Column data
     */
    public ColumnData getColumnData(String colName) {
        return this.getColumnData(this.getRows(), colName);
    }

    /**
     * Get column data
     *
     * @param col The data column
     * @return Column data
     */
    public ColumnData getColumnData(DataColumn col) {
        return this.getColumnData(col.getColumnName());
    }

    /**
     * Get column data
     *
     * @param rows The data row list
     * @param colName The data column name
     * @return Column values
     */
    public ColumnData getColumnData(List<DataRow> rows, String colName) {
        ColumnData colData = new ColumnData(this.findColumn(colName));
        for (DataRow row : rows) {
            colData.addData(row.getValue(colName));
        }

        return colData;
    }

    /**
     * Select data rows
     *
     * @param expression SQL expression
     * @return Selected data rows
     */
    public List<DataRow> select(String expression) {
        SQLExpression e = new SQLExpression(expression);
        List<DataRow> dataRows = new ArrayList<>();
        for (int i = 0; i < this.rows.size(); i++) {
            DataRow row = this.rows.get(i);
            row.setRowIndex(i);
            if (e.eval(row.getItemMap())) {
                dataRows.add(row);
            }
        }

        return dataRows;
    }

    /**
     * Select and form a new data table
     *
     * @param expression SQL expression
     * @param dataColumns Data columns
     * @return Selected data table
     */
    public DataTable select(String expression, DataColumn[] dataColumns) {
        DataTable result = new DataTable();
        List<DataRow> dataRows = this.select(expression);
        for (DataColumn dc : dataColumns) {
            DataColumn newDc = new DataColumn(dc.getColumnName(),
                    dc.getDataType());
            newDc.setCaptionName(dc.getCaptionName());
            result.columns.add(newDc);
        }

        for (DataRow r : dataRows) {
            try {
                DataRow newRow = result.newRow();
                newRow.copyFrom(r);
                result.addRow(newRow);
            } catch (Exception ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }
    
    /**
     * Select and form a new data table
     *
     * @param expression SQL expression
     * @return Selected data table
     */
    public DataTable sqlSelect(String expression) {
        DataTable result = new DataTable();
        List<DataRow> dataRows = this.select(expression);
        for (DataColumn dc : this.columns) {
            DataColumn newDc = new DataColumn(dc.getColumnName(),
                    dc.getDataType());
            newDc.setCaptionName(dc.getCaptionName());
            result.columns.add(newDc);
        }

        for (DataRow r : dataRows) {
            try {
                DataRow newRow = result.newRow();
                newRow.copyFrom(r);
                result.addRow(newRow);
            } catch (Exception ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return result;
    }

//      //以下为数据表扩展方法实现集合
//      /**  
//       * 功能描述：  返回符合过滤条件的数据行集合，并返回
//       * @param
//      * @return: DataTable    
//       */
//      public List<DataRow> select(String filterString) {
//          List<DataRow> rows = new ArrayList<DataRow>();
//          if (StringUtil.isNotEmpty(filterString)) {
//             for (Object row : this.rows) {
//                  DataRow currentRow = (DataRow) row;
//                  if ((Boolean) dataExpression.compute(filterString,
//                          currentRow.getItemMap())) {
//                      rows.add(currentRow);
//                  }
//              }
//              return rows;
//          } else {
//             return this.rows;
//         }
//      }
//      /**  
//       * 功能描述：  对当前表进行查询 过滤，并返回指定列集合拼装的DataTable对象
//       * @param
//       * @return: DataTable    
//       */
//      public DataTable select(String filterString,
//              String[] columns,
//              boolean distinct) throws Exception {
//         DataTable result = new DataTable();
//          List<DataRow> rows = select(filterString);
//         //构造表结构
//          for (String c : columns) {
//              DataColumn dc = this.columns.get(c);
//              DataColumn newDc = new DataColumn(dc.getColumnName(),
//                      dc.getDataType());
//              newDc.setCaptionName(dc.getCaptionName());
//              result.columns.add(newDc);
//          }
//          //填充数据
//          for (DataRow r : rows) {
//              DataRow newRow = result.newRow();
//              newRow.copyFrom(r);
//              result.addRow(newRow);
//          }
//          return result;
//      }    
//      /**  
//       * 功能描述：  根据指定表达式对符合过滤条件的数据进行计算
//       * @param
//      * @return: Object
//      * @author: James Cheung
//      * @version: 2.0 
//       */
//      public Object compute(String expression,
//              String filter) {
//          return dataExpression.compute(expression, select(filter));
//      }
    public Object max(String columns,
            String filter) {
        return null;
    }

    public Object min(String columns,
            String filter) {
        return null;
    }

    public Object avg(String columns,
            String filter) {
        return null;
    }

    public Object max(String columns,
            String filter,
            String groupBy) {
        return null;
    }

    public Object min(String columns,
            String filter,
            String groupBy) {
        return null;
    }

    public Object avg(String columns,
            String filter,
            String groupBy) {
        return null;
    }

    /**
     * Clone
     *
     * @return Cloned DataTable object
     */
    @Override
    public Object clone() {
        DataTable table = new DataTable();
        table.tableName = this.tableName;
        table.tag = this.tag;
        table.readOnly = this.readOnly;
        for (DataColumn col : this.columns) {
            DataColumn newcol = (DataColumn) col.clone();
            //newcol.setTable(table);
            table.addColumn(new Field(newcol));
        }

        for (DataRow row : this.rows) {
            try {
                DataRow newrow = this.newRow();
                newrow.copyFrom(row);
                table.addRow(newrow);
            } catch (Exception ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return table;
    }

    /**
     * Clone table - Vectorlayer with fields
     *
     * @return DataTable
     */
    public DataTable cloneTable_Field() {
        DataTable table = new DataTable();
        table.tableName = this.tableName;
        table.tag = this.tag;
        table.readOnly = this.readOnly;
        for (DataColumn col : this.columns) {
            Field newcol = new Field(col.getColumnName(), col.getDataType());
            newcol.setCaptionName(col.getCaptionName());
            newcol.setColumnIndex(col.getColumnIndex());
            newcol.setReadOnly(col.isReadOnly());
            //newcol.setTable(table);
            table.addColumn(newcol);
        }

        for (DataRow row : this.rows) {
            try {
                DataRow newrow = this.newRow();
                newrow.copyFrom(row);
                table.addRow(newrow);
            } catch (Exception ex) {
                Logger.getLogger(DataTable.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return table;
    }

    /**
     * Convert to string
     *
     * @return The string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (DataColumn col : this.columns) {
            if (sb.length() == 0) {
                sb.append(col.getColumnName());
            } else {
                sb.append("\t");
                sb.append(col.getColumnName());
            }
        }
        sb.append("\n");

        SimpleDateFormat format;
        int n = this.getRowCount();
        if (n > 100) {
            n = 100;
        }
        Object v;
        for (int r = 0; r < n; r++) {
            DataRow row = this.rows.get(r);
            int i = 0;
            for (DataColumn col : this.columns) {
                if (i > 0) {
                    sb.append("\t");
                }
                switch (col.getDataType()) {
                    case Date:
                        format = new SimpleDateFormat(col.getFormat());
                        sb.append(format.format((Date) row.getValue(col.getColumnName())));
                        break;
                    case String:
                        sb.append("'").append(row.getValue(col.getColumnName()).toString()).append("'");
                        break;
                    default:
                        v = row.getValue(col.getColumnName());
                        if (v == null)
                            sb.append("null");
                        else
                            sb.append(v.toString());
                        break;
                }
                i += 1;
            }
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Convert to string
     *
     * @param dateFormat Date format string
     * @return The string
     */
    public String toString(String dateFormat) {
        String str = "";
        for (DataColumn col : this.columns) {
            str += "," + col.getColumnName();
        }
        str = str.substring(1);
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        for (DataRow row : this.rows) {
            String line = "";
            for (DataColumn col : this.columns) {
                if (col.getDataType() == DataTypes.Date) {
                    line += "," + format.format((Date) row.getValue(col.getColumnName()));
                } else {
                    line += "," + row.getValue(col.getColumnName()).toString();
                }
            }
            line = line.substring(1);
            str += System.getProperty("line.separator") + line;
        }

        return str;
    }

    /**
     * Convert to string
     *
     * @param decimalNum Decimal number
     * @return The string
     */
    public String toString(int decimalNum) {
        return this.toString("yyyyMMddHH", decimalNum);
    }

    /**
     * Convert to string
     *
     * @param dateFormat Date format string
     * @param decimalNum Decimal number
     * @return The string
     */
    public String toString(String dateFormat, int decimalNum) {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        String dFormat = "%1$." + String.valueOf(decimalNum) + "f";
        String str = "";
        for (DataColumn col : this.columns) {
            str += "," + col.getColumnName();
        }
        str = str.substring(1);
        String vstr;
        for (DataRow row : this.rows) {
            String line = "";
            for (DataColumn col : this.columns) {
                vstr = row.getValue(col.getColumnName()).toString();
                switch (col.getDataType()) {
                    case Float:
                    case Double:
                        if (MIMath.isNumeric(vstr)) {
                            line += "," + String.format(dFormat, Double.parseDouble(vstr));
                        } else {
                            line += ",";
                        }
                        break;
                    case Date:
                        line += "," + format.format((Date) row.getValue(col.getColumnName()));
                        break;
                    default:
                        line += "," + vstr;
                        break;
                }
            }
            line = line.substring(1);
            str += System.getProperty("line.separator") + line;
        }

        return str;
    }

    /**
     * Save as csv file
     *
     * @param fileName File name
     * @throws java.io.IOException
     */
    public void saveAsCSVFile(String fileName) throws IOException {
        this.saveAsCSVFile(fileName, "yyyyMMddHH");
    }

    /**
     * Save as csv file
     *
     * @param fileName File name
     * @param dateFormat Date format string
     * @throws java.io.IOException
     */
    public void saveAsCSVFile(String fileName, String dateFormat) throws IOException {
        if (!fileName.endsWith(".csv")) {
            fileName = fileName + ".csv";
        }

        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        BufferedWriter sw = new BufferedWriter(new FileWriter(new File(fileName)));
        String str = "";
        for (DataColumn col : this.columns) {
            str += "," + col.getColumnName();
        }
        str = str.substring(1);
        sw.write(str);

        for (DataRow row : this.rows) {
            String line = "";
            for (DataColumn col : this.columns) {
                if (col.getDataType() == DataTypes.Date) {
                    line += "," + format.format((Date) row.getValue(col.getColumnName()));
                } else {
                    if (row.getValue(col.getColumnName()) == null) {
                        line += ",";
                    } else {
                        str = row.getValue(col.getColumnName()).toString();
                        if (str.equals("NaN")) {
                            str = "";
                        }
                        line += "," + str;
                    }
                }
            }
            line = line.substring(1);
            sw.newLine();
            sw.write(line);
        }
        sw.flush();
        sw.close();
    }

    /**
     * Save as ASCII file
     *
     * @param fileName File name
     * @throws java.io.IOException
     */
    public void saveAsASCIIFile(String fileName) throws IOException {
        this.saveAsASCIIFile(fileName, "yyyyMMddHH");
    }

    /**
     * Save as ASCII file
     *
     * @param fileName File name
     * @param dateFormat Date format string
     * @throws java.io.IOException
     */
    public void saveAsASCIIFile(String fileName, String dateFormat) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        BufferedWriter sw = new BufferedWriter(new FileWriter(new File(fileName)));
        String str = "";
        for (DataColumn col : this.columns) {
            str += " " + col.getColumnName();
        }
        str = str.substring(1);
        sw.write(str);

        for (DataRow row : this.rows) {
            String line = "";
            for (DataColumn col : this.columns) {
                if (col.getDataType() == DataTypes.Date) {
                    line += " " + format.format((Date) row.getValue(col.getColumnName()));
                } else {
                    line += " " + row.getValue(col.getColumnName()).toString();
                }
            }
            line = line.substring(1);
            sw.newLine();
            sw.write(line);
        }
        sw.flush();
        sw.close();
    }

    /**
     * Join data table
     *
     * @param dataTable The input data table
     * @param colName The column name for join
     */
    public void join(DataTable dataTable, String colName) {
        this.join(dataTable, colName, colName, false);
    }

    /**
     * Join data table
     *
     * @param dataTable The input data table
     * @param colName The column name for join
     * @param isUpdate If update the existing values with same column name
     */
    public void join(DataTable dataTable, String colName, boolean isUpdate) {
        this.join(dataTable, colName, colName, isUpdate);
    }

    /**
     * Join data table
     *
     * @param dataTable The input data table
     * @param colName_this The column name of this data table for join
     * @param colName_in The column name of the input data table for join
     * @param isUpdate If update the existing values with same column name
     */
    public void join(DataTable dataTable, String colName_this, String colName_in, boolean isUpdate) {
        DataColumn col_this = this.findColumn(colName_this);
        if (col_this == null) {
            System.out.println("There is no column of " + colName_this + " in this table");
            return;
        }
        DataColumn col_in = dataTable.findColumn(colName_in);
        if (col_in == null) {
            System.out.println("There is no column of " + colName_in + " in this table");
            return;
        }

        List<String> values_this = this.getColumnData(colName_this).getDataStrings();
        List<String> values_in = dataTable.getColumnData(colName_in).getDataStrings();

        List<String> colNames = this.getColumnNames();
        List<String> newColNames = new ArrayList<>();
        for (DataColumn col : dataTable.columns) {
            if (col.getColumnName().equals(colName_in)) {
                continue;
            }
            if (!colNames.contains(col.getColumnName())) {
                Field newCol = new Field(col.getColumnName(), col.getDataType());
                newCol.setJoined(true);
                this.addColumn(newCol);
                newColNames.add(col.getColumnName());
            }
        }
        String value;
        int idx;
        for (int i = 0; i < this.getRowCount(); i++) {
            value = values_this.get(i);
            idx = values_in.indexOf(value);
            if (idx >= 0) {
                if (isUpdate) {
                    for (String cn : dataTable.getColumnNames()) {
                        if (cn.equals(colName_in)) {
                            continue;
                        }
                        this.setValue(i, cn, dataTable.getValue(idx, cn));
                    }
                } else {
                    for (String cn : newColNames) {
                        this.setValue(i, cn, dataTable.getValue(idx, cn));
                    }
                }
            }
        }
    }

    /**
     * Remove joined data columns
     */
    public void removeJoin() {
        for (DataColumn col : this.columns) {
            if (col.isJoined()) {
                this.removeColumn(col);
            }
        }
    }
}
