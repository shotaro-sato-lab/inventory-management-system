package 在庫管理システム;

import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class AnalysisCLI {

	private Scanner sc = new Scanner(System.in);
	private ProductRepository productRepo;
	private MaterialRepository materialRepo;
	private RecipeRepository recipeRepo;

	public AnalysisCLI(ProductRepository productRepo, MaterialRepository materialRepo, RecipeRepository recipeRepo) {
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

	public void analysisStart() {

		while (true) {
			System.out.println("1:材料から商品を検索する　2:必要な材料が切れてしまっている商品一覧　0:戻る");

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

				showProductByInternalMaterial();
				break;

			case 2:

				showProductMadeByNoStockMaterial();
				break;

			case 0:

				System.out.println("***戻る***");
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	public void showProductByInternalMaterial() {

		System.out.println("材料から商品を検索します");
		int flag = -1;
		int internalMaterialId;

		while (true) {

			if (confirm("材料名から検索する場合はyを、IDから検索する場合はnを入力してください")) {
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

				internalMaterialId = materialRepo.getInternalMaterialIdByMaterialName(materialName);

			} else {
				String materialId = inputString("ID");
				if (!materialRepo.materialExistById(materialId)) {
					System.out.println("入力されたIDの材料は存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalMaterialId = materialRepo.getInternalMaterialIdByMaterialId(materialId);

			}

			break;
		}

		for (int internalProductId : recipeRepo.findInternalProductIdByInternalMaterialId(internalMaterialId)) {
			productRepo.showProductByInternalProductId(internalProductId);
		}

	}

	public void showProductMadeByNoStockMaterial() {

		List<Integer> noStockInternalMaterialIds = materialRepo.getInternalNoStockMaterialId();

		if (noStockInternalMaterialIds.isEmpty()) {
			System.out.println("在庫切れの材料は存在しません");
			return;
		}

		Set<Integer> affectedInternalProductIds = new HashSet<>();

		for (int internalMaterialId : noStockInternalMaterialIds) {
			List<Integer> internalProductIds = recipeRepo.findInternalProductIdByInternalMaterialId(internalMaterialId);
			affectedInternalProductIds.addAll(internalProductIds);
		}

		if (affectedInternalProductIds.isEmpty()) {
			System.out.println("材料が切れている商品は存在しません");
			return;
		}

		System.out.println("材料の在庫が切れている商品が見つかりました");
		for (int internalProductId : affectedInternalProductIds) {
			productRepo.showProductByInternalProductId(internalProductId);
		}
		System.out.println("選択画面に戻ります");

	}

}
