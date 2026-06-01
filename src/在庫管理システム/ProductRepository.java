package 在庫管理システム;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
	private Map<Integer, Product> productByInternalProductId = new HashMap<>(); //内部IDから商品を得るハッシュマップ
	private Map<String, Integer> internalProductIdByName = new HashMap<>(); //名前から内部Idを得るハッシュマップ
	private int count = 0; //内部IDの作成に使用する

	//内部IDの作成
	private int makeInternalProductID() {
		count++;
		return count;
	}

	//現在の内部IDの最大値を得る
	private void getCount(int maxProductId) {
		count = maxProductId;
	}

	//商品名から商品の内部IDを取得する
	public int getInternalProductIdByProductName(String name) {
		return internalProductIdByName.get(name);
	}

	//商品IDから商品の内部IDを取得する
	public int getInternalProductIdByProductId(String productId) {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductId().equals(productId)) {
				return p.getInternalProductId();
			}
		}
		return -1;
	}

	//商品の追加
	public void addProduct(String productId, String name, int price, int addStock) {
		int internalProductId = makeInternalProductID();
		productByInternalProductId.put(internalProductId,
				new Product(internalProductId, productId, name, price, addStock));
		internalProductIdByName.put(name, internalProductId);
	}

	//商品の削減
	public void removeProduct(String name) {
		int internalProductId = internalProductIdByName.get(name);
		internalProductIdByName.remove(name);
		productByInternalProductId.remove(internalProductId);
	}

	//入力された商品の内部IDに対応する商品の情報を表示する。使用材料からの商品検索で使用している。
	public void showProductByInternalProductId(int internalProductId) {
		Product p = productByInternalProductId.get(internalProductId);
		System.out.println(p);
	}

	//入力された名前に一致する商品を取り出す。在庫の追加・削減で使用している。
	public Product findProduct(String name) {
		Integer productId = internalProductIdByName.get(name);
		if (productId != null) {
			return productByInternalProductId.get(productId);
		}
		return null;
	}

	//名前にキーワードを含む商品のデータを表示する
	public void findProductByNameKeyword(String keyword) {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
				System.out.println(p);
			}
		}
	}

	//IDにキーワードを含む商品のデータを表示する
	public void findProductByIdKeyword(String keyword) {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductId().toLowerCase().contains(keyword.toLowerCase())) {
				System.out.println(p);
			}
		}
	}

	//すべての商品データを表示する
	public void showProduct() {
		for (Product p : productByInternalProductId.values()) {
			System.out.println(p);
		}
	}

	//在庫がない商品のデータを表示する
	public void showNoStockProduct() {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductStock() == 0) {
				System.out.println(p);
			}
		}
	}

	//商品が存在するかの確認(商品名から検索)
	public boolean productExistByName(String name) {
		return internalProductIdByName.containsKey(name);
	}

	//商品が存在するかの確認(商品IDから検索)
	public boolean productExistByProductId(String productId) {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductId().equals(productId)) {
				return true;
			}
		}
		return false;
	}

	//商品名にキーワードを含むものが存在するかの確認
	public boolean productNameKeywordExist(String keyword) {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductName().toLowerCase().contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	//IDにキーワードを含む商品が存在するかの確認
	public boolean productIdKeywordExist(String keyword) {
		for (Product p : productByInternalProductId.values()) {
			if (p.getProductId().toLowerCase().contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	//商品名の変更
	public boolean changeProductName(String oldName, String newName) {
		Integer internalProductId = internalProductIdByName.get(oldName);

		if (internalProductId == null) {
			System.out.println("商品が存在しません");
			return false;
		}

		Product p = productByInternalProductId.get(internalProductId);

		if (internalProductIdByName.containsKey(newName)) {
			System.out.println("同じ名前の商品が既に存在します");
			return false;
		}

		p.changeProductName(newName);
		internalProductIdByName.remove(oldName);
		internalProductIdByName.put(newName, internalProductId);
		return true;
	}

	//ファイルにデータを書き込む
	public void saveToFile() {
		try (PrintWriter pw = new PrintWriter("products.txt")) {
			for (Product p : productByInternalProductId.values()) {
				pw.println(p.getInternalProductId() + "," + p.getProductId() + "," + p.getProductName() + ","
						+ p.getProductPrice() + ","
						+ p.getProductStock());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ファイルからデータを読み込む
	public void loadFromFile() {
		try (BufferedReader bf = new BufferedReader(new FileReader("products.txt"))) {
			String line;
			int maxId = 0;
			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length != 5)
					continue;

				int internalProductId = Integer.parseInt(parts[0].trim());
				String productID = parts[1].trim();
				String name = parts[2].trim();
				int price = Integer.parseInt(parts[3].trim());
				int stock = Integer.parseInt(parts[4].trim());

				if (internalProductId > maxId)
					maxId = internalProductId;
				internalProductIdByName.put(name, internalProductId);
				productByInternalProductId.put(internalProductId,
						new Product(internalProductId, productID, name, price, stock));

			}

			getCount(maxId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}