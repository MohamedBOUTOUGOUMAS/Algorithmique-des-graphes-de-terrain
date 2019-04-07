package algorithms;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Collectors;

public class Reader {
	
	static int sizeEdges = 0; 
	
	static Map<Integer, Integer> nodeAndDegre = new LinkedHashMap();
	
	public static void calculDegre(String path) {
		BufferedReader lecteurAvecBuffer = null;
	    String ligne;
	 
	    try{
			lecteurAvecBuffer = new BufferedReader(new FileReader(path));
			
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				String [] aretes = new String[2];
				aretes = ligne.split("\\s+", 2);
				int p = Integer.parseInt(aretes[0]);
				int q = Integer.parseInt(aretes[1]);
				if(!nodeAndDegre.containsKey(p)) {
					nodeAndDegre.put(p, 0);
				}
				if(!nodeAndDegre.containsKey(q)) {
					nodeAndDegre.put(q, 0);
				}
				nodeAndDegre.put(p, nodeAndDegre.get(p)+1);
				nodeAndDegre.put(q, nodeAndDegre.get(q)+1);
				
			}
			//System.out.println(nodeAndDegre);
			
			lecteurAvecBuffer.close();
			
	    }catch (IOException e) {
			// TODO: handle exception
		}      
	}
	
	public static ArrayList<Edge> read(String path) {
		BufferedReader lecteurAvecBuffer = null;
	    String ligne;
	    ArrayList<Edge> edges = new ArrayList<>();
	    try{
			lecteurAvecBuffer = new BufferedReader(new FileReader(path));
			
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				String [] aretes = new String[2];
				aretes = ligne.split("\\s+", 2);
				try {
					// Dans le cas de données non pré-traitées 
					// Essentiel pour la suite (BFS, ...)
					edges.add(new Edge(Integer.parseInt(aretes[0]), Integer.parseInt(aretes[1]))); 
					// Sinon : Nombre de ligne lu ( lien par ligne : id2 --> id45 )
 					sizeEdges += 1; // Pas de stockage dans la mémoire !!
					
				}catch (NumberFormatException e) {

				}
			}
			      
			lecteurAvecBuffer.close();
			
			return edges;
		}
	    catch(IOException exc){
	    	System.out.println("Erreur d'ouverture");
	    }
	    return edges;
	    
	}
	
	public static Set<Integer> listSommets(ArrayList<Edge> buf){
		Set<Integer> sommets = new HashSet<>();
		for(Edge e : buf) {
			sommets.add(e.p);
			sommets.add(e.q);
		}
		return sommets;
	}
	
	public static Integer nbSommets(ArrayList<Edge> buf) {
		
		return listSommets(buf).size();
		
	}
	
	
	public static Integer nbAretes(ArrayList<Edge> buf) {
		return buf.size();
	}
	
	public static ArrayList<Edge> cleanData(ArrayList<Edge> buf){
		Set<Edge> aretes = new HashSet<>();
		for(Edge e : buf) {
			if(e.p != e.q) // selfLoops
				aretes.add(e);
		}
		ArrayList<Edge> l = new ArrayList<>(aretes);
		return l;
	}
	
	public static Set<Integer> listNeighbors(int s, ArrayList<Edge> edges){
		Set<Integer> neighbors = new HashSet<>(); 
		for(Edge e : edges) {
			if(s == e.p) {
				neighbors.add(e.q);
			}else if(s == e.q) {
				neighbors.add(e.p);
			}
		}
		return neighbors;
	}
	
	public static Integer nodeDegree(int s, ArrayList<Edge> edges) {
		return listNeighbors(s, edges).size();
	}
	
	public static boolean contains(ArrayList<Edge> edges, int p, int q) {
		for (Edge e : edges) {
			if (e.p == p && e.q == q || e.p == (q) && e.q == (p))
				return true;
		}
		return false;
	}
	
	public static Integer specialQuantity(ArrayList<Edge> edges) {
		ArrayList<Integer> sommets = new ArrayList<>(listSommets(edges));
		Integer somme = 0;
		ArrayList<Edge> arete = new ArrayList<>();
		for(Integer a : sommets) {
			for(Integer b : sommets ) {
				if(a == b) continue;
				if( ! contains(arete, a, b)) {
					arete.add(new Edge(a, b));
					somme += (nodeAndDegre.get(a) + nodeAndDegre.get(b));
				}
			}
		}
		return somme;
	}
	
	
	public static void degreeDistribution() {
		System.out.println("begin");
		
		Map<Integer, Integer> degreNBSommets = new LinkedHashMap<>();
		for(Entry<Integer, Integer> e : nodeAndDegre.entrySet()) {
			degreNBSommets.put(e.getValue(), 0);
		}
		for(int i = 0; i<nodeAndDegre.keySet().size(); i++) {
			degreNBSommets.put((int)nodeAndDegre.values().toArray()[i], degreNBSommets.get((int)nodeAndDegre.values().toArray()[i])+1);
		}
		
		
		File file = new File("graphe.txt");
		
		try {
			FileWriter fichier = new FileWriter(file);
			
				
			for(int i = 0; i<nodeAndDegre.keySet().size(); i++) {
				fichier.write(degreNBSommets.get((int)nodeAndDegre.values().toArray()[i]) + " " + (int)nodeAndDegre.values().toArray()[i]);
				fichier.write("\n");
			}
			
			System.out.println("end");
			fichier.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	public static void storeAsListEdge(ArrayList<Edge> edges) {
		
		File file = new File("graphe_ListOfEdges.txt");
		
		try {
			FileWriter fichier = new FileWriter(file);
			for(int i=0; i<edges.size(); i++) {
				fichier.write(edges.get(i).p + " " +edges.get(i).q);
				fichier.write("\n");
			}
			
			fichier.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}
	
	
	public static void storeAsAdjacencyMatrix(ArrayList<Edge> edges) {
		
		ArrayList<Integer> sommets = new ArrayList<>(listSommets(edges));
		int nbsommets = sommets.size();
		int [][] matrix = new int[nbsommets][nbsommets];
		
		File file = new File("graphe_AdjacencyMatrix.txt");
		try {
			FileWriter fichier = new FileWriter(file);
			for(int i =0; i<nbsommets; i++) {
				for (int j = 0; j < nbsommets; j++) {
					if(edges.contains(new Edge(sommets.get(i),sommets.get(j)))) {
						matrix[i][j] = 1;
						fichier.write(i+" "+j+" "+1);
						fichier.write("\n");
					}else {
						matrix[i][j] = 0;
						fichier.write(i+" "+j+" "+0);
						fichier.write("\n");
					}
				}
			}
			
			fichier.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void storeAsAdjacencyArray(ArrayList<Edge> edges) {
		ArrayList<Integer> sommets = new ArrayList<>(listSommets(edges));
		int nbsommets = sommets.size();
		Map<Integer, Integer> map = new HashMap();
		File file = new File("graphe_AdjacencyArray.txt");
		try {
			FileWriter fichier = new FileWriter(file);
			
			for (int i = 0; i < nbsommets; i++) {
				int r = sommets.get(i);
				ArrayList<Integer> voisins = new ArrayList<>(listNeighbors(r, edges));
				map.put(r, voisins.size());
			}
			
			
			Map<Integer, Integer> sortedMap = map.entrySet().stream()
	                .sorted(Map.Entry.<Integer,Integer> comparingByValue().reversed())
	                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
	                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
			
			
			
			for (Entry<Integer, Integer> e : sortedMap.entrySet()) {
				
				ArrayList<Integer> voisins = new ArrayList<>(listNeighbors(e.getKey(), edges));
				Collections.sort(voisins);
				
				String str="";
				for(int i = 0; i<voisins.size(); i++) {
					str += voisins.get(i)+" ";
				}
				fichier.write(e.getKey()+"\t"+str);	
				fichier.write("\n");
			}
			
			fichier.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	
	
	public static int BFS(ArrayList<Edge> edges, int s) {
		ArrayList<Integer> sommets = new ArrayList<>(listSommets(edges));
		int nbsommets = sommets.size();
		boolean [] tags = new boolean[nbsommets];
		int distSU = 0;
		ArrayList<Integer> fifo = new ArrayList();
		fifo.add(s);
		tags[sommets.indexOf(s)] = true;
		int u = -1;
		while(fifo.size()> 0) {
			u = fifo.remove(0);
			//System.out.println(u);
			distSU += 1;
			ArrayList<Integer> neighbor = new ArrayList<>(listNeighbors(u, edges));
			for(Integer v : neighbor) {
				if(!tags[sommets.indexOf(v)]) {
					fifo.add(v);
					tags[sommets.indexOf(v)] = true;
				}
			}
		}
		return distSU;
	}
	
	public static int calculDiametre(ArrayList<Edge> edges) { // En distance de voisins
		
		ArrayList<Integer> sommets = new ArrayList<>(listSommets(edges));
		int nbsommets = sommets.size();
		Random rand =  new Random();
		Vector<Integer> vectDist = new Vector<>();
		
		for(int i=0; i < 1000; i++) {
			vectDist.add(BFS(edges,sommets.get(rand.nextInt(nbsommets))));
		}
		
		return Collections.max(vectDist);
		
	}
	
	
	public static Set<Integer> readNeighbors(int s, String filePath){
		
		Set<Integer> voisins = new HashSet<>();
		BufferedReader lecteurAvecBuffer = null;
	    String ligne;
	    try{
			lecteurAvecBuffer = new BufferedReader(new FileReader(filePath));
			
			while ((ligne = lecteurAvecBuffer.readLine()) != null) {
				String [] tab = new String[2];
				tab = ligne.split("\t", 2);
				if(Integer.parseInt(tab[0])==s) {
					String[] l = tab[1].split(" ");
					for (int i = 0; i < l.length; i++) {
						voisins.add(Integer.parseInt(l[i]));
					}
				}
				
			}
			lecteurAvecBuffer.close();
			
		}
	    catch(IOException exc){
	    	System.out.println("Erreur d'ouverture");
	    }
		
	    return voisins;
		
	}
	
	
	public static void listAllTriangles(ArrayList<Edge> edges) {
		
		File file = new File("Triangles.txt");
		
		try {
			FileWriter fichier = new FileWriter(file);
			
			for(Edge e : edges) {
				if(e.p != e.q) {
					Set<Integer> U = readNeighbors(e.p, "graphe_AdjacencyArray.txt");
					Set<Integer> V = readNeighbors(e.q, "graphe_AdjacencyArray.txt");
					Set<Integer> W;
					if(U.size()>V.size()) {
						V.retainAll(U);
						W = V;
					}else {
						U.retainAll(V);
						W = U;
					}
					
					for(Integer i: W) {
						if(i != e.p && i != e.q) {
							fichier.write(e.p+" "+e.q+" "+i);
							fichier.write("\n");
						}
						
					}
				}
				
			}
			System.out.println("end");
			fichier.close();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		
		
	}
	
	
	
	public static void main(String [] args) {
//		ArrayList<Edge> buf = read("email-Eu-core-clean.txt");
//		ArrayList<Edge> buf = read("com-amazon.ungraph-clean.txt");
//		ArrayList<Edge> buf = read("/Vrac/TME_CPA_19-02-20/com-amazon.ungraph-clean.txt");
//		System.out.println(nbSommets(buf));
//		System.out.println(nbAretes(buf));
//		buf = cleanData(buf);
		calculDegre("com-amazon.ungraph-clean.txt");
//		calculDegre("email-Eu-core-clean.txt");
		degreeDistribution();
//		storeAsListEdge(buf);
//		storeAsAdjacencyMatrix(buf);
//		storeAsAdjacencyArray(buf);
//		System.out.println(BFS(buf, buf.get(125).p));
//		readNeighbors(buf.get(0).p, "graphe_AdjacencyArray.txt");
		
//		listAllTriangles(buf);
		
		
//		calculDegre("email-Eu-core-clean.txt");
		
	}
	
}
