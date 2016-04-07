package it.kdevgroup.incaneva;

import java.util.List;

/**
 * Created by andrea on 07/04/16.
 */
public class BlogEvent {

    public static final String KEY_blogname = "blogname";
    public static final String KEY_blogname_slug = "blogname_slug";
    public static final String KEY_id = "ID";
    public static final String KEY_post_title = "post_title";
    public static final String KEY_post_content = "post_content";
    public static final String KEY_category_name = "category_name";
    public static final String KEY_category_link = "category_link";
    public static final String KEY_event_type = "event_type";
    public static final String KEY_post_thumbnail = "post_thumbnail";
    public static final String KEY_evcal_srow = "evcal_srow";
    public static final String KEY_evcal_erow = "evcal_erow";
    public static final String KEY_evcal_event_color = "evcal_event_color";


    private int ID;
    private String blogName;
    private String blogNameSlug;
    private String postTitle;
    private String postContent;
    private String categoryName;
    //    private String categoryLink;
    private List<String> eventType;
    private String imageLink;
    private long startTime;
    private long endTime;
    private String eventColor;

    public BlogEvent() {
    }

    public BlogEvent(int ID,
                     String blogName,
                     String blogNameSlug,
                     String postTitle,
                     String postContent,
                     String categoryName,
                     List<String> eventType,
                     String imageLink,
                     long startTime,
                     long endTime,
                     String eventColor) {
        this.ID = ID;
        this.blogName = blogName;
        this.blogNameSlug = blogNameSlug;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.categoryName = categoryName;
        this.eventType = eventType;
        this.imageLink = imageLink;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventColor = eventColor;
    }


    public int getID() {
        return ID;
    }

    public String getBlogName() {
        return blogName;
    }

    public String getBlogNameSlug() {
        return blogNameSlug;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getEventType() {
        return eventType;
    }

    public String getImageLink() {
        return imageLink;
    }

    public long getStartTime() {
        return startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getEventColor() {
        return eventColor;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setBlogName(String blogName) {
        this.blogName = blogName;
    }

    public void setBlogNameSlug(String blogNameSlug) {
        this.blogNameSlug = blogNameSlug;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setEventType(List<String> eventType) {
        this.eventType = eventType;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setEventColor(String eventColor) {
        this.eventColor = eventColor;
    }
}
