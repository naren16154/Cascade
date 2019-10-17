package execution.cascade;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {
	
	static Map<String, Map<String, List<String>>> workbookData = new LinkedHashMap<String, Map<String,List<String>>>();
	static Map<String, String> testSet = new HashMap<String, String>();
	static Map<String, String> setProduct = new HashMap<String, String>();
	
	public void getDataFromExcel(String excelPath)
	{
		FileInputStream fs=null;
		XSSFWorkbook wb=null;
		try {
			fs = new FileInputStream(excelPath);
			wb = new XSSFWorkbook(fs);
			XSSFSheet ws ;
			int noOfSheets = wb.getNumberOfSheets();
			for(int s=0;s<noOfSheets;s++)
			{
				Map<String, List<String>> setData = new LinkedHashMap<String,List<String>>();
				ws = wb.getSheetAt(s);
				int rownum = ws.getPhysicalNumberOfRows();
				String cellValue = null;
				String setValue = null;
				List<String> testCaseData= null;
				for (int i = 1; i < rownum; i++) {
					if(null != cellToString(ws.getRow(i).getCell(0)))
					{
						if(i>1)
							setData.put(setValue, testCaseData);
						setValue=cellToString(ws.getRow(i).getCell(0));
						setProduct.put(setValue, ws.getSheetName());
						testCaseData = new LinkedList<String>();
					}
					cellValue = cellToString(ws.getRow(i).getCell(1));
					testCaseData.add(cellValue);
					testSet.put(cellValue, setValue);
				}
				setData.put(setValue, testCaseData);
				workbookData.put(ws.getSheetName(), setData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				wb.close();
				fs.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	private static String cellToString(XSSFCell cell) {
		try{
			int type = cell.getCellType();
			Object result;
			switch (type) {
			case 0:
				result = (long)cell.getNumericCellValue();
				break;
			case 1:
				result = cell.getStringCellValue();
				break;
			case 2:
				result = cell.getBooleanCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK :
				result = null;
				break;
			default:
					result = cell.getRichStringCellValue();
				break;
			}
			return result.toString().trim();
		}catch(NullPointerException e){
			return null;
		}
	}

}
