package 在庫管理システム;

public class EcListing {
	private final int internalEcSiteId; //ECサイトの内部ID
	private final int internalProductId; //商品の内部ID
	private String sellingStatus; //販売状況
	private int sellingPrice; //販売価格

	public EcListing(int internalEcSiteId, int internalProductId, String sellingStatus, int sellingPrice) {
		this.internalEcSiteId = internalEcSiteId;
		this.internalProductId = internalProductId;
		this.sellingStatus = sellingStatus;
		this.sellingPrice = sellingPrice;
	}

	public int getInternalEcSiteId() {
		return internalEcSiteId;
	}

	public int getInternalProductId() {
		return internalProductId;
	}

	public String getSellingStatus() {
		return sellingStatus;
	}

	public int getSellingPrice() {
		return sellingPrice;
	}

	//販売状況を変更する
	public void changeSellingStatus(String newSellingStatus) {
		sellingStatus = newSellingStatus;
	}

	//販売価格を変更する
	public void changeSellingPrice(int newSellingPrice) {
		sellingPrice = newSellingPrice;
	}

}
