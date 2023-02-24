CREATE TABLE <DBUSER>.[DYNAMICCALLERLIST](
	[ID] [numeric](18, 0) NOT NULL,
	[SERVICEID] [numeric](18, 0) NOT NULL,
	[LOADID] [numeric](18, 0) NOT NULL,
	[CALLERNAME] [varchar](1000) NOT NULL,
	[PHONE1] [varchar](100) NOT NULL,
	[PHONE1EXT] [varchar](100) NULL,
	[PHONE2] [varchar](100) NULL,
	[PHONE2EXT] [varchar](100) NULL,
	[PHONE3] [varchar](100) NULL,
	[PHONE3EXT] [varchar](100) NULL,
	[PHONE4] [varchar](100) NULL,
	[PHONE4EXT] [varchar](100) NULL,
	[PHONE5] [varchar](100) NULL,
	[PHONE5EXT] [varchar](100) NULL,
	[PHONE6] [varchar](100) NULL,
	[PHONE6EXT] [varchar](100) NULL,
	[PHONE7] [varchar](100) NULL,
	[PHONE7EXT] [varchar](100) NULL,
	[PHONE8] [varchar](100) NULL,
	[PHONE8EXT] [varchar](100) NULL,
	[PHONE9] [varchar](100) NULL,
	[PHONE9EXT] [varchar](100) NULL,
	[PHONE10] [varchar](100) NULL,
	[PHONE10EXT] [varchar](100) NULL,
	[SCHEDULEDCALL] [datetime] NULL,
	[COMMENTS] [varchar](1000) NULL,
	[LOADREQUESTTYPE] [numeric](3, 0) DEFAULT 1,
	[RECORDHANDLEFLAG] [numeric](1, 0) DEFAULT 1,
	[PRIORITY] [numeric](18, 0) NULL,
	[AGENTLOGINID] [varchar](10) NULL,
	[LASTACTIONDATETIME] [datetime] NULL,
	[METAFIELD1] [varchar](4000) NULL,
	[METAFIELD2] [varchar](4000) NULL,
	[METAFIELD3] [varchar](4000) NULL,
	[METAFIELD4] [varchar](4000) NULL,
	[METAFIELD5] [varchar](4000) NULL,
	[METAFIELD6] [varchar](4000) NULL,
	[METAFIELD7] [varchar](4000) NULL,
	[METAFIELD8] [varchar](4000) NULL,
	[METAFIELD9] [varchar](4000) NULL,
	[METAFIELD10] [varchar](4000) NULL,
	[METAFIELD11] [varchar](4000) NULL,
	[METAFIELD12] [varchar](4000) NULL,
	[METAFIELD13] [varchar](4000) NULL,
	[METAFIELD14] [varchar](4000) NULL,
	[METAFIELD15] [varchar](4000) NULL,
	[METAFIELD16] [varchar](4000) NULL,
	[METAFIELD17] [varchar](4000) NULL,
	[METAFIELD18] [varchar](4000) NULL,
	[METAFIELD19] [varchar](4000) NULL,
	[METAFIELD20] [varchar](4000) NULL,
	[FIELDSTOMODIFY] [varchar](8000) NULL,
 CONSTRAINT [PK_DYNAMICCALLERLIST] PRIMARY KEY CLUSTERED 
(
	[ID] ASC,
	[SERVICEID] ASC,
	[LOADID] ASC
))