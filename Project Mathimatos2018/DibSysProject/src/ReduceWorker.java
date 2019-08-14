import java.io.*;
import java.net.*;
import java.util.*;

import com.google.gson.Gson;

public class ReduceWorker implements Runnable{
	
	private List<Directions> message = new ArrayList<Directions>();
	
	ServerSocket providerSocket = null;
	static ObjectOutputStream mout = null;					//out gia na steilei ston master
	static ObjectInputStream min = null;					//in gia na lamvanei apo ton master
	private static Socket masterSocket, connection = null;
	private static final List<Directions> lat_lon_dest_list = new ArrayList<Directions>(); 

	public static void main()
	{
		
		connectToMaster();
		Thread t1 = new Thread(new ReduceWorker());
		Thread t2 = new Thread(new ReduceWorker());
		
		t1.start();
		t2.start();
		
		try {
			t1.join();
			t2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		   
	}
	
	public void run()
	{
		initialize();
	}
	
	
	public static void connectToMaster(){
		try {
			masterSocket = new Socket(InetAddress.getByName("198.251.1.1"), 3412);
			mout = new ObjectOutputStream(masterSocket.getOutputStream());
			min = new ObjectInputStream(masterSocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendResults(Directions d){
		boolean flag = true;
		do{
			try{
				mout.writeObject(d);
				mout.flush();
			}catch(IOException e){
				e.printStackTrace();
			}
			
			try{
				String ack = min.readUTF();
				if(ack.equals("Ack")){
					flag = false;
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}while(flag);
	}
	
	
	public void initialize()
	{
		try {
			System.out.println("server is waiting for connection...");
			providerSocket= new ServerSocket(4321); //o server dexetai aithmata mono sth port=4321
	
			while (true) {
				//stelnw ta object sto stream
				connection=providerSocket.accept(); //molis lavei aithma epistrefei socketx2 to server k to client to ena perimenei na parei k allo atithma enw to allo to stelnei
				ObjectOutputStream out= new ObjectOutputStream(connection.getOutputStream());
				ObjectInputStream in = new ObjectInputStream(connection.getInputStream());
				
				out.writeObject("connection successful");
				out.flush();
				do {
					try {
						message = (List<Directions>)in.readObject();
						
						for(Directions dir : message)
							lat_lon_dest_list.add(dir);
						
						System.out.println(connection.getInetAddress().getHostAddress()+">List of directions acquired");
						//inet address mporoume mesw autou na create ip
											
						
					} catch (ClassNotFoundException classnot) {
						System.err.println("Data received in unknown format");
					}
					
				} while (!message.equals("bye"));
				//lamvanei sms mexri na lavei to sms bye, tote 8qa stamathsei na lamvanei sms apo ton client
				in.close();
				out.close();
				connection.close();
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} finally {
			try {
				providerSocket.close();
			} catch (IOException ioException) {
				ioException.printStackTrace();
			}
		}
	}
	
	public void reduce() { //http://stackoverflow.com/questions/15990721/find-route-with-androids-google-maps-api
		Directions bestDir = new Directions();
		long minDistance=0; //μικρότερη απόσταση 
		
		for(Directions dir : lat_lon_dest_list)
		{
			if(dir.getDistance()>minDistance)
			{
				minDistance = dir.getDistance();
				bestDir = new Directions(dir);
			}
		}
		
		sendResults(bestDir);
	}
	

}
