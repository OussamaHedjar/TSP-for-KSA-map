import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class GUI implements ActionListener{

	BuildMap map = new BuildMap("Ksa cities.csv");
	//map.displayMap();
	ArrayList<City> TspCities = new ArrayList<>();
	private static JButton button1;
	private static JTextField textField;// number of cities
    private static JTextField textField1;//whether to set limits or not
    private static JTextField textField2;//number of iterations
    private static JTextField textField3;//temperature
    private static JTextField textField4;//number of generations
    private static JTextField textField5;//gas money
    private static JTextField textField6;//cities
    ////////////////////////////////////////////////////////////////////////////////////
    private static JLabel label ;// number of cities
    private static JLabel label1;//whether to set limits or not
    private static JLabel label2;//number of iterations
    private static JLabel label3;//temperature
    private static JLabel label4;//number of generations
    private static JLabel label5;//gas money
    private static JLabel label6;//cities
    private static JLabel results1;//for printing results
    private static JLabel results2;//for printing results
    private static JLabel results3;//for printing results
    int NbCities,iterations,genertions,GasMoney,temperature;
		String CityName, answer;
	public static void main(String[] args){
		
	        JFrame frame = new JFrame();
	        JPanel panel = new JPanel();
	        frame.setSize(200, 200);
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        frame.add(panel);
	        panel.setLayout(null);
	        
	        // remebmer in setbound it goes: x , y, width, height.
	        //cities
	        label6 = new JLabel("Enter cities names: ");
	        label6.setBounds(20,50, 120, 20);
	        panel.add(label6);
	        textField6 = new JTextField(35);
	        textField6.setBounds(130,50, 360, 20);
	        panel.add(textField6);
	        //set limits or not
	        label1 = new JLabel("Would you like to set a limit for iterations? ");
	        label1.setBounds(505,50, 250, 20);
	        panel.add(label1);
	        textField1 = new JTextField(35);
	        textField1.setBounds(750,50, 50, 20);
	        panel.add(textField1);
	        //number of iterations
	        label2 = new JLabel("set the number of iterations: ");
	        label2.setBounds(810,50, 180, 20);
	        panel.add(label2);
	        textField2 = new JTextField(35);
	        textField2.setBounds(980,50, 50, 20);
	        panel.add(textField2);
	        //temperature
	        label3 = new JLabel("set the initial temeprature: ");
	        label3.setBounds(20,80,170, 20);
	        panel.add(label3);
	        textField3 = new JTextField(35);
	        textField3.setBounds(170,80, 60, 20);
	        panel.add(textField3);
	        //number of generations
	        label4 = new JLabel("set the number of generations: ");
	        label4.setBounds(235,80,200, 20);
	        panel.add(label4);
	        textField4 = new JTextField(35);
	        textField4.setBounds(415,80, 60, 20);
	        panel.add(textField4);
	        //gas money
	        label5 = new JLabel("Price for KM/Liter: ");
	        label5.setBounds(480,80,150, 20);
	        panel.add(label5);
	        textField5 = new JTextField(35);
	        textField5.setBounds(590,80, 60, 20);
	        panel.add(textField5);
	        // buttons to search using the algorithms
	        button1 = new JButton("Solve TSP Problem");
	        button1.setBounds(200, 110, 180, 30);
	        button1.addActionListener(new GUI());
	        panel.add(button1);
	        
	        results1 = new JLabel("");
	        results1.setBounds(20,130,700, 200);
	        panel.add(results1);
	        
	        results2 = new JLabel("");
	        results2.setBounds(20,210,700, 200);
	        panel.add(results2);
	        
	        results3 = new JLabel("");
	        results3.setBounds(20,290,700, 200);
	        panel.add(results3);
	        
	        frame.setVisible(true);
	        
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String[] names = textField6.getText().split(",");
		for(String name : names)
		{
			TspCities.add(new City(name));
		}
		answer = textField1.getText();
		iterations = Integer.parseInt(textField2.getText());
		temperature = Integer.parseInt(textField3.getText());
		genertions = Integer.parseInt(textField4.getText());
		GasMoney = Integer.parseInt(textField5.getText());
		GeneticAlgorithms geneticAlgorithms  = new GeneticAlgorithms(TspCities);
		HillClimbing hc = new HillClimbing();
		SimulatedAnnealing sa = new SimulatedAnnealing();
		Route route = new Route(TspCities, map.graph);
		SARoute Route = new SARoute(TspCities);
		switch(answer) {
		
		case "yes":
			results1.setText("<html>" + hc.Printer(hc.FindShortestRoute(route, GasMoney, iterations), GasMoney).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")+ "</html>");
			results2.setText("<html>" +sa.Printer(sa.FindRoute(temperature, Route), GasMoney).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
			results3.setText("<html>" +geneticAlgorithms.dispaly(geneticAlgorithms.GA(TspCities, genertions), GasMoney).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")+ "</html>" );
			break;
		case "no":
			iterations = 100; temperature = 50; genertions = 30;
			results1.setText("<html>" + hc.Printer(hc.FindShortestRoute(route, GasMoney, iterations), GasMoney).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")+ "</html>");
			results2.setText("<html>" +sa.Printer(sa.FindRoute(temperature, Route), GasMoney).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>") + "</html>");
			results3.setText("<html>" +geneticAlgorithms.dispaly(geneticAlgorithms.GA(TspCities, genertions), GasMoney).replaceAll("<","&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br/>")+ "</html>" );
			break;
		default: results1.setText("please choose to either control the number or iterations or no.");
		}
		}
	 
	 
	 

}
