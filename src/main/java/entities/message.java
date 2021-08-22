package entities;

public class message {
    private int sid;
    private int rid;
    private String text;
    private String type;
    public message(){}

    public message(int id, String text) {
        this.sid = id;
        this.text = text;
    }

    public message(int sid, int rid, String text, String type) {
        this.sid = sid;
        this.rid = rid;
        this.text = text;
        this.type = type;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
