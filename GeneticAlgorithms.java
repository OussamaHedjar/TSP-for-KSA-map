import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.IntStream;


class GeneticRoute{
	
    private boolean IsFitnessChanged = true;//flag to when cities fitness of the route is changed.
    private double fitness = 0;
	ArrayList<City> cities  = new ArrayList<>();
	//a list that will hold the coming cities from user.
	ArrayList<City> C  = new ArrayList<>();
	//a list that will hold graph cities.
	ArrayList<City> D  = new ArrayList<>();
	static double DistancePassed = 0;
	public GeneticRoute(GeneticAlgorithms geneticAlgorithms) {
		geneticAlgorithms.IntialRoute.forEach(x -> cities.add(null));
	}
	//we changed the idea, i won't pass graph array as a parameter, m gonna call it here. 
	public GeneticRoute(ArrayList<City> towns) {
		C.addAll(0, towns);
		D.addAll(0, BuildMap.graph);
		graphit();
	}
	//this method produces a list of cities we can work on. by taking name from B compare it to A, and then we get all the info in list of Cities.
	public ArrayList<City> graphit(){
		for(int i = 0; i < C.size(); i++)
		{
			for(int j = 0; j < D.size(); j++)
			{
				if(C.get(i).cityName.equals(D.get(j).cityName))
				{
					cities.add(D.get(j));
				}
			}
		}
		return cities;
			}
	
	public double CalculateTotalDistance() {
		int citiesSize = cities.size();
		return this.cities.stream().mapToDouble(x -> {
			int Index = this.cities.indexOf(x);
			if(Index < citiesSize - 1) DistancePassed = x.MeasureDistance(this.cities.get(Index + 1));
			return DistancePassed;
		}).sum() + this.cities.get(0).MeasureDistance(this.cities.get(citiesSize - 1));
	}
	//we use it to calculate the fitness of the route .
	public double GetFitness() {
		if(IsFitnessChanged == true) {
			fitness = ((1/CalculateTotalDistance())*10000);
			IsFitnessChanged = false;
		}
		return fitness;
	}
	public String toString() {return Arrays.toString(cities.toArray());}

}

class Population{
	ArrayList<GeneticRoute> routes = new ArrayList<GeneticRoute>(GeneticAlgorithms.Population_size);
	
	public Population(int populationsize, GeneticAlgorithms geneticAlgorithms) {
		IntStream.range(0, populationsize).forEach(x -> routes.add(new GeneticRoute(geneticAlgorithms.IntialRoute)));
	}
	public Population(int populationsize, ArrayList<City> towns) {
		IntStream.range(0, populationsize).forEach(x -> routes.add(new GeneticRoute(towns)));
	}
	
	public void SortRoutesByFitness() {
		routes.sort((route1,route2) -> {
			int flag = 0;
			if(route1.GetFitness() > route2.GetFitness()) flag = -1;
			else if(route1.GetFitness() < route2.GetFitness()) flag = 1;
			return flag;
		});				
	}
	
	//we going to dispaly the whole UI.
	void FinalPrintPopulation(Population population, double money) {
		System.out.println("----------------------------------------------------------------------------");
		System.out.print("Final Route: ");
		for(int x = 0; x < population.routes.get(0).cities.size();x++)
			{
				if(x < population.routes.get(0).cities.size() - 1) System.out.print(population.routes.get(0).cities.get(x).cityName + ", ");
				else if(x == population.routes.get(0).cities.size() - 1) System.out.print(population.routes.get(0).cities.get(x).cityName + ".");
			}
		System.out.println();
		System.out.println("----------------------------------------------------------------------------");
		System.out.printf("Distance Traveled: %4.2f KM." , population.routes.get(0).CalculateTotalDistance());
		System.out.println();
		System.out.println("----------------------------------------------------------------------------");
		System.out.printf("Distance cost: %4.2f SR. \n" , (population.routes.get(0).CalculateTotalDistance()* 2.18) / money );
	}
}

public class GeneticAlgorithms {

	//Mutation_rate: is the probability of chromosome gene doing a random mutation, the range is between 0 & 1.
	//in the tsp we treat the route as a chromosome, and city as a gene. 
	public static final double Mutation_rate = 0.25;
	//Tournament_Selection_size is used for route (chromosome) crossover.
	public static final int  Tournament_Selection_size = 3;
	//the size is by choice.
	public static final int Population_size = 8;
	public static final int ELITE_ROUTE = 1; //will not be exposed to any crossover or any mutation.
	//public static final int GENERATIONS = 30;// you can change this.This is the iterations.
	ArrayList<City> IntialRoute = null;
	
	public GeneticAlgorithms(ArrayList<City> intialroute) {IntialRoute = intialroute;}
	
	Population Evolve(Population population) {return MutatePopulation(CrossoverPopulation(population));}
	
	Population CrossoverPopulation(Population population) {
		Population crossoverPopulation = new Population(population.routes.size(),this);
		IntStream.range(0, ELITE_ROUTE).forEach(x -> crossoverPopulation.routes.set(x, population.routes.get(x)));
		IntStream.range(ELITE_ROUTE, crossoverPopulation.routes.size()).forEach(x -> {
			//using the selecttournamentpopulation method should give us the fittest route, and the we'll cross between the two.
			GeneticRoute route1 = SelectTournamentPopulation(population).routes.get(0);
			GeneticRoute route2 = SelectTournamentPopulation(population).routes.get(0);
			//from below we'll get a new route, we'll set in the crossoverpopulation
			crossoverPopulation.routes.set(x, CrossoverRoute(route1, route2));
		});
		return crossoverPopulation;
	}
	
	//we gonna filter the population to get an elite route ,and for the non elite (since we made 8) we going to use mutate route.
	Population MutatePopulation(Population population) {
		population.routes.stream().filter(x -> population.routes.indexOf(x) >= ELITE_ROUTE).forEach(x -> MutateRoute(x));
		return population;
	}
	//it is as the name suggests.
	GeneticRoute CrossoverRoute(GeneticRoute route1, GeneticRoute route2) {
		GeneticRoute crossroute = new GeneticRoute(this);
		GeneticRoute temproute1 = route1;
		GeneticRoute temproute2 = route2;
		if(Math.random() < 0.5)
		{
			temproute1 = route2;
			temproute2 = route1;
		}
		for(int x = 0; x < crossroute.cities.size()/2;x++)
			crossroute.cities.set(x, temproute1.cities.get(x));
		return FittingNullsInCrossoverRoute(crossroute, temproute2);
	}
	private GeneticRoute FittingNullsInCrossoverRoute(GeneticRoute crossoverroute, GeneticRoute route) {
		route.cities.stream().filter(x -> !crossoverroute.cities.contains(x)).forEach(cX -> {
			for(int y = 0; y < route.cities.size(); y++)
			{
				if(crossoverroute.cities.get(y) == null)
				{
					crossoverroute.cities.set(y, cX);
					break;
				}
			}
		});
		return crossoverroute;
	}
	
	//this is where the routes starts to change positions of the cities. under the condition the random number is less than mutation rate, if it is
	// we'll take two indecies and flip them.
	GeneticRoute MutateRoute(GeneticRoute route) {
		route.cities.stream().filter(x -> Math.random() < Mutation_rate ).forEach(cX -> {
			 int y = (int) (Math.random() * route.cities.size());
			 City cY = route.cities.get(y);
			 route.cities.set(route.cities.indexOf(cX), cY);
			 route.cities.set(y,cX);
		});
		return route;
	}
	//using the constant, will have 3 routes in this tournament population, then will choose a random number of routes , and sort them by fitness.
	Population SelectTournamentPopulation(Population population) {
		Population TournamentPopulation = new Population(Tournament_Selection_size, this);
		IntStream.range(0, Tournament_Selection_size).forEach(x -> TournamentPopulation.routes.set(x, population.routes.get((int) Math.random() *
				population.routes.size())));
		TournamentPopulation.SortRoutesByFitness();
		return TournamentPopulation;
	}
	
	public Population GA(ArrayList<City> route, int Generations) {
		Population population = new Population(Population_size, route);
		GeneticAlgorithms geneticAlgorithms = new GeneticAlgorithms(route);
		int count = 0;
		while(count < Generations)
		{
			population = geneticAlgorithms.Evolve(population);
			population.SortRoutesByFitness();
			count++;
			/*System.out.print("Route: ");
			for(int x = 0; x < population.routes.get(0).cities.size();x++)
				{
					if(x < population.routes.get(0).cities.size() - 1) System.out.print(population.routes.get(0).cities.get(x).cityName + ", ");
					else if(x == population.routes.get(0).cities.size() - 1) System.out.print(population.routes.get(0).cities.get(x).cityName + ". \n");
				}
			System.out.print("Fitness of this route: " + population.routes.get(0).GetFitness() + " Distance: " + population.routes.get(0).CalculateTotalDistance());
			System.out.println();*/
		}
		//population.FinalPrintPopulation(population, money);
		return population;
	}
	public String dispaly(Population population, double money) {
		String ro = population.routes.get(0).toString();
		return "------------------------------------  Genetic Algorithms    ---------------------------------------- \n"
			 + "Final Route: " + ro + "\n Distanece traveled: " + String.format("%.2f",population.routes.get(0).CalculateTotalDistance()) + "KM. \n"  
		+ "Distance cost: " + String.format("%.2f",((population.routes.get(0).CalculateTotalDistance()* 2.18) / money) ) +" SR. \n" +
		"---------------------------------------------------------------------------- \n";
	}
}
