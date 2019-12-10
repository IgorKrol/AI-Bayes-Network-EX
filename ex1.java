


public class ex1 {
	
	public static void main(String[] args) {
		System.out.println("Hello");
		BNlist bN = new BNlist();
		bN.bnFparse("input.txt");
		String[] e = {"J"};
		String[] f = null;
//		System.out.println(e.length);
//		String a = "A>B<C";
//		String[] b = a.split("[\\><]");
//		for (String string : b) {
//			System.out.println(string);
//		}
		System.out.println(bN.isDependent(bN.getBNlist().get(0),bN.getBNlist().get(1), e));
		System.out.println(bN.isDependent(bN.getBNlist().get(0),bN.getBNlist().get(1), f));
//		bN.isDependend(bN.getBNlist().get(1),bN.getBNlist().get(0), null);
//		bN.isDependend(bN.getBNlist().get(4),bN.getBNlist().get(3), null);
		
//		System.out.print("");
//		System.out.println(bN);
	}

}
