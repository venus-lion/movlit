package movlit.be.movie.domain;

public enum MovieRole {

    DIRECTOR("D"), CAST("C");

    private final String value;

    MovieRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
