package com.example.myrecipe;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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


    private static final String GEMINI_API_KEY = "AIzaSyDrzUv_h4UXEO6ktLia-sp6i54s5suN-sQ";
    private static final String GEMINI_ENDPOINT =  "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.0-pro:generateContent?key=";
    EditText etMessage;
    Button btnSend;
    TextView tvResponse; // This will show the latest AI response

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
                // Clear the input field immediately
                etMessage.setText("");
                tvResponse.setText("Thinking..."); // Provide immediate feedback
                sendMessageToGemini(userMessage);
            } else {
                Toast.makeText(ChatActivity.this, "Please type a message.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessageToGemini(String message) {
        try {
            // 1. Build the JSON request body for Gemini
            JSONObject json = new JSONObject();
            JSONArray contents = new JSONArray();
            JSONObject content = new JSONObject();
            content.put("role", "user"); // Role for user message

            JSONArray parts = new JSONArray();
            JSONObject part = new JSONObject();
            part.put("text", message);
            parts.put(part);

            content.put("parts", parts);
            contents.put(content);
            json.put("contents", contents);

            // Optional: Add generation config for more control
            // JSONObject generationConfig = new JSONObject();
            // generationConfig.put("temperature", 0.7); // Adjust for creativity (0.0-1.0)
            // generationConfig.put("topP", 0.95);
            // generationConfig.put("topK", 40);
            // generationConfig.put("maxOutputTokens", 1024);
            // json.put("generationConfig", generationConfig);


            // 2. Build the request
            RequestBody body = RequestBody.create(
                    json.toString(),
                    MediaType.parse("application/json; charset=utf-8") // Specify charset
            );

            Request request = new Request.Builder()
                    .url(GEMINI_ENDPOINT + GEMINI_API_KEY) // Append API key directly to URL
                    .post(body)
                    .build();

            // 3. Call the API asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    runOnUiThread(() -> {
                        tvResponse.setText("Network Error: " + e.getMessage());
                        Toast.makeText(ChatActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String responseData = response.body() != null ? response.body().string() : "No body";
                    Log.d("Gemini_Response", responseData);

                    if (response.isSuccessful()) {
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray candidates = jsonObject.getJSONArray("candidates");
                            if (candidates.length() > 0) {
                                JSONObject firstCandidate = candidates.getJSONObject(0);
                                JSONObject content = firstCandidate.getJSONObject("content");
                                JSONArray parts = content.getJSONArray("parts");
                                if (parts.length() > 0) {
                                    String reply = parts.getJSONObject(0).getString("text");
                                    runOnUiThread(() -> tvResponse.setText("AI: " + reply.trim()));
                                } else {
                                    runOnUiThread(() -> tvResponse.setText("AI: No parts found in response."));
                                }
                            } else {
                                runOnUiThread(() -> tvResponse.setText("AI: No candidates found in response."));
                            }
                        } catch (JSONException e) {
                            runOnUiThread(() -> {
                                tvResponse.setText("Failed to parse response: " + e.getMessage());
                                Toast.makeText(ChatActivity.this, "Failed to parse AI response.", Toast.LENGTH_LONG).show();
                            });
                        }
                    } else {
                        runOnUiThread(() -> {
                            tvResponse.setText("Error " + response.code() + ": " + responseData);
                            Toast.makeText(ChatActivity.this, "API Error: " + response.code(), Toast.LENGTH_LONG).show();
                        });
                    }
                }
            });

        } catch (JSONException e) {
            runOnUiThread(() -> {
                tvResponse.setText("JSON error: " + e.getMessage());
                Toast.makeText(ChatActivity.this, "Internal JSON error.", Toast.LENGTH_LONG).show();
            });
        }
    }
}