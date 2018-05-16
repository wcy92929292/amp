package com.udbac.util;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.ClientAnchor.AnchorType;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 操作XLSX文件工具类
 * @author LFQ
 * @DATE 2016-04-11
 */
public class XlsxUtil {

	private XSSFWorkbook xssfWorkbook;
	private XSSFSheet sheet;
	private String filePath;
	
	//边框
	public static final short BORDER_TOP = 0;		//上边框
	public static final short BORDER_RIGHT = 1;	//右边框
	public static final short BORDER_BOTTOM = 2;	//下边框
	public static final short BORDER_LEFT = 3;	//左边框
	public static final short BORDER_ALL = 4;		//所有边框
	
	public static final short BORDER_THIN = CellStyle.BORDER_THIN;	//细边框
	public static final short BORDER_DASHED = CellStyle.BORDER_DASHED;	//虚边框
	public static final short BORDER_MEDIUM = CellStyle.BORDER_MEDIUM;	//粗边框
	
	//对齐
	public static final short ALIGN_CENTER = CellStyle.ALIGN_CENTER;//中
	public static final short ALIGN_LEFT = CellStyle.ALIGN_LEFT;	//左
	public static final short ALIGN_RIGHT = CellStyle.ALIGN_RIGHT;	//右
	public static final short ALIGN_VERTICAL_CENTER = CellStyle.VERTICAL_CENTER;	//垂直居中
	
	//值类型
	public static final short CELL_BOOLEAN = Cell.CELL_TYPE_BOOLEAN;	//boolean
	public static final short CELL_STRING = Cell.CELL_TYPE_STRING;		//String
	public static final short CELL_FORMULA = Cell.CELL_TYPE_FORMULA;	//公式型
	public static final short CELL_NUMERIC = Cell.CELL_TYPE_NUMERIC;	//数字
	
	//字体粗细
	public static final short FONT_BOLD = HSSFFont.BOLDWEIGHT_BOLD;		//粗体
	public static final short FONT_NORMAL = HSSFFont.BOLDWEIGHT_NORMAL;	//正常
	
	//颜色：
	public static final int[] COLOR_WHITE = {255,255,255};	//白色
	public static final int[] COLOR_BLACK = {0,0,0};		//白色
	
	//字体
	public static final String FONT_NAME_SONGTI = "宋体";			//宋体
	public static final String FONT_NAME_CALIBRI = "Calibri";	//Calibri
	
	//数字格式样式
	public static final String NUMBER_FORMAT_THOUSANDS = "#,##0";	//千分位
	public static final String NUMBER_FORMAT_PERCENT = "0.00%";	//两位百分比
	public static final String NUMBER_FORMAT_TWO_DECIMALS = "0.00";	//两位小数
	
	
	public static final String Y = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	
	FileOutputStream fos = null;
	
	/**
	 * 初始化工作簿,如果存在文件则读取，否则新建
	 * @param filePath  Excel文件路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public XlsxUtil(String filePath) throws FileNotFoundException,IOException{
		
		this.filePath = filePath;
		System.out.println(filePath);
		File f = new File(filePath);
		if(f.exists()){
			xssfWorkbook = new XSSFWorkbook(new FileInputStream(filePath));
			try{
				sheet = xssfWorkbook.getSheetAt(0);
			}catch(Exception e){
				sheet = xssfWorkbook.createSheet();
			}
		}else{
			xssfWorkbook = new XSSFWorkbook();
			sheet = xssfWorkbook.createSheet();
		}
	}//end Constructor()
	
	public XlsxUtil(XSSFWorkbook xssfWorkbook){
		this.xssfWorkbook = xssfWorkbook;
		try{
			this.sheet = xssfWorkbook.getSheetAt(0);
		}catch(Exception e){
			sheet = xssfWorkbook.createSheet();
		}
	}
	
	
	/**
	 * 根据文件流创建
	 * @param fis
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public XlsxUtil(InputStream fis) throws FileNotFoundException,IOException{
		xssfWorkbook = new XSSFWorkbook(fis);
		sheet = xssfWorkbook.getSheetAt(0);
	}
	
	
	
	/**
	 * 选择指定sheet进行操作
	 *  @param 
	 *      int n  :  第几个sheet
	 *      String newName : 设置sheet的新名称,  为null 时不设置名称
	 */
	public void setSheet(int n,String newName){
		
		try{
			sheet = xssfWorkbook.getSheetAt(n);
		}catch(Exception e){
			sheet = xssfWorkbook.createSheet();
		}
		
		if(newName != null){
			xssfWorkbook.setSheetName(n,newName);
		}
	}//end setSheet
	
	
	/**
	 * 设置指定单元格背景颜色
	 * @param row		从0开始算第几行
	 * @param column	从0开始算第几列
	 * @param rgb		RGB颜色值
	 */
	public XSSFCellStyle setCellBgColor(int row,int column,int[] rgb,boolean ... isFlush){
		XSSFCellStyle cellStyle = getCellStyle(row,column,isFlush);
		
		setCellBgColor(cellStyle, rgb);
		
		getCell(row, column).setCellStyle(cellStyle);
		
		return cellStyle;
	}//end setCellBgColor
	
	/**
	 * 设置背景颜色
	 * @param cellStyle
	 * @param rgb
	 * @return
	 */
	public XSSFCellStyle setCellBgColor(XSSFCellStyle cellStyle,int[] rgb){
		
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle();
		}
		
		cellStyle.setFillForegroundColor(new XSSFColor(new Color(rgb[0], rgb[1], rgb[2])));
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		return cellStyle;
	}//end setCellBgColor
	
	private XSSFCellStyle getCellStyle(int row,int column,boolean[] isFlush){
		XSSFCellStyle cellStyle = null;
		if(isFlush.length == 0 || isFlush[0] == false){
			cellStyle = getCell(row, column).getCellStyle();
		}else{
			cellStyle = xssfWorkbook.createCellStyle();
		}
		return cellStyle;
	}//end getCellStyle
	
	/**
	 * 创建样式
	 * @param bgColor	背景颜色 RGB
	 * @param format	内容格式化
	 * @param fontName	字体名称
	 * @param fontSize	字体大小
	 * @param fontColor	字体颜色
	 * @param fontBold	字体粗细
	 * @param align		水平居中
	 * @param valign	垂直居中
	 * @param borderStyle	边框样式
	 * @param border		边框位置
	 */
	public XSSFCellStyle createCellStyle(
				int[] bgColor,String format,
				String fontName,Short fontSize,int[] fontColor,Short fontBold,
				Short align,Short valign,
				Short borderStyle,short... border){
			
			XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
			
			if(bgColor != null && bgColor.length == 3){
				setCellBgColor(cellStyle, bgColor);
			}
			
			if(format != null &&  !"".equals(format)){
				format(cellStyle,format);
			}
			
			if(fontName != null && !"".equals(fontName)){
				setFont(cellStyle, fontName,fontSize);
			}
			
			if(align != null){
				setAlign(cellStyle,align);
			}
			if(valign != null){
				setAlign(cellStyle,valign);
			}
			
			if(bgColor != null){
				setCellBgColor(cellStyle,bgColor);
			}
			
			if(fontColor != null){
				setFontColor(cellStyle,fontColor);
			}
			
			if(borderStyle != null && border != null){
				//setMergedRegionBorder(x,y,borderStyle,border);
				setBorder(cellStyle, borderStyle, border);
			}
			
			
			if(fontBold != null){
				setFontBold(cellStyle, fontBold);
			}
			
			return cellStyle;
	}
	
	/**
	 * 克隆样式
	 * @param row		目标行
	 * @param column	目标列
	 * @param cellStyle	样式
	 */
	public void cloneCellStyle(int row,int column,XSSFCellStyle cellStyle){
		XSSFCell cell = getCell(row, column);
		cell.setCellStyle(cellStyle);
	}//end cloneCellStyle
	
	/**
	 * 设置默认列样式
	 * @param indexColumn	目标列
	 * @param style			目标行
	 */
	public void setDefaultColumnStyle(int indexColumn,CellStyle style){
		sheet.setDefaultColumnStyle(indexColumn, style);
	}//end setDefaultColumnStyle()
	
	/**
	 * 格式化单元格数值格式
	 * @return
	 */
//	public XSSFCellStyle format(String fmt,XSSFCellStyle cellStyle){
//		XSSFDataFormat format = xssfWorkbook.createDataFormat();
//		
//		if(cellStyle == null){
//			cellStyle = xssfWorkbook.createCellStyle();
//		}
//		
//		cellStyle.setDataFormat(format.getFormat("#,###")); 
//		return cellStyle;
//	}
	
	/**
	 * 设置目标单元格内容格式
	 * @param r
	 * @param c
	 * @param fmt
	 */
	public XSSFCellStyle format(int r,int c,String fmt,boolean ... isFlush){
		
		XSSFCellStyle cellStyle = getCellStyle(r, c, isFlush);
		
		format(cellStyle,fmt);
		
		getCell(r, c).setCellStyle(cellStyle);
		return cellStyle;
	}//end format
	
	/**
	 * 内容格式化
	 * @param cellStyle	
	 * @param fmt
	 * @return
	 */
	public XSSFCellStyle format(XSSFCellStyle cellStyle,String fmt){
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle(); 
		}
		if(fmt.contains("y") || fmt.contains("m") || fmt.contains("d") ||
			fmt.contains("h") || fmt.contains("s") || fmt.contains("$") ||
			fmt.contains("￥")){
			//日期yyyy年m月d日  货币  ￥#,##0
			cellStyle.setDataFormat(xssfWorkbook.createDataFormat().getFormat(fmt));
		}else{
			//数字  0.00   百分比  0.00%  科学计数法  0.00E+00
			cellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat(fmt));
		}
		
		return cellStyle;
	}//end format
	
	/**
	 * 获取当前SHEET最后一行的行号
	 * @return
	 */
	public int getLastRow(){
		return sheet.getLastRowNum();
	}//end getLastRow
	
	/**
	 * 获取指定行最后一列
	 * @return
	 */
	public int getLastCell(int rowNum){
		XSSFRow row = sheet.getRow(rowNum);
		
		if(row == null){
			return 0;
		}
		return row.getLastCellNum();
	}//end getLastCell
	
	/**
	 * 获取整个sheet的最后一列
	 * @return
	 */
	public int getLastCell(){
		int lastRow = getLastRow();
		int lastCell = 0;
		int cell;
		for (int i = 0; i < lastRow; i++) {
			cell = getLastCell(i);
			if(cell > lastCell){
				lastCell = cell;
			}
		}
		return lastCell;
	}//end getLastCell
	
	/**
	 * 图片未缩放进行插入，
	 * @param col1	图片左上角坐标
	 * @param row1	图片右上角坐标
	 * @param imgPath	图片路径
	 */
	public void insertImage(int row1,int col1,int row2,int col2,String imgPath) {  
	         BufferedImage bufferImg = null;     
	        //先把读进来的图片放到一个ByteArrayOutputStream中，以便产生ByteArray    
	        try {  
	            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();     
	            bufferImg = ImageIO.read(new File(imgPath));
	            String imgType = "";
	            int pictureType = 0;
	            
	            if(imgPath.endsWith("jpg") || imgPath.endsWith("JPG") || imgPath.endsWith("JPEG") || imgPath.endsWith("jpeg")){
	            	imgType = "jpg";
	            	pictureType = HSSFWorkbook.PICTURE_TYPE_JPEG;
	            }
	            else if(imgPath.endsWith("png") || imgPath.endsWith("gif")){
	            	imgType = "png";
	            	pictureType = XSSFWorkbook.PICTURE_TYPE_PNG;
	            }else{
	            	throw new RuntimeException("image is false");
	            }
	            
	            ImageIO.write(bufferImg, imgType, byteArrayOut);
	            Drawing patriarch = sheet.createDrawingPatriarch();     

	            ClientAnchor anchor = new XSSFClientAnchor();
//	            anchor.setDx1(0);
//	            anchor.setDy1(0);
//	            anchor.setDx2(2000);
//	            anchor.setDy2(10);
	            anchor.setCol1(col1);
	            anchor.setRow1(row1);
	            anchor.setCol2(col2);
	            anchor.setRow2(row2);
	            
	            anchor.setAnchorType(AnchorType.MOVE_AND_RESIZE); 
	            //插入图片    
	            patriarch.createPicture(anchor, xssfWorkbook.addPicture(byteArrayOut.toByteArray(), pictureType));
	            
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }
	}//emd insertImage
	
	public XSSFWorkbook getWorkBook(){
		return xssfWorkbook;
	}
	
	
//	/**
//	 * 配置边框数据   0 上  1 右  2下 3左 
//	 */
//	public XSSFCellStyle setBorder(XSSFCellStyle border,int... borders){
//		if(border == null){
//			border = xssfWorkbook.createCellStyle();
//		}
//		for (int i = 0; i < borders.length; i++) {
//			switch (borders[i]) {
//				case TOP_BORDER:
//						border.setBorderTop(CellStyle.BORDER_THIN);
//					break;
//				case RIGHT_BORDER:
//						border.setBorderRight(CellStyle.BORDER_THIN);
//					break;
//				case BOTTOM_BORDER:
//						border.setBorderBottom(CellStyle.BORDER_THIN);
//					break;
//				case LEFT_BORDER:
//						border.setBorderLeft(CellStyle.BORDER_THIN);
//					break;
//			}//end 
//		}//end for 
//		return border;
//	}//end setBorder()
	
	/**
	 * 指定单元格设置边框   
	 * @param row		单元格所在行  
	 * @param column 	单元格所在列
	 * @param cellStyle	单元格样式
	 * @param borderStyle 线条样式
	 * 					 	BORDER_THIN		//细边框
							BORDER_DASHED 	//虚边框
							BORDER_MEDIUM 	//粗边框
	 * @param borders 0 上  1 右  2下 3左  4所有边框
	 * 
	 * 注意克隆的样式，会导致使用同一个cellStyle的样式会改变
	 */
	public XSSFCellStyle setBorder(int row,int column,XSSFCellStyle cellStyle,short borderStyle,short... borders){
		
		setBorder(cellStyle, borderStyle, borders);
		
		getCell(row, column).setCellStyle(cellStyle);
		return cellStyle;
	}//end setBorder()
	
	/**
	 * 设置背景颜色
	 * @param cellStyle
	 * @param borderStyle
	 * @param borders
	 * @return
	 */
	public XSSFCellStyle setBorder(XSSFCellStyle cellStyle,short borderStyle,short... borders){
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle();
		}
		
		FOR:for (int i = 0; i < borders.length; i++) {
			switch (borders[i]) {
				case BORDER_TOP:
					cellStyle.setBorderTop(borderStyle);
					break;
				case BORDER_RIGHT:
					cellStyle.setBorderRight(borderStyle);
					break;
				case BORDER_BOTTOM:
					cellStyle.setBorderBottom(borderStyle);
					break;
				case BORDER_LEFT:
					cellStyle.setBorderLeft(borderStyle);
					break;
				case BORDER_ALL:
					cellStyle.setBorderTop(borderStyle);
					cellStyle.setBorderRight(borderStyle);
					cellStyle.setBorderBottom(borderStyle);
					cellStyle.setBorderLeft(borderStyle);
					break FOR;
			}//end 
		}//end for 
		
		return cellStyle;
	}
	
	/**
	 * 设置合并单元格边框
	 */
	public void setMergedRegionBorder(int row,int column,short borderStyle,short... borders){
		int firstColumn =0; 
        int lastColumn = 0;   
        int firstRow = 0;   
        int lastRow = 0; 
        int i = 0 ,j = 0;
        boolean isMergedRegion = false;
        
		int sheetMergeCount = sheet.getNumMergedRegions();   
	    for (; i < sheetMergeCount; i++) {   
	    	CellRangeAddress range = sheet.getMergedRegion(i);   
	    	firstColumn = range.getFirstColumn(); 
	    	lastColumn = range.getLastColumn();   
	    	firstRow = range.getFirstRow();   
	    	lastRow = range.getLastRow();
	    	
	    	if(row >= firstRow && row <= lastRow){ 
	    		if(column >= firstColumn && column <= lastColumn){ 
	    			isMergedRegion = true;
	    			break; 
	    		} 
	    	}   
	    } //end for
	    XSSFCellStyle cellStyle = null;
	    if(!isMergedRegion){
	    	setBorder(row, column,getCell(row, column).getCellStyle(),borderStyle, borders);
	    }else{
	    	for (i = 0; i < borders.length; i++) {
	    		//上边框
				if(borders[i] == BORDER_TOP || borders[i] == BORDER_ALL){
					for(j = firstColumn; j <= lastColumn; j++){
						if(j == firstColumn){
							cellStyle  = getCell(row, column).getCellStyle();
						}
							setBorder(firstRow,j,cellStyle,borderStyle,BORDER_TOP);
					}
				}
				
				//右边框
				if(borders[i] == BORDER_RIGHT || borders[i] == BORDER_ALL){
					for(j = firstRow; j <= lastRow; j++){
						setBorder(j,lastColumn,cellStyle,borderStyle,BORDER_RIGHT);
					}
				}
				
				//下边框
				if(borders[i] == BORDER_BOTTOM || borders[i] == BORDER_ALL){
					for(j = firstColumn; j <= lastColumn; j++){
						setBorder(lastRow,j,cellStyle,borderStyle,BORDER_BOTTOM);
					}
				}
				
				//左边框
				if(borders[i] == BORDER_LEFT || borders[i] == BORDER_ALL){
					for(j = firstRow; j <= lastRow; j++){
						if(j == firstColumn){
							cellStyle  = getCell(row, column).getCellStyle();
						}
							setBorder(j,firstColumn,cellStyle,borderStyle,BORDER_LEFT);
					}
				}
				
			}//end for( i < borders.length)
	    }//end if - else
	}//end setMergedRegionBorder()
	
	/**
	 * 设置指定列宽
	 * @param columnIndex	目标列
	 * @param width			列宽度
	 */
	public void setColumnWidth(int columnIndex,int width){
		sheet.setColumnWidth(columnIndex, width * 256);
	}//end setColumnWidth()
	
	/**
	 * 获取指定列的宽度
	 * @param columnIndex
	 * @return
	 */
	public int getColumnWidth(int columnIndex){
		return sheet.getColumnWidth(columnIndex) / 256;
	}//end getColumnWidth()
	
	/**
	 * 设置指定行高
	 * @param rowIndex	目标行
	 * @param height	行高度
	 */
	public void setRowHeight(int rowIndex,short height){
		getRow(rowIndex).setHeight((short)(height * 20));
	}//end setRowHeight
	
	/**
	 * 获取目标行的高
	 * @param rowIndex	目标行
	 * @return
	 */
	public int getRowHeight(int rowIndex){
		return getRow(rowIndex).getHeight() / 20;
	}//end getRowHeight
	
	/**
	 *	获取目标行
	 * @param rowNum
	 * @return
	 */
	public XSSFRow getRow(int rowNum){
		XSSFRow row = sheet.getRow(rowNum);
		if(row == null){
			row = sheet.createRow(rowNum);
		}
		return row;
	}//end getRow
	
	
	/**
	 * 是否显示网格
	 * @param displayGridlines
	 */
	public void setDisplayGridlines(boolean displayGridlines){
		sheet.setDisplayGridlines(displayGridlines);
	}
	
	/**
	 * 创建字体
	 * @param fontName
	 * @param fontsize
	 * @return
	 */
//	public XSSFFont setFont(XSSFFont font,String fontName,int fontsize){
//		if(font == null){
//			font = xssfWorkbook.createFont();
//		}
//		font.setFontName(fontName);
//		font.setFontHeightInPoints((short)fontsize);
//		return font;
//	}//end setFont
	
	/**
	 * 指定单元格设置字体
	 * @param row		单元格所在行
	 * @param column	单元格所在列
	 * @param fontName	字体名称
	 * @param fontsize	字体大小
	 */
	public XSSFCellStyle setFont(int row,int column,String fontName,short fontsize,boolean ...isFlush){
		
		XSSFCellStyle cellStyle = getCellStyle(row, column, isFlush);
		
		setFont(cellStyle, fontName, fontsize);
		
		getCell(row, column).setCellStyle(cellStyle);
		
		return cellStyle;
	}//end setFont
	
	public XSSFCellStyle setFont(XSSFCellStyle cellStyle,String fontName,short fontsize){
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle();
		}
		
		XSSFFont font = xssfWorkbook.createFont();
//		XSSFFont font = cellStyle.getFont();
		font.setFontName(fontName);
		font.setFontHeightInPoints(fontsize);
		cellStyle.setFont(font);
		
		return cellStyle;
	}
	
	/**
	 * 设置字体颜色
	 * @param row		目标单元格所在行
	 * @param column	目标单元格所在列
	 * @param rgb		rgb颜色值
	 * @param isFlush	是否刷新样式
	 */
	public XSSFCellStyle setFontColor(int row,int column,int[] rgb,boolean ...isFlush){
		XSSFCellStyle cellStyle = getCellStyle(row, column, isFlush);
		
		setFontColor(cellStyle, rgb);
		
		getCell(row, column).setCellStyle(cellStyle);
		
		return cellStyle;
	}//end setFontColor()
	
	public XSSFCellStyle setFontColor(XSSFCellStyle cellStyle,int[] rgb){
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle();
		}
		
		XSSFFont font = cellStyle.getFont();
		font.setColor(new XSSFColor(new Color(rgb[0],rgb[1],rgb[2])));
		cellStyle.setFont(font);
		
		return cellStyle;
	}
	
	/**
	 * 设置字体是否粗体
	 * @param row		目标单元格所在行
	 * @param column	目标单元格所在列
	 * @param fontStyle	字体样式 :
	 * 					FONT_NORMAL	正常
	 * 					FONT_BOLD	粗体
	 * @param isFlush	是否刷新单元格样式
	 * @return
	 */
	public XSSFCellStyle setFontBold(int row,int column,short fontStyle,boolean ...isFlush){
		XSSFCellStyle cellStyle = getCellStyle(row, column, isFlush);
		
		setFontBold(cellStyle, fontStyle);
		
		getCell(row, column).setCellStyle(cellStyle);
		
		return cellStyle;
	}//end setFontBold
	
	public XSSFCellStyle setFontBold(XSSFCellStyle cellStyle,short fontStyle){
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle();
		}
			
		XSSFFont font = cellStyle.getFont();
		font.setBoldweight(fontStyle);
		
		cellStyle.setFont(font);
		
		return cellStyle;
	}
	
	/**
	 * 设置字体居中，居右，居左等样式
	 */
//	public XSSFCellStyle setAlign(XSSFCellStyle cellStyle,short align){
//		if(cellStyle == null){
//			cellStyle = xssfWorkbook.createCellStyle();
//		}
//		cellStyle.setAlignment(align);
//		return cellStyle;
//	}//setAlign 
	
	/**
	 * 设置指定单元格对齐
	 * @param row		单元格所在行
	 * @param column	单元格所在列
	 * @param align		ALIGN_CENTER;	//左
						ALIGN_LEFT;		//居中
						ALIGN_RIGHT;	//右
	 */
	public XSSFCellStyle setAlign(int row,int column,short align,boolean ...isFlush){
//		XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
		XSSFCellStyle cellStyle = getCellStyle(row, column, isFlush);
		
		setAlign(cellStyle, align);
		
		getCell(row, column).setCellStyle(cellStyle);
		
		return cellStyle;
	}//setAlign
	
	public XSSFCellStyle setAlign( XSSFCellStyle cellStyle,short align){
		if(cellStyle == null){
			cellStyle = xssfWorkbook.createCellStyle();
		}
		
		if(align == ALIGN_VERTICAL_CENTER){
			cellStyle.setVerticalAlignment(align);
		}else{
			cellStyle.setAlignment(align);
		}
		return cellStyle;
	}
	
//	/**
//	 * 设置将样式赋值给指定单元格
//	 * @param r
//	 * @param c
//	 */
//	public void setCellStyle(int r,int c,XSSFCellStyle style){
//		XSSFCell cell = getCell(r, c);
//		cell.setCellStyle(style);
//	}//end setCellStyle();
	
	
	/**
	 * 在末尾行，一行一行追加数据
	 * @param String[] 一行数据
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void writeLineData(String[] data) throws FileNotFoundException, IOException{
		int lastRow = sheet.getLastRowNum();
		
		lastRow= sheet.getRow(lastRow)==null?0:lastRow+1;
		XSSFRow row = sheet.createRow(lastRow);
		
		XSSFCell cell;
		for (int i = 0; i < data.length; i++) {
			cell = row.createCell(i);
			cell.setCellValue(data[i]);
			cell.setCellType(XSSFCell.CELL_TYPE_STRING);
			XSSFCellStyle cellStyle = xssfWorkbook.createCellStyle();
			cell.setCellStyle(cellStyle);
		}
		
	}//end writeLineData(String[] data)
	
	/**
	 * 合并单元格
	 * @param firstRow	左上角单元格所在行
	 * @param lastRow	右下角单元格所在行
	 * @param firstCol	左上角单元格所在列
	 * @param lastCol	右下角单元格所在列
	 */
	public void setRangeCell(int firstRow,int firstCol,int lastRow,int lastCol){
		int x = 0,y=0;
		//清空非首格的值
		for(x = firstRow; x <= lastRow; x++){
			for(y = firstCol;y <= lastCol;y++){
				if(x == firstRow && y == firstCol){
					continue;
				}
				removeCell(x, y);
			}
		}
		sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
	}//end setRangeCell
	

	/**
	 * 删除指定单元格
	 * @param r
	 * @param c
	 */
	public void removeCell(int r,int c){
		this.getRow(r).removeCell(this.getCell(r, c));
	}
	
	/**
	 * 写入数据并设置单元格样式
	 * @param data
	 * @param r
	 * @param c
	 * @param cellStyle
	 */
	public void writeCellData(Object data,int r,int c,XSSFCellStyle cellStyle){
		writeCellData(data, r, c);
		cloneCellStyle(r, c, cellStyle);
	}
	
	/**
	 * 在指定的单元格写入数据
	 * type :
	 * 		CELL_BOOLEAN
	 * 		CELL_FORMULA
	 * 		CELL_STRING
	 * 		CELL_NUMERIC
	 */
	public void writeCellData(Object data,int r,int c){
		
		int[] rangeFirst = getRangeFirst(r, c);
		r = rangeFirst[0];
		c = rangeFirst[1];
		
		XSSFCell cell = this.getCell(r, c);
		
		if(data instanceof Boolean){
			cell.setCellType(CELL_BOOLEAN);
			cell.setCellValue((boolean)data);
		}
//		case CELL_FORMULA:
//			cell.setCellValue(data);
//			break;
		else if(data instanceof String){
			cell.setCellType(CELL_STRING);
			cell.setCellValue(data.toString());			
		}
		else if(data instanceof Integer || data instanceof Short || data instanceof Float || 
		   data instanceof Double || data instanceof Byte || data instanceof Long){
			cell.setCellType(CELL_NUMERIC);
			cell.setCellValue(new Double(data.toString()));
		}
		else if(data instanceof Calendar){
			cell.setCellValue((Calendar)data);
		}
		else if(data instanceof Date){
			cell.setCellValue((Date)data);
		}
		else if(data instanceof RichTextString){
			cell.setCellValue((RichTextString)data);
		}else if(data != null){
			cell.setCellType(CELL_STRING);
			cell.setCellValue(data.toString());
		}
	}//end writeCellData()
	
	
	/**
	 * 获取合并的单元格的首个单元格
	 * @param r
	 * @param c
	 * @return
	 */
	public int[] getRangeFirst(int r,int c){
		int[] rc = new int[2]; 
		int sheetMergeCount = sheet.getNumMergedRegions();
			for(int i = 0 ; i < sheetMergeCount ; i++){     
	            CellRangeAddress ca = sheet.getMergedRegion(i);     
	            int firstColumn = ca.getFirstColumn();     
	            int lastColumn = ca.getLastColumn();     
	            int firstRow = ca.getFirstRow();     
	            int lastRow = ca.getLastRow();     
	            if(r >= firstRow && r <= lastRow){     
	            if(c >= firstColumn && c <= lastColumn){     
	                r = firstRow; 
	                c = firstColumn; 
	            }     
	        }     
	    }//end for  
		
		
		rc[0] = r;
		rc[1] = c;
		return rc;
	}//end getRangeFirst
	
	
	/**
	 * 在指定单元格写入公式
	 * @param formula
	 * @param r
	 * @param c
	 */
	public void writeFormulaData(String formula,int r,int c){
		int[] rangeFirst = getRangeFirst(r,c);
		r = rangeFirst[0];
		c = rangeFirst[1];
		
		XSSFCell cell = getCell(r, c);
		cell.setCellType(XlsxUtil.CELL_FORMULA);
		cell.setCellFormula(formula);
	}
	
	
	/**
	 * 得到指定的单元格
	 * @param r
	 * @param c
	 * @return
	 */
	public XSSFCell getCell(int r,int c){
		
		XSSFRow row = sheet.getRow(r);
		if(row == null){
			row = sheet.createRow(r);
		}
		
		XSSFCell cell = row.getCell(c);
		if(cell == null){
			cell = row.createCell(c);
		}
		
		return cell;
	}//end getCell()
	
	
	/**
	 * 得到指定单元格数据 有单元格合并判断,推荐使用
	 * @param r
	 * @param c
	 * @return
	 */
	public Object getCellData(int r,int c){
		//判断是否为合并单元格
		if(isMergedRegion(r, c)){
			return getMergedRegionValue(r, c);
		}
		return  getCellValue(getCell(r, c));
	}//end getCellData
	
	
	/**
	 * 获取字符串类型的数据
	 * @param r
	 * @param c
	 * @return
	 */
	public String readCellData(int r,int c){
		return String.valueOf(getCellData(r,c));
	}
	
	/**
	 * 关闭并保存数据,如果path不为空，则另存为其它文件。
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void close(String newPath){
		try {
			if(newPath != null){
				fos = new FileOutputStream(newPath);
			}else{
				fos = new FileOutputStream(filePath);
			}
			xssfWorkbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(fos!=null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}//end close();
	
	
	/**   
     * 判断指定的单元格是否是合并单元格   
     * @param sheet    
     * @param row 行下标   
     * @param column 列下标   
     * @return   
     */   
     public boolean isMergedRegion(int row ,int column) {   
//       int sheetMergeCount = sheet.getNumMergedRegions();   
//       for (int i = 0; i < sheetMergeCount; i++) {   
//             CellRangeAddress range = sheet.getMergedRegion(i);   
//             int firstColumn = range.getFirstColumn(); 
//             int lastColumn = range.getLastColumn();   
//             int firstRow = range.getFirstRow();   
//             int lastRow = range.getLastRow();   
//             if(row >= firstRow && row <= lastRow){ 
//                     if(column >= firstColumn && column <= lastColumn){ 
//                        return true;   
//                     } 
//             }   
//       } 
       return getMergedRegion(row,column) != -1;   
     } //end isMergedRegion()
     
     
     /**
      * 获取指定单元格所属的合并单元格
      * @param row
      * @param column
      * @return
      */
     public int getMergedRegion(int row ,int column){
    	 int sheetMergeCount = sheet.getNumMergedRegions();   
         for (int i = 0; i < sheetMergeCount; i++) {   
               CellRangeAddress range = sheet.getMergedRegion(i);   
               int firstColumn = range.getFirstColumn(); 
               int lastColumn = range.getLastColumn();   
               int firstRow = range.getFirstRow();   
               int lastRow = range.getLastRow();   
               if(row >= firstRow && row <= lastRow){ 
                       if(column >= firstColumn && column <= lastColumn){ 
                          return i;   
                       }
               }   
         } 
    	 return -1;
     }//end getMergedRegion
     
     
	
	 /**    
     * 获取单元格的值    未做单元格合并判断
     * @param cell    
     * @return    
     */     
     private Object getCellValue(Cell cell){
    	 Object value = "";
    	 
         if(cell == null) value = "";     
         if(cell.getCellType() == Cell.CELL_TYPE_STRING){     
        	 value = cell.getStringCellValue();     
         }else if(cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){     
        	 value = cell.getBooleanCellValue();     
         }else if(cell.getCellType() == Cell.CELL_TYPE_FORMULA){ 
        	 CellValue cellValue = xssfWorkbook.getCreationHelper().createFormulaEvaluator().evaluate(cell);
        	 switch (cellValue.getCellType()) {
        	 case Cell.CELL_TYPE_BOOLEAN:
        		 value = cellValue.getBooleanValue();
        		 break;
        	 case Cell.CELL_TYPE_NUMERIC:
        		 value = cellValue.getNumberValue();
        		 break;
        	 case Cell.CELL_TYPE_STRING:
        		 value = cellValue.getStringValue();
        		 break;
        	 case Cell.CELL_TYPE_BLANK:
        		 value = "";
        		 break;
        	 case Cell.CELL_TYPE_ERROR:
        		 value = "";
        		 break;
        	 default:
        		 value = cell.getCellFormula();
        		 break;
        	 }
         }else if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC){     
             value = cell.getNumericCellValue();     
         }
         
         return value;     
     } //end 
     
     /**   
      * 读取excel文件   
      * @param wb    
      * @param sheetIndex sheet页下标：从0开始   
      * @param startReadLine 开始读取的行:从0开始   
      * @param tailLine 去除最后读取的行   
      */ 
      @SuppressWarnings("unused") 
     private void readExcel(Workbook  wb,int sheetIndex, int startReadLine, int tailLine){ 
          Sheet sheet = wb.getSheetAt(sheetIndex);   
          Row row = null;   
          for(int i=startReadLine; i<sheet.getLastRowNum()-tailLine+1; i++) {   
              row = sheet.getRow(i); 
              for(Cell c : row) {   
                  boolean isMerge = isMergedRegion(i, c.getColumnIndex()); 
                  if(isMerge) { 
                      String rs = String.valueOf(getMergedRegionValue(row.getRowNum(), c.getColumnIndex())); 
//                      System.out.println(rs + " "); 
                      
                  }else{ 
//                      System.out.print(c.getRichStringCellValue()+" ");   
                  } 
                  
              } 
          }
      }//end readExcel()
	
      /**    
       * 获取合并单元格的值   
       * @param row    
       * @param column    
       * @return    
       */     
       private Object getMergedRegionValue(int row , int column){     
           int sheetMergeCount = sheet.getNumMergedRegions();     
                
           for(int i = 0 ; i < sheetMergeCount ; i++){     
               CellRangeAddress ca = sheet.getMergedRegion(i);     
               int firstColumn = ca.getFirstColumn();     
               int lastColumn = ca.getLastColumn();     
               int firstRow = ca.getFirstRow();     
               int lastRow = ca.getLastRow();     
               if(row >= firstRow && row <= lastRow){     
                    if(column >= firstColumn && column <= lastColumn){     
                       Row fRow = sheet.getRow(firstRow); 
                       Cell fCell = fRow.getCell(firstColumn); 
                       return getCellValue(fCell) ; 
                   }     
               }     
           }//end for     
           return "" ;     
       }//end getMergedRegionValue
       
       /**
        * 删除 firstX,firstY —— lastX,lastY 组成的矩形内的所有单元格 
        * @param firstX	矩形第一个角的X轴
        * @param firstY	矩形第一个角的Y轴
        * @param lastX	矩形最后一个角的X轴
        * @param lastY	矩形最后一个角的Y轴
        */
       public void removeRectangleCell(int firstX,int firstY,int lastX,int lastY){
    	   XSSFCell cell = null;
    	   XSSFRow row = null;
    	   int index = -1;
    	   for (int i = firstX; i <= lastX; i++) {
    		   //得到行
    		   row = sheet.getRow(i);
    		   if(row == null){continue;}
    		   
    		   for (int j = firstY; j <= lastY; j++) {
    			   //删除合并的单元格
    			   index = getMergedRegion(i, j);
    			   if(index != -1){
    				   sheet.removeMergedRegion(index);
    			   }
    			   //得到行并删除列
    			   cell = row.getCell(j);
    			   if(cell != null){
    				   row.removeCell(cell);
    			   }
    		   }//end for(j <= lastY)
    	   }//end for(i <= lastX)
       }//end removeRectangleCell()
       
       
       /**
        * 将数值型列号转成字母型列号 ，从0开始
        * @param columnNum
        * @return
        */
       public static String getColumn(int columnNum){
	   		if((columnNum/Y.length()) == 0){
	   			return String.valueOf(Y.charAt(columnNum)) ;
	   		}
	   		return getColumn(columnNum / Y.length() - 1) + getColumn(columnNum % Y.length());
   		}//end getColumn()
       
       
       /**
        * 将字母型列号转成数值型列号 ，数值型号从0开始
        * @param columnNum
        * @return
        */
       public static int getColumnNum(String column){
    	    column = column.toUpperCase();
    	    
    	    if(column.length()==1){
    	    	return Y.indexOf(column);
    	    }
    	  //  100  10  1
    	    return (int) (Math.pow(26, column.length() - 1) * (getColumnNum(column.substring(0,1)) + 1) + getColumnNum(column.substring(1,column.length())));
   		}//end getColumnNum()
       
       public static void main(String[] args) {
    	   int columnNum = XlsxUtil.getColumnNum("X");
    	   System.out.println(columnNum);
	}
}
