/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication54;

/**
 *
 * @author ovais
 */
 class Ronaldo {

    private Node[] vertices; // stores the nodes of the graph
    private int size; // number of nodes in the graph
    private MinPriorityQueue queue;
    public int[] ABC=new int[10];

    public Ronaldo(int size) {
        this.size = size;
        vertices = new Node[size];
        addNodes();
        queue = new MinPriorityQueue(size);
    }

    public class Node {
        int name;
        int cost;
        Neighbour neighbourList;
        State state;

        Node(int name) {
            this.name = name;
            state = State.NEW;
            cost = Integer.MAX_VALUE;
        }
    }

    public class Neighbour {
        int index;
        int weight;
        Neighbour next;

        public Neighbour(int index, Neighbour next, int weight) {
            this.index = index;
            this.next = next;
            this.weight = weight;
        }
    }

    private void addNodes() {
        for (int i = 1; i <= size; i++) {
            addNode(i);
        }
    }

    public void addNode(int name) {
        vertices[name - 1] = new Node(name);
    }

    public void addEdge(int sourceName, int destiName, int weight) {
        int srcIndex = sourceName - 1;
        int destiIndex = destiName - 1;
        Node srcNode = vertices[srcIndex];
        Node destiNode = vertices[destiIndex];
        srcNode.neighbourList = new Neighbour(destiIndex, srcNode.neighbourList, weight);
        // the graph is non directional so if from S, D is reachable then vice
        // versa is also true
        destiNode.neighbourList = new Neighbour(srcIndex, destiNode.neighbourList, weight);
    }

    public void computeSortestPathsFrom(int sourceNodeName) {
        for (int i = 0; i < size; i++) {
            if (vertices[i].name == sourceNodeName) {
                applyDijkstraAlgorithm(vertices[i]);
                break;// in this case we need not traverse the nodes which are
                        // not reachable from the source Node
            }
        }
    }

    /**
     * The algorithm is based upon BFS.
     */
    private void applyDijkstraAlgorithm(Node sourceNode) {
        queue.add(sourceNode);
        sourceNode.state = State.IN_Q;
        sourceNode.cost = 0; // cost of reaching Source from Source Node itself
                                // is 0, for all others we still need to
                                // discover the cost so the cost for them has
                                // been already initialized to Integer.MAX_VALUE
        while (!queue.isEmpty()) {
            Node visitedNode = queue.remove();
            visitedNode.state = State.VISITED;
            Neighbour connectedEdge = visitedNode.neighbourList;
            while (connectedEdge != null) {
                Node neighbour = vertices[connectedEdge.index];
                // adding the not enqued neighbor nodes in the queue
                if (neighbour.state == State.NEW) {
                    queue.add(neighbour);
                    neighbour.state = State.IN_Q;
                }
                // updating [relaxing] the costs of each non visited neighbor
                // node if its
                // have been made lesser.
                if (neighbour.state != State.VISITED && ((connectedEdge.weight + visitedNode.cost) < neighbour.cost)) {
                    neighbour.cost = connectedEdge.weight + visitedNode.cost;
                }
                connectedEdge = connectedEdge.next;
            }
        }
        
        //now printing the shortest distances
        for(int i = 0; i < size; i++){
            if(vertices[i].cost != Integer.MAX_VALUE){
                ABC[i]=vertices[i].cost;
                System.out.println("distance from "+sourceNode.name +" to "+vertices[i].name+" is " +vertices[i].cost);
            }else{
                System.out.println(vertices[i].name +" is not reachable from "+sourceNode.name);
            }
        }
    }

    public enum State {
        NEW, IN_Q, VISITED
    };

    /**
     * 
     * This is a simple queue implemented using array. Ideally MinPriority Queue
     * should be implemented using Heap but for making it easy to understand
     * currently implementation of Normal Queue (its remove() method()) has been
     * modified.
     *
     */
    public class MinPriorityQueue {
        Node[] queue;
        int maxSize;
        int rear = -1, front = -1;

        MinPriorityQueue(int maxSize) {
            this.maxSize = maxSize;
            queue = new Node[maxSize];
        }

        public void add(Node node) {
            queue[++rear] = node;
        }

        public Node remove() {
            Node minValuedNode = null;
            int minValue = Integer.MAX_VALUE;
            int minValueIndex = -1;
            front++;
            for (int i = front; i <= rear; i++) {
                if (queue[i].state == State.IN_Q && queue[i].cost < minValue) {
                    minValue = queue[i].cost;
                    minValuedNode = queue[i];
                    minValueIndex = i;
                }
            }

            swap(front, minValueIndex); // this ensures deletion is still done
                                        // from front;
            queue[front] = null;// lets not hold up unnecessary references in
                                // the queue
            return minValuedNode;
        }

        public void swap(int index1, int index2) {
            Node temp = queue[index1];
            queue[index1] = queue[index2];
            queue[index2] = temp;
        }

        public boolean isEmpty() {
            return front == rear;
        }
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JFrame().setVisible(true);
            }
        });
    }
}
