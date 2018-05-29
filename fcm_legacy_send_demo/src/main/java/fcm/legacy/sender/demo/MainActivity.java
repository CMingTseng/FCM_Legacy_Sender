package fcm.legacy.sender.demo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import fcm.legacy.sender.FCMSender;
import fcm.legacy.sender.service.SendContentData;
import fcm.legacy.sender.service.SendPackagingData;
import fcm.legacy.sender.service.SendPackagingMapData;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText fcm_key_input = (EditText) findViewById(R.id.fcm_key_input);
        final EditText receiver_input = (EditText) findViewById(R.id.receiver_input);
        final String receivertoken = getIntent().getStringExtra("FCM_Recive_Client");
        if (!TextUtils.isEmpty(receivertoken)) {
            receiver_input.setText(receivertoken);
        }
        final EditText data_key_1 = (EditText) findViewById(R.id.data_key_1);
        final EditText data_key_2 = (EditText) findViewById(R.id.data_key_2);
        final EditText data_key_3 = (EditText) findViewById(R.id.data_key_3);
        final EditText data_key_4 = (EditText) findViewById(R.id.data_key_4);

        final EditText data_key_value_1 = (EditText) findViewById(R.id.data_key_value_1);
        final EditText data_key_value_2 = (EditText) findViewById(R.id.data_key_value_2);
        final EditText data_key_value_3 = (EditText) findViewById(R.id.data_key_value_3);
        final EditText data_key_value_4 = (EditText) findViewById(R.id.data_key_value_4);
        final Button send = (Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Gson gson = new Gson();
                String fcm_key_inputs = fcm_key_input.getText().toString();
                String receiver_inputs = receiver_input.getText().toString();
                String input_key_1s = data_key_1.getText().toString();
                if (TextUtils.isEmpty(fcm_key_inputs)) {
                    fcm_key_inputs = "AXXXXXXXXXXXXXXXX";
                }
                if (TextUtils.isEmpty(receiver_inputs)) {
                    receiver_inputs = "dgIYWaT7CiU:APA91bHb3vVxFW21dSYBmDtr2c9HAIf0bWREUgJuhwutWFQ5Ln8E0HUvtqZ5jYgcLSkWaAXUYGrbzZxyexNpaVktKHXI6uiPB9aZZ3fe9fV1WkaZztEe0T55G7uW65rcdv3YE74uFkoL";
                }
                final FCMSender sender = new FCMSender(fcm_key_inputs);

                if (TextUtils.isEmpty(input_key_1s)) {
                    input_key_1s = "fountain";
                }

                String input_key_2s = data_key_2.getText().toString();
                if (TextUtils.isEmpty(input_key_2s)) {
                    input_key_2s = "message";
                }

                String input_key_3s = data_key_3.getText().toString();
                if (TextUtils.isEmpty(input_key_3s)) {
                    input_key_3s = "msgType";
                }

                String input_key_4s = data_key_4.getText().toString();
                if (TextUtils.isEmpty(input_key_4s)) {
                    input_key_4s = "device_id";
                }

                final String data_key_value_1s = data_key_value_1.getText().toString();
                final String data_key_value_2s = data_key_value_2.getText().toString();
                final String data_key_value_3s = data_key_value_3.getText().toString();
                final String data_key_value_4s = data_key_value_4.getText().toString();

                final Map<String, String> detailcontentmap = new HashMap<String, String>();
                detailcontentmap.put(input_key_1s, data_key_value_1s);
                detailcontentmap.put(input_key_2s, data_key_value_2s);
                detailcontentmap.put(input_key_3s, data_key_value_3s);
                detailcontentmap.put(input_key_4s, data_key_value_4s);

                //FIXME  Use Gson JsonObject send notification
                final JsonObject sendinfo = new JsonObject();
                Type type = new TypeToken<Map>() {
                }.getType();
                final JsonElement jsonElement = gson.toJsonTree(detailcontentmap, type);
                sendinfo.add("data", jsonElement);
                sendinfo.addProperty("to", receiver_inputs);
                sender.sendMessageToFcm(sendinfo);

                //FIXME  this is default  use self create object
                // FIXME way1
                final SendContentData content = new SendContentData(data_key_value_1s, data_key_value_2s, Integer.valueOf(data_key_value_3s), Long.valueOf(data_key_value_4s));
                type = new TypeToken<Map<String, String>>() {
                }.getType();
                final String jsonInString = gson.toJson(content);
                final Map<String, String> detailcontentmapbygson = gson.fromJson(jsonInString, type);
                final Set<String> keys = detailcontentmapbygson.keySet();
                for (String s : keys) {
                    Log.d(TAG, "annotation name: " + s);
                    Log.d(TAG, "annotation SerializedName value : " + detailcontentmapbygson.get(s));
                }
                final Field[] fields = content.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if (null == field.getAnnotation(SerializedName.class)) {
                        continue;
                    }
                    Log.d(TAG, "Field annotation name: " + field.getName());
                    Log.d(TAG, "Field annotation SerializedName: " + field.getAnnotation(SerializedName.class).value());
                }

                sender.sendMessageToFcmByDefaultPackage(new SendPackagingData(receiver_inputs, content));
                // FIXME way 2
                sender.sendMessageToFcmByDefaultPackage(new SendPackagingMapData(receiver_inputs, detailcontentmap));
            }
        });
    }
}
