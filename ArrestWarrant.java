package arrestwarrant;

import java.util.*;

public class ArrestWarrant{

    public static int numRoads, numCities, infinity = Integer.MAX_VALUE;
    public static int passengers = 20;
    public static long solution;
 
    public static void main(String[] args){

    	solution = infinity;
        Scanner input = new Scanner(System.in);
        numCities = input.nextInt();
        numRoads = input.nextInt();
                
        ArrayList<Road>[] adjList = new ArrayList[numCities];
        ArrayList<City> cities = new ArrayList();        
        PriorityQueue<Node> pq = new PriorityQueue();
        
        //Initalizing arraylists
        for(int i = 0; i < numCities; i++){

            adjList[i] = new ArrayList<Road>();

        }
        
        for(int i = 0; i < numCities; i++){

            City temp = new City(i,input.nextInt(),input.nextInt());
            
            cities.add(temp);

        }
        
        //Storing graph input in adjList
        for(int i = 0; i < numRoads; i++){
           
            int source = input.nextInt() - 1;
            int dest = input.nextInt() - 1;
            int cost = input.nextInt();
                  
            Road temp1 = new Road(cost,dest);
            Road temp2 = new Road(cost,source);

            adjList[source].add(temp1);
            adjList[dest].add(temp2);
                                                                    
        }
                                             
        City Southchest = cities.get(0); 
        Node curState = Southchest.states[19];
        curState.minCost = 0;       
        pq.add(curState);
        int cityID, curPass, pirates;
        
        //
        while(!pq.isEmpty()){
            
            curState = pq.poll();
            cityID = curState.cityID;
            curPass = curState.numPass;   
            int prevPass = curPass, arrests, bribes, bribeCost, min = 1;
                                                    
            pirates = cities.get(cityID).numPirates;
                
            arrests = 0;
            bribes = pirates;
                                          
            for(int i = 0; i < (pirates+1); i++){
                    
                curPass = prevPass;
                curPass += (bribes-arrests);
                    
                if(cityID == numCities-1)
                    min = 0;
                    
                if(curPass > 20 || curPass < min){
                        
                    bribes--;
                    arrests++;
                    continue;
                        
                }
                                                           
                bribeCost = (bribes*cities.get(cityID).bribeCost);
                    
                if(cityID == numCities - 1){
                        
                    int cost = curState.minCost + bribeCost;
                    if(cost < solution)
                        solution = cost;
                        
                    if(cost < curState.minCost)
                        curState.minCost = cost;
                        
                }
                    
                //Ensuring we don't try to reach another state from the final city with zero pirates left
                if(curPass != 0){
                        
                    for(Road r: adjList[cityID]){                                       
                    
                        int cost = (r.cost*curPass) + bribeCost;
                        Node dest = cities.get(r.dest).states[curPass-1];
                                
                        if(cost + curState.minCost < dest.minCost){
                        
                            dest.minCost = cost + curState.minCost;
                        
                            if(dest.minCost < solution)                    
                                pq.add(dest);
                            
                        }                    
                    }
                    
                }
                                              
                bribes--;
                arrests++;
                    
            }          
                
        }
        
        System.out.println(solution);
            
    }
   
    public static class Road implements Comparable<Road>{

        public int cost, dest;

        @Override
        public int compareTo(Road r) {
                
            return this.cost - r.cost;
                
        }
        
        public Road(int c, int d){
            
            this.cost = c;
            this.dest = d;
            
        }

    }

    public static class City{

	public int ID, numPirates, bribeCost;
	public Node[] states;          

        public City(int i, int p, int b){
            
            this.ID = i;
            this.numPirates = p;
            this.bribeCost = b;

            states = new Node[20];

            for(i = 0; i < 20; i++){
            	states[i] = new Node();
            	states[i].numPass = i+1;
            	states[i].cityID = this.ID;
            	states[i].minCost = infinity;
            }
           
        }
                  
    }

    public static class Node implements Comparable<Node>{

    	public int minCost, numPass, cityID;
    	public boolean visited;

    	@Override
        public int compareTo(Node n) {
           
            return this.minCost - n.minCost;
            
        }

    }
    
}