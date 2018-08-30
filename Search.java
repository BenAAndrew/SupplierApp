package application;

import java.util.ArrayList;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class Search {
	/*
	 * Supplier Search compares each supplier to the entered search depending on the filter 
	 * @param String filter, @param String searchItem, @param ArrayList<Supplier> items
	 * @return ArrayList<Supplier>
	 */
	public static ArrayList<Supplier> supplier(String filter, String searchItem, ArrayList<Supplier> items) {
		ArrayList<Supplier> refinedItems = new ArrayList<Supplier>();
		try {
			if (filter.equals("Name")) {
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i).getSupName().toLowerCase().contains(searchItem.toLowerCase())) {
						refinedItems.add(items.get(i));
					}
				}
			} else if (filter.equals("Region")) {
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i).getSupRegionString().toLowerCase().contains(searchItem.toLowerCase())) {
						refinedItems.add(items.get(i));
					}
				}
			} else if (filter.equals("Country")) {
				for (int i = 0; i < items.size(); i++) {
					if (items.get(i).getSupAddress().getBldCountry().toLowerCase().contains(searchItem.toLowerCase())) {
						refinedItems.add(items.get(i));
					}
				}
			}
		} catch (Exception e) {}
		return refinedItems;
	}

	/*
	 * Products search by type simply refreshes the window depending on whcih filter type they select
	 * @param String filter
	 * @return String
	 */
	public static String productsType(String filter) {
		if (filter.equals("Price")) {
			return "SearchProductPrice.fxml";
		} else if (filter.equals("Quantity")) {
			return "SearchProductQuantity.fxml";
		} else if (filter.equals("Discontinued") || filter.equals("Not Discontinued")) {
			return "SearchProductDisc.fxml";
		} else {
			return "SearchProduct.fxml";
		}
	}
}