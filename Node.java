

import java.util.HashMap;
import java.util.Vector;

public class Node {

	private String name;
	private Vector<String> values;
	private Vector<Node> parents;
	private HashMap<Vector<String>, Double> cpt;
	private Vector<Node> children;

	Node(String name){
		this.name = name;
		cpt=new HashMap<Vector<String>, Double>();
		values = new Vector<String>();
		parents = new Vector<Node>();
		children = new Vector<Node>();
	}

	/* adds new CPT to node on creation*/
	public void addCPT(Vector<String> keys, double prob) {
		cpt.put(keys, prob);
	}

	public void addParents(Node p) {
		parents.add(p);
		p.addChild(this);
	}

	public void addChild(Node c) {
		children.add(c);
	}
	
	public void addValues(String[] val) {
		for(String v : val)
			values.add(v);
	}
	
	public String getName() {
		return this.name;
	}
	
	public Vector<Node> getParents(){
		return parents;
	}
	
	public Vector<Node> getChildren() {
		return this.children;
	}
	
	public String toString() {
		String res = "";
		res+= name + '\n' + values + '\n';
		for(Node i : parents) {
			res+= i.name + ", ";
		}
		res+="\n";
		res+=cpt;
		return res;
	}


}
