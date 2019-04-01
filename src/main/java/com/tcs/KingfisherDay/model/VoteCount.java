package com.tcs.KingfisherDay.model;

public class VoteCount {

	private String itemId;
	private int likeCount;
	private int dislikeCount;

	public VoteCount() {

	}

	public VoteCount(String itemId, int likeCount, int dislikeCount) {
		super();
		this.itemId = itemId;
		this.likeCount = likeCount;
		this.dislikeCount = dislikeCount;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public int getLikeCount() {
		return likeCount;
	}

	public void setLikeCount(int likeCount) {
		this.likeCount = likeCount;
	}

	public int getDislikeCount() {
		return dislikeCount;
	}

	public void setDislikeCount(int dislikeCount) {
		this.dislikeCount = dislikeCount;
	}
}
