package patrushevoleg.ru.lab2;

import android.text.format.DateFormat;

public class Record {

    public enum Priority {MAX, LOW, NORMAL}

    private String name;
    private Priority priority;
    private String description;
    private DateFormat date;
    private boolean isDone;

    public DateFormat getDate() {
        return date;
    }

    public void setDate(DateFormat date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public Record copy(){
        Record copy = new Record();
        copy.setPriority(priority);
        copy.setName(name);
        copy.setDate(date);
        copy.setDescription(description);
        copy.setDone(isDone);
        return copy;
    }
}