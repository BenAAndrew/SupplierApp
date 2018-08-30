package application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.*;

import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.collections.*;
import javafx.scene.text.*;
import javafx.scene.*;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;

public class Controller implements Initializable {
	private static URL location;
	@FXML private TableView<Supplier> table;
	@FXML private TableColumn<Supplier, String> supplierColumn;
	@FXML private TableColumn<Supplier, String> regionColumn;
	@FXML private TableColumn<Product, String> productColumn;
	@FXML private TableColumn<Product, String> addressColumn;
	@FXML private TableColumn<Product, String> codeColumn; 
	@FXML private ObservableList<Supplier> data = FXCollections.observableArrayList();
	
	@FXML private ComboBox<String> filter;
	@FXML private TextField searchItem;
	@FXML private TextField minimum;
	@FXML private TextField maximum;
	
	@FXML private Label title;
	@FXML private Label reportCode;
	@FXML private Label reportAddress;
	@FXML private Label reportRegion;
	
	@FXML private TableView<Product> reportTable;
	@FXML private TableColumn<Product, String> reportColumnCode;
	@FXML private TableColumn<Product, String> reportColumnMake;
	@FXML private TableColumn<Product, String> reportColumnModel;
	@FXML private TableColumn<Product, String> reportColumnPrice;
	@FXML private TableColumn<Product, String> reportColumnQty;
	@FXML private TableColumn<Product, String> reportColumnDisc;
	
	@FXML private ComboBox<String> productCombo;
	@FXML private ComboBox<String> productAction;
	@FXML private Label currentValue;
	@FXML private TextField updateValue;
	
	@FXML private TextField newCode;
	@FXML private TextField newMake;
	@FXML private TextField newModel;
	@FXML private TextField newPrice;
	@FXML private TextField newQty;
	
	@FXML private TextField newName;
	@FXML private TextField newStreet;
	@FXML private TextField newTown;
	@FXML private TextField newPcode;
	@FXML private TextField newCountry;
	@FXML private ComboBox<String> supRegion;
	
	/*
	 * Get all data uses reader to generate products and suppliers from the text file data.txt
	 * @return Arraylist<Supplier>
	 */
	public static ArrayList<Supplier> getAllData() {
		ArrayList<Supplier> items = new ArrayList<Supplier>();
		//Divides text into Supplier & Product groups
		String[] data = ReadWrite.reader().split("&&");
		int totalSuppliers = data.length;
		for (int i = 0; i < totalSuppliers; i++) {
			//Divides Supplier & Product into their properties
			String[] currentSupplier = data[i].split(",");
			int supCode = Integer.valueOf(currentSupplier[0].substring(1));
			String supName = currentSupplier[1];
			SupRegion region = SupRegion.valueOf(currentSupplier[7]);
			Address address = new Address(Integer.valueOf(currentSupplier[2]), currentSupplier[3], currentSupplier[4],
					currentSupplier[5], currentSupplier[6]);
			ArrayList<Product> products = new ArrayList<Product>();
			//Supplier is 8 indexes long and each Product is 6 indexes long
			int totalProducts = (currentSupplier.length - 8) / 6;
			for (int j = 0; j < totalProducts; j++) {
				int index = 8 + (j * 6);
				Product product = new Product(Integer.valueOf(currentSupplier[index]), currentSupplier[index + 1],
						currentSupplier[index + 2], Double.valueOf(currentSupplier[index + 3]),
						Integer.valueOf(currentSupplier[index + 4]), Boolean.valueOf(currentSupplier[index + 5]));
				products.add(product);
			}
			Supplier supplier = new Supplier(supCode, supName, address, region, products);
			items.add(supplier);
		}
		return items;
	}

	/*
	 * Uses getAllData to fetch Supplier data and then applies this to an observableList for javaFx table
	 * @return Arraylist<Supplier>
	 */
	public ObservableList<Supplier> getData() {
		ArrayList<Supplier> items = getAllData();
		data.addAll(items);
		return data;
	}

	/*
	 * Gets the current window and removes text containing the directory in which the system exists
	 * @return String
	 */
	public static String getCurrentWindowFile(int fileNameLength) {
		return location.toString().substring(location.toString().length() - fileNameLength, location.toString().length());
	}

	//Initialize is the main function in a JavaFX controller. Here it sets window content including tables
	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		try {
			System.out.println(url);
			location = url;
			getAllData();
			if(getCurrentWindowFile(30).contains("SearchProduct")) {
				filter.setValue(Main.activeProOption);
			} else {
				Main.activeProOption = null;
			}
			//Used to store filter options for productSearch between refreshes
			if (getCurrentWindowFile(11).equals("Report.fxml") || getCurrentWindowFile(14).equals("ReportQty.fxml")) {
				buildReport();
			} else if (getCurrentWindowFile(16).equals("ProductEdit.fxml")) {
				buildProUpdate();
			} else if (getCurrentWindowFile(18).equals("ProductDelete.fxml")) {
				buildProDelete();
			} else if (getCurrentWindowFile(15).equals("ProductAdd.fxml")) {
				buildProAdd();
			} else if (getCurrentWindowFile(17).equals("SupplierEdit.fxml")) {
				buildSupUpdate();
			} //else if (getCurrentWindowFile(17).equals("SupplierAdd2.fxml")) {
				//FIX
				//buildSupAdd();} 
			else if (getCurrentWindowFile(19).equals("UpdateSupplier.fxml")
					|| getCurrentWindowFile(19).equals("SupplierDelete.fxml")
					|| getCurrentWindowFile(19).equals("ProductAddMain.fxml")) {
				supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supName"));
				codeColumn.setCellValueFactory(new PropertyValueFactory<>("supCode"));
				regionColumn.setCellValueFactory(new PropertyValueFactory<>("supRegionString"));
				addressColumn.setCellValueFactory(new PropertyValueFactory<>("addressPrint"));
				table.setItems(getData());
			} else {
				supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supName"));
				regionColumn.setCellValueFactory(new PropertyValueFactory<>("supRegionString"));
				productColumn.setCellValueFactory(new PropertyValueFactory<>("productList"));
				table.setItems(getData());
			}
		} catch (Exception e) {
		}
	}

	
	public static String getLocation() {
		return location.toString();
	}

	@FXML
	public void main(ActionEvent event) {
		Main.createWindow("Main.fxml");
	}

	@FXML
	public void searchSupplier(ActionEvent event) {
		Main.createWindow("SearchSupplier.fxml");
	}

	@FXML
	public void updateProduct(ActionEvent event) {
		Main.createWindow("UpdateProduct.fxml");
	}

	@FXML
	public void searchProducts(ActionEvent event) {
		Main.createWindow("SearchProduct.fxml");
	}

	@FXML
	public void updateStock(ActionEvent event) {
		Main.createWindow("UpdateStock.fxml");
	}

	@FXML
	public void updateSupplier(ActionEvent event) {
		Main.createWindow("UpdateSupplier.fxml");
	}

	@FXML
	public void addSupplier(ActionEvent event) {
		Main.createWindow("SupplierAdd.fxml");
	}

	@FXML
	public void addProduct(ActionEvent event) {
		Main.createWindow("ProductAddMain.fxml");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@FXML
	public void searchSupplierSubmit(ActionEvent event) {
		if(filter.getValue() != null && searchItem.getText().length() != 0) {
			ArrayList<Supplier> foundSuppliers = Search.supplier(filter.getValue(), searchItem.getText(), getAllData());
			if(foundSuppliers.size() > 0) {
				data.removeAll(data);
				data.addAll(foundSuppliers);
				table.setItems(data);
			} else {
				error("No results found");
			}
		} else {
			error("Invalid search data");
		}
	}

	public void searchProductsType(ActionEvent event) {
		Main.activeProOption = filter.getValue();
		Main.createWindow(Search.productsType(filter.getValue()));
	}

	public boolean getSearchItemFound(Product currentProduct) {
		boolean found = false;
		if(filter.getValue() != null) {
			try {
				switch (filter.getValue()) {
					case "Make":
						if (currentProduct.getProMake().toLowerCase().contains(searchItem.getText().toLowerCase())) {found = true;} break;
					case "Model":
						if (currentProduct.getProModel().toLowerCase().contains(searchItem.getText().toLowerCase())) {found = true;} break;
					case "Code":
						if (Integer.toString(currentProduct.getProCode()).contains(searchItem.getText())) {found = true;} break;
					case "Price":
						if ((Integer.valueOf(minimum.getText()) <= currentProduct.getProPrice())
								&& (Integer.valueOf(maximum.getText()) >= currentProduct.getProPrice())) {found = true;} break;
					case "Quantity":
						if (Integer.valueOf(minimum.getText()) <= currentProduct.getProQtyAvailable()) {found = true;} break;
					case "Discontinued":
						if (currentProduct.getProDiscontinued()) {found = true;} break;
					case "Not Discontinued":
						if (!(currentProduct.getProDiscontinued())) {found = true;} break;
					default:
						break;
				} 
			} catch (Exception e) {}	
		}
		return found;
		/*
		 * if(filter.getValue().equals("Make")) {
		 * if(currentProduct.getProMake().toLowerCase().contains(searchItem.getText().
		 * toLowerCase())) { found = true; } } if(filter.getValue().equals("Model")) {
		 * if(currentProduct.getProModel().toLowerCase().contains(searchItem.getText().
		 * toLowerCase())) { System.out.println(currentProduct.getProModel()); found =
		 * true; } } if(filter.getValue().equals("Code")) {
		 * if(Integer.toString(currentProduct.getProCode()).contains(searchItem.getText(
		 * ))) { found = true; } } if(filter.getValue().equals("Price")) {
		 * if((Integer.valueOf(minimum.getText()) <= currentProduct.getProPrice()) &&
		 * (Integer.valueOf(maximum.getText()) >= currentProduct.getProPrice())) { found
		 * = true; } } if(filter.getValue().equals("Quantity")) {
		 * if(Integer.valueOf(minimum.getText()) <= currentProduct.getProQtyAvailable())
		 * { found = true; } } if(filter.getValue().equals("Discontinued")) {
		 * if(currentProduct.getProDiscontinued()) { found = true; } }
		 * if(filter.getValue().equals("Not Discontinued")) {
		 * if(!(currentProduct.getProDiscontinued())) { found = true; } } return found;
		 */
	}

	@FXML
	public void searchProductSubmit(ActionEvent event) {
		try {		
			ArrayList<Supplier> allSuppliers = getAllData();
			ArrayList<Supplier> finalSuppliers = new ArrayList<Supplier>();
			for (Supplier i : allSuppliers) {
				ArrayList<Product> currentProducts = i.getSupProducts();
				ArrayList<Product> finalProducts = new ArrayList<Product>();
				for (Product j : currentProducts) {
					if (getSearchItemFound(j)) {
						finalProducts.add(j);
					}
				}
				if (finalProducts.size() > 0) {
					i.setSupProducts(finalProducts);
					i.setProductList();
					finalSuppliers.add(i);
				}
			}
			boolean errorAlert = false;
			if(getCurrentWindowFile(18).equals("SearchProduct.fxml") || getCurrentWindowFile(16).equals("UpdateStock.fxml") ||  getCurrentWindowFile(18).equals("UpdateProduct.fxml")) {
				if(searchItem.getText().length() == 0) {
					error("No search data entered"); errorAlert = true;
				}
			} else if(getCurrentWindowFile(23).equals("SearchProductPrice.fxml")) {
				if(minimum.getText().length() == 0 || maximum.getText().length() == 0) {
					error("No search data entered"); errorAlert = true;
				}
			} else if(getCurrentWindowFile(26).equals("SearchProductQuantity.fxml")) {
				if(minimum.getText().length() == 0) {
					error("No search data entered"); errorAlert = true;
				}
			} 
			if(finalSuppliers.size() == 0) {
				if(!errorAlert) {
					error("Invalid search data/ No results");
				}
			} else {
				data.removeAll(data);
				data.addAll(finalSuppliers);
				table.setItems(data);
			}
		} catch (Exception e) {}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@FXML
	public void getSelectedRow(MouseEvent event) {
		if (event.getClickCount() == 2) // Checking double click
		{
			try {
				Main.activeSupplier = table.getSelectionModel().getSelectedItem().getSupName();
				if (getCurrentWindowFile(16).equals("UpdateStock.fxml")) {
					createQtyUpdate();
				} else if (getCurrentWindowFile(18).equals("UpdateProduct.fxml")) {
					createProUpdate();
				} else if (getCurrentWindowFile(19).equals("UpdateSupplier.fxml")) {
					createSupUpdate();
				} else if (getCurrentWindowFile(19).equals("ProductAddMain.fxml")) {
					Main.createWindow("ProductAdd.fxml");
				} else {
					Main.createWindow("Report.fxml");
				}
			} catch (Exception e) {
			}
		}
	}

	public ObservableList<Product> getProductTable(ArrayList<Product> products) {
		ObservableList<Product> productData = FXCollections.observableArrayList();
		productData.addAll(products);
		return productData;
	}

	@FXML
	public void buildReport() {
		title.setText(Main.activeSupplier);
		ArrayList<Supplier> items = getAllData();
		Supplier supplier = null;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getSupName().contains(title.getText())) {
				supplier = items.get(i);
			}
		}
		reportCode.setText(Integer.toString(supplier.getSupCode()));
		Address address = supplier.getSupAddress();
		reportAddress.setText(address.getBldNum() + " " + address.getBldStreet() + ", " + address.getBldTown() + ", \n"
				+ address.getBldPcode() + ", " + address.getBldCountry());
		reportRegion.setText(supplier.getSupRegionString());
		reportColumnCode.setCellValueFactory(new PropertyValueFactory<>("proCode"));
		reportColumnMake.setCellValueFactory(new PropertyValueFactory<>("proMake"));
		reportColumnModel.setCellValueFactory(new PropertyValueFactory<>("proModel"));
		reportColumnPrice.setCellValueFactory(new PropertyValueFactory<>("proPrice"));
		reportColumnQty.setCellValueFactory(new PropertyValueFactory<>("proQtyAvailable"));
		reportColumnDisc.setCellValueFactory(new PropertyValueFactory<>("proDiscontinued"));
		reportTable.setItems(getProductTable(supplier.getSupProducts()));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@FXML
	public void getSelectedReportRow(MouseEvent event) {
		if (event.getClickCount() == 2) // Checking double click
		{
			try {
				/*
				 * System.out.println("abc"); Product product =
				 * reportTable.getSelectionModel().getSelectedItem(); String productCode =
				 * Integer.toString(product.getProCode()); String productQty =
				 * Integer.toString(product.getProQtyAvailable());
				 * 
				 * TextInputDialog dialog = new TextInputDialog(productQty);
				 * dialog.setTitle("Update product: "+productCode);
				 * dialog.setHeaderText("Please change the quantity of this product below");
				 * dialog.setContentText("New quantity:"); Optional<String> newQty =
				 * dialog.showAndWait();
				 * 
				 * if (newQty.isPresent()){
				 * product.setProQtyAvailable(Integer.valueOf(newQty.get()));
				 * System.out.println(Integer.toString(product.getProQtyAvailable())); }
				 * 
				 * reportTable.refresh();
				 */
			} catch (Exception e) {
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// EDIT DECISION//
	public void deleteOrEdit(boolean product) {
		String title = "";
		if (product) {
			title = "product";
		} else {
			title = "supplier";
		}
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Product decision");
		alert.setHeaderText("Would you like to edit or delete a " + title + "?");
		ButtonType edit = new ButtonType("Edit");
		ButtonType delete = new ButtonType("Delete");
		ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
		alert.getButtonTypes().setAll(edit, delete, cancel);
		Optional<ButtonType> result = alert.showAndWait();
		if (product) {
			if (result.get() == edit) {
				Main.createWindow("ProductEdit.fxml");
			} else if (result.get() == delete) {
				Main.createWindow("ProductDelete.fxml");
			}
		} else {
			if (result.get() == edit) {
				Main.createWindow("SupplierEdit.fxml");
			} else if (result.get() == delete) {
				submitDeleteSupplier();
			}
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GET PRODUCT//
	@FXML
	public Product getProduct(Supplier supplier) {
		String productInfo = productCombo.getValue();
		Product product = null;
		for (int j = 0; j < supplier.getSupProducts().size(); j++) {
			Product currentProduct = supplier.getSupProducts().get(j);
			if (currentProduct.getProductDetails().equals(productInfo)) {
				product = currentProduct;
				System.out.println(product.getProCode());
			}
		}
		return product;
	}

	@FXML
	public void actionChosen() {
		try {
			ArrayList<Supplier> items = getAllData();
			Supplier supplier = getSupplier();
			Product product = getProduct(supplier);
			currentValue.setText(updateAction(productAction.getValue(), product));
			if(productAction.getValue().equals("Discontinued")) {
				updateValue.setDisable(true);
				if(currentValue.getText().equals("True")) {
					currentValue.setText("True (will be set to false on submit)");
				} else {
					currentValue.setText("False (will be set to true on submit)");
				}
			}
		} catch (Exception e) {
			Main.createWindow("ProductEdit.fxml");
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRO ADD//
	@FXML
	public void submitProductAdd() {
		try {
			Supplier supplier = getSupplier();
			boolean codeTaken = false;
			for(Product i : supplier.getSupProducts()) {
				if(i.getProCode() == Integer.valueOf(newCode.getText())) {
					error("Product code already taken");
					codeTaken = true;
				}
			} 
			if(!codeTaken) {
				Product product = new Product(Integer.valueOf(newCode.getText()), newMake.getText(), newModel.getText(),
						Double.valueOf(newPrice.getText()), Integer.valueOf(newQty.getText()), false);
				String supCode = String.valueOf(supplier.getSupCode());
				String proCode = String.valueOf(product.getProCode());
				String proDetails = formattedProDetails(product);
				ReadWrite.proUpdateWriter(proDetails, supCode, proCode, false);
				Main.createWindow("UpdateProduct.fxml");
			}
		} catch (Exception e) {
			error("Missing/invalid inputs");
		}
	}

	@FXML
	public void buildProAdd() {
		title.setText(Main.activeSupplier);
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRO DELETE//
	@FXML
	public void submitProductDelete() {
		try {
			Supplier supplier = getSupplier();
			String supCode = String.valueOf(supplier.getSupCode());
			Product product = getProduct(supplier);
			String proCode = String.valueOf(product.getProCode());
			ReadWrite.deleteWriter(supCode, proCode, true);
			Main.createWindow("UpdateProduct.fxml");
		} catch (Exception e) {
			error("No product selected");
		}
	}

	@FXML
	public void buildProDelete() {
		title.setText(Main.activeSupplier);
		Supplier supplier = getSupplier();
		ArrayList<String> productOptions = new ArrayList<String>();
		for (int j = 0; j < supplier.getSupProducts().size(); j++) {
			productOptions.add(supplier.getSupProducts().get(j).getProductDetails());
		}
		productCombo.setItems(FXCollections.observableArrayList(productOptions));
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//PRO EDIT//
	@FXML
	public void submitProductUpdate() {
		try {
			Supplier supplier = getSupplier();
			String supCode = String.valueOf(supplier.getSupCode());
			Product product = getProduct(supplier);
			String proCode = String.valueOf(product.getProCode());
			switch (productAction.getValue()) {
			case "Code":
				product.setProCode(Integer.valueOf(updateValue.getText()));
				break;
			case "Make":
				product.setProMake(updateValue.getText());
				break;
			case "Model":
				product.setProModel(updateValue.getText());
				break;
			case "Price":
				product.setProPrice(Double.valueOf(updateValue.getText()));
				break;
			case "Discontinued":
				product.setProDiscontinued(!(product.getProDiscontinued()));
				break;
			}
			String proDetails = formattedProDetails(product);
			ReadWrite.proUpdateWriter(proDetails, supCode, proCode, true);
			Main.createWindow("UpdateProduct.fxml");
		} catch (Exception e) {
			error("Missing required input/ Invalid input");
		}
	}

	@FXML
	public void buildProUpdate() {
		title.setText(Main.activeSupplier);
		ArrayList<Supplier> items = getAllData();
		Supplier supplier = null;
		for (Supplier i : items) {
			if (i.getSupName().equals(title.getText())) {
				supplier = i;
			}
		}
		ArrayList<String> productOptions = new ArrayList<String>();
		for (Product j : supplier.getSupProducts()) {
			productOptions.add(j.getProductDetails());
		}
		productCombo.setItems(FXCollections.observableArrayList(productOptions));
		ArrayList<String> actionOptions = new ArrayList<String>(Arrays.asList("Code", "Make", "Model", "Price", "Discontinued"));
		productAction.setItems(FXCollections.observableArrayList(actionOptions));
	}

	public String updateAction(String action, Product product) {
		String result = "";
		switch (action) {
		case "Code":
			return String.valueOf(product.getProCode());
		case "Make":
			return product.getProMake();
		case "Model":
			return product.getProModel();
		case "Price":
			return String.valueOf(product.getProPrice());
		case "Discontinued":
			return String.valueOf(product.getProDiscontinued());
		}
		return result;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//SUP ADD//
	@FXML
	public void supplierAdd() {
		try {
			ArrayList<Supplier> items = getAllData();
			boolean codeTaken = false;
			for(Supplier i : items) {
				if(i.getSupCode() == Integer.valueOf(newCode.getText())) {
					error("Supplier code already taken");
					codeTaken = true;
				}
			}
			if(!codeTaken) {
				String[] street = newStreet.getText().split(" ");
				String region = "OUTSIDE_EU";
				if (supRegion.getValue().equals("United Kingdom")) {
					region = "UNITED_KINGDOM";
				} else if (supRegion.getValue().equals("Europe")) {
					region = "EUROPE";
				}
				String newSup = "$" + newCode.getText() +"&&";
				int x = Integer.valueOf(street[0]);
				newSup += newName.getText() + "," + street[0] + "," + street[1] + "," + newTown.getText()+ "," + newPcode.getText() + "," + newCountry.getText() + "," + region;
				Main.activeSupplier = newName.getText();
				ReadWrite.newSupplierWriter(newSup);
				Main.createWindow("Main.fxml");
			}
		} catch (Exception e) {
			error("Missing/Invalid inputs");
		}
	}
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SUP DELETE//
	@FXML
	public void submitDeleteSupplier() {
		Main.activeSupplier = String.valueOf(table.getSelectionModel().getSelectedItem().getSupCode());
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText(
				"Are you sure you want to delete Supplier " + table.getSelectionModel().getSelectedItem().getSupName());

		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.OK) {
			ReadWrite.deleteWriter(Main.activeSupplier, null, false);
			Main.createWindow("UpdateSupplier.fxml");
		}
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// SUP EDIT//
	@FXML
	public void buildSupUpdate() {
		title.setText(Main.activeSupplier);
		ArrayList<String> actionOptions = new ArrayList<String>(Arrays.asList("Code", "Name", "Address", "Town/City", "Postcode", "Country", "Region"));
		productAction.setItems(FXCollections.observableArrayList(actionOptions));
	}
	
	@FXML
	public void supAction() {
		ArrayList<Supplier> items = getAllData();
		Supplier supplier = getSupplier();
		String val = "";
		if(productAction.getValue() != null) {
			switch(productAction.getValue()) {
				case "Code": val = String.valueOf(supplier.getSupCode()); break;
				case "Name": val = supplier.getSupName(); break;
				case "Address": val = supplier.getSupAddress().getBldNum()+" "+supplier.getSupAddress().getBldStreet(); break;
				case "Town/City": val = supplier.getSupAddress().getBldTown(); break;
				case "Postcode": val = supplier.getSupAddress().getBldPcode(); break;
				case "Country": val = supplier.getSupAddress().getBldCountry(); break;
				case "Region": val = supplier.getSupRegion().getRegionAsString(); break;
				default: val = ""; break;
			}
			currentValue.setText(val);
		}
	}
	
	@FXML
	public void submitSupplierUpdate() {
		try {
			Supplier supplier = getSupplier();
			String supCode = String.valueOf(supplier.getSupCode());
			boolean codeChange = false;
			String newVal = updateValue.getText();
			int pos = 0;
			boolean valid = true;
			if(productAction.getValue() != null && newVal.length() > 0) {
				switch(productAction.getValue()) {
					case "Code": int x = Integer.valueOf(newVal); codeChange = true;  break;
					case "Name": break;
					case "Address": int y = Integer.valueOf(newVal.split(" ")[0]); ReadWrite.supUpdateWriter(supCode, newVal.split(" ")[0], codeChange, 1); newVal = newVal.substring(newVal.indexOf(" ")+1); pos = 2; break;
					case "Town/City": pos = 3; break;
					case "Postcode": pos = 4; break;
					case "Country": pos = 5; break;
					case "Region": newVal = regionMatch(newVal); pos = 6; break;
					default: error("Please select a property to change"); valid = false; break;
				}
				if(valid) {
					ReadWrite.supUpdateWriter(supCode, newVal, codeChange, pos);
					Main.createWindow("UpdateSupplier.fxml");
				}
			} else {
				error("Missing required input");
			}
		} catch (Exception e) {
			error("Invalid update value");
		}
	}
	
	
	private String regionMatch(String newVal) {
		String region = "";
		if(newVal.toLowerCase().contains("united kingdom")) {
			region = "UNITED_KINGDOM";
		} else if(newVal.toLowerCase().contains("europe") || newVal.toLowerCase().equals("eu")) {
			region = "EUROPE";
		} else {
			region = "OUTSIDE_EU";
		}
		return region;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// GET SUPPLIER//
	@FXML
	public Supplier getSupplier() {
		ArrayList<Supplier> items = getAllData();
		Supplier supplier = null;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i).getSupName().contains(Main.activeSupplier)) {
				supplier = items.get(i);
			}
		}
		return supplier;
	}

	@FXML
	public ArrayList<String> getSupplierChoices(Supplier supplier) {
		ArrayList<Product> products = new ArrayList<Product>();
		ArrayList<String> choices = new ArrayList<String>();
		products = supplier.getSupProducts();
		for (int j = 0; j < products.size(); j++) {
			choices.add(products.get(j).getProductDetails());
			System.out.println(products.get(j).getProductDetails());
		}
		return choices;
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// QTY UPDATE//
	@FXML
	public void createQtyUpdate() {
		Supplier supplier = getSupplier();
		ArrayList<String> choices = getSupplierChoices(supplier);
		ChoiceDialog<String> productDialog = new ChoiceDialog<>("Choose a product", choices);
		productDialog.setTitle("Update quantity");
		productDialog.setHeaderText("Please select the product you wish to change below");
		productDialog.setContentText("Choose your product:");
		Optional<String> productOpt = productDialog.showAndWait();

		if (productOpt.isPresent()) {
			productDialog.close();
			int selectedProduct = Integer.valueOf(String.valueOf(productOpt).substring(15, String.valueOf(productOpt).indexOf(",")));
			Product product = null;
			for (Product i: supplier.getSupProducts()) {
				if (i.getProCode() == selectedProduct) {
					product = i;
				}
			}

			TextInputDialog qtyDialog = new TextInputDialog(String.valueOf(product.getProQtyAvailable()));
			qtyDialog.setTitle("Enter new quantity");
			qtyDialog.setHeaderText("Please change the quantity of this product below");
			qtyDialog.setContentText("New Quantity for product " + selectedProduct);
			Optional<String> qty = qtyDialog.showAndWait();
			if (qty.isPresent()) {
				try {
					String getQuantity = qty.toString().substring(qty.toString().indexOf("[") + 1,qty.toString().indexOf("]"));
					int quantity = Integer.parseInt(getQuantity);
					product.setProQtyAvailable(Integer.valueOf(quantity));
					String proDetails = formattedProDetails(product);
					String supCode = String.valueOf(supplier.getSupCode());
					String proCode = String.valueOf(product.getProCode());
					ReadWrite.proUpdateWriter(proDetails, supCode, proCode, true);
				} catch (NumberFormatException e) {
					error("Invalid quantity");
				}
			}	
		} else {
			error("No product chosen");
		}
		Main.createWindow("UpdateStock.fxml");
	}

	/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	@FXML
	public void createProUpdate() {
		System.out.println("update product");
		Main.activeSupplier = table.getSelectionModel().getSelectedItem().getSupName();
		deleteOrEdit(true);
	}

	@FXML
	public void createSupUpdate() {
		Main.activeSupplier = table.getSelectionModel().getSelectedItem().getSupName();
		deleteOrEdit(false);
	}

	public void error(String text) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Error");
		alert.setHeaderText("Invalid value entered");
		alert.setContentText(text);
		alert.showAndWait();
	}

	public String formattedProDetails(Product product) {
		return String.valueOf(product.getProCode()) + "," + product.getProMake() + "," + product.getProModel() + ","
				+ String.valueOf(product.getProPrice()) + "," + String.valueOf(product.getProQtyAvailable()) + ","
				+ product.getProDiscontinued();
	}
}