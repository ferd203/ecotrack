package org.talend.designer.codegen.translators.technical;

import org.talend.core.model.process.INode;
import org.talend.designer.codegen.config.CodeGeneratorArgument;
import java.util.Map;
import java.util.List;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.core.model.process.IConnection;
import org.talend.core.model.metadata.types.JavaTypesManager;
import org.talend.core.model.metadata.types.JavaType;
import org.talend.core.model.metadata.IMetadataTable;
import org.talend.core.model.metadata.IMetadataColumn;

public class TUniqRowOutBeginJava
{
  protected static String nl;
  public static synchronized TUniqRowOutBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TUniqRowOutBeginJava result = new TUniqRowOutBeginJava();
    nl = null;
    return result;
  }

  public final String NL = nl == null ? (System.getProperties().getProperty("line.separator")) : nl;
  protected final String TEXT_1 = "";
  protected final String TEXT_2 = NL + NL + "//////////////////////////" + NL + "long nb_";
  protected final String TEXT_3 = " = 0;" + NL + "" + NL + "int bufferSize_";
  protected final String TEXT_4 = " = ";
  protected final String TEXT_5 = ";";
  protected final String TEXT_6 = NL + "\tlog.debug(\"";
  protected final String TEXT_7 = " - Start to process the data from datasource.\");";
  protected final String TEXT_8 = NL + "class rowStruct_";
  protected final String TEXT_9 = " extends ";
  protected final String TEXT_10 = "Struct {" + NL + "" + NL + "    long id_";
  protected final String TEXT_11 = ";" + NL + "" + NL + "    @Override" + NL + "    public void readData(ObjectInputStream dis) {" + NL + "        super.readData(dis);" + NL + "        try {" + NL + "\t\t\tthis.id_";
  protected final String TEXT_12 = " = dis.readLong();" + NL + "        } catch (IOException e) {" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "        }" + NL + "    }" + NL + "" + NL + "    @Override" + NL + "    public String toString() {" + NL + "        return \"{\" + super.toString() + \"\\t\" + id_";
  protected final String TEXT_13 = " + \"}\";" + NL + "    }" + NL + "" + NL + "    @Override" + NL + "    public void writeData(ObjectOutputStream dos) {" + NL + "        super.writeData(dos);" + NL + "        try {" + NL + "\t\t\t// Integer" + NL + "\t\t\tdos.writeLong(this.id_";
  protected final String TEXT_14 = ");" + NL + "" + NL + "        } catch (IOException e) {" + NL + "\t\t\tthrow new RuntimeException(e);" + NL + "        }" + NL + "    }" + NL + "" + NL + "    public boolean duplicateTo(rowStruct_";
  protected final String TEXT_15 = " other) {" + NL;
  protected final String TEXT_16 = NL + "\t\tif (this.";
  protected final String TEXT_17 = " == null) {" + NL + "\t\t\tif (other.";
  protected final String TEXT_18 = " != null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t}" + NL + "\t\t} else {" + NL + "\t\t\tif (other.";
  protected final String TEXT_19 = " == null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t} else {" + NL + "\t\t\t    if (!this.";
  protected final String TEXT_20 = ".equals(other.";
  protected final String TEXT_21 = ")) {" + NL + "\t\t\t        return false;" + NL + "\t\t\t    }" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_22 = NL + "\t\tif(this.";
  protected final String TEXT_23 = " != other.";
  protected final String TEXT_24 = "){" + NL + "\t\t\treturn false;" + NL + "\t\t}" + NL + "\t\t\t";
  protected final String TEXT_25 = " == null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t} else {" + NL + "\t\t\t    if (Math.abs(this.";
  protected final String TEXT_26 = " - other.";
  protected final String TEXT_27 = ") > 0.00000001) {" + NL + "\t\t\t        return false;" + NL + "\t\t\t    }" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_28 = NL + "\t\tif(Math.abs(this.";
  protected final String TEXT_29 = ") > 0.00000001) {" + NL + "\t\t\treturn false;" + NL + "\t\t}" + NL + "\t\t\t";
  protected final String TEXT_30 = " == null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t} else {" + NL + "\t\t\t    if(this.";
  protected final String TEXT_31 = " && !other.";
  protected final String TEXT_32 = "){" + NL + "\t\t\t\t\treturn false;" + NL + "\t\t\t\t}else if(!this.";
  protected final String TEXT_33 = " && other.";
  protected final String TEXT_34 = "){" + NL + "\t\t\t\t\treturn false;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t\t";
  protected final String TEXT_35 = "){" + NL + "\t\t\treturn false;" + NL + "\t\t}else if(!this.";
  protected final String TEXT_36 = "){" + NL + "\t\t\treturn false;" + NL + "\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_37 = " == null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t} else {" + NL + "\t\t\t\tString this_";
  protected final String TEXT_38 = "_";
  protected final String TEXT_39 = " = String.valueOf(this.";
  protected final String TEXT_40 = ");" + NL + "\t\t\t\tString other_";
  protected final String TEXT_41 = " = String.valueOf(other.";
  protected final String TEXT_42 = ");" + NL + "\t\t\t\t" + NL + "\t\t\t    if(!this_";
  protected final String TEXT_43 = ".equals";
  protected final String TEXT_44 = "(other_";
  protected final String TEXT_45 = ")){" + NL + "\t\t\t\t\treturn false;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_46 = " == null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t} else {" + NL + "\t\t\t    if(!this.";
  protected final String TEXT_47 = " == null) {" + NL + "\t\t\t    return false;" + NL + "\t\t\t} else {" + NL + "\t\t\t";
  protected final String TEXT_48 = NL + "\t\t\t    if (!this.";
  protected final String TEXT_49 = ".stripTrailingZeros().equals(other.";
  protected final String TEXT_50 = ".stripTrailingZeros())) {" + NL + "\t\t\t        return false;" + NL + "\t\t\t    }" + NL + "\t\t\t";
  protected final String TEXT_51 = ")) {" + NL + "\t\t\t        return false;" + NL + "\t\t\t    }" + NL + "\t\t\t";
  protected final String TEXT_52 = NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_53 = "(other.";
  protected final String TEXT_54 = NL + NL + "        return true;" + NL + "    }" + NL + "" + NL + "}" + NL + "" + NL + "// comparator for first sort" + NL + "class Comparator_1_";
  protected final String TEXT_55 = " implements Comparator<rowStruct_";
  protected final String TEXT_56 = "> {" + NL + "" + NL + "    public int compare(rowStruct_";
  protected final String TEXT_57 = " arg0, rowStruct_";
  protected final String TEXT_58 = " arg1) {" + NL + "\t\tint compare = 0;";
  protected final String TEXT_59 = NL + "\t\tif (arg0.";
  protected final String TEXT_60 = " == null) {" + NL + "\t\t\tif (arg1.";
  protected final String TEXT_61 = " != null) {" + NL + "\t\t\t    return -1;" + NL + "\t\t\t}" + NL + "\t\t} else {" + NL + "\t\t\tif (arg1.";
  protected final String TEXT_62 = " == null) {" + NL + "\t\t\t    return 1;" + NL + "\t\t\t} else {" + NL + "\t\t\t\tcompare = arg0.";
  protected final String TEXT_63 = ".compareTo(arg1.";
  protected final String TEXT_64 = ");" + NL + "\t\t\t    if (compare != 0) {" + NL + "\t\t\t        return compare;" + NL + "\t\t\t    }" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_65 = NL + "\t\tif(arg0.";
  protected final String TEXT_66 = " > arg1.";
  protected final String TEXT_67 = "){" + NL + "\t\t\treturn 1;" + NL + "\t\t}else if(arg0.";
  protected final String TEXT_68 = " < arg1.";
  protected final String TEXT_69 = "){" + NL + "\t\t\treturn -1;" + NL + "\t\t}" + NL + "\t\t\t";
  protected final String TEXT_70 = " == null) {" + NL + "\t\t\t    return 1;" + NL + "\t\t\t} else {" + NL + "\t\t\t    if (Math.abs(arg0.";
  protected final String TEXT_71 = " - arg1.";
  protected final String TEXT_72 = ") > 0.00000001) {" + NL + "\t\t\t        if(arg0.";
  protected final String TEXT_73 = "){" + NL + "\t\t\t        \treturn 1;" + NL + "\t\t\t        }else{" + NL + "\t\t\t        \treturn -1;" + NL + "\t\t\t        }" + NL + "\t\t\t    }" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_74 = NL + "\t\tif(Math.abs(arg0.";
  protected final String TEXT_75 = ") > 0.00000001) {" + NL + "\t\t\tif(arg0.";
  protected final String TEXT_76 = "){" + NL + "\t\t\t\treturn 1;" + NL + "\t\t\t}else{" + NL + "\t\t\t\treturn -1;" + NL + "\t\t\t}" + NL + "\t\t}" + NL + "\t\t\t";
  protected final String TEXT_77 = " == null) {" + NL + "\t\t\t    return 1;" + NL + "\t\t\t} else {" + NL + "\t\t\t    if(arg0.";
  protected final String TEXT_78 = " && !arg1.";
  protected final String TEXT_79 = "){" + NL + "\t\t\t\t\treturn 1;" + NL + "\t\t\t\t}else if(!arg0.";
  protected final String TEXT_80 = " && arg1.";
  protected final String TEXT_81 = "){" + NL + "\t\t\t\t\treturn -1;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t\t";
  protected final String TEXT_82 = "){" + NL + "\t\t\treturn 1;" + NL + "\t\t}else if(!arg0.";
  protected final String TEXT_83 = "){" + NL + "\t\t\treturn -1;" + NL + "\t\t}" + NL + "\t\t\t\t";
  protected final String TEXT_84 = " == null) {" + NL + "\t\t\t    return 1;" + NL + "\t\t\t} else {" + NL + "\t\t\t\tString arg0_";
  protected final String TEXT_85 = " = String.valueOf(arg0.";
  protected final String TEXT_86 = ");" + NL + "\t\t\t\tString arg1_";
  protected final String TEXT_87 = " = String.valueOf(arg1.";
  protected final String TEXT_88 = ");" + NL + "\t\t\t\tcompare = arg0_";
  protected final String TEXT_89 = ".compareTo";
  protected final String TEXT_90 = "(arg1_";
  protected final String TEXT_91 = ");" + NL + "\t\t\t    if(compare != 0){" + NL + "\t\t\t\t\treturn compare;" + NL + "\t\t\t\t}" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_92 = " == null) {" + NL + "\t\t\t    return 1;" + NL + "\t\t\t} else {" + NL + "\t\t\t\t";
  protected final String TEXT_93 = NL + "\t\t\t    compare = arg0.";
  protected final String TEXT_94 = ".stripTrailingZeros().compareTo(arg1.";
  protected final String TEXT_95 = ".stripTrailingZeros());" + NL + "\t\t\t\t";
  protected final String TEXT_96 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_97 = NL + "\t\t\t    if (compare != 0) {" + NL + "\t\t\t        return compare;" + NL + "\t\t\t    }" + NL + "\t\t\t}" + NL + "        }" + NL + "\t\t\t";
  protected final String TEXT_98 = ");" + NL + "\t\t\t\t" + NL + "\t\t\t    compare = arg0_";
  protected final String TEXT_99 = " == null) {" + NL + "\t\t\t    return 1;" + NL + "\t\t\t} else {" + NL + "\t\t\t    compare = arg0.";
  protected final String TEXT_100 = "(arg1.";
  protected final String TEXT_101 = NL + "        return Long.compare(arg0.id_";
  protected final String TEXT_102 = ", arg1.id_";
  protected final String TEXT_103 = ");" + NL + "    }" + NL + "" + NL + "}" + NL + "" + NL + "int bufferSize_1_";
  protected final String TEXT_104 = " = bufferSize_";
  protected final String TEXT_105 = ";" + NL + "" + NL + "rowStruct_";
  protected final String TEXT_106 = "[] buffer_1_";
  protected final String TEXT_107 = " = new rowStruct_";
  protected final String TEXT_108 = "[bufferSize_1_";
  protected final String TEXT_109 = "];" + NL + "" + NL + "for(int i_";
  protected final String TEXT_110 = " = 0; i_";
  protected final String TEXT_111 = " < buffer_1_";
  protected final String TEXT_112 = ".length; i_";
  protected final String TEXT_113 = "++){" + NL + "\tbuffer_1_";
  protected final String TEXT_114 = "[i_";
  protected final String TEXT_115 = "] = new rowStruct_";
  protected final String TEXT_116 = "();" + NL + "}" + NL + "" + NL + "int rowsInBuffer_1_";
  protected final String TEXT_117 = " = 0;" + NL + "" + NL + "Comparator<rowStruct_";
  protected final String TEXT_118 = "> comparator_1_";
  protected final String TEXT_119 = " = new Comparator_1_";
  protected final String TEXT_120 = "();" + NL + "" + NL + "java.util.ArrayList<java.io.File> files_1_";
  protected final String TEXT_121 = " = new java.util.ArrayList<java.io.File>();" + NL + "" + NL + "String temp_file_path_prefix_";
  protected final String TEXT_122 = " + \"/\" + jobName + \"_";
  protected final String TEXT_123 = "_\" + Thread.currentThread().getId() + \"_\" + pid  + \"_\";" + NL;
  protected final String TEXT_124 = NL;

  public String generate(Object argument)
  {
    final StringBuffer stringBuffer = new StringBuffer();
    stringBuffer.append(TEXT_1);
    
CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
INode node = (INode)codeGenArgument.getArgument();

String directory = ElementParameterParser.getValue(node, "__TEMP_DIRECTORY__");
String buffer = ElementParameterParser.getValue(node, "__BUFFER_SIZE__");
final boolean isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));
String bufferSize = "1000000";
if(("S").equals(buffer)){
	bufferSize = "500000";
}else if(("B").equals(buffer)){
	bufferSize = "2000000";
} else if(("M").equals(buffer)){
	bufferSize = "1000000";
} else {
	bufferSize = buffer;
}

if (("").equals(directory)) { 
	directory=ElementParameterParser.getValue(node.getProcess(), "__COMP_DEFAULT_FILE_DIR__") + "/temp"; 
} 
String cid = ElementParameterParser.getValue(node, "__CID__");
boolean ignoreTrailingZerosForBigDecimal = "true".equalsIgnoreCase(ElementParameterParser.getValue(node, "__IGNORE_TRAILING_ZEROS_FOR_BIGDECIMAL__"));

String connName = "";
if (node.getIncomingConnections().size()==1) {
	IConnection conn = node.getIncomingConnections().get(0);
	connName = conn.getName();
}

///////////////
List<IMetadataTable> metadatas = node.getMetadataList();
if ((metadatas!=null)&&(metadatas.size()>0) && !("").equals(connName)) {//HSS_____0
    IMetadataTable metadata = metadatas.get(0);
    List<IMetadataColumn> columnList = metadata.getListColumns();
    List<Map<String, String>> keyColumns = (List<Map<String,String>>)ElementParameterParser.getObjectValue(node, "__UNIQUE_KEY__");
	List<IMetadataColumn> uniqueColumnList = new java.util.ArrayList<IMetadataColumn>();
	List<Boolean> uniqueColumnCaseFlagList = new java.util.ArrayList<Boolean>();
	for(int i = 0; i < keyColumns.size(); i++){
		Map<String, String> keyColumn = keyColumns.get(i);
		if(("true").equals(keyColumn.get("KEY_ATTRIBUTE"))){
			uniqueColumnList.add(columnList.get(i));
			uniqueColumnCaseFlagList.add(!("true").equals(keyColumn.get("CASE_SENSITIVE")));
		}
	}

    stringBuffer.append(TEXT_2);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_3);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(bufferSize );
    stringBuffer.append(TEXT_5);
    
if (isLog4jEnabled) {

    stringBuffer.append(TEXT_6);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_7);
    
}

    stringBuffer.append(TEXT_8);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_9);
    stringBuffer.append(connName );
    stringBuffer.append(TEXT_10);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_11);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_12);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_13);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_14);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_15);
    
	
	for(int i = 0; i < uniqueColumnList.size(); i++){//HSS_____0_____2
		IMetadataColumn keyColumn = uniqueColumnList.get(i);
		Boolean flag = uniqueColumnCaseFlagList.get(i);
		JavaType javaType = JavaTypesManager.getJavaTypeFromId(keyColumn.getTalendType());
		boolean nullable = keyColumn.isNullable();
		
		if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG){//HSS_____0_____2_____1
			if(nullable){
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_19);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_21);
    
			}else{
			
    stringBuffer.append(TEXT_22);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_23);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_24);
    
			}
		}else if(javaType == JavaTypesManager.FLOAT || javaType == JavaTypesManager.DOUBLE){//HSS_____0_____2_____1
			if(nullable){
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_25);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_27);
    
			}else{
			
    stringBuffer.append(TEXT_28);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_26);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_29);
    
			}
		}else if(javaType == JavaTypesManager.BOOLEAN){//HSS_____0_____2_____1
			if(nullable){
				
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_30);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_32);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_34);
    
			}else{
				
    stringBuffer.append(TEXT_22);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_35);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_33);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_36);
    
			}
		}else if(javaType == JavaTypesManager.BYTE_ARRAY){//HSS_____0_____2_____1
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    
		}else if(javaType == JavaTypesManager.DATE){//HSS_____0_____2_____1
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_45);
    
		}else if(javaType == JavaTypesManager.BIGDECIMAL){//HSS_____0_____2_____1
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_47);
    
			if (ignoreTrailingZerosForBigDecimal) {
			
    stringBuffer.append(TEXT_48);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_49);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_50);
    
			} else {
			
    stringBuffer.append(TEXT_48);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_20);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_51);
    
			}
			
    stringBuffer.append(TEXT_52);
    
		}else if(javaType == JavaTypesManager.OBJECT){//HSS_____0_____2_____1
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    
		}else if(javaType == JavaTypesManager.STRING){//HSS_____0_____2_____1
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_46);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_43);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_53);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_45);
    
		}else if(javaType == JavaTypesManager.LIST){//HSS_____0_____2_____1
			
    stringBuffer.append(TEXT_16);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_17);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_18);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_37);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_39);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_40);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_41);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_42);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_43);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_44);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_45);
    
		}//HSS_____0_____2_____1
		
	}//HSS_____0_____2
	

    stringBuffer.append(TEXT_54);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_55);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_57);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_58);
    
	
	for(int i = 0; i < uniqueColumnList.size(); i++){//HSS_____0_____3
		IMetadataColumn keyColumn = uniqueColumnList.get(i);
		Boolean flag = uniqueColumnCaseFlagList.get(i);
		JavaType javaType = JavaTypesManager.getJavaTypeFromId(keyColumn.getTalendType());
		boolean nullable = keyColumn.isNullable();
		
		if(javaType == JavaTypesManager.BYTE || javaType == JavaTypesManager.CHARACTER 
					|| javaType == JavaTypesManager.SHORT || javaType == JavaTypesManager.INTEGER 
					|| javaType == JavaTypesManager.LONG){//HSS_____0_____3_____1
			if(nullable){
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_64);
    
			}else{
			
    stringBuffer.append(TEXT_65);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_67);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_69);
    
			}
		}else if(javaType == JavaTypesManager.FLOAT || javaType == JavaTypesManager.DOUBLE){//HSS_____0_____3_____1
			if(nullable){
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_70);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_72);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_73);
    
			}else{
			
    stringBuffer.append(TEXT_74);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_71);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_75);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_66);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_76);
    
			}
		}else if(javaType == JavaTypesManager.BOOLEAN){//HSS_____0_____3_____1
			if(nullable){
				
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_77);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_79);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_81);
    
			}else{
				
    stringBuffer.append(TEXT_65);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_78);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_82);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_80);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_83);
    
			}
		}else if(javaType == JavaTypesManager.BYTE_ARRAY){//HSS_____0_____3_____1
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_88);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    
		}else if(javaType == JavaTypesManager.DATE){//HSS_____0_____3_____1
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_62);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_64);
    
		}else if(javaType == JavaTypesManager.BIGDECIMAL){//HSS_____0_____3_____1
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_92);
    
				if (ignoreTrailingZerosForBigDecimal) {
				
    stringBuffer.append(TEXT_93);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_94);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_95);
    
				} else {
				
    stringBuffer.append(TEXT_93);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_63);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_96);
    
				}
				
    stringBuffer.append(TEXT_97);
    
		}else if(javaType == JavaTypesManager.OBJECT){//HSS_____0_____3_____1
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    
		}else if(javaType == JavaTypesManager.STRING){//HSS_____0_____3_____1
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_99);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_89);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_100);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_64);
    
		}else if(javaType == JavaTypesManager.LIST){//HSS_____0_____3_____1
			
    stringBuffer.append(TEXT_59);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_60);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_85);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_86);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_98);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_89);
    stringBuffer.append((flag ? "IgnoreCase" : "") );
    stringBuffer.append(TEXT_90);
    stringBuffer.append(keyColumn.getLabel() );
    stringBuffer.append(TEXT_38);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_91);
    
		}//HSS_____0_____3_____1
		
	}//HSS_____0_____3
	

    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_104);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_109);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_115);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_116);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_117);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_118);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_119);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_120);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_121);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_4);
    stringBuffer.append(directory );
    stringBuffer.append(TEXT_122);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_123);
    
}//HSS_____0

    stringBuffer.append(TEXT_124);
    return stringBuffer.toString();
  }
}
