package kh.com.kshrd.v3.excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPicture;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFShape;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import kh.com.kshrd.v3.Content;
import kh.com.kshrd.v3.Description;
import kh.com.kshrd.v3.SectionDescription;
import kh.com.kshrd.v3.SectionImage;
import kh.com.kshrd.v3.repository.ConnectionManagement;
import kh.com.kshrd.v3.repository.ContentRepository;
import kh.com.kshrd.v3.threads.CopyExcel;
import kh.com.kshrd.v3.threads.ExportExcelToPDF;
import kh.com.kshrd.v3.threads.ImageConverter;

public class CargotecExcel {

	// TODO: For reading content from excel
	private List<Content> readContentList(String fileUrl, int sheetNumber, Integer[] startRow, Integer[] endRow)
			throws Exception {

		FileInputStream inputStream = new FileInputStream(new File(fileUrl));
		Workbook workbook = WorkbookFactory.create(inputStream);
		Sheet sheet = workbook.getSheetAt(sheetNumber);

		if (startRow.length != endRow.length)
			throw new Exception("[==> Message]: startRow & endRow does not match!!");

		List<Content> contentLists = new ArrayList<>();
		for (int i = 0; i < startRow.length; i++) {
			List<Content> contentList = readBetween(sheet, startRow[i], endRow[i]);
			contentLists.addAll(contentList);
		}
		return contentLists;
	}

	private List<Content> readBetween(Sheet sheet, int startRow, int endRow) {
		List<Content> contentList = new ArrayList<>();
		startRow = startRow - 1;
		endRow = endRow - 1;

		for (int row = startRow; row <= endRow; row++) {
			HSSFRow r = (HSSFRow) sheet.getRow(row);

			Content content = new Content();
			content.setChapter(((r.getCell(0) + "").replace(".0", "")));
			content.setDescription(r.getCell(1).toString());
			content.setRe(r.getCell(2).toString());
			content.setPage(r.getCell(3).toString());

			contentList.add(content);
		}
		return contentList;
	}

	private List<SectionImage> readSectionImages(String fileUrl, int sheetNumber, int startRow) throws Exception {
		List<SectionImage> sectionImages = null;
		try {
			FileInputStream inputStream = new FileInputStream(new File(fileUrl));
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(sheetNumber);
			sectionImages = readImageSections(inputStream, sheet, startRow);
			workbook.close();
			inputStream.close();

		} catch (Exception e) {
			System.err.println("[=>MESSAGE: " + e.getMessage() + "...]");
		}
		return sectionImages;
	}

	private List<SectionImage> readImageSections(FileInputStream inputStream, Sheet sheet, int startRow)
			throws Exception {
		List<SectionImage> sectionImageList = new ArrayList<>();
		List<String> images = readImage(inputStream, sheet);
		try {
			for (int start = startRow, i = 0; true; start += 72, i++) {
				SectionImage sectionImage = new SectionImage();

				// TODO: jump to section row
				HSSFRow r = (HSSFRow) sheet.getRow(start);
				String section = r.getCell(0).toString();

				// TODO: jump to section title
				r = (HSSFRow) sheet.getRow(start + 1);
				String title = r.getCell(0).toString();
				String number = r.getCell(5).toString();

				// TODO: jump to page row
				r = (HSSFRow) sheet.getRow(start + 35);
				String page = r.getCell(0).toString();

				sectionImage.setSection(section);
				sectionImage.setTitle(title);
				sectionImage.setNumber(number);
				sectionImage.setPage(page);
				sectionImage.setUrl(images.get(i));
				sectionImageList.add(sectionImage);
			}
		} catch (Exception e) {
			System.err.println("[=>MESSAGE: " + e.getMessage() + "...]");
		}
		return sectionImageList;
	}

	private List<String> readImage(FileInputStream inputStream, Sheet sheet) throws Exception {

		List<String> images = new ArrayList<>();

		// TODO: jump to image row
		HSSFPatriarch dravingPatriarch = (HSSFPatriarch) sheet.getDrawingPatriarch();
		List<HSSFShape> shapes = dravingPatriarch.getChildren();

		// TODO: TO LOOP THROUGH ALL THE IMAGES
		Map<Integer, Object> map = new TreeMap<>();

		for (HSSFShape shape : shapes) {
			if (shape instanceof HSSFPicture) {
				HSSFPicture hssfPicture = (HSSFPicture) shape;
				int row = hssfPicture.getClientAnchor().getRow1();
				map.put(row, hssfPicture);
			}
		}
		String fileLocation = System.getProperty("user.home") + File.separator + "CARGOTEC" + File.separator + "images";
		String pngLocation =  fileLocation + File.separator + "PNG"; 
				
		if (!new File(fileLocation).exists()) {
			new File(fileLocation).mkdirs();
		}
		if (!new File(pngLocation).exists()) {
			new File(pngLocation).mkdirs();
		}
		
		int i = 0;
		Iterator imageList = map.entrySet().iterator();
		while (imageList.hasNext()) {
			Map.Entry entry = (Map.Entry) imageList.next();
			String key = entry.getKey() + "";

			HSSFPicture image = (HSSFPicture) entry.getValue();
			PictureData picture = (PictureData) image.getPictureData();
			String ext = picture.suggestFileExtension();
			byte[] data = picture.getData();
			FileOutputStream out = null;
			i++;

			String fileName = "PIC_" + i + "." + ext;
			String fileDestination = fileLocation + File.separator + fileName;

			out = new FileOutputStream(fileDestination);
			out.write(data);
			out.close();
			
			//Convert EMF file to PNG file
			String fName = "PIC_" + i + ".PNG";
			String pngOutput = pngLocation + File.separator + fName;
			ImageConverter.convertEMF(fileDestination, pngOutput);
			
			images.add(pngOutput);
		}
		
		return images;
	}

	private List<SectionDescription> readSectionDescriptions(String fileUrl, int sheetNumber, int startRow)
			throws Exception {
		List<SectionDescription> sectionDescriptions = new ArrayList<>();
		try {
			FileInputStream inputStream = new FileInputStream(new File(fileUrl));
			Workbook workbook = WorkbookFactory.create(inputStream);
			Sheet sheet = workbook.getSheetAt(sheetNumber);

			while (true) {
				SectionDescription sd = this.readSectionDescription(sheet, startRow);
				sectionDescriptions.add(sd);
				// TODO: jump to next description
				startRow += 72;
			}
		} catch (Exception e) {
			System.err.println("[=>MESSAGE: " + e.getMessage() + "...]");
		}
		return sectionDescriptions;
	}

	private SectionDescription readSectionDescription(Sheet sheet, int startRow) throws Exception {
		try {
			// System.out.println("=>> START ROW: " + startRow);
			SectionDescription sectionDescription = new SectionDescription();

			// TODO: jump to section row
			HSSFRow r = (HSSFRow) sheet.getRow(startRow);
			String section = r.getCell(0).toString();

			// TODO: jump to description row
			int descriptionMetaRow = startRow + 1;
			r = (HSSFRow) sheet.getRow(descriptionMetaRow);
			String title = r.getCell(0).toString();
			String number = r.getCell(5).toString();

			int descriptionRow = descriptionMetaRow + 2;
			r = (HSSFRow) sheet.getRow(descriptionRow);

			List<Description> descriptions = new ArrayList<>();

			try {
				for (int row = descriptionRow; r.getCell(0).toString() != ""; row++) {
					Description description = new Description();
					description.setPos(r.getCell(0).toString());
					description.setQty(Integer.parseInt((r.getCell(1).toString()).replace(".0", "")));
					description.setPartNo(r.getCell(2).toString());
					description.setDescription(r.getCell(3).toString());
					description.setRemark(r.getCell(4).toString());
					descriptions.add(description);
					r = (HSSFRow) sheet.getRow(row + 1);
				}
			} catch (Exception e) {
				System.err.println("[=>MESSAGE: " + e.getMessage() + "...]");
			}
			// TODO: jump page row
			r = (HSSFRow) sheet.getRow(startRow + 35);

			sectionDescription.setPage(r.getCell(0).toString());
			sectionDescription.setDescription(descriptions);
			sectionDescription.setSection(section);
			sectionDescription.setTitle(title);
			sectionDescription.setNumber(number);

			return sectionDescription;
		} catch (Exception e) {
			throw new Exception("End Of Description");
		}
	}

	public List<Content> combine(String fileUrl) throws Exception {

		// TODO: Get Content List
		List<Content> contentList = new CargotecExcel().readContentList(fileUrl, 1, new Integer[] { 3, 39 },
				new Integer[] { 34, 70 });
		// TODO: Get Section Description
		List<SectionDescription> sectionDescriptions = readSectionDescriptions(fileUrl, 0, 36);
		// TODO: Get Section Image
		List<SectionImage> sectionImages = readSectionImages(fileUrl, 0, 0);

		for(Content content: contentList){
			try {
				findDescriptionByPage(sectionDescriptions, content);
				findImageByPage(sectionImages, content);
			} catch (Exception e) {
				System.err.println("[=>MESSAGE: " + e.getMessage() + "...[Method: combine(String)]]");
			}
		}
		
		return contentList;
	}

	private void findDescriptionByPage(List<SectionDescription> sectionDescriptions, Content content) throws Exception {
		
		for(SectionDescription sd: sectionDescriptions){
			if (sd.getPage().equals(content.getPage()))
				content.setSectionDescription(sd);
		}
	}

	private void findImageByPage(List<SectionImage> sectionImages, Content content) {
		for(SectionImage si: sectionImages){
			String contentPage = content.getPage();
			String sectionPage = si.getPage();
			if (sectionPage.contains(".")) {
				contentPage = contentPage.substring(0, contentPage.indexOf("-")).trim();
				sectionPage = sectionPage.substring(0, sectionPage.indexOf(".")).trim();
			}
			if (sectionPage.equals(contentPage)) {
				content.setSectionImage(si);
			}
		}
	}

	public static void main(String[] args) throws Exception {
		
		System.out.println("[=>>Starting...]");
		
		new PropertiesManagement();
		// String fileUrl = "D:\\Cargotec\\cargotec\\HIAB18000XGR(201603).xls";
		
		ConnectionManagement.getConnection().setAutoCommit(false);
		try {
			if (args.length > 0) {
				ExportExcelToPDF exportExcelToPDF = new ExportExcelToPDF(args[0]);
				exportExcelToPDF.start();
				CopyExcel copyExcel = new CopyExcel(args[0]);
				copyExcel.start();

				List<Content> contentList = new CargotecExcel().combine(args[0]);

				ContentRepository contentRepository = new ContentRepository();
				contentRepository.save(contentList, args[0]);

				ConnectionManagement.getConnection().commit();
			} else {
				System.out.println("PLEASE ENTER YOUR EXCEL FILE...");
				System.err.println();
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				ConnectionManagement.getConnection().rollback();
				System.err.println("YOU HAVE BEEN SOME ERRORS...");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		ConnectionManagement.closeConnection();
	}
}
