package common.entity;

import java.io.File;
import java.io.Serializable;
import java.util.List;

/**
 * MyFile represents the File information.
 * fileName, original path, Description , size, byte-array
 */
public class MyFile implements Serializable {

    private String Description = null;
    private String fileName = null;
    private String filePath = null;
    private int size = 0;
    public byte[] mybytearray;

    /**
     * @param size
     */
    public void initArray(int size) {
        mybytearray = new byte[size];
    }

    /**
     * constructor of MyFile
     *
     * @param filepath
     * @param fileName
     */
    public MyFile(String filepath, String fileName) {
        this.fileName = fileName;
        this.filePath = filepath;
    }

    /**
     * @return name file
     */
    public String getFileName() {
        return fileName;
    }

	/**
	 * set new file name
	 * @param fileName
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * 
	 * @return path of the file
	 */
	public String getFilepath() {
		return filePath;
	}
	public String getFilepathInServer(String namefile) {
		return System.getProperty("user.dir") + "\\serverFiles\\"+namefile;
	}
	/**
	 * 
	 * @param filePath
	 */
	public void setFilepath(String filePath) {
		this.filePath = filePath;
	}

	/**
	 * 
	 * @return size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * 
	 * @param size
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * 
	 * @return mybytearray
	 */
	public byte[] getMybytearray() {
		return mybytearray;
	}

	/**
	 * 
	 * @param i
	 * @return byte_array
	 */
	public byte getMybytearray(int i) {
		return mybytearray[i];
	}

	/**
	 * 
	 * @param mybytearray
	 */
	public void setMybytearray(byte[] mybytearray) {

		for (int i = 0; i < mybytearray.length; i++)
			this.mybytearray[i] = mybytearray[i];
	}

	/**
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}

	/**
	 * 
	 * @param description
	 */
	public void setDescription(String description) {
		Description = description;
	}

}
