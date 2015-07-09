package gaongil.dto.cloud_refactoring.client;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * Created by yoon on 15. 7. 10..
 */
public class PlainText implements ClientDTO {

    private String text;

    public PlainText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "PlainText{" +
                "text='" + text + '\'' +
                '}';
    }
}