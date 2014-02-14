import java.io.*;
import java.util.*;

class Nodes implements Comparable<Nodes>
{
  int node_no;
   public LinkedList<Links> n_link = new LinkedList<Links>();
  public double distance = Double.POSITIVE_INFINITY;
  public Nodes parent;
  public Nodes(int node_no)
  {
    this.node_no=node_no;
  }
  public int getNodeNo()
  {
    return node_no;
  }
  public int compareTo(Nodes n)
  {
        return Double.compare(distance, n.distance);
  }
}

