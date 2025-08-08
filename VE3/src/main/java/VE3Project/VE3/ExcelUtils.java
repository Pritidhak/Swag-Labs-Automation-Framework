package VE3Project.VE3;



import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;

public class ExcelUtils {
    private Workbook workbook;
    private Sheet sheet;

    public ExcelUtils(String filePath, String sheetName) throws Exception {
        FileInputStream fis = new FileInputStream(filePath);
        workbook = new XSSFWorkbook(fis);
        sheet = workbook.getSheet(sheetName);
    }

    public String getCellData(int row, int col) {
        return sheet.getRow(row).getCell(col).getStringCellValue();
    }

    public int getRowCount() {
        return sheet.getPhysicalNumberOfRows();
    }
}

