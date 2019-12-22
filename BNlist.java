

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
/**
 * class representing Bayesian Network
 * @author igork
 *
 */
public class BNlist {
	Vector<Node> bNlist;

	public BNlist() {
		bNlist = new Vector<Node>();
	}
	/**
	 * this function calls parser to be executed for this BNlist
	 * @param path = input.txt
	 * @return String with result for output
	 */
	public String bnFparse(String path) {
		try {
			BnFileParser fp = new BnFileParser(path, this);
			return fp.parse();
		} catch (Exception e) {
			System.err.println(e.getStackTrace()[0].getFileName()+"\n"+e.getStackTrace()[0].getLineNumber() +": "+e);
			return e.getMessage();
		}
	}
	/**
	 * dependency check between start to target given evidence nodes
	 * @param start
	 * @param target
	 * @param evidence
	 * @return yes if dependent, no if not
	 */
	public String isDependent(Node start, Node target, String[] evidence) {
		Vector<String> paths = getAllPaths(start, target);
		if(paths.size()==0) {
			return "no";
		}
		else {
			int indepCount = 0;
			for(String path : paths) {
				String[] nodes = path.split("[\\><]");
				String[] op = new String[nodes.length-1];
				char tempOp;
				int indexOp = 0;
				for(int i = 0; i < path.length(); i++) {
					tempOp = path.charAt(i);
					if(tempOp == '<' || tempOp == '>') {
						op[indexOp++] = tempOp + "";
					}
				}

				for(int i = 1; i < nodes.length - 1; i++) {
					boolean isE = false;
					if(evidence != null && evidence.length !=0)
						for(String e : evidence) {
							if(e.equals(nodes[i])) {
								isE = true;
							}
						}
					if(isE) {
						if((op[i].equals("<") && op[i+1].equals(">")) ||
								(op[i].equals(">") && op[i+1].equals(">"))) {
							indepCount++;
							break;
						}
					}
					else {
						if(op[i-1].equals(">") && op[i].equals("<")) {
							if(evidence == null || evidence.length == 0) {
								indepCount++;
							}
							else {
								if(!searchEvidence(nodes[i], evidence)) {
									indepCount++;
									break;
								}
							}
						}
					}
				}
			}
			if (indepCount == paths.size()) {
//				System.out.println("yes");
				
				return "yes";
			}
			else {
//				System.out.println("no");
				return "no";
			}
		}
	}
	
	/**
	 * Search 's' children for evidence from 'e'
	 * @param s
	 * @param e
	 * @return if there is evidence under 's' return true
	 */
	public boolean searchEvidence(String s, String[] e) {
		Node start = null;
		for(Node temp : bNlist) {
			if(temp.getName().equalsIgnoreCase(s)) {
				start = temp;
				break;
			}
		}
		return checkChildEvidence(start, e);
	}
	/**
	 * Subfunction: RECURSIVLY search 's' children for evidence from 'e'
	 * @param s
	 * @param evi
	 * @return
	 */
	private boolean checkChildEvidence(Node s, String[] evi) {
		for(String e : evi) {
			if(s.getName().equalsIgnoreCase(e)) {
				return true;
			}
		}
		if(s.getChildren().isEmpty()) {
			return false;
		}
		else {
			for(Node c : s.getChildren()) {
				return checkChildEvidence(c, evi);
			}
		}
		return false;
	}

	/**
	 * Finds all paths from s -> t
	 * @param s = start
	 * @param t = target
	 * @return	Vector with all paths
	 */
	public Vector<String> getAllPaths(Node s, Node t) {
		Vector<String> paths = new Vector<String>();
		if(s == t) {
			paths.add(s.getName());
		}

		for(Node p : s.getParents()) {
			if(p == t) {
				paths.add(s.getName()+"<"+p.getName());
			}
			for(Node c : p.getChildren()) {
				if(c==t) {
					paths.add(s.getName()+"<"+p.getName()+">"+c.getName());
				}
			}
		}
		if(!s.getChildren().isEmpty()) {
			for(Node c : s.getChildren()) {
				if (c == t) {
					paths.add(s.getName()+">"+c.getName());
				}
				else {
					getPath(c, t, s.getName(), paths);
				}
			}
		}
		return paths;
	}

	/**
	 * Subfunction for searching path, if there is a path, add it to pV vector
	 * @param s
	 * @param t
	 * @param path
	 * @param pV
	 * @return
	 */
	public String getPath(Node s, Node t, String path, Vector<String> pV) {

		for(Node p : s.getParents()) {
			if(p == t) {
				pV.add(path+">"+s.getName()+"<"+p.getName());
			}
		}

		for(Node c : s.getChildren()) {
			if (c == t || c.getChildren().size()==0) {
				if(c == t)
					pV.add(path+">"+s.getName()+">"+c.getName());
			}
			else {
				getPath(c, t, path+">"+s.getName(), pV);
			}
		}
		return path;
	}
	
	
	public Vector<Node> getBNlist() {
		return bNlist;
	}
	
	int MultCount;
	int SumCount;
	/**
	 * Variable Elimination Function
	 * @param evidences
	 * @param eliminations = elimination order
	 * @param resName = Key:which node Value:which value
	 * @return String with Probability,Sum Count, Mult Count
	 */
	public String VarElimination(HashMap<String, String> evidences, Vector<String> eliminations, Pair<String,String> resName){
		MultCount = 0;
		SumCount = 0;
		Vector<Cpt> cptVec = new Vector<Cpt>();
		// init cpt list for variable elimination computation
		for(Node node : bNlist) {
			cptVec.add(node.getCpt().clone());
		}
		
		// change all evidences in cpt
		for(String e : evidences.keySet()) {
			for(Cpt cpt : cptVec) {
				if(cpt.getName().contains(e)) {
					int index = cpt.getName().indexOf(e);
					cpt.getCpt().keySet().removeIf(k -> !k.get(index).equals(evidences.get(e)));
				}
			}
		}

		//elimination
		Vector<String> resNameVec = new Vector<String>();
		resNameVec.add(resName.getKey());
		Cpt resCpt = new Cpt(resNameVec);

		for(String eliminate : eliminations) {
			Vector<Cpt> candidates = new Vector<Cpt>();
			cptVec.removeIf(can->{
				if(can.getName().contains(eliminate)) {
					candidates.add(can);
					return true;
				}
				return false;
			});

			Cpt afterEliminationCpt = null;
			candidates.sort((x1,x2)->Integer.compare(x1.getCpt().keySet().size(), x2.getCpt().keySet().size()));
			for(Cpt candi : candidates) {//eliminate
				afterEliminationCpt = joint(afterEliminationCpt, candi);
			}
			if(afterEliminationCpt != null) {
				afterEliminationCpt = sum(afterEliminationCpt, eliminate);
			}
			cptVec.add(afterEliminationCpt);
		}
			Cpt res = joint(cptVec.get(0), cptVec.get(1));
			res = normalize(res);
			System.out.println(res);
			float floatResult = 0;
			for(Vector<String> r : res.getCpt().keySet()) {
				if(r.get(res.getName().indexOf(resName.getKey())).equals(resName.getValue())) {
					floatResult = res.getCpt().get(r);
				}
			}
			DecimalFormat df = new DecimalFormat("0.00000");
			String result = df.format(floatResult) + "," + SumCount + "," + MultCount;
			System.out.println(result);
			return result;
	}

	/**
	 * Normalize Cpt c
	 * @param c
	 * @return Normalized Cpt
	 */
	public Cpt normalize(Cpt c) {
		float nor = 0;
		for(Vector<String> k : c.getCpt().keySet()) {
			if(nor!=0) SumCount++;
			nor+=c.getCpt().get(k);
		}
		for(Vector<String> k : c.getCpt().keySet()) {
			c.getCpt().put(k, c.getCpt().get(k)/nor);
		}
		return c;
	}

	/**
	 * Joint function - fA x fB
	 * @param fA
	 * @param fB
	 * @return Cpt of db joint of fA x fB
	 */
	public Cpt joint(Cpt fA, Cpt fB) {

		//if null return second one
		if(fA == null) {
			return fB;
		}

		// Creation of name for result Cpt 
		Vector<String> newName = new Vector<String>();
		Vector<String> cptUkey = new Vector<String>();	//fA&fB (names)


		for(String n : fA.name)
			newName.add(n);
		for(String n : fB.name) {
			if(!newName.contains(n)) {
				newName.add(n);
			}
			else {
				cptUkey.add(n);
			}
		}
		Cpt newCpt = new Cpt(newName);
		// joint
		int lineCount = 0;
		for(Vector<String> r1 : fA.getCpt().keySet()){
			Vector<String> rowUkey = new Vector<String>();	
			Vector<String> newRowCpt = (Vector<String>) r1.clone();	//this row will be the start of new key for newCpt
			for(String uKey : cptUkey) {
				if(fA.getName().contains(uKey)) {
					rowUkey.add(r1.get(fA.getName().indexOf(uKey)));	//rowUkey initializer
				}
			}
			for(Vector<String> r2 : fB.getCpt().keySet()) {
				newRowCpt = (Vector<String>) r1.clone();
				if(isIn(rowUkey, cptUkey, r2, fB.getName())) {
					for(String n2 : fB.getName()) {
						if(!cptUkey.contains(n2)) {
							newRowCpt.add(r2.get(fB.getName().indexOf(n2)));

						}
					}
					newCpt.addCPT(newRowCpt, fB.getCpt().get(r2) * fA.getCpt().get(r1));
					MultCount++;
				}
			}
		}
		return newCpt;
	}
	
	/**
	 * Sum and eliminate function for Cpt
	 * @param c = cpt
	 * @param e = node to sum and eliminate
	 * @return	Cpt after sum and elimination
	 */
	public Cpt sum(Cpt c, String e) {
		//		System.out.println(c);
		int index = c.getName().indexOf(e);
		Vector<String> cNewName = (Vector<String>) c.getName().clone();	//new name
		cNewName.remove(index);
		Cpt cNew = new Cpt(cNewName);
		for(Vector<String> row : c.getCpt().keySet()) {
			Vector<String> tempRow = (Vector<String>) row.clone();

			float value = c.getCpt().get(tempRow); //new value

			tempRow.remove(index);
			if(cNew.getCpt().containsKey(tempRow)) {
				float tempValue = cNew.getCpt().get(tempRow) + value;
				SumCount++;
				cNew.getCpt().put(tempRow, tempValue);
			}
			else {
				cNew.getCpt().put(tempRow, value);
			}
		}
		return cNew;
	}

	/**
	 * Check if f Contains cKey
	 * @param cKey = cKey (fA&fB)
	 * @param cKeyName = Nodes in cKey
	 * @param f = Cpt
	 * @param fName = Nodes in Cpt
	 * @return if f contains cKey returns true
	 */
	private boolean isIn(Vector<String> cKey, Vector<String> cKeyName, Vector<String> f, Vector<String> fName) {
		int contains = 0;
		for(String cN : cKeyName) {
			for(String fN : fName) {
				if(fN.equalsIgnoreCase(cN)) {
					if(cKey.get(cKeyName.indexOf(cN)).equals(f.get(fName.indexOf(fN)))) {
						contains++;
					}
				}
			}
		}
		if(contains == cKey.size())
			return true;
		return false;
	}

	public String toString() {
		String res = "";
		for(Node n : bNlist) {
			res+= n + "\n";
		}
		return res;

	}
}
