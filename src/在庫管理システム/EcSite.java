package 在庫管理システム;

public class EcSite {

	private final int internalEcSiteId; //ECサイトの内部ID
	private String ecSiteId; //ECサイトのID
	private String ecSiteName; //ECサイトの名前
	private float feeRate; //手数料の率
	private int deliveryCost; //配送料

	public EcSite(int internalEcSiteId, String ecSiteId, String EcSiteName, float feeRate, int deliveryCost) {

		this.internalEcSiteId = internalEcSiteId;
		this.ecSiteId = ecSiteId;
		this.ecSiteName = EcSiteName;
		this.feeRate = feeRate;
		this.deliveryCost = deliveryCost;

	}

	public int getInternalEcSiteId() {
		return internalEcSiteId;
	}

	public String getEcSiteId() {
		return ecSiteId;
	}

	public String getEcSiteName() {
		return ecSiteName;
	}

	public float getFeeRate() {
		return feeRate;
	}

	public int getDeliveryCost() {
		return deliveryCost;
	}

	//ECサイトIDの変更
	public void changeEcSiteId(String newEcSiteId) {
		ecSiteId = newEcSiteId;
	}

	//ECサイト名の変更
	public void changeEcSiteName(String newEcSiteName) {
		ecSiteName = newEcSiteName;
	}

	//ECサイトの手数料の率の変更
	public void changeFeeRate(float newFeeRate) {
		feeRate = newFeeRate;
	}

	//配送料の変更
	public void changeDeliveryCost(int newDeliveryCost) {
		deliveryCost = newDeliveryCost;
	}

	@Override
	public String toString() {
		return "ECサイトID:" + ecSiteId + "　ECサイト名:" + ecSiteName + "　手数料の率:" + feeRate + "　配送料:" + deliveryCost;
	}
}
