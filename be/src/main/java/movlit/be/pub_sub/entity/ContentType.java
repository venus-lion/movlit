package movlit.be.pub_sub.entity;

public enum ContentType {
    MOVIE("M"),
    BOOK("B");

    private final String value;

    ContentType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
