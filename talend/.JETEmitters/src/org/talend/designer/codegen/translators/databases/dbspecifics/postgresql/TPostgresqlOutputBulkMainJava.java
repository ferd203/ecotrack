package org.talend.designer.codegen.translators.databases.dbspecifics.postgresql;

import org.talend.core.model.process.INode;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.process.IConnectionCategory;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import java.util.List;
import org.talend.core.model.process.ElementParameterParser;

public class TPostgresqlOutputBulkMainJava
{
  protected static String nl;
  public static synchronized TPostgresqlOutputBulkMainJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TPostgresqlOutputBulkMainJava result = new TPostgresqlOutputBulkMainJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "\t\t\t\tlog.debug(\"";
  protected final String TEXT_2 = " - Retrieving records from the datasource.\");" + NL + "\t\t\t";
  protected final String TEXT_3 = NL + "\t\t\t\tlog.debug(\"";
  protected final String TEXT_4 = " - Retrieved records count: \"+ nb_line_";
  protected final String TEXT_5 = " + \" .\");" + NL + "\t\t\t";
  protected final String TEXT_6 = " - Retrieved records count: \"+ globalMap.get(\"";
  protected final String TEXT_7 = "_NB_LINE\") + \" .\");" + NL + "\t\t\t";
  protected final String TEXT_8 = " - Written records count: \" + nb_line_";
  protected final String TEXT_9 = NL + "\t\t\t\tfinal StringBuffer log4jSb_";
  protected final String TEXT_10 = " = new StringBuffer();" + NL + "\t\t\t";
  protected final String TEXT_11 = " - Retrieving the record \" + (nb_line_";
  protected final String TEXT_12 = ") + \".\");" + NL + "\t\t\t";
  protected final String TEXT_13 = " - Writing the record \" + nb_line_";
  protected final String TEXT_14 = " + \" to the file.\");" + NL + "\t\t\t";
  protected final String TEXT_15 = " - Processing the record \" + nb_line_";
  protected final String TEXT_16 = " + \".\");" + NL + "\t\t\t";
  protected final String TEXT_17 = " - Processed records count: \" + nb_line_";
  protected final String TEXT_18 = NL + " ";
  protected final String TEXT_19 = NL + "               \tString[] row_";
  protected final String TEXT_20 = "=new String[]{";
  protected final String TEXT_21 = ", ";
  protected final String TEXT_22 = "};";
  protected final String TEXT_23 = NL + "                        \trow_";
  protected final String TEXT_24 = "[";
  protected final String TEXT_25 = "] = String.valueOf(";
  protected final String TEXT_26 = ".";
  protected final String TEXT_27 = ");";
  protected final String TEXT_28 = "   \t\t\t\t" + NL + "\t\t\t\t       \tif(";
  protected final String TEXT_29 = " != null){";
  protected final String TEXT_30 = NL + "                            \trow_";
  protected final String TEXT_31 = "] = ";
  protected final String TEXT_32 = ";";
  protected final String TEXT_33 = NL + "    \t                    \trow_";
  protected final String TEXT_34 = "] = FormatterUtils.format_Date(";
  protected final String TEXT_35 = NL + "            \t            \trow_";
  protected final String TEXT_36 = "] = java.nio.charset.Charset.forName(";
  protected final String TEXT_37 = ").decode(java.nio.ByteBuffer.wrap(";
  protected final String TEXT_38 = ")).toString();";
  protected final String TEXT_39 = NL + "  \t\t\t\t\t\t}";
  protected final String TEXT_40 = NL + "\t\t\t\t\tfor (int i_";
  protected final String TEXT_41 = " = 0; i_";
  protected final String TEXT_42 = " < row_";
  protected final String TEXT_43 = ".length; i_";
  protected final String TEXT_44 = "++) {" + NL + "\t               \t\tString temp_";
  protected final String TEXT_45 = " = row_";
  protected final String TEXT_46 = "[i_";
  protected final String TEXT_47 = "];" + NL + "\t               \t\t" + NL + "\t    \t\t        if(i_";
  protected final String TEXT_48 = " != 0) {" + NL + "    \t    \t       \t\tout";
  protected final String TEXT_49 = ".write(OUT_DELIM_";
  protected final String TEXT_50 = ".toString());" + NL + "        \t        \t}" + NL + "        \t        \tif(!";
  protected final String TEXT_51 = ".equals(temp_";
  protected final String TEXT_52 = ")){" + NL + "        \t        \t  temp_";
  protected final String TEXT_53 = " = temp_";
  protected final String TEXT_54 = ".replaceAll(\"\\\\\\\\\",\"\\\\\\\\\\\\\\\\\");" + NL + "        \t        \t}" + NL + "\t\t           \t\tout";
  protected final String TEXT_55 = ".write(temp_";
  protected final String TEXT_56 = ");" + NL + "    \t\t        }";
  protected final String TEXT_57 = "++) {" + NL + "\t\t\t\t\t\tif(i_";
  protected final String TEXT_58 = " != 0) {" + NL + "                        \tout";
  protected final String TEXT_59 = ".toString());" + NL + "                        }" + NL + "                \t\tString temp_";
  protected final String TEXT_60 = "];" + NL + "                        if(!";
  protected final String TEXT_61 = ")) {" + NL + "                            if (escape_mode_";
  protected final String TEXT_62 = " == 0 ) { // double quotes" + NL + "                                temp_";
  protected final String TEXT_63 = ".replace(\"\" + textEnclosure_";
  protected final String TEXT_64 = ", \"\" + textEnclosure_";
  protected final String TEXT_65 = " + textEnclosure_";
  protected final String TEXT_66 = ");" + NL + "                            }else{" + NL + "                                temp_";
  protected final String TEXT_67 = ".replace(\"\\\\\", \"\\\\\\\\\");" + NL + "                                temp_";
  protected final String TEXT_68 = ", \"\\\\\" + textEnclosure_";
  protected final String TEXT_69 = ");" + NL + "                            }" + NL + "                            out";
  protected final String TEXT_70 = ".append(textEnclosure_";
  protected final String TEXT_71 = ");" + NL + "                        \tout";
  protected final String TEXT_72 = ");" + NL + "                        }else{" + NL + "                            out";
  protected final String TEXT_73 = ");" + NL + "                        }" + NL + "                \t}";
  protected final String TEXT_74 = NL + "    \t\t\tout";
  protected final String TEXT_75 = ".write(OUT_DELIM_ROWSEP_";
  protected final String TEXT_76 = ");" + NL + "    \t\t\tnb_line_";
  protected final String TEXT_77 = "++;";
  protected final String TEXT_78 = NL + "                    log.debug(\"";
  protected final String TEXT_79 = " + \".\");";
  protected final String TEXT_80 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    
	//this util class use by set log4j debug paramters
	class DefaultLog4jFileUtil {
	
		INode node = null;
	    String cid = null;
 		boolean isLog4jEnabled = false;
 		String label = null;
 		
 		public DefaultLog4jFileUtil(){
 		}
 		public DefaultLog4jFileUtil(INode node) {
 			this.node = node;
 			this.cid = node.getUniqueName();
 			this.label = cid;
			this.isLog4jEnabled = ("true").equals(org.talend.core.model.process.ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));
 		}
 		
 		public void setCid(String cid) {
 			this.cid = cid;
 		}
 		
		//for all tFileinput* components 
		public void startRetriveDataInfo() {
			if (isLog4jEnabled) {
			
    stringBuffer.append(TEXT_1);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_2);
    
			}
		}
		
		public void retrievedDataNumberInfo() {
			if (isLog4jEnabled) {
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    
			}
		}
		
		public void retrievedDataNumberInfoFromGlobalMap(INode node) {
			if (isLog4jEnabled) {
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_7);
    
			}
		}
		
		//for all tFileinput* components 
		public void retrievedDataNumberInfo(INode node) {
			if (isLog4jEnabled) {
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_4);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_5);
    
			}
		}
		
		public void writeDataFinishInfo(INode node) {
			if(isLog4jEnabled){
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    
			}
		}
		
 		//TODO delete it and remove all log4jSb parameter from components
		public void componentStartInfo(INode node) {
			if (isLog4jEnabled) {
			
    stringBuffer.append(TEXT_9);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_10);
    
			}
		}
		
		//TODO rename or delete it
		public void debugRetriveData(INode node,boolean hasIncreased) {
			if(isLog4jEnabled){
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid);
    stringBuffer.append(hasIncreased?"":"+1");
    stringBuffer.append(TEXT_12);
    
			}
		}
		
		//TODO rename or delete it
		public void debugRetriveData(INode node) {
			debugRetriveData(node,true);
		}
		
		//TODO rename or delete it
		public void debugWriteData(INode node) {
			if(isLog4jEnabled){
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    
			}
		}
		
		public void logCurrentRowNumberInfo() {
			if(isLog4jEnabled){
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_15);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_16);
    
			}
		}
		
		public void logDataCountInfo() {
			if(isLog4jEnabled){
			
    stringBuffer.append(TEXT_3);
    stringBuffer.append(label);
    stringBuffer.append(TEXT_17);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_5);
    
			}
		}
	}
	
	final DefaultLog4jFileUtil log4jFileUtil = new DefaultLog4jFileUtil((INode)(((org.talend.designer.codegen.config.CodeGeneratorArgument)argument).getArgument()));
	
    stringBuffer.append(TEXT_18);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();
String cid = node.getUniqueName();
boolean isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));
List<IMetadataTable> metadatas = node.getMetadataList();
    
    String fileType = ElementParameterParser.getValue(node, "__FILETYPE__");
    
    String encoding = ElementParameterParser.getValue(node,"__ENCODING__");
    
    String nullString = ElementParameterParser.getValue(node,"__NULLSTRING__");   
    
if ((metadatas!=null)&&(metadatas.size()>0)) {
    IMetadataTable metadata = metadatas.get(0);
    if (metadata!=null) {
    	List< ? extends IConnection> conns = node.getIncomingConnections();
    	for (IConnection conn : conns) {
    		if (conn.getLineStyle().hasConnectionCategory(IConnectionCategory.DATA)) {
    			List<IMetadataColumn> columns = metadata.getListColumns();
    			int sizeColumns = columns.size();
    			//for bug TDI-22555

    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    for(int j=0;j<sizeColumns;j++){if(j != 0) {
    stringBuffer.append(TEXT_21);
    }
    stringBuffer.append(nullString);
    }
    stringBuffer.append(TEXT_22);
    
    			for (int i = 0; i < sizeColumns; i++) {
    				IMetadataColumn column = columns.get(i);
					JavaType javaType = JavaTypesManager.getJavaTypeFromId(column.getTalendType());
					String pattern = column.getPattern() == null || column.getPattern().trim().length() == 0 ? null : column.getPattern();
					if(JavaTypesManager.isJavaPrimitiveType( column.getTalendType(), column.isNullable())){

    stringBuffer.append(TEXT_23);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_27);
    
					}else {

    stringBuffer.append(TEXT_28);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_29);
                      
                    		if(javaType == JavaTypesManager.STRING ){

    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_31);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_32);
    
							}else if(javaType == JavaTypesManager.DATE && pattern != null){

    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_34);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_21);
    stringBuffer.append( pattern );
    stringBuffer.append(TEXT_27);
    
        					}else if(javaType == JavaTypesManager.BYTE_ARRAY){

    stringBuffer.append(TEXT_35);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_36);
    stringBuffer.append(encoding );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_38);
    
							}else{

    stringBuffer.append(TEXT_33);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_24);
    stringBuffer.append(i);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(conn.getName() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(column.getLabel() );
    stringBuffer.append(TEXT_27);
    
        					}

    stringBuffer.append(TEXT_39);
    
					}
				}
    			if("TXTFILE".equals(fileType)){

    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_50);
    stringBuffer.append(nullString );
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    
    			}else if("CSVFILE".equals(fileType)){

    stringBuffer.append(TEXT_40);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(nullString);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_73);
    
				}

    stringBuffer.append(TEXT_74);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_76);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_77);
    if(isLog4jEnabled){
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    }
    
    		}
    	}
    }
}

    stringBuffer.append(TEXT_80);
    return stringBuffer.toString();
  }
}
