package com.github.angelndevil2.jmx.json;

import com.github.angelndevil2.xii4j.Stats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.InvocationTargetException;


/**
 * @since 0.0.3
 * javax.management.j2ee.statistics.Stats to Json using com.github.angelndevil2.xii4j.Stats
 *
 * @author k, Created on 16. 3. 13.
 */
public class StatsJson {

    /**
     *
     * @param stats javax.management.j2ee.statistics.Stats
     * @return json string
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static String toJsonString(final Object stats)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

        Stats stat = new Stats();
        stat.initializeFrom(stats);

        Gson json = new GsonBuilder().serializeNulls().create();
        return json.toJson(stat);
    }
}
