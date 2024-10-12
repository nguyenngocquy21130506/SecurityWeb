package com.commenau.dao;

import com.commenau.connectionPool.Connection;
import com.commenau.connectionPool.ConnectionPool;
import com.commenau.dto.LogDTO;
import com.commenau.log.Log;
import com.commenau.model.Rule;

import java.util.List;

public class LogDao {
    public int save(Log log) {
        return ConnectionPool.getConnection().withHandle(n -> {
            return n.createUpdate("insert into logs(endpoint,status,method,address,country,region,city,level,preValue,value) values ( :endpoint,:status , :method , :address , :country , :region , :city , :level , :preValue , :value )")
                    .bind("endpoint", log.getEndpoint())
                    .bind("method", log.getMethod())
                    .bind("address", log.getAddress())
                    .bind("country", log.getCountry())
                    .bind("region", log.getRegion())
                    .bind("city", log.getCity())
                    .bind("level", log.getLevel().getValue())
                    .bind("preValue", log.getPreValue())
                    .bind("value", log.getValue())
                    .bind("status", log.getStatus()).execute();
        });
    }

    public boolean checkWarning(Rule rule, String address) {
        return ConnectionPool.getConnection().withHandle(n -> {
            var query = n.createQuery(rule.getSql());
            query.bind("address", address);

            for (var x : rule.getData().entrySet()) {
                query.bind(x.getKey(), x.getValue());
            }

            return query.mapTo(Integer.class).one() > 0;
        });
    }

    public List<LogDTO> getAllLogDTO() {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select endpoint, status, address as ipAddress,level, preValue, value, createAt from logs order BY createAt desc").mapToBean(LogDTO.class).list();
        });
    }

    public List<LogDTO> getLogForLevel(int level) {
        return ConnectionPool.getConnection().withHandle(handle -> {
            return handle.createQuery("select endpoint, status, address as ipAddress,level, preValue, value, createAt from logs where level = ? order BY createAt desc")
                    .bind(0,level)
                    .mapToBean(LogDTO.class).list();
        });
    }
}
