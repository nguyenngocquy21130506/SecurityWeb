package com.commenau.log;

import com.commenau.dao.LogDao;
import com.commenau.dto.LogDTO;
import com.commenau.filter.RequestFilter;
import com.commenau.model.LocationInFo;
import com.commenau.model.LogLevel;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class LogService {
    Gson gson = new Gson();
    @Inject
    LogDao dao;

    private LocationInFo getLocationInFo(String ip) {
        try {
            String apiUrl = "https://api.ip2location.io/?key=3CF8C3907C6B17A19073AF2376B1EC0E&ip=" + ip;

            // Tạo đối tượng URL từ URL của API
            URL url = new URL(apiUrl);

            // Mở kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Thiết lập phương thức yêu cầu (GET, POST, PUT, DELETE, vv.)
            connection.setRequestMethod("GET");

            // Đọc phản hồi từ API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Phân tích phản hồi JSON bằng Gson
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();
            LocationInFo responseObject = gson.fromJson(response.toString(), LocationInFo.class);

            connection.disconnect();
            return responseObject;

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    private Log init(LogLevel level, String status) {
        HttpServletRequest request = RequestFilter.getCurrentRequest();
        String ip = request.getHeader("X-FORWARDED-FOR");
        var location = getLocationInFo(ip);
        var log = Log.builder().status(status).endpoint(request.getServletPath()).method(request.getMethod()).address(ip).level(level).build();
        if (location == null) return log;
        log.setCity(location.getCityName());
        log.setCountry(location.getCountryName());
        log.setRegion(location.getRegionName());
        return log;
    }

    private boolean save(Log log) {
        boolean save = dao.save(log) > 0;
        RequestFilter.addLog(log);
        return save;
    }


    public boolean save(LogLevel level, String status, Logable pre, Logable value) {
        Log log = init(level, status);

        if (pre != null && value != null) {
            log.setPreValue(gson.toJson(pre.getData()));
            log.setValue(gson.toJson(value.getData()));
        }
        return save(log);
    }

    public boolean save(LogLevel level, String status, Logable value) {
        Log log = init(level, status);
        if (value != null) {
            log.setValue(gson.toJson(value.getData()));
        }
        return save(log);
    }

    public boolean save(LogLevel level, String status, Map<String, String> data) {
        Log log = init(level, status);
        log.setValue(gson.toJson(data));
        return save(log);
    }
    public boolean save(LogLevel level, String status, Map<String, String> preData,Map<String, String> data) {
        Log log = init(level, status);
        log.setPreValue(gson.toJson(preData));
        log.setValue(gson.toJson(data));
        return save(log);
    }

    public List<LogDTO> getAllLog() {
        return dao.getAllLogDTO();
    }

    public List<LogDTO> getLogForLevel(int level) {
        return dao.getLogForLevel(level);
    }
}
