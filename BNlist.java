

import java.util.HashMap;
import java.util.Vector;

public class BNlist {

	Vector<Node> bNlist;

	public BNlist() {
		bNlist = new Vector<Node>();
	}

	public void bnFparse(String path) {
		try {
			BnFileParser fp = new BnFileParser(path, this);
			fp.parse();
		} catch (Exception e) {
			System.err.println(e);
		}
	}
	/* Dependency check, evidence = all given evidence.
	 * returns true - if dependent, else: false */
	public boolean isDependent(Node start, Node target, String[] evidence) {
		Vector<String> paths = getAllPaths(start, target);
		if(paths.size()==0) {
			return true;
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
				System.out.println("yes");
				return false;
			}
			else {
				System.out.println("no");
				return true;
			}
		}
	}
	/*  search 's' children for evidence from 'e' */
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
	/* RECURSIVLY search 's' children for evidence from 'e'  */
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

	/* finds all paths s->t */
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

	/* recursive path finder s->t*/
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
	
	public void VarElimination(Vector<String> elim){
		Vector<Cpt> cptVec = new Vector<Cpt>();
		for(Node node : bNlist) {
			cptVec.add(node.getCpt());
		}
		for(String e : elim) {
			for(Cpt cpt : cptVec) {
				if(cpt.getName().contains(e)) {
					
				}
			}
		}
		
	}
	/*	Joint function for variable elimination	*/
	public Cpt joint(Cpt fA, Cpt fB) {
		// Creation of name for result Cpt 
		Vector<String> newName = new Vector<String>();
		Vector<String> cptKey = new Vector<String>();

		for(String n : fA.name)
			newName.add(n);
		for(String n : fB.name) {
			if(!newName.contains(n)) {
				newName.add(n);
			}
			else {
				cptKey.add(n);
			}
		}
		
		Cpt newCpt = new Cpt(newName);
		// joint
		for(Vector<String> i : fA.getCpt().keySet()) {
			Vector<String> cKey = new Vector<>();
			double cValue = fA.getCpt().get(i);
			for(int k = 0; k < fA.getName().size(); k++) {
				if(cptKey.contains(fA.getName().get(k))) {
					cKey.add(i.get(k));
				}
			}
			
			for(Vector<String> j : fB.getCpt().keySet()) {
				if(isIn(cKey,j)){
					Vector<String> resKey = (Vector<String>)i.clone();
					int index = 0;
					for(String n: fB.name) {
						if(!fA.name.contains(n)) {
							resKey.add(j.get(index));
						}
						index++;
					}
					newCpt.addCPT(resKey, cValue * fB.getCpt().get(j));
				}
			}
		}	
		return newCpt;
	}
	/*	summing function of variable elimination	*/
	public Cpt sum(Cpt c, String e) {
		int index = c.getName().indexOf(e);
		c.getName().remove(index);
		Cpt cNew = new Cpt(c.getName());
		for(Vector<String> row : c.getCpt().keySet()) {
			double value = c.getCpt().get(row);
			row.remove(index);
			if(cNew.getCpt().containsKey(row)) {
				cNew.getCpt().put(row, value+cNew.getCpt().get(row));
			}
			else {
				cNew.getCpt().put(row, value);
			}
		}
		return cNew;
	}
	
	/*	function for check if j contains cKey	*/
	private boolean isIn(Vector<String> cKey, Vector<String> j) {
		int contains = 0;
		for(String k : cKey) {
			for(String c : j) {
				if(k.equals(c))
					contains++;
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
