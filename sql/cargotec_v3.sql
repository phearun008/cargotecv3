/*
Navicat SQL Server Data Transfer

Source Server         : CARGOTEC
Source Server Version : 110000
Source Host           : 192.168.178.109:1433
Source Database       : EXCEL_DB
Source Schema         : dbo

Target Server Type    : SQL Server
Target Server Version : 110000
File Encoding         : 65001

Date: 2016-09-05 18:30:02
*/


-- ----------------------------
-- Table structure for content_v3
-- ----------------------------
DROP TABLE [content_v3]
GO
CREATE TABLE [content_v3] (
[id] int NOT NULL IDENTITY(1,1) ,
[chapter] varchar(255) NULL ,
[description] varchar(255) NULL ,
[reference] varchar(255) NULL ,
[page] varchar(255) NULL ,
[section_title] varchar(255) NULL ,
[section_image_location] varchar(255) NULL ,
[section_number] varchar(255) NULL ,
[file_id] int NULL ,
[section_reference] varchar(255) NULL 
)


GO
DBCC CHECKIDENT(N'[content_v3]', RESEED, 129)
GO

-- ----------------------------
-- Records of content_v3
-- ----------------------------
BEGIN TRANSACTION
GO
SET IDENTITY_INSERT [content_v3] ON
GO
SET IDENTITY_INSERT [content_v3] OFF
GO
COMMIT TRANSACTION
GO

-- ----------------------------
-- Table structure for description_v3
-- ----------------------------
DROP TABLE [description_v3]
GO
CREATE TABLE [description_v3] (
[pos] varchar(255) NULL ,
[qty] varchar(255) NULL ,
[part_no] varchar(255) NULL ,
[description] varchar(255) NULL ,
[remarks] varchar(255) NULL ,
[content_id] int NULL ,
[id] int NOT NULL IDENTITY(1,1) 
)


GO
DBCC CHECKIDENT(N'[description_v3]', RESEED, 15290)
GO

-- ----------------------------
-- Records of description_v3
-- ----------------------------
BEGIN TRANSACTION
GO
SET IDENTITY_INSERT [description_v3] ON
GO
SET IDENTITY_INSERT [description_v3] OFF
GO
COMMIT TRANSACTION
GO

-- ----------------------------
-- Table structure for file_v3
-- ----------------------------
DROP TABLE [file_v3]
GO
CREATE TABLE [file_v3] (
[name] varchar(255) NULL ,
[create_date] datetime2(7) NULL ,
[id] int NOT NULL IDENTITY(1,1) 
)


GO
DBCC CHECKIDENT(N'[file_v3]', RESEED, 5)
GO

-- ----------------------------
-- Records of file_v3
-- ----------------------------
BEGIN TRANSACTION
GO
SET IDENTITY_INSERT [file_v3] ON
GO
SET IDENTITY_INSERT [file_v3] OFF
GO
COMMIT TRANSACTION
GO

-- ----------------------------
-- Indexes structure for table content_v3
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table content_v3
-- ----------------------------
ALTER TABLE [content_v3] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table description_v3
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table description_v3
-- ----------------------------
ALTER TABLE [description_v3] ADD PRIMARY KEY ([id])
GO

-- ----------------------------
-- Indexes structure for table file_v3
-- ----------------------------

-- ----------------------------
-- Primary Key structure for table file_v3
-- ----------------------------
ALTER TABLE [file_v3] ADD PRIMARY KEY ([id])
GO
