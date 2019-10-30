package io.syndesis.extension.twitter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.camel.util.ObjectHelper;

import twitter4j.JSONObject;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.User;

public class TweetMedia {
	
	private static final String twitterID = "twitterID";
	private static final String twitterName = "twitterName";
	private static final String twitterScreenName = "twitterScreenName";
	private static final String tweetCreatedAt = "tweetCreatedAt";
	private static final String url = "url";

	private User user;
	private Date createdAt;
	private MediaEntity[] mediaEntities;
	
	public TweetMedia(User user, Date timestamp, MediaEntity[] mediaEntities) {
		this.user = user;
		this.createdAt = timestamp;
		this.mediaEntities = mediaEntities;
	}
	
	public TweetMedia(Status status) {
		if (!ObjectHelper.isEmpty(status)) {
			this.user = status.getUser();
			this.createdAt = status.getCreatedAt();
			this.mediaEntities = status.getMediaEntities();
		}
	}

	/**
	 * Builds valid JSON 
	 * 
	 * <code>
	 * 
	 * </code>
	 * 
	 * 
	 * @return
	 */
	public JSONObject toJSON() {
		JSONObject json = null;
		
		if (ObjectHelper.isNotEmpty(this.user) &&  ObjectHelper.isNotEmpty(this.createdAt) && ObjectHelper.isNotEmpty(mediaEntities)) {
			json = new JSONObject();
			json.put(twitterID, this.user.getId());
			json.put(twitterName, this.user.getName());
			json.put(twitterScreenName, this.user.getScreenName());
			
			json.put(tweetCreatedAt, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this.createdAt));
			
			
			for (MediaEntity mediaEntity : mediaEntities) {
				String mediaURL = mediaEntity.getMediaURL();
				String mediaHTTPSurl = mediaEntity.getMediaURLHttps();
				String fallback = mediaEntity.getDisplayURL();
				 
				
				if (ObjectHelper.isNotEmpty(mediaURL)) {
					json.append(url, mediaURL);
				} else if (ObjectHelper.isNotEmpty(mediaHTTPSurl)) {
					json.append(url, mediaHTTPSurl);
				} else if (ObjectHelper.isNotEmpty(fallback)) {
					json.append(url, fallback);
				} else {
					json.append(url, "https://quarkus.io/assets/images/quarkus_logo_horizontal_rgb_reverse.svg");
				}
			}
		}
		return json;
	}
	
	@Override
	public String toString() {
		JSONObject json = toJSON();
		return ObjectHelper.isNotEmpty(json) ? json.toString() : null;
	}
}
