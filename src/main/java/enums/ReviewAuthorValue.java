package enums;

import com.auth0.jwt.internal.com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ReviewAuthorValue {

    NORMAL_REVIEWER,
    VALUED_REVIEWER;

    private static Map<String, ReviewAuthorValue> namesMap = new HashMap<>(2);

    static {
        namesMap.put("Normal reviewer", NORMAL_REVIEWER);
        namesMap.put("Valued reviewer", VALUED_REVIEWER);
    }

    @JsonCreator
    public static ReviewAuthorValue fromJson(@JsonProperty("name") String name) {
        return namesMap.get(name);
    }

    @JsonValue
    public String getReviewAuthorValue() {
        return this.name();
    }
}
