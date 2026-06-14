package 在庫管理システム;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EcListingRepository {
	HashMap<Integer, List<EcListing>> ecListingByInternalEcSiteId = new HashMap<>();

	//ECサイトの販売状況や価格を保持するクラスとECサイトの内部IDとの紐づけを行う
	public void addEcListing(int internalEcSite, int internalProductId, String sellingStatus, int sellingPrice) {
		if (!checkEcSiteExist(internalEcSite)) {
			List<EcListing> ecListings = new ArrayList<EcListing>();
			ecListingByInternalEcSiteId.put(internalEcSite, ecListings);
		}

		ecListingByInternalEcSiteId.get(internalEcSite)
				.add(new EcListing(internalEcSite, internalProductId, sellingStatus, sellingPrice));

	}

	//紐づけを解除
	public void removeListingsByEcSiteId(int internalEcSiteId) {
		ecListingByInternalEcSiteId.remove(internalEcSiteId);
	}

	//ECサイトから商品を削除する
	public void removeProductFromEcSite(int internalEcSite, int internalProductId) {
		List<EcListing> ecListings = ecListingByInternalEcSiteId.get(internalEcSite);

		if (ecListings == null) {
			return;
		}

		ecListings.removeIf(ecLi -> ecLi.getInternalProductId() == internalProductId);

		if (ecListingByInternalEcSiteId.get(internalEcSite).isEmpty()) {
			ecListingByInternalEcSiteId.remove(internalEcSite);
		}

	}

	//ECサイト内部IDから検索して、ハッシュマップが作られているかを確認する
	public boolean checkEcSiteExist(int internalEcSiteId) {
		if (ecListingByInternalEcSiteId.get(internalEcSiteId) != null) {
			return true;
		}

		return false;
	}

	//指定されたECサイトに登録したい商品が既に登録されていないかを確認する
	public boolean existProductInEcListingByInternalEcSiteId(int internalEcSite, int internalProductId) {

		List<EcListing> ecLi = ecListingByInternalEcSiteId.get(internalEcSite);

		if (ecLi == null) {
			return false;
		}

		for (EcListing ecLis : ecLi) {
			if (ecLis.getInternalProductId() == internalProductId) {
				return true;
			}
		}
		return false;
	}

	//ECサイトでの商品の販売状態を扱うクラスを得る
	public EcListing getEcListing(int internalEcSiteId, int internalProductId) {

		List<EcListing> ecListings = ecListingByInternalEcSiteId.get(internalEcSiteId);
		if (ecListings == null) {
			return null;
		}

		for (EcListing ecLi : ecListings) {
			if (ecLi.getInternalProductId() == internalProductId) {
				return ecLi;
			}
		}
		return null;
	}

	//販売状況の変更
	public void changeSellingStatus(EcListing ecLi, String newSellingStatus) {
		ecLi.changeSellingStatus(newSellingStatus);
	}

	//販売価格の変更
	public void changeSellingPrice(EcListing ecli, int newSellingPrice) {
		ecli.changeSellingPrice(newSellingPrice);
	}

	//全ECサイトから商品を削除する
	public boolean removeProductFromAllEcSite(int internalProductId) {
		int flag = 0;
		for (List<EcListing> ecListings : ecListingByInternalEcSiteId.values()) {
			int count = 0;
			for (EcListing ecLi : ecListings) {
				if (ecLi.getInternalProductId() == internalProductId) {
					ecListings.remove(count);
					count++;
					flag = 1;
				}
			}
		}

		ecListingByInternalEcSiteId.entrySet().removeIf(entry -> entry.getValue().isEmpty());

		if (flag == 1) {
			return true; //少なくとも一つのECサイトから商品を削除したとき
		}

		return false; //既にすべてのECサイトから商品が削除されていた時
	}

	//商品IDからECサイトの内部IDと販売状況を取得する
	public HashMap<Integer, String> getInternalEcSiteIdAndSellingStatusByInternalProductId(int internalProductId) {
		HashMap<Integer, String> sellingStatusByInternalEcSiteId = new HashMap<Integer, String>();
		for (List<EcListing> ecListings : ecListingByInternalEcSiteId.values()) {
			for (EcListing ecLi : ecListings) {
				if (ecLi.getInternalProductId() == internalProductId) {
					sellingStatusByInternalEcSiteId.put(ecLi.getInternalEcSiteId(), ecLi.getSellingStatus());
				}
			}
		}

		return sellingStatusByInternalEcSiteId;
	}

	//ファイルへの保存
	public void saveToFile() {
		try (PrintWriter pw = new PrintWriter("eclistings.txt")) {
			for (List<EcListing> ecLis : ecListingByInternalEcSiteId.values()) {
				for (EcListing ecLi : ecLis) {
					pw.println(ecLi.getInternalEcSiteId() + "," + ecLi.getInternalProductId() + ","
							+ ecLi.getSellingStatus() + "," +
							+ecLi.getSellingPrice());
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ファイルから読み出す
	public void loadFromFile() {
		try (BufferedReader bf = new BufferedReader(new FileReader("eclistings.txt"))) {
			String line;

			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length % 4 != 0)
					continue;

				int internalEcSiteId = Integer.parseInt(parts[0].trim());
				int internalProductId = Integer.parseInt(parts[1].trim());
				String sellingStatus = parts[2].trim();
				int sellingPrice = Integer.parseInt(parts[3].trim());

				addEcListing(internalEcSiteId, internalProductId, sellingStatus, sellingPrice);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
