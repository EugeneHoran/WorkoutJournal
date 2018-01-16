
package eh.workout.journal.com.workoutjournal.db.entinty;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Exercise {
    @SerializedName("id")
    private String id;
    @SerializedName("description")
    private String description;
    @SerializedName("name")
    private String name;
    @SerializedName("name_original")
    private String nameOriginal;
    @SerializedName("uuid")
    private String uuid;
    @SerializedName("category")
    private String category;
    @SerializedName("muscles")
    private List<String> muscles ;
    @SerializedName("muscles_secondary")
    private List<String> musclesSecondary ;
    @SerializedName("equipment")
    private List<String> equipment ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameOriginal() {
        return nameOriginal;
    }

    public void setNameOriginal(String nameOriginal) {
        this.nameOriginal = nameOriginal;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<String> getMuscles() {
        return muscles;
    }

    public void setMuscles(List<String> muscles) {
        this.muscles = muscles;
    }

    public List<String> getMusclesSecondary() {
        return musclesSecondary;
    }

    public void setMusclesSecondary(List<String> musclesSecondary) {
        this.musclesSecondary = musclesSecondary;
    }

    public List<String> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<String> equipment) {
        this.equipment = equipment;
    }

}
