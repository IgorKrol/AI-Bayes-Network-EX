

import java.io.*;
import java.util.Vector;

public class BnFileParser {

	BufferedReader br;
	Vector<Node> bn;

	public BnFileParser(String path, Vector<Node> b){
		bn = b;
		File file = new File(path);
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void parse() throws IOException {
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
				while(!(line = br.readLine()).isEmpty()) {
					String tempV = line.substring(line.lastIndexOf(',')+1);
					String tempK = line.substring(0, line.lastIndexOf(','));
					bn.get(index).addCPT(tempK, Double.parseDouble(tempV));
				}
			}
		}// end variables

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
