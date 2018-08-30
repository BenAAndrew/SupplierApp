package application;

import java.util.ArrayList;

public class Supplier {
	private int supCode;
	private String supName;
	private Address supAddress;
	private SupRegion supRegion;
	private ArrayList<Product> supProducts = new ArrayList<Product>();
	private String productList = "";
	private String supRegionString;
	private String addressPrint;

	public Supplier(int supCode, String supName, Address supAddress, SupRegion supRegion, ArrayList<Product> supProducts) {
		this.supCode = supCode;
		this.supName = supName;
		this.supAddress = supAddress;
		this.supRegion = supRegion;
		this.supRegionString = supRegion.getRegionAsString();
		this.supProducts = supProducts;
		for (int i = 0; i < supProducts.size(); i++) {
			this.productList += supProducts.get(i).getProductDetails();
		}
		this.addressPrint = supAddress.getBldNum()+ " " +supAddress.getBldStreet()+ ", " +supAddress.getBldTown()+", " +supAddress.getBldCountry()+ ", " +supAddress.getBldPcode();
	}

	public int getSupCode() {
		return supCode;
	}

	public void setSupCode(int supCode) {
		this.supCode = supCode;
	}

	public String getSupName() {
		return supName;
	}

	public void setSupName(String supName) {
		this.supName = supName;
	}

	public Address getSupAddress() {
		return supAddress;
	}

	public void setSupAddress(Address supAddress) {
		this.supAddress = supAddress;
	}

	public SupRegion getSupRegion() {
		return supRegion;
	}

	public void setSupRegion(SupRegion supRegion) {
		this.supRegion = supRegion;
	}

	public ArrayList<Product> getSupProducts() {
		return supProducts;
	}

	public void setSupProducts(ArrayList<Product> supProducts) {
		this.supProducts = supProducts;
	}

	public String getProductList() {
		return productList;
	}

	//Joins all of the suppliers Product information in a single String assigned in the object which is needed for the table
	public void setProductList() {
		this.productList = "";
		for (Product i : supProducts) {
			this.productList += i.getProductDetails();
		}
	}

	public String getSupRegionString() {
		return supRegionString;
	}

	public void setSupRegionString(String supRegionString) {
		this.supRegionString = supRegionString;
	}

	public String getAddressPrint() {
		return addressPrint;
	}

	public void setAddressPrint(String addressPrint) {
		this.addressPrint = addressPrint;
	}
}
