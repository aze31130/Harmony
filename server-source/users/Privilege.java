package users;

public enum Privilege {
    NONE(0),
    CONTRIBUTOR(1),
    MODERATOR(2),
    ADMINISTRATOR(3),
    MAINTAINER(4);

    public final int level;

    private Privilege(int level) {
        this.level = level;
    }
}
