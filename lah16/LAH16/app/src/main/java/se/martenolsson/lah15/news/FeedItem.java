package se.martenolsson.lah15.news;

public class FeedItem {
    private String mId;
    private String image;
    private String title;
    private String body;


    public String getMid() {
        return mId;
    }
    public void setMid(String string) {
        this.mId = string;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String string) {
        this.image = string;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String string) {
        this.title = string;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String string) {
        this.body = string;
    }

}
