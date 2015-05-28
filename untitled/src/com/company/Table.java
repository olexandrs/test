package com.company;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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

    private XSSFSheet sheet;
    private CTTableColumns columns;
    private Workbook wb;
    private int count;

    public Table() {
        count = 0;
        wb = new XSSFWorkbook();
        sheet = (XSSFSheet) wb.createSheet();

        //Create
        XSSFTable table = sheet.createTable();
        sheet.setDefaultColumnWidth(23);
        //table.setDisplayName("Test");
        CTTable cttable = table.getCTTable();

        //Set which area the table should be placed in
        AreaReference reference = new AreaReference(new CellReference(0, 0),
                new CellReference(2,2));
        cttable.setRef(reference.formatAsString());
        cttable.setId(1);
        cttable.setName("Test");
        cttable.setTotalsRowCount(3000);

        columns = cttable.addNewTableColumns();
        columns.setCount(30);
    }

    public void fillTable(ArrayList<ArrayList<String>> result
            , ArrayList<ArrayList<Integer>> resultValue
            , ArrayList<ArrayList<Integer>> delta
            , int searchSize) throws  IOException {

        CTTableColumn column;
        XSSFRow row;
        XSSFCell cell;
        sheet.createRow(count + 1);
        count++;
        for(int i=0; i< searchSize; i++) {
            //Create column
            column = columns.addNewTableColumn();
            column.setName("Column");
            column.setId(i);
            //Create row
            row = sheet.createRow(count + 1);
            count++;
            int k = 0;
            for(int j = 0; j < result.size(); j++) {
                //Create cell
                cell = row.createCell(k);
                ArrayList<Integer> val = resultValue.get(j);
                cell.setCellValue(val.get(i) + "  C");
                k++;

                //Create cell
                cell = row.createCell(k);
                ArrayList<Integer> deltas = delta.get(j);
                cell.setCellValue(deltas.get(i) + "  delta");
                k++;

                //Create cell
                cell = row.createCell(k);
                ArrayList<String> res = result.get(j);
                cell.setCellValue(res.get(i));
                k++;
            }
        }
    }

    public void save(String name) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(name);
        wb.write(fileOut);
        fileOut.close();
    }
}
