import java.util.HashMap;
import java.util.Vector;

public class Cpt {

	Vector<String> name;
	HashMap<Vector<String>, Double> cpt;
	
	public Cpt(Vector<String> n) {
		cpt = new HashMap<Vector<String>, Double>();
		name = n;
	}
	
	public void addCPT(Vector<String> keys, double prob) {
		cpt.put(keys, prob);
	}
	
	public HashMap<Vector<String>, Double> getCpt(){
		return cpt;
	}
	
	public Vector<String> getName() {
		return name;
	}
	
	public int cptSize() {
		return cpt.size();
	}
	
	public String toString() {
		return cpt.toString();
	}
}
