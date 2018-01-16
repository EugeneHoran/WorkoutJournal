package eh.workout.journal.com.workoutjournal.db.entinty;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "exercise",
        foreignKeys = {@ForeignKey(entity = ExerciseCategory.class,
                parentColumns = "id",
                childColumns = "category")}
)
public class Exercise implements Comparable<Exercise> {
    @Override
    public int compareTo(@NonNull Exercise o) {
        return this.getName().compareTo(o.getName());
    }

    @PrimaryKey
    @NonNull
    private String id;
    @SerializedName("exercise_equipment_id")
    private int exerciseEquipmentId;
    private String description;
    private String name;
    @SerializedName("name_original")
    private String nameOriginal;
    private String uuid;
    private String category;
    private String categoryName;
    private List<String> muscles;
    @SerializedName("muscles_secondary")
    private List<String> musclesSecondary;
    private List<String> equipment;
    private Long timestampRecent;
    @Ignore
    private boolean isShownInRecent = false;
    @Ignore
    private boolean selected = false;
    private boolean recent;


    @Ignore
    public String getEquipmentString() {
        StringBuilder item = new StringBuilder();
        for (String s : equipment) {
            item.append(s).append(",");
        }
        return item.toString();
    }

    public Exercise() {
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public void setId(String id) {
        this.id = id;
    }

    public int getExerciseEquipmentId() {
        return exerciseEquipmentId;
    }

    public void setExerciseEquipmentId(int exerciseEquipmentId) {
        this.exerciseEquipmentId = exerciseEquipmentId;
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


    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
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

    public Long getTimestampRecent() {
        return timestampRecent;
    }

    public void setTimestampRecent(Long timestampRecent) {
        this.timestampRecent = timestampRecent;
    }

    public boolean isRecent() {
        return recent;
    }

    public void setRecent(boolean recent) {
        this.recent = recent;
    }

    @Ignore
    public boolean getIsShownInRecent() {
        return isShownInRecent;
    }

    @Ignore
    public void setShownInRecent(boolean shownInRecent) {
        isShownInRecent = shownInRecent;
    }

    @Ignore
    public boolean isSelected() {
        return selected;
    }

    @Ignore
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
