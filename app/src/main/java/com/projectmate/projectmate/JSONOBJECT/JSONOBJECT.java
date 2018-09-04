package com.projectmate.projectmate.JSONOBJECT;

import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class JSONOBJECT {


        private String name;
        private String institution;
        private String city;
        private String country;
        private String username;
        private int ranking;
        private JSONArray skills;

        public void JSONObject(String name, String institution, String city, String country,
                               String username, int ranking , ArrayList<String> listx, ArrayList<Integer> intx){
            this.name = name;
            this.institution = institution;
            this.city = city;
            this.country = country;
            this.username = username;
            this.ranking = ranking;

            JSONArray values = new JSONArray();
            for (int i = 0; i < listx.size(); i++) {
                JSONObject skills = new JSONObject();
                skills.put("Language",listx.get(i));
                skills.put("Scale",intx.get(i));
                values.set(i,skills);
            }
            JSONObject json =  new JSONObject();
            json.put("skills",skills);
        }

        public JSONObject main(String[] args){
            JSONObject obj = new JSONObject();
            obj.put("name",name);
            obj.put("institution",institution);
            obj.put("city",city);
            obj.put("country",country);
            obj.put("username",username);
            obj.put("ranking",ranking);
            obj.put("skills",skills);
            return obj;
        }

    }

}
