package users;

public enum Privilege {
    MAINTAINER(4),
    ADMINISTRATOR(3),
    MODERATOR(2),
    CONTRIBUTOR(1),
    NONE(0);

    public final int level;

    private Privilege(int level) {
        this.level = level;
    }
}
