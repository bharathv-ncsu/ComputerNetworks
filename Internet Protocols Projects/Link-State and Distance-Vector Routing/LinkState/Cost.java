import java.io.*;
import java.util.*;
class Cost
{
    public double min_distance;
    public Nodes parent;
    public Cost(double min_distance, Nodes parent)
    {
        this.min_distance=min_distance;
        this.parent=parent;
    }
}

