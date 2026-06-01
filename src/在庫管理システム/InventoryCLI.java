package 在庫管理システム;

import java.util.Scanner;

public class InventoryCLI {
	private Scanner sc = new Scanner(System.in);
	private ProductRepository productRepo;
	private RecipeRepository recipeRepo;

	public InventoryCLI(ProductRepository productRepo, RecipeRepository recipeRepo) {
		this.productRepo = productRepo;
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

	public void productStart() {

		while (true) {
			System.out.println("1:商品一覧　2:商品追加　3:在庫の追加・削減　4:キーワード検索　5:商品名変更　6:商品削除　7:在庫切れの商品一覧　0:戻る");

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

				System.out.println("===商品一覧===");
				productRepo.showProduct();
				break;

			case 2: //商品追加

				addProduct();
				break;

			case 3: //在庫の追加・削減

				changeProductStock();
				break;

			case 4: //キーワード検索

				keywordSearch();
				break;

			case 5: //商品名変更

				changeName();
				break;

			case 6: //商品削除

				removeProduct();
				break;

			case 7:

				System.out.println("===在庫切れの商品一覧===");
				productRepo.showNoStockProduct();
				break;

			case 0:

				System.out.println("***戻る***");
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private void addProduct() {
		System.out.println("===商品追加===");

		while (true) {

			String id = inputString("ID");

			String name = inputString("商品名");
			if (productRepo.productExistByName(name)) {
				System.out.println("既に同じ名前の商品があります");

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
				System.out.println("商品を追加しました");
				productRepo.addProduct(id, name, price, addStock);
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

	private void changeProductStock() {
		System.out.println("===在庫の追加・削減===");

		while (true) {

			String name = inputString("商品名");
			if (!productRepo.productExistByName(name)) {
				System.out.println("入力された名前の商品は存在しません");
				continue;
			}

			if (confirm("在庫の増加を行う場合はyを、在庫の削減を行う場合はnを入力してください")) {

				//在庫の増加を選んだ場合
				int amountAdd = inputInt("追加数を入力してください");

				if (!confirm("商品名:" + name + "   追加数:" + amountAdd + "でよろしいですか？")) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						break;
					}
				}

				if (amountAdd > 0) {
					if (productRepo.findProduct(name).addProductStock(amountAdd)) {
						System.out.println("在庫を追加しました");
						break;
					} else {
						System.out.println("0より大きい値を入れてください");
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
			if (!confirm("商品名:" + name + "   削減数:" + amountRemove + "でよろしいですか？")) {
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			if (amountRemove > 0) {
				if (productRepo.findProduct(name).removeProductStock(amountRemove)) {
					System.out.println("在庫を削減しました");
					break;
				} else {
					System.out.println("在庫不足または0以下の値は指定できません");
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
			if (confirm("商品名から検索する場合はyを、IDから検索する場合はnを入力してください")) {

				//商品名からの検索
				System.out.println("商品名から検索を行います");
				String keywordName = inputString("キーワード");
				if (productRepo.productNameKeywordExist(keywordName)) {
					System.out.println("キーワードに一致する商品が見つかりました");
					productRepo.findProductByNameKeyword(keywordName);
					break;
				}

				System.out.println("キーワードに一致する商品は見つかりませんでした");
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
			if (productRepo.productIdKeywordExist(keywordID)) {
				System.out.println("キーワードに一致する商品が見つかりました");
				productRepo.findProductByIdKeyword(keywordID);
			}

			System.out.println("キーワードに一致する商品は見つかりませんでした");
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
		System.out.println("===商品名変更===");
		while (true) {
			String oldName = inputString("商品名");

			String newName = inputString("変更後の商品名");
			if (!productRepo.changeProductName(oldName, newName)) {
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}
			System.out.println("商品名を変更しました");
			break;
		}
	}

	private void removeProduct() {
		System.out.println("===商品削除===");
		while (true) {
			String name = inputString("商品名");
			if (!productRepo.productExistByName(name)) {
				System.out.println("入力された名前の商品は存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			if (!confirm("本当に削除してもよろしいですか？")) {
				System.out.println("選択画面に戻ります");
				break;
			}

			int internalProductId = productRepo.getInternalProductIdByProductName(name);

			productRepo.removeProduct(name);
			System.out.println("商品を削除しました");

			if (recipeRepo.confirmRecipeByInternalProductId(internalProductId)) {
				recipeRepo.removeRecipe(internalProductId);
				System.out.println("登録されていたレシピも削除されました");
				break;
			}

			System.out.println("商品のレシピは未登録だったため、レシピ削除は行いませんでした");
			System.out.println("選択画面に戻ります");
			break;
		}
	}

}