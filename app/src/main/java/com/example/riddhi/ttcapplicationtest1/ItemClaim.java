package com.example.riddhi.ttcapplicationtest1;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ItemClaim extends Fragment {

    EditText category, description, location, color, date;
    View fragmentView;
    String token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.activity_item_claim, container, false);

        token = getArguments().getString(MainActivityLogin.Token);

        category = fragmentView.findViewById(R.id.txtItemCategory);
        description = fragmentView.findViewById(R.id.txtItemDescription);
        location = fragmentView.findViewById(R.id.txtItemLostLoc);
        color = fragmentView.findViewById(R.id.txtItemColor);
        date = fragmentView.findViewById(R.id.txtItemLostDate);

        Button submitButton = fragmentView.findViewById(R.id.btnSubmitClaim);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!date.getText().toString().isEmpty() && !category.getText().toString().isEmpty()
                        && !color.getText().toString().isEmpty() && !location.getText().toString().isEmpty() && !description.getText().toString().isEmpty()) {
                    try {
                        if(category.getText().toString().length() < 3){
                            Toast.makeText(getActivity(), "Min length 3 required for should  Category", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(description.getText().toString().length() < 3 || description.getText().toString().length() > 50){
                            Toast.makeText(getActivity(), "Description should less than 50 characters", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        String data = new PostData().execute(
                                token,
                                category.getText().toString(),
                                color.getText().toString(),
                                description.getText().toString(),
                                location.getText().toString(),
                                date.getText().toString()).get();

                        if(data != null){
                            Toast.makeText(getActivity(), "Item Claimed Successfully", Toast.LENGTH_SHORT).show();
                            category.setText(""); //.toString(),
                            color.setText(""); //.toString(),
                            description.setText(""); //.toString(),
                            location.setText(""); //.toString(),
                            date.setText(""); //.toString();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return fragmentView;
    }

    private class PostData extends AsyncTask<String, Void, String> {
        protected String doInBackground(String... params) {
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Webhook.IPADDRESS + "/api/ClaimedItems");

            httppost.addHeader("Content-Type", "application/x-www-form-urlencoded");
            httppost.addHeader("Authorization", "Bearer " + params[0]);
            try {
//                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
//                entityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

//                entityBuilder.addTextBody("Category", params[1], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("Color", params[2], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("Description", params[3], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("Location", params[4], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("DateLost", params[5], ContentType.TEXT_PLAIN);

//                if (params[6] != null) {
//                    File file = new File(params[6]);
//                    Log.d("EDIT USER PROFILE", "UPLOAD: file length = " + file.length());
//                    Log.d("EDIT USER PROFILE", "UPLOAD: file exist = " + file.exists());
//                    //entityBuilder.addPart("avatar", new FileBody(file, "application/octet"));
//                    entityBuilder.addBinaryBody("file", file);
//                }

//                entityBuilder.addTextBody("Category", params[1], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("Color", params[2], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("Description", params[3], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("Location", params[4], ContentType.TEXT_PLAIN);
//                entityBuilder.addTextBody("DateLost", params[5], ContentType.TEXT_PLAIN);

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("Category", params[1]));
                nameValuePairs.add(new BasicNameValuePair("Color", params[2]));
                nameValuePairs.add(new BasicNameValuePair("Description", params[3]));
                nameValuePairs.add(new BasicNameValuePair("Location", params[4]));
                nameValuePairs.add(new BasicNameValuePair("DateLost", params[5]));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));


//                HttpEntity entity = entityBuilder.build();
//                httppost.setEntity(entity);

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

                String json = EntityUtils.toString(response.getEntity());

//                JSONObject myObject = new JSONObject(json);
//                Log.d("responseData1", json);

                if(response.getStatusLine().getStatusCode() != 201){
                    return  null;
                }


//                String token = String.valueOf(myObject.get("access_token"));
//
//                UserObject object = new UserObject();
//                object.fullName = String.valueOf(myObject.get("fullName"));
//                object.userName = String.valueOf(myObject.get("userName"));
//                object.token = String.valueOf(myObject.get("access_token"));
//                object.role = String.valueOf(myObject.get("role"));


//                JSONTokener tokener = new JSONTokener(json);
//                JSONArray finalResult = new JSONArray(tokener);

//                Log.d("responseData2", String.valueOf(finalResult.getString(0)));

                return json;
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            } catch (IOException e) {
                e.printStackTrace();
                // TODO Auto-generated catch block
            }
            return null;
        }
    }

}
