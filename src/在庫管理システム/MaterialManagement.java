package 在庫管理システム;

public class MaterialManagement {
	private final int materialManagementId; //材料と個数の紐づけ番号
	private int internalMaterialId; //材料の内部ID
	private int materialAmount; //材料の個数

	public MaterialManagement(int materialManagementId, int internalMaterialId, int materialAmount) {
		if (materialAmount <= 0) {
			throw new IllegalArgumentException("不正な入力値");
		}

		this.materialManagementId = materialManagementId;
		this.internalMaterialId = internalMaterialId;
		this.materialAmount = materialAmount;
	}

	public int getMaterialManagementId() {
		return materialManagementId;
	}

	public int getInternalMaterialId() {
		return internalMaterialId;
	}

	public int getMaterialAmount() {
		return materialAmount;
	}

	public boolean changeMaterialCount(int newMaterialAmount) {
		if (newMaterialAmount <= 0) {
			return false;
		}

		materialAmount = newMaterialAmount;
		return true;
	}

}
