package com.github.angelndevil2.universaljvmagent;

import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author k, Created on 16. 3. 12.
 */
public class JSONTest {

    public static class TestObject {
        public final int id;
        public final String name;
        public final ArrayList<Integer> intArray;

        public TestObject() {
            this.id = 1;
            this.name = "test/";
            this.intArray = new ArrayList<Integer>();
            for (int idx = 0; idx < 10; idx++) this.intArray.add(idx);
        }
    }

    private Object obj = new TestObject();

    @Test
    public void testWithToString() {
        System.out.print("using default toString() => ");
        System.out.println(obj);
        System.out.println("");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testWithJSONSimple() {
        // should know the object structure!
        JSONObject json = new JSONObject();
        json.put("id", ((TestObject)obj).id);
        json.put("name", ((TestObject)obj).name);
        json.put("intArray", ((TestObject)obj).intArray);

        System.out.print("using JSON Simple => ");
        System.out.print(json.toJSONString());
        System.out.println("");
    }

    @Test
    public void testWithGson() {
        Gson gson = new Gson();

        System.out.print("using GSon => ");
        System.out.print(gson.toJson(obj));
        System.out.println("");
    }
}
