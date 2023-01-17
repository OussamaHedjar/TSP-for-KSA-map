import java.util.ArrayList;

public class City {

	String cityName;
	ArrayList<Edge> neighbors = new ArrayList<>();
	//longitude is y which covers East/West, while latitude is x which covers North/South.
	private double latitude,longitude;
	double heuristic;
	final static double Earth_Radius = 6371;//in KM
	
	public City(String cityName) {
		this.cityName = cityName;
	}
	
	public void addNeighbor(double distance, City neighborNode) {
		Edge neighbor = new Edge(distance, neighborNode);
		neighbors.add(neighbor);
	}
	//we calculate the shortest distance between two points on a globe using The Haversine formula.
	public double MeasureDistance(City city) {
		double deltaLongitude = city.getLongitude() - this.getLongitude();
		double deltaLatitude = city.getLatitude() - this.getLatitude();
		double Dis = Math.pow(Math.sin(deltaLatitude / 2), 2)
                + Math.cos(this.getLatitude()) * Math.cos(city.getLatitude())
                * Math.pow(Math.sin(deltaLongitude / 2),2);
		return 2 * Math.asin(Math.sqrt(Dis)) * Earth_Radius;
	}
	
	
	public void setLatitude(double latitude) {this.latitude = latitude;}
	public void setLongitude(double longitude) {this.longitude = longitude;}
	public double getLatitude() {return latitude;}
	public double getLongitude() {return longitude;}
	public String toString() {return  cityName ;}
	
	
}


class Edge {
	City connectedTo;
	double distance;

	public Edge(double distance, City connectedTo) {
		this.distance = distance;
		this.connectedTo = connectedTo;
	}
}
