package 在庫管理システム;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecipeRepository {

	private HashMap<Integer, Recipe> recipeByInternalProductId = new HashMap<>();

	//レシピの追加
	public boolean addRecipe(int internalProductId, int internalMaterialId, int amount) {

		Recipe re = recipeByInternalProductId.get(internalProductId);

		if (re == null) {
			re = new Recipe(internalProductId);
			recipeByInternalProductId.put(internalProductId, re);
		}

		if (re.containMaterial(internalMaterialId)) {
			return false; //既に同じ材料がレシピに登録されていた場合
		}

		re.addRecipeList(internalMaterialId, amount);
		return true;

	}

	//商品の内部ID経由のレシピの削除（商品削除に連動して行われる）
	public void removeRecipe(int internalProductId) {
		recipeByInternalProductId.remove(internalProductId);
	}

	//商品の内部IDからレシピが存在するかを確認し、存在する場合はレシピを返す
	public Recipe findRecipeByInternalProductId(int internalProductId) {
		return recipeByInternalProductId.get(internalProductId);
	}

	//商品の内部IDからレシピが存在するかの確認を行う
	public boolean confirmRecipeByInternalProductId(int internalProductId) {
		if (recipeByInternalProductId.get(internalProductId) == null) {
			return false;
		}

		return true;
	}

	//材料の内部IDから商品の内部IDを得る
	public List<Integer> findInternalProductIdByInternalMaterialId(int internalMaterialId) {
		List<Integer> internalProductIdByInternalMaterialId = new ArrayList<>();
		for (Recipe re : recipeByInternalProductId.values()) {
			for (MaterialManagement mama : re.getRecipes()) {
				if (internalMaterialId == mama.getInternalMaterialId()) {
					internalProductIdByInternalMaterialId.add(re.getRecipeId());
				}
			}
		}

		return internalProductIdByInternalMaterialId;
	}

	//ファイルにデータを書き込む
	public void saveToFile() {
		try (PrintWriter pw = new PrintWriter("recipes.txt")) {
			for (Recipe recipe : recipeByInternalProductId.values()) {
				for (MaterialManagement management : recipe.getRecipes()) {
					pw.println(recipe.getRecipeId() + "," + management.getMaterialManagementId() + ","
							+ management.getInternalMaterialId() + ","
							+ management.getMaterialAmount());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ファイルからデータを読み込む
	public void loadFromFile() {
		try (BufferedReader bf = new BufferedReader(new FileReader("recipes.txt"))) {
			String line;

			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length % 4 != 0)
					continue;

				int recipeId = Integer.parseInt(parts[0].trim()); //レシピIDは商品の内部IDと同じ
				Recipe recipe = recipeByInternalProductId.get(recipeId);
				if (recipe == null) {
					recipe = new Recipe(recipeId);
				}
				recipeByInternalProductId.put(recipeId, recipe);

				int materialManagementId = Integer.parseInt(parts[1].trim());
				int internalMaterialId = Integer.parseInt(parts[2].trim());
				int materialAmount = Integer.parseInt(parts[3].trim());

				if (materialManagementId > recipe.getCount()) {
					recipe.changeCount(materialManagementId);
				}

				recipe.reAddRecipeList(materialManagementId, internalMaterialId, materialAmount);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
