package org.gl.ceir.graph.model;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

@Component
public class RegisteredDeviceRawList {
	@JsonProperty("Date")
	@SerializedName("Date")
	private String date;
	
	@JsonProperty("NOT KNOWN")
	@SerializedName("NOT KNOWN")
	private String notKnown;

	@JsonProperty("Wearable")
	@SerializedName("Wearable")
	private String wearable;
	
	@JsonProperty("WLAN Router")
	@SerializedName("WLAN Router")
	private String wlanRouter;
	
	@JsonProperty("Handheld")
	@SerializedName("Handheld")
	private String handhled;
	
	@JsonProperty("Modem")
	@SerializedName("Modem")
	private String modem;
	
	@JsonProperty("Vehicle")
	@SerializedName("Vehicle")
	private String vehicle;
	
	@JsonProperty("Dongle")
	@SerializedName("Dongle")
	private String dongle;
	
	@JsonProperty("IoT Device")
	@SerializedName("IoT Device")
	private String ioTDevice;
	
	@JsonProperty("Smartphone")
	@SerializedName("Smartphone")
	private String smartphone;
	
	@JsonProperty("Mobile Phone/Feature phone")
	@SerializedName("Mobile Phone/Feature phone")
	private String mobileFeaturePhone;
	
	/*
	 * @JsonProperty("NA")
	 * 
	 * @SerializedName("NA") private String na;
	 */
	
	@JsonProperty("Tablet")
	@SerializedName("Tablet")
	private String tablet;
	
	@JsonProperty("Connected Computer")
	@SerializedName("Connected Computer")
	private String connectedComputer;
	
	
	@JsonProperty("Portable(include PDA)")
	@SerializedName("Portable(include PDA)")
	private String portableIncludePDA;
	
	@JsonProperty("e-Book")
	@SerializedName("e-Book")
	private String eBook;
	
	@JsonProperty("Module")
	@SerializedName("Module")
	private String module;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getNotKnown() {
		return notKnown;
	}

	public void setNotKnown(String notKnown) {
		this.notKnown = notKnown;
	}

	public String getWearable() {
		return wearable;
	}

	public void setWearable(String wearable) {
		this.wearable = wearable;
	}

	public String getWlanRouter() {
		return wlanRouter;
	}

	public void setWlanRouter(String wlanRouter) {
		this.wlanRouter = wlanRouter;
	}

	public String getHandhled() {
		return handhled;
	}

	public void setHandhled(String handhled) {
		this.handhled = handhled;
	}

	public String getModem() {
		return modem;
	}

	public void setModem(String modem) {
		this.modem = modem;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getDongle() {
		return dongle;
	}

	public void setDongle(String dongle) {
		this.dongle = dongle;
	}

	public String getIoTDevice() {
		return ioTDevice;
	}

	public void setIoTDevice(String ioTDevice) {
		this.ioTDevice = ioTDevice;
	}

	public String getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(String smartphone) {
		this.smartphone = smartphone;
	}

	public String getMobileFeaturePhone() {
		return mobileFeaturePhone;
	}

	public void setMobileFeaturePhone(String mobileFeaturePhone) {
		this.mobileFeaturePhone = mobileFeaturePhone;
	}

	/*
	 * public String getNa() { return na; }
	 * 
	 * public void setNa(String na) { this.na = na; }
	 */
	public String getTablet() {
		return tablet;
	}

	public void setTablet(String tablet) {
		this.tablet = tablet;
	}

	public String getConnectedComputer() {
		return connectedComputer;
	}

	public void setConnectedComputer(String connectedComputer) {
		this.connectedComputer = connectedComputer;
	}

	public String getPortableIncludePDA() {
		return portableIncludePDA;
	}

	public void setPortableIncludePDA(String portableIncludePDA) {
		this.portableIncludePDA = portableIncludePDA;
	}

	public String geteBook() {
		return eBook;
	}

	public void seteBook(String eBook) {
		this.eBook = eBook;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegisteredDeviceRawList [date=");
		builder.append(date);
		builder.append(", notKnown=");
		builder.append(notKnown);
		builder.append(", wearable=");
		builder.append(wearable);
		builder.append(", wlanRouter=");
		builder.append(wlanRouter);
		builder.append(", handhled=");
		builder.append(handhled);
		builder.append(", modem=");
		builder.append(modem);
		builder.append(", vehicle=");
		builder.append(vehicle);
		builder.append(", dongle=");
		builder.append(dongle);
		builder.append(", ioTDevice=");
		builder.append(ioTDevice);
		builder.append(", smartphone=");
		builder.append(smartphone);
		builder.append(", mobileFeaturePhone=");
		builder.append(mobileFeaturePhone);
		/*
		 * builder.append(", na="); builder.append(na);
		 */
		builder.append(", tablet=");
		builder.append(tablet);
		builder.append(", connectedComputer=");
		builder.append(connectedComputer);
		builder.append(", portableIncludePDA=");
		builder.append(portableIncludePDA);
		builder.append(", eBook=");
		builder.append(eBook);
		builder.append(", module=");
		builder.append(module);
		builder.append("]");
		return builder.toString();
	}
	
}
