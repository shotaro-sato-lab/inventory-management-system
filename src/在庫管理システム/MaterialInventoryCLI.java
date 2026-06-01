package 在庫管理システム;

import java.util.Scanner;

public class MaterialInventoryCLI {

	private Scanner sc = new Scanner(System.in);
	private MaterialRepository materialRepo;
	private RecipeRepository recipeRepo;

	public MaterialInventoryCLI(MaterialRepository materialRepo, RecipeRepository recipeRepo) {
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

	public MaterialRepository getRepo() {
		return materialRepo;
	}

	public void saveMaterial() {
		materialRepo.saveToFile();
	}

	public void Start() {
		materialRepo.loadFromFile();

		while (true) {
			System.out.println("1:材料一覧　2:材料追加　3:在庫の追加・削減　4:キーワード検索　5:材料名変更　6:材料削除　7:在庫切れの材料一覧　0:戻る");

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

				System.out.println("===材料一覧===");
				materialRepo.showAllMaterial();
				break;

			case 2: //材料追加

				addMaterial();
				break;

			case 3: //在庫の追加・削減

				changeMaterialStock();
				break;

			case 4: //キーワード検索

				keywordSearch();
				break;

			case 5: //材料名変更

				changeName();
				break;

			case 6: //材料削除

				removeMaterial();
				break;

			case 7:

				System.out.println("===在庫切れの材料一覧===");
				materialRepo.showNoStockMaterial();
				break;

			case 0:

				System.out.println("***戻る***");
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private void addMaterial() {
		System.out.println("===材料追加===");

		while (true) {

			String id = inputString("ID");

			String name = inputString("材料名");
			if (materialRepo.materialExistByName(name)) {
				System.out.println("既に同じ名前の材料があります");

				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			int price = inputInt("価格");

			if (price < 0) {
				System.out.println("価格は0以上の値を入力してください");
				continue;
			}

			int addStock = inputInt("在庫数");

			if (addStock < 0) {
				System.out.println("在庫数は0以上を入力してください");
				continue;
			}

			if (confirm("この内容で追加しますか？")) {
				System.out.println("材料を追加しました");
				materialRepo.addMaterial(id, name, price, addStock);
				break;
			}

			if (confirm("入力をやり直しますか？")) {
				System.out.println("入力をやり直します");
				continue;
			} else {
				System.out.println("選択画面に戻ります");
				break;
			}

		}

	}

	private void changeMaterialStock() {
		System.out.println("===在庫の追加・削減===");

		while (true) {

			String name = inputString("材料名");
			if (!materialRepo.materialExistByName(name)) {
				System.out.println("入力された名前の材料は存在しません");
				continue;
			}

			if (confirm("在庫の増加を行う場合はyを、在庫の削減を行う場合はnを入力してください")) {

				//在庫の増加を選んだ場合
				int amountAdd = inputInt("追加数を入力してください");

				if (!confirm("材料名:" + name + "   追加数:" + amountAdd + "でよろしいですか？")) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						break;
					}
				}

				if (amountAdd > 0) {
					if (materialRepo.addMaterialStock(name, amountAdd)) {
						break;
					} else {
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

			//在庫の削減を選んだ場合
			int amountRemove = inputInt("削減数を入力してください");
			if (!confirm("材料名:" + name + "   削減数:" + amountRemove + "でよろしいですか？")) {
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			if (amountRemove > 0) {
				if (materialRepo.removeMaterialStock(name, amountRemove)) {
					break;
				} else {
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

	private void keywordSearch() {
		System.out.println("===キーワード検索===");
		while (true) {
			if (confirm("材料名から検索する場合はyを、IDから検索する場合はnを入力してください")) {

				//商品名からの検索
				System.out.println("材料名から検索を行います");
				String keywordName = inputString("キーワード");
				if (materialRepo.materialNameKeywordExist(keywordName)) {
					System.out.println("キーワードに一致する材料が見つかりました");
					materialRepo.findMaterialByNameKeyword(keywordName);
					break;
				}

				System.out.println("キーワードに一致する材料は見つかりませんでした");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			//IDからの検索
			System.out.println("IDから検索を行います");
			String keywordID = inputString("キーワード");
			if (materialRepo.materialIdKeywordExist(keywordID)) {
				System.out.println("キーワードに一致する材料が見つかりました");
				materialRepo.findMaterialByIdKeyword(keywordID);
			}

			System.out.println("キーワードに一致する材料は見つかりませんでした");
			if (confirm("入力をやり直しますか？")) {
				System.out.println("入力をやり直します");
				continue;
			} else {
				System.out.println("選択画面に戻ります");
				break;
			}
		}
	}

	private void changeName() {
		System.out.println("===材料名変更===");
		while (true) {
			String oldName = inputString("材料名");

			String newName = inputString("変更後の材料名");
			if (!materialRepo.changeMaterialName(oldName, newName)) {
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}
			break;
		}
	}

	private void removeMaterial() {
		System.out.println("===材料削除===");
		while (true) {
			String name = inputString("材料名");
			if (!materialRepo.materialExistByName(name)) {
				System.out.println("入力された名前の材料は存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}
			int internalMaterialId = materialRepo.getInternalMaterialIdByMaterialName(name);
			if (!recipeRepo.findInternalProductIdByInternalMaterialId(internalMaterialId).isEmpty()) {
				System.out.println("削除しようとしている材料を使用した商品が登録されています。");
				System.out.println("材料を削除する前にこの材料を使用している商品の削除を行ってください");
				System.out.println("選択画面に戻ります");
				break;
			}

			if (!confirm("本当に削除してもよろしいですか？")) {
				System.out.println("選択画面に戻ります");
				break;
			}

			System.out.println("材料を削除しました");
			materialRepo.removeMaterial(name);

			break;
		}
	}

}
