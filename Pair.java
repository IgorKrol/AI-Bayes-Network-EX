
/**
 * this class represent simple pair {key=value}
 * @author igork
 *
 * @param <T>
 * @param <U>
 */
public class Pair<T,U> {
	
	T key;
	U value;
	
	public Pair(T key, U value) {
		this.key = key;
		this.value = value;
	}
	public String toString() {
		return key+"="+value;
	}
	public T getKey() {
		return key;
	}
	public void setKey(T key) {
		this.key = key;
	}
	public U getValue() {
		return value;
	}
	public void setValue(U value) {
		this.value = value;
	}
	
	
}
