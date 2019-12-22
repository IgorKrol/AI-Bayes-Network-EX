
import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Ex1 {
	
	public static void main(String[] args) throws IOException {

		BNlist bN = new BNlist();
		String str = bN.bnFparse("input.txt");
	    BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
	    writer.write(str);
	     
	    writer.close();
	}

}
