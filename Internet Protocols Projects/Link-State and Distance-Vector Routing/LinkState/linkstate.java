import java.io.*;
import java.util.*;
class linkstate
{

  public static Cost [][] cost_matrix;
  public static int count = 0;
  public int[] comparison=new int[32];
  
    public static int num_nodes = 0;
   //public static long [] time = new long[32];
  public void dijkstra(Nodes start)
  {
//   long startTime = System.nanoTime();
   // int count = 0;
    //System.out.println("\nsource in dijkstra : " + start.getNodeNo());
    start.distance=0.0;
   // start.parent = start;

    if (cost_matrix[start.getNodeNo()-1][start.getNodeNo()-1] == null)
    {
//        System.out.println("Came in for i "+start.getNodeNo());
	cost_matrix[start.getNodeNo()-1][start.getNodeNo()-1] = new Cost(start.distance,start);
    }
    else
    {
        cost_matrix[start.getNodeNo()-1][start.getNodeNo()-1].min_distance = start.distance;
        cost_matrix[start.getNodeNo()-1][start.getNodeNo()-1].parent = start;
    }

    PriorityQueue<Nodes> NodeQueue = new PriorityQueue<Nodes>();
    long startTime = System.nanoTime();
//    System.out.println(" startTime: " +startTime);
    NodeQueue.add(start);
    while(!NodeQueue.isEmpty())
    {
      Nodes least_cost_node=NodeQueue.poll(); 
      Iterator<Links> itr  = least_cost_node.n_link.iterator();
      while(itr.hasNext())
      {
       Links link = itr.next();
        if (link != null)
        {
	  count++;
	 Nodes n1 = link.neighbor;
	double sum_weight = least_cost_node.distance + link.weight;
	  if(sum_weight<n1.distance)
	  {
	      NodeQueue.remove(n1);
	      comparison[start.getNodeNo()-1]+=1;
	      n1.distance=sum_weight;
	      n1.parent=least_cost_node;
	      NodeQueue.add(n1);
	
	    Nodes temp = n1;
            while(temp.parent.getNodeNo() != start.getNodeNo())
            {
                temp = temp.parent;
            }

  	    if (cost_matrix[start.getNodeNo()-1][n1.getNodeNo()-1] == null)
	    {
	        cost_matrix[start.getNodeNo()-1][n1.getNodeNo()-1] = new Cost(n1.distance,temp);
	    }
	    else
	    {
		cost_matrix[start.getNodeNo()-1][n1.getNodeNo()-1].min_distance = n1.distance;
		cost_matrix[start.getNodeNo()-1][n1.getNodeNo()-1].parent = temp;
	    }
	    
//	    NodeQueue.add(n1);
	 }
	}
      }
    }
    long endTime = System.nanoTime();
  //  System.out.println("endTime: " +endTime);
    long totalTime = (endTime-startTime)/1000;
    //System.out.println(start.getNodeNo());
    //System.out.println(totalTime);
    //time[start.getNodeNo()]=totalTime;
    return;
   }   
   
  public static void main(String[] args) throws Exception
  {
   
    String line;
    
    int i=0,j=0;
    BufferedReader br = new BufferedReader(new FileReader(args[0]));
    num_nodes = Integer.parseInt(br.readLine());
    cost_matrix = new Cost[num_nodes][num_nodes]; 
    System.out.println("Number of nodes is : "+num_nodes);
    
    Nodes[] node_array = new Nodes[num_nodes];
    while(i<num_nodes)
    {
      node_array[i]=new Nodes(i+1);
      i++;
    }
    Double[][] paths;
    paths =new Double[num_nodes][num_nodes];
    
    for(i=0;i<num_nodes;i++)
    {
      for(j=0;j<num_nodes;j++)
      {
	paths[i][j]=Double.POSITIVE_INFINITY;
      }
    }
    
    while((line=br.readLine())!=null)
    {
      String[] parts= line.split(" ");
      int src_node=Integer.parseInt(parts[0]);
      int target_node=Integer.parseInt(parts[1]);
      double weight=Double.parseDouble(parts[2]);
      paths[src_node-1][target_node-1]=weight;
      paths[target_node-1][src_node-1]=weight;
     }
    br.close();
    
    for(i=0;i<num_nodes;i++)
    {
      for(j=0;j<num_nodes;j++)
      {
	if(paths[i][j]!=Double.POSITIVE_INFINITY)
	{
	  node_array[i].n_link.add(new Links(node_array[j],paths[i][j]));	  
	}
      }
    }
    
   // long startTime = System.nanoTime();
    int counter = 0;
    for(i=0,counter=0;counter<num_nodes;counter++)
    {
        linkstate ls = new linkstate();   
	for (j=0;j<num_nodes;j++)
	{
	    node_array[j].distance = Double.POSITIVE_INFINITY;
	    node_array[j].parent = null;
	}

        //long startTime = System.nanoTime();
          ls.dijkstra(node_array[i]);
        //long endTime = System.nanoTime();
        //long totalTime = (endTime - startTime)/1000 ;
	//time[i] = totalTime;
	i++;
	i=i%num_nodes;
	//System.out.println("Came"+count+" "+num_nodes);
     
    }
  //  long endTime = System.nanoTime(); 
/*
    System.out.println("\nCostMatrix consisting of two values: weight and parent of the node\n");
    for (int a = 0; a<num_nodes;a++)
    {
        for (int b=0; b<num_nodes;b++)
        {
            System.out.print(cost_matrix[a][b].min_distance + " " + cost_matrix[a][b].parent.getNodeNo()+ "\t");
        }
	System.out.println("\n\n");
    }
*/
    int node1 = (Integer.parseInt(args[1])) - 1;
    int node2 = (Integer.parseInt(args[2])) -1;
    System.out.println("Cost between node " + args[1] + " to node " + args[2] +" is " + cost_matrix[node1][node2].min_distance);
    System.out.println("\nRouting Table for Node " + args[1] + " :");
    System.out.println("==========================================\n");
    System.out.println("Destination\tCost\tNeighbor"); 
    System.out.println("-----------------------------------------");
    for (int k=0;k<num_nodes;k++)
    {
	int dest = k+1;
	System.out.println(dest + "\t\t" + cost_matrix[node1][k].min_distance + "\t" +cost_matrix[node1][k].parent.getNodeNo()); 
    }

    System.out.println("\n\nRouting Table for Node " + args[2] + " :");
    System.out.println("==========================================\n");
    System.out.println("Destination\tCost\tNeighbor");
    System.out.println("-----------------------------------------");
    for (int k=0;k<num_nodes;k++)
    {
        int dest = k+1;
        System.out.println(dest + "\t\t" + cost_matrix[node2][k].min_distance + "\t" +cost_matrix[node2][k].parent.getNodeNo());
    }

//    long totalTime = (endTime - startTime)/1000 ;
   // System.out.println("\n \nTotal time taken to compute paths between all pairs : " + totalTime + " microseconds");
   /* System.out.println("\n\nTotal time taken in microseconds to compute shortest paths from each node:");
    System.out.println("============================\n");
    System.out.println("Node\t\tTime");
    System.out.println("----------------------------");
    for (int k=0;k<num_nodes;k++)
    {
        int dest = k+1;
        System.out.println(dest + "\t\t" + time[k]);
    }*/
  }
}

