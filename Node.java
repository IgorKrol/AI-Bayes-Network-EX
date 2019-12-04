import java.util.HashMap;
import java.util.Vector;

public class Node {
	
	private String name;
	private Vector<Boolean> value;
	private Vector<Node> perents;
	private HashMap<Vector<String>, Double> cpt;
	
	Node(String name){
		this.name = name;
		cpt=new HashMap<Vector<String>, Double>();
	}
	
	/* adds new CPT to node on creation*/
	public void addCPT(Vector<String> keys, double prob) {
		cpt.put(keys, prob);
	}
	
	public void addParents() {
	}
	
}
