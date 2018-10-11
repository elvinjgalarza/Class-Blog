package guestbook;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

// create Guestbook class as the guestbook name or key

@Entity
public class Guestbook {
    @Id long id;
    String name;

    public Guestbook(String name) {
        this.name = name;
    }
}