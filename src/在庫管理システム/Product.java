package 在庫管理システム;

public class Product {
	private final int internalProductId; //商品の内部ID
	private String productId; //商品の外部ID
	private String productName; //商品名
	private int productPrice; //価格
	private int productStock; //在庫数

	public Product(int internalId, String id, String productName, int price, int stock) {
		if (price < 0 || stock < 0) {
			throw new IllegalArgumentException("不正な入力値");
		}
		this.internalProductId = internalId;
		this.productId = id;
		this.productName = productName;
		this.productPrice = price;
		this.productStock = stock;
	}

	public int getInternalProductId() {
		return internalProductId;
	}

	public String getProductId() {
		return productId;
	}

	public String getProductName() {
		return productName;
	}

	public int getProductPrice() {
		return productPrice;
	}

	public int getProductStock() {
		return productStock;
	}

	public void changeProductName(String newName) {
		productName = newName;
	}

	@Override
	public String toString() {
		return "ID: " + productId + "　商品名: " + productName + "　価格: " + productPrice + "　在庫数: " + productStock;
	}

	public boolean productIsOutOfStock() {
		return productStock == 0;
	}

	public boolean addProductStock(int amount) {
		if (amount <= 0)
			return false;
		productStock += amount;
		return true;
	}

	public boolean removeProductStock(int amount) {
		if (amount <= 0 || productStock < amount)
			return false;
		productStock -= amount;
		return true;
	}
}