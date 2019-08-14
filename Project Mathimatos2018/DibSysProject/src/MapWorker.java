import com.google.gson.*;
import java.io.*;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.net.*;


public class MapWorker {
	
	private List<Directions> lat_lon_dest_list;
	private String lat_long;
	
	private Socket masterSocket = null;
	private ObjectOutputStream out = null;
	private ObjectInputStream in = null;
	private Socket reducerSocket = null;
	private ObjectOutputStream rout = null;
	private ObjectInputStream rin = null;
	
	
	public void main() throws IOException{
		
		//Get the database from the .txt to memory
		
		lat_lon_dest_list = new ArrayList<Directions>();
						
		final String FILENAME = "database.txt";

		BufferedReader br = null;
		FileReader fr = null;

		try {

			fr = new FileReader(FILENAME);
			br = new BufferedReader(fr);

			String sCurrentLine;
			Directions dir = new Directions();

			br = new BufferedReader(new FileReader(FILENAME));

			while ((sCurrentLine = br.readLine()) != null) {
				
				if(sCurrentLine.equals("$")){
					
					dir.setLatLong(br.readLine());
					dir.setDistance(Long.parseLong(br.readLine()));
					
					String s = "";
					while(!sCurrentLine.equals("$"))
					{
						s += br.readLine();
					}
					
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JsonElement jelem = gson.fromJson(s, JsonElement.class);
					JsonObject jobj = jelem.getAsJsonObject();
					
					dir.setDirections(jobj);
					lat_lon_dest_list.add(dir);
				}

			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {

			try {

				if (br != null)
					br.close();

				if (fr != null)
					fr.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
		
		//Read Lat_Long from Master
		
		try {
			
			masterSocket = new Socket (InetAddress.getByName("172.16.1.37"),4321); //grafw thn ip tou server pou 8eloume na epikoinwnisouime
			out = new ObjectOutputStream(masterSocket.getOutputStream());
			in = new ObjectInputStream(masterSocket.getInputStream());
		
			try {
				
				lat_long = (String) in.readObject();
				sendToReducers(lat_long);
			
			} catch (ClassNotFoundException classNot) {
				System.err.println("Data received in unknown format");
			}
		
		
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		}		
		
	}
		

	
	public List<Directions> map(String lat_long){ //same for source...h lista an dn pernage evgaze error

		List<Directions> myMap = new ArrayList<Directions>();
		double lat_source=0, long_source=0, lat_dest=0, long_dest=0;
		double lat_source_list=0, long_source_list=0, lat_dest_list=0, long_dest_list=0;
		String lat_long_rounded="", lat_long_list_rounded="";

		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);

		lat_source = Double.parseDouble( lat_long.split("|")[0].split(",")[0] );
		long_source = Double.parseDouble( lat_long.split("|")[0].split(",")[1] );
		lat_dest = Double.parseDouble( lat_long.split("|")[1].split(",")[0] );
		long_dest = Double.parseDouble( lat_long.split("|")[1].split(",")[1] );

		lat_long_rounded = df.format(lat_source).toString() + df.format(long_source).toString() 
			+ df.format(lat_dest).toString() + df.format(long_dest).toString() ;

		for(Directions dir : lat_lon_dest_list) 
		{
			lat_source = Double.parseDouble( dir.getLatLong().split("|")[0].split(",")[0] );
			long_source = Double.parseDouble( dir.getLatLong().split("|")[0].split(",")[1] );
			lat_dest = Double.parseDouble( dir.getLatLong().split("|")[1].split(",")[0] );
			long_dest = Double.parseDouble( dir.getLatLong().split("|")[1].split(",")[1] );

			lat_long_list_rounded = df.format(lat_source_list).toString() + df.format(long_source_list).toString() 
				+ df.format(lat_dest_list).toString() + df.format(long_dest_list).toString() ;

			if(lat_long_rounded.equals(lat_long_list_rounded))	{
				myMap.add(dir); 
			}
		}

		return myMap;
	}
	
	
	public void sendToReducers(String lat_long){
		
		try {
			
			reducerSocket= new Socket (InetAddress.getByName("172.16.1.37"),4321); //grafw thn ip tou server pou 8eloume na epikoinwnisouime
			rout= new ObjectOutputStream(reducerSocket.getOutputStream());
			rin= new ObjectInputStream(reducerSocket.getInputStream());
			
			System.out.println("Server>" + lat_long);
			
			rout.writeObject("Sending map results...");
			rout.flush();
			
			rout.writeObject(map(lat_long));
			rout.flush();
			
			rout.writeObject("bye");
			rout.flush();
			
			askGoogleAPI(lat_long);
			
			
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} 
		
		finally {
			try {
				in.close();
				out.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	public void askGoogleAPI(String lat_long)
	{
		
		try {
			
			String wakeUp = (String)rin.readObject(); //if we get here, the mapper has been selected to ask the api
			
			//Take the path JSON
			String pathUrl = 
					"https://maps.googleapis.com/maps/api/directions/json?"
					+ "origin=" + lat_long.split("|")[0] + "&destination=" + lat_long.split("|")[1]
					+ "&key=AIzaSyC-OpMJkIoPUsw5sJ0JiuSCgwXDNXd-Ak8";
			
			URL pathURL = new URL(pathUrl);
		    HttpURLConnection request = (HttpURLConnection) pathURL.openConnection();
		    request.connect();
	
		    // Convert to a JSON object
		    JsonParser jp = new JsonParser(); //from gson
		    JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		    JsonObject pathJSON = root.getAsJsonObject();
		    
	
		    String distanceUrl = 
					"http://maps.googleapis.com/maps/api/distancematrix/json"
					+ "origins=" + lat_long.split("|")[0] + "&destinations=" + lat_long.split("|")[1];
			
			URL distanceURL = new URL(distanceUrl);
		    request = (HttpURLConnection) distanceURL.openConnection();
		    request.connect();
	
		    // Convert to a JSON object
		    jp = new JsonParser(); //from gson
		    root = jp.parse(new InputStreamReader((InputStream) request.getContent())); //Convert the input stream to a json element
		    JsonObject distanceJSON = root.getAsJsonObject();
		    
		    long distance = 
		    		distanceJSON.getAsJsonObject("rows")
		    		.getAsJsonObject("elements")
		    		.get("distance").getAsLong();
		    
		    Directions dir = new Directions(pathJSON, lat_long, distance);
			
		    rout.writeObject(dir);
			rout.flush();
			
			try(FileWriter fw = new FileWriter("database.txt", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter db = new PrintWriter(bw))
				{
				    db.println("$\n" + lat_long + "\n" + distance);
				    db.println(pathJSON.toString());
				    
				} catch (IOException e) {
				    e.printStackTrace();
				}
			
		
		} catch (UnknownHostException unknownHost) {
			System.err.println("You are trying to connect to an unknown host!");
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} 
	}
	
}

