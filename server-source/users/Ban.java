package users;

import java.util.Date;

public class Ban {
    public int accountId;
    public String ip;
    public BanType type;
    public Date end;
    public int source;

    public Ban(BanType type, int accountId, String ip, int source, Date end) {
        this.type = type;
        this.accountId = accountId;
        this.ip = ip;
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
