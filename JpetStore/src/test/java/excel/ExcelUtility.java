package excel;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility 
{
    private static Workbook workbook; 
    private static Sheet sheet; 

    // Loads the Excel file into memory
    public static void loadExcelData(String filePath) 
    {
        try (FileInputStream fis = new FileInputStream(filePath)) 
        { 
            workbook = new XSSFWorkbook(fis); 
        } 
        catch (IOException e) {
            e.printStackTrace(); 
        }
    }

    // Sets the active sheet by name
    public static void setSheet(String sheetName) 
    {
        sheet = workbook.getSheet(sheetName); 
        if (sheet == null) { // If the sheet does not exist, throw an error
            throw new IllegalArgumentException("Sheet " + sheetName + " does not exist in the workbook");
        }
    }

    // Retrieves cell data as a String from the specified row and column
    public static String getCellData(int rowNum, int colNum)
    {
        if (sheet == null)  // Ensures a sheet is selected before fetching data
        {
            throw new IllegalStateException("Sheet has not been set. Call setSheet() first.");
        }
        DataFormatter formatter = new DataFormatter(); // Formats cell values to String
        try {
            return formatter.formatCellValue(sheet.getRow(rowNum).getCell(colNum)); // Retrieves and formats cell data
        }
        catch (NullPointerException e) { // Handles cases where the cell is empty or missing
            return "Cell not found";
        }
    }
}
