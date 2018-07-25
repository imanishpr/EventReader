	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cloudinary.*;
import com.cloudinary.Util.*;
import com.cloudinary.utils.ObjectUtils;

//apache poi imports

public class EventsFromXLS{

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		try {
			
			
			
			Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
					  "cloud_name", "parc-india",
					  "api_key", "137447452869923",
					  "api_secret", "tynQQxYZEajwczLluFbYuAYXUAo"));
			File toUpload = null;
			Map uploadResult = null;
			DBArrow SQLArrow = DBArrow.getArrow();
			
			
			
			
			
			InputStream is = new FileInputStream("/Users/mac/waste/capers_event.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(is);

			XSSFSheet sheet1 = wb.getSheetAt(0);
			System.out.println("Reading Sheet: " + sheet1.getSheetName());

			XSSFRow r0 = sheet1.getRow(0);
			XSSFRow r1 = sheet1.getRow(1);

			// reading cells
//			System.out.println("Cell r1c1: " + r0.getCell(0));
//			System.out.println("Cell r1c2: " + r0.getCell(1));
//			System.out.println("Cell r2c1: " + r1.getCell(0));
			
			
			
	        try {
	            boolean xxx = false;
	            String ID = "XX";
				Row R = null;
				int ii=0;
				int eventId = 0;
				Iterator<Row> rowIterator = sheet1.iterator();
				while(rowIterator.hasNext()){
					R=rowIterator.next();

//					System.out.println(R.getCell(0) + "0");
//					System.out.println(R.getCell(1)+ "1");
//					System.out.println(R.getCell(2)+ "2");
//					System.out.println(R.getCell(3)+ "3");
//					System.out.println(R.getCell(4)+ "4");
//					System.out.println(R.getCell(5)+ "5");
//					System.out.println(R.getCell(6)+ "6");
//					System.out.println(R.getCell(7)+ "7");
//					System.out.println(R.getCell(8)+ "8");
//					System.out.println(R.getCell(9)+ "9");
//					System.out.println(R.getCell(10)+ "10");
//
//					System.out.println("Done Row");
					if(R.getCell(0).toString().equals("id")){
						ii++;
						continue;
					}
					System.out.println("INSERTING ROW NUMBER"+ ii);
					if(ID.equals(R.getCell(0).toString())){
						System.out.println("SKIPPING THE SAME EVENT ROW BUT INSERTING IN PRICE TABLES");
		                if(eventId > 0) {
		                	PreparedStatement statement = SQLArrow.getPreparedStatement("INSERT INTO price  (event_id, price_currency, price_amount, description,price_name,category_id ) values (?, ?, ?, ?, ?, ?)");
		                	statement.setInt(1, eventId);
		                	statement.setString(2, "INR");
		                	statement.setInt(3, Integer.parseInt(R.getCell(9).toString().trim().substring(0, R.getCell(9).toString().trim().length()-2)));
		                	statement.setString(4, R.getCell(13).toString());
		                	statement.setString(5, R.getCell(10).toString());
		                	statement.setString(6, R.getCell(12).toString());
			                if(SQLArrow.fireBowfishing(statement) == 1){
			                	System.out.println("INSERTED PRICE FOR ROW NUMBER"+ ii);
			                }
		                }
						
						ID = R.getCell(0).toString();
					}else{
						ID = R.getCell(0).toString();
						PreparedStatement statement = SQLArrow.getPreparedStatementForId("INSERT INTO event  (event_type, eventdate, status, description,venue,event_name,timings,creation_date,event_city,max_participants, event_city_id, img_url, start_price ) values (?, ?, ?, ?, ?, ?, ?,NOW(),?, ?, ?,?,99)");
		                statement.setInt(1, 1);
		                statement.setString(2, R.getCell(4).toString());
		                statement.setInt(3, 1);
		                statement.setString(4, R.getCell(5).toString());
		                statement.setString(5, R.getCell(8).toString());
		                statement.setString(6, R.getCell(1).toString());
		                statement.setString(7, R.getCell(7).toString());
		                statement.setString(8, R.getCell(6).toString());
		                statement.setString(9, R.getCell(2).toString());
		                //if(R.getCell(2).toString().equalsIgnoreCase("delhi"))
		                	statement.setInt(10, new Integer(R.getCell(6).toString().substring(0,1)));
		                //else
		                	//statement.setInt(10, 0);
		                
		                toUpload = new File("/Users/mac/Downloads/"+R.getCell(11).toString());
		    			uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
		                
		                statement.setString(11, uploadResult.get("secure_url").toString());
		                System.out.println(statement.toString());
		                if(SQLArrow.fireBowfishing(statement) == 1){
		                	System.out.println("INSERTED ROW NUMBER"+ ii);
			                ResultSet rs = statement.getGeneratedKeys();
			                if(rs.next())
			                {
			                	eventId = rs.getInt(1);
			                }
			                if(eventId > 0) {
			                	statement = SQLArrow.getPreparedStatement("INSERT INTO price  (event_id, price_currency, price_amount, description,price_name,category_id ) values (?, ?, ?, ?, ?, ?)");
			                	statement.setInt(1, eventId);
			                	statement.setString(2, "INR");
			                	statement.setInt(3, Integer.parseInt(R.getCell(9).toString().trim().substring(0, R.getCell(9).toString().trim().length()-2)));
			                	statement.setString(4, R.getCell(13).toString());
			                	statement.setString(5, R.getCell(10).toString());
			                	statement.setString(6, R.getCell(12).toString());
				                if(SQLArrow.fireBowfishing(statement) == 1){
				                	System.out.println("INSERTED PRICE FOR ROW NUMBER"+ ii);
				                }
			                }
		                }

					}
					ii++;
//		            bufferedWriter.write((R.getCell(0).toString().trim().substring(0, R.getCell(0).toString().trim().length()-2)));
//		            bufferedWriter.newLine();
//					System.out.println(R.getCell(0).toString().substring(0, R.getCell(0).toString().length()-2));
				}
				SQLArrow.relax(null);
	             
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            SQLArrow.rollBack(null);
	        }
			
		
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
