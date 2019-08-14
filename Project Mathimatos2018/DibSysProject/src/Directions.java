import com.google.gson.JsonObject;

public class Directions {
	private JsonObject directions;
	private long distance;
	private String LatLong;
	
	public Directions(Object g){
		Directions dummy = (Directions) g;
		this.directions = dummy.getDirections();
		this.LatLong = dummy.getLatLong();
		this.distance = dummy.getDistance();
	}
	
	public Directions(JsonObject g){
		this.directions = g;
	}
	
	public Directions(JsonObject g, String LatLong, long distance){
		this.directions = g;
		this.LatLong = LatLong;
		this.distance = distance;
	}
	
	public Directions(String LatLong){
		this.LatLong = LatLong;
	}
	
	public Directions(){
		this.distance = 0;
	}
	
	public void setDirections(JsonObject directions){
		this.directions = directions;
	}
	
	public void setLatLong(String LatLong){
		this.LatLong = LatLong;
	}
	
	public void setDistance(long dist){
		this.distance = dist;
	}
	
	public String getLatLong(){
		return this.LatLong;
	}
	
	public long getDistance(){
		return this.distance;
	}
	
	public JsonObject getDirections(){
		return this.directions;
	}
	
	public boolean isEmpty()
	{
		if (distance==0)
			return true;
		else
			return false;
	}
	
}
