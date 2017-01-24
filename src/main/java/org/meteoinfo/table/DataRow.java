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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 *
 * @author Yaqiang Wang
 */
public class DataRow {

    private int rowIndex = -1;
    private DataColumnCollection columns;
    private DataTable table;
    private final Map<String, Object> itemMap = new LinkedHashMap<>();

    /**
     * Constructor
     */
    public DataRow() {
    }

    /**
     * Constructor
     *
     * @param table The data table
     */
    public DataRow(DataTable table) {
        this.table = table;
    }

    /**
     * Get row index
     *
     * @return int Row index
     */
    public int getRowIndex() {
        return rowIndex;
    }

    /**
     * Get data table
     *
     * @return DataTable The data table
     */
    public DataTable getTable() {
        return this.table;
    }
    
    /**
     * Set data table
     * @param value Data table
     */
    public void setTable(DataTable value){
        this.table = value;
    }

    /**
     * Set columns
     *
     * @param columns Columns
     */
    public void setColumns(DataColumnCollection columns) {
        this.columns = columns;        
    }

    /**
     * Get columns
     *
     * @return the columns
     */
    public DataColumnCollection getColumns() {
        return columns;
    }

    /**
     * Set a value
     *
     * @param index Column index
     * @param value The value
     */
    public void setValue(int index, Object value) {
        setValue(this.columns.get(index), value);
    }

    /**
     * Set a value
     *
     * @param columnName Column name
     * @param value The value
     */
    public void setValue(String columnName, Object value) {
        setValue(this.columns.get(columnName), value);
    }

    /**
     * Set a vlaue
     *
     * @param column The data column
     * @param value The value
     */
    public void setValue(DataColumn column, Object value) {
        if (column != null) {
            String lowerColumnName = column.getColumnName().toLowerCase();
            if (itemMap.containsKey(lowerColumnName)) {
                itemMap.remove(lowerColumnName);
            }
            itemMap.put(lowerColumnName, column.convertTo(value));
        }
    }

    /**
     * Add column
     *
     * @param column The column
     */
    public void addColumn(DataColumn column) {
        Object value = null;
        switch (column.getDataType()) {
            case Integer:
                value = 0;
                break;
            case String:
                value = "";
                break;
            case Float:
                value = 0.0f;
                break;
            case Double:
                value = 0.0d;
                break;
        }
        this.setValue(column, value);
    }

    /**
     * Remove a data column
     *
     * @param column The data column
     */
    public void removeColumn(DataColumn column) {
        if (column != null) {
            String lowerColumnName = column.getColumnName().toLowerCase();
            if (itemMap.containsKey(lowerColumnName)) {
                itemMap.remove(lowerColumnName);
            }
        }
    }

    /**
     * Rename column
     *
     * @param oldName The old name
     * @param name The new name
     */
    public void renameColumn(String oldName, String name) {
        oldName = oldName.toLowerCase();
        if (itemMap.containsKey(oldName)) {
            Object value = itemMap.get(oldName);
            this.setValue(name, value);
            itemMap.remove(oldName);
        }
    }

    /**
     * Get value
     *
     * @param index Column index
     * @return The value
     */
    public Object getValue(int index) {
        String colName = this.columns.get(index).getColumnName().toLowerCase();
        return this.getItemMap().get(colName);
    }

    /**
     * Get value
     *
     * @param columnName Column name
     * @return The value
     */
    public Object getValue(String columnName) {
        return this.getItemMap().get(columnName.toLowerCase());
    }

    /**
     * Get item map
     *
     * @return The item map
     */
    public Map<String, Object> getItemMap() {
        return itemMap;
    }

    /**
     * Set row index
     *
     * @param rowIndex Row index
     */
    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    /**
     * Copy from a data row
     *
     * @param row The data row
     */
    public void copyFrom(DataRow row) {
        this.itemMap.clear();
        for (Object c : this.columns) {
            this.itemMap.put(c.toString().toLowerCase(), row.getValue(c.toString()));
        }
    }

    /**
     * Clone
     *
     * @return Cloned DataRow object
     */
    @Override
    public Object clone() {
        DataRow row = new DataRow();
        row.setColumns(columns);
        for (Object c : this.columns) {
            row.itemMap.put(c.toString().toLowerCase(), this.getValue(c.toString()));
            
        }

        return row;
    }
}
