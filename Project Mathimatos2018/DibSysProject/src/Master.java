import java.util.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Master {

	static ServerSocket mappersSocket;		//socket gia tous mappers
	static ServerSocket clientSocket;		//Socket gia ton client
	static ServerSocket redSocket;			//Socket gia ton reducer
	static Socket mconnection[] = new Socket[2];
	static Socket cconnection;
	static Socket rconnection;
	static List<Directions> cache = new ArrayList<Directions>();
	static ObjectInputStream cin;		//streams eisodou/exodou gia ton client
	static ObjectOutputStream cout;
	static ObjectInputStream min[] = new ObjectInputStream[2];		//streams eisodou/exodou gia tous mappers
	static ObjectOutputStream mout[] = new ObjectOutputStream[2];
	static ObjectInputStream rin;		//streams eisodou/exodou gia ton reducer
	static ObjectOutputStream rout;
	static String mappersIP[] = new String[2];
	static String clientIP;
	static String reducerIP;
	static String query;			//received query
	
	
	
	public static void main(String[] args){
		
		Directions d = null;
		
		initialize();
		
		try {
			
			clientSocket = new ServerSocket(1234, 1);
			cconnection = clientSocket.accept();
			System.out.println("Client >" +cconnection.getInetAddress()+ "<connected.." );
			clientIP = cconnection.getInetAddress().toString();
			cout = new ObjectOutputStream(cconnection.getOutputStream());
			cin = new ObjectInputStream(cconnection.getInputStream());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		WaitForNewQueriesThread();
		d = searchCache(query);
		if(d != null){
			sendResultsToClient(d);
			
		}
		
		else {
			
			distributeToMappers();
			
		}
			
	}
	
	public static void initialize(){
		try{
			
			mappersSocket = new ServerSocket(4321, 2);
			redSocket = new ServerSocket(3412, 1);
			
			rconnection = redSocket.accept();	
			System.out.println("Reducer >"+rconnection.getInetAddress()+"< connected.." );
			reducerIP = rconnection.getInetAddress().toString();
			rout = new ObjectOutputStream(rconnection.getOutputStream());
			rin = new ObjectInputStream(rconnection.getInputStream());
			
			for(int i=0; i<2; i++){
				mconnection[i] = mappersSocket.accept();	
				System.out.println("Mapper >"+mconnection[i].getInetAddress()+"< connected.." );
				mappersIP[i] = mconnection[i].getInetAddress().toString();
				mout[i] = new ObjectOutputStream(mconnection[i].getOutputStream());
				min[i] = new ObjectInputStream(mconnection[i].getInputStream());
			}
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static void WaitForNewQueriesThread(){
		try{
			query = (String) cin.readObject();
		}catch(ClassNotFoundException e){
			System.err.println("Error while receiving a query!");
			e.printStackTrace();
		}catch(IOException e){
			System.err.println("Error while receiving a query!");
			e.printStackTrace();
		}finally{
			System.out.println("New Query recieved...");
		}
	}
	
	public static void checkCache(){
		if(cache.size()>100){
			for(int i=0; i<cache.size()-100;i++){
				cache.remove(0);
			}
		}
	}
	
	public static Directions searchCache(String key){
		
		for(Directions d : cache){
			if(d.getLatLong() != null && d.getLatLong().contains(key)){
				return d;
			}
		}
		return null;
		
	}
	
	
	
	public static void distributeToMappers(){
	
		for(int i=0; i<2; i++){
				try{
					mout[i].writeObject(query);
					mout[i].flush();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
			
			if (collectDataFromReducers() == false)
			{
				int chooseMapper = (
						Integer.parseInt(query.split("|")[0].split(",")[0]) 
						+ Integer.parseInt(query.split("|")[0].split(",")[1])
						+ Integer.parseInt(query.split("|")[1].split(",")[0])
						+ Integer.parseInt(query.split("|")[1].split(",")[1])
						) % 3; //+1 to number of the mappers
				
				try{
					mout[chooseMapper].writeObject("askGoogleAPI");
					mout[chooseMapper].flush();
					
				}catch(IOException e){
					e.printStackTrace();
				}
			}
	}
	
	
	
	public static void ackToReducers(){
		try{
			String ack = "Ack";
			
			rout.writeObject(ack);
			rout.flush();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static boolean collectDataFromReducers(){
		try {
			Directions answer = new Directions(rin.readObject());
			if(searchCache(answer.getLatLong())==null){
				updateCache(answer);
				checkCache();
			}
			ackToReducers();
			sendResultsToClient(answer);
			
			if(answer.isEmpty())
				return false;
			else
				return true;
			
		} catch (ClassNotFoundException | IOException e) {	
			e.printStackTrace();
			return false;
		}
		
	}
	
	
	
	public static boolean updateCache(Directions d){
		try{
			cache.add(d);
			checkCache();
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public static void sendResultsToClient(Directions ans){
		System.out.println("Sending results to client...");
	}
	
}
