package cri.free.FitnessInstructor.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.springframework.stereotype.Component;

@Component
public class JsonParserUtil {
    private Gson gson = new Gson();
    public String extractTextFromJsonResponse(String jsonResponse) {
        try {
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
            JsonArray choicesArray = jsonObject.getAsJsonArray("choices");
            if (choicesArray != null && !choicesArray.isJsonNull()) {
                JsonObject choiceObject = choicesArray.get(0).getAsJsonObject();
                if (choiceObject != null && !choiceObject.isJsonNull()) {
                    JsonObject messageObject = choiceObject.getAsJsonObject("message");
                    if (messageObject != null && !messageObject.isJsonNull()) {
                        JsonElement textElement = messageObject.getAsJsonObject().get("content");
                        if (textElement != null && !textElement.isJsonNull()) {
                            String text = textElement.getAsString();
                            return cleanGeneratedText(text);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error: Unable to extract text from JSON response: " + jsonResponse);
        }
        return "";
    }
    private String cleanGeneratedText(String text) {
        // Удаление символов новой строки и других нежелательных символов
        text = text.replaceAll("\\n", " ").trim();
        // Замена любыми другими символами или последовательностью, которую нужно удалить
        text = text.replaceAll("reply_markup=markup", "").trim();
        return text;
    }
}

