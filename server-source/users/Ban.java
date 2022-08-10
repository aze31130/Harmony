package users;

import java.util.Date;

public class Ban {
    public BanType type;
    public Date begin;
    public Date end;
    public String target;
    public String source;

    public Ban(BanType type, String target, String source, Date begin, Date end) {
        this.type = type;
        this.target = target;
        this.source = source;
        this.begin = begin;
        this.end = end;
    }
}
