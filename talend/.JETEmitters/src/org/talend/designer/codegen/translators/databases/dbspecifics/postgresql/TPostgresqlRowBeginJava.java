package org.talend.designer.codegen.translators.databases.dbspecifics.postgresql;

import org.talend.core.model.process.INode;
import org.talend.core.model.process.ElementParameterParser;
import org.talend.designer.codegen.config.CodeGeneratorArgument;

public class TPostgresqlRowBeginJava
{
  protected static String nl;
  public static synchronized TPostgresqlRowBeginJava create(String lineSeparator)
  {
    nl = lineSeparator;
    TPostgresqlRowBeginJava result = new TPostgresqlRowBeginJava();
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
  protected final String TEXT_18 = NL + "\t\t\t\tif(conn_";
  protected final String TEXT_19 = " != null) {" + NL + "\t\t\t\t\tif(conn_";
  protected final String TEXT_20 = ".getMetaData() != null) {" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_21 = NL + "\t\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_22 = " - Uses an existing connection ";
  protected final String TEXT_23 = ".\");" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_24 = NL + "\t\t\t\t\t\t\tlog.debug(\"";
  protected final String TEXT_25 = " - Uses an existing connection with username '\" + conn_";
  protected final String TEXT_26 = ".getMetaData().getUserName() + \"'. Connection URL: \" + conn_";
  protected final String TEXT_27 = ".getMetaData().getURL() + \".\");" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_28 = NL + "\t\t\t\t\t}" + NL + "\t\t\t\t}" + NL + "\t\t\t";
  protected final String TEXT_29 = NL + "\t\t\tconn_";
  protected final String TEXT_30 = " = java.sql.DriverManager.getConnection(url_";
  protected final String TEXT_31 = ", dbUser_";
  protected final String TEXT_32 = ", dbPwd_";
  protected final String TEXT_33 = ");" + NL + "\t\t\t";
  protected final String TEXT_34 = ".rollback();" + NL + "\t\t\t";
  protected final String TEXT_35 = ".commit();" + NL + "\t\t\t";
  protected final String TEXT_36 = ".close();" + NL + "\t\t\t";
  protected final String TEXT_37 = NL + "\t\t\tif(\"com.mysql.cj.jdbc.Driver\".equals((String)globalMap.get(\"driverClass_";
  protected final String TEXT_38 = "\"))" + NL + "\t\t\t    && routines.system.BundleUtils.inOSGi()) {" + NL + "\t\t\t        Class.forName(\"com.mysql.cj.jdbc.AbandonedConnectionCleanupThread\")." + NL + "\t\t\t            getMethod(\"checkedShutdown\").invoke(null, (Object[]) null);" + NL + "\t\t\t}" + NL + "\t\t\t";
  protected final String TEXT_39 = NL + "\t\t\t\tconn_";
  protected final String TEXT_40 = ".setAutoCommit(";
  protected final String TEXT_41 = NL + "\t\t\t\tlog.";
  protected final String TEXT_42 = "(\"";
  protected final String TEXT_43 = " - \" + ";
  protected final String TEXT_44 = ".getMessage());" + NL + "\t\t\t";
  protected final String TEXT_45 = NL + "\t    \t\tlog.";
  protected final String TEXT_46 = "\");" + NL + "\t\t\t";
  protected final String TEXT_47 = NL + "\t\t\t\tpstmt_";
  protected final String TEXT_48 = ".executeBatch();" + NL + "\t\t\t";
  protected final String TEXT_49 = NL + "\t\t\t\tint countSum_";
  protected final String TEXT_50 = " = 0;" + NL + "\t\t\t\tfor(int countEach_";
  protected final String TEXT_51 = ": pstmt_";
  protected final String TEXT_52 = ".executeBatch()) {" + NL + "\t\t\t\t\tcountSum_";
  protected final String TEXT_53 = " += (countEach_";
  protected final String TEXT_54 = " < 0 ? 0 : ";
  protected final String TEXT_55 = ");" + NL + "\t\t\t\t}" + NL + "\t\t\t";
  protected final String TEXT_56 = NL + "\t\t\tString driverClass_";
  protected final String TEXT_57 = " = \"";
  protected final String TEXT_58 = "\";" + NL + "\t\t    java.lang.Class jdbcclazz_";
  protected final String TEXT_59 = " = java.lang.Class.forName(driverClass_";
  protected final String TEXT_60 = ");" + NL + "\t\t";
  protected final String TEXT_61 = ",dbUser_";
  protected final String TEXT_62 = ",dbPwd_";
  protected final String TEXT_63 = " = (java.sql.Connection)globalMap.get(\"";
  protected final String TEXT_64 = NL + "\t\t            java.util.Map<String, routines.system.TalendDataSource> dataSources_";
  protected final String TEXT_65 = " = (java.util.Map<String, routines.system.TalendDataSource>) globalMap.get(KEY_DB_DATASOURCES);" + NL + "\t\t            if (null != dataSources_";
  protected final String TEXT_66 = ") {" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_67 = NL + "            \t\t\tString dsAlias_";
  protected final String TEXT_68 = " = ";
  protected final String TEXT_69 = ";" + NL + "                \t\tif (dataSources_";
  protected final String TEXT_70 = ".get(dsAlias_";
  protected final String TEXT_71 = ") == null) {" + NL + "                   \t\t\tthrow new RuntimeException(\"No DataSource with alias: \" + dsAlias_";
  protected final String TEXT_72 = " + \" available!\");" + NL + "                      \t}" + NL + "                \t\tconn_";
  protected final String TEXT_73 = " = dataSources_";
  protected final String TEXT_74 = ").getConnection();" + NL + "\t\t            } else {" + NL + "\t\t\t\t";
  protected final String TEXT_75 = NL + "\t\t\t\t\tString dbUser_";
  protected final String TEXT_76 = ";" + NL + "\t        " + NL + "            \t\t";
  protected final String TEXT_77 = NL + "            \t\t" + NL + "            \t\t";
  protected final String TEXT_78 = " " + NL + "\tfinal String decryptedPassword_";
  protected final String TEXT_79 = " = routines.system.PasswordEncryptUtil.decryptPassword(";
  protected final String TEXT_80 = ");";
  protected final String TEXT_81 = NL + "\tfinal String decryptedPassword_";
  protected final String TEXT_82 = "; ";
  protected final String TEXT_83 = NL + "        \t\t   \t" + NL + "        \t        String dbPwd_";
  protected final String TEXT_84 = " = decryptedPassword_";
  protected final String TEXT_85 = ";" + NL + "\t        " + NL + "\t\t\t\t\t";
  protected final String TEXT_86 = NL + "\t\t\t\t\tconn_";
  protected final String TEXT_87 = ");" + NL + "\t\t\t\t";
  protected final String TEXT_88 = NL + "\t\t\t    }" + NL + "\t\t\t\t";
  protected final String TEXT_89 = NL + "\t\t\t\t\tif(conn_";
  protected final String TEXT_90 = ".getAutoCommit()) {" + NL + "\t\t\t\t\t\t";
  protected final String TEXT_91 = NL + "\t\t\t\t\t}        " + NL + "\t\t\t\t\tint commitEvery_";
  protected final String TEXT_92 = ";" + NL + "\t\t\t\t\tint commitCounter_";
  protected final String TEXT_93 = " = 0;" + NL + "\t\t\t\t";
  protected final String TEXT_94 = NL + "\t\t\t\tString url_";
  protected final String TEXT_95 = " = \"jdbc:postgresql://\"+";
  protected final String TEXT_96 = "+\":\"+";
  protected final String TEXT_97 = "+\"/\"+";
  protected final String TEXT_98 = ";" + NL + "\t\t\t\t";
  protected final String TEXT_99 = " + \"?\" + ";
  protected final String TEXT_100 = ";" + NL + "\t\t";
  protected final String TEXT_101 = NL + "\tjava.sql.Connection conn_";
  protected final String TEXT_102 = " = null;" + NL + "\tString query_";
  protected final String TEXT_103 = " = \"\";" + NL + "\tboolean whetherReject_";
  protected final String TEXT_104 = " = false;";
  protected final String TEXT_105 = NL + "    resourceMap.put(\"conn_";
  protected final String TEXT_106 = "\", conn_";
  protected final String TEXT_107 = NL + "        java.sql.PreparedStatement pstmt_";
  protected final String TEXT_108 = " = conn_";
  protected final String TEXT_109 = ".prepareStatement(";
  protected final String TEXT_110 = ");" + NL + "        resourceMap.put(\"pstmt_";
  protected final String TEXT_111 = "\", pstmt_";
  protected final String TEXT_112 = NL + "        java.sql.Statement stmt_";
  protected final String TEXT_113 = ".createStatement();" + NL + "        resourceMap.put(\"stmt_";
  protected final String TEXT_114 = "\", stmt_";
  protected final String TEXT_115 = NL;
  protected final String TEXT_116 = NL;

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
	
    
	class DefaultLog4jCodeGenerateUtil extends DefaultLog4jFileUtil{

 		String connection = "";
 		boolean hasInit = false;
 		String dataAction ;
 		String dataOperationPrefix;
		String useBatchSize;
		String batchSize;
		String dbSchema;
 		boolean logCommitCounter = false;

		public DefaultLog4jCodeGenerateUtil(){
		}

		public DefaultLog4jCodeGenerateUtil(INode node) {
			super(node);
	    	init();
		}

	    public void beforeComponentProcess(INode node){
	    	this.node = node;
	    	init();
	    }

		private void init() {
			if(hasInit){
				return;
			}
 			this.cid = node.getUniqueName();
			this.isLog4jEnabled = ("true").equals(ElementParameterParser.getValue(node.getProcess(), "__LOG4J_ACTIVATE__"));
			String useConn = ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__");
			if(useConn == null || "".equals(useConn) || "true".equals(useConn)){
				connection = ElementParameterParser.getValue(node,"__CONNECTION__");
				if(!"".equals(connection)){
					connection = "'" + connection+"' ";
				}
			}
			//for output
			dataAction = ElementParameterParser.getValue(node,"__DATA_ACTION__");
			if(dataAction != null && !("").equals(dataAction)){
				logCommitCounter=true;
			}
			useBatchSize = ElementParameterParser.getValue(node, "__USE_BATCH_SIZE__");
			batchSize =ElementParameterParser.getValue(node, "__BATCH_SIZE__");
			hasInit = true;
		}

		public void debugDriverClassName() {
			logInfo(node,"debug",cid+" - Driver ClassName: \"+driverClass_"+cid+"+\".");
		}

		public void debugConnectionParams(INode node) {
			beforeComponentProcess(node);
			debugDriverClassName();
		}

		public void useExistConnection(INode node){
			beforeComponentProcess(node);
			if(isLog4jEnabled) {
			
    stringBuffer.append(TEXT_18);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_19);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_20);
    if (cid.contains("tImpala") || cid.contains("tHive")) {
    stringBuffer.append(TEXT_21);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_22);
    stringBuffer.append(connection );
    stringBuffer.append(TEXT_23);
    } else {
    stringBuffer.append(TEXT_24);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_25);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_26);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_27);
    }
    stringBuffer.append(TEXT_28);
    
			}
		}

		public void connect(INode node){
			beforeComponentProcess(node);
			connect();
		}

		public void connect(){
			connect_begin();
			
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_31);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_32);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_33);
    
			connect_end();
		}
		
		public void connect_begin(){
			logInfo(node,"debug",cid+" - Connection attempt to '\" + url_"+cid+" + \"' with the username '\" + dbUser_"+cid+" + \"'.");
		}

		public void connect_begin_noUser(){
			logInfo(node,"debug",cid+" - Connection attempt to '\" + url_"+cid+" + \"'.");
		}

		public void connect_end(){
			logInfo(node,"debug",cid+" - Connection to '\" + url_"+cid+" + \"' has succeeded.");
		}

		public void rollback(INode node){
			beforeComponentProcess(node);
			logInfo(node,"debug",cid+" - Connection "+connection+"starting to rollback.");
			
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_34);
    
			logInfo(node,"debug",cid+" - Connection "+connection+"rollback has succeeded.");
		}

		public void commit(INode node){
			beforeComponentProcess(node);
			commit();
		}

		private void commit(){
			commit_begin();
			
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_35);
    
			commit_end();
		}

		private void commit_begin(){
			if(logCommitCounter){
				logInfo(node,"debug",cid+" - Connection "+connection+"starting to commit \" + commitCounter_"+cid+"+ \" records.");
			}else{
				logInfo(node,"debug",cid+" - Connection "+connection+"starting to commit.");
			}
		}
		private void commit_end(){
			logInfo(node,"debug",cid+" - Connection "+connection+"commit has succeeded.");
		}

		public void close(INode node){
			beforeComponentProcess(node);
			close();
		}

		private void close(){
			close_begin();
			
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_36);
     /* TESB-24900 - graceful shutdown for MYSQL connection */ 
    stringBuffer.append(TEXT_37);
    stringBuffer.append((connection!=null)?connection.replaceAll("'","").trim():"");
    stringBuffer.append(TEXT_38);
    
			close_end();
		}

		public void close_begin(){
			logInfo(node,"debug",cid+" - Closing the connection "+connection+"to the database.");
		}
		public void close_end(){
			logInfo(node,"debug",cid+" - Connection "+connection+"to the database closed.");
		}

		public void autoCommit(INode node,boolean autoCommit){
			beforeComponentProcess(node);
			logInfo(node,"debug",cid+" - Connection is set auto commit to '"+autoCommit+"'.");
			
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_40);
    stringBuffer.append(autoCommit);
    stringBuffer.append(TEXT_33);
    
		}

		public void query(INode node){
			beforeComponentProcess(node);
			//for input
	 		String dbquery= ElementParameterParser.getValue(node, "__QUERY__");
			dbquery = org.talend.core.model.utils.NodeUtil.replaceCRLFInMEMO_SQL(dbquery);
			logInfo(node,"debug",cid+" - Executing the query: '\" + "+dbquery +" + \"'.");
		}

		public void retrieveRecordsCount(INode node){
			beforeComponentProcess(node);
			logInfo(node,"debug",cid+" - Retrieved records count: \"+nb_line_"+cid+" + \" .");
		}

		public void logError(INode node,String logLevel,String exception){
	    	beforeComponentProcess(node);
	    	if(isLog4jEnabled){
	    	
    stringBuffer.append(TEXT_41);
    stringBuffer.append(logLevel);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_43);
    stringBuffer.append(exception);
    stringBuffer.append(TEXT_44);
    
			}
	    }

	    public void logError(INode node,String logLevel){
	    	logError(node,logLevel,"e");
	    }
	    
	    public void logInfo(INode node,String logLevel,String message){
	    	beforeComponentProcess(node);
	    	if(isLog4jEnabled){
	    	
    stringBuffer.append(TEXT_45);
    stringBuffer.append(logLevel);
    stringBuffer.append(TEXT_42);
    stringBuffer.append(message);
    stringBuffer.append(TEXT_46);
    
			}
	    }
		/**
		*batchType :
		*			1: do not get return value of executeBatch();
		*			2: get return value of executeBatch();
		*
		*/
		public void executeBatch(INode node,int batchType){
			beforeComponentProcess(node);
			boolean logBatch = ("true").equals(useBatchSize) && !("").equals(batchSize) && !("0").equals(batchSize);
			if(logBatch){
				logInfo(node,"debug",cid+" - Executing the "+dataAction+" batch.");
			}
			if(batchType==1){
			
    stringBuffer.append(TEXT_47);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_48);
    
			}else if(batchType==2){
				boolean isMysqlBatchInsert = false;
				if ((node.getUniqueName().contains("tMysqlOutput")||node.getUniqueName().contains("tAmazonMysqlOutput")) && ("INSERT").equals(dataAction)) {
					isMysqlBatchInsert = true;
				}
			
    stringBuffer.append(TEXT_49);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_50);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_51);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_52);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_53);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_54);
    stringBuffer.append(isMysqlBatchInsert? "1" : "countEach_"+cid );
    stringBuffer.append(TEXT_55);
    
			}
			if(logBatch){
				logInfo(node,"debug",cid+" - The "+dataAction+" batch execution has succeeded.");
			}
		}
	}

	DefaultLog4jCodeGenerateUtil log4jCodeGenerateUtil = new DefaultLog4jCodeGenerateUtil();

    
	//this util class use by connection component
	class DefaultDBRowUtil {
	
		protected String cid ;
		protected String dbproperties ;
		protected String dbhost;
	    protected String dbport;
	    protected String dbname;
	    protected String dbuser;
		protected String dbpwd ;
		protected String useExistingConn;
		protected DefaultLog4jCodeGenerateUtil log4jCodeGenerateUtil;
	    
	    public void beforeComponentProcess(INode node){
	    	cid = node.getUniqueName();
	    	log4jCodeGenerateUtil = new DefaultLog4jCodeGenerateUtil();
			dbproperties = ElementParameterParser.getValue(node, "__PROPERTIES__");
			dbhost = ElementParameterParser.getValue(node, "__HOST__");
	    	dbport = ElementParameterParser.getValue(node, "__PORT__");
	    	dbname = ElementParameterParser.getValue(node, "__DBNAME__");
	    	dbuser = ElementParameterParser.getValue(node, "__USER__");

	    }
	    
	    public void afterUseExistConnection(INode node) {
	    	log4jCodeGenerateUtil.useExistConnection(node);
	    }
		
	    public String getDirverClassName(INode node){
			return "";
		}
	    
	    public void setURL(INode node) {
	    }
	    
		public void createConnection(INode node) {
			this.createConnection(node, true);
		}

		public void classForName(INode node){
		
    stringBuffer.append(TEXT_56);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_57);
    stringBuffer.append(this.getDirverClassName(node) );
    stringBuffer.append(TEXT_58);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_59);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    
			newInstance(node);
		}
		
		protected void newInstance(INode node) {
			//do nothing as default
			//for sybase jdbc in java 11, we have to call jdbcclazz.newInstance method
		}
		
		public void newConnection(){
		
    stringBuffer.append(TEXT_29);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_61);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_62);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_60);
    
		}

		public void createConnection(INode node, boolean needUserAndPassword) {
	 		useExistingConn = ElementParameterParser.getValue(node,"__USE_EXISTING_CONNECTION__");
	 		if(("true").equals(useExistingConn)) {
				String connection = ElementParameterParser.getValue(node,"__CONNECTION__");
				String conn = "conn_" + connection;;
				
    stringBuffer.append(TEXT_39);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_63);
    stringBuffer.append(conn);
    stringBuffer.append(TEXT_46);
    
					this.afterUseExistConnection(node);
			} else {
	 			boolean specify_alias = "true".equals(ElementParameterParser.getValue(node, "__SPECIFY_DATASOURCE_ALIAS__"));
		 		if(specify_alias){
				
    stringBuffer.append(TEXT_64);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_65);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_66);
    
		                String alias = ElementParameterParser.getValue(node, "__DATASOURCE_ALIAS__");
						
    stringBuffer.append(TEXT_67);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_68);
    stringBuffer.append((null != alias && !("".equals(alias)))?alias:"\"\"");
    stringBuffer.append(TEXT_69);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_71);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_72);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_73);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_70);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_74);
    
				}
				this.classForName(node);
				this.setURL(node);
				log4jCodeGenerateUtil.debugConnectionParams(node);
				if(needUserAndPassword) {
				
    stringBuffer.append(TEXT_75);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_68);
    stringBuffer.append(dbuser);
    stringBuffer.append(TEXT_76);
    
            		String passwordFieldName = "__PASS__";
            		
    stringBuffer.append(TEXT_77);
    if (ElementParameterParser.canEncrypt(node, passwordFieldName)) {
    stringBuffer.append(TEXT_78);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_79);
    stringBuffer.append(ElementParameterParser.getEncryptedValue(node, passwordFieldName));
    stringBuffer.append(TEXT_80);
    } else {
    stringBuffer.append(TEXT_81);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_68);
    stringBuffer.append( ElementParameterParser.getValue(node, passwordFieldName));
    stringBuffer.append(TEXT_82);
    }
    stringBuffer.append(TEXT_83);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_84);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_85);
    log4jCodeGenerateUtil.connect_begin();
					newConnection();
				} else {
					log4jCodeGenerateUtil.connect_begin_noUser();
				
    stringBuffer.append(TEXT_86);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_30);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_87);
    
				}
				log4jCodeGenerateUtil.connect_end();
				if(specify_alias){
				
    stringBuffer.append(TEXT_88);
    
				}
			}
		}

		public void setCommit(INode node){
			if(!("true").equals(useExistingConn)) {
				String commitEvery = ElementParameterParser.getValue(node, "__COMMIT_EVERY__");
				boolean useTransaction = !("false").equals(ElementParameterParser.getValue(node,"__USE_TRANSACTION__"));
				if(!("").equals(commitEvery) && !("0").equals(commitEvery) && useTransaction) {
				
    stringBuffer.append(TEXT_89);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_90);
    log4jCodeGenerateUtil.autoCommit(node,false);
    stringBuffer.append(TEXT_91);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_68);
    stringBuffer.append(commitEvery);
    stringBuffer.append(TEXT_92);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_93);
    
				}
			}
		}

		public String getQueryString(INode node) {
			String dbquery= ElementParameterParser.getValue(node, "__QUERY__");
			dbquery = org.talend.core.model.utils.NodeUtil.replaceCRLFInMEMO_SQL(dbquery);
			
			return dbquery;
		}
		
		public void afterConnection(INode node){
		}
	}//end DefaultDBInputUtil class
	
	DefaultDBRowUtil dbRowBeginUtil = new DefaultDBRowUtil();

    

	class DBRowBeginUtil extends DefaultDBRowUtil{
	
		public void beforeComponentProcess(INode node){
			super.beforeComponentProcess(node);
			cid = node.getUniqueName();
	    }
	    
		public void setURL(INode node) {
			if(dbproperties == null || ("\"\"").equals(dbproperties) || ("").equals(dbproperties)) {
		
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(dbhost);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(dbport);
    stringBuffer.append(TEXT_97);
    stringBuffer.append(dbname);
    stringBuffer.append(TEXT_98);
    
	        } else {
				
    stringBuffer.append(TEXT_94);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_95);
    stringBuffer.append(dbhost);
    stringBuffer.append(TEXT_96);
    stringBuffer.append(dbport);
    stringBuffer.append(TEXT_97);
    stringBuffer.append(dbname);
    stringBuffer.append(TEXT_99);
    stringBuffer.append(dbproperties);
    stringBuffer.append(TEXT_100);
    
			}
		}
		
		public String getDirverClassName(INode node){
			return "org.postgresql.Driver";
		}
	}
	dbRowBeginUtil = new DBRowBeginUtil();

    
	CodeGeneratorArgument codeGenArgument = (CodeGeneratorArgument) argument;
	INode node = (INode)codeGenArgument.getArgument();
	String cid = node.getUniqueName();
	String dbproperties = ElementParameterParser.getValue(node, "__PROPERTIES__");
	boolean usePrepareStatement = "true".equals(ElementParameterParser.getValue(node,"__USE_PREPAREDSTATEMENT__"));
	dbRowBeginUtil.beforeComponentProcess(node);
	log4jCodeGenerateUtil = dbRowBeginUtil.log4jCodeGenerateUtil;

    stringBuffer.append(TEXT_101);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_102);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_103);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_104);
    
    dbRowBeginUtil.createConnection(node);

    stringBuffer.append(TEXT_105);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_106);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_80);
    
    dbRowBeginUtil.setCommit(node);

    if (usePrepareStatement ) {

    stringBuffer.append(TEXT_107);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid);
    stringBuffer.append(TEXT_109);
    stringBuffer.append(dbRowBeginUtil.getQueryString(node));
    stringBuffer.append(TEXT_110);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_111);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    
    } else {

    stringBuffer.append(TEXT_112);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_108);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_113);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_114);
    stringBuffer.append(cid );
    stringBuffer.append(TEXT_80);
    
    }
	dbRowBeginUtil.afterConnection(node);
	
    stringBuffer.append(TEXT_115);
    stringBuffer.append(TEXT_116);
    return stringBuffer.toString();
  }
}
