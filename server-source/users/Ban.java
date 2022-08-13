package users;

import java.util.Date;

public class Ban {
    public BanType type;
    public Date begin;
    public Date end;
    public String target;
    public String source;

    public Ban(BanType type, String target, String source, Date begin, Date end) {
        //We assure here that the end date is after the begin date.
        //Checks will be applied to the Ban command
        this.type = type;
        this.target = target;
        this.source = source;
        this.begin = begin;
        this.end = end;
    }

    public Boolean isStillOngoingBan() {
        //Gets today's date
        Date today = new Date();

        //If the ban end is still in the future then the ban is still ongoing
        return today.before(this.end);
    }
}
