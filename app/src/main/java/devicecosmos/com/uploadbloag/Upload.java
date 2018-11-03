package devicecosmos.com.uploadbloag;

public class Upload {
    private String Title,Date,Time,Tags,Details,ImageURL,YouTubelink,BlogID;

    public Upload() {
    }

    public Upload(String title, String date, String time,String tags, String details,
                  String imageURL,String youTubelink,String blogID) {
        Title = title;
        Date = date;
        Time = time;
        Tags = tags;
        Details = details;
        ImageURL = imageURL;
        YouTubelink=youTubelink;
        BlogID = blogID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }


    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }

    public String getYouTubelink() {
        return YouTubelink;
    }

    public void setYouTubelink(String youTubelink) {
        YouTubelink = youTubelink;
    }

    public String getBlogID() {
        return BlogID;
    }

    public void setBlogID(String blogID) {
        BlogID = blogID;
    }
}
