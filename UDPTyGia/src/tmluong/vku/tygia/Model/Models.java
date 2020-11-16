package tmluong.vku.tygia.Model;

import java.io.Serializable;

public class Models implements Serializable{
	private String id;
	private String country;
	private String cash;
	
	public Models(String id,String country, String cash) {
		super();
		this.id = id;
		this.country = country;
		this.cash = cash;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCash() {
		return cash;
	}
	public void setCash(String cash) {
		this.cash = cash;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Id: "+this.id+" | Country: "+this.country+" | Cash: "+this.cash;
	}
}
