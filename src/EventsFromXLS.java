	
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cloudinary.*;

import com.cloudinary.utils.ObjectUtils;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//apache poi imports

public class EventsFromXLS{

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException {
		try {
			
		    
        	Jedis jedis = RedisBullet.getPool();
        	
			
        	
        	
			Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
					  "cloud_name", "parc-india",
					  "api_key", "",
					  "api_secret", ""));
			
			
			
			File toUpload = null;
			Map uploadResult = null;
			DBArrow SQLArrow = DBArrow.getArrow();
			Map<Integer, String> cities = new HashMap<Integer, String>();
			Map<Integer, String> games = new HashMap<Integer, String>();
			PreparedStatement statement1 = SQLArrow.getPreparedStatement("select * from cities");
	        try (ResultSet rs = SQLArrow.fire(statement1)) {
	        	while (rs.next()) {
	        		System.out.println(rs.getInt("city_id") + "CITYYY");
	        		jedis.set("GLOBAL_CITY_"+Integer.toString(rs.getInt("city_id")),rs.getString("display_name"));
	        		cities.put(rs.getInt("city_id"), rs.getString("display_name"));
	        	}
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        	System.out.println(e.getMessage());
	        }
			
			statement1 = SQLArrow.getPreparedStatement("select * from games");
	        try (ResultSet rs = SQLArrow.fire(statement1)) {
	        	while (rs.next()) {
	        		jedis.set("GLOBAL_GAMES_"+Integer.toString(rs.getInt("game_id")),rs.getString("display_name"));
	        		games.put(rs.getInt("game_id"), rs.getString("display_name"));
	        	}
	        }catch(Exception e)
	        {
	        	e.printStackTrace();
	        	System.out.println(e.getMessage());
	        }
	        
	        
			InputStream is = new FileInputStream("C:\\Users\\USER\\Desktop\\TICKETING OWN\\09TH SEPTEMBER\\capers_event1.xlsx");
			XSSFWorkbook wb = new XSSFWorkbook(is);

			XSSFSheet sheet1 = wb.getSheetAt(0);
			//System.out.println("Reading Sheet: " + sheet1.getSheetName());

			XSSFRow r0 = sheet1.getRow(0);
			XSSFRow r1 = sheet1.getRow(1);
			Integer redisId = null;
	        try {
	            boolean xxx = false;
	            String ID = "XX";
				Row R = null;
				int ii=0;
				int eventId = 0;
				Iterator<Row> rowIterator = sheet1.iterator();
				while(rowIterator.hasNext()){
					R=rowIterator.next();

					System.out.println(R.getCell(0) + "0");
					System.out.println(R.getCell(1)+ "1");
					System.out.println(R.getCell(2)+ "2");
					System.out.println(R.getCell(3)+ "3");
					System.out.println(R.getCell(4)+ "4");
					System.out.println(R.getCell(5)+ "5");
					System.out.println(R.getCell(6)+ "6");
					System.out.println(R.getCell(7)+ "7");
					System.out.println(R.getCell(8)+ "8");
					System.out.println(R.getCell(9)+ "9");
					System.out.println(R.getCell(10)+ "10");
					System.out.println(R.getCell(11)+ "11");
					System.out.println(R.getCell(12)+ "12");
					System.out.println(R.getCell(13)+ "13");
//
				System.out.println("Done Row");
					if( R.getCell(0) !=null && R.getCell(0).toString().equals("id")){
						ii++;
						continue;
					}
					System.out.println("INSERTING ROW NUMBER"+ ii);
					if(ID.equals(R.getCell(0).toString())){
						System.out.println("SKIPPING THE SAME EVENT ROW BUT INSERTING IN PRICE TABLES" + eventId);
		                if(eventId > 0) {
		                	PreparedStatement statement = SQLArrow.getPreparedStatement("INSERT INTO price  (event_id, price_currency, price_amount, description,price_name,category_id ) values (?, ?, ?, ?, ?, ?)");
		                	statement.setInt(1, eventId);
		                	statement.setString(2, "INR");
		                	statement.setInt(3, Integer.parseInt(R.getCell(9).toString().trim().substring(0, R.getCell(9).toString().trim().length()-2)));
		                	statement.setString(4, R.getCell(13).toString());
		                	statement.setString(5, R.getCell(10).toString());
		                	statement.setString(6, R.getCell(12).toString());
;
			                if(SQLArrow.fireBowfishing(statement) == 1){
			                	System.out.println("INSERTED PRICE FOR ROW NUMBER"+ ii);
			                }
		                }
						
						ID = R.getCell(0).toString();
					}else{
						ID = R.getCell(0).toString();
						PreparedStatement statement = SQLArrow.getPreparedStatementForId("INSERT INTO event  (event_type, eventdate, status, description,venue,event_name,timings,creation_date,event_city,max_participants, event_city_id, img_url, start_price ) values (?, ?, ?, ?, ?, ?, ?,NOW(),?, ?, ?,?,?)");
		                statement.setInt(1, Integer.parseInt(R.getCell(6).toString().trim().substring(0, R.getCell(6).toString().trim().length()-2)));
		                statement.setString(2, R.getCell(4).toString());
		                statement.setInt(3, 1);
		                statement.setString(4, "Sorry No Data !!");
		                statement.setString(5, R.getCell(8).toString());
		                statement.setString(6, R.getCell(1).toString());
		                statement.setString(7, R.getCell(7).toString());
		                statement.setString(8,cities.get((Integer.parseInt(R.getCell(6).toString().trim().substring(0, R.getCell(6).toString().trim().length()-2)))));
		                statement.setString(9, R.getCell(2).toString());
		                //if(R.getCell(2).toString().equalsIgnoreCase("delhi"))
		                System.out.println(R.getCell(6).toString());
		                	statement.setInt(10,Integer.parseInt(R.getCell(6).toString().trim().substring(0, R.getCell(6).toString().trim().length()-2)));
		                //else
		                	//statement.setInt(10, 0);
		                
		                toUpload = new File("C:\\Users\\USER\\Desktop\\TICKETING OWN\\09TH SEPTEMBER\\"+R.getCell(11).toString());
		    			uploadResult = cloudinary.uploader().upload(toUpload, ObjectUtils.emptyMap());
		                
		                statement.setString(11, uploadResult.get("secure_url").toString());
		                //statement.setString(11, "dfdf");
		                System.out.println(statement.toString());
		                statement.setInt(12, Integer.parseInt(R.getCell(14).toString().trim().substring(0, R.getCell(14).toString().trim().length()-2)));
		                if(SQLArrow.fireBowfishing(statement) == 1){
		                	System.out.println("INSERTED ROW NUMBER"+ ii);
			                ResultSet rs = statement.getGeneratedKeys();
			                if(rs.next())
			                {
			                	eventId = rs.getInt(1);
			                	jedis.set(Integer.toString(eventId), R.getCell(5).toString());
			                	
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
				
			    Set<String> names=jedis.keys("ACTIVE_CITIES_*");

			    Iterator<String> it = names.iterator();
			    while (it.hasNext()) {
			    	
			        String s = it.next();
			        jedis.del(s);
			    }
			    Set<String> gameNames=jedis.keys("ACTIVE_GAMES_*");

			    Iterator<String> it2 = gameNames.iterator();
			    while (it2.hasNext()) {
			    	
			        String s = it2.next();
			        jedis.del(s);
			    }
				statement1 = SQLArrow.getPreparedStatement("select event_type from EVENT GROUP BY event_type");
				jedis.set("ACTIVE_GAMES_81","All");
		        try (ResultSet rs = SQLArrow.fire(statement1)) {
		        	while (rs.next()) {
		        		jedis.set("ACTIVE_GAMES_"+Integer.toString(rs.getInt("event_type")),games.get(rs.getInt("event_type")));
		        	}
		        }catch(Exception e)
		        {
		        	e.printStackTrace();
		        	System.out.println(e.getMessage());
		        }
				
				statement1 = SQLArrow.getPreparedStatement("select event_city_id from EVENT GROUP BY event_city_id");
				jedis.set("ACTIVE_CITIES_1","All Cities");
		        try (ResultSet rs = SQLArrow.fire(statement1)) {
		        	while (rs.next()) {
		        		System.out.println(cities.get(rs.getInt("event_city_id")) + "My_IDDDD");
		        		jedis.set("ACTIVE_CITIES_"+Integer.toString(rs.getInt("event_city_id")),cities.get(rs.getInt("event_city_id")));
		        	}
		        }catch(Exception e)
		        {
		        	e.printStackTrace();
		        	System.out.println(e.getMessage());
		        }
				SQLArrow.relax(null);
				
				jedis.close();
				is.close();
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	            SQLArrow.rollBack(null);
	        }
			
		
		
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
