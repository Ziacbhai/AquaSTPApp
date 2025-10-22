package Models;


public class DashboardItem {
    private String title;
    private String description;
    private int iconResId;
    private int gradientResId;

    public DashboardItem(String title, String description, int iconResId, int gradientResId) {
        this.title = title;
        this.description = description;
        this.iconResId = iconResId;
        this.gradientResId = gradientResId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getIconResId() {
        return iconResId;
    }

    public int getGradientResId() {
        return gradientResId;
    }
}
