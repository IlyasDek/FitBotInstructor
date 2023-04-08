
package cri.free.FitnessInstructor.services;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
import cri.free.FitnessInstructor.utils.JsonParserUtil;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class ChatGPTService {
    private static final String API_KEY = "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";
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private JsonParserUtil jsonParserUtil;
    @Autowired
    public ChatGPTService(JsonParserUtil jsonParserUtil) {
        this.jsonParserUtil = jsonParserUtil;
    }

    private OkHttpClient createCustomOkHttpClient() {
        return new OkHttpClient.Builder()
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .build();
    }
    private OkHttpClient client = createCustomOkHttpClient();


    private Gson gson = new Gson();


    public String getResponseFromChatGPTAPI(List<String> userAnswers) {
        String userAnswersText = String.format("Пол: %s\nОпыт: %s\nЦель: %s\nВозраст: %s\nДней в неделю: %s\nРост и вес: %s\nInjuries: %s", userAnswers.get(0),
                userAnswers.get(1), userAnswers.get(2), userAnswers.get(3), userAnswers.get(4),
                userAnswers.get(5), userAnswers.get(6));

        String pattern = "В каждую тренировку необходимо включить минимум одно упражнение на:\n" +
                "\n" +
                "переднюю поверхность бедра – квадрицепс;\n" +
                "заднюю поверхность бедра – ягодичные и бедренные бицепсы;\n" +
                "пресс;\n" +
                "жим;\n" +
                "тягу руками." +
                "Для квадрицепсов: выпады, приседания на двух и одной ноге, прыжки на платформу.\n" +
                "Для бицепсов бедер и ягодиц: наклоны, ягодичные мостики, становые тяги.\n" +
                "Жимы для трицепсов, плеч и груди: отжимания на брусьях и от пола, жимы на наклонной скамье, лежа и жимы над головой.\n" +
                "Тяговые упражнения для предплечья, бицепсов, мышц спины: тяга к животу, подтягивания на низкой перекладине прямым и обратным хватом.\n" +
                "Для мышц кора – стабилизаторов корпуса – поясницы и пресса: скручивания на фитболе, «велосипед», планки, в том числе боковые, «скалолаз», подъем ног в висе." +
                "Делайте каждое упражнение по 3-5 подходов. Таким образом за тренировку вы выполните 15-25 рабочих сетов.\n" +
                "\n" +
                "Большой объем на начальных этапах нередко приносит больше вреда, чем пользы, либо говорит о том, что вы тренируетесь неинтенсивно и тратите время занятий впустую."
                + "Для роста мышечной массы хорошо работает прием изменения количества повторов. Используйте это при самостоятельном составлении программы тренировок.\n" +
                "\n" +
                "Например:\n" +
                "\n" +
                "понедельник – большой вес и мало повторов (5-8);\n" +
                "среда – малый вес и много повторов (12-15);\n" +
                "пятница – средний вес и среднее количество повторов (8-12)." +
                "Малый диапазон повторов – от 1 до 5 – делает мышцы более плотными и развивает силу.\n" +
                "Средний диапазон – от 6 до 12 – в равной степени увеличивает массу и силу мышц.\n" +
                "Большой – 13 и больше – увеличивает объем и развивает силовую выносливость." +
                "Длительность интервалов отдыха зависит от количества повторов:\n" +
                "\n" +
                "после 1-3 повторов – отдыхайте 3-5 минут;\n" +
                "после 4-7 – 2-3 минуты;\n" +
                "после 8-12 – 1-2 минуты;\n" +
                "после 13 и более повторов – максимум 1 минуту." +
                "Пример программы домашней круговой тренировки для начинающих:\n" +
                "\n" +
                "ходьба выпадами – 20 повторов;\n" +
                "отжимания – 10 повторов;\n" +
                "приседания без отягощений – 20 повторов;\n" +
                "тяга гантелей в наклоне одной рукой – 10 повторов;\n" +
                "прыжки с расстановкой ног и подъемом рук – 30 повторов;\n" +
                "планка – 15 секунд.\n" +
                "Пример программы круговой тренировки в зале:\n" +
                "\n" +
                "выпады – 12 повторов;\n" +
                "приседания с гирей – 12 повторов;\n" +
                "жим гантелей лежа на наклонной скамье – 12 повторов;\n" +
                "подъем штанги на бицепс – 12 повторов;\n" +
                "разгибание рук на канатно-рукоятном блоке – 12 повторов;\n" +
                "подъем гантелей через стороны – 12 повторов;\n" +
                "подтягивания на тренажере – 12 повторов;\n" +
                "прыжки через скакалку – 1 минута.";

        String prompt = "Ответы, предоставленные пользователем:\n" + userAnswersText + "\nНаспиши план тренировок и диету для пользователя исходя из этих данных так," +
                " будто ты профессиональный диетолог и фитнес тренер, который состоавляет это для своих клиентов. " +
                "Напиши ее вкратце и только важную информацию, ограничься 1000 символами, вот шаблон как должно выглядеть: " + pattern;

        Request request = buildRequest(createRequestBody(prompt));

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String generatedText = handleSuccessfulRequest(response);
                return generatedText.trim();
            } else {
                handleErrorResponse(response);
                return "Error: Unable to generate personalized fitness plan";
            }
        } catch (IOException e) {
            System.err.println("Error in connecting to ChatGPT API: " + e.getMessage());
            return "Error: Unable to generate personalized fitness plan";
        }
    }

    private void handleErrorResponse(Response response) throws IOException {
        System.err.println("Error in ChatGPT API response: " + response.message());
        System.err.println("Response code: " + response.code());
        System.err.println("Response body: " + response.body().string());
    }

    private String handleSuccessfulRequest(Response response) throws IOException {
        String responseBody = response.body().string();
        System.out.println("generatedText: " + responseBody);
        String generatedText = jsonParserUtil.extractTextFromJsonResponse(responseBody);
        return generatedText;
    }

    private Request buildRequest(String requestBody) {
        return new Request.Builder()
                .url(API_URL)
                .post(RequestBody.create(JSON, requestBody))
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();
    }

    private String createRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("model", "gpt-3.5-turbo");

        JsonObject userMessage = new JsonObject();
        userMessage.addProperty("role", "user");
        userMessage.addProperty("content", prompt);

        JsonArray messages = new JsonArray();
        messages.add(userMessage);

        requestBody.add("messages", messages);
        requestBody.addProperty("max_tokens", 1000);
        requestBody.addProperty("n", 1);
        requestBody.addProperty("temperature", 0.7);

        return gson.toJson(requestBody);
    }
}