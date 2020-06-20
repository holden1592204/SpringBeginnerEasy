package com.example.demo.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inquiry;

@Repository
public class InquiryDaoImpl implements InquiryDao {
	
	private final JdbcTemplate jdbcTemplate;
	
	@Autowired
	public InquiryDaoImpl(JdbcTemplate jdbcTemplate) { //引数にDIコンテナで生成されたインスタンスが渡される
		this.jdbcTemplate = jdbcTemplate; //↑で生成されたインスタンスをjdbcTemplateに格納する
	}

	@Override
	public void insertInquiry(Inquiry inquiry) {
		jdbcTemplate.update("INSERT INTO inquiry(name, email, contents, created) VALUES(?, ?, ?, ?)", 
				inquiry.getName(), inquiry.getEmail(), inquiry.getContents(), inquiry.getCreated()); //?の数だけ引数を増やす
	}

	@Override
	public List<Inquiry> getAll() {
		String sql = "SELECT id, name, email, contents, created FROM inquiry";
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);  //ListにMapが入っている状態
		List<Inquiry> list = new ArrayList<Inquiry>();  //初期化
		for(Map<String, Object> result : resultList) {
			Inquiry inquiry = new Inquiry();  //インスタンス化したinquiryの中にMapの中身を詰めていく
			inquiry.setId((int)result.get("id"));  
			inquiry.setName((String)result.get("name"));  //オブジェクトとして返ってくる物をintやStringに変換する＝キャスト
			inquiry.setEmail((String)result.get("email"));
			inquiry.setContents((String)result.get("contents"));
			inquiry.setCreated(((Timestamp)result.get("created")).toLocalDateTime());
			list.add(inquiry); //複数行あってもlistの中にinquiryが詰められていくようになっている
		}
		return list;  //詰めた後のlistをreturnする
	}

	@Override
	public int updateInquiry(Inquiry inquiry) {
		return jdbcTemplate.update("UPDATE inquiry SET name = ?, email = ?, contents = ? WHERE id =?",
				inquiry.getName(), inquiry.getEmail(), inquiry.getContents(), inquiry.getId());
	}

}
