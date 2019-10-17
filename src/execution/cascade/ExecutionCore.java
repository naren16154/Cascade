package execution.cascade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExecutionCore extends GUI{
	
	String baseCommand = "java -jar \"%s\" -Dscript=\"%s\" -Dbrowser=\"%s\" -DconfigFile=\"%s\" -DdataFile=\"%s\" -DlogPath=\"%s\" -DinstanceId=\"%s\" -DtemplateFile=\"%s\"";
	Map<String, String> testInstance = new HashMap<String, String>();
	Map<String, String> testStatus = new HashMap<String, String>();
	Map<String, Thread> testThread = new HashMap<String, Thread>();
	String excelPath = logPath+"\\adfReports\\"+(new SimpleDateFormat("MM-dd-yyyy")).format(System.currentTimeMillis());
	String logBasePath = logPath+"\\adflogs\\"+(new SimpleDateFormat("MM-dd-yyyy")).format(System.currentTimeMillis())+"\\%s";
	String script="com.checklist.scripts.SearchScript";
	String jar="D:\\Softwares\\Checklist\\OnBoarding_Mobile\\target\\OnBoarding_Mobile-0.0.1-SNAPSHOT.jar";
	static int count = 1;
	static int randomNo;
	
	public void createAndMonitorRuns()
	{
		Random ran = new Random();
		randomNo = ran.nextInt();
		if("Parallel".equals(flowName))
		{
			createParallelRuns();
			createDashboard();
			monitorRuns();
		}
		else if("Serial".equals(flowName))
		{
			for(String test : testCaseList)
				testStatus.put(test, "Requested");
			createDashboard();
			createAndMonitorSearialRuns();
		}
	}
	
	public void createParallelRuns()
	{
		for(String test : testCaseList)
		{
			testThread.put(test, createThread(test));
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void createAndMonitorSearialRuns()
	{
		for(String test : testCaseList)
		{
			Thread t = createThread(test);
			while(true)
			{
				if(0 == getQueueCount())
				{
					testStatus.put(test, "InProgress");
					updateExcelReport();
					break;
				}
			}
			
			while(t.isAlive())
			{
				try {
					Thread.sleep(30000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			updateTestStatus(test);
			if(!"Passed".equals(testStatus.get(test)))
			{
				for(String str : testCaseList)
				{
					if("Requested".equals(testStatus.get(str)))
						testStatus.put(str, "NoRun");
				}
				updateExcelReport();
				break;
			}
			updateExcelReport();
		}
		
	}
	
	public void createDashboard()
	{
		new File(excelPath).mkdirs();
		excelPath += "\\Report_"+(new SimpleDateFormat("MMdd")).format(System.currentTimeMillis())+".xlsx";
		File excelFile = new File(excelPath);
		if(!excelFile.exists())
			try {
				createExcel(excelFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		insertdataExcel(excelFile);
	}
	
	public void monitorRuns()
	{
		int totalRuns = testStatus.size();
		int runsInQueue = totalRuns;
		int diffInCount = 0;
		List<String> inQueueList = testCaseList;
		List<String> inProgressList = new ArrayList<String>();
		
		boolean flag = true;
		do{
			try {
				Thread.sleep(30000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			diffInCount = runsInQueue-getQueueCount();
			if(0 == diffInCount)
			{
				for(String test : inProgressList)
				{
					if(false == testThread.get(test).isAlive())
					{
						updateTestStatus(test);
						inProgressList.remove(test);
					}
				}
			}
			else
			{
				for(int i=0;i<diffInCount ;i++)
				{
					String test = inQueueList.get(i);
					inProgressList.add(test);
					inQueueList.remove(i);
					testStatus.put(test, "InProgress");
				}
				
			}
			
			updateExcelReport();
			
			if(inQueueList.isEmpty() && inProgressList.isEmpty())
				flag = false;
		}while(flag);
	}
	
	private void updateExcelReport() {
		int changes = 0;
		FileInputStream fs=null;
		XSSFWorkbook wb=null;
		try {
			fs = new FileInputStream(excelPath);
			wb = new XSSFWorkbook(fs);
			XSSFSheet ws = wb.getSheet(randomNo+"");
			int rownum = ws.getPhysicalNumberOfRows();
			String actualstatus = "";
			String expectedstatus = "";
			String testName = "";
			for(int i=0; i< rownum ; i++)
			{
				testName = cellToString(ws.getRow(i).getCell(3));
				actualstatus = cellToString(ws.getRow(i).getCell(5));
				expectedstatus = testStatus.get(testName);
				if(expectedstatus.equalsIgnoreCase(actualstatus))
					continue;
				ws.getRow(i).getCell(5).setCellValue(expectedstatus);
				if("Passed".equalsIgnoreCase(expectedstatus) || "Failed".equalsIgnoreCase(expectedstatus))
					ws.getRow(i).getCell(6).setCellValue(String.format(logBasePath,testInstance.get(testName)+".html"));
				changes++;
			}
			fs.close();
			if(changes>0)
				writeExcel(wb, new File(excelPath));	
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	private void updateTestStatus(String test) {
		File xmlFile = new File(String.format(logBasePath,testInstance.get(test)+".xml"));
		File htmlFile = new File(String.format(logBasePath,testInstance.get(test)+".html"));
		
		if(!xmlFile.exists())
			testStatus.put(test, "NoRun");
		else if(!htmlFile.exists())
			testStatus.put(test, "Terminated");
		else
		{
			DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder;
			try {
				docBuilder = builderFactory.newDocumentBuilder();
				Document doc = docBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();
				
				XPathFactory xpathFactory = XPathFactory.newInstance();
	            XPath xpath = xpathFactory.newXPath();
	            XPathExpression expr = xpath.compile("Adflog/log[@level='STATUS']");
				NodeList nList = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
				Element element = (Element) nList.item(0);
				testStatus.put(test, element.getAttribute("message"));
			} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
				e.printStackTrace();
			}
		}
		
	}

	private int getQueueCount() {
		String sURL = "http://localhost:4444/grid/api/hub/";
		int queueCount = 0;
	    URL url;
		try {
			url = new URL(sURL);

			HttpURLConnection request = (HttpURLConnection) url.openConnection();
			request.connect();
			JsonParser jp = new JsonParser();
			JsonElement root = jp.parse(new InputStreamReader(
					(InputStream) request.getContent()));
			JsonObject rootobj = root.getAsJsonObject();
			queueCount = rootobj.get("newSessionRequestCount").getAsInt();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return queueCount;
	}

	private void insertdataExcel(File excelFile) {
		FileInputStream fs=null;
		XSSFWorkbook wb=null;
		try {
			fs = new FileInputStream(excelFile);
			wb = new XSSFWorkbook(fs);
			XSSFSheet ws = wb.getSheet(randomNo+"");
			int rownum = ws.getPhysicalNumberOfRows();
			for(String test : testCaseList)
			{
				XSSFRow row = ws.createRow(rownum);
				int col=0;
				List<String> list = getRowData(test);
				list.add(0, rownum+"");
				for(String value : list)
				{
					row.createCell(col).setCellValue(value);
					col++;
				}
				rownum++;
			}
			fs.close();
		writeExcel(wb, excelFile);	
		}catch (Exception e){
			e.printStackTrace();
		}
		
	}

	private void writeExcel(XSSFWorkbook wb, File excelFile) throws IOException {
			FileOutputStream fo = new FileOutputStream(excelFile);
			wb.write(fo);
			fo.flush();
			fo.close();
	}

	private List<String> getRowData(String test) {
		List<String> rowData = new ArrayList<String>();
		rowData.add(setProduct.get(testSet.get(test)));
		rowData.add(testSet.get(test));
		rowData.add(test);
		rowData.add("Script : "+script+"\n"+"Config : "+configPath+"\n"+"DataFile : "+test+"\n"+"Browser : "+browserName);
		rowData.add(testStatus.get(test));
		rowData.add("");
		
		return rowData;
	}

	private void createExcel(File file) throws IOException {
		FileOutputStream fout = new FileOutputStream(file);
		fout.close();
		if(file.exists())
		{
			XSSFWorkbook wb=null;
			try {
				wb = new XSSFWorkbook();
				XSSFSheet ws = wb.createSheet(randomNo+"");
				XSSFRow row = ws.createRow(0);
				List<String> headers = new ArrayList<String>();
				headers.add("S.No.");
				headers.add("Product");
				headers.add("Set Name");
				headers.add("Test Name");
				headers.add("Test Details");
				headers.add("Status");
				headers.add("Logf File");
 				int col=0;
				for(String value : headers)
				{
					row.createCell(col).setCellValue(value);
					col++;
				}
				writeExcel(wb, file);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	public String createCommand(String arg)
	{
		String instanceID = randomNo+"-"+count;
		count++;
		String command = String.format(baseCommand, jar, script, browserName, configPath, arg, logPath, instanceID, styleSheetPath ).trim()+" "+commandText.trim();
		testInstance.put(arg, instanceID);
		testStatus.put(arg, "Requested");
		return command;
	}
	
	public Thread createThread(String arg)
	{
		Thread thread = new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	runCommand(arg);
		    }
		});
		thread.start();
		return thread;
	}
	
	public void runCommand(String arg)
	{
			String command = createCommand(arg);
			System.out.println(command);
			try {
				Runtime.getRuntime().exec("cmd.exe /c start cmd /k "+command);
			} catch (IOException e) {
				e.printStackTrace();
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
