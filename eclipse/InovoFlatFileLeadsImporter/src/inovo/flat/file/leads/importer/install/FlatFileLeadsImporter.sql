CREATE TABLE <DBUSER>.[LEADSDATAFILEALIAS](
	[ID] [numeric](18, 0) IDENTITY(1,1) NOT NULL,
	[ALIAS] [varchar](1000) NOT NULL,
	[ENABLEALIAS] [varchar](10) DEFAULT ('FALSE'),
	[PREBATCHIMPORTSQLCOMMAND] [varchar](8000) NULL,
	[POSTBATCHIMPORTSQLCOMMAND] [varchar](8000) NULL,
	[ALTERNATESOURCEIDSQLCOMMAND] [varchar](8000) NULL,
	[POSTLEADREQUESTSQLCOMMAND] [varchar](8000) NULL,
	[SOURCEIDFIELD] [varchar](1000) NULL,
	[SERVICEIDFIELD] [varchar](1000) NULL,
	[DEFAULTSERVICEID] [numeric](18, 0) DEFAULT 0,
	[LOADIDFIELD] [varchar](1000) NULL,
	[DEFAULTLOADID] [numeric](18, 0) DEFAULT 0,
	[PRIORITYFIELD] [varchar](1000) NULL,
	[DEFAULTPRIORITY] [numeric](18, 0) DEFAULT 0,
	[LEADREQUESTTYPEFIELD] [varchar](1000) NULL,
	[DEFAULTLEADLEADREQUESTTYPE] [numeric] (18,0) DEFAULT 1,
	[CALLERNAMEFIELDS] [varchar](1000) NULL,
	[COMMENTSFIELDS] [varchar](1000) NULL,
	[COMADELIM] [varchar] (1) DEFAULT ',',
	[USEFILENAMEASLOADNAME] [varchar](10) DEFAULT 'FALSE',
	[FORCECREATINGLOAD] [varchar](10) DEFAULT 'FALSE',
	[PHONE1FIELD] [varchar](30) NULL,
	[PHONE2FIELD] [varchar](30) NULL,
	[PHONE3FIELD] [varchar](30) NULL,
	[PHONE4FIELD] [varchar](30) NULL,
	[PHONE5FIELD] [varchar](30) NULL,
	[PHONE6FIELD] [varchar](30) NULL,
	[PHONE7FIELD] [varchar](30) NULL,
	[PHONE8FIELD] [varchar](30) NULL,
	[PHONE9FIELD] [varchar](30) NULL,
	[PHONE10FIELD] [varchar](30) NULL,
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
	[SOURCEPATH] [varchar](4000) NULL,
	[CURRENTFILEFIELDS] [varchar](8000) NULL,
	[ENABLENEWLOAD] [varchar](10) DEFAULT 'FALSE',
	[LOADNAMEMASK] [varchar](8000) NULL,
	[FILELOOKUPMASK] [varchar](4000) NULL,
 CONSTRAINT [PK_LEADSDATAFILEALIAS] PRIMARY KEY CLUSTERED 
(
	[ID] ASC
))