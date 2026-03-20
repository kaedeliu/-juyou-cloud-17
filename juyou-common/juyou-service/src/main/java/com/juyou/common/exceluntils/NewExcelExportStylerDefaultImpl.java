package com.juyou.common.exceluntils;

import cn.afterturn.easypoi.excel.export.styler.AbstractExcelExportStyler;
import cn.afterturn.easypoi.excel.export.styler.IExcelExportStyler;
import org.apache.poi.ss.usermodel.*;

import static javax.swing.UIManager.getFont;


public class NewExcelExportStylerDefaultImpl extends AbstractExcelExportStyler
        implements IExcelExportStyler {
    public NewExcelExportStylerDefaultImpl(Workbook workbook) {
        super.createStyles(workbook);
    }

    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = this.workbook.createFont();
        font.setBold(true); // 字体加粗
        titleStyle.setFont(font);
        titleStyle.setAlignment(HorizontalAlignment.CENTER);//居中
        titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        titleStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());//设置颜色（黄色）
        titleStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        titleStyle.setBorderRight(BorderStyle.THIN);//设置右边框
//        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        titleStyle.setWrapText(true);


        return titleStyle;
    }



    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }

    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle titleStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontHeightInPoints((short) 12);
        titleStyle.setFont(font);
//        titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
//        titleStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        return titleStyle;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle style = workbook.createCellStyle();
//        style.setAlignment(CellStyle.ALIGN_CENTER);
//        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setDataFormat(STRING_FORMAT);
        if (isWarp) {
            style.setWrapText(true);
        }
        return style;
    }
}
