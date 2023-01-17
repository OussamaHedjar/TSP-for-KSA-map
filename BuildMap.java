import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class BuildMap {
static ArrayList<City> graph = new ArrayList<>();
	
	public BuildMap(String fileName) {
		String delimiter = ",";
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			for(String line; (line = br.readLine()) != null; ) {
				String[] values = line.split(delimiter);
				String firstCityName = values[0].strip();
				double distance = Double.parseDouble(values[1].strip());
				String secondCityName = values[2].strip();
				createEdge(firstCityName, secondCityName, distance);
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		addCoordinates("Cities Co-ordinates.csv");
	}

	private void addNodeIfUnique(String cityName) {
		boolean nameIsUnique = true;
		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).cityName.equals(cityName)) {
				nameIsUnique = false;
				break;
			}
		}

		if (nameIsUnique) {
			graph.add(new City(cityName));
		}
	}

	private void createEdge(String firstCityName, String secondCityName, double distance) {
		addNodeIfUnique(firstCityName);
		addNodeIfUnique(secondCityName);

		City firstCity = null;
		City secondCity = null;

		for (int i = 0; i < graph.size(); i++) {
			if (graph.get(i).cityName.equals(firstCityName)) {
				firstCity = graph.get(i);
			}
			else if (graph.get(i).cityName.equals(secondCityName)) {
				secondCity = graph.get(i);
			}
		}

		if (firstCity == null || secondCity == null) {
			throw new NullPointerException(
				"first or second city or both are null\n" 
				+ "First city = " + firstCity + "\n"
				+ "Seconds city = " + secondCity 
			);
		}
		else {
			firstCity.addNeighbor(distance, secondCity);
			secondCity.addNeighbor(distance, firstCity);
		}
	}

	public void displayMap() {
		for (int i = 0; i < graph.size(); i++) {
			City city = graph.get(i);
			print(city.cityName);
            System.out.println("latitude: " + city.getLatitude() + " , longitude: " + city.getLongitude());
			for (int j = 0; j < city.neighbors.size(); j++) {
				Edge neighborEdge = city.neighbors.get(j);
				print("\t" + neighborEdge.connectedTo.cityName + " - " + neighborEdge.distance + " KM");
			}
		}
	}

	private void addCoordinates(String filename) {
		String delimiter = ",";
		try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
			for(String line; (line = br.readLine()) != null; ) {
				String[] values = line.split(delimiter);
				for(int i = 0; i < graph.size();i++)
				{
					if(graph.get(i).cityName.equals(values[0].strip()))
					{
						//to use Haversine formula we need them in Radius.
						graph.get(i).setLatitude(Math.toRadians(Double.parseDouble(values[1].strip())));
						graph.get(i).setLongitude( Math.toRadians(Double.parseDouble(values[2].strip())));
						break;
					}
				}
			}
		}
	catch (IOException e) {
		e.printStackTrace();
	}
	}
	public void print(String str) {
		System.out.println(str);
	}

}
