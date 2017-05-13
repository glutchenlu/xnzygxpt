package develop.cl.com.crsp.JavaBean;


public class Message {

	private int messageid;
	private String userid;
	private String message;
	private String message_class;
	private String message_time;

	public int getMessageid() {
		return messageid;
	}

	public void setMessageid(int messageid) {
		this.messageid = messageid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage_class() {
		return message_class;
	}

	public void setMessage_class(String message_class) {
		this.message_class = message_class;
	}

	public String getMessage_time() {
		return message_time;
	}

	public void setMessage_time(String message_time) {
		this.message_time = message_time;
	}

}
