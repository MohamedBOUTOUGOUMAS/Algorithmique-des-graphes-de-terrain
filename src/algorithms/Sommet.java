package algorithms;

import java.util.ArrayList;

public class Sommet {
	
	int s;
	int degree;
	ArrayList<Edge> graphe;
	
	public Sommet(int s, ArrayList<Edge> graphe) {
		this.s = s;
		this.graphe = graphe;
		this.degree = Reader.nodeDegree(s, graphe);
	}
	

}
