package main;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import pojo.Info;

import com.ibatis.sqlmap.client.SqlMapClient;
import com.ibatis.sqlmap.engine.builder.xml.SqlMapConfigParser;

public class Main {

	/**
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		SqlMapConfigParser configParser = new SqlMapConfigParser();
		try {
			Date start = new Date(10000000);
			Date end = new Date(10000000000l);
			System.out.println("start:" + start);
			System.out.println("end:" + start);

			InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("ibatis.xml");
			SqlMapClient client = configParser.parse(is);

			System.out.println("prepare data........");
			client.startTransaction();
			client.delete("info.deleteall");
			client.endTransaction();

			client.startTransaction();
			HashMap<String, Date> map = new HashMap<String, Date>();
			map.put("start", start);
			map.put("end", end);
			client.insert("info.insertpojo",map);
			client.commitTransaction();

			System.out.println("\nUSE jdgcType=\"DATE\" result...........................");
			//jdbcType="DATE"
			List<pojo.Info> list = client.queryForList("info.selectpojo1");
			System.out.println("list size(should be 1):" + list.size());
			Info info = list.get(0);
			System.out.println("start:" + info.getBegin());
			System.out.println("end:" + info.getEnd());

			System.out.println("\nUSE jdgcType=\"date\" result............................");
			//jdbcType="date"
			list = client.queryForList("info.selectpojo2");
			System.out.println("list size(should be 1):" + list.size());
			info = list.get(0);
			System.out.println("start:" + info.getBegin());
			System.out.println("end:" + info.getEnd());
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
