package com.auth0.samples.authapi.user;

import oracle.jdbc.OraclePreparedStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CrossOrigin()
@Configuration
@PropertySource("classpath:application.properties")
@RestController
public class Rec_control {

//	private static final Logger LOGGER = LoggerFactory.getLogger(Rec_control.class);

	@Autowired
	private Environment env;

	public Connection getconn() {



		try {
			System.out.println("try oracledriver");
			Class.forName("oracle.jdbc.OracleDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("error from oracledriver");
		}

		Properties props = new Properties();
		props.setProperty("user", env.getProperty("spring.datasource.username"));
		props.setProperty("password", env.getProperty("spring.datasource.password"));
		try {
			return DriverManager.getConnection(env.getProperty("spring.datasource.url"), props);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(
			value = "/select",
			method = POST,
			params = {"uid"},
			produces = "application/json")
	@ResponseBody
	// @RequestMapping(value = "/select", method = RequestMethod.GET, produces = "application/json")
	public String selectdb(@RequestParam(value = "uid") String uid,
						   @RequestBody Map<String, List<String> > tparams) throws SQLException, JSONException {


			List<String> params = (List<String>) tparams.values().toArray()[0];

			Connection conn = getconn();
		try{
//		LOGGER.error("demo error2 from select - {}", uid);
//		LOGGER.info("demo info from select - {}", uid);
//		LOGGER.debug("demo debug from select - {}", uid);
//		LOGGER.warn("demo warn from select - {}", uid);


			Statement st;
			st = conn.createStatement();


			PreparedStatement query = conn.prepareStatement("SELECT " + env.getProperty("db.sqlcol") + " FROM " + env.getProperty("db.hashql") + " WHERE " + env.getProperty("db.quid") + " = ?");
			query.setString(1, uid);

			ResultSet resultSet = null;

			try {
				resultSet = query.executeQuery(); // st.executeQuery(query);
			} catch (Exception ex) {
				query.close();
				st.close();
				conn.close();
				return ex.toString();
			}


			if (!resultSet.next()) return null; // "{\"result\": \"error\"}";


			String secquery = resultSet.getString("QSQL");
			query = conn.prepareStatement(secquery);


			if (params != null)
				for (int i = 0; i < params.size(); i++) {

					((OraclePreparedStatement) query).setStringAtName(Integer.toString(i), params.get(i));

				}

			//=======================================

			try {
				System.out.println("-- " + query + " ---");
				resultSet = query.executeQuery();
			} catch (Exception ex) {
				query.close();
				st.close();
				conn.close();
				return ex.toString();
			}


			JSONArray json = new JSONArray();
			ResultSetMetaData rsmd = resultSet.getMetaData();
			while (resultSet.next()) {
				int numColumns = rsmd.getColumnCount();
				JSONObject obj = new JSONObject();
				for (int i = 1; i <= numColumns; i++) {
					String column_name = rsmd.getColumnName(i);
					obj.put(column_name, resultSet.getObject(column_name));
				}
				json.put(obj);
			}


			query.close();
			st.close();
			conn.close();
			return json.toString(); //temp.substring(0, temp.length() - 2) + "]";
		} finally {

			conn.close();

		}

	}
}
