import java.util.ArrayList;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

/*
 * quadTree
 * v1.0
 * Author: James Liang
 * Date: 10/30/18
 * Description: Organizes primary grids by splitting into a quadTree structure - collection of 2x2 grids
 */

public class quadTree<E> {
	
	private Node root;
	int maxBoundX;
	int maxBoundY;
	
	/**
	 * quadTree Constructor
	 * 
	 * Initializes with the dimensions of the grid
	 * 
	 * @param maxBX
	 * @param maxBY
	 */
	
	quadTree(int maxBX, int maxBY){
		this.maxBoundX = maxBX;
		this.maxBoundY = maxBY;
		root = new Node(0,0,this.maxBoundX, this.maxBoundY, 0);
	}
	
	/**
	 * addAgent
	 * Calls agentC() method so that method may be used in main
	 * 
	 * @param agent
	 */
	
	public void addAgent(Shape agent) {
		root = add(this.root, agent);
	}
	
	/**
	 * agentC
	 * Returns the number of shapes/agents contained within the quadTree
	 * 
	 * @return agentList.size
	 */
	
	public int agentC() {
		return root.agentList.size();
	}
	
	/**
	 * drawGrid
	 * Draws the quadTree (called in main) - can't reference root node in main
	 * 
	 * @param g
	 */
	
	public void drawGrid(Graphics g) {
		
		draw(g, root);
		
	}
	
	/**
	 * draw
	 * Draws the quadTree recursively, used in drawGrid.
	 * 
	 * @param g
	 * @param root
	 */
	
	public void draw(Graphics g, Node root) {
		
		g.drawLine(root.lowerBoundX, root.lowerBoundY, root.lowerBoundX, root.higherBoundY);
		g.drawLine(root.lowerBoundX, root.lowerBoundY, root.higherBoundX, root.lowerBoundY);
		g.drawLine(root.higherBoundX, root.lowerBoundY, root.higherBoundX, root.higherBoundY);
		g.drawLine(root.lowerBoundX, root.higherBoundY, root.higherBoundX, root.higherBoundY);
		
		g.setColor(Color.BLACK);
		
		if (root.q1 != null) {
			draw(g, root.q1);
			draw(g, root.q2);
			draw(g, root.q3);
			draw(g, root.q4);
			
		}
		
	}
	
	/**
	 * removeAgent
	 * Removes agent from subtree, called from main
	 * 
	 * @param agent
	 */
	
	public void removeAgent(Shape agent) {
		root = remove(root, agent);
	}
	
	/**
	 * remove
	 * Removes agent from subtree recursively
	 * 
	 * @param root
	 * @param agent
	 * @return
	 */
	
	public Node remove(Node root, Shape agent) {
		
		root.agentList.remove(agent);
		
		if (root.q1 != null) {
			root.q1 = remove(root.q1, agent);
		} 
		
		if (root.q2 != null) {
			root.q2 = remove(root.q2, agent);
		}

		if (root.q3 != null) {
			root.q3 = remove(root.q3, agent);
		}
		
		if (root.q4 != null) {
			root.q4 = remove(root.q4, agent);
		}
		
		return root;
		
		
	}
	
	
	@Deprecated
	public void display() {
		displayAgent(root);
	}
	
	
	@Deprecated
	public void displayAgent(Node root) {

		System.out.println(root.agentList.size()+" SIZE");
		
		if (root.q1 != null){
			displayAgent(root.q1);
			displayAgent(root.q2);
			displayAgent(root.q3);
			displayAgent(root.q4);
		}
		
		return;
	}
	
	/**
	 * add
	 * Adds agent to quadTree. Recursively traverses quadTree with DFS, and will add to any quadTree node that intersects with inserted shape.
	 * 
	 * @param root
	 * @param agent
	 * @return
	 */
	
	public Node add(Node root, Shape agent) {
		
		if (agent.intersects(root.boundingBox)) {
			root.addList(agent);
		}
		
		if (root.q1 != null) {
			root.q1 = add(root.q1, agent);
		} 
		
		if (root.q2 != null) {
			root.q2 = add(root.q2, agent);
		}

		if (root.q3 != null) {
			root.q3 = add(root.q3, agent);
		}
		
		if (root.q4 != null) {
			root.q4 = add(root.q4, agent);
		}
		
		return root;
		
	}
	
	/**
	 * updateTree
	 * 
	 * Uses a combination of methods to update the overall gridTree
	 * Functions include: updating shape collisions, updating shape placement in quadTree nodes, creating new grids
	 */
	
	public void updateTree() {
		ArrayList<Shape> removeList = findChanged(root); //Finds displaced nodes
		
		root = manageNode(root, removeList); //Updates shape placement in Nodes
		
		root = update(root); //Creates new quadTree branches, removes old ones
		
		root = checkCollide(root); //Updates speed properties of shapes - updates collisions
		
		//display();
	}
	
	/**
	 * getList
	 * Returns an arrayList of all the shapes/agents in a quadtree.
	 * 
	 * @return
	 */
	
	public ArrayList<Shape> getList(){
		return root.agentList;
	}
	
	/**
	 * update
	 * If the number of shapes in a certain node exceeds a threshold (in this case 5), then split the node into 4 more subnodes
	 * Viceversa, if a non-leaf node contains less than 5 shapes, then delete all of its subnodes
	 * 
	 * @param root
	 * @return
	 */
	
	public Node update(Node root) {
		
		if (root.listLen() >= 5) {
			if (root.q1 == null) {
				
				if (root.levelCounter <= 5) {
				
					int midY = (int)Math.round((root.higherBoundY-root.lowerBoundY)/2)+root.lowerBoundY;
					int midX = (int)Math.round((root.higherBoundX-root.lowerBoundX)/2)+root.lowerBoundX;
			
					//Creates new nodes by dividing up the dimensions of the current node
					
					root.q1 = new Node(midX, root.lowerBoundY, root.higherBoundX, midY, root.levelCounter + 1);
					root.q2 = new Node(root.lowerBoundX, root.lowerBoundY, midX, midY, root.levelCounter + 1);
					root.q3 = new Node(root.lowerBoundX, midY, midX, root.higherBoundY, root.levelCounter + 1);
					root.q4 = new Node(midX, midY, root.higherBoundX, root.higherBoundY, root.levelCounter + 1);
					root.split(); //Distributes the shapes inside the current root into the subnodes
					
				}
					
				return root;
				
			} else {
				root.q1 = update(root.q1);
				root.q2 = update(root.q2);
				root.q3 = update(root.q3);
				root.q4 = update(root.q4);
			}
		} else {
			//If agentList falls below threshold, and is not a leafNode, then nullify subNodes
			if (root.q1 != null) {
				root.q1 = null;
				root.q2 = null;
				root.q3 = null;
				root.q4 = null;
				return root;
			}
		}
		
		return root;
	
	}
	
	/**
	 * findChanged
	 * Determines which shapes have been displaced from their native node - if a shape moves outside a node, it will be part of the returned arrayList
	 * 
	 * @param root
	 * @return
	 */
	
	public ArrayList<Shape> findChanged(Node root){
	
		if (root.q1 == null) {
			
			ArrayList<Shape> removeList = new ArrayList<Shape>();

			for (int i = 0; i < root.agentList.size(); i++) {
				if (root.agentList.get(i).intersects(root.boundingBox) == false) { //If no intersection occurs at a leaf node, then my shape must be displaced
					removeList.add(root.agentList.get(i));
				}
			}
			
			return removeList;
			
		}
		
		ArrayList<Shape> changedAgent = new ArrayList<Shape>();
		
		//Changed agent stacks the arrayLists of all the root's subnodes
		
		changedAgent.addAll(findChanged(root.q1));
		changedAgent.addAll(findChanged(root.q2));
		changedAgent.addAll(findChanged(root.q3));
		changedAgent.addAll(findChanged(root.q4));
		
		return changedAgent;
		
	}
	
	/**
	 * manageNode
	 * 
	 * Using the list of displacedNodes (found in the previous method), remove all, and subsequently add.
	 * Add function naturally adds the shape to all the nodes that intersect with it, so positions of all shapes relative to nodes will be updated.
	 * 
	 * @param root
	 * @param removeList
	 * @return
	 */
	
	public Node manageNode(Node root, ArrayList<Shape> removeList) {
		
		for (int i = 0; i < removeList.size(); i++) {
			root = remove(root, removeList.get(i));
			root = add(root, removeList.get(i));
		}
		
		return root;
		
	}

	/**
	 * checkCollide
	 * Using the intersection function contained within shape, determine which shapes intersect
	 * If shapes intersect, use a reorientation method alongside velocity distribution functions to determine what to do (further elaborated on below)
	 * Note: Shape comparison only occurs within individual nodes, so 2 intersection shapes that belong to adjacent nodes will not be considered to intersect
	 * 
	 * @param root
	 * @return
	 */
	
	public Node checkCollide(Node root) {
		
		if (root.q1 != null) { //Recursive traversal
			root.q1 = checkCollide(root.q1);
			root.q2 = checkCollide(root.q2);
			root.q3 = checkCollide(root.q3);
			root.q4 = checkCollide(root.q4);
		} else { //Checks only leaf nodes
			for (int i = 0; i < root.agentList.size(); i++) { //
				for (int a = i+1; a < root.agentList.size(); a++) {
					if (root.agentList.get(i).intersects(root.agentList.get(a))) {
						
						int dx1 = root.agentList.get(i).getDx();
						int dy1 = root.agentList.get(i).getDy();
						int dx2 = root.agentList.get(a).getDx();
						int dy2 = root.agentList.get(a).getDy();
						
						Shape ag1 = root.agentList.get(i);
						Shape ag2 = root.agentList.get(a);
						
						if (ag1.getX() < ag2.getX()) { //If on left side of other shape, left shape must go left
							
							ag1.setDx(-Math.abs(dx1));
							ag2.setDx(Math.abs(dx2));
							
							root.agentList.set(i, ag1);
							root.agentList.set(a, ag2);
			
						} else if (ag1.getX() > ag2.getX()) { //If on right side (again, ag1) of other shape, right shape must go right
							
							ag1.setDx(Math.abs(dx1));
							ag2.setDx(-Math.abs(dx2));
							
							root.agentList.set(i, ag1);
							root.agentList.set(a, ag2);
							
						}
						
						if (ag1.getY() < ag2.getY()) { //Same logic for x - if below other shape, go down
							
							ag1.setDy(-Math.abs(dy1));
							ag2.setDy(Math.abs(dy2));
							
							root.agentList.set(i, ag1);
							root.agentList.set(a, ag2);
							
						} else if (ag1.getY() > ag2.getY()){ //If above other shape, go up
							
							ag1.setDy(Math.abs(dy1));
							ag2.setDy(-Math.abs(dy2));
							
							root.agentList.set(i, ag1);
							root.agentList.set(a, ag2);
							
						}
							
						if (dx1 == 0 && dy1 == 0) { //Distribution of velocity for a single moving mass colliding with stationary mass (mass means shape)
							
							ag1.setDx(.7*dx2);
							ag1.setDy(.7*dy2);
							ag2.setDx(.3*dx2);
							ag2.setDy(.3*dy2);
							
						} else if (dx2 == 0 && dy2 == 0) { //Same logic
						
							ag1.setDx(.3*dx1);
							ag1.setDy(.3*dy1);
							ag2.setDx(.7*dx1);
							ag2.setDy(.7*dy1);
							
						} else { //Distribution of velocity of a mass intersecting with a mass that has a horizontal or vertical trajectory
						
							if (dx1 == 0) {
								ag1.setDx(.7*dx2);
								ag2.setDx(.3*dx2);
								root.agentList.set(i, ag1);
								root.agentList.set(a, ag2);
							} else if (dx2 == 0) {
								ag1.setDx(.3*dx1);
								ag2.setDx(.7*dx1); 
								root.agentList.set(i, ag1);
								root.agentList.set(a, ag2);
							}
							
							if (dy1 == 0) {
								ag1.setDy(.7*dy2);
								ag2.setDy(.3*dy2); 
								root.agentList.set(i, ag1);
								root.agentList.set(a, ag2);
							} else if (dy2 == 0) {
								ag1.setDy(.3*dy1);
								ag2.setDy(.7*dy1);
								root.agentList.set(i, ag1);
								root.agentList.set(a, ag2);
							}
						}
						
					}
				}
			}
		}
		
		return root;
		
	}
	

	
	private class Node{ //Private contained nodeClass (nodes of the subtree)
		
		public int lowerBoundX;
		public int lowerBoundY;
		public int higherBoundX;
		public int higherBoundY;
		private Rectangle boundingBox;
		
		public int levelCounter = 0; // "Levels" of nodes - prevents infinite recursion of created nodes
		public Node q1;
		public Node q2;
		public Node q3;
		public Node q4;
		
		ArrayList<Shape> agentList = new ArrayList<Shape>(); //ArrayList of all shapes within node
		
		/**
		 * Constructor for Node
		 * Defines the nth iteration of this node, alongside the dimensions
		 * 
		 * @param lowerBoundX
		 * @param lowerBoundY
		 * @param higherBoundX
		 * @param higherBoundY
		 * @param levelCounter
		 */
		
		Node(int lowerBoundX, int lowerBoundY, int higherBoundX, int higherBoundY, int levelCounter){
			this.levelCounter = levelCounter;
			this.lowerBoundX = lowerBoundX;
			this.lowerBoundY = lowerBoundY;
			this.higherBoundX = higherBoundX;
			this.higherBoundY = higherBoundY;
			boundingBox = new Rectangle(lowerBoundX, lowerBoundY, higherBoundX-lowerBoundX, higherBoundY-lowerBoundY);
		}
		
		/**
		 * split
		 * Distributes shapes in current nodes to subnodes
		 * Loops through agentList, adds to a subnode (only one!) if an agent/shape intersects with a subnode
		 */
		
		public void split() {
			
			for (int i = 0; i < agentList.size(); i++) {
				if (agentList.get(i).intersects(q1.boundingBox)) {
					q1.addList(agentList.get(i));
				} else if (agentList.get(i).intersects(q2.boundingBox)) {
					q2.addList(agentList.get(i));
				} else if (agentList.get(i).intersects(q3.boundingBox)) {
					q3.addList(agentList.get(i));
				} else if (agentList.get(i).intersects(q4.boundingBox)) {
					q4.addList(agentList.get(i));
				}
			}
			
		}

		/**
		 * addList
		 * Adds an agent to agentList
		 * 
		 * @param agent
		 */
		
		public void addList(Shape agent) {
			agentList.add(agent);
		}
		
		/**
		 * listLen
		 * Returns length of arrayList
		 * 
		 * @return
		 */
		
		public int listLen() {
			return agentList.size();
		}
		
		//Node constructor
		
	}
	
}