package objects.user;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import enums.AdminActivityStatus;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMethod;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

@DatabaseTable(tableName = "admin-activity-log")
public class AdminActivity {

    public AdminActivity() {
        // ORMLite needs a no-arg constructor
    }

    public AdminActivity(int editorId, HttpMethod method, AdminActivityStatus status, String type, String exception, int itemId) {
        this.editorId = editorId;
        this.method = method;
        this.status = status;
        this.type = type;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Timestamp(System.currentTimeMillis()));

        if(exception != null) {
            this.exception = exception;
        }
        else {
            this.itemId = itemId;
            this.exception = "none";
        }
    }

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private int editorId;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private HttpMethod method;

    @DatabaseField
    private String type;

    @DatabaseField
    private int itemId;

    @DatabaseField
    private String date;

    @DatabaseField
    private String exception;

    @DatabaseField(dataType = DataType.ENUM_STRING)
    private AdminActivityStatus status;

    public int getId() {
        return id;
    }

    public int getEditorId() {
        return editorId;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getType() {
        return type;
    }

    public int getItemId() {
        return itemId;
    }

    public String getDate() {
        return date;
    }

    public String getException() {
        return exception;
    }

    public AdminActivityStatus getStatus() {
        return status;
    }
}
