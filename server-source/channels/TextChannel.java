package channels;

import java.util.ArrayList;
import java.util.List;

public class TextChannel extends Channel {

    public List<Message> messages;

	public TextChannel(String name, String description, Boolean ageRestricted) {
        super(name, description, ageRestricted);

        this.messages = new ArrayList<Message>();
    }
}