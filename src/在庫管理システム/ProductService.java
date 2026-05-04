package 在庫管理システム;

import java.util.Map;

public class ProductService {

	static void addStock(Map<Integer, Product> products, int id, int amount) {
		if (amount <= 0) {
			return;
		} else {
			if (products.get(id) != null) {
				products.get(id).addStock(amount);
			}
		}
	}

	static void removeStock(Map<Integer, Product> products, int id, int amount) {
		if (amount <= 0) {
			return;
		}

		if (products.get(id) != null) {
			products.get(id).removeStock(amount);
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

	static void showPersonalStockStatus(Map<Integer, Product> products, int id) {
		if (products.get(id) != null) {
			if (products.get(id).isOutOfStock()) {
				System.out.println(products.get(id).getName() + "：在庫がありません");
			} else {
				System.out.println(products.get(id).getName() + "の在庫数：" + products.get(id).getStock());
			}
		}
	}

}
