package tg.firstB;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GreatestCycle {
	
	/**
	 * Loads the file containing graph represented by adjacency matrix. It 
	 * parses it and return matrix of integers.  
	 * 
	 * @param file with graph
	 * @return array of integer arrays representing the matrix.
	 * @throws IOException
	 */
	public static int[][] loadIntMatrix(String file) throws IOException {				
		
		List<String> contents = Files.readAllLines(Paths.get(file), Charset.forName("UTF-8"));
		
		final int n = Integer.parseInt(contents.get(0));
		int[][] g = new int[n][n];

		int index = 2;

		for (int i = 0; i < n; i++) {
			String[] line = contents.get(index).split("\\s"); 
			
			for (int j = 0; j < n; j++) {				
				g[i][j] = Integer.parseInt(line[j]);
			}
			
			index++;
		}
			
		return g;		
	}

	/**
	 * Programs entry point. 
	 * 
	 * @param args Path of the file which contains configuration of the graph.
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		if (args.length != 1) {
			System.err.println("Program prima točno jedan argument: putanja do datoteke s konfiguracijom grafa.");
		}
		
		final String fpath = args[0];

		AdjacencyListGraph graph = new AdjacencyListGraph(loadIntMatrix(fpath));
		
		Set<Set<Integer>> cycles = graph.findAllCycles(); 
		System.out.println(cycles);
		System.out.println("Biggest iz da:" + Collections.max(cycles, new Comparator<Set<Integer>>() {

			@Override
			public int compare(Set<Integer> o1, Set<Integer> o2) {
				return o1.size() - o2.size();
			}
		}));
		
	}	
}


interface Graph {
}

/**
 * Class which implements the graph represented as a adjacency list. 
 * @author antonio
 *
 */
final class AdjacencyListGraph implements Graph {
	
	private List<Pair> edges;
	private List<Integer> vertices;
	private List<List<Integer>> cycles = new ArrayList<List<Integer>>();

	/**
	 * Construct the graph given the adjacency matrix.
	 * 
	 * @param data {@code int[][]} adjacency matrix
	 */
	public AdjacencyListGraph(final int[][] data) {
		
		final int n = data.length;
		vertices = new ArrayList<>();
		edges = new ArrayList<>(n*n);
		
		for (int i = 0; i < data.length; i++) {
			
			vertices.add(i);

			for (int j = 0; j < n; j++) {

				if (data[i][j] == 1) {
					edges.add(new Pair(i, j));
				}
			}
		}
	}
	
	/**
	 * Compute and return the set of sets of vertices.
	 * 
	 * @return set of sets of vertices
	 */
	public Set<Set<Integer>> findAllCycles() {
		
//		for (Integer v : vertices) {			 // ne triba po svima mislim.
		int v = vertices.get(0);
			List<Integer> path = new ArrayList<Integer>();
			Set<Integer> exVertices= new HashSet<>(); 
			findCycles(v, v, path, exVertices);
//		}
		
	    Set<Set<Integer>> cs = new HashSet<Set<Integer>>(cycles.size());
		for (List<Integer> c : cycles) {
			cs.add(new HashSet<Integer>(c));
		}
		
		return cs;
	}

	/**
	 * 
	 * @param start
	 * @param v
	 * @param path
	 * @param visited
	 */
	public void findCycles( int start
						  , int current
						  , List<Integer> path
						  , Set<Integer> visited) {

		path.add(current); visited.add(current);

		for (Pair edge : adjacentEdges(current)) {

			final boolean isCycle = visited.contains(edge.snd) && path.get(path.size()-2) != edge.snd;
			
			if (isCycle) {
				cycles.add(path.subList(path.indexOf(edge.snd), path.size()));
			}

			if (!path.contains(edge.snd)) {
				findCycles(start, edge.snd, new ArrayList<>(path), new HashSet<>(visited));
			}
		}
	}
	
	/**
	 * Get all the edges adjacent to the given vertex.
	 * @param v
	 * @return
	 */
	public List<Pair> adjacentEdges(int v) {
		
		List<Pair> adjecents = new ArrayList<>();
		
		for (Pair edge : edges) {
			if (edge.fst == v) {
				adjecents.add(edge);
			}
		}
		
		return adjecents;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Pair pair : edges) {
			sb.append(pair);
		}
		return sb.toString();
	}
}

class Tuple {
	
	protected int[] tuple;
	
	public Tuple(final int n, int ... elems) {
		tuple = new int[n];
		System.arraycopy(elems, 0, tuple, 0, n);		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(tuple.length*2+1);
		sb.append("( ");
		for (int i : tuple) {
			sb.append(i);
			sb.append(',');
		}
		sb.setCharAt(sb.length()-1,' ');
		sb.append(')');
		return sb.toString();		
	}
}

class Pair extends Tuple {
	
	public int fst, snd;
	
	public Pair(int ... elems) {
		super(2, elems);
		fst = tuple[0];
		snd = tuple[1];
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fst + snd; 
		
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pair other = (Pair) obj;
		if (fst == other.fst && snd == other.snd) return true;
		if (fst == other.snd && fst == other.snd) return true;
		return false;
	}
}