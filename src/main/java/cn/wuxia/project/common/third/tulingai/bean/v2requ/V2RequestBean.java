package cn.wuxia.project.common.third.tulingai.bean.v2requ;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class V2RequestBean {

	/**
	 * 输入的信息
	 */
	@NotNull
	Perception perception;
	/**
	 * 用户参数
	 */
	@NotNull
	UserInfo userInfo;

	public V2RequestBean(String appkey, String userId, String text) {
		this.perception = new Perception(text);
		this.userInfo = new UserInfo(appkey, userId);
	}

	public V2RequestBean(String appkey, String userId, String text, String city) {
		new V2RequestBean(appkey, userId, text);
		this.perception.selfInfo = new SelfInfo(city);
	}

	public class Perception {
		/**
		 * 文本信息
		 */
		@NotNull
		InputText inputText;

		/**
		 * 地理位置信息
		 */
		SelfInfo selfInfo;

		public Perception(String text) {
			this.inputText = new InputText(text);
		}

		public InputText getInputText() {
			return inputText;
		}

		public void setInputText(InputText inputText) {
			this.inputText = inputText;
		}

	}

	public class InputText {
		/**
		 * 直接输入文本
		 */
		@NotBlank
		@Length(min = 1, max = 128)
		String text;

		public InputText(String text) {
			this.text = text;
		}

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

	}

	public class SelfInfo {
		Location location;

		public SelfInfo(String city) {
			this.location = new Location(city);
		}

		public Location getLocation() {
			return location;
		}

		public void setLocation(Location location) {
			this.location = location;
		}

	}

	public class Location {
		/**
		 * 城市：北京
		 */
		String city;
		/**
		 * 经度 119.239293
		 */
		String longitude;
		/**
		 * 纬度 39.45492
		 */
		String latitude;
		/**
		 * 最近的地理位置：上地环岛南
		 */
		String nearest_poi_name;
		/**
		 * 省份：北京
		 */
		String province;

		/**
		 * 街道：信息路
		 */
		String street;

		public Location(String city) {
			this.city = city;
		}

		public Location() {
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getLongitude() {
			return longitude;
		}

		public void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public String getLatitude() {
			return latitude;
		}

		public void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public String getNearest_poi_name() {
			return nearest_poi_name;
		}

		public void setNearest_poi_name(String nearest_poi_name) {
			this.nearest_poi_name = nearest_poi_name;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

	}

	public class UserInfo {
		String apiKey;
		String userId;

		public UserInfo(String apiKey, String userId) {
			this.apiKey = apiKey;
			this.userId = userId;
		}

		public String getApiKey() {
			return apiKey;
		}

		public void setApiKey(String apiKey) {
			this.apiKey = apiKey;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

	}

	public Perception getPerception() {
		return perception;
	}

	public void setPerception(Perception perception) {
		this.perception = perception;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

}
