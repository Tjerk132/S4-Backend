package enums;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Category {

    GARDEN_AND_TOOLS,
    KITCHEN,
    SPORTS,
    HOUSEHOLD_AND_LIVING,
    ELECTRONICS;

    private static Map<String, Category> namesMap = new HashMap<>(6);

    static {
        namesMap.put("Garden and tools", GARDEN_AND_TOOLS);
        namesMap.put("Kitchen", KITCHEN);
        namesMap.put("Sports", SPORTS);
        namesMap.put("Household and living", HOUSEHOLD_AND_LIVING);
        namesMap.put("Electronics", ELECTRONICS);
    }

    @JsonCreator
    public static Category fromJson(@JsonProperty("name") String name) {
        return namesMap.get(name);
    }

    @JsonValue
    public String getCategory() {
        return this.name();
    }
}
