import java.util.*;

/**
 * Solves the shortest-path problem in an undirected, connected simple graph.
 * The program receives a graph, a starting vertex and a destination vertex.
 * It finds a path containing the smallest possible number of arcs and returns
 * the path as a list of {@code Arc} objects in traversal order. If the starting
 * vertex and destination vertex are the same, the result is an empty list.
 * Undirected edges are represented by two oppositely directed arcs.
 */
public class GraphTask {

   /** Creates the program runner. */
   public GraphTask() {
   }

   /**
    * Main method that starts the example tests.
    * @param args command-line arguments (not used)
    */
   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /** Runs the example tests and the large-graph performance test. */
   public void run() {
      // 1. The starting vertex and destination are the same.
      Graph g1 = new Graph ("Example 1");
      Vertex a1 = g1.createVertex ("A");
      checkShortestPath (g1, a1, a1, 0);

      // 2. There is a direct edge between the vertices.
      Graph g2 = new Graph ("Example 2");
      Vertex a2 = g2.createVertex ("A");
      Vertex b2 = g2.createVertex ("B");
      addUndirectedEdge (g2, a2, b2);
      checkShortestPath (g2, a2, b2, 1);

      // 3. The only path has three edges.
      Graph g3 = new Graph ("Example 3");
      Vertex a3 = g3.createVertex ("A");
      Vertex b3 = g3.createVertex ("B");
      Vertex c3 = g3.createVertex ("C");
      Vertex d3 = g3.createVertex ("D");
      addUndirectedEdge (g3, a3, b3);
      addUndirectedEdge (g3, b3, c3);
      addUndirectedEdge (g3, c3, d3);
      checkShortestPath (g3, a3, d3, 3);

      // 4. The algorithm must choose the two-edge path over a three-edge path.
      Graph g4 = new Graph ("Example 4");
      Vertex a4 = g4.createVertex ("A");
      Vertex b4 = g4.createVertex ("B");
      Vertex c4 = g4.createVertex ("C");
      Vertex d4 = g4.createVertex ("D");
      Vertex e4 = g4.createVertex ("E");
      addUndirectedEdge (g4, a4, b4);
      addUndirectedEdge (g4, b4, d4);
      addUndirectedEdge (g4, a4, c4);
      addUndirectedEdge (g4, c4, e4);
      addUndirectedEdge (g4, e4, d4);
      checkShortestPath (g4, a4, d4, 2);

      // 5. There are two different paths of the same minimum length.
      Graph g5 = new Graph ("Example 5");
      Vertex a5 = g5.createVertex ("A");
      Vertex b5 = g5.createVertex ("B");
      Vertex c5 = g5.createVertex ("C");
      Vertex d5 = g5.createVertex ("D");
      addUndirectedEdge (g5, a5, b5);
      addUndirectedEdge (g5, b5, d5);
      addUndirectedEdge (g5, a5, c5);
      addUndirectedEdge (g5, c5, d5);
      checkShortestPath (g5, a5, d5, 2);

      // 6. Performance test with more than 2000 vertices.
      Graph largeGraph = new Graph ("Large graph");
      largeGraph.createRandomSimpleGraph (2500, 3500);
      Vertex start = largeGraph.first;
      Vertex end = start;
      while (end.next != null) {
         end = end.next;
      }

      long startTime = System.nanoTime();
      largeGraph.shortestPath (start, end);
      long elapsedTime = System.nanoTime() - startTime;
      System.out.println ("Large graph (2500 vertices): search time " + elapsedTime / 1_000_000.0 + " ms");
   }

   /**
    * Adds an undirected edge as two oppositely directed arcs.
    *
    * @param graph graph to which the edge is added
    * @param from first endpoint
    * @param to second endpoint
    */
   private void addUndirectedEdge (Graph graph, Vertex from, Vertex to) {
      graph.createArc ("a" + from + "_" + to, from, to);
      graph.createArc ("a" + to + "_" + from, to, from);
   }

   /**
    * Finds a shortest path, checks its length and verifies that its arcs form
    * a continuous path from the starting vertex to the destination vertex.
    * Also prints the graph, the two vertices and the returned path.
    *
    * @param graph graph to search
    * @param start starting vertex
    * @param end destination vertex
    * @param expectedLength expected number of arcs in a shortest path
    */
   private void checkShortestPath (Graph graph, Vertex start, Vertex end, int expectedLength) {
      List<Arc> path = graph.shortestPath (start, end);

      if (path.size() != expectedLength) {
         throw new AssertionError ("Expected " + expectedLength + " arcs, got " + path.size());
      }

      Vertex current = start;
      for (Arc pathArc : path) {
         boolean arcBelongsToCurrentVertex = false;
         Arc outgoing = current.first;
         while (outgoing != null) {
            if (outgoing == pathArc) {
               arcBelongsToCurrentVertex = true;
               break;
            }
            outgoing = outgoing.next;
         }

         if (!arcBelongsToCurrentVertex) {
            throw new AssertionError ("Arc " + pathArc + " does not leave vertex " + current);
         }
         current = pathArc.target;
      }

      if (current != end) {
         throw new AssertionError ("The path does not end at vertex " + end);
      }

      System.out.println ("\n" + graph);
      System.out.println ("Start vertex: " + start);
      System.out.println ("Destination vertex: " + end);
      System.out.println ("Returned path (arcs): " + path);
      System.out.println ("Path check: OK");
   }

   /**
    * Vertex represents one vertex in the graph. Each vertex has an identifier,
    * a reference to the next vertex in the graph's vertex list, and a reference
    * to the first outgoing arc. The outgoing arcs are used to find neighbouring
    * vertices when searching for the shortest path.
    */
   class Vertex {

      private String id;
      private Vertex next;
      private Arc first;
      private int info = 0;
      // You can add more fields, if needed

      /**
       * Creates a vertex with an identifier, a next vertex and a first outgoing arc.
       * @param s vertex identifier
       * @param v next vertex in the graph's vertex list
       * @param e first outgoing arc of this vertex
       */
      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      /**
       * Creates a vertex with the given identifier.
       * @param s vertex identifier
       */
      Vertex (String s) {
         this (s, null, null);
      }

      /** Returns the identifier of this vertex. */
      @Override
      public String toString() {
         return id;
      }
   }


   /**
    * Represents one directed arc in the graph. An undirected edge is
    * represented by two Arc objects, one for each direction.
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      // You can add more fields, if needed

      /**
       * Creates an arc with an identifier, a target vertex and a next arc.
       * @param s arc identifier
       * @param v target vertex
       * @param a next arc in the adjacency list
       */
      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      /**
       * Creates an arc with the given identifier.
       * @param s arc identifier
       */
      Arc (String s) {
         this (s, null, null);
      }

      /** Returns the identifier of this arc. */
      @Override
      public String toString() {
         return id;
      }
   }


   /** Represents a graph as a linked list of vertices and outgoing arcs. */
   class Graph {

      private String id;
      private Vertex first;
      private int info = 0;
      // You can add more fields, if needed

      Graph (String s, Vertex v) {
         id = s;
         first = v;
      }

      Graph (String s) {
         this (s, null);
      }

      @Override
      public String toString() {
         String nl = System.getProperty ("line.separator");
         StringBuffer sb = new StringBuffer (nl);
         sb.append (id);
         sb.append (nl);
         Vertex v = first;
         while (v != null) {
            sb.append (v.toString());
            sb.append (" -->");
            Arc a = v.first;
            while (a != null) {
               sb.append (" ");
               sb.append (a.toString());
               sb.append (" (");
               sb.append (v.toString());
               sb.append ("->");
               sb.append (a.target.toString());
               sb.append (")");
               a = a.next;
            }
            sb.append (nl);
            v = v.next;
         }
         return sb.toString();
      }

      /** Creates a vertex and adds it to this graph.
       * @param vid identifier of the new vertex
       * @return the created vertex
       */
      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

      /** Creates a directed arc from one vertex to another.
       * @param aid identifier of the new arc
       * @param from starting vertex
       * @param to target vertex
       * @return the created arc
       */
      public Arc createArc (String aid, Vertex from, Vertex to) {
         Arc res = new Arc (aid);
         res.next = from.first;
         from.first = res;
         res.target = to;
         return res;
      }

      /**
       * Create a connected undirected random tree with n vertices.
       * Each new vertex is connected to some random existing vertex.
       * @param n number of vertices added to this graph
       */
      public void createRandomTree (int n) {
         if (n <= 0)
            return;
         Vertex[] varray = new Vertex [n];
         for (int i = 0; i < n; i++) {
            varray [i] = createVertex ("v" + String.valueOf(n-i));
            if (i > 0) {
               int vnr = (int)(Math.random()*i);
               createArc ("a" + varray [vnr].toString() + "_" + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_" + varray [vnr].toString(), varray [i], varray [vnr]);
            } else {}
         }
      }

      /**
       * Create an adjacency matrix of this graph.
       * Side effect: corrupts info fields in the graph
       * @return adjacency matrix
       */
      public int[][] createAdjMatrix() {
         info = 0;
         Vertex v = first;
         while (v != null) {
            v.info = info++;
            v = v.next;
         }
         int[][] res = new int [info][info];
         v = first;
         while (v != null) {
            int i = v.info;
            Arc a = v.first;
            while (a != null) {
               int j = a.target.info;
               res [i][j]++;
               a = a.next;
            }
            v = v.next;
         }
         return res;
      }

      /**
       * Create a connected simple (undirected, no loops, no multiple
       * arcs) random graph with n vertices and m edges.
       * @param n number of vertices
       * @param m number of edges
       */
      public void createRandomSimpleGraph (int n, int m) {
         if (n <= 0)
            return;
         if (n > 2500)
            throw new IllegalArgumentException ("Too many vertices: " + n);
         if (m < n-1 || m > n*(n-1)/2)
            throw new IllegalArgumentException ("Impossible number of edges: " + m);
         first = null;
         createRandomTree (n);       // n-1 edges created here
         Vertex[] vert = new Vertex [n];
         Vertex v = first;
         int c = 0;
         while (v != null) {
            vert[c++] = v;
            v = v.next;
         }
         int[][] connected = createAdjMatrix();
         int edgeCount = m - n + 1;  // remaining edges
         while (edgeCount > 0) {
            int i = (int)(Math.random()*n);  // random source
            int j = (int)(Math.random()*n);  // random target
            if (i==j)
               continue;  // no loops
            if (connected [i][j] != 0 || connected [j][i] != 0)
               continue;  // no multiple edges
            Vertex vi = vert [i];
            Vertex vj = vert [j];
            createArc ("a" + vi.toString() + "_" + vj.toString(), vi, vj);
            connected [i][j] = 1;
            createArc ("a" + vj.toString() + "_" + vi.toString(), vj, vi);
            connected [j][i] = 1;
            edgeCount--;  // a new edge happily created
         }
      }

      /**
       * Finds a path with the smallest possible number of arcs between two
       * vertices. Breadth-first search guarantees that the first path found
       * has the minimum number of arcs.
       *
       * @param start the starting vertex
       * @param end the destination vertex
       * @return a list of arcs in the order in which they are traversed;
       *         an empty list if start and end are the same vertex
       * @throws IllegalArgumentException if either vertex is null, does not
       *         belong to this graph, or no path exists between the vertices
       */
      public List<Arc> shortestPath (Vertex start, Vertex end) {
         if (start == null || end == null) {
            throw new IllegalArgumentException ("Vertices must not be null");
         }
         if (!containsVertex (start) || !containsVertex (end)) {
            throw new IllegalArgumentException ("Both vertices must belong to this graph");
         }

         List<Arc> path = new ArrayList<>();
         if (start == end) {
            return path;
         }

         Queue<Vertex> queue = new ArrayDeque<>();
         Set<Vertex> visited = new HashSet<>();
         Map<Vertex, Vertex> previousVertex = new HashMap<>();
         Map<Vertex, Arc> previousArc = new HashMap<>();

         queue.add (start);
         visited.add (start);

         while (!queue.isEmpty()) {
            Vertex current = queue.remove();
            Arc arc = current.first;

            while (arc != null) {
               Vertex next = arc.target;

               if (visited.add (next)) {
                  previousVertex.put (next, current);
                  previousArc.put (next, arc);

                  if (next == end) {
                     Vertex vertex = end;
                     while (vertex != start) {
                        path.add (previousArc.get (vertex));
                        vertex = previousVertex.get (vertex);
                     }
                     Collections.reverse (path);
                     return path;
                  }

                  queue.add (next);
               }
               arc = arc.next;
            }
         }

         throw new IllegalArgumentException ("No path exists between the vertices");
      }

      /**
       * Checks whether a vertex belongs to this graph.
       *
       * @param vertex vertex to check
       * @return true if the exact vertex object is in this graph
       */
      private boolean containsVertex (Vertex vertex) {
         Vertex current = first;
         while (current != null) {
            if (current == vertex) {
               return true;
            }
            current = current.next;
         }
         return false;
      }
   }

}
