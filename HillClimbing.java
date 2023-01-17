import java.util.ArrayList;
import java.util.Arrays;

class Route{
	
	 ArrayList<City> Cities  = new ArrayList<>();
	//a list that will hold the coming cities from user.
	ArrayList<City> A  = new ArrayList<>();
	//a list that will hold graph cities.
	ArrayList<City> B  = new ArrayList<>();
	static double DistanceTraveled = 0;
	
	public Route(ArrayList<City> towns, ArrayList<City> g) {
		A.addAll(0, towns);
		B.addAll(0, g);
		graphit();
	}
	// the second constructor will initialize those cities using a pre-existing route instance.
	public Route(Route route) {route.Cities.stream().forEach(x -> Cities.add(x));}

	//this method produces a list of cities we can work on. by taking name from B compare it to A, and then we get all the info in list of Cities.
	public ArrayList<City> graphit(){
		for(int i = 0; i < A.size(); i++)
		{
			for(int j = 0; j < B.size(); j++)
			{
				if(A.get(i).cityName.equals(B.get(j).cityName))
				{
					Cities.add(B.get(j));
				}
			}
		}
		return Cities;
	}
	/* we can use collection shuffle the taken list of cities. to have a random starting point
		Collections.shuffle(Cities);*/
	
	public String toString() {return  Arrays.toString(Cities.toArray()); }
	//this will calculate the distance starting from the first city to the last ; city1 + city2...etc until at the end it adds city1 with cityN.
	/*we going circling around the cities using mapToDouble function which does:
	 * Stream mapToDouble (ToDoubleFunction mapper) returns a DoubleStream consisting of the results of applying the given function to the elements of this stream.
	 * this is a lazy operation, will take list of cities provided (AS A STREAM) and do the math itself.*/
	public double TotalDistance() {
		int CitiesSize = Cities.size();
		return this.Cities.stream().mapToDouble(x -> {
			int Index = this.Cities.indexOf(x);
			if(Index < CitiesSize - 1) DistanceTraveled = x.MeasureDistance(this.Cities.get(Index + 1));
			return DistanceTraveled;
		}).sum() + this.Cities.get(CitiesSize - 1).MeasureDistance(this.Cities.get(0)); 
	}
	//cannot use it since the length of the given array is dynamic.  
	/*public String TotalStringDistance() {
		String returnValue
	}*/
}
public class HillClimbing {
	
	public static final int NUMBER_OF_RANDOM_RESTARTS = 10;
    //we'll implement random restart hill climbing.using a selected number of restarts that the algorithm can do, if it reaches local maxima.
	public ArrayList<Route> findshortestrout(Route route, double money, int MAXIMUM_ITERATIONS){
		ArrayList<Route> routes = new ArrayList<Route>();
		for(int i = 0; i < NUMBER_OF_RANDOM_RESTARTS; i++) routes.add(FindShortestRoute(route,money,MAXIMUM_ITERATIONS));
		//we're going to store these routes sorted by the shortest distance usinng the method SortRoutesByShortestDistance
		SortRoutesByShortestDistance(routes);
		PrintFinalRoute(routes, money);
		return routes;
	}
	public Route FindShortestRoute(Route currentR,double money,int MAXIMUM_ITERATIONS) {
		Route NeighborRoute;
		int count = 0;
		//to my understanding with this loop we're applying Steepest-Ascent Hill Climbing, where we keep looping until we either find a better route or run out of iterations.
		while(count < MAXIMUM_ITERATIONS)
		{
			//we use here ObtainedRoute to gives us another one, we use it to compare to see which is better the new one which we swapped between two cities.
			NeighborRoute = ObtainedRoute(new Route(currentR));
			if(NeighborRoute.TotalDistance() <= currentR.TotalDistance())
			{
				count = 0;
				currentR = new Route(NeighborRoute);
				/*the behavior of the algorithm printer:
				  System.out.print("Progress Route: ");
				for(int i = 0; i < currentR.Cities.size(); i++ ) {
					if(i < currentR.Cities.size() - 1) {
				        System.out.print(currentR.Cities.get(i).cityName + ", ");} 
					else if(i == currentR.Cities.size() - 1) {
						System.out.print(currentR.Cities.get(i).cityName + "."); 
						}
					}
				System.out.println();*/
			}
			else count++;
			
		}
		//System.out.print(Printer(currentR, money));
		return currentR;
	}
	 
	public String Printer(Route currentR,double money) {
		String ro = currentR.toString(); 
		return   "------------------------------------  Hill Climbing         ----------------------------------------\n"
			+"Final Route: " + ro + "\n Distanece traveled: " + String.format("%.2f",currentR.TotalDistance()) + "KM. \n"  
		+ "Distance cost: " + String.format("%.2f",((currentR.TotalDistance()* 2.18) / money) ) +" SR. \n" +
		"---------------------------------------------------------------------------- \n";
		}
	public void PrintFinalRoute(ArrayList<Route> routes,double money) {
		//we print the resulted route and all of its information
				System.out.println("----------------------------------------------------------------------------");
				System.out.print("Final Route: ");
				for(int i = 0; i < routes.get(0).Cities.size(); i++ ) {
					if(i < routes.get(0).Cities.size() - 1) {
				        System.out.print(routes.get(0).Cities.get(i).cityName + ", ");} 
					else if(i == routes.get(0).Cities.size() - 1) {
						System.out.print(routes.get(0).Cities.get(i).cityName + "."); 
						}
					}
				System.out.println();
				System.out.println("----------------------------------------------------------------------------");
				System.out.printf("Distanece traveled: %.2f KM. \n", routes.get(0).TotalDistance());
				System.out.println("----------------------------------------------------------------------------");
				System.out.printf("Distance cost: %.2f SR. \n" ,((routes.get(0).TotalDistance()* 2.18) / money  ));
	}
	 public void SortRoutesByShortestDistance(ArrayList<Route> routes) {
		 routes.sort((route1, route2) -> {
			 int flag = 0;
			 if(route1.TotalDistance() < route2.TotalDistance()) flag = -1;
			 else if(route1.TotalDistance() > route2.TotalDistance()) flag = 1;
			 return flag;
		 });
	 }
	 private Route ObtainedRoute(Route route) {
		int i1 = 0,i2 = 0;
		while(i1 == i2)
		{
			i1 =  (int) (route.Cities.size()* Math.random());
			i2 =  (int) (route.Cities.size()* Math.random());

		}
		//flip cities indices randomly.
		City city1 = route.Cities.get(i1);
		City city2 = route.Cities.get(i2);
		route.Cities.set(i2, city1);
		route.Cities.set(i1, city2);

		return route;
	}

}