public class Data {
    public Data(int id, String name, String subject, String address, String data) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.address = address;
        this.date = data;
    }

    final private int id;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSubject() {
        return subject;
    }

    public String getAddress() {
        return address;
    }

    public String getData() {
        return date;
    }

    final private String name;
    final private String subject;
    final private String address;
    final private String date;
}
