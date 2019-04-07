package algorithms;

public class Edge {
	public int p;
	public int q;
	//public double dist;

	public Edge(int p, int q) {
		this.p = p;
		this.q = q;
		//this.dist = d;
	}
	
	
	public boolean equals(Edge e) {
		if((this.p == e.p && this.q == e.q) || (this.p == e.q && this.q == e.p)) {
			return true;
		}
		return false;
	}
}