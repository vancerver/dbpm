<cfset cfAdminPass = url['cfAdminPass'] />
<cfset dbUserName = url['dbUserName'] />
<cfset dbHostUrl = url['dbHostUrl'] />
<cfset dbName = url['dbName'] />
<cfset dbPort = url['dbPort'] />
<cfset dsnName = url['dsnName'] />


<cfscript>
    // Login is always required. This example uses a single line of code.
    createObject("component","cfide.adminapi.administrator").login(#cfAdminPass#);

    // Instantiate the data source object.
    myObj = createObject("component","cfide.adminapi.datasource");

    // Required arguments for a data source.
    stDSN = structNew();
    stDSN.driver = "MSSQLServer";
    stDSN.name= #dsnName#;
    stDSN.host = #dbHostUrl#;
    stDSN.port = #port#;
    stDSN.database = #dbName#;
    stDSN.username = #dbUserName#;
    stDSN.password = #newPassword#

    // Optional and advanced arguments.
    stDSN.login_timeout = "29";
    stDSN.timeout = "23";
    stDSN.interval = 6;
    stDSN.buffer = "64000";
    stDSN.blob_buffer = "64000";
    stDSN.setStringParameterAsUnicode = "false";
    stDSN.description = "Northwind SQL Server";
    stDSN.pooling = true;
    stDSN.maxpooledstatements = 999;
    stDSN.enableMaxConnections = "true";
    stDSN.maxConnections = "299";
    stDSN.alter = false;
    stDSN.update = true;

    //Create a DSN.
    myObj.setMSSQL(argumentCollection=stDSN);	
</cfscript>