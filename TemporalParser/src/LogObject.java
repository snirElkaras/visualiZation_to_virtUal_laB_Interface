
public class LogObject {
	private String name;
	private String id;
	private String lab_type;
	private String timestamp;
	private String format;
	private String action;
	private String unnecessaryTab1;
	private String unnecessaryTab2;
	private String type;
	private String message;
	
	public LogObject(String name, String id, String lab_type, String timestamp,
			String format, String action, String unnecessaryTab1,
			String unnecessaryTab2, String type, String message) {
		this.name = name;
		this.id = id;
		this.lab_type = lab_type;
		this.timestamp = timestamp;
		this.format = format;
		this.action = action;
		this.unnecessaryTab1 = unnecessaryTab1;
		this.unnecessaryTab2 = unnecessaryTab2;
		this.type = type;
		this.message = message;
		
	}
	
	public LogObject(String[] split) {
		this.name = split[0];
		this.id = split[1];
		this.lab_type = split[2];
		this.timestamp = split[3];
		this.format = split[4];
		this.action = split[5];
		this.unnecessaryTab1 = split[6];
		this.unnecessaryTab2 = split[7];
		this.type = split[8];
		this.message = split[9];
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLab_type() {
		return lab_type;
	}

	public void setLab_type(String lab_type) {
		this.lab_type = lab_type;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUnnecessaryTab1() {
		return unnecessaryTab1;
	}

	public void setUnnecessaryTab1(String unnecessaryTab1) {
		this.unnecessaryTab1 = unnecessaryTab1;
	}

	public String getUnnecessaryTab2() {
		return unnecessaryTab2;
	}

	public void setUnnecessaryTab2(String unnecessaryTab2) {
		this.unnecessaryTab2 = unnecessaryTab2;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String toString(){
		return this.name+"$"+this.id+"$"+this.lab_type+"$"+this.timestamp+"$"+this.format+"$"+this.action+"$"+this.type+"$"+this.message;
	}
	
	public void print(){
		System.out.println(toString());;
	}
	
	
}
