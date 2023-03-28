package cse340.finalproject;


// A 'generic' public class used for storing and retrieving multiple data of any type
public final class MultipleData {
    private Object data1;
    private Object data2;

    public MultipleData(Object data1, Object data2) {
        this.data1 = data1;
        this.data2 = data2;
    }

    public Object getData1() {
        return data1;
    }

    public Object getData2() {
        return data2;
    }
}
