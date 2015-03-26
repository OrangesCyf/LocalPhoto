package com.example.photopicker.models;

import java.io.Serializable;
import java.util.List;

/**    
 */
public class PhotoList implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8299719063159793280L;
	private List<PhotoInfo> list;

	
	public PhotoList() {
    }

    public PhotoList(List<PhotoInfo> list) {
        this.list = list;
    }

    public List<PhotoInfo> getList() {
		return list;
	}

	public void setList(List<PhotoInfo> list) {
		this.list = list;
	}
	
}
