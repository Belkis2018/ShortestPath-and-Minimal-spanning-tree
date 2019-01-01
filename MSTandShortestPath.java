
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.nio.channels.UnsupportedAddressTypeException;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.border.Border;

//import panel.EdgesListener;

import java.awt.event.*;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseAdapter;

public class MSTandShortestPath extends JFrame {

    public static panel panel;
    public static panel2 panel2;

    public MSTandShortestPath() {
        setSize(1200, 800);
        setTitle("GRAPH GUI: MSTandShortestPath");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panel = new panel();
        panel2 = new panel2();

        add(panel, BorderLayout.WEST);
        add(panel2, BorderLayout.CENTER);
        setVisible(true);

    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            JOptionPane.showMessageDialog(null, "There was a error setting up a GUI: ");
            System.exit(0);
        }
        new MSTandShortestPath();
    }

}






class panel extends JPanel{
	public static JRadioButton AddVertex;// Radio Button For Drawing Vertices
	public static JRadioButton AddEdge;// Radio Button For Drawing Edges
	public static JRadioButton MoveVertex;
	public static JRadioButton ShortestPath;// Radio Button To Calculate and Display Shortest Path Between Two Points
	public static JRadioButton Change_a_weight_to;//Radio Button to change the weight of Selected Vertices
	private static final int NO_PARENT = -1;// Variable used in Shortest Path Algo to show the node having no parent 
	private static  int V = 0;// variable to store the total number of nodes for Minimum Spanning Algorithm
	public static JTextField weight;//Textfields to take the input value for assigning the weights to an Edge
	public panel() {

		setLayout(new GridLayout(0, 1,5,5));
		setBorder(BorderFactory.createTitledBorder("Functions:"));

		//RADIO BUTTONS
		AddVertex = new JRadioButton("AddVertex", true);
		AddEdge = new JRadioButton("AddEdge");
		MoveVertex = new JRadioButton ("MoveVertex");
		ShortestPath = new JRadioButton ("ShortestPath");
		Change_a_weight_to = new JRadioButton ("Change a weight_to");


		// Setting the fonts properties
		AddVertex.setFont(new Font("TimesNewRoman",Font.PLAIN,16));
		AddEdge.setFont(new Font("TimesNewRoman",Font.PLAIN,16));
		MoveVertex.setFont(new Font("TimesNewRoman",Font.PLAIN,16));
		ShortestPath.setFont(new Font("TimesNewRoman",Font.PLAIN,16));
		Change_a_weight_to.setFont(new Font("TimesNewRoman",Font.PLAIN,16));

		//Adding Radio Button to the Group for the GUI
		ButtonGroup rd = new ButtonGroup();
		rd.add(AddVertex);
		rd.add(AddEdge);
		rd.add(MoveVertex);
		rd.add(ShortestPath);
		rd.add(Change_a_weight_to);


		//Initializing buttons

		JButton a = new JButton("Add all Edges");
		JButton b = new JButton("Random Weights");
		JButton c = new JButton("Minimal Spanning Tree");
		JButton d = new JButton("Help");
	
		//Settings for fonts 
		a.setFont(new Font("TimesNewRoman",Font.PLAIN,20));
		b.setFont(new Font("TimesNewRoman",Font.PLAIN,20));
		c.setFont(new Font("TimesNewRoman",Font.PLAIN,20));
		d.setFont(new Font("TimesNewRoman",Font.PLAIN,20));
		
		//Adding Radio Button to the panel
		add(AddVertex);
		add(AddEdge);
		add(MoveVertex);
		add(ShortestPath);
		add(Change_a_weight_to);

		weight = new JTextField(10);
		add(weight);
		
		//adding buttons to panel
		add(a);
		add(b);
		add(c);
		add(d);
		
		//Action Listeners for buttons
		d.addActionListener(new ActionListener() {		//Help Button
			@Override
			public void actionPerformed(ActionEvent e) {
				new HelpButton();
			}
		});

		b.addActionListener(new ActionListener() {		//Randoom weight button
			@Override
			public void actionPerformed(ActionEvent e) {
				new RandomWeights();


			}
		});
		
		c.addActionListener(new ActionListener() {	//MInimal Spanning Tree
			
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null,"Not yet implemented:");
				
			}
		});

		a.addActionListener(new ActionListener() {		//Add all edges 
			@Override
			public void actionPerformed(ActionEvent e) {
				panel2.selectedPairs.clear();
				Random rand = new Random();


				for(int i = 0 ; i < panel2.pairs.size(); i++){
					boolean at_least_one_edge = false;
					for(int j = 0 ; j < panel2.pairs.size(); j++){

						if(i != j){
							if(!at_least_one_edge){
								panel2.selectedPairs.add(panel2.pairs.get(i));
								panel2.selectedPairs.add(panel2.pairs.get(j));
								panel2.vertices_list.add(i);
								panel2.vertices_list.add(j);
								at_least_one_edge = true;
							}else{
								int  random_num = rand.nextInt(2) + 0;
								System.out.print(random_num);
								if(random_num == 1){
									panel2.selectedPairs.add(panel2.pairs.get(i));
									panel2.selectedPairs.add(panel2.pairs.get(j));
									panel2.vertices_list.add(i);
									panel2.vertices_list.add(j);
								}
							}
						}
					}
				}
				System.out.println(panel2.selectedPairs.size());
				MSTandShortestPath.panel2.reDrawPicture();

			}
		});


	}
	class EdgesListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			//todo 
		}
	}

	//Shortest Path algorithm for using Dijkstra's algorithm
	// algorithm for a graph represented 
	// using adjacency matrix representation
	
	public static void dijkstra(int[][] adjacencyMatrix,
			int startVertex, int endVertex)
	{
		int nVertices = adjacencyMatrix[0].length;

		// shortestDistances[i] will hold the
		// shortest distance from src to i
		int[] shortestDistances = new int[nVertices];

		// added[i] will true if vertex i is
		// included / in shortest path tree
		// or shortest distance from src to 
		// i is finalized
		boolean[] added = new boolean[nVertices];

		// Initialize all distances as 
		// INFINITE and added[] as false
		for (int vertexIndex = 0; vertexIndex < nVertices; 
				vertexIndex++)
		{
			shortestDistances[vertexIndex] = Integer.MAX_VALUE;
			added[vertexIndex] = false;
		}

		// Distance of source vertex from
		// itself is always 0
		shortestDistances[startVertex] = 0;

		// Parent array to store shortest
		// path tree
		int[] parents = new int[nVertices];

		// The starting vertex does not 
		// have a parent
		parents[startVertex] = NO_PARENT;

		// Find shortest path for all 
		// vertices
		for (int i = 1; i < nVertices; i++)
		{

			// Pick the minimum distance vertex
			// from the set of vertices not yet
			// processed. nearestVertex is 
			// always equal to startNode in 
			// first iteration.
			int nearestVertex = -1;
			int shortestDistance = Integer.MAX_VALUE;
			for (int vertexIndex = 0;
					vertexIndex < nVertices; 
					vertexIndex++)
			{
				if (!added[vertexIndex] &&
						shortestDistances[vertexIndex] < 
						shortestDistance) 
				{
					nearestVertex = vertexIndex;
					shortestDistance = shortestDistances[vertexIndex];
				}
			}

			// Mark the picked vertex as
			// processed
			added[nearestVertex] = true;

			// Update dist value of the
			// adjacent vertices of the
			// picked vertex.
			for (int vertexIndex = 0;
					vertexIndex < nVertices; 
					vertexIndex++) 
			{
				int edgeDistance = adjacencyMatrix[nearestVertex][vertexIndex];

				if (edgeDistance > 0
						&& ((shortestDistance + edgeDistance) < 
								shortestDistances[vertexIndex])) 
				{
					parents[vertexIndex] = nearestVertex;
					shortestDistances[vertexIndex] = shortestDistance + 
							edgeDistance;
				}
			}
		}

		printSolution(startVertex, shortestDistances, parents, endVertex);
	}

	// A utility function to print 
	// the constructed distances
	// array and shortest paths
	private static void printSolution(int startVertex,
			int[] distances,
			int[] parents, int endVertex)
	{
		int nVertices = distances.length;
		System.out.println("\nVertex\t Distance\tPath");

		for (int vertexIndex = 0; 
				vertexIndex < nVertices; 
				vertexIndex++) 
		{
			if (vertexIndex != startVertex) 
			{
				if(vertexIndex == endVertex){
					System.out.print("\n" + startVertex + " -> ");
					System.out.print(vertexIndex + " \t\t ");
					System.out.print(distances[vertexIndex] + "\t\t");
					printPath(vertexIndex, parents);
				}
			}
		}
	}

	// Function to print shortest path
	// from source to currentVertex
	// using parents array
	private static void printPath(int currentVertex,
			int[] parents)
	{

		// Base case : Source node has
		// been processed
		if (currentVertex == NO_PARENT)
		{
			return;
		}
		printPath(parents[currentVertex], parents);
		panel2.dspathNodes.add(currentVertex);
		//System.out.print(currentVertex + " ");
	}


}




class HelpButton {
	//  public static void main(String[] args) {

	static String content = "HOW TO USE GRAPH:\n"
			+"\n"
			+ " 1)  Selct Add Vertex radio button(will be selected bydefault) and click on the Mpapping area to create a vertex:\n"
			+"\n"
			+" 2)  Now click on AddEdge radio button and then selct start and end vertex from mapping area by simply clicking on them:\n"
			+"\n"
			+" 3)  If you want you can use add all edges button to radomly create edges betwween vertices\n"
			+"\n"
			+" 4)  now click on change a weight to radio button to assign weightd manually; to do so type desired int no into text area below radio button   then click  on the start vertex and then end vertex\n(some times the changes that u made might not appear on the graph but it worked behind the scene "
			+"\n"
			+"\n"
			+" 5)  or you can use RANDOM WEIGHTS button to assign weights to all edges randomly\n"
			+"\n"
			+" 6)  If you want to move any vertex click on it and then click on desired area where you want to move it\n"
			+"\n"
			+" 7)  SHORTEST PATH:\n"
			+"\n"
			+" 8)  to find SHIRTEST PATH click on shortestPath radio button then click on start vertex and click on the end vertex: \n"
			+"\n"
			+" 9)  MINIMAL SPANNING TREE\n"
			+"\n"
			+" 10) to find minimal spamning tree simply click Minimal Spanning Tree BUtton:\n"
			+"\n"
			+"11) TO EXIT click on close button on top right corner:"
			;




	public HelpButton() {
		JFrame helpFrame = new JFrame("Instruction:");
		helpFrame.setSize(1200, 800);
		JPanel txtArea = new JPanel();

		helpFrame.getContentPane().add(txtArea);
		JTextArea a = new JTextArea(content);
		a.setSize(1100,790);
		a.setFont(a.getFont().deriveFont(20f));
		a.setLineWrap(true);
		a.setEditable(false);
		txtArea.add(a);

		helpFrame.setVisible(true);

	}
}


class RandomWeights extends JFrame{
	public static  int[][] adjacent_structure;
	// Function to check whether the provided nodes in arguments are connected with edge or not
	public static boolean checkThePointAreConnectedWithEdge(Integer first, Integer second){
		for(int i = 0 ; i < panel2.vertices_list.size(); i+=2){
			if(panel2.vertices_list.get(i) == first && panel2.vertices_list.get(i+1) == second){
				return true;
			}
		} 
		return false;
	}

	public static void initializeStructure(){
		try{
			ArrayList<Point> pairs = panel2.pairs;// Array list to store the points drawn by the user in the GUI
			ArrayList<Point> selectedPairs = panel2.selectedPairs;//Data structure to store the points that are selected by the use to draw the edges
			//System.out.println(pairs.size());
			adjacent_structure = new int[pairs.size()][pairs.size()];//Adjacent structure that will be used in the Minimum Spanning Tree algo and Shortest Path algo 
			// Randomly Assigning the weights of the edges
			for(int i = 0 ; i < pairs.size(); i++){
				for(int j = 0 ; j < pairs.size(); j++){
					if(checkThePointAreConnectedWithEdge(i,j)){

						adjacent_structure[i][j] = 1;
						adjacent_structure[j][i] = 1;
					}
				}


			}
			for(int i = 0 ; i < pairs.size(); i++){
				for(int j = 0 ; j < pairs.size(); j++){

					System.out.print(adjacent_structure[i][j] + " ");

				}
				System.out.println();
			}

		}
		catch(Exception ex){

		}
	}
	public RandomWeights(){
		//System.out.println("Comming here");
		ArrayList<Point> pairs = panel2.pairs;// Array list to store the points drawn by the user in the GUI
		ArrayList<Point> selectedPairs = panel2.selectedPairs;//Data structure to store the points that are selected by the user to draw the edges
		//System.out.println(pairs.size());
		adjacent_structure = new int[pairs.size()][pairs.size()];//Adjacent structure that will be used in the Minimum Spanning Tree algo and Shortest Path algo 
		for(int i = 0 ; i < panel2.vertices_list.size(); i+=2){
			adjacent_structure[panel2.vertices_list.get(i)][panel2.vertices_list.get(i+1)] = 4; 
		}

		// Randomly Assigning the weights of the edges
		for(int i = 0 ; i < pairs.size(); i++){
			for(int j = 0 ; j < pairs.size(); j++){
				if(checkThePointAreConnectedWithEdge(i,j)){
					Random rand = new Random();

					int  random_num = rand.nextInt(9) + 1;

					adjacent_structure[i][j] = random_num;
					adjacent_structure[j][i] = random_num;
				}
			}

		}
		for(int i = 0 ; i < pairs.size(); i++){
			for(int j = 0 ; j < pairs.size(); j++){

				System.out.print(adjacent_structure[i][j] + " ");

			}
			System.out.println();
		}


		panel2.assign_weights_flag = true;
		MSTandShortestPath.panel2.reDrawPicture();

	}



}








class panel2 extends JPanel {

    public static boolean assign_weights_flag = false;// Flag the check whether user has triggere the event to assign the random weights
    public static boolean mst_flag = false;// Flag to check whether user has triggered the event to calculate and display the MST
    public static boolean clear_dspathNodes_flag = false;// Flag to clear the data structure storing the shortest path algo nodes
    public static ArrayList<Point> pairs;// Data structure to hold the points draws by the user on gui
    public static ArrayList<Point> selectedPairs;//Data structre to hold the selcted point between which we have to draw the edges
    public static ArrayList<Integer> dspathNodes;// Shortes path node holding data structure indeces
    public static ArrayList<Point> shortest_path_pairs;// Shortest path node holding data structure of Point datatype
    public static ArrayList<Point> edge_to_change_weight;// List of points for which we have to change the weights on user triggering

    public static ArrayList<Point> mstPairs; // Points containg data structure for MST;
    
    public static ArrayList<Integer> vertex_to_be_moved;

    public static ArrayList<Integer> vertices_list;// List of vertices used in the algo's;

    public panel2() {

        setLayout(null);
        setBorder(BorderFactory.createTitledBorder("MAPPING AREA"));
        //Initializing Variables
        pairs = new ArrayList<Point>();
        selectedPairs = new ArrayList<Point>();
        mstPairs = new ArrayList<Point>();
        vertices_list = new ArrayList<Integer>();
        shortest_path_pairs = new ArrayList<Point>();
        dspathNodes = new ArrayList<Integer>();
        edge_to_change_weight = new ArrayList<Point>();
        vertex_to_be_moved = new ArrayList<Integer>();
        
        
        
        // Adding the mouse click event listner
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                
                // Check whether the radio button for changing weights is enables
                if(panel.MoveVertex.isSelected()){
                    
                    int i = 0;
                    if(vertex_to_be_moved.size() == 1){
                         for(Point a : selectedPairs){
                            if(a == pairs.get(vertex_to_be_moved.get(0))){
                                selectedPairs.set(selectedPairs.indexOf(a), new Point(e.getX(), e.getY()));
                            }
                            
                            
                        }
                         
                           for(Point a : mstPairs){
                            if(a == pairs.get(vertex_to_be_moved.get(0))){
                                mstPairs.set(mstPairs.indexOf(a), new Point(e.getX(), e.getY()));
                            }
                            
                            
                        }
                        pairs.set(vertex_to_be_moved.get(0), new Point(e.getX(), e.getY()));
                        
                       
                        vertex_to_be_moved.clear();
                    }else{
                      for (Point point : pairs) {
                       // Getting the node around which the user has clicked
                        if ((e.getX() >= point.x - 20 && e.getX() <= point.x + 20)
                                && (e.getY() >= point.y - 20 && e.getY() <= point.y + 20)) {
                            
                            vertex_to_be_moved.add(pairs.indexOf(point));
                        }
                    }
                    }
                 
                }
                else if (panel.Change_a_weight_to.isSelected()) {

                    int i = 0;

                    for (Point point : pairs) {
                        ++i;
                        // Getting the node around which the user has clicked
                        if ((e.getX() >= point.x - 20 && e.getX() <= point.x + 20)
                                && (e.getY() >= point.y - 20 && e.getY() <= point.y + 20)) {
                            
                            // Check if the user has clicked the same node twice
                            if (edge_to_change_weight.size() % 2 == 1 && edge_to_change_weight.get(edge_to_change_weight.size() - 1) == point) {
                               
                                
                                edge_to_change_weight.remove(edge_to_change_weight.size() - 1);
                                
                            } else {
                                
                                // Handling the activity of changing wights by the user
                                edge_to_change_weight.add(point);
                                if (edge_to_change_weight.size() == 2) {
                                    System.out.println(Integer.parseInt(panel.weight.getText().toString()));
                                    RandomWeights.adjacent_structure[pairs.indexOf(edge_to_change_weight.get(0))][pairs.indexOf(edge_to_change_weight.get(1))] = Integer.parseInt(panel.weight.getText().toString());
                                    RandomWeights.adjacent_structure[pairs.indexOf(edge_to_change_weight.get(1))][pairs.indexOf(edge_to_change_weight.get(0))] = Integer.parseInt(panel.weight.getText().toString());
                                    edge_to_change_weight.clear();
                                }
                            }
                        }
                    }
                } else if (panel.ShortestPath.isSelected()) {//Checking whether the user is wishing to see the shortest possible path between two points
                    int i = 0;

                    for (Point point : pairs) {
                        ++i;
                           // Getting the node around which the user has clicked
                        if ((e.getX() >= point.x - 20 && e.getX() <= point.x + 20)
                                && (e.getY() >= point.y - 20 && e.getY() <= point.y + 20)) {
                            if (shortest_path_pairs.size() % 2 == 1 && shortest_path_pairs.get(shortest_path_pairs.size() - 1) == point) {
                                shortest_path_pairs.remove(shortest_path_pairs.size() - 1);
                                System.out.println("Rmoved the shortest Pair");
                            } else {
                                // Handling the process in which user wish to calculate the shortest path between two selected points
                                shortest_path_pairs.add(point);
                                if (shortest_path_pairs.size() == 2) {
                                    MSTandShortestPath.panel.dijkstra(RandomWeights.adjacent_structure, pairs.indexOf(shortest_path_pairs.get(0)), pairs.indexOf(shortest_path_pairs.get(1)));
                                    shortest_path_pairs.clear();

                                    clear_dspathNodes_flag = true;

                                    System.out.println();

                                    System.out.println("Clearing the structure for storing the shortest veritices");
                                }
                            }
                        }
                    }
                } else if (panel.AddEdge.isSelected()) {//Handlign the case in which user wish to add an edge between two selected points

                    int i = 0;
                    for (Point point : pairs) {
                        ++i;
                         // Getting the node around which the user has clicked
                        if ((e.getX() >= point.x - 20 && e.getX() <= point.x + 20)
                                && (e.getY() >= point.y - 20 && e.getY() <= point.y + 20)) {
                            
                            //Checking whether the user has clicked the same node twice
                            if (selectedPairs.size() % 2 == 1 && selectedPairs.get(selectedPairs.size() - 1) == point) {
                                selectedPairs.remove(selectedPairs.size() - 1);
                                System.out.println("Removed");
                            } else {
                               assign_weights_flag = true;
                                int arr[][] = new int[pairs.size()][pairs.size()];
                                try{
                                for(int j = 0 ; j < pairs.size(); j++){
                                    for(int k = 0 ; k < pairs.size(); k++){
                                        arr[j][k] = RandomWeights.adjacent_structure[j][k];
                                        
                                    }
                                }
                                }catch(Exception ex){
                                    //RandomWeights.adjacent_structure = arr;
                                }
                                 
                                RandomWeights.adjacent_structure = arr;

                              
                               
                                
                                selectedPairs.add(point);
                                vertices_list.add(pairs.indexOf(point));

                            }

                        }

                    }
                   
                } else {
                   
                    pairs.add(new Point(e.getX(), e.getY()));
                }
                repaint();
                //prints the x and y cordinate just for information purposes
                System.out.println("X " + e.getX() + " y " + e.getY());

            }

        });

    }
    //Function to redraw the GUI
    public void reDrawPicture() {
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);// Changing the color for drawing gui
        //Display the changed weight if the user has triggered it
        if (panel.Change_a_weight_to.isSelected()) {
            for (Point point : pairs) {

                try {
                    //Displaying the selected point green
                    if (edge_to_change_weight.get(edge_to_change_weight.size() - 1) == point && edge_to_change_weight.size() == 1) {

                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.RED);
                    }
                } catch (Exception ex) {

                }
                g.fillOval(point.x - 9, point.y - 6, 18, 18);

                //            g.hitClip(point.x-9, point.y-6, 18, 18);
            }
        } else if (panel.ShortestPath.isSelected()) {// Dispalying the case in which user want to see the shortest path between two points
            for (Point point : pairs) {
                // Displaying the selected point in green
                try {
                    if (shortest_path_pairs.get(shortest_path_pairs.size() - 1) == point && shortest_path_pairs.size() == 1) {

                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.RED);
                    }
                } catch (Exception ex) {

                }
                g.fillOval(point.x - 9, point.y - 6, 18, 18);// Drawing point

            }
        } else {
            //Default case to draw the point where the user has clicked
            for (Point point : pairs) {
                try {
                    if (selectedPairs.get(selectedPairs.size() - 1) == point && selectedPairs.size() % 2 == 1) {
                        g.setColor(Color.GREEN);
                    } else {
                        g.setColor(Color.RED);
                    }
                } catch (Exception ex) {

                }
                g.fillOval(point.x - 9, point.y - 6, 18, 18);

            }
        }

        g.setColor(Color.BLUE);
        //Drawing the Edges 
        for (int i = 1; i < selectedPairs.size(); i += 2) {
            //if(i != 0){

            g.drawLine(selectedPairs.get(i - 1).x, selectedPairs.get(i - 1).y, selectedPairs.get(i).x, selectedPairs.get(i).y);
         
            if (assign_weights_flag) {
                try {
                    g.drawString(Integer.toString(RandomWeights.adjacent_structure[vertices_list.get(i - 1)][vertices_list.get(i)]), (selectedPairs.get(i - 1).x + selectedPairs.get(i).x) / 2 + 5, (selectedPairs.get(i - 1).y + selectedPairs.get(i).y) / 2 + 5);
                } catch (Exception ex) {

                }
            }
            //}

        }

        for (Integer a : dspathNodes) {
            System.out.print(a + "->");
        }
        int j = 0;
        g.setColor(Color.GREEN);

        //Drawing the shortest path between two points in green
        try {
            for (int i = 0; i < dspathNodes.size() - 1; i++) {
                j = i + 1;
                g.drawLine(pairs.get(dspathNodes.get(i)).x, pairs.get(dspathNodes.get(i)).y, pairs.get(dspathNodes.get(j)).x, pairs.get(dspathNodes.get(j)).y);
                //for(int a  = 0 ; a < vertices_list.size();a+=2){
                //  System.out.println("(" + vertices_list.get(i-1) + "," + vertices_list.get(i) + ")");
                // }

            }
        } catch (Exception ex) {

        }
        if (clear_dspathNodes_flag) {
            dspathNodes.clear();
            clear_dspathNodes_flag = false;
        }
        g.setColor(Color.red);
        
                 g.setColor(Color.GREEN);
        if(panel.MoveVertex.isSelected()){
            if(vertex_to_be_moved.size() == 1){
                            //Default case to draw the point where the user has clicked
            for (int i = 0 ; i < pairs.size(); i++) {
                    if(i == vertex_to_be_moved.get(0)){
                        g.fillOval(pairs.get(i).x - 9, pairs.get(i).y - 6, 18, 18);
                    }
                }
            }else{
               
                //panel.MoveVertex.setSelected(false);
                //panel.AddVertex.setSelected(true);
               
                System.out.println("called");
            }
        }
        g.setColor(Color.RED);
        
        //Drawing the minimum spanning tree
        if (mst_flag) {
            g.setColor(Color.GREEN);

            for (int i = 1; i < mstPairs.size(); i += 2) {
                //if(i != 0){
                g.drawLine(mstPairs.get(i - 1).x, mstPairs.get(i - 1).y, mstPairs.get(i).x, mstPairs.get(i).y);
                
            }

        }

     
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {

            }

        });

        mst_flag = false;
    }

}
