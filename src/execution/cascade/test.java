package execution.cascade;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public class test {
public static void main(String[] args) throws IOException {
	File file = new File("D:\\Softwares\\FrameWork_Workspace\\Cascade\\src\\execution\\cascade\\sample.js");
	Desktop.getDesktop().browse(file.toURI());
}
}
