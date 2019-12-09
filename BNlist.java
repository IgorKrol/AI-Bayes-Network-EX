

import java.util.Vector;

public class BNlist {

	Vector<Node> bNlist;

	public BNlist() {
		bNlist = new Vector<Node>();
	}

	public void bnFparse(String path) {
		try {
			BnFileParser fp = new BnFileParser(path, bNlist);
			fp.parse();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

	
	
	public String toString() {
		String res = "";
		for(Node n : bNlist) {
			res+= n + "\n";
		}
		return res;
		
	}
}
