@echo off
SETLOCAL

SET PUTTY_DIR="C:\Program Files\PuTTY"
SET PSCP=pscp.exe
SET PLINK=plink.exe

SET DEPLOY_DIR=/deployment/staging

IF "%1" == "-h" (
CALL :ShowHelp
GOTO EXIT
)

CALL :ReadInput %*

CALL :ValidateInput

IF "%SCRIPT_ERROR%"=="1" (
	goto ShowHelp 
	)

CALL %CONFIG_FILE%
CALL :CheckConfiguration


SET DEPLOY_HOST=%NODE_PARAM_NAME%

for %%i IN ("%ENV_FILE_PATH%") do (
	SET ENV_FILE_NAME=%%~ni
	SET ENV_FILE_EXT=%%~xi
)

SET FILE_NAME=%ENV_FILE_NAME%%ENV_FILE_EXT%

for %%i IN ("%PACKAGE_PATH%") do (
	SET ENV_PACKAGE_NAME=%%~ni
	SET ENV_PACKAGE_EXT=%%~xi
)

SET PACKAGE_NAME=%ENV_PACKAGE_NAME%%ENV_PACKAGE_EXT%


SET PUTTY_PASSWORD_ARG=-pw %USER_PASSWORD%

::echo %PUTTY_DIR%\%PLINK% -ssh %PUTTY_PASSWORD_ARG% %DEPLOY_USER%@%DEPLOY_HOST% 

echo y | %PUTTY_DIR%\%PLINK% -ssh %PUTTY_PASSWORD_ARG% %DEPLOY_USER%@%DEPLOY_HOST% "exit" 2>&1 | findstr /C:"Access denied" && (
echo error while ...............
EXIT /B 1

)

:: Creating Direcotry
::%PUTTY_DIR%\%PLINK% -batch -ssh %PUTTY_PASSWORD_ARG% %DEPLOY_USER%@%DEPLOY_HOST% "mkdir --parents %DEPLOY_DIR%"

:: Copying Packages
::%PUTTY_DIR%\%PSCP% -batch %PUTTY_PASSWORD_ARG% %PACKAGE_PATH% %ENV_FILE_PATH% %DEPLOY_USER%@%DEPLOY_HOST%:%DEPLOY_DIR% 

ECHO %FILE_NAME%
ECHO %PACKAGE_NAME%
::Changing Permission and building environment argument
%PUTTY_DIR%\%PLINK% -batch -ssh %PUTTY_PASSWORD_ARG% %DEPLOY_USER%@%DEPLOY_HOST% "cd %DEPLOY_DIR% && chmod 755 %FILE_NAME%"
 
::ECHO 

::> nul 2>&1 


ECHO SUCCESS



EXIT /B 0

:ValidateInput	
	echo %PACKAGE_PATH%
	IF %PACKAGE_PATH%=="" (
 		echo ERROR : -p does not specified
 		SET SCRIPT_ERROR=1
 	) else (		
		if not exist %PACKAGE_PATH% (
		echo ERROR : Path %PACKAGE_PATH% does not exists.
 		SET SCRIPT_ERROR=1
		)
	)
	ECHO %CONFIG_FILE%
	IF %CONFIG_FILE%=="" (
 		echo ERROR : -c does not specified
 		SET SCRIPT_ERROR=1
 	) else (		
		if not exist %CONFIG_FILE% (
		echo ERROR : Path %CONFIG_FILE% does not exists.
 		SET SCRIPT_ERROR=1
		)
	) 
	
	ECHO DONE

EXIT /B 1	

:CheckConfiguration
SET Error = 0
	IF %DEPLOY_USER%=="" (
	echo Error: Did not specify "DEPLOY_USER"
	SET SCRIPT_ERROR=1
	)
	
	IF %ENV_FILE_PATH%=="" (
	echo Error: Did not specify "ENV_FILE_PATH"
	SET SCRIPT_ERROR=1
	)
EXIT /B 0
	
:ReadInput
	:loop
	IF NOT "%1" == "" (
		IF "%1" == "-p" (
		SET PACKAGE_PATH=%2
		::echo %PACKAGE_PATH%
		SHIFT	
		)
		IF "%1" == "-id" (
		SET DEPLOYER_ID=%2
		::echo %DEPLOYER_ID% 
		SHIFT	
		)
		IF "%1" == "-pw" (
		SET USER_PASSWORD=%2
		::ECHO %USER_PASSWORD%
		SHIFT	
		)
		IF "%1" == "-host" (
		SET NODE_PARAM_NAME=%2
		ECHO %NODE_PARAM_NAME%
		SHIFT	
		)
		IF "%1" == "-ss" (
		SET START_STOP=%2
		SHIFT	
		)
		IF "%1" == "-c" (
		SET CONFIG_FILE=%2
		::echo %CONFIG_FILE%
		SHIFT	
		)
		SHIFT
		goto loop
	)

EXIT /B 0


:ShowHelp	
echo.
echo deploy.bat usage
echo -h  Show help
echo -p  Package path
echo -id Deployer Id
echo -pw Password
echo -host node parameter name
echo -ss start stop the process
echo -c configuration file
echo. 	DEPLOY_USER user to use when deploying
echo. 	ENV_FILE_PATH path to environment file
echo. 	HOST_PARAM_NAME1 names hosts 

goto :END

:END
EndLocal
EXIT /B 0
	

