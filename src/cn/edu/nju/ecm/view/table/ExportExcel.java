package cn.edu.nju.ecm.view.table;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExportExcel {

	private JTable table1;
	private JTable table2;
	private FileOutputStream fos;
	private JFileChooser jfc = new JFileChooser();
	private HSSFWorkbook wb;

	public ExportExcel(JTable table1, JTable table2) {
		this.table1 = table1;
		this.table2 = table2;
		jfc.addChoosableFileFilter(new FileFilter() {
			public boolean accept(File file) {
				return (file.getName().indexOf("xls") != -1);
			}

			public String getDescription() {
				return "Excel";
			}
		});

		jfc.showSaveDialog(null);
		File file = jfc.getSelectedFile();
		try {
			this.fos = new FileOutputStream(file + ".xls");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
	}

	public void export() {
		wb = new HSSFWorkbook();
		exportEvidence();
		exportFact();
		try {
			wb.write(fos);
			fos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private void exportFact() {
		HSSFSheet hs = wb.createSheet("事实清单");

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直

		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		HSSFFont font = wb.createFont(); 
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);// 设置字体大小
        titleStyle.setFont(font);
        
		TableModel tm2 = table2.getModel();
		int row2 = tm2.getRowCount();
		int cloumn2 = tm2.getColumnCount() - 1;

		CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 5);
		hs.addMergedRegion(cra);
		HSSFRow hrTitle2 = hs.createRow(0);
		String title2 = "事实清单";
		HSSFRichTextString titles2 = new HSSFRichTextString(title2);
		HSSFCell hcTitle2 = hrTitle2.createCell(0);
		hcTitle2.setCellValue(titles2);
		hcTitle2.setCellStyle(titleStyle);

		int beginRow = 0; // 起始终示合并row
		int beginId = 0; // 起始终示合并ID
		for (int i = 0; i < row2; i++) {
			HSSFRow hr = hs.createRow(i+1);
			for (int j = 0; j < cloumn2; j++) {
				if (i == 0) {
					String value = tm2.getColumnName(j);
					int len = value.length();
					hs.setColumnWidth((short) j, (short) (len * 800));
					HSSFRichTextString srts = new HSSFRichTextString(value);
					HSSFCell hc = hr.createCell((short) j);
					hc.setCellValue(srts);
					hc.setCellStyle(cellStyle);
				} else {
					if (i == 1 && j == 0) {
						beginRow = i+1;
						if (tm2.getValueAt(i - 1, 0) != null) {
							beginId = ((int) tm2.getValueAt(i - 1, 0));
						}
					} else if (i == row2 - 1 && j <= 2) {
						cra = new CellRangeAddress(beginRow, i+1, j, j);
						hs.addMergedRegion(cra);
					} else if (tm2.getValueAt(i - 1, 0) != null
							&& ((int) tm2.getValueAt(i - 1, 0)) != beginId) {
						cra = new CellRangeAddress(beginRow, i, j, j);
						hs.addMergedRegion(cra);
						if (j == 2) {
							beginId = ((int) tm2.getValueAt(i - 1, 0));
							beginRow = i+1;
						}
					}
					if (tm2.getValueAt(i - 1, j) != null) {
						String value = tm2.getValueAt(i - 1, j).toString();
						HSSFRichTextString srts = new HSSFRichTextString(value);
						HSSFCell hc = hr.createCell((short) j);
						if (value.equals("") || value == null) {
							hc.setCellValue(new HSSFRichTextString(""));
						} else {
							hc.setCellValue(srts);
							hc.setCellStyle(cellStyle);
						}
					}
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	private void exportEvidence() {
		HSSFSheet hs = wb.createSheet("证据清单");

		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直

		HSSFCellStyle titleStyle = wb.createCellStyle();
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中
		titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
		HSSFFont font = wb.createFont(); 
        font.setFontName("宋体");
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);// 设置字体大小
        titleStyle.setFont(font);
        
		TableModel tm1 = table1.getModel();
		int row1 = tm1.getRowCount();
		int cloumn1 = tm1.getColumnCount() - 1;

		CellRangeAddress cra = new CellRangeAddress(0, 0, 0, 8);
		// 在sheet里增加合并单元格
		hs.addMergedRegion(cra);
		HSSFRow hrTitle = hs.createRow(0);
		String title = "证据清单";
		HSSFRichTextString titles = new HSSFRichTextString(title);
		HSSFCell hcTitle = hrTitle.createCell(0);
		hcTitle.setCellValue(titles);
		hcTitle.setCellStyle(titleStyle);

		int beginRow = 0; // 起始终示合并row
		int beginId = 0; // 起始终示合并ID
		for (int i = 0; i < row1; i++) {
			HSSFRow hr = hs.createRow(i + 1);
			for (int j = 0; j < cloumn1; j++) {
				if (i == 0) {
					String value = tm1.getColumnName(j);
					int len = value.length();
					hs.setColumnWidth((short) j, (short) (len * 800));
					HSSFRichTextString srts = new HSSFRichTextString(value);
					HSSFCell hc = hr.createCell((short) j);
					hc.setCellValue(srts);
					hc.setCellStyle(cellStyle);
				} else {
					if (i == 1 && j == 0) {
						beginRow = i + 1;
						if (tm1.getValueAt(i - 1, 0) != null) {
							beginId = ((int) tm1.getValueAt(i - 1, 0));
						}
					} else if (i == row1 - 1 && j <= 6) {
						cra = new CellRangeAddress(beginRow, i + 1, j, j);
						hs.addMergedRegion(cra);
					} else if (tm1.getValueAt(i - 1, 0) != null
							&& ((int) tm1.getValueAt(i - 1, 0)) != beginId) {
						cra = new CellRangeAddress(beginRow, i, j, j);
						hs.addMergedRegion(cra);
						if (j == 6) {
							beginId = ((int) tm1.getValueAt(i - 1, 0));
							beginRow = i + 1;
						}
					}
					if (tm1.getValueAt(i - 1, j) != null) {
						String value = tm1.getValueAt(i - 1, j).toString();
						HSSFRichTextString srts = new HSSFRichTextString(value);
						HSSFCell hc = hr.createCell((short) j);
						if (value.equals("") || value == null) {
							hc.setCellValue(new HSSFRichTextString(""));
						} else {
							hc.setCellValue(srts);
							hc.setCellStyle(cellStyle);
						}
					}
				}
			}
		}
	}
}
