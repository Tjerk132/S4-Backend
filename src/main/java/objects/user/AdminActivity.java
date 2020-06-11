package objects.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import enums.AdminActivityStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@DatabaseTable(tableName = "admin-activity-log")
public class AdminActivity {

    public AdminActivity() {
        // ORMLite needs a no-arg constructor
    }

    public AdminActivity(int editorId, RequestMethod method, AdminActivityStatus status, String type, int itemId) {
        this.editorId = editorId;
        this.method = method;
        this.status = status;
        this.type = type;
        this.itemId = itemId;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                        .format(new Timestamp(System.currentTimeMillis()));
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int editorId;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private RequestMethod method;

    @DatabaseField
    private String type;

    @DatabaseField
    private int itemId;

    @DatabaseField
    private String date;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private AdminActivityStatus status;

}
