package com.example.myrecipe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
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

    private static final String GEMINI_API_KEY = "AIzaSyDrzUv_h4UXEO6ktLia-sp6i54s5suN-sQ";// מפתח API קבוע לשליחת בקשות לג'מיני
    private static final String GEMINI_ENDPOINT = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";// כתובת ה-API
    EditText etMessage;// שדה להזנת הודעת המשתמש
    Button btnSend;// כפתור לשליחת ההודעה
    TextView tvResponse;// תיבת טקסט להצגת תגובת ה-AI
    OkHttpClient client = new OkHttpClient();// לקוח לשליחת בקשות רשת

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etMessage = findViewById(R.id.etChatWithAIMessage);
        btnSend = findViewById(R.id.btnChatWithAISend);
        tvResponse = findViewById(R.id.tvChatWithAIResponse);

        btnSend.setOnClickListener(v -> {// כאשר נלחץ כפתור השליחה
            String userMessage = etMessage.getText().toString().trim();
            if (!userMessage.isEmpty()) {

                etMessage.setText("");
                tvResponse.setText("Thinking...");
                sendMessageToGemini(userMessage);// שולח את ההודעה ל-Gemini
            } else {
                Toast.makeText(ChatActivity.this, "Please type a message.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendMessageToGemini(String message) {// פעולה ששולחת את ההודעה ל-Gemini API
        try {

            JSONObject json = new JSONObject();// יוצר אובייקט JSON
            JSONArray contents = new JSONArray();// יוצר מערך
            JSONObject content = new JSONObject();//יוצר
            content.put("role", "user");// מגדיר שהמשתמש הוא הדובר

            JSONArray parts = new JSONArray();// חלקים של התוכן
            JSONObject part = new JSONObject();// חלק אחד של הטקסט
            part.put("text", message);// מכניס את ההודעה של המשתמש
            parts.put(part);// מוסיף למערך החלקים

            content.put("parts", parts);// מוסיף את החלקים לאובייקט התוכן
            contents.put(content);// מוסיף את התוכן למערך הכולל
            json.put("contents", contents);// מכניס הכל לתוך JSON סופי

            RequestBody body = RequestBody.create(// יוצר גוף בקשת POST
                    json.toString(),// תוכן הבקשה בפורמט JSON
                    MediaType.parse("application/json; charset=utf-8")// סוג תוכן JSON
            );

            Request request = new Request.Builder() // בונה את בקשת ה-POST
                    .url(GEMINI_ENDPOINT + GEMINI_API_KEY)// כתובת ה-API עם המפתח
                    .post(body)// מציין שזה POST
                    .build();// בונה את הבקשה


            client.newCall(request).enqueue(new Callback() {//שולח את הבקשה
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();// אם הבקשה נכשלה
                    runOnUiThread(() -> {// מריץ את הפעולה על ת'רד ראשי
                        tvResponse.setText("Network Error: " + e.getMessage());// מציג שגיאת רשת
                        Toast.makeText(ChatActivity.this, "Network error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {// אם התקבלה תשובה
                    String responseData = response.body() != null ? response.body().string() : "No body";
                    Log.d("Gemini_Response", responseData);

                    if (response.isSuccessful()) {// אם התשובה הצליחה
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);// ממיר את התשובה ל-JSON
                            JSONArray candidates = jsonObject.getJSONArray("candidates");// מוציא את רשימת התשובות
                            if (candidates.length() > 0) {// אם יש תשובה
                                JSONObject firstCandidate = candidates.getJSONObject(0);// לוקח את הראשונה
                                JSONObject content = firstCandidate.getJSONObject("content");// התוכן שבתוכה
                                JSONArray parts = content.getJSONArray("parts");// מקבל את חלקי התשובה
                                if (parts.length() > 0) {
                                    String reply = parts.getJSONObject(0).getString("text");// מוציא את הטקסט עצמו
                                    runOnUiThread(() -> tvResponse.setText("AI: " + reply.trim()));
                                } else {
                                    runOnUiThread(() -> tvResponse.setText("Error"));
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
                Toast.makeText(ChatActivity.this, " error.", Toast.LENGTH_LONG).show();
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_about) {
            Intent intent = new Intent(this, AboutAppActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_favorites_list) {
            Intent intent = new Intent(this, FavoritesListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_myRecipes_list) {
            Intent intent = new Intent(this, MyRecipesListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_recipe_list) {
            Intent intent = new Intent(this, RecipeListActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_upload_new_recipe) {
            Intent intent = new Intent(this, UploadNewRecipeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_chat) {
            return true;
        } else if (itemId == R.id.menu_item_alarm) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.menu_item_logout) {
            Toast.makeText(this, "Logout clicked", Toast.LENGTH_SHORT).show();
            SharedPreferences sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.remove("user_id");
            editor.remove("user_email");
            editor.remove("user_token");
            editor.apply();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, FirstScreenActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}