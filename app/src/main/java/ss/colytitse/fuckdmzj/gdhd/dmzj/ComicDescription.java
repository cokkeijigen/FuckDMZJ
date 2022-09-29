package ss.colytitse.fuckdmzj.gdhd.dmzj;

import java.util.List;

public class ComicDescription {

    private String id;
    private String title;
    private String subtitle;
    private String types;
    private String zone;
    private String status;
    private String last_update_chapter_name;
    private String last_updatetime;
    private String cover;
    private String authors;
    private String description;
    private String first_letter;
    private String direction;
    private String islong;
    private String copyright;
    private List<Chapter> ChapterDescription;

    public void setChapterDescription(List<Chapter> chapterDescription) {
        ChapterDescription = chapterDescription;
    }

    public List<Chapter> getChapterDescription() {
        return ChapterDescription;
    }

    public ComicDescription() {
        this.id = "";
        this.title = "";
        this.subtitle = "";
        this.types = "";
        this.zone = "";
        this.status = "";
        this.last_update_chapter_name = "";
        this.last_updatetime = "";
        this.cover = "";
        this.authors = "";
        this.description = "";
        this.first_letter = "";
        this.direction = "";
        this.islong = "";
        this.copyright = "";
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTypes() {
        return types;
    }

    public String getZone() {
        return zone;
    }

    public String getStatus() {
        return status;
    }

    public String getLastUpdateChapterName() {
        return last_update_chapter_name;
    }

    public String getLastUpdatetime() {
        return last_updatetime;
    }

    public String getCover() {
        return cover;
    }

    public String getAuthors() {
        return authors;
    }

    public String getDescription() {
        return description;
    }

    public String getFirstLetter() {
        return first_letter;
    }

    public String getDirection() {
        return direction;
    }

    public String getIslong() {
        return islong;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setLastUpdateChapterName(String last_update_chapter_name) {
        this.last_update_chapter_name = last_update_chapter_name;
    }

    public void setLastUpdatetime(String last_updatetime) {
        this.last_updatetime = last_updatetime;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFirstLetter(String first_letter) {
        this.first_letter = first_letter;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setIslong(String islong) {
        this.islong = islong;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }
}
