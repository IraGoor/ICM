package server.controllers;

/**
 * Database Connections details object.
 * Used for supporting more than one connection options. (for App Server GUI)
 */
public class DBDetails {

	public String DB_HOST = "remotemysql.com";
	public String DB_SCHEME = "yRBHdnFuc9";
	public String DB_USERNAME = "yRBHdnFuc9";
	public String DB_PORT = "3306";
	public String DB_PASSWORD = "QOMMWb8Jo6";

	/**
	 * DBDetails Constructor.
	 * @param dB_HOST
	 * @param dB_SCHEME
	 * @param dB_USERNAME
	 * @param dB_PASSWORD
	 * @param dB_PORT
	 */
	public DBDetails(String dB_HOST, String dB_SCHEME, String dB_USERNAME,  String dB_PASSWORD ,String dB_PORT) {
		DB_HOST = dB_HOST;
		DB_SCHEME = dB_SCHEME;
		DB_USERNAME = dB_USERNAME;
		DB_PORT = dB_PORT;
		DB_PASSWORD = dB_PASSWORD;
	}

	public String getDB_HOST() {
		return DB_HOST;
	}

	public void setDB_HOST(String dB_HOST) {
		DB_HOST = dB_HOST;
	}

	public String getDB_SCHEME() {
		return DB_SCHEME;
	}

	public void setDB_SCHEME(String dB_SCHEME) {
		DB_SCHEME = dB_SCHEME;
	}

	public String getDB_USERNAME() {
		return DB_USERNAME;
	}

	public void setDB_USERNAME(String dB_USERNAME) {
		DB_USERNAME = dB_USERNAME;
	}

	public String getDB_PORT() {
		return DB_PORT;
	}

	public void setDB_PORT(String dB_PORT) {
		DB_PORT = dB_PORT;
	}

	public String getDB_PASSWORD() {
		return DB_PASSWORD;
	}

	public void setDB_PASSWORD(String dB_PASSWORD) {
		DB_PASSWORD = dB_PASSWORD;
	}

}// class dbDetails