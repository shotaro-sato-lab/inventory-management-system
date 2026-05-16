package 在庫管理システム;

import java.util.Map;

public class ProductService {

	static void addStock(Map<Integer, Product> products, int internalId, int amount) {
		if (amount <= 0) {
			return;
		} else {
			if (products.get(internalId) != null) {
				products.get(internalId).addStock(amount);
			}
		}
	}

	static void removeStock(Map<Integer, Product> products, int internalId, int amount) {
		if (amount <= 0) {
			return;
		}

		if (products.get(internalId) != null) {
			products.get(internalId).removeStock(amount);
		}
	}

	static void showAllStockStatus(Map<Integer, Product> products) {
		for (Product p : products.values()) {
			if (p.isOutOfStock()) {
				System.out.println(p.getName() + "は在庫がありません");
			} else {
				System.out.println(p);
			}
		}
	}

	static void showPersonalStockStatus(Map<Integer, Product> products, int internalId) {
		if (products.get(internalId) != null) {
			if (products.get(internalId).isOutOfStock()) {
				System.out.println(products.get(internalId).getName() + "：在庫がありません");
			} else {
				System.out.println(products.get(internalId).getName() + "の在庫数：" + products.get(internalId).getStock());
			}
		}
	}

}