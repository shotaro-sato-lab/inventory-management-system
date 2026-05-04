package 在庫管理システム;

public class Product {
	private final int id;
	private String name;
	private int price;
	private int stock;

	public Product(int id, String name, int price, int stock) {
		if (price < 0 || stock < 0) {
			throw new IllegalArgumentException("不正な入力値");
		}
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getStock() {
		return stock;
	}

	public void changeName(String newName) {
		name = newName;
	}

	@Override
	public String toString() {
		return "商品名: " + name + "価格: " + price + "在庫数: " + stock;
	}

	public boolean isOutOfStock() {
		return stock == 0;
	}

	public boolean addStock(int amount) {
		if (amount <= 0)
			return false;
		stock += amount;
		return true;
	}

	public boolean removeStock(int amount) {
		if (amount <= 0 || stock < amount)
			return false;
		stock -= amount;
		return true;
	}
}
