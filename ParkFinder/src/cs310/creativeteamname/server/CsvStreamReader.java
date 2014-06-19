package cs310.creativeteamname.server;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.logging.Logger;

import cs310.creativeteamname.shared.*;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
public class CsvStreamReader {
	//TODO implement this class
	private static Logger logger = Logger.getLogger("logger");
	public static void addImagesWithLibrary(InputStream stream, HashMap<Integer, Park> parks){
		
		CSVParser parser = new CSVParser(new InputStreamReader(stream), CSVStrategy.EXCEL_STRATEGY);
		try {
			String[][] result = parser.getAllValues();
			int parkId;
			for(String[] row : result){
				try{
					parkId = Integer.parseInt(row[0]);
					String parkImgUrl = row[1];
					Park p = parks.get(parkId);
					if(p != null){
						p.setImageUrl(parkImgUrl);
					}
				}catch(NumberFormatException nfe){
					// do nuttin'
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
