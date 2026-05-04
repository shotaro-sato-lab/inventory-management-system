package 在庫管理システム;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class ProductRepository {
	private Map<Integer, Product> productById = new HashMap<>();
	private Map<String, Integer> idByName = new HashMap<>();
	private int count = 0;

	private int makeId() {
		count++;
		return count;
	}

	private void getCount(int maxId) {
		count = maxId;
	}

	public void addProduct(String name, int price, int addStock) {
		int id = makeId();
		productById.put(id, new Product(id, name, price, addStock));
		idByName.put(name, id);
	}

	public void removeProduct(String name) {
		int id = idByName.get(name);
		idByName.remove(name);
		productById.remove(id);
	}

	public Product findProduct(String name) {
		Integer id = idByName.get(name);
		if (id == null) {
			return null;
		}
		Product p = productById.get(id);
		return p;
	}

	public void showProduct() {
		for (Product p : productById.values()) {
			System.out.println(p);
		}
	}

	public void showNoStockProduct() {
		for (Product p : productById.values()) {
			if (p.getStock() == 0) {
				System.out.println(p);
			}
		}
	}

	public boolean productExist(String name) {
		return idByName.containsKey(name);
	}

	public boolean changeName(String oldName, String newName) {
		Integer id = idByName.get(oldName);

		if (id == null) {
			System.out.println("商品が存在しません");
			return false;
		}

		Product p = productById.get(id);

		if (idByName.containsKey(newName)) {
			System.out.println("同じ名前の商品が既に存在します");
			return false;
		}

		p.changeName(newName);
		idByName.remove(oldName);
		idByName.put(newName, id);
		return true;
	}

	public void saveToFile() {
		try (PrintWriter pw = new PrintWriter("products.txt")) {
			for (Product p : productById.values()) {
				pw.println(p.getId() + "," + p.getName() + "," + p.getPrice() + "," + p.getStock());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loadFromFile() {
		try (BufferedReader bf = new BufferedReader(new FileReader("products.txt"))) {
			String line;
			int maxId = 0;
			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length != 4)
					continue;

				int id = Integer.parseInt(parts[0].trim());
				String name = parts[1].trim();
				int price = Integer.parseInt(parts[2].trim());
				int stock = Integer.parseInt(parts[3].trim());

				if (id > maxId)
					maxId = id;
				idByName.put(name, id);
				productById.put(id, new Product(id, name, price, stock));

			}

			getCount(maxId);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
