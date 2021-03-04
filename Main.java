package project2;

import java.io.BufferedReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

class Graph {
	ArrayList<Vertex> vertexArrayList = new ArrayList<Vertex>();
	ArrayList<String> vertexNames = new ArrayList<String>();
	 
	
	private int vertexSize;
	private int edgeSize;
	private int vertexNum;
	private int edgeNum;
	private String sourceVertex;
	
	public Graph(){
		vertexNum=0;
		edgeNum = 0;
		vertexSize =0;
		edgeSize=0;
	}
	
	public boolean addVertex(String name) {
		if (!vertexNames.contains(name) && name != null) {
			
			vertexNames.add(name);
			Vertex addVertex = new Vertex(name,vertexNum);
			vertexNum++;
			vertexArrayList.add(addVertex);
			vertexSize++;
			
			return true;
		}
		return false;
	}
	public boolean addEdge(String toVertex, String fromVertex, int weight){
		
		int w = weight;
		
		if(!vertexNames.contains(fromVertex)||!vertexNames.contains(toVertex)){return false;} //Check if both Vertices exists
		
		
		Vertex hold_f= vertexArrayList.get(vertexNames.indexOf(fromVertex)); //Vertex that the edge is from
		Vertex hold_t = vertexArrayList.get(vertexNames.indexOf(toVertex)); //Vertex that the edge is going to
		
		
		if(!hold_f.checkDestExistAlready(hold_t)){
			
			Edges newEdge = new Edges(hold_t, hold_t, w, edgeNum);
			edgeNum++;
			hold_f.addEdge(newEdge);
			edgeSize++;
			hold_t.incVertexIn();
			return true;
		}else{
			return false;
		}
	}
	public void print(){
		for(int i = 0;i<vertexArrayList.size();i++){
			System.out.println("("+vertexArrayList.get(i).getSerialNum()+") "+vertexArrayList.get(i).getName());
			if(vertexArrayList.get(i).hasEdge()){
				
				for(int k = 0; k<vertexArrayList.get(i).numEdgesOutOf();k++){
					System.out.print("   (");
					System.out.print(vertexArrayList.get(i).returnEdge(k).getSN()+")"); //print serial number of edge
					if(vertexArrayList.get(i).returnEdge(k).hasLabel()){
						
						
						System.out.print("("+vertexArrayList.get(i).returnEdge(k).getLabel()+")");
					}
					System.out.print("--->");
					System.out.println(vertexArrayList.get(i).returnEdge(k).getTo().getName());
				}
			}
		}
	}
	public void printSize(){
		System.out.println("Vertex Size "+ vertexSize);
		System.out.println("Edge Size "+edgeSize);
	}
	
	public void printVertex(String nameTP){
		if(vertexNames.contains(nameTP)){
			Vertex temp = vertexArrayList.get(vertexNames.indexOf(nameTP));
			
			System.out.println("("+temp.getSerialNum()+")"+temp.getName());
			
			if(temp.hasEdge()){
				
				for(int k = 0; k<temp.numEdgesOutOf();k++){
					System.out.print("   (");
					System.out.print(temp.returnEdge(k).getSN()+")"); //print serial number of edge
					if(temp.returnEdge(k).hasLabel()){
						
						
						System.out.print("("+temp.returnEdge(k).getLabel()+")");
					}
					System.out.print("--->");
					System.out.println(temp.returnEdge(k).getTo().getName());
				}
			}
		}else{System.out.println("Does not have vertex");}
	}
	public boolean deleteEdge(){
		Scanner s = new Scanner(System.in);
		System.out.println("From Vertex");
		String FN = s.nextLine();
		System.out.println("To Vertex");
		String TN = s.nextLine();
		if(!vertexNames.contains(FN)||!vertexNames.contains(TN)){return false;} //Check if both vertices exists
		else{
			Vertex holder = vertexArrayList.get(vertexNames.indexOf(FN));
			Vertex tooVertex = vertexArrayList.get(vertexNames.indexOf(TN));
			edgeSize--;
			return holder.removeEdge(tooVertex);
		}
		
		
	}
	
	public boolean deleteVertex(String nameToDel){
		if(!vertexNames.contains(nameToDel)){return false;} //Check if Vertex Exists
		
		
		Vertex vertexToDelete = vertexArrayList.get(vertexNames.indexOf(nameToDel));
		for(int i = 0;i<vertexArrayList.size();i++){
			if(vertexArrayList.get(i).hasEdge()){
				vertexArrayList.get(i).removeEdge(vertexToDelete);
			}
		}
		
		vertexToDelete.decVertexInEdgesOut();
		
		
		vertexArrayList.remove(vertexNames.indexOf(nameToDel));
		vertexNames.remove(nameToDel);
		return true;
	}
	
	public void topSort(){
		Queue holder = new Queue();
		int ha = vertexArrayList.size();
		int iter =0;
		
		while(iter<ha){
			for(int i=0;i<vertexArrayList.size();i++){
				if(vertexArrayList.get(i).getInDeg()==0){
					
					holder.enque(vertexArrayList.get(i));
					
					this.deleteVertex(vertexArrayList.get(i).getName());
				}
			}
			iter++;
		}
		
		if(holder.size==ha){
			
			int counter = 0;
			while (!holder.empty()){
				System.out.print(holder.deque());
				counter++;
				if(holder.size>0){
					System.out.print(", ");
				}
				if(counter==6){
					System.out.println();
					counter=0;
				}
			}
		}else{
			System.out.println("Topological Sort");
			System.out.println("Cycle found... no sort possible");
		}
		
	}
	public void identifySource(String s){
		sourceVertex = s;
	}

	public void printWithInDeg(){
		for(int i =0;i<vertexArrayList.size();i++){
			
			System.out.println(vertexArrayList.get(i).getName()+" "+vertexArrayList.get(i).getInDeg());
			
		}
	}
	public void shortestPath(){
		PriorityQueue<Integer> pq = new PriorityQueue<Integer>(vertexArrayList.size());
		ArrayList<Integer>key = new ArrayList<Integer>();
		ArrayList<Vertex>val = new ArrayList<Vertex>();
		vertexArrayList.get(vertexNames.indexOf(sourceVertex)).setShortestPathFromSource(0); //From Source to Source length is 0
		pq.add(vertexArrayList.get(vertexNames.indexOf(sourceVertex)).getShortestPathFromSource()); //Source vertex put in the queue
		key.add(0);
		val.add(vertexArrayList.get(vertexNames.indexOf(sourceVertex)));
		while(!pq.isEmpty()){
			int s = pq.peek();
			pq.remove();
			int inde = key.indexOf(s);
			key.remove(inde);
			Vertex n = val.get(inde);
			val.remove(inde);
			
			if(n.handled()){continue;}
			n.handle();
			
			for(int i=0;i<n.numEdgesOutOf();i++){
				
				if(!n.returnEdge(i).getTo().handled()){
					int pathLength = n.getShortestPathFromSource()+n.returnEdge(i).getWeight();
					if(pathLength<n.returnEdge(i).getTo().getShortestPathFromSource()){
						n.returnEdge(i).getTo().setShortestPathFromSource(pathLength);
						n.returnEdge(i).getTo().setPred(n);
						pq.add(pathLength);
						key.add(pathLength);
						val.add(n.returnEdge(i).getTo());
					}
				}
			}
		}
		System.out.println("Shortest Paths from source vertex ("
				+vertexArrayList.get(vertexNames.indexOf(sourceVertex)).getSerialNum() +") "
				+ vertexArrayList.get(vertexNames.indexOf(sourceVertex)).getName() +" to :");
		for(int k = 0;k<vertexArrayList.size();k++){
			
			System.out.print("("+vertexArrayList.get(k).getSerialNum()+") ");
			System.out.print(vertexArrayList.get(k).getName()+": ");
			if(vertexArrayList.get(k).getShortestPathFromSource()==Integer.MAX_VALUE){
				System.out.print("no path");
				}else{
				System.out.print(vertexArrayList.get(k).getShortestPathFromSource());
				Vertex n = vertexArrayList.get(k);
				System.out.print(" (");
				while(true){
					System.out.print(n.getName());
					n = n.getPred();
					if(n==null){
						break;
					}
					System.out.print(", ");
				}
				System.out.print(")");
			}
			System.out.println();
		}
	}
}
class Queue {
	Vertex[] vertixNames;
	int front;
	int back;
	int size;

	public Queue() {
		vertixNames = new Vertex[10];
		front = 0;
		back = 0;
	}

	public void enque(Vertex n) {
		if (size == 0) {
			vertixNames[front] = n;
			
		} else {
			if (size == vertixNames.length) {
				Vertex[] array2 = new Vertex[size * 2];
				for (int i = 0; i < size; i++) {
					array2[i] = vertixNames[i];
					vertixNames = array2;
				}
			}
			vertixNames[back+1]=n;
			back++;
		}
		size++;
	}

	public String deque() {
		Vertex n;
		if(size==0){
			n = null;
		}
		else if(front==back){
			n = vertixNames[front];
			size--;
		}else{
			n = vertixNames[front];
			front++;
			size--;
		}
		
		return "("+n.getSerialNum()+")"+n.getName();
		
	}
	public boolean empty(){
		return (size<=0);
	}
	public void printQueue(){
		for(int i =0;i<vertixNames.length;i++){
			System.out.println(vertixNames[i].getName());
		}
	}
}






class Edges {
	private Vertex from;
	private Vertex to;
	private int weight;
	private int serialNum;
	private String label;
	
	public Edges(Vertex f, Vertex t, int w, int sn){
		from = f;
		to = t;
		weight = w;
		serialNum = sn;
		
	}
	public Vertex getfrom(){
		return from;
	}
	public Vertex getTo(){
		return to;
	}
	public int getWeight(){
		return weight;
	}
	public int getSN(){
		return serialNum;
	}
	public String getLabel(){
		return label;
	}
	public boolean hasLabel(){
		if(label==null){
			return false;
		}
		return true;
	}
}


class Vertex {
	
	private String name;
	private Vertex next;
	private int serialNumber;
	private Edges adj;
	private String label;
	private int inDeg;
	private int shortestPathFromSource;
	private Vertex pred;
	private boolean handled;
	private ArrayList<Edges> edgeHolder = new ArrayList<Edges>();
	
	public Vertex(String name, int sn){
		this.name=name;
		this.serialNumber = sn;
		this.shortestPathFromSource= Integer.MAX_VALUE;
		pred = null;
		handled = false;
		
	}
	public boolean handled(){
		return handled;
	}
	public void handle(){
		handled = true;
	}
	public int getShortestPathFromSource(){
		return shortestPathFromSource;
	}
	public void setShortestPathFromSource(int s){
		shortestPathFromSource = s;
	}
	public void setPred(Vertex p){
		pred=p;
	}
	public Vertex getPred(){
		return pred;
	}
	public int getSerialNum(){
		return serialNumber;
	}
	public void setNext(Vertex next){
		this.next=next;
	}
	public String getName(){
		return name;
	}
	public String getLabel(){
		if(label=="null");{
			label = "No Label was entered";
		}
		return label;
	}
	public void addEdge(Edges e){
		edgeHolder.add(e);
	}
	public int numEdgesOutOf(){
		return edgeHolder.size();
	}
	public boolean checkDestExistAlready(Vertex too){
		
		boolean ret = false;
		for(int i =0;i<edgeHolder.size();i++){
			if(edgeHolder.get(i).getTo().getName().equals(too.getName())){
				ret = true;
			}
		}
		return ret;
	}
	public boolean hasEdge(){
		if(edgeHolder.size()>0){
			return true;
		}
		return false;
	}
	public Edges returnEdge(int i ){
		return edgeHolder.get(i);
	}
	public boolean removeEdge(Vertex deleteVertex){
		for(int i = 0;i<edgeHolder.size();i++){
			if(deleteVertex.name.equals(edgeHolder.get(i).getTo().name)){
				
				deleteVertex.decVertexIn();
				
				edgeHolder.remove(edgeHolder.get(i));
				
				return true;
			}
		}
		return false;
	}
	public void incVertexIn(){
		inDeg++;
	}
	public void decVertexIn(){
		if(inDeg>0){
			inDeg--;
		}
	}
	public int getInDeg(){
		return inDeg;
	}
	public void decVertexInEdgesOut(){
		for(int i =0;i<edgeHolder.size();i++){
			edgeHolder.get(i).getTo().decVertexIn();
		}
	}
}


public class Main {

	public static void main(String[] args) {
		ArrayList<String> listOfStrings = new ArrayList<String>();
		String fileName = "data.txt";
		String line = null;
		final long startTime = System.currentTimeMillis();
		Graph m = new Graph();
		try {
			
			FileReader fileReader = new FileReader(fileName);

			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while ((line = bufferedReader.readLine()) != null) {
				line = line.trim();
				listOfStrings.add(line);
			}		
			for(int i =0;i<listOfStrings.size();i++){
				if(listOfStrings.get(i).length()<=0){
					continue;
				}
				if(listOfStrings.get(i).equals("-1")){
					
					i++;//Single source index
					m.identifySource(listOfStrings.get(i));
					i++;// //======== index]
					System.out.println("########################################");
					m.print();
					System.out.println();
					//m.topSort();
					m.shortestPath();
					System.out.println();
					m = new Graph();
				}else{
			 		String[] s = listOfStrings.get(i).split("\\s+");
					m.addVertex(s[0]);
					m.addVertex(s[1]);
					m.addEdge(s[1], s[0], Integer.parseInt(s[2]));
				}
			}
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
		final long endTime = System.currentTimeMillis();
		System.out.println("The execution time for the algorithm is: " + (endTime - startTime) + " millisecond(s).");
	}
}


