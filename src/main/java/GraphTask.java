import java.util.*;

/** Container class to different classes, that makes the whole
 * set of classes one class formally.
 */

/** Shortest path between two vertices.
 *Lühim tee kahe tipu vahel. Programm võtab sisendiks
 * sidusa lihtgraafi ning kaks tippu: algtipu ja lõpptipu. Programm leiab nende
 * vahelise tee, milles on kaari võimalikult vähe, ning tagastab selle kaarte
 * listina selles järjekorras, nagu tee läbitakse. Kui algtipp ja lõpptipp on
 * sama tipp, tagastab programm tühja List objekti."
 */
public class GraphTask {

   /** Main method. */
   public static void main (String[] args) {
      GraphTask a = new GraphTask();
      a.run();
   }

   /** Actual main method to run examples and everything. */
   public void run() {
      Graph g1 = new Graph ("Example 1");
      Vertex a1 = g1.createVertex ("A");
      checkPathLength (g1, a1, a1, 0);

      Graph g2 = new Graph ("Example 2");
      Vertex a2 = g2.createVertex ("A");
      Vertex b2 = g2.createVertex ("B");
      addUndirectedEdge (g2, a2, b2);
      checkPathLength (g2, a2, b2, 1);

      Graph g3 = new Graph ("Example 3");
      Vertex a3 = g3.createVertex ("A");
      Vertex b3 = g3.createVertex ("B");
      Vertex c3 = g3.createVertex ("C");
      Vertex d3 = g3.createVertex ("D");
      addUndirectedEdge (g3, a3, b3);
      addUndirectedEdge (g3, b3, c3);
      addUndirectedEdge (g3, c3, d3);
      checkPathLength (g3, a3, d3, 3);

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
      checkPathLength (g4, a4, d4, 2);

      Graph g5 = new Graph ("Example 5");
      Vertex a5 = g5.createVertex ("A");
      Vertex b5 = g5.createVertex ("B");
      Vertex c5 = g5.createVertex ("C");
      Vertex d5 = g5.createVertex ("D");
      addUndirectedEdge (g5, a5, b5);
      addUndirectedEdge (g5, b5, d5);
      addUndirectedEdge (g5, a5, c5);
      addUndirectedEdge (g5, c5, d5);
      checkPathLength (g5, a5, d5, 2);

      Graph largeGraph = new Graph ("Large graph");
      largeGraph.createRandomSimpleGraph (2500, 3500);
      Vertex start = largeGraph.first;
      Vertex end = start;
      while (end.next != null) {
         end = end.next;
      }
      long startTime = System.nanoTime();
      List<Arc> largePath = largeGraph.shortestPath (start, end);
      long elapsedTime = System.nanoTime() - startTime;
      System.out.println ("Large graph (2500 vertices): " + largePath.size()
              + " arcs; search time " + elapsedTime / 1_000_000.0 + " ms");
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
    * Runs a shortest-path example and checks the expected number of arcs.
    *
    * @param graph graph to search
    * @param start starting vertex
    * @param end destination vertex
    * @param expectedLength expected number of arcs in the shortest path
    */
   private void checkPathLength (Graph graph, Vertex start, Vertex end,
                                 int expectedLength) {
      List<Arc> path = graph.shortestPath (start, end);
      if (path.size() != expectedLength) {
         throw new AssertionError ("Expected " + expectedLength
                 + " arcs, got " + path.size());
      }
      System.out.println (graph.id + ": " + path);
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

      Vertex (String s, Vertex v, Arc e) {
         id = s;
         next = v;
         first = e;
      }

      Vertex (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

   }


   /** Arc represents one arrow in the graph. Two-directional edges are
    * represented by two Arc objects (for both directions).
    */
   class Arc {

      private String id;
      private Vertex target;
      private Arc next;
      private int info = 0;
      // You can add more fields, if needed

      Arc (String s, Vertex v, Arc a) {
         id = s;
         target = v;
         next = a;
      }

      Arc (String s) {
         this (s, null, null);
      }

      @Override
      public String toString() {
         return id;
      }

   }


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

      public Vertex createVertex (String vid) {
         Vertex res = new Vertex (vid);
         res.next = first;
         first = res;
         return res;
      }

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
               createArc ("a" + varray [vnr].toString() + "_"
                  + varray [i].toString(), varray [vnr], varray [i]);
               createArc ("a" + varray [i].toString() + "_"
                  + varray [vnr].toString(), varray [i], varray [vnr]);
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
            throw new IllegalArgumentException 
               ("Impossible number of edges: " + m);
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


