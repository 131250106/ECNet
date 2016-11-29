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
			return "ȷ��";
		case View:
			return "�޸�";
		default:
			return "ȷ��";
		}
	}

	public static String getTitleByModelType(OpenModal model, ElementType type) {
		String title = "";
		if (model == OpenModal.Create) {
			title += "�½�";
		} else if (model == OpenModal.Edit) {
			title += "�޸�";
		} else if (model == OpenModal.View) {
			title += "�鿴";
		} else {

		}

		if (type == ElementType.Model) {
			title += "֤����ģ��";
		} else if (type == ElementType.EBody) {
			title += "����";
		} else if (type == ElementType.HRelation) {
			title += "��ͷ��ϵ";
		} else if (type == ElementType.EHeader) {
			title += "��ͷ";
		} else if (type == ElementType.Connector) {
			title += "����";
		} else {

		}
		title += "����Ϣ";

		return title;
	}

}
