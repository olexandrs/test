package com.company;

import java.io.FileOutputStream;
import java.io.IOException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFTable;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumn;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTTableColumns;

/**
 * Created by Olexandr_Sulima on 5/15/2015.
 */
public class Table {

    public void createTable() throws  IOException {
        Workbook wb = new XSSFWorkbook();
        XSSFSheet sheet = (XSSFSheet) wb.createSheet();

        //Create
        XSSFTable table = sheet.createTable();
        table.setDisplayName("Test");
        CTTable cttable = table.getCTTable();


        //Set which area the table should be placed in
        AreaReference reference = new AreaReference(new CellReference(0, 0),
                new CellReference(2,2));
        cttable.setRef(reference.formatAsString());
        cttable.setId(1);
        cttable.setName("Test");
        cttable.setTotalsRowCount(1);

        CTTableColumns columns = cttable.addNewTableColumns();
        columns.setCount(3);
        CTTableColumn column;
        XSSFRow row;
        XSSFCell cell;
        for(int i=0; i<3; i++) {
            //Create column
            column = columns.addNewTableColumn();
            column.setName("Column");
            column.setId(i+1);
            //Create row
            row = sheet.createRow(i);
            for(int j=0; j<3; j++) {
                //Create cell
                cell = row.createCell(j);
                if(i == 0) {
                    cell.setCellValue("Column"+j);
                } else {
                    cell.setCellValue(j);
                }
            }
        }

        FileOutputStream fileOut = new FileOutputStream("ooxml-table.xlsx");
        wb.write(fileOut);
        fileOut.close();
    }
}
