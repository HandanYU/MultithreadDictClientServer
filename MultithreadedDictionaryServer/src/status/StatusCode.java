/**
 * 
 */
/**
 * @author yuhandan
 *
 */
package status;
public class StatusCode{
	public final static int UNSEEN = 0; // the word does not exist in dictionary
	public final static int EMPTY = 1; // the client's input is empty
	public final static int SUCCESS = 2; // operation succeed
	public final static int EXIST = 3; // the word exists in dictionary
	
	
	public final static int QUERY = 4;
	public final static int ADD = 5;
	public final static int REMOVE = 6;
	public final static int UPDATE = 7;
	
	// exception
	public final static int FAIL = 8;

	public final static int ESC = -1;
	public final static int CONNECT = 10;
}