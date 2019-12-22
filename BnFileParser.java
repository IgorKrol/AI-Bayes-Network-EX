

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

/**
 *	this class parses input into nodes and executes queries 
 * @author igork
 *
 */
public class BnFileParser {

	BufferedReader br;
	Vector<Node> bn;
	BNlist bNet;
	String result;

	/**
	 * initializer
	 * @param path = files path (input.txt for this assignment)
	 * @param b = Bayesian Network list
	 */
	public BnFileParser(String path, BNlist b){
		result = "";
		bNet = b;
		bn = b.getBNlist();
		File file = new File(path);
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public String parse() throws IOException {
		br.readLine();


		/*	Variables	*/
		String line = br.readLine();
		line = line.substring(line.indexOf(':') + 2);
		String[] tempString = line.split(",");
		for(String i : tempString) {
			bn.add(new Node(i));
		}

		/*	Info for variables	*/
		while(!(line = br.readLine()).equalsIgnoreCase("Queries")) {
			while(!line.isEmpty()) {
				int index = indexOfNodeByName(line.substring(4));
				line = br.readLine();
				line = line.substring(line.indexOf(':') + 2);
				tempString = line.split(",");
				bn.get(index).addValues(tempString);

				line = br.readLine();
				addParentsToNode(line, index);

				br.readLine();
				//cpt
				bn.get(index).newCPT();
				while(!(line = br.readLine()).isEmpty()) {
					String[] tempSplitCPT = line.split("=");
					Vector<String> tempVec;
					if(tempSplitCPT[0].isEmpty()) {
						tempVec = new Vector<String>();
					}
					else {
						tempVec= new Vector<String>(Arrays.asList(tempSplitCPT[0].split(","))); 	
					}
					float lastValue = 1;
					for(int i = 1; i < tempSplitCPT.length; i++) {
						Vector<String> tempVecCopy = (Vector<String>) tempVec.clone(); 
						String[] tempKVcpt = tempSplitCPT[i].split(",");
						//						System.out.println(Arrays.deepToString(tempVecCopy.toArray()));;
						tempVecCopy.add(tempKVcpt[0]);
						float value = Float.parseFloat(tempKVcpt[1]);
						lastValue-=value;
						bn.get(index).addCPT(tempVecCopy, value);
					}
					tempVec.add(bn.get(index).getValue(-1));
					bn.get(index).addCPT(tempVec, lastValue);
				}
			}
		}// end variables
		while((line = br.readLine()) != null) {
			//Dependency check
			if(line.charAt(0) != 'P') {
				String[] fSplit = line.split("\\|");
				String[] varSplit = fSplit[0].split("-");
				if(fSplit.length > 1) {
					String[] evidence = fSplit[1].split(",");
					for(int i = 0; i < evidence.length; i++) {
						evidence[i] = evidence[i].split("=")[0];
					}
					result+=bNet.isDependent(bn.get(indexOfNodeByName(varSplit[0])), bn.get(indexOfNodeByName(varSplit[1])), evidence)+"\n";
				}
				else {
					result+=bNet.isDependent(bn.get(indexOfNodeByName(varSplit[0])), bn.get(indexOfNodeByName(varSplit[1])), null)+"\n";
				}
			}
			//Variable elimination
			else {
				String eliTemp = line.substring(line.lastIndexOf(',')+1);
				Vector<String> eliminations = new Vector<String>();
				eliminations.addAll(Arrays.asList(eliTemp.split("-")));
				String temp_var_evi = line.substring(line.indexOf('(')+1,line.indexOf(')'));

				String eviString = temp_var_evi.substring(temp_var_evi.indexOf('|')+1);
				String[] eviArr = eviString.split(",");
				HashMap<String, String> evidences = new HashMap<String, String>();
				for(String e : eviArr) {
					String[] e_v = e.split("=");
					evidences.put(e_v[0], e_v[1]);
				}

				String[] resNameTemp = temp_var_evi.substring(0, temp_var_evi.indexOf('|')).split("=");
				Pair<String, String> resName = new Pair<String, String>(resNameTemp[0],resNameTemp[1]);

				//check if query can be executed without variable elimination
				boolean needVarElimination = true;
				int count_evidences_in_parents = 0;
				Vector<String> quick_key = new Vector<String>();
				Node n = bn.get(indexOfNodeByName(resName.key));
				if(evidences.keySet().size() == n.getParents().size()) {
					for(String evidence : evidences.keySet()) {
						if(n.containsParent(evidence)) {
							count_evidences_in_parents++;
						}
					}
					if(count_evidences_in_parents == evidences.keySet().size()) {
						needVarElimination = false;
						Cpt c = n.getCpt();
						for(String name : c.getName()) {
							if(evidences.containsKey(name)) {
								quick_key.add(evidences.get(name));
							}
							else {
								quick_key.add(resName.getValue());
							}
						}
						DecimalFormat df = new DecimalFormat("0.00000");
						result+= df.format(c.getCpt().get(quick_key))+",0,0\n";
					}

				}
				if(needVarElimination)
					result+=bNet.VarElimination(evidences, eliminations, resName)+"\n";
			}
		}
		return result;
	}

	public void addParentsToNode(String line, int index) {
		String t = line.substring(line.indexOf(':') + 2);
		if(!t.equalsIgnoreCase("none")) {
			String[] tempString = t.split(",");
			for(String i : tempString) {
				bn.get(index).addParents(bn.get(indexOfNodeByName(i)));
			}
		}
	}

	public int indexOfNodeByName(String n) {
		int index = -1;
		for(Node nod : bn) {
			index++;
			if(nod.getName().equalsIgnoreCase(n)) {
				return index;
			}
		}
		return -1;
	}
}
