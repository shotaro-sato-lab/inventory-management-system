package 在庫管理システム;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;

public class EcSiteRepository {

	HashMap<Integer, EcSite> ecSiteByInternalEcSiteId = new HashMap<>();
	HashMap<String, Integer> internalEcSiteIdByEcSiteName = new HashMap<>();

	private int count = 0; //ECサイトの内部IDの作成に使用

	public int makeInternalEcSiteId() {
		count++;
		return count;
	}

	public void changeCount(int newCount) {
		count = newCount;
	}

	//ECサイトの追加
	public boolean addEcSite(String ecSiteId, String ecSiteName, float feeRate, int deliveryCost) {

		if (internalEcSiteIdByEcSiteName.containsKey(ecSiteName)) {
			return false;
		}

		int internalEcSiteId = makeInternalEcSiteId();
		ecSiteByInternalEcSiteId.put(internalEcSiteId,
				new EcSite(internalEcSiteId, ecSiteId, ecSiteName, feeRate, deliveryCost));
		internalEcSiteIdByEcSiteName.put(ecSiteName, internalEcSiteId);
		return true;

	}

	//ECサイト名からECサイトを削除
	public void removeEcSite(String ecSiteName) {
		ecSiteByInternalEcSiteId.remove(internalEcSiteIdByEcSiteName.get(ecSiteName));
		internalEcSiteIdByEcSiteName.remove(ecSiteName);
	}

	//ECサイト名からECサイトを返す
	public EcSite findEcSiteByEcSiteName(String ecSiteName) {

		Integer internalEcSiteId = internalEcSiteIdByEcSiteName.get(ecSiteName);
		if (internalEcSiteId == null) {
			return null;
		}

		return ecSiteByInternalEcSiteId.get(internalEcSiteId);
	}

	//同じECサイトIDが既に存在するかを確認する
	public boolean exsitEcSiteId(String ecSiteid) {
		for (EcSite ec : ecSiteByInternalEcSiteId.values()) {
			if (ec.getEcSiteId().equals(ecSiteid)) {
				return true;
			}
		}

		return false;
	}

	//同じECサイト名が既に存在しないか確認する
	public boolean existEcSiteName(String ecSiteName) {
		return internalEcSiteIdByEcSiteName.containsKey(ecSiteName);
	}

	//ECサイトIDからECサイトの内部IDを得る
	public int getInternalEcSiteIdByEcSiteId(String ecSiteId) {
		for (EcSite ec : ecSiteByInternalEcSiteId.values()) {
			if (ec.getEcSiteId().equals(ecSiteId)) {
				return ec.getInternalEcSiteId();
			}
		}
		return -1;
	}

	//ECサイトIDからECサイトIDを変更する
	public void changeEcSiteId(String oldEcSiteId, String newEcSiteId) {
		for (EcSite ec : ecSiteByInternalEcSiteId.values()) {
			if (ec.getEcSiteId().equals(oldEcSiteId)) {
				ec.changeEcSiteId(newEcSiteId);
			}
		}
	}

	//ECサイト名からECサイト名を変更する
	public void changeEcSiteName(String oldEcSiteName, String newEcSiteName) {
		Integer internalEcSiteId = internalEcSiteIdByEcSiteName.get(oldEcSiteName);
		ecSiteByInternalEcSiteId.get(internalEcSiteId).changeEcSiteName(newEcSiteName);
		internalEcSiteIdByEcSiteName.remove(oldEcSiteName);
		internalEcSiteIdByEcSiteName.put(newEcSiteName, internalEcSiteId);
	}

	//ECサイト名から検索して手数料の率を変更する
	public boolean changeFeeRate(String ecSiteName, float newFeeRate) {
		Integer internalEcSiteId = internalEcSiteIdByEcSiteName.get(ecSiteName);
		if (internalEcSiteId == null) {
			return false;
		}

		ecSiteByInternalEcSiteId.get(internalEcSiteId).changeFeeRate(newFeeRate);
		return true;
	}

	//ECサイト名から検索して配送料を変更する
	public boolean changeDeliveryCost(String ecSiteName, int newDeliveryCost) {
		Integer internalEcSiteId = internalEcSiteIdByEcSiteName.get(ecSiteName);
		if (internalEcSiteId == null) {
			return false;
		}

		ecSiteByInternalEcSiteId.get(internalEcSiteId).changeDeliveryCost(newDeliveryCost);
		return true;
	}

	//ファイルへの保存
	public void saveToFile() {
		try (PrintWriter pw = new PrintWriter("ecsites.txt")) {
			for (EcSite ec : ecSiteByInternalEcSiteId.values()) {
				pw.println(ec.getInternalEcSiteId() + "," + ec.getEcSiteId() + "," + ec.getEcSiteName() + ","
						+ ec.getFeeRate() + "," + ec.getDeliveryCost());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ファイルから読み出す
	public void loadFromFile() {
		try (BufferedReader bf = new BufferedReader(new FileReader("ecsites.txt"))) {
			String line;
			int counter = 0; //ECサイトの内部ID作成のためのcountを戻すために用意

			while ((line = bf.readLine()) != null) {
				String[] parts = line.split(",");

				if (parts.length % 5 != 0)
					continue;

				int internalEcSiteId = Integer.parseInt(parts[0].trim());
				String ecSiteId = parts[1].trim();
				String ecSiteName = parts[2].trim();
				float feeRate = Float.parseFloat(parts[3].trim());
				int deliveryCost = Integer.parseInt(parts[4].trim());

				internalEcSiteIdByEcSiteName.put(ecSiteName, internalEcSiteId);
				ecSiteByInternalEcSiteId.put(internalEcSiteId,
						new EcSite(internalEcSiteId, ecSiteId, ecSiteName, feeRate, deliveryCost));

				if (internalEcSiteId > counter) {
					counter = internalEcSiteId;
				}
			}

			changeCount(counter);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
