package com.mcc.fshopper.network.parser;

import com.mcc.fshopper.model.Comment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nasir on 8/13/2016.
 */
public class CommentsParser {

    public ArrayList<Comment> getComments(String response) {
        if (response != null) {
            try {
                JSONObject objResponse = new JSONObject(response);

                ArrayList<Comment> commentList = new ArrayList<>();
                JSONArray jsonArray = objResponse.getJSONArray(ParserKey.KEY_DATA);

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String id = null, message = null, dateTime = null;

                    if (jsonObject.has(ParserKey.KEY_ID)) {
                        id = jsonObject.getString(ParserKey.KEY_ID);
                    }
                    if (jsonObject.has(ParserKey.KEY_MESSAGE)) {
                        message = jsonObject.getString(ParserKey.KEY_MESSAGE);
                    }
                    if (jsonObject.has(ParserKey.KEY_CREATED_TIME)) {
                        dateTime = jsonObject.getString(ParserKey.KEY_CREATED_TIME);
                    }
                    commentList.add(new Comment(id, message, dateTime));
                }
                return commentList;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
