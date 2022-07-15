package nl.knaw.dans.lib.dataverse.model.dataverse;

public class DataverseTheme {
    int id;
    String logo;
    String logoBackgroundColor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoBackgroundColor() {
        return logoBackgroundColor;
    }

    public void setLogoBackgroundColor(String logoBackgroundColor) {
        this.logoBackgroundColor = logoBackgroundColor;
    }
}
