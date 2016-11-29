package cn.edu.nju.ecm.view.entity;

import java.awt.Rectangle;

import cn.edu.nju.ecm.view.ViewHelper;
import cn.edu.nju.ecm.view.entity.ElementDialog.ElementType;
import cn.edu.nju.ecm.view.entity.ElementDialog.OpenModal;
import cn.edu.nju.ecm.view.entity.panel.EBodyPanel;
import cn.edu.nju.ecm.view.entity.panel.ECMModelPanel;

public class ElementPanelFactory {

	public static ElementPanel getPanelByType(ElementType type) {
		ElementPanel elementPanel = null;
		switch (type) {
		case EBody:
			elementPanel = new EBodyPanel();
			break;
		case Model:
			elementPanel = new ECMModelPanel();
			break;
		default:
			break;
		}
		return elementPanel;
	}

	public static Rectangle getRecByType(ElementType type) {
		Rectangle rec = null;
		switch (type) {
		case EBody:
			rec = ViewHelper.getBounds(588, 400);
			break;
		case Model:
			rec = ViewHelper.getBounds(405, 245);
			break;
		default:
			break;
		}
		return rec;
	}

	public static String getButtonTextByOpenModel(OpenModal model) {
		switch (model) {
		case Create:
		case Edit:
			return "确定";
		case View:
			return "修改";
		default:
			return "确定";
		}
	}

	public static String getTitleByModelType(OpenModal model, ElementType type) {
		String title = "";
		if (model == OpenModal.Create) {
			title += "新建";
		} else if (model == OpenModal.Edit) {
			title += "修改";
		} else if (model == OpenModal.View) {
			title += "查看";
		} else {

		}

		if (type == ElementType.Model) {
			title += "证据链模型";
		} else if (type == ElementType.EBody) {
			title += "链体";
		} else if (type == ElementType.HRelation) {
			title += "链头关系";
		} else if (type == ElementType.EHeader) {
			title += "链头";
		} else if (type == ElementType.Connector) {
			title += "联结";
		} else {

		}
		title += "的信息";

		return title;
	}

}
