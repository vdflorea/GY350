
import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.Stack;

public class BadGuy {
	
	Image myImage;
	final int BAD_GUY_WIDTH;
	final int BAD_GUY_HEIGHT;
	int x=0,y=0;
	boolean hasPath=false;
	Stack<Node> finalPath = new Stack<>();

	public BadGuy( Image i ) {
		myImage=i;
		x = 30; // Initial X position of Bad Guy
		y = 10; // Initial Y position of Bad Guy
		BAD_GUY_WIDTH = myImage.getWidth(null);
		BAD_GUY_HEIGHT = myImage.getHeight(null);
	}
	
	public void reCalcPath(boolean map[][],int targx, int targy) {
		// TO DO: calculate A* path to targx,targy, taking account of walls defined in map[][]
		Node[][] nodeArr = new Node[map.length][map.length];
		Node startingNode = null;
		Node targetNode = null;
		finalPath = new Stack<>(); // Re-instantiate (clear) the path from previous calculation

		System.out.println("TARGET X:" +targx);
		System.out.println("TARGET Y:" +targy);

		LinkedList<Node> openList = new LinkedList<>();
		LinkedList<Node> closedList = new LinkedList<>();

		// Refresh nodes to map the game board (including walls that are added)
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[x].length; y++) {
				nodeArr[x][y] = new Node(x, y, map[x][y]);
				Node currNode = nodeArr[x][y];

				// CONDITION 1: Each wall node is unwalkable i.e. place on closed list
				if (currNode.isWall) {
					closedList.add(nodeArr[x][y]);
				}
			}
		}


		startingNode = nodeArr[this.x][this.y];
		targetNode = nodeArr[targx][targy];


		// Calculate f, g and h for neighbours around the starting node
		// g = Cost of getting from a node to one of its neighbours
		// h = Estimated cost of getting from a node to the target node
		// f = g + h (Algorithm's best guess of total cost to get from starting location to target location)
		// parent = The previous node of the current node (through which the current one was discovered)

		openList.add(startingNode); // Add starting node to the open list
		boolean pathExists = false;

		while (!openList.isEmpty()) {
			Node currNode = getNodeWithLowestFScore(openList); // Return node with lowest F score from open list

			System.out.println(targetNode.x);
			System.out.println(targetNode.y);
			// Once we finally reach the target node:
			// -> Push target node and every parent of every node along the path all the way to the start node to the Stack
			if (currNode == targetNode) {
				reconstructPath(currNode, finalPath);
				hasPath = true;
				pathExists = true;
				break;
			}

			// Each node that we expand, add it to the closed list AND remove it from the open list
			openList.remove(currNode);
			closedList.add(currNode);

			for (Node neighbour : getNeighbours(currNode, nodeArr)) { // Implement this method
				if (neighbour.isWall || closedList.contains(neighbour)) {
					continue;
				}

				int tentativeGScore = currNode.g + distanceBetween(currNode, neighbour); // G score we are "uncertain" about
				if (tentativeGScore < neighbour.g) {
					neighbour.parent = currNode;
					neighbour.g = tentativeGScore;
					neighbour.h = distanceBetween(neighbour, targetNode); // Calculate manhattan distance (heuristic) between neighbour node and target node
					neighbour.f = neighbour.g + neighbour.h;

					if (!openList.contains(neighbour)) {
						openList.add(neighbour);
					}
				}
			}
		}

		// Open list is empty and NO path has been found (i.e. no path exists between start and target node)
		if (!pathExists) {
			hasPath = false;
		}
	}

	public Node getNodeWithLowestFScore(LinkedList<Node> openList) {
		Node lowestFNode = openList.getFirst(); // Set initially to first node in open list

		for (Node n : openList) {
			if (n.f < lowestFNode.f) {
				lowestFNode = n;
			}
		}
		return lowestFNode;
	}

	private void reconstructPath(Node targetNode, Stack<Node> finalPath) {
		Node currNode = targetNode;
		while (currNode != null) {
			finalPath.push(currNode);
			currNode = currNode.parent;
		}
	}

	private LinkedList<Node> getNeighbours(Node currNode, Node[][] nodeArr) {
		LinkedList<Node> neighbours = new LinkedList<>();

		// NOTE: Difference in x/y between each Node is always 20
		int[][] directions = { {-20, 0}, {20, 0}, {0, -20}, {0, 20} }; // Up, Down, Left, Right

		for (int[] direction : directions) {
			int newX = currNode.x + direction[0];
			int newY = currNode.y + direction[1];

			if (newX >= 0 && newX < nodeArr.length && newY >= 0 && newY < nodeArr[0].length) { // Handling out of bounds
				neighbours.add(nodeArr[newX][newY]);
			}
		}

		return neighbours;
	}

	private int distanceBetween(Node node1, Node node2) {
		// Calculates manhattan distance between two nodes

		return Math.abs(node1.x - node2.x) + Math.abs(node1.y - node2.y);
	}
	
	public void move(boolean map[][],int targx, int targy) {
		if (hasPath) {
			// TO DO: follow A* path, if we have one defined
			if (!finalPath.isEmpty()) {
				System.out.println(finalPath.size());
				System.out.println("hi");
				Node nextNode = finalPath.pop();
				this.x = nextNode.x;
				this.y = nextNode.y;
			}
		}
		else {
			// no path known, so just do a dumb 'run towards' behaviour
			int newx=x, newy=y;
			if (targx<x)
				newx--;
			else if (targx>x)
				newx++;
			if (targy<y)
				newy--;
			else if (targy>y)
				newy++;
			if (!map[newx][newy]) {
				x=newx;
				y=newy;
			}
		}
	}
	
	public void paint(Graphics g) {
		g.drawImage(myImage, x*20, y*20, null);
	}
	
}

