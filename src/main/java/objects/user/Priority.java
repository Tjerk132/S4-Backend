package objects.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import enums.Role;

@DatabaseTable(tableName = "roles")
public class Priority {

    public Priority() {
        // ORMLite needs a no-arg constructor
    }

    public Priority(Integer userId, Role role) {
        this.userId = userId;
        this.role = role;
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private Integer userId;

    @DatabaseField(dataType = DataType.ENUM_STRING, unknownEnumName = "USER")
    private Role role;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

}
