<?php
#Vars - written at 2011-03-31
$dbhost="localhost";
$dbname="usr_web363_1";
$dbuser="web363";
$dbpass="ddsv2008";
$dbport=3306;
$dbsocket="";
$compression=1;
$backup_path="/srv/www/htdocs/web363/html/mysqldumper/work/backup/";
$logdatei="/srv/www/htdocs/web363/html/mysqldumper/work/log/mysqldump_perl.log.gz";
$completelogdatei="/srv/www/htdocs/web363/html/mysqldumper/work/log/mysqldump_perl.complete.log.gz";
$sendmail_call="/usr/sbin/sendmail -t -i";
$nl="\n";
$cron_dbindex=-3;
$cron_printout=1;
$cronmail=1;
$cronmail_dump=1;
$cronmailto="franz\@pub-highlander.de";
$cronmailto_cc="";
$cronmailfrom="Dartligaverwaltung";
$cron_use_sendmail=1;
$cron_smtp="localhost";
$cron_smtp_port="25";
@cron_db_array=("usr_web363_1");
@cron_dbpraefix_array=("");
@cron_command_before_dump=("");
@cron_command_after_dump=("");
@ftp_server=("","","");
@ftp_port=(21,21,21);
@ftp_mode=(0,0,0);
@ftp_user=("","","");
@ftp_pass=("","","");
@ftp_dir=("/","/","/");
@ftp_timeout=(30,30,30);
@ftp_useSSL=(0,0,0);
@ftp_transfer=(0,0,0);
$mp=0;
$multipart_groesse=0;
$email_maxsize=10485760;
$auto_delete=1;
$max_backup_files=5;
$perlspeed=10000;
$optimize_tables_beforedump=1;
$logcompression=1;
$log_maxsize=1048576;
$complete_log=1;
$my_comment="";
?>