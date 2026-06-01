package 在庫管理システム;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ

		Scanner sc = new Scanner(System.in);
		ProductRepository productRepo = new ProductRepository();
		MaterialRepository materialRepo = new MaterialRepository();
		RecipeRepository recipeRepo = new RecipeRepository();

		InventoryCLI cli = new InventoryCLI(productRepo, recipeRepo);
		MaterialInventoryCLI mateCli = new MaterialInventoryCLI(materialRepo, recipeRepo);
		RecipeCLI reciCli = new RecipeCLI(productRepo, materialRepo, recipeRepo);
		AnalysisCLI anaCli = new AnalysisCLI(productRepo, materialRepo, recipeRepo);

		productRepo.loadFromFile();
		materialRepo.loadFromFile();
		recipeRepo.loadFromFile();

		while (true) {
			System.out.println("1:商品の操作　2:材料の操作　3:レシピの操作　4:便利な機能　0:終了");

			String choiceStr = sc.nextLine();
			int choice;
			try {
				choice = Integer.parseInt(choiceStr);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
				continue;
			}

			switch (choice) {
			case 1:

				cli.productStart();
				break;

			case 2:

				mateCli.Start();
				break;

			case 3:

				reciCli.recipeStart();
				break;

			case 4:

				anaCli.analysisStart();
				break;

			case 0:

				System.out.println("***終了***");
				productRepo.saveToFile();
				materialRepo.saveToFile();
				recipeRepo.saveToFile();
				sc.close();
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}
		}

	}
}