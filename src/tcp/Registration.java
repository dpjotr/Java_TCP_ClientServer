package tcp;

import java.io.Serializable;
import java.util.Date;

public class Registration implements Serializable {

    private Date date;		// Дата регистрации
    private String user;	// Идентификатор пользователя

    public Registration(Date date, String user) throws Exception {
        if (date == null || user == null || user.trim().isEmpty()) {
            throw new Exception ("Invalid parameters of registration");
        }
        this.date = date;
        this.user = user;
    }
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
