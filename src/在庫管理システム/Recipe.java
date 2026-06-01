package 在庫管理システム;

import java.util.ArrayList;
import java.util.List;

public class Recipe {

	private final int recipeId; //レシピのID　商品の内部IDと同じである
	private List<MaterialManagement> recipes = new ArrayList<>();
	private int count = 0; //材料セットIDの作成に使用

	public Recipe(int recipeId) {

		this.recipeId = recipeId;
		this.recipes = new ArrayList<>();
	}

	//配列に材料と個数のセットを追加
	public void addRecipeList(int internalMaterialId, int amount) {
		count++;
		recipes.add(new MaterialManagement(count, internalMaterialId, amount));
	}

	//ファイルから読み込んだ時に配列に再配置する
	public void reAddRecipeList(int managementId, int internalMaterialId, int amount) {
		recipes.add(new MaterialManagement(managementId, internalMaterialId, amount));
	}

	public List<MaterialManagement> getRecipes() {
		return recipes;
	}

	public int getRecipeId() {
		return recipeId;
	}

	public int getCount() {
		return count;
	}

	//既に同じ材料がレシピに登録されていないかを判断する
	public boolean containMaterial(int internalMaterialId) {
		for (MaterialManagement mama : recipes) {
			if (mama.getInternalMaterialId() == internalMaterialId) {
				return true;
			}
		}
		return false;
	}

	//ファイルを読み込んだ時に、countの値を元に戻す
	public void changeCount(int newCount) {
		count = newCount;
	}

}
