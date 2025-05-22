package com.example.myrecipe;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private static final String OPENAI_API_KEY = "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"; // 🔐 Replace with your actual key
    private static final String OPENAI_ENDPOINT = "https://api.openai.com/v1/chat/completions";

    EditText etMessage;
    Button btnSend;
    TextView tvResponse;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        tvResponse = findViewById(R.id.tvResponse);

        btnSend.setOnClickListener(v -> {
            String userMessage = etMessage.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                sendMessageToOpenAI(userMessage);
            }
        });
    }

    private void sendMessageToOpenAI(String message) {
        try {
            // 1. Build the JSON request body
            JSONObject json = new JSONObject();
            json.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", message);
            messages.put(userMessage);

            json.put("messages", messages);

            // 2. Build the request
            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(OPENAI_ENDPOINT)
                    .header("Authorization", "Bearer " + OPENAI_API_KEY)
                    .post(body)
                    .build();

            // 3. Call the API asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> tvResponse.setText("Failure Error: " + e.getMessage()));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body() != null ? response.body().string() : "No body";
                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray choices = jsonObject.getJSONArray("choices");
                            String reply = choices.getJSONObject(0).getJSONObject("message").getString("content");

                            runOnUiThread(() -> tvResponse.setText("AI: " + reply.trim()));
                        } catch (JSONException e) {
                            runOnUiThread(() -> tvResponse.setText("Failed to parse response"));
                        }
                    } else {
                        runOnUiThread(() -> tvResponse.setText("Error " + response.code() + ": " + responseData));
                    }
//                    if (response.isSuccessful()) {
//                        String responseData = response.body().string();
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(responseData);
//                            JSONArray choices = jsonObject.getJSONArray("choices");
//                            String reply = choices.getJSONObject(0).getJSONObject("message").getString("content");
//
//                            runOnUiThread(() -> tvResponse.setText("AI: " + reply.trim()));
//                        } catch (JSONException e) {
//                            runOnUiThread(() -> tvResponse.setText("Failed to parse response"));
//                        }
//
//                    } else {
//                        runOnUiThread(() -> tvResponse.setText("Response Error: " + response.message()));
//                    }
                }
            });

        } catch (JSONException e) {
            tvResponse.setText("JSON error: " + e.getMessage());
        }
    }
}