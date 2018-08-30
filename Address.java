package application;

public class Address {
	private int bldNum;
	private String bldStreet;
	private String bldTown;
	private String bldPcode;
	private String bldCountry;

	public Address(int bldNum, String bldStreet, String bldTown, String bldPcode, String bldCountry) {
		this.bldNum = bldNum;
		this.bldStreet = bldStreet;
		this.bldTown = bldTown;
		this.bldPcode = bldPcode;
		this.bldCountry = bldCountry;
	}
	
	//Formats address to be written to text file when Supplier is updated
	public String getFullAddress() {
		return String.valueOf(bldNum) +","+ bldStreet +","+ bldTown +","+ bldPcode +","+ bldCountry;
	}

	public int getBldNum() {
		return bldNum;
	}

	public void setBldNum(int bldNum) {
		this.bldNum = bldNum;
	}

	public String getBldStreet() {
		return bldStreet;
	}

	public void setBldStreet(String bldStreet) {
		this.bldStreet = bldStreet;
	}

	public String getBldTown() {
		return bldTown;
	}

	public void setBldTown(String bldTown) {
		this.bldTown = bldTown;
	}

	public String getBldPcode() {
		return bldPcode;
	}

	public void setBldPcode(String bldPcode) {
		this.bldPcode = bldPcode;
	}

	public String getBldCountry() {
		return bldCountry;
	}

	public void setBldCountry(String bldCountry) {
		this.bldCountry = bldCountry;
	}
}
