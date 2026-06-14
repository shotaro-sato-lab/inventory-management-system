package 在庫管理システム;

import java.util.Scanner;

public class EcSiteCLI {

	private Scanner sc = new Scanner(System.in);
	private ProductRepository productRepo;
	private MaterialRepository materialRepo;
	private EcSiteRepository ecSiteRepo;
	private EcListingRepository ecListingRepo;

	public EcSiteCLI(ProductRepository productRepo, MaterialRepository materialRepo, EcSiteRepository ecSiteRepo,
			EcListingRepository ecListingRepo) {
		this.productRepo = productRepo;
		this.materialRepo = materialRepo;
		this.ecSiteRepo = ecSiteRepo;
		this.ecListingRepo = ecListingRepo;
	}

	private int inputInt(String message) {
		while (true) {
			System.out.println(message);
			String input = sc.nextLine();

			if (input.trim().isEmpty()) {
				System.out.println("値を入力してください");
				continue;
			}

			if (input.contains(",")) {
				System.out.println(",（カンマ）は使用しないでください");
				continue;
			}

			try {
				return Integer.parseInt(input);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private float inputFloat(String message) {
		while (true) {
			System.out.println(message);
			String input = sc.nextLine();

			if (input.trim().isEmpty()) {
				System.out.println("値を入力してください");
				continue;
			}

			if (input.contains(",")) {
				System.out.println(",（カンマ）は使用しないでください");
				continue;
			}

			try {
				return Float.parseFloat(input);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
			}
		}
	}

	private String inputString(String message) {
		System.out.println(message + ":");
		while (true) {
			String input = sc.nextLine();

			if (input.contains(",")) {
				System.out.println(",（カンマ）は使用しないでください");
				continue;
			}

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

	public void ecSiteStart() {

		while (true) {

			System.out.println(
					"1:ECサイトの登録　2:ECサイトの削除　3:ECサイトの登録情報の変更　4:ECサイト一覧　5:販売商品の登録　"
							+ "6:ECサイトから商品を削除する　7:商品の販売状況の変更　8:商品の販売価格の変更　0:戻る");

			String choiceStr = sc.nextLine();
			int choice;
			try {
				choice = Integer.parseInt(choiceStr);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
				continue;
			}

			switch (choice) {

			case 1: //ECサイトの登録

				addEcSite();
				break;

			case 2: //ECサイトの削除

				removeEcSite();
				break;

			case 3: //ECサイトの登録情報の変更

				changeEcSiteInfo();
				break;

			case 4: //ECサイト一覧

				for (EcSite ec : ecSiteRepo.ecSiteByInternalEcSiteId.values()) {
					System.out.println(ec);
				}
				break;

			case 5: //販売商品の登録

				addProductToEcSite();
				break;

			case 6: //ECサイトから商品を削除する

				removeProductFromEcSite();
				break;

			case 7: //商品の販売状況の変更

				changeSellingStatus();
				break;

			case 8: //商品の販売価格の変更

				changeSellingPrice();
				break;

			case 0:

				System.out.println("***戻る***");
				return;

			default:

				System.out.println("正しい数字を入力してください");

			}
		}

	}

	public void addEcSite() {

		while (true) {

			System.out.println("ECサイトの登録を行います");

			String ecSiteId = inputString("ECサイトのID");
			if (ecSiteRepo.exsitEcSiteId(ecSiteId)) {
				System.out.println("同じIDのECサイトが既に存在します");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			String ecSiteName = inputString("ECサイトの名前");
			if (ecSiteRepo.existEcSiteName(ecSiteName)) {
				System.out.println("同じ名前のECサイトが既に存在します");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			float feeRate = inputFloat("ECサイトの手数料の率");
			int deliveryCost = inputInt("配送料");

			ecSiteRepo.addEcSite(ecSiteId, ecSiteName, feeRate, deliveryCost);

			System.out.println("ECサイトを登録しました");

			return;

		}

	}

	public void removeEcSite() {

		System.out.println("ECサイトの削除を行います");

		while (true) {

			String ecSiteName = inputString("ECサイトの名前");

			if (!ecSiteRepo.existEcSiteName(ecSiteName)) {
				System.out.println("該当するECサイトは存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}
			int internalEcSiteId = ecSiteRepo.internalEcSiteIdByEcSiteName.get(ecSiteName);

			ecSiteRepo.removeEcSite(ecSiteName);
			ecListingRepo.removeListingsByEcSiteId(internalEcSiteId);

			System.out.println("登録されていたECサイトを削除しました");
			return;
		}
	}

	public void changeEcSiteInfo() {

		System.out.println("ECサイトの登録情報の変更を行います");

		while (true) {

			System.out.println("1:ECサイトIDの変更　2:ECサイト名の変更　3:ECサイトの手数料の率の変更　4:配送料の変更　0:戻る");

			String choiceStr = sc.nextLine();
			int choice;
			try {
				choice = Integer.parseInt(choiceStr);
			} catch (NumberFormatException e) {
				System.out.println("正しい数字を入力してください");
				continue;
			}

			switch (choice) {
			case 1: //ECサイトIDの変更

				changeEcSiteId();
				break;

			case 2: //ECサイト名の変更

				changeEcSiteName();
				break;

			case 3: //ECサイトの手数料の率の変更

				changeFeeRate();
				break;

			case 4: //配送料の変更

				changeDeliveryCost();
				break;

			case 0:

				System.out.println("***戻る***");
				return;

			default:
				System.out.println("正しい数字を入力してください");
			}

		}

	}

	public void changeEcSiteId() {

		System.out.println("ECサイトのIDの変更を行います");
		while (true) {

			String ecSiteId = inputString("変更前のECサイトのID");
			if (!ecSiteRepo.exsitEcSiteId(ecSiteId)) {
				System.out.println("入力されたECサイトのIDを持つECサイトは登録されていないです");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			String newEcSiteId = inputString("変更後のECサイトのID");
			if (ecSiteRepo.exsitEcSiteId(newEcSiteId)) {
				System.out.println("入力されたIDと同じIDのECサイトが既に登録されています");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			ecSiteRepo.changeEcSiteId(ecSiteId, newEcSiteId);
			System.out.println("ECサイトのIDを変更しました");

			return;
		}
	}

	public void changeEcSiteName() {

		System.out.println("ECサイトの名前を変更します");
		while (true) {

			String oldEcSiteName = inputString("変更前のECサイトの名前");
			if (!ecSiteRepo.existEcSiteName(oldEcSiteName)) {
				System.out.println("該当するECサイトは存在しません");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			String newEcSiteName = inputString("変更後のECサイトの名前");
			if (ecSiteRepo.existEcSiteName(newEcSiteName)) {
				System.out.println("入力された名前と同じ名前のECサイトが既に存在します");
				if (confirm("入力をやり直しますか？")) {
					System.out.println("入力をやり直します");
					continue;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}

			ecSiteRepo.changeEcSiteName(oldEcSiteName, newEcSiteName);
			System.out.println("ECサイト名を変更しました");

			return;
		}
	}

	public void changeFeeRate() {

		System.out.println("ECサイトの手数料の率を変更します");
		while (true) {

			String ecSiteName = inputString("変更するECサイトの名前");
			float newFeeRate = inputFloat("新しい手数料の率");

			if (ecSiteRepo.changeFeeRate(ecSiteName, newFeeRate)) {
				System.out.println("手数料を変更しました");
				return;
			}

			System.out.println("該当するECサイトが存在しません");
			if (confirm("入力をやり直しますか？")) {
				System.out.println("入力をやり直します");
				continue;
			} else {
				System.out.println("選択画面に戻ります");
				return;
			}

		}
	}

	public void changeDeliveryCost() {

		System.out.println("配送料を変更します");
		while (true) {

			String ecSiteName = inputString("変更するECサイトの名前");
			int newDeliveryCost = inputInt("新しい配送料");

			if (ecSiteRepo.changeDeliveryCost(ecSiteName, newDeliveryCost)) {
				System.out.println("配送料を変更しました");
				return;
			}

			System.out.println("該当するECサイトが存在しません");
			if (confirm("入力をやり直しますか？")) {
				System.out.println("入力をやり直します");
				continue;
			} else {
				System.out.println("選択画面に戻ります");
				return;
			}
		}
	}

	public void addProductToEcSite() {

		System.out.println("ECサイトに商品を登録します");
		while (true) {

			int internalProductId;

			if (confirm("商品名から検索する場合はyを、IDから検索する場合はnを入力してください")) {
				//商品名から商品内部IDを得る場合

				System.out.println("商品名から検索します");
				String productName = inputString("商品名");
				if (!productRepo.productExistByName(productName)) {
					System.out.println("該当する商品が存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductName(productName);
			} else {
				//商品IDから商品内部IDを得る場合

				System.out.println("商品IDから検索します");
				String productId = inputString("商品ID");
				if (!productRepo.productExistByProductId(productId)) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductId(productId);
			}

			while (true) {
				int internalEcSiteId;

				if (confirm("ECサイト名から検索する場合はyを、ECサイトIDから検索する場合はnを入力してください")) {
					//ECサイト名から検索する場合

					String ecSiteName = inputString("ECサイト名");
					if (!ecSiteRepo.existEcSiteName(ecSiteName)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.internalEcSiteIdByEcSiteName.get(ecSiteName);
				} else {
					//ECサイトIDから検索する場合

					String ecSiteId = inputString("ECサイトID");
					if (!ecSiteRepo.exsitEcSiteId(ecSiteId)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.getInternalEcSiteIdByEcSiteId(ecSiteId);
				}

				if (ecListingRepo.existProductInEcListingByInternalEcSiteId(internalEcSiteId, internalProductId)) {
					System.out.println("商品は既に該当するECサイトに登録されています");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				String sellingStatus;
				if (confirm("商品を販売中にする場合はyを、販売停止状態にする場合はnを入力してください")) {
					sellingStatus = "販売中";
				} else {
					sellingStatus = "販売停止";
				}

				int sellingPrice = inputInt("販売価格");

				ecListingRepo.addEcListing(internalEcSiteId, internalProductId, sellingStatus, sellingPrice);
				System.out.println("商品を登録しました");

				if (confirm("ECサイトへの登録を続行しますか?")) {
					break;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}
		}
	}

	public void removeProductFromEcSite() {

		System.out.println("ECサイトから商品を削除します");
		while (true) {

			int internalProductId;

			if (confirm("商品名から検索する場合はyを、IDから検索する場合はnを入力してください")) {
				//商品名から商品内部IDを得る場合

				System.out.println("商品名から検索します");
				String productName = inputString("商品名");
				if (!productRepo.productExistByName(productName)) {
					System.out.println("該当する商品が存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductName(productName);
			} else {
				//商品IDから商品内部IDを得る場合

				System.out.println("商品IDから検索します");
				String productId = inputString("商品ID");
				if (!productRepo.productExistByProductId(productId)) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductId(productId);
			}

			while (true) {
				int internalEcSiteId;

				if (confirm("ECサイト名から検索する場合はyを、ECサイトIDから検索する場合はnを入力してください")) {
					//ECサイト名から検索する場合

					String ecSiteName = inputString("ECサイト名");
					if (!ecSiteRepo.existEcSiteName(ecSiteName)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.internalEcSiteIdByEcSiteName.get(ecSiteName);
				} else {
					//ECサイトIDから検索する場合

					String ecSiteId = inputString("ECサイトID");
					if (!ecSiteRepo.exsitEcSiteId(ecSiteId)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.getInternalEcSiteIdByEcSiteId(ecSiteId);
				}

				if (!ecListingRepo.existProductInEcListingByInternalEcSiteId(internalEcSiteId, internalProductId)) {
					System.out.println("該当するECサイトに商品は登録されていません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				ecListingRepo.removeProductFromEcSite(internalEcSiteId, internalProductId);
				System.out.println("ECサイトから商品を削除しました");

				if (confirm("ECサイトからの削除を続行しますか?")) {
					break;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}
		}

	}

	public void changeSellingStatus() {
		System.out.println("商品の販売状況を変更します");
		while (true) {

			System.out.println("販売状況を変更する商品を検索します");
			int internalProductId;

			if (confirm("商品名から検索する場合はyを、IDから検索する場合はnを入力してください")) {
				//商品名から商品内部IDを得る場合

				System.out.println("商品名から検索します");
				String productName = inputString("商品名");
				if (!productRepo.productExistByName(productName)) {
					System.out.println("該当する商品が存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductName(productName);
			} else {
				//商品IDから商品内部IDを得る場合

				System.out.println("商品IDから検索します");
				String productId = inputString("商品ID");
				if (!productRepo.productExistByProductId(productId)) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductId(productId);
			}

			while (true) {
				System.out.println("販売先のECサイトを検索します");
				int internalEcSiteId;

				if (confirm("ECサイト名から検索する場合はyを、ECサイトIDから検索する場合はnを入力してください")) {
					//ECサイト名から検索する場合

					String ecSiteName = inputString("ECサイト名");
					if (!ecSiteRepo.existEcSiteName(ecSiteName)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.internalEcSiteIdByEcSiteName.get(ecSiteName);
				} else {
					//ECサイトIDから検索する場合

					String ecSiteId = inputString("ECサイトID");
					if (!ecSiteRepo.exsitEcSiteId(ecSiteId)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.getInternalEcSiteIdByEcSiteId(ecSiteId);
				}

				if (!ecListingRepo.existProductInEcListingByInternalEcSiteId(internalEcSiteId, internalProductId)) {
					System.out.println("該当するECサイトに商品は登録されていません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				EcListing ecLi = ecListingRepo.getEcListing(internalEcSiteId, internalProductId);
				String oldSellingStatus = ecLi.getSellingStatus();
				System.out.println("現在の販売状況:" + oldSellingStatus);

				String newSellingStatus;
				if (confirm("商品を販売中にする場合はyを、販売停止状態にする場合はnを入力してください")) {
					newSellingStatus = "販売中";
				} else {
					newSellingStatus = "販売停止";
				}

				ecListingRepo.changeSellingStatus(ecLi, newSellingStatus);
				System.out.println("販売状況を変更しました");
				System.out.println("現在の販売状況:" + newSellingStatus);

				if (confirm("他のECサイトでの販売状況も変更しますか?")) {
					break;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}
		}
	}

	public void changeSellingPrice() {
		System.out.println("商品の販売価格を変更します");
		while (true) {

			System.out.println("販売価格を変更する商品を検索します");
			int internalProductId;

			if (confirm("商品名から検索する場合はyを、IDから検索する場合はnを入力してください")) {
				//商品名から商品内部IDを得る場合

				System.out.println("商品名から検索します");
				String productName = inputString("商品名");
				if (!productRepo.productExistByName(productName)) {
					System.out.println("該当する商品が存在しません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductName(productName);
			} else {
				//商品IDから商品内部IDを得る場合

				System.out.println("商品IDから検索します");
				String productId = inputString("商品ID");
				if (!productRepo.productExistByProductId(productId)) {
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				internalProductId = productRepo.getInternalProductIdByProductId(productId);
			}

			while (true) {
				System.out.println("販売先のECサイトを検索します");
				int internalEcSiteId;

				if (confirm("ECサイト名から検索する場合はyを、ECサイトIDから検索する場合はnを入力してください")) {
					//ECサイト名から検索する場合

					String ecSiteName = inputString("ECサイト名");
					if (!ecSiteRepo.existEcSiteName(ecSiteName)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.internalEcSiteIdByEcSiteName.get(ecSiteName);
				} else {
					//ECサイトIDから検索する場合

					String ecSiteId = inputString("ECサイトID");
					if (!ecSiteRepo.exsitEcSiteId(ecSiteId)) {
						System.out.println("該当するECサイトが存在しません");
						if (confirm("入力をやり直しますか？")) {
							System.out.println("入力をやり直します");
							continue;
						} else {
							System.out.println("選択画面に戻ります");
							return;
						}
					}

					internalEcSiteId = ecSiteRepo.getInternalEcSiteIdByEcSiteId(ecSiteId);
				}

				if (!ecListingRepo.existProductInEcListingByInternalEcSiteId(internalEcSiteId, internalProductId)) {
					System.out.println("該当するECサイトに商品は登録されていません");
					if (confirm("入力をやり直しますか？")) {
						System.out.println("入力をやり直します");
						continue;
					} else {
						System.out.println("選択画面に戻ります");
						return;
					}
				}

				EcListing ecLi = ecListingRepo.getEcListing(internalEcSiteId, internalProductId);
				int oldSellingPrice = ecLi.getSellingPrice();
				System.out.println("現在の販売価格:" + oldSellingPrice);

				int newSellingPrice = inputInt("変更後の販売価格");

				ecListingRepo.changeSellingPrice(ecLi, newSellingPrice);
				System.out.println("販売価格を変更しました");
				System.out.println("現在の販売価格:" + newSellingPrice);

				if (confirm("他のECサイトでの販売価格も変更しますか?")) {
					break;
				} else {
					System.out.println("選択画面に戻ります");
					return;
				}
			}
		}
	}

}
