package common.entity;

import java.io.Serializable;

/**
 * infoSystem is all the information systems of the college.
 * A data structure for client-server communication.
 * @version 1.0 - 01/2020
 * @author Group-10: Idan Abergel, Eden Schwartz, Ira Goor, Hen Hess, Yuda Hatam
 */
public class InfoSystem implements Serializable{

	private static final long serialVersionUID = 1L;
	String SystemID;
	String UserName;
	

	/**
	 * InfoSystem constructor, defined by Database fields.
	 * @param systemID
	 * @param userName
	 */
	public InfoSystem(String systemID, String userName) {		
		SystemID = systemID;
		UserName = userName;
	}

	/**
	 * @return the systemID
	 */
	public String getSystemID() {
		return SystemID;
	}

	/**
	 * @param systemID the systemID to set
	 */
	public void setSystemID(String systemID) {
		SystemID = systemID;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return UserName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}

}
