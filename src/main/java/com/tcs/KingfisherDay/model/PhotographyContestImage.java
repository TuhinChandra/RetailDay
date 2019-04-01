package com.tcs.KingfisherDay.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PHOTOGRAPHY_CONTEST_IMAGE")
public class PhotographyContestImage {
	@Id
	@Column(name = "image_id", nullable = false)
	private int imageId;
	@Column(name = "owner", nullable = false)
	private String owner;
	@Column(name = "image_name", nullable = true)
	private String imageName;
	@Column(name = "content", nullable = false)
	private String content;

	public PhotographyContestImage() {

	}

	public PhotographyContestImage(int imageId, String owner, String imageName, String content) {
		super();
		this.imageId = imageId;
		this.owner=owner;
		this.imageName = imageName;
		this.content = content;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	

}