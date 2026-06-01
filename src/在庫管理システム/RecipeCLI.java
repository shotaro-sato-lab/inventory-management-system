package 在庫管理システム;

import java.util.Scanner;

public class RecipeCLI {

	private Scanner sc = new Scanner(System.in);
	private ProductRepository productRepo;
	private MaterialRepository materialRepo;
	private RecipeRepository recipeRepo;

	public RecipeCLI(ProductRepository productRepo, MaterialRepository materialRepo, RecipeRepository recipeRepo) {
		this.productRepo = productRepo;
		this.materialRepo = materialRepo;
		this.recipeRepo = recipeRepo;
	}

	private int inputInt(String message) {
		while (true) {
			System.out.println(message);
			String input = sc.nextLine();

			if (input.trim().isEmpty()) {
				System.out.println("値を入力してください");
				continue;
			}

			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + ":");
		while (true) {
			String input = sc.nextLine();
			if (input.trim().isEmpty()) {
				System.out.println(message + "は空欄にはできません");
				System.out.println(message + ":");
				continue;
			} else {
				return input;
			}
		}

	}

	private boolean confirm(String message) {
		while (true) {
			System.out.println(message + "(y/n)");
			String input = sc.nextLine();

			if (input.equalsIgnoreCase("y"))
				return true;
			if (input.equalsIgnoreCase("n"))
				return false;

			System.out.println("yまたはnを入力してください");
		}
	}

	public void recipeStart() {

		while (true) {
			System.out.println("1:レシピ追加　2:レシピ検索　0:戻る");

			String choiceStr = sc.nextLine();
			int choice;
			try {
				choice = Integer.parseInt(choiceStr);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
				continue;
			}

			switch (choice) {

			case 1: //レシピ追加

				addRecipe();
				break;

			case 2: //レシピ検索

				searchRecipe();
				break;

			case 0:

				System.out.println("***戻る***");
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private void addRecipe() {
		System.out.println("===レシピ追加===");

		while (true) {

			String productName = inputString("商品名");
			if (!productRepo.productExistByName(productName)) {
				System.out.println("入力された名前の商品は存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			while (true) {
				String materialName = inputString("材料名");
				if (!materialRepo.materialExistByName(materialName)) {
					System.out.println("入力された名前の材料は存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				int amount = inputInt("使用する個数");
				if (amount <= 0) {
					System.out.println("使用する個数は正の値を入力してください");
					continue;
				}

				System.out.println("材料名:" + materialName + "　使用する個数:" + amount);
				if (!confirm("これでよろしいですか？")) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				int internalProductId = productRepo.getInternalProductIdByProductName(productName);
				int internalMaterialId = materialRepo.getInternalMaterialIdByMaterialName(materialName);
				if (!recipeRepo.addRecipe(internalProductId, internalMaterialId, amount)) {
					//既に同じ材料がレシピに登録されていた場合
					System.out.println("既に同じ材料がレシピに登録されています");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				//まだレシピには登録されていなかったとき
				System.out.println("レシピの材料に追加しました");

				if (confirm("レシピへの材料の追加を続けますか？")) {
					System.out.println("追加作業を続けます");
					continue;
				}

				return;

			}

		}

	}

	private void searchRecipe() {
		System.out.println("レシピ検索");
		while (true) {

			if (confirm("商品名から検索する場合はyを、商品IDから検索する場合はnを入力してください")) {

				//商品名からレシピを検索して表示
				String productName = inputString("商品名");
				if (productRepo.productExistByName(productName)) {
					showRecipeByProductName(productName);
					break;
				} else {
					System.out.println("入力された名前の商品は存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						break;
					}
				}
			} else {

				//商品IDからレシピを検索して表示
				String productId = inputString("商品ID");
				if (productRepo.productExistByProductId(productId)) {
					showRecipeByProductId(productId);
					break;
				} else {
					System.out.println("入力された商品IDを持つ商品は存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						break;
					}
				}

			}
		}
	}

	private void showRecipeByProductName(String productName) {
		int internalProductId = productRepo.getInternalProductIdByProductName(productName);
		Recipe re = recipeRepo.findRecipeByInternalProductId(internalProductId);

		if (re == null) {
			System.out.println("入力された商品はまだレシピが登録されていません");
			return;
		}

		System.out.println("該当する商品のレシピが見つかりました");
		for (MaterialManagement mama : re.getRecipes()) {
			int internalMaterialId = mama.getInternalMaterialId();
			Material material = materialRepo.getMaterialByInternalMaterialId(internalMaterialId);
			System.out.println("材料ID:" + material.getMaterialId() + "　材料名:" + material.getMaterialName() + "　個数:"
					+ mama.getMaterialAmount());
		}
	}

	private void showRecipeByProductId(String productId) {
		int internalProductId = productRepo.getInternalProductIdByProductId(productId);
		Recipe re = recipeRepo.findRecipeByInternalProductId(internalProductId);

		if (re == null) {
			System.out.println("入力された商品はまだレシピが登録されていません");
			return;
		}

		System.out.println("該当する商品のレシピが見つかりました");
		for (MaterialManagement mama : re.getRecipes()) {
			int internalMaterialId = mama.getInternalMaterialId();
			Material material = materialRepo.getMaterialByInternalMaterialId(internalMaterialId);
			System.out.println("材料ID:" + material.getMaterialId() + "　材料名:" + material.getMaterialName() + "　個数:"
					+ mama.getMaterialAmount());
		}
	}

}
