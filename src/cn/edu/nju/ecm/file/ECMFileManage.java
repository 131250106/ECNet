package cn.edu.nju.ecm.file;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import cn.edu.nju.ecm.canvas.model.CanvasElement;
import cn.edu.nju.ecm.canvas.model.ECModel;
import cn.edu.nju.ecm.canvas.model.entity.ConnectorModel;
import cn.edu.nju.ecm.canvas.model.entity.EBodyModel;
import cn.edu.nju.ecm.canvas.model.entity.EHeaderModel;
import cn.edu.nju.ecm.canvas.model.entity.HRelationModel;
import cn.edu.nju.ecm.entity.detail.EBody;
import cn.edu.nju.ecm.entity.detail.EHeader;
import cn.edu.nju.ecm.entity.detail.HConnector;
import cn.edu.nju.ecm.entity.detail.HRelation;
import cn.edu.nju.ecm.utility.EvidenceFileTypeManagement;

public class ECMFileManage {

	public static String NEW_LINE = System.getProperty("line.separator");

	@SuppressWarnings("unchecked")
	public static void readModelFromECMFile(ECModel model) throws DocumentException {
		SAXReader reader = new SAXReader();
		reader.setEncoding("gb2312");
		Document doc = reader.read(model.getFile());
		Element root = doc.getRootElement();
		String title = root.element("title").getText();
		String description = root.element("description").getText();
		int maxElementID = Integer.parseInt(root.element("maxID").getText());
		model.setTitle(title);
		model.setDescription(description);
		model.setMaxIDNumber(maxElementID);

		List<Element> ebodys = root.elements("ebody");
		for (Element ebody : ebodys) {
			model.getElements().add(parseEBody(ebody));
		}

		List<Element> eheaerds = root.elements("eheader");
		for (Element eheader : eheaerds) {
			model.getElements().add(parseEHeader(eheader, model));
		}

		List<Element> connectors = root.elements("connector");
		for (Element connector : connectors) {
			model.getElements().add(parseConnector(connector));
		}

		List<Element> hrelations = root.elements("hrelation");
		for (Element hrelation : hrelations) {
			model.getElements().add(parseHRelation(hrelation, model));
		}

	}
	
	@SuppressWarnings("resource")
	public static void readModelFromXLSFile(ECModel model) throws IOException{
		InputStream stream = new FileInputStream(model.getFile());  
		
		Workbook wb = new HSSFWorkbook(stream);  
        
        Sheet sheet1 = wb.getSheetAt(0);  
        
        int beginId = -1;
        for(int i=2;i<sheet1.getLastRowNum()+1;i++){
        	Row row = sheet1.getRow(i);
        	
        	Cell cell = row.getCell(0);
        	int currentId = Integer.parseInt(cell.getStringCellValue());
        	if(i==2){
				beginId = Integer.parseInt(cell.getStringCellValue());
			}else if(currentId==beginId){
				continue;
			}else{
				beginId = currentId;
			}
        	String name = row.getCell(1).getStringCellValue();
    		String content = row.getCell(2).getStringCellValue();
    		String evidenceType = row.getCell(3).getStringCellValue();
    		String commiter = row.getCell(4).getStringCellValue();
    		String evidenceReason = row.getCell(5).getStringCellValue();
    		String evidenceConclusion = row.getCell(6).getStringCellValue();
    		EBody entityEBody = new EBody(name, content, evidenceType, commiter, evidenceReason, evidenceConclusion);
    		model.getElements().add(new EBodyModel(0, 0, 30, 80, currentId, entityEBody));
        }
        Sheet sheet2 = wb.getSheetAt(1);  
        
        beginId = -1;
        ConnectorModel connectorModel = null;
        for(int i=2;i<sheet2.getLastRowNum()+1;i++){
        	Row row = sheet2.getRow(i);
        	
        	Cell cell = row.getCell(0);
        	int currentId = Integer.parseInt(cell.getStringCellValue());
        	
        	if(i==2||currentId!=beginId){
				beginId = currentId;
				String name = row.getCell(1).getStringCellValue();
	    		String content = row.getCell(2).getStringCellValue();
	    		HConnector entityHConnector = new HConnector(name, content);
	    		connectorModel = new ConnectorModel(0, 0, 30, 30, currentId, entityHConnector);
	    		model.getElements().add(connectorModel);
			}else if(currentId==beginId){
			}
        	String temp = row.getCell(4).getStringCellValue();
        	if(!temp.equals("")){
        		int bodyId = Integer.parseInt(temp);
        		CanvasElement body = model.getElementByID(bodyId);
        		if(body!=null){			//暂时不判断是否有重复的链头
        			//1.创建链头
        			String name = "";
        			String content = row.getCell(3).getStringCellValue();
	    			String keySentence = row.getCell(5).getStringCellValue();
	    			EHeader entityEHeader = new EHeader(name, content, keySentence);
	    			EHeaderModel headerModel = new EHeaderModel(0, 0, 0, 0, model.getMaxId()+1, entityEHeader);
	    			//2.建立链头与链体之间的关系
	    			headerModel.setConnectedOwner(body);
	    			model.getElements().add(headerModel);
	    			//3.创建箭头
	    			content = "";
	    			HRelation entityHRealtion = new HRelation(name, content);
	    			HRelationModel relationModel = new HRelationModel(0, 0, 0, 0, 0, entityHRealtion);
	    			//4.建立箭头与链头，连接点的关系
	    			relationModel.setConnectedOwner(headerModel);
	    			relationModel.setConnectedSon(connectorModel);
	    			model.getElements().add(relationModel);
        		}
        	}
        }
        model.autoFormat();
	}

	public static EBodyModel parseEBody(Element ebody) {
		String name = ebody.element("name").getText();
		String content = ebody.element("content").getText();
		String evidenceType = ebody.element("evidenceType").getText();
		String commiter = ebody.element("commiter").getText();
		String evidenceReason = ebody.element("evidenceReason").getText();
		String evidenceConclusion = ebody.element("evidenceConclusion").getText();
		EBody entityEBody = new EBody(name, content, evidenceType, commiter, evidenceReason, evidenceConclusion);
		
		@SuppressWarnings("unchecked")
		List<Element> evidenceFiles = ebody.element("files").elements();
		for (Element file : evidenceFiles) {
			String type = file.attributeValue("type");
			String filePath = file.getText();
			entityEBody.getEvidenceFilePathsAndType().put(filePath, EvidenceFileTypeManagement.decodeDirectly(type));
		}

		int x = Integer.parseInt(ebody.attributeValue("x"));
		int y = Integer.parseInt(ebody.attributeValue("y"));
		int height = Integer.parseInt(ebody.attributeValue("height"));
		int width = Integer.parseInt(ebody.attributeValue("width"));
		int id = Integer.parseInt(ebody.attributeValue("id"));

		return new EBodyModel(x, y, height, width, id, entityEBody);
	}

	public static EHeaderModel parseEHeader(Element eheader, ECModel model) {
		String name = eheader.element("name").getText();
		String content = eheader.element("content").getText();
		String keySentence = eheader.element("keySentence").getText();
		int ownerID = 0;
		EHeader entityEHeader = new EHeader(name, content, keySentence);
		if (eheader.element("ownerID") != null) {
			ownerID = Integer.parseInt(eheader.element("ownerID").getText());
		}

		int x1 = Integer.parseInt(eheader.attributeValue("x1"));
		int y1 = Integer.parseInt(eheader.attributeValue("y1"));
		int x2 = Integer.parseInt(eheader.attributeValue("x2"));
		int y2 = Integer.parseInt(eheader.attributeValue("y2"));
		int id = Integer.parseInt(eheader.attributeValue("id"));

		EHeaderModel headerModel = new EHeaderModel(x1, y1, x2, y2, id, entityEHeader);
		if (ownerID != 0) {
			headerModel.setConnectedOwner(model.getElementByID(ownerID));
			model.getElementByID(ownerID).getConnectedOutputs().add(headerModel);
		}

		return headerModel;
	}

	public static ConnectorModel parseConnector(Element connector) {
		String name = connector.element("name").getText();
		String content = connector.element("content").getText();
		HConnector entityHConnector = new HConnector(name, content);

		int x = Integer.parseInt(connector.attributeValue("x"));
		int y = Integer.parseInt(connector.attributeValue("y"));
		int height = Integer.parseInt(connector.attributeValue("height"));
		int width = Integer.parseInt(connector.attributeValue("width"));
		int id = Integer.parseInt(connector.attributeValue("id"));

		return new ConnectorModel(x, y, height, width, id, entityHConnector);
	}

	public static HRelationModel parseHRelation(Element hRelation, ECModel model) {
		String name = hRelation.element("name").getText();
		String content = hRelation.element("content").getText();
		int ownerID = -1, sonID = -1;
		HRelation entityHRealtion = new HRelation(name, content);
		if (hRelation.element("ownerID") != null) {
			ownerID = Integer.parseInt(hRelation.element("ownerID").getText());
		}
		if (hRelation.element("sonID") != null) {
			sonID = Integer.parseInt(hRelation.element("sonID").getText());
		}

		int x1 = Integer.parseInt(hRelation.attributeValue("x1"));
		int y1 = Integer.parseInt(hRelation.attributeValue("y1"));
		int x2 = Integer.parseInt(hRelation.attributeValue("x2"));
		int y2 = Integer.parseInt(hRelation.attributeValue("y2"));
		int id = Integer.parseInt(hRelation.attributeValue("id"));

		HRelationModel relationModel = new HRelationModel(x1, y1, x2, y2, id, entityHRealtion);
		if (ownerID != -1) {
			relationModel.setConnectedOwner(model.getElementByID(ownerID));
			model.getElementByID(ownerID).getConnectedOutputs().add(relationModel);
		}
		if (sonID != -1) {
			relationModel.setConnectedSon(model.getElementByID(sonID));
			model.getElementByID(sonID).getConnectedInputs().add(relationModel);
		}

		return relationModel;
	}

	public static void saveModelToFile(ECModel model, String filePath) throws IOException {
		BufferedWriter modelWriter = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(filePath), "gb2312"));
		modelWriter.append("<ECMModel>" + ECMFileManage.NEW_LINE);

		modelWriter.append("\t<title>" + model.getTitle() + "</title>" + ECMFileManage.NEW_LINE);
		modelWriter.append("\t<description>" + model.getDescription() + "</description>" + ECMFileManage.NEW_LINE);
		modelWriter.append("\t<maxID>" + model.getMaxIDNumber() + "</maxID>" + ECMFileManage.NEW_LINE);

		modelWriter.flush();
		for (CanvasElement ce : model.getElements()) {
			modelWriter.append(ce.toXMLString());
			modelWriter.flush();
		}
		modelWriter.append("</ECMModel>");
		modelWriter.flush();
		modelWriter.close();
	}
	
	@SuppressWarnings("unchecked")
	public static void readFileToModel(ECModel model) throws DocumentException {				//TODO 待测试！！
		SAXReader reader = new SAXReader();
		reader.setEncoding("gb2312");
		Document doc = reader.read(model.getFile());
		Element root = doc.getRootElement();
		String title = root.element("title").getText();
		String description = root.element("description").getText();
		model.setTitle(title);
		model.setDescription(description);
		
		List<Element> connectors = root.elements("fact");
		for (Element connector : connectors) {
			//1.解析事实信息（也就是联结点）
			String name = connector.element("name").getText();
			String content = connector.element("content").getText();
			HConnector entityHConnector = new HConnector(name, content);
			int id = Integer.parseInt(connector.attributeValue("id"));
			CanvasElement connectorModel = new ConnectorModel(0, 0, 30, 30, id, entityHConnector);
			model.getElements().add(connectorModel);
			
			//2.解析事实信息中的链体信息
			List<Element> ebodys = connector.elements("ebody");
			for (Element ebody : ebodys) {
				//2.1 解析链体信息
				name = ebody.element("name").getText();
				content = ebody.element("content").getText();
				String evidenceType = ebody.element("evidenceType").getText();
				String commiter = ebody.element("commiter").getText();
				String evidenceReason = ebody.element("evidenceReason").getText();
				String evidenceConclusion = ebody.element("evidenceConclusion").getText();
				EBody entityEBody = new EBody(name, content, evidenceType, commiter, evidenceReason, evidenceConclusion);
				id = Integer.parseInt(ebody.attributeValue("id"));
				CanvasElement bodyModel = new EBodyModel(0, 0, 30, 80, id, entityEBody);
				model.getElements().add(bodyModel);
				
				//2.2 解析连体中的链头信息
				List<Element> eheaerds = ebody.elements("eheader");
				for (Element eheader : eheaerds) {
					//2.2.1 解析链头信息
					name = eheader.element("name").getText();
					content = eheader.element("content").getText();
					String keySentence = eheader.element("keySentence").getText();
					EHeader entityEHeader = new EHeader(name, content, keySentence);
					id = Integer.parseInt(eheader.attributeValue("id"));
					CanvasElement headerModel = new EHeaderModel(0, 0, 0, 0, id, entityEHeader);
					//2.2.2 将链头绑定在链体上
					headerModel.setConnectedOwner(bodyModel);
					model.getElements().add(headerModel);
					//2.2.3 创建箭头 
	    			HRelation entityHRealtion = new HRelation("", "");
	    			HRelationModel relationModel = new HRelationModel(0, 0, 0, 0, 0, entityHRealtion);
	    			//2.2.4 通过箭头将链头与联结点关联起来
	    			relationModel.setConnectedOwner(headerModel);
	    			relationModel.setConnectedSon(connectorModel);
	    			model.getElements().add(relationModel);
				}
			}
		}
		model.setMaxIDNumber(model.getMaxId()+1);
	}

}
