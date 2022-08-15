package users;

import java.util.Date;

public class Ban {
    public BanType type;
    public Date end;
    public String target;
    public String source;

    public Ban(BanType type, String target, String source, Date end) {
        this.type = type;
        this.target = target;
        this.source = source;
        this.end = end;
    }

    public Boolean isStillOngoingBan() {
        //Gets today's date
        Date today = new Date();

        //If the ban end is still in the future then the ban is still ongoing
        return today.before(this.end);
    }
}
