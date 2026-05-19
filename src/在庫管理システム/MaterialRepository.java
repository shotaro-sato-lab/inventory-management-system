package 在庫管理システム;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class MaterialRepository {
	private HashMap<Integer, Material> materialByInternalMaterialId = new HashMap<>();
	private HashMap<String, Integer> internalMaterialIdByMaterialName = new HashMap<>();
	private int count = 0; //内部IDの作成に使用

	//内部IDの作成
	private int makeInternalMaterialId() {
		count++;
		return count;
	}

	//現在のIDの最大値を得る
	private void getCount(int maxId) {
		count = maxId;
	}

	//材料の追加
	public void addMaterial(String materialId, String materialName, int materialPrice, int materialStock) {
		int internalMaterialId = makeInternalMaterialId();
		materialByInternalMaterialId.put(internalMaterialId,
				new Material(internalMaterialId, materialId, materialName, materialPrice, materialStock));
		internalMaterialIdByMaterialName.put(materialName, internalMaterialId);
	}

	//材料の削除
	public boolean removeMaterial(String materialName) {
		if (!materialExist(materialName)) {
			return false;
		}

		int internalMaterialId = internalMaterialIdByMaterialName.get(materialName);
		materialByInternalMaterialId.remove(internalMaterialId);
		internalMaterialIdByMaterialName.remove(materialName);
		return true;
	}

	//すべての材料の表示
	public void showAllMaterial() {
		for (Material m : materialByInternalMaterialId.values()) {
			System.out.println(m);
		}
	}

	//材料の在庫数の追加
	public boolean addMaterialStock(String materialName, int amount) {
		if (materialExist(materialName)) {
			int internalMaterialId = internalMaterialIdByMaterialName.get(materialName);
			Material m = materialByInternalMaterialId.get(internalMaterialId);

			if (!m.addMaterialStock(amount)) {
				System.out.println("追加数は正の値を入力してください");
				return false;
			}

			System.out.println("在庫を追加しました");
			return true;
		}

		System.out.println("該当する材料が存在しません");
		return false;
	}

	//材料の在庫数の削減
	public boolean removeMaterialStock(String materialName, int amount) {
		if (materialExist(materialName)) {
			int internalMaterialId = internalMaterialIdByMaterialName.get(materialName);
			Material m = materialByInternalMaterialId.get(internalMaterialId);

			if (!m.removeMaterialStock(amount)) {
				System.out.println("在庫不足または0以下の値は指定できません");
				return false;
			}

			System.out.println("在庫を減らしました");
			return true;
		}

		return false;
	}

	//入力された名前に一致する材料が存在するかの確認
	public boolean materialExist(String materialName) {
		return internalMaterialIdByMaterialName.containsKey(materialName);
	}

	//名前にキーワードを含む材料があるかの確認
	public boolean materialNameKeywordExist(String keyword) {
		for (Material m : materialByInternalMaterialId.values()) {
			if (m.getMaterialName().toLowerCase().contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	//IDにキーワードを含む材料があるかの確認
	public boolean materialIdKeywordExist(String keyword) {
		for (Material m : materialByInternalMaterialId.values()) {
			if (m.getMaterialId().toLowerCase().contains(keyword.toLowerCase())) {
				return true;
			}
		}
		return false;
	}

	//名前にキーワードを含む材料の表示
	public void findMaterialByNameKeyword(String keyword) {
		for (Material m : materialByInternalMaterialId.values()) {
			if (m.getMaterialName().toLowerCase().contains(keyword)) {
				System.out.println(m);
			}
		}
	}

	//IDにキーワードを含む材料の表示
	public void findMaterialByIdKeyword(String keyword) {
		for (Material m : materialByInternalMaterialId.values()) {
			if (m.getMaterialId().toLowerCase().contains(keyword)) {
				System.out.println(m);
			}
		}
	}

	//在庫切れの材料の表示
	public void showNoStockMaterial() {
		for (Material m : materialByInternalMaterialId.values()) {
			if (m.getMaterialStock() == 0) {
				System.out.println(m);
			}
		}
	}

	//材料名の変更
	public boolean changeMaterialName(String oldName, String newName) {
		if (!materialExist(oldName)) {
			System.out.println("入力された名前の材料は存在しません");
			return false;
		}

		if (materialExist(newName)) {
			System.out.println("同じ名前の材料が既に存在します");
			return false;
		}

		int internalMaterialId = internalMaterialIdByMaterialName.get(oldName);
		Material m = materialByInternalMaterialId.get(internalMaterialId);
		internalMaterialIdByMaterialName.remove(oldName);
		internalMaterialIdByMaterialName.put(newName, internalMaterialId);
		System.out.println("材料名を変更しました");
		m.changeMaterialName(newName);

		return true;
	}

	//ファイルにデータを書き込む
	public void saveToFile() {
		try (PrintWriter pw = new PrintWriter("materials.txt")) {
			for (Material m : materialByInternalMaterialId.values()) {
				pw.println(m.getInternalMaterialId() + "," + m.getMaterialId() + "," + m.getMaterialName() + ","
						+ m.getMaterialPrice() + "," + m.getMaterialStock());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ファイルからデータを読み込む
	public void loadFromFile() {
		try (BufferedReader bf = new BufferedReader(new FileReader("materials.txt"))) {
			String line;
			int maxId = 0;

			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length != 5)
					continue;

				int internalMaterialId = Integer.parseInt(parts[0].trim());
				String materialId = parts[1].trim();
				String materialName = parts[2].trim();
				int materialPrice = Integer.parseInt(parts[3].trim());
				int materialStock = Integer.parseInt(parts[4].trim());

				if (internalMaterialId > maxId) {
					maxId = internalMaterialId;
				}

				materialByInternalMaterialId.put(internalMaterialId,
						new Material(internalMaterialId, materialId, materialName, materialPrice, materialStock));
				internalMaterialIdByMaterialName.put(materialName, internalMaterialId);
			}

			getCount(maxId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
