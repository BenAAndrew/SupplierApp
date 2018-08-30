package application;

public class Product {
	private int proCode;
	private String proMake;
	private String proModel;
	private double proPrice;
	private int proQtyAvailable;
	private boolean proDiscontinued;

	public Product(int proCode, String proMake, String proModel, double proPrice, int proQtyAvailable, boolean proDiscontinued) {
		this.proCode = proCode;
		this.proMake = proMake;
		this.proModel = proModel;
		this.proPrice = proPrice;
		this.proQtyAvailable = proQtyAvailable;
		this.proDiscontinued = proDiscontinued;
	}

	/* 
	 * get Product Details prints the current product's properties
	 * @return String
	 */
	public String getProductDetails() {
		String out = "Code: " + Integer.toString(getProCode()) + ", Make: " + getProMake() + ", Model: "+ getProModel();
		out += ", Price: " + Double.toString(getProPrice()) + ", Quantity: " + Integer.toString(getProQtyAvailable());
		if (getProDiscontinued()) {
			out += ", discontinued\n";
		} else {
			out += ", not discontinued\n";
		}
		return out;
	}

	public int getProCode() {
		return proCode;
	}

	public void setProCode(int proCode) {
		this.proCode = proCode;
	}

	public String getProMake() {
		return proMake;
	}

	public void setProMake(String proMake) {
		this.proMake = proMake;
	}

	public String getProModel() {
		return proModel;
	}

	public void setProModel(String proModel) {
		this.proModel = proModel;
	}

	public double getProPrice() {
		return proPrice;
	}

	public void setProPrice(double proPrice) {
		this.proPrice = proPrice;
	}

	public int getProQtyAvailable() {
		return proQtyAvailable;
	}

	public void setProQtyAvailable(int proQtyAvailable) {
		this.proQtyAvailable = proQtyAvailable;
	}

	public boolean getProDiscontinued() {
		return proDiscontinued;
	}

	public void setProDiscontinued(boolean proDiscontinued) {
		this.proDiscontinued = proDiscontinued;
	}
}
