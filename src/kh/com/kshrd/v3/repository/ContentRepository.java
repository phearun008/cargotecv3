package kh.com.kshrd.v3.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import kh.com.kshrd.v3.Content;
import kh.com.kshrd.v3.Description;

public class ContentRepository {
	private Connection con;
	
	public ContentRepository() {
		con = ConnectionManagement.getConnection();
	}

	public boolean save(List<Content> contents, String fileName) throws SQLException {

		long fileId = save(fileName);
		
		PreparedStatement p;
		for (Content content : contents) {
			String sql = "INSERT INTO content_v3 (chapter, description, reference, page, section_reference, section_title, section_image_location, section_number, file_id) VALUES (?,?,?,?,?,?,?,?,?);";
			p = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
			p.setString(1, content.getChapter());
			p.setString(2, content.getDescription());
			p.setString(3, content.getRe());
			p.setString(4, content.getPage());
			if (content.getSectionImage() != null) {
				p.setString(5, content.getSectionImage().getNumber());
				p.setString(6, content.getSectionImage().getTitle());
				p.setString(7, content.getSectionImage().getUrl());
				p.setString(8, content.getSectionImage().getSection());
			} else {
				p.setString(5, null);
				p.setString(6, null);
				p.setString(7, null);
				p.setString(8, null);
			}
			p.setLong(9, fileId);
			
			// TODO: Get ID After Insert
			p.executeUpdate();
			ResultSet rs = p.getGeneratedKeys();
			long id = 1;
			if (rs != null && rs.next()) {
				id = rs.getLong(1);
				PreparedStatement p1 = con.prepareStatement(
						"INSERT INTO description_v3 (pos, qty, part_no, description, remarks, content_id) VALUES(?, ?, ?, ?, ?, ?)");
				if (content.getSectionDescription() != null) {
					for (Description desc : content.getSectionDescription().getDescription()) {
						p1.setString(1, desc.getPos());
						p1.setInt(2, desc.getQty());
						p1.setString(3, desc.getPartNo());
						p1.setString(4, desc.getDescription());
						p1.setString(5, desc.getRemark());
						p1.setLong(6, id);
						p1.addBatch();
					}
					p1.executeBatch();
				}
			}
		}
		return true;
	}

	// TODO: TO SAVE THE FILE INFORMATION TO THE DATABASE
	private Long save(String fileName) {
		String sql = "INSERT INTO file_v3(name, create_date) VALUES(?, GETDATE());";
		try (PreparedStatement pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);) {
			pstmt.setString(1, fileName);
			pstmt.executeUpdate();
			ResultSet rs = pstmt.getGeneratedKeys();
			if (rs != null && rs.next()) {
				return rs.getLong(1);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
			return 0L;
		}
		return 0L;
	}
}
