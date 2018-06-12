JSONObject json;

void setup() {

  json = loadJSONObject("../data/sample.json");

  String src = json.getString("src");
  
  println("src: " + src + ".");
  
  JSONArray captures = json.getJSONArray("capture");
  for(int i = 0; i < captures.size(); i++) {
    JSONObject el = captures.getJSONObject(i);
    String id = el.getString("id");
    int level = el.getInt("level");
    int weight = el.getInt("weight");
    println("id: " + id + ", level: " + level + ", weight: " + weight);
  }

}