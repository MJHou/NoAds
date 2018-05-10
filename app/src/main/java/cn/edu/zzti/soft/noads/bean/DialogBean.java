package cn.edu.zzti.soft.noads.bean;

/**
 * DialogBean 对应的bean
 */
public class DialogBean {
    private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DialogBean(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
