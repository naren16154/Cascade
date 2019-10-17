package execution.cascade;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class GUI extends ReadExcel{
	
	static String excelPath = null;
	static String productName = null;
	static String setName = null;
	static List<String> testCaseList = new LinkedList<String>();
	static String browserName = null;
	static String configPath = null;
	static String flowName = null;
	static String commandText = null;
	static String logPath = null;
	static String styleSheetPath = null;

	public static void main(String[] args) {
		GUI obj = new GUI();
		obj.createGUI();
	}

	public void createGUI() {
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(900, 700);
//		frame.setLocation(480, 150);
		frame.setLocation(200, 0);
		frame.setTitle("CASCADE");
		
		// select Excel
		JLabel excelLabel = new JLabel("Select Excel : ");
		excelLabel.setBounds(10, 10, 130, 30);
		frame.getContentPane().add(excelLabel);
		JTextField excel = new JTextField();
		excel.setBounds(150, 10, 500, 30);
		frame.getContentPane().add(excel);
		JButton browse1 = new JButton("Browse");
		browse1.setBounds(660, 10, 80, 30);
		frame.getContentPane().add(browse1);

		//Select product
		JLabel productLabel = new JLabel("Select Product : ");
		productLabel.setBounds(10, 50, 130, 30);
		frame.getContentPane().add(productLabel);
		
		Vector<String> productItems=new Vector<String>();
		productItems.add("");
	    JComboBox<String> product = new JComboBox<String>(productItems);
	    product.setBounds(150, 50, 200, 30);
		frame.getContentPane().add(product);
		
		//Test Set
		JLabel setlabel = new JLabel("Select Set : ");
		setlabel.setBounds(10, 100, 130, 30);
		frame.getContentPane().add(setlabel);
		
		Vector<String> setItems=new Vector<String>();
		setItems.add("");
	    JComboBox<String> set = new JComboBox<String>(setItems);
	    set.setBounds(150, 100, 500, 30);
		frame.getContentPane().add(set);
		
		//Test Cases
		JLabel testLabel = new JLabel("Select Test Case : ");
		testLabel.setBounds(10, 150, 130, 30);
		frame.getContentPane().add(testLabel);
		
		Vector<String> testItems=new Vector<String>();
		testItems.add("");
	    JComboBox<String> testCases = new JComboBox<String>(testItems);
	    testCases.setBounds(150, 150, 500, 30);
		frame.getContentPane().add(testCases);
		
		//Browser
		JLabel browserLabel = new JLabel("Browser : ");
		browserLabel.setBounds(10, 200, 130, 30);
		frame.getContentPane().add(browserLabel);
		
		String[] list = {"Firefox","Chrome","Internet Explorer","Edge","Mobile"};
	    JComboBox<String> browser = new JComboBox<String>(list);
	    browser.setBounds(150, 200, 100, 30);
		frame.getContentPane().add(browser);
		
		//config File
		JLabel configLabel = new JLabel("Config File : ");
		configLabel.setBounds(10, 250, 130, 30);
		frame.getContentPane().add(configLabel);
		
		JTextField config = new JTextField();
		config.setBounds(150, 250, 500, 30);
		frame.getContentPane().add(config);
		
		JButton browse2 = new JButton("Browse");
		browse2.setBounds(660, 250, 80, 30);
		frame.getContentPane().add(browse2);

		browse2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("C:/"));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter filter = new FileNameExtensionFilter(".xml", "xml");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					config.setText(fileChooser.getSelectedFile().toString());
				}
			}
		});
		
		//Log File
				JLabel logLabel = new JLabel("Log File path : ");
				logLabel.setBounds(10, 300, 130, 30);
				frame.getContentPane().add(logLabel);
				
				JTextField log = new JTextField();
				log.setBounds(150, 300, 500, 30);
				frame.getContentPane().add(log);
				
				JButton browse3 = new JButton("Browse");
				browse3.setBounds(660, 300, 80, 30);
				frame.getContentPane().add(browse3);

				browse3.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setCurrentDirectory(new File("C:/"));
						fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
						fileChooser.setAcceptAllFileFilterUsed(false);
						int rVal = fileChooser.showOpenDialog(null);
						if (rVal == JFileChooser.APPROVE_OPTION) {
							log.setText(fileChooser.getSelectedFile().toString());
						}
					}
				});
		
				//Style Sheet
				JLabel styleLabel = new JLabel("Style Sheet : ");
				styleLabel.setBounds(10, 350, 130, 30);
				frame.getContentPane().add(styleLabel);
				
				JTextField style = new JTextField();
				style.setBounds(150, 350, 500, 30);
				frame.getContentPane().add(style);
				
				JButton browse4 = new JButton("Browse");
				browse4.setBounds(660, 350, 80, 30);
				frame.getContentPane().add(browse4);

				browse4.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						JFileChooser fileChooser = new JFileChooser();
						fileChooser.setCurrentDirectory(new File("C:/"));
						fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
						FileFilter filter = new FileNameExtensionFilter(".xsl", "xsl");
						fileChooser.addChoosableFileFilter(filter);
						fileChooser.setAcceptAllFileFilterUsed(false);
						int rVal = fileChooser.showOpenDialog(null);
						if (rVal == JFileChooser.APPROVE_OPTION) {
							style.setText(fileChooser.getSelectedFile().toString());
						}
					}
				});
		//Execution Flow
		JLabel flowLabel = new JLabel("Execution Flow : ");
		flowLabel.setBounds(10, 400, 130, 30);
		frame.getContentPane().add(flowLabel);
		
		String[] list1 = {"Parallel","Serial","Eflow"};
	    JComboBox<String> flow = new JComboBox<String>(list1);
	    flow.setBounds(150, 400, 100, 30);
		frame.getContentPane().add(flow);
		
		//Command
		JLabel commandLabel = new JLabel("Command File : ");
		commandLabel.setBounds(10, 450, 130, 30);
		frame.getContentPane().add(commandLabel);
		
		JTextArea command = new JTextArea();
		command.setLineWrap(true);
		command.setWrapStyleWord(true);
		command.setText("-DbasePath=\"D:\\\" -DlogName=\"adflogFile\" -DtimeStamp=\"false\" -Dsuccess_robot=\"false\" -Dsuccess_scroll=\"false\" -Dfailure_robot=\"false\" -Dfailure_scroll=\"true\" -Derror_robot=\"false\" -Derror_scroll=\"true\" -DisRCServer=\"true\" -Dport=\"4444\" -DopenLogFile=\"false\" oracle.adf.scripts.ADFScriptRunner");
		command.setBounds(150, 450, 500, 100);
		frame.getContentPane().add(command);
		
		//Submit Button
		JButton submit = new JButton("Run");
		submit.setBounds(300, 570, 70, 30);
		frame.getContentPane().add(submit);
		
		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				excelPath = excel.getText().trim();
				productName = product.getSelectedItem().toString();
				setName = set.getSelectedItem().toString();
				browserName = browser.getSelectedItem().toString();
				configPath = config.getText().trim();
				logPath = log.getText().trim();
				styleSheetPath = style.getText().trim();
				flowName = flow.getSelectedItem().toString();
				commandText = command.getText().trim();
				String testcase = testCases.getSelectedItem().toString();
				if(testcase.equals("All..."))
				{
					for(int i=2; i<testCases.getItemCount() ; i++)
						testCaseList.add(testCases.getItemAt(i).toString());
				}
				else
					testCaseList.add(testCases.getSelectedItem().toString());
				
//				System.out.println("---"+excelPath+"---"+productName+"---"+setName+"---"+browserName+"---"+configPath+"---"+logPath+"---"+styleSheetPath+"---"+flowName+"---"+commandText);
				ExecutionCore obj = new ExecutionCore();
				obj.createAndMonitorRuns();
			}
		});
		
		//Cancel Button
		JButton cancel = new JButton("Cancel");
		cancel.setBounds(400, 570, 100, 30);
		frame.getContentPane().add(cancel);
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
		
		browse1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File("C:/"));
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				FileFilter filter = new FileNameExtensionFilter(".xlsx", "xlsx");
				fileChooser.addChoosableFileFilter(filter);
				fileChooser.setAcceptAllFileFilterUsed(false);
				int rVal = fileChooser.showOpenDialog(null);
				if (rVal == JFileChooser.APPROVE_OPTION) {
					excel.setText(fileChooser.getSelectedFile().toString());
					excelPath = excel.getText().toString().trim();
					
					getDataFromExcel(excelPath);
					product.removeAllItems();
					product.addItem("");
					product.addItem("All...");
					for(String str : workbookData.keySet())
					product.addItem(str);
				}
			}
		});
		
		product.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String productValue = (String) product.getSelectedItem();
				if(null != productValue)
				{
					set.removeAllItems();
					set.addItem("");
				if(!"".equals(productValue))
				{
					set.addItem("All...");
					if("All...".equals(productValue))
					{
					for(String p : workbookData.keySet())
					{
						for(String s : workbookData.get(p).keySet())
							set.addItem(s);
					}
					}
					else
					{
						for(String s : workbookData.get(productValue).keySet())
							set.addItem(s);
					}
				}
				}
				}
		});
		
		set.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String productValue = (String) product.getSelectedItem();
				String setValue = (String) set.getSelectedItem();
				if(null !=setValue)
				{
					testCases.removeAllItems();
					testCases.addItem("");
				if(!"".equals(setValue))
				{
					testCases.addItem("All...");
					if("All...".equals(productValue))
					{
						 if("All...".equals(setValue))
						 {
							for(String p : workbookData.keySet())
							{
								for(String s : workbookData.get(p).keySet())
									for(String t : workbookData.get(p).get(s))
										testCases.addItem(t);
							}
						 }
						 else
						 {
							 for(String p : workbookData.keySet())
								{
										if(workbookData.get(p).containsKey(setValue))
										{
											for(String t : workbookData.get(p).get(setValue))
												testCases.addItem(t);
										}
								}
						 }
					}
					else
					{
						if("All...".equals(setValue))
						{
							for(String s : workbookData.get(productValue).keySet())
								for(String t : workbookData.get(productValue).get(s))
								testCases.addItem(t);
						}
						else
						{
							for(String t : workbookData.get(productValue).get(setValue))
								testCases.addItem(t);	
						}
					}
				}
				}
				}
		});
		
		//Future Enhancements
		JLabel label = new JLabel("");
		label.setBounds(300, 650, 100, 30);
		frame.getContentPane().add(label);
		
		frame.setVisible(true);
	}

}
