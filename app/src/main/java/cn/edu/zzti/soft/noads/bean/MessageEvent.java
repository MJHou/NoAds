package cn.edu.zzti.soft.noads.bean;

/**
 * @desc EventBus起始传递的对象，和目标接受的对象
 */

public class MessageEvent {
    //路径
    private String path;
    //host
    private String host;
    //EventBus接受的对象根据此值，进行相对于的逻辑运算
    private int type;

    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public MessageEvent(String host, int type) {
        this.host = host;
        this.type = type;
    }
    public MessageEvent(Object obj, int type) {
        this.obj = obj;
        this.type = type;
    }

    public MessageEvent(String host,String path, int type) {
        this.host = host;
        this.path = path;
        this.type = type;
    }

    public MessageEvent() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
