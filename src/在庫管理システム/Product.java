package 在庫管理システム;

public class Product {
	private final int internalId; //内部ID
	private String id; //外部ID
	private String name; //商品名
	private int price; //価格
	private int stock; //在庫数

	public Product(int internalId, String id, String name, int price, int stock) {
		if (price < 0 || stock < 0) {
			throw new IllegalArgumentException("不正な入力値");
		}
		this.internalId = internalId;
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public int getInternalId() {
		return internalId;
	}

	public String getId() {
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
		return "ID: " + id + "　商品名: " + name + "　価格: " + price + "　在庫数: " + stock;
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