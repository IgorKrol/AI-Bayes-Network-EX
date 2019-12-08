package afda;

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
		while((line = br.readLine()).equalsIgnoreCase("Queries")) {
			while(!line.isBlank()) {
				int index = bn.indexOf(line.substring(4));
				line = br.readLine();
				line = line.substring(line.indexOf(':') + 2);
				tempString = line.split(",");
				bn.get(index).addValues(tempString);
				line = br.readLine();
				line = line.substring(line.indexOf(':') + 2);
				tempString = line.split(",");
				for(String i : tempString) {
					bn.get(index).addParents(bn.get(bn.indexOf(i)));
				}
				br.readLine();
				while(!(line = br.readLine()).isBlank()) {
					String tempV = line.substring(line.lastIndexOf(','));
					String tempK = line.substring(0, line.lastIndexOf(','));
					bn.get(index).addCPT(tempK, Double.parseDouble(tempV));
				}
			}
		}// end variables
		
	}


}
