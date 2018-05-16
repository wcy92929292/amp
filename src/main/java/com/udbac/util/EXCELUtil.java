package com.udbac.util;
/**
 * 
 * @author WangLi
 *
 */
public class EXCELUtil {
	  /**
	    * 2007版本以上
	    * 根据输入的列号获得excel的列号的编码，从1开始的
	    * @param columnNum
	    * @return
	    */
	public  int columnToIndex(String column) {
		if (!column.matches("[A-Z]+")) {
			try {
				throw new Exception("Invalid parameter");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		int index = 0;
		char[] chars = column.toUpperCase().toCharArray();
		for (int i = 0; i < chars.length; i++) {
			index += ((int) chars[i] - (int) 'A' + 1)
					* (int) Math.pow(26, chars.length - i - 1);
		}
		return index;
	}
   /**
    * 2003版本
    * 根据输入的列号获得excel的列号的编码，从1开始的,存在特殊符号问题
    * @param columnNum
    * @return
    */
	public String indexToColumn(int columnNum) {
        int first;
        int last;
        String result = "";
        if (columnNum > 256)
                columnNum = 256;
        first = columnNum / 27;
        last = columnNum - (first * 26);

        if (first > 0)
                result = String.valueOf((char) (first + 64));

        if (last > 0)
                result = result + String.valueOf((char) (last + 64));

        return result;
}
	/**
	* 用于将excel表格中列索引转成列号字母，从A对应1开始
	* 
	* @param index
	*            列索引
	* @return 列号
	*/
	public String getColumnName(int index) {
	        if (index <= 0) {            
	        	try {                      
	        		throw new Exception("Invalid parameter");          
	        		} catch (Exception e) {                     
	        			e.printStackTrace();               
	        			}       
	        	}        
	        index--;       
	        String column = "";    
	        do {                
	        	if (column.length() > 0) {
	                        index--;
	                }
	                column = ((char) (index % 26 + (int) 'A')) + column;
	                index = (int) ((index - index % 26) / 26);
	        } while (index > 0);
	        return column;
	}
	//测试代码
	public static void main(String[] args){
		
		for(int i=1;i<10000;i++){
			System.out.println(new EXCELUtil().getColumnName(i));
		}
	}

}
