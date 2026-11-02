-- 1. Create a database
CREATE DATABASE TestDB;
GO

-- 2. Use that database
USE TestDB;
GO

-- 3. Create a table
CREATE TABLE Students (
    StudentId INT IDENTITY(1,1) PRIMARY KEY,
    Name      NVARCHAR(100) NOT NULL,
    Age       INT           NOT NULL,
    City      NVARCHAR(100) NULL
);
GO

-- 4. Insert some sample data
INSERT INTO Students (Name, Age, City) VALUES
('Ali',   22, 'Karachi'),
('Sara',  21, 'Lahore'),
('Ahmed', 24, 'Islamabad');
GO

-- 5. Check that rows exist
SELECT * FROM Students;
GO


-- Create a SQL Server login
USE master;
GO
CREATE LOGIN testuser WITH PASSWORD = 'TestUser@12345', CHECK_POLICY = ON;
GO

-- Map that login to a user in TestDB
USE TestDB;
GO
CREATE USER testuser FOR LOGIN testuser;
ALTER ROLE db_datareader ADD MEMBER testuser;
ALTER ROLE db_datawriter ADD MEMBER testuser;
GO

SELECT DISTINCT
    protocol_desc,
    local_net_address,
    local_tcp_port
FROM sys.dm_exec_connections
WHERE local_tcp_port IS NOT NULL;

SELECT @@VERSION;

SELECT DISTINCT
    local_tcp_port
FROM sys.dm_exec_connections
WHERE local_tcp_port IS NOT NULL;




-- Remove the user from TestDB if it exists
USE TestDB;
GO
DROP USER IF EXISTS testuser;
GO

-- Remove the server login if it exists
USE master;
GO
DROP LOGIN IF EXISTS testuser;
GO

-- Remove database user if it exists
USE TestDB;
GO
IF EXISTS (SELECT 1 FROM sys.database_principals WHERE name = 'testuser')
    DROP USER testuser;
GO

-- Remove server login if it exists
USE master;
GO
IF EXISTS (SELECT 1 FROM sys.server_principals WHERE name = 'testuser')
    DROP LOGIN testuser;
GO




-- 1) Create the SQL login at server level
USE master;
GO
CREATE LOGIN testuser 
WITH PASSWORD = 'TestUser@12345',
     CHECK_POLICY = OFF;
GO

-- 2) Create the database user in TestDB mapped to that login
USE TestDB;
GO
CREATE USER testuser FOR LOGIN testuser;
GO

-- 3) Grant read & write permissions
ALTER ROLE db_datareader ADD MEMBER testuser;
ALTER ROLE db_datawriter  ADD MEMBER testuser;
GO
