import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

public class Cpt {

	Vector<String> name;
	HashMap<Vector<String>, Float> cpt;
	
	public Cpt(Vector<String> n) {
		cpt = new HashMap<Vector<String>, Float>();
		name = (Vector<String>) n.clone();
	}
	
	public void addCPT(Vector<String> keys, float prob) {
		cpt.put(keys, prob);
	}
	
	public Cpt clone() {
		Cpt cloneCpt = new Cpt((Vector<String>) name.clone());
		for(Vector<String> c : cpt.keySet()) {
			cloneCpt.addCPT((Vector<String>) c.clone(), cpt.get(c));
		}
		return cloneCpt;
	}
	
	public HashMap<Vector<String>, Float> getCpt(){
		return cpt;
	}
	
	public Vector<String> getName() {
		return name;
	}
	
	public int cptSize() {
		return cpt.size();
	}
	
	public String toString() {
		return name + "\n" +cpt.toString();
	}
}
