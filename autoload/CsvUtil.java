// src/com/campus/util/CsvUtil.java
package com.campus.util;

import com.campus.entity.Activity;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CsvUtil {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void exportActivity(List<Activity> list, String filePath) throws Exception {
        try (PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filePath), "UTF-8"))) {
            // 表头
            pw.println("ID,名称,地点,开始时间,结束时间,状态,人数上限");
            
            // 数据：实时计算状态，不直接使用 activity.getStatus()
            for (Activity a : list) {
                // 核心：实时计算状态
                String realStatus = calcRealStatus(a);
                
                pw.printf("%d,\"%s\",\"%s\",\"%s\",\"%s\",\"%s\",%d%n",
                    a.getId(),
                    a.getName(),
                    a.getLocation(),
                    a.getStartTime().format(FMT),
                    a.getEndTime().format(FMT),
                    realStatus,  // ← 使用计算后的状态
                    a.getMaxNum());
            }
        }
    }

    /* 根据时间计算真实状态 */
    private static String calcRealStatus(Activity a) {
        String dbStatus = a.getStatus();
        if ("已取消".equals(dbStatus)) return "已取消";
        
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(a.getStartTime())) return "未开始";
        if (now.isAfter(a.getEndTime())) return "已结束";
        return "报名中";
    }
}
