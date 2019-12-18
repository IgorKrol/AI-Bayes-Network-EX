
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Ex1 {
	
	public static void main(String[] args) throws IOException {
		Vector<String> v = new Vector<String>();
		v.add("A");
		v.add("B");
		v.add("C");
		Cpt c = new Cpt(v);
		v = new Vector<String>();
		v.add("True");
		v.add("True");
		v.add("True");
		c.addCPT(v, 0.25);
		v = new Vector<String>();
		v.add("True");
		v.add("True");
		v.add("False");
		c.addCPT(v, 0.25);
		v = new Vector<String>();
		v.add("True");
		v.add("False");
		v.add("True");
		c.addCPT(v, 0.25);
		v = new Vector<String>();
		v.add("True");
		v.add("False");
		v.add("False");
		c.addCPT(v, 0.25);
		
//		System.out.println(c);
		for(Vector<String> vec : c.getCpt().keySet()) {
			System.out.println(c.getCpt().get(vec));
			for(String s : vec) {
				System.out.print(s+", ");
			}
			System.out.println();
		}
		
		int index = c.getName().indexOf("C");
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
		
		System.out.println(cNew);
		
//		for(Vector<String> vec : c.getCpt().keySet()) {
//			for(String s : vec) {
//
//				System.out.print(s+", ");
//			}
//			System.out.println();
//		}
		
		
//		for(Vector<String> row : c.getCpt().keySet()) {
//			Vector<String> tempRow = (Vector<String>) row.clone();
//			double val = 0;
//			for(Vector<String> r : c.getCpt().keySet()) {
//				c.getCpt().get(row);
//			}
//			c.getCpt().put(tempRow, val);
//		}
		
//		for(Vector<String> vec : c.getCpt().keySet()) {
//			for(String s : vec) {
//				System.out.print(s+", ");
//			}
//			System.out.println();
//		}
//		System.out.println("Hello");
//		BNlist bN = new BNlist();
//		bN.bnFparse("input.txt");
//		String[] e = {"J"};
//		String[] f = null;
//		
//		String str = bN.toString();
//		System.out.println(str);
//	    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
//	    writer.write(str);
	     
//	    writer.close();
	}

}
