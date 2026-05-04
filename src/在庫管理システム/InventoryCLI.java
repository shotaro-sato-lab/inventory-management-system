package 在庫管理システム;

import java.util.Scanner;

public class InventoryCLI {
	private Scanner sc = new Scanner(System.in);
	private ProductRepository repo = new ProductRepository();

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

	public void start() {

		repo.loadFromFile();

		while (true) {
			System.out.println("1:商品一覧　2:商品追加　3:在庫追加　4:在庫削減　5:商品検索　6:商品名変更　7:商品削除　8:在庫切れの商品一覧　0:終了");

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
				repo.showProduct();
				break;

			case 2: //商品追加

				addProduct();
				break;

			case 3: //在庫追加

				addStock();
				break;

			case 4: //在庫削減

				removeStock();
				break;

			case 5: //商品検索

				productSearch();
				break;

			case 6: //商品名変更

				changeName();
				break;

			case 7: //商品削除

				removeProduct();
				break;

			case 8:

				System.out.println("===在庫切れの商品一覧===");
				repo.showNoStockProduct();
				break;

			case 0:

				System.out.println("***終了***");
				repo.saveToFile();
				sc.close();
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private void addProduct() {
		System.out.println("===商品追加===");

		while (true) {

			String name = inputString("商品名");
			if (repo.productExist(name)) {
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
				repo.addProduct(name, price, addStock);
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

	private void addStock() {
		System.out.println("===在庫追加===");

		while (true) {

			String name = inputString("商品名");
			if (!repo.productExist(name)) {
				System.out.println("入力された名前の商品は存在しません");
				continue;
			}

			int amount = inputInt("追加数を入力してください");
			if (!confirm("商品名:" + name + "   追加数:" + amount + "でよろしいですか？")) {
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			if (repo.findProduct(name).addStock(amount)) {
				System.out.println("在庫を追加しました");
				break;
			} else {
				System.out.println("0より大きい値を入れてください");
				continue;
			}
		}
	}

	private void removeStock() {
		System.out.println("===在庫削減===");

		while (true) {

			String name = inputString("商品名");
			if (!repo.productExist(name)) {
				System.out.println("入力された名前の商品は存在しません");
				continue;
			}

			int amount = inputInt("削減数を入力してください");
			if (!confirm("商品名:" + name + "   削減数:" + amount + "でよろしいですか？")) {
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}
			if (repo.findProduct(name).removeStock(amount)) {
				System.out.println("在庫を削減しました");
				break;
			} else {
				System.out.println("在庫不足または0以下の値は指定できません");
			}
		}
	}

	private void productSearch() {
		System.out.println("===商品検索===");
		while (true) {
			String name = inputString("商品名");
			if (!repo.productExist(name)) {
				System.out.println("入力された名前の商品は存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			Product p = repo.findProduct(name);

			System.out.println(p);
			break;
		}

	}

	private void changeName() {
		System.out.println("===商品名変更===");
		while (true) {
			String oldName = inputString("商品名");

			String newName = inputString("変更後の商品名");
			if (!repo.changeName(oldName, newName)) {
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
			if (!repo.productExist(name)) {
				System.out.println("入力された名前の商品は存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					break;
				}
			}

			repo.removeProduct(name);
			System.out.println("商品を削除しました");
			break;
		}
	}

}
