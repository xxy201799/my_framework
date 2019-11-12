package com.xxy.util;

import com.xxy.annotation.Table;
import javafx.scene.control.Tab;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableServer.THREAD_POLICY_ID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseHelper.class);

    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL;

    private static final QueryRunner QUERY_RUNNER;
    private static final BasicDataSource DATA_SOURCE;

    /**
     * 实例化连接操作，读取属性文件
     */
    static {
        CONNECTION_THREAD_LOCAL = new ThreadLocal<Connection>();
        QUERY_RUNNER = new QueryRunner();
        Properties conf = PropsUtil.loadProps("config.properties");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String password = conf.getProperty("jdbc.password");
        DATA_SOURCE = new BasicDataSource();

        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(password);

    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection(){
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if(conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            }catch (SQLException e){
                LOGGER.error("get connection failure" ,e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_THREAD_LOCAL.set(conn);
            }
        }
        return conn;
    }

    /**
     * 关闭数据库连接
     */
    public static void  closeConnection(){
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if(conn != null){
            try {
                conn.close();
            }catch (SQLException e){
                LOGGER.error("close connection failure", e);
            }finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * 查询实体列表
     */
    public static <T> List<T> queryEntityList( Class<T> entityClass, String sql, Object... params){
        List<T> entityList;
        try {
            Connection conn = getConnection();
            entityList = QUERY_RUNNER.query(conn,sql,new BeanListHandler<T>(entityClass),params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure", e);
            throw new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return entityList;
    }

    /**
     * 执行查询语句
     */
    public static List<Map<String, Object>> excuteQuery(String sql,Object... params){
        List<Map<String, Object>> result;
        try {
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        }catch (SQLException e){
            LOGGER.error("excute query failure", e);
            throw new RuntimeException(e);
        }
        return result;
    }
    /**
     * 执行更新语句,返回受影响的行数
     */
    public static int excuteUpdate(String sql, Object... params){
        int rows = 0;
        try {
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        }catch (SQLException e){
            LOGGER.error("excute update failure", e);
            throw new RuntimeException(e);
        }finally {
            closeConnection();
        }
        return rows;
    }

    /**
     * 插入实体
     */
    public static <T> boolean insertEntity(Class<T> entityClass,Map<String, Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity:fieldMap is empty");
            return false;
        }

        String tableName = getTableName(entityClass);
        /**
         * 此机制用于获取表名，如果表名不存在则返回false;
         */
        if(!StringUtils.isNotBlank(tableName)){
            LOGGER.error("this "+entityClass.getName()+"'s entity must have a Table Annotation");
            return false;
        }
        String sql = "INSERT INTO " + tableName;
        StringBuilder cloumns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for(String fieldName : fieldMap.keySet()){
            cloumns.append(fieldName).append(", ");
            values.append("?, ");
        }
        //替换最后一个“，”为 ')'
        cloumns.replace(cloumns.lastIndexOf(", "), cloumns.length(), ")");
        values.replace(values.lastIndexOf(", "), values.length(), ")");
        sql += cloumns + "VALUES" + values;
        Object[] params = fieldMap.values().toArray();
        return excuteUpdate(sql, params) == 1;
    }

    /**
     * 更新实体
     */
    public static <T> boolean updateEntity(Class<T> entityClass, Long id, Map<String, Object> fieldMap){
        if(MapUtils.isEmpty(fieldMap)){
            LOGGER.error("can not update entity:fieldMap is empty");
            return false;
        }
        String tableName = getTableName(entityClass);
        /**
         * 此机制用于获取表名，如果表名不存在则返回false;
         */
        if(!StringUtils.isNotBlank(tableName)){
            LOGGER.error("this "+entityClass.getName()+"'s entity must have a Table Annotation");
            return false;
        }
        String sql = "UPDATE " + tableName + " SET ";
        StringBuilder cloumns = new StringBuilder();
        for(String fieldName : fieldMap.keySet()){
            cloumns.append(fieldName).append("=?, ");
        }
        sql += cloumns.substring(0, cloumns.lastIndexOf(", ")) + " WHERE id=?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();
        return  excuteUpdate(sql, params) == 1;
    }
    /**
     * 删除实体
     */
    public static <T> boolean deleteEntity(Class<T> entityClass, Long id){

        String tableName = getTableName(entityClass);
        /**
         * 此机制用于获取表名，如果表名不存在则返回false;
         */
        if(!StringUtils.isNotBlank(tableName)){
            LOGGER.error("this "+entityClass.getName()+"'s entity must have a Table Annotation");
            return false;
        }
        String sql = "DELETE FROM" + tableName + "WHERE id = ?";
        return excuteUpdate(sql, id) == 1;
    }

    /**
     * 获取实体类表名
     * @param entityClass
     * @param <T>
     * @return
     */
    private static <T> String getTableName(Class<T> entityClass) {
        boolean result = entityClass.isAnnotationPresent(Table.class);
        if(!result){
            return "";
        }else {
            Table table = entityClass.getAnnotation(Table.class);
            return table.value();
        }
    }

    /**
     * 开启事务
     */
    public static void beginTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try {
                conn.setAutoCommit(false);
            }catch (SQLException e){
                LOGGER.error("begin transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_THREAD_LOCAL.set(conn);
            }
        }
    }

    /**
     * 提交事务
     */
    public static void commitTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try {
                conn.commit();
                conn.close();
            }catch (SQLException e){
                LOGGER.error("commit transaciton failuer",e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }

    /**
     * 回滚事务
     */
    public static void rollbackTransaction(){
        Connection conn = getConnection();
        if(conn != null){
            try {
                conn.rollback();
                conn.close();
            }catch (SQLException e){
                LOGGER.error("rollback transaction failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_THREAD_LOCAL.remove();
            }
        }
    }
}
