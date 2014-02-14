import java.util.*;
import java.io.*;
import java.net.*;

class Nodes implements Serializable
{
    public double cost;
    public int neighbour_node;

    public Nodes() {
        //this.node_no=node_no;
        neighbour_node=0;
        cost= 0.0;
    }
    
    public Nodes(double cost,int neighbour_node)
    {
      this.cost=cost;
      this.neighbour_node=neighbour_node;
    }
    
    public String toString(boolean last)
    {
      String cost_string = Double.toString(cost);
      String neighbour_string = Integer.toString(neighbour_node);
      String return_string = cost_string + " " + neighbour_string;
      if(!last)
	return_string=return_string+"\n";
      else
	return_string=return_string+" ";
      return return_string;
    }

}

class Client extends DistanceVector_UDP implements Runnable
{
  BufferedReader br;
  DatagramSocket clientSocket;
  String line;
  public static Thread client_thread;
  public Client()
  {
    try
    {
      br = new BufferedReader(new FileReader(filename));
      clientSocket = new DatagramSocket();
    }
    catch(Exception e)
    {
    }
    client_thread = new Thread(this);
    client_thread.start();
  }
  
  
  public synchronized void run()
  {
      
      try
      {
		while((line=br.readLine())!=null)
		{
		    String[] parts= line.split(" ");
		    if(parts.length>0)
		    {
		    int src_node=Integer.parseInt(parts[0]);
		    if(src_node!=my_node)
		    {
			boolean neighbour_flag=false;
			for(i=0;i<neighbours.length;i++)
			{
			  if(src_node==neighbours[i])
			  {
			    neighbour_flag=true;
			  }
			}
			if(neighbour_flag)
			{
			String input_file="input"+Integer.toString(my_node)+".txt";
			File file = new File(input_file);
			if (file.exists()){
			    file.delete();
			}
			InetAddress ip_address = InetAddress.getByName(parts[1]);
			int port_num=Integer.parseInt(parts[2]);
			FileWriter fw = new FileWriter(file);
			for(i=0;i<node_array.length;i++)
			{
			  if(i==node_array.length-1)
			    fw.write(node_array[i].toString(true));
			  else
			    fw.write(node_array[i].toString(false));
			}
			
			fw.close();
			
			byte[] sendData=new byte[(int)file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(sendData);  
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip_address, port_num);
			clientSocket.send(sendPacket);
			}
		    }
		    }
	      }
	    }
      
      catch(Exception e)
      {
      }
    }

}

class Server extends DistanceVector_UDP implements Runnable
{
  DatagramSocket serverSocket;
  byte[] receiveData;
  String line;
  public static Thread server_thread;
  public static  Nodes[][] node_matrix;
  
  public Server()
  {
    try
    {
      serverSocket = new DatagramSocket(serverport);
      
    }
    catch(Exception e)
    {
    }
    node_matrix = new Nodes[2][node_array.length];
    server_thread=new Thread(this);
    server_thread.start();
  }
  
  public synchronized void run()
  {
      while(true)
      {
      try
      {
	  receiveData = new byte[1000];
          serverSocket.setSoTimeout(20000);
	  DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
	  serverSocket.receive(receivePacket);
	  String output_file="output"+Integer.toString(my_node)+".txt";
	  File file = new File(output_file);
	  if (file.exists()){
	      file.delete();
	  }
	  FileOutputStream fos = new FileOutputStream(file);
	  fos.write(receiveData);
	  fos.close();
	  BufferedReader br = new BufferedReader(new FileReader(output_file));
	  int i=0;
	  while((line=br.readLine())!=null)
	  {
	      String[] parts= line.split(" ");
	      double cost=Double.parseDouble(parts[0]);
              
	      int neighbour_node=Integer.parseInt(parts[1]);
	      Nodes n1 = new Nodes(cost,neighbour_node);
	      received_array[i]=n1;
	      i++;
	  }
	  
	  for(i=0;i<node_array.length;i++)
	  {
	    node_matrix[0][i]=node_array[i];
	    node_matrix[1][i]=received_array[i];
	  }
	  //PrintTable();
	  Bellman();
	  //PrintTable();
	  
	  
      }
      catch(SocketTimeoutException ste)
      {
		PrintTable();
		System.exit(0);
      }
      catch(Exception e)
      {
	  e.printStackTrace();
      }
      
      }
   }

    public static void Bellman()
    {
	double oldCost;
        double newCost;
        int i,j,count;
        int cur_node=0;
	    
	    for(i=0;i<node_array.length;i++)
	    {
	      if(node_matrix[1][i].cost==0.0)
	      {
		
		cur_node=node_matrix[1][i].neighbour_node-1;
	        break;
	      }
	    }
            int update_flag=0;
            for(i=0;i<node_array.length;i++)
            {
	      oldCost = node_matrix[0][i].cost;
	      newCost = node_matrix[0][cur_node].cost+node_matrix[1][i].cost;
	            if(newCost<oldCost)
                    {
                        //iterations[j]++;
                        //oldCost=newCost;
                        update_flag=1;
                        node_matrix[0][i].cost=newCost;
                        node_matrix[0][i].neighbour_node=node_matrix[0][cur_node].neighbour_node;

		    }
	    }
	    if(update_flag==1)
	    {
	     // send_neighbours();
	     for(i=0;i<node_array.length;i++)
	     {
		node_array[i]=node_matrix[0][i];
	     }
	     send_neighbours(cur_node);
	     
	    }

    }   
   
	    public static void send_neighbours(int cur_node)
	    {
	    
	     BufferedReader br;
  DatagramSocket sendSocket;
  String line;   
	    
	    
	    
    try
    {
      br = new BufferedReader(new FileReader(filename));
      sendSocket = new DatagramSocket();
    

	//	System.out.println("Entered try");
		while((line=br.readLine())!=null)
		{
		    String[] parts= line.split(" ");
		    if(parts.length>0)
		    {
		    int src_node=Integer.parseInt(parts[0]);
		    if(src_node!=my_node && src_node!=cur_node+1)
		    {
			boolean neighbour_flag=false;
			for(i=0;i<neighbours.length;i++)
			{
			  if(src_node==neighbours[i])
			  {
			    neighbour_flag=true;
			  }
			}
			if(neighbour_flag)
			{
		String input_file="input"+Integer.toString(my_node)+".txt";	
			File file = new File(input_file);
			//String ip_address=parts[1];
			if (file.exists()){
			    file.delete();
			}
			InetAddress ip_address = InetAddress.getByName(parts[1]);
			int port_num=Integer.parseInt(parts[2]);
			
			FileWriter fw = new FileWriter(file);
			
			for(i=0;i<node_array.length;i++)
			{
			
			  if(i==node_array.length-1)
			    fw.write(node_array[i].toString(true));
			  else
			    fw.write(node_array[i].toString(false));
			}
			fw.close();
			
			byte[] sendData=new byte[(int)file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(sendData);  
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip_address, port_num);
			//System.out.println("Sending packet");
			sendSocket.send(sendPacket);
			}
		    }
		    }
	     }
	    }
      
      catch(Exception e)
      {
	e.printStackTrace();
      }
	    
	    }
   
   
	      
	    public static void PrintTable() {
	    
	    System.out.println("Routing Table for Node ");
	    System.out.println("===========================\n");
	    System.out.println("Destination\tCost\tNeighbor");
	    System.out.println("---------------------------------------------");
	    for (int c = 0; c<node_array.length; c++)
	    {
		int node_indexValue = node_array[c].neighbour_node;
		int column=c+1;
		System.out.println(column+"\t\t"+ node_array[c].cost+"\t"+node_indexValue + "\t");
	    }
	    
	    }
}	   
      
class DistanceVector_UDP
{
	public static Nodes[] node_array;
	public static Nodes[] received_array;
	public static int[] neighbours;
	public static int i,mat_size;
	public static int my_node;
	public static String filename;
	public static int serverport;
        public DistanceVector_UDP(double[] paths) {
        mat_size = paths.length;
        node_array=new Nodes[mat_size];
        received_array=new Nodes[mat_size];
        neighbours=new int[mat_size];
        for( int i=0; i<mat_size; i++) {
                node_array[i] = new Nodes();
                neighbours[i] = -1;
            }
	  }
	  
	 public DistanceVector_UDP()
	 {
	 }
	
	 public static void readMatrix(double[] paths,int my_node) {

	int i=0;
        for (int r = 0; r<paths.length; r++)
        {   
	     
                if (paths[r]>=0.0)
                {
                    node_array[r].cost = paths[r];
                    node_array[r].neighbour_node=r+1;
                    if(r+1!=my_node)
                    {
                        neighbours[i]=r+1;
                        i++;
                    }
                }
                else
                {
                    node_array[r].cost = 100000.0;
                    node_array[r].neighbour_node=100000;
                }
            }

        }
      
    public static void main(String[] args) throws Exception
    {

        String line;
        int i=0,j=0;
        int num_nodes = 0;
        BufferedReader br = new BufferedReader(new FileReader(args[1]));
        num_nodes = Integer.parseInt(br.readLine());
        System.out.println("Number of nodes is : "+num_nodes);
        my_node=Integer.parseInt(args[0]);
	System.out.println("Computing Routing Table for : "+my_node);
        
	filename=args[2];
	serverport=Integer.parseInt(args[3]);
        double[] paths;
        paths =new double[num_nodes];

        for(i=0; i<num_nodes; i++)
        {
            for(j=0; j<num_nodes; j++)
            {
                paths[i]=-1.0;
            }
        }

        while((line=br.readLine())!=null)
        {
	      String[] parts= line.split(" ");
	      int src_node=Integer.parseInt(parts[0]);
            
	      int target_node=Integer.parseInt(parts[1]);
	      double weight=Double.parseDouble(parts[2]);
	    
	      if(src_node==my_node)
	      {
		paths[target_node-1]=weight;
	      }
	      else if(target_node==my_node)
	      {
	        paths[src_node-1]=weight;
	      }
         }
        
        br.close();
        paths[my_node-1]=0.0;
               

        new DistanceVector_UDP(paths);
        readMatrix(paths,my_node);
        
        Server s = new Server();
        try
        {
	  Thread.sleep(10000);
	}
	catch(Exception e)
	{
	}
        Client c = new Client();
        Server.server_thread.join();
        Client.client_thread.join();
        
      }

}






