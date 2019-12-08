package afda;


public class ex1 {
	
	public static void main(String[] args) {
		System.out.println("Hello");
		BNlist bN = new BNlist();
		bN.bnFparse("D:\\eclipse\\eclipse-workspace\\BSproj\\src\\afda\\input.txt");
		System.out.print("");
		System.out.println(bN);
	}

}
