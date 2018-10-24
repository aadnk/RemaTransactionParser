package com.comphenix.rema1000.io.excel;

import org.apache.poi.ss.usermodel.CellStyle;

public class WorkbookStyle {
    private CellStyle headerStyle;
    private CellStyle dateStyle;

    public WorkbookStyle(CellStyle headerStyle, CellStyle dateStyle) {
        this.headerStyle = headerStyle;
        this.dateStyle = dateStyle;
    }

    public CellStyle getHeaderStyle() {
        return headerStyle;
    }

    public CellStyle getDateStyle() {
        return dateStyle;
    }
}