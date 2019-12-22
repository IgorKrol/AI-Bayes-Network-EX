

import java.util.HashMap;
import java.util.Vector;

/**
 * this class represents Node in bayesian network
 * @param name contains node's name from input
 * @param values represents which states node can hold
 * @param parents vector containing all nodes which this node has direct dependency on
 * @param cpt contains probabilities table
 * @param children contains all nodes which dependent on this node
 * @author igork
 *
 */
public class Node {

	private String name;
	private Vector<String> values;
	private Vector<Node> parents;
	private Cpt cpt;
	private Vector<Node> children;

	Node(String name){
		this.name = name;
		values = new Vector<String>();
		parents = new Vector<Node>();
		children = new Vector<Node>();
	}
	/**
	 * check if p presents in 'parents'
	 * @param p = parent name
	 * @return true if p present
	 */
	public boolean containsParent(String p) {
		for(Node parent : parents) {
			if(parent.getName().equalsIgnoreCase(p)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * adds row for CPT
	 * @param keys = vector of values, represent key for table
	 * @param prob = probability value
	 */
	public void addCPT(Vector<String> keys, float prob) {
		cpt.addCPT(keys, prob);
	}
	
	/**
	 * creates new CPT - name will be parents nodes
	 */
	public void newCPT() {
		Vector<String> cptName = new Vector<String>();
		for(Node n : parents) {
			cptName.add(n.name);
		}
		cptName.add(this.name);
		cpt = new Cpt(cptName);
	}
	
	public void addParents(Node p) {
		parents.add(p);
		p.addChild(this);
	}
	
	public Cpt getCpt() {
		return cpt;
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
	/**
	 * returns value(i)
	 * @param i = index
	 * @return if i = -1 return last value, default: return value[i]
	 */
	public String getValue(int i) {
		if(i==-1) {
			return values.lastElement();
		}
		return values.get(i);
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
