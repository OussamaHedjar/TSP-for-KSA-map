import java.util.ArrayList;
import java.util.Arrays;

class SARoute{
	ArrayList<City> ccities  = new ArrayList<City>();
	//a list that will hold the coming cities from user.
		ArrayList<City> E  = new ArrayList<>();
		//a list that will hold graph cities.
		ArrayList<City> F  = new ArrayList<>();
		static double DistanceTraveled = 0;
		public SARoute(ArrayList<City> towns) {//with this one to populate the arraylist of ccities.
			E.addAll(0, towns);
			F.addAll(0, BuildMap.graph);
			graphit();
		}
		// the second constructor will initialize these cities using a pre-existing route instance.
		public SARoute(SARoute route) {this.ccities.addAll(route.ccities);}

		//this method produces a list of cities we can work on. by taking name from B compare it to A, and then we get all the info in list of Cities.
		public ArrayList<City> graphit(){
			for(int i = 0; i < E.size(); i++)
			{
				for(int j = 0; j < F.size(); j++)
				{
					if(E.get(i).cityName.equals(F.get(j).cityName))
					{
						ccities.add(F.get(j));
					}
				}
			}
			return ccities;
		}
		public String toString() {return Arrays.toString(ccities.toArray());}
		
		public double TotalDistance() {
			int ccitiesSize = ccities.size();
			return this.ccities.stream().mapToDouble(x -> {
				int Index = this.ccities.indexOf(x);
				if(Index < ccitiesSize - 1) DistanceTraveled = x.MeasureDistance(this.ccities.get(Index + 1));
				return DistanceTraveled;
			}).sum() + this.ccities.get(ccitiesSize - 1).MeasureDistance(this.ccities.get(0)); 
		}

        
		
} 
public class SimulatedAnnealing {

	public static final double Rate_Of_Cooling = 0.005;
	public static final double Min_Temperature = 0.99;
	
	public SARoute FindRoute(double temp,SARoute route) {
		SARoute ShortestRoute = new SARoute(route);
		SARoute AdjacentRoute;
		while(temp > Min_Temperature)
		{
			AdjacentRoute = ObtainedRoute(new SARoute(route));
			if(route.TotalDistance() < ShortestRoute.TotalDistance()) ShortestRoute = new SARoute(route);
			if(AcceptRoute(route.TotalDistance(), AdjacentRoute.TotalDistance(), temp)) route = new SARoute(AdjacentRoute);
			/*System.out.println("----------------------------------------------------------------------------");
			System.out.print("Route: ");
			for(int i = 0; i < ShortestRoute.ccities.size(); i++ ) {
				if(i <  ShortestRoute.ccities.size() - 1) {
			        System.out.print( ShortestRoute.ccities.get(i).cityName + ", ");} 
				else if(i ==  ShortestRoute.ccities.size() - 1) {
					System.out.print( ShortestRoute.ccities.get(i).cityName + "."); 
					}
				}
			System.out.println();
			System.out.println("----------------------------------------------------------------------------");
			System.out.println("Current temperature: " + temp);
			System.out.printf("Distanece traveled: %.2f KM. \n", ShortestRoute.TotalDistance());
			System.out.println("----------------------------------------------------------------------------");*/
			temp *= 1 - Rate_Of_Cooling;
		}
		return ShortestRoute;
	}
	public String Printer(SARoute currentR,double money) {
		String ro = currentR.toString(); 
		return   "------------------------------------  Simulated Annealing   ---------------------------------------- \n"
		 + "Final Route: " + ro + "\n Distanece traveled: " + String.format("%.2f",currentR.TotalDistance()) + "KM. \n"  
		+ "Distance cost: " + String.format("%.2f",((currentR.TotalDistance()* 2.18) / money) ) +" SR. \n" +
		"---------------------------------------------------------------------------- \n";
		}
	//with use this to swapp between routes, if new route has a better (smaller) value then the current one.Else we swapp (accept new route) routes with probability. 
	private boolean AcceptRoute(double currentDistance, double adjacentDistance, double temperature) {
		boolean  acceptroute = false;//shortestDistance = false,
		double acceptprobability = 1;
		if(adjacentDistance >= currentDistance) {
			acceptprobability = Math.exp(-(adjacentDistance - currentDistance) / temperature);
			//shortestDistance = false;
		}
		double Ranum = Math.random();
		if(acceptprobability >= Ranum) acceptroute = true;
		return acceptroute;
	}
	private SARoute ObtainedRoute(SARoute route) {
		int i1 = 0,i2 = 0;
		while(i1 == i2)
		{
			i1 =  (int) (route.ccities.size()* Math.random());
			i2 =  (int) (route.ccities.size()* Math.random());

		}
		//flip cities indices randomly.
		City city1 = route.ccities.get(i1);
		City city2 = route.ccities.get(i2);
		route.ccities.set(i2, city1);
		route.ccities.set(i1, city2);
        //will return the route with two have cities been swapped.
		return route;
	}
	
}
