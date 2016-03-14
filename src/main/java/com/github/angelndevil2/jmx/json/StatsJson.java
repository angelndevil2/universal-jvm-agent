package com.github.angelndevil2.jmx.json;

import com.github.angelndevil2.xii4j.JDBCConnectionPoolStats;
import com.github.angelndevil2.xii4j.JDBCConnectionStats;
import com.github.angelndevil2.xii4j.JDBCStats;
import com.github.angelndevil2.xii4j.Stats;
import com.github.angelndevil2.xii4j.util.ReflectionUtil;
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

        Stats stat;

        if (ReflectionUtil.isInterfaceInInterfaceArray(JDBCStats.IMPLEMENTED_FOR, stats.getClass().getInterfaces())) {

            // check JDBCStats
            stat = new JDBCStats();
            stat.initializeFrom(stats);

        } else if (ReflectionUtil.isInterfaceInInterfaceArray(JDBCConnectionPoolStats.IMPLEMENTED_FOR, stats.getClass().getInterfaces())) {

            // check JDBCConnectionPoolStats
            stat = new JDBCConnectionPoolStats();
            stat.initializeFrom(stats);

        } else if (ReflectionUtil.isInterfaceInInterfaceArray(JDBCConnectionStats.IMPLEMENTED_FOR, stats.getClass().getInterfaces())) {

            // check JDBCConnetctionStats
            stat = new JDBCConnectionStats();
            stat.initializeFrom(stats);

        } else {
            // Stats
            stat = new Stats();
            stat.initializeFrom(stats);
        }


        Gson json = new GsonBuilder().serializeNulls().create();
        return json.toJson(stat);
    }
}
