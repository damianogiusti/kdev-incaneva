package it.kdevgroup.incaneva;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrea on 07/04/16.
 */
public class BlogEvent implements Parcelable {

    private static final String TAG = "BlogEvent";

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
    public static final String KEY_post_month_numerical = "post_month_numerical";
    public static final String KEY_post_day_numerical = "post_day_numerical";
    public static final String KEY_post_time_hour = "post_time_hour";
    public static final String KEY_evcal_week_day = "evcal_week_day";
    public static final String KEY_evcal_start_time_min = "evcal_start_time_min";
    public static final String KEY_post_year = "post_year";


    private int ID;
    private String blogName;
    private String blogNameSlug;
    private String postTitle;
    private Spanned postContent;
    private String categoryName;
    private String categoryLink;
    private List<String> eventType;
    private String imageLink;
    private long startTime;
    private long endTime;
    private String eventColor;
    private String eventMonth;
    private String eventDay;
    private String eventHour;
    private String dayofWeek;
    private String eventMinute;
    private String eventYear;

    public BlogEvent() {
        eventType = new ArrayList<>();
    }

    public BlogEvent(int ID,
                     String blogName,
                     String blogNameSlug,
                     String postTitle,
                     Spanned postContent,
                     String categoryName,
                     String categoryLink,
                     List<String> eventType,
                     String imageLink,
                     long startTime,
                     long endTime,
                     String eventColor,
                     String eventMonth,
                     String eventDay,
                     String eventHour,
                     String dayofWeek,
                     String eventMinute,
                     String eventYear) {
        this.ID = ID;
        this.blogName = blogName;
        this.blogNameSlug = blogNameSlug;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.categoryName = categoryName;
        this.categoryLink = categoryLink;
        this.eventType = eventType;
        this.imageLink = imageLink;
        this.startTime = startTime;
        this.endTime = endTime;
        this.eventColor = eventColor;
        this.eventMonth = eventMonth;
        this.eventDay = eventDay;
        this.eventHour = eventHour;
        this.dayofWeek = dayofWeek;
        this.eventMinute = eventMinute;
        this.eventYear = eventYear;
    }


    //metodi di get


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

    public Spanned getPostContent() {
        return postContent;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public List<String> getEventType() {
        return eventType;
    }

    public String getCategoryLink() {
        return categoryLink;
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

    public String getEventMonth() {
        return eventMonth;
    }

    public String getEventDay() {
        return eventDay;
    }

    public String getEventHour() {
        return eventHour;
    }

    public String getDayofWeek() {
        return dayofWeek;
    }

    public String getEventMinute() {
        return eventMinute;
    }

    public String getEventYear() {
        return eventYear;
    }

    //metodi di set

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setCategoryLink(String categoryLink) {
        this.categoryLink = categoryLink;
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

    public void setPostContent(Spanned postContent) {
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

    public void setEventMonth(String eventMonth) {
        this.eventMonth = eventMonth;
    }

    public void setEventDay(String eventDay) {
        this.eventDay = eventDay;
    }

    public void setEventHour(String eventHour) {
        this.eventHour = eventHour;
    }

    public void setDayofWeek(String dayofWeek) {
        this.dayofWeek = dayofWeek;
    }

    public void setEventMinute(String eventMinute) {
        this.eventMinute = eventMinute;
    }

    public void setEventYear(String eventYear) {
        this.eventYear = eventYear;
    }


    // PARTE PER LA PARCELLIZZAZIONE

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(blogName);
        dest.writeString(blogNameSlug);
        dest.writeString(postTitle);
        dest.writeString(Html.toHtml(postContent));
        dest.writeString(categoryName);
        dest.writeString(categoryLink);
        dest.writeStringList(eventType);
        dest.writeString(imageLink);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeString(eventColor);
        dest.writeString(eventMonth);
        dest.writeString(eventDay);
        dest.writeString(eventHour);
        dest.writeString(eventMinute);
        dest.writeString(dayofWeek);
        dest.writeString(eventYear);
    }

    public final static Parcelable.Creator<BlogEvent> CREATOR = new ClassLoaderCreator<BlogEvent>() {
        @Override
        public BlogEvent createFromParcel(Parcel source, ClassLoader loader) {
            return new BlogEvent(source);
        }

        @Override
        public BlogEvent createFromParcel(Parcel source) {
            return new BlogEvent(source);
        }

        @Override
        public BlogEvent[] newArray(int size) {
            return new BlogEvent[size];
        }
    };

    // costruttore che inizializza l'oggetto a partire da un Parcel salvato
    private BlogEvent(Parcel in) {
        ID = in.readInt();
        blogName = in.readString();
        blogNameSlug = in.readString();
        postTitle = in.readString();
        postContent = Html.fromHtml(in.readString());
        categoryName = in.readString();
        categoryLink = in.readString();
        eventType = new ArrayList<>();
        in.readStringList(eventType);
        imageLink = in.readString();
        startTime = in.readLong();
        endTime = in.readLong();
        eventColor = in.readString();
        eventMonth = in.readString();
        eventDay = in.readString();
        eventHour = in.readString();
        eventMinute = in.readString();
        dayofWeek = in.readString();
        eventYear = in.readString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof BlogEvent))
            return false;
        BlogEvent event = (BlogEvent) o;

        return ID == event.getID() &&
                this.blogName.equals(event.getBlogName()) &&
                this.blogNameSlug.equals(event.getBlogNameSlug()) &&
                this.postTitle.equals(event.getPostTitle()) &&
                this.postContent.equals(event.getPostContent()) &&
                this.categoryName.equals(event.getCategoryName()) &&
                this.categoryLink.equals(event.getCategoryLink()) &&
                this.eventType.size() == event.getEventType().size() &&
                this.imageLink.equals(event.getImageLink()) &&
                this.startTime == event.getStartTime() &&
                this.endTime == event.getEndTime() &&
                this.eventColor.equals(event.getEventColor())
                ;
    }
}
