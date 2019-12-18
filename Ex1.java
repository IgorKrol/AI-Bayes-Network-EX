
import java.io.*;

public class Ex1 {
	
	public static void main(String[] args) throws IOException {
		System.out.println("Hello");
		BNlist bN = new BNlist();
		bN.bnFparse("input3.txt");
		String[] e = {"J"};
		String[] f = null;
		
		String str = bN.toString();
		System.out.println(str);
//	    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
//	    writer.write(str);
	     
//	    writer.close();
	}

}
