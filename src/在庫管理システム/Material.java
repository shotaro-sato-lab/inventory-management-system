package 在庫管理システム;

public class Material {
	private final int internalMaterialId; //材料の内部ID
	private String materialId; //材料の外部ID
	private String materialName; //材料の名前
	private int materialPrice; //材料の価格
	private int materialStock; //材料の在庫数

	public Material(int internalMaterialId, String materialId, String materialName, int materialPrice,
			int materialStock) {
		if (materialPrice < 0 || materialStock < 0) {
			throw new IllegalArgumentException("不正な入力値");
		}

		this.internalMaterialId = internalMaterialId;
		this.materialId = materialId;
		this.materialName = materialName;
		this.materialPrice = materialPrice;
		this.materialStock = materialStock;
	}

	public int getInternalMaterialId() {
		return internalMaterialId;
	}

	public String getMaterialId() {
		return materialId;
	}

	public String getMaterialName() {
		return materialName;
	}

	public int getMaterialPrice() {
		return materialPrice;
	}

	public int getMaterialStock() {
		return materialStock;
	}

	public boolean addMaterialStock(int amount) {
		if (amount > 0) {
			materialStock += amount;
			return true;
		}

		return false;
	}

	public boolean removeMaterialStock(int amount) {
		if (amount < 0 || materialStock < amount)
			return false;

		materialStock -= amount;
		return true;
	}

	@Override
	public String toString() {
		return "ID:" + getMaterialId() + "　材料名:" + getMaterialName() + "　価格: " + getMaterialPrice() + "　在庫数:"
				+ getMaterialStock();
	}

	public void changeMaterialName(String newName) {
		materialName = newName;
	}

}
