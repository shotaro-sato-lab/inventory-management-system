package 在庫管理システム;

import java.util.Map;

public class ProductService {

	static void addProductStock(Map<Integer, Product> products, int internalId, int amount) {
		if (amount <= 0) {
			return;
		} else {
			if (products.get(internalId) != null) {
				products.get(internalId).addProductStock(amount);
			}
		}
	}

	static void removeProductStock(Map<Integer, Product> products, int internalId, int amount) {
		if (amount <= 0) {
			return;
		}

		if (products.get(internalId) != null) {
			products.get(internalId).removeProductStock(amount);
		}
	}

	static void showAllProductStockStatus(Map<Integer, Product> products) {
		for (Product p : products.values()) {
			if (p.productIsOutOfStock()) {
				System.out.println(p.getProductName() + "は在庫がありません");
			} else {
				System.out.println(p);
			}
		}
	}

	static void showPersonalProductStockStatus(Map<Integer, Product> products, int internalId) {
		if (products.get(internalId) != null) {
			if (products.get(internalId).productIsOutOfStock()) {
				System.out.println(products.get(internalId).getProductName() + "：在庫がありません");
			} else {
				System.out.println(products.get(internalId).getProductName() + "の在庫数：" + products.get(internalId).getProductStock());
			}
		}
	}

}