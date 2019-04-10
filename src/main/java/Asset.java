import java.util.List;

/**
 * Created by YANG205 on 2019/1/23.
 */
public class Asset {
    private String title;
    private String fileName;
    private String color;
    private String colorCode;
    private List<String> modelYear;
    private String location;
    private List<String> category;
    private String angle;
    private List<String> theme;
    private String description;
    private String toolkit;
    private String rights;
    private String owner;
    private String language;
    private String resolution;
    private List<String> models;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public List<String> getModelYear() {
        return modelYear;
    }

    public void setModelYear(List<String> modelYear) {
        this.modelYear = modelYear;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }

    public String getAngle() {
        return angle;
    }

    public void setAngle(String angle) {
        this.angle = angle;
    }

    public List<String> getTheme() {
        return theme;
    }

    public void setTheme(List<String> theme) {
        this.theme = theme;
    }

    public String getToolkit() {
        return toolkit;
    }

    public void setToolkit(String toolkit) {
        this.toolkit = toolkit;
    }

    public String getRights() {
        return rights;
    }

    public void setRights(String rights) {
        this.rights = rights;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public List<String> getModels() {
        return models;
    }

    public void setModels(List<String> models) {
        this.models = models;
    }

    @Override
    public String toString() {
        return "Asset{" +
                "title='" + title + '\'' +
                ", fileName='" + fileName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                ", modelYear=" + modelYear +
                ", location='" + location + '\'' +
                ", category=" + category +
                ", angle='" + angle + '\'' +
                ", theme=" + theme +
                ", description='" + description + '\'' +
                ", toolkit='" + toolkit + '\'' +
                ", rights='" + rights + '\'' +
                ", owner='" + owner + '\'' +
                ", language='" + language + '\'' +
                ", resolution='" + resolution + '\'' +
                ", models=" + models +
                '}';
    }
}
