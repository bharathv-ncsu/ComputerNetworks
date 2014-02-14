**************************************************
Link-State and Distance-Vector Routing Algorithms
Authors:Bharath Venkatesh, Sridevi Jantli
Contact:bharat@ncsu.edu, sjantli@ncsu.edu
Platform: Java 1.6 using OpenJDK
OS:Linux
**************************************************
How to Run:
===========
1. Run the make file on the target directory.
	cd <target directory> 
	make

Testing Scenario:
=================
1. Run Linkstate routing algorithm
	java linkstate <input filename> <node1> <node2>

2. Run DistanceVector routing algorithm(centralized approach)
	java DistanceVector <initial node> <input filename> <node1> <node2>

3.Run DistanceVector_UDP (Distributed approach using UDP sockets)
	java DistanceVector_UDP <my_node> <input-filename> <serverlist> <port_number>

Files:
=======
1. Linkstate Folder:
	a. linkstate.java : It runs the Dijkstra algorithm to compute paths.
	b. Nodes.java : It maintains the data-structures for Nodes.
	c. Links.java : It maintains the data-structures for Links.
	d. Cost.java : It maintains the data-structure for the routing tables.


References:
1. Text Book :Introduction to Algorithms-Thomas H Cormen,Charles E Leiserson, Ronald L Rivest, Clifford Stein.
2. Text Book : Java Network Programming-Elliotte Rusty Harold.  