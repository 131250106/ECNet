package cn.edu.nju.ecm.file;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;

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
	public static void readModelFromFile(ECModel model) throws DocumentException {
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

	public static EBodyModel parseEBody(Element ebody) {
		String name = ebody.element("name").getText();
		String content = ebody.element("content").getText();
		EBody entityEBody = new EBody(name, content);
		
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
		boolean connected = Boolean.parseBoolean(eheader.element("connected").getText());
		int ownerID = 0;
		EHeader entityEHeader = new EHeader(name, content);
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
		headerModel.setConnectedOwner(connected);

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
			relationModel.setConnectedOwner(true);
		}
		if (sonID != -1) {
			relationModel.setConnectedSon(model.getElementByID(sonID));
			model.getElementByID(sonID).getConnectedInputs().add(relationModel);
			relationModel.setConnectedSon(true);
		}

		return relationModel;
	}

	public static void saveModelToFile(ECModel model, String filePath) throws IOException {
		// TODO Auto-generated method stub
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

}
