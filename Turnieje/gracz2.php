﻿<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<META HTTP-EQUIV="Content-Type" CONTENT="text/html; charset=utf-8">
</head>
<body>
<script type="text/javascript">
function wstaw44(p_nr,p_fname,p_lname){
	window.opener.document.<?php echo $_REQUEST['form'];?>.p_<?php echo $_REQUEST['pole'];?>.value=p_nr;
  window.opener.document.<?php echo $_REQUEST['form'];?>.player_10<?php echo $_REQUEST['pole'];?>.value= p_lname + ' ' + p_fname;
  window.close();
}

</SCRIPT>
<?php
error_reporting(E_ERROR | E_PARSE);
include_once("db.php");
global $currentlang; 
$currentlang = "german";
if (!strncmp($_SERVER['HTTP_ACCEPT_LANGUAGE'], 'pl', strlen('2'))) {
	$currentlang = "polish";
}
include_once("language/lang-".$currentlang.".php");

echo _Wybierzgracza;

echo '<form method=post>';
echo _Wpiszfragmentnazwygracza.' <input type="text" name="name" value="'.$_POST[name].'" /> <br><br>';
echo '<input type="submit" value="'._Wyslij.'" />';
echo '<input type="hidden" value="'.$_REQUEST['cup_id'].'" />';
echo '</form>';

if($_POST[name]!=''){
  
  $sql = "select id,fname,lname,player_number FROM "._DB_PREFIX."_players ".
   "where (fname LIKE '%$_POST[name]%' or lname LIKE '%$_POST[name]%') and cup_id=".$_REQUEST['cup_id'];
   
	$result = mysql_query($sql);

while($row = mysql_fetch_row($result)){
	echo "$row[1] $row[2] <a href=\"javascript:wstaw44('$row[3]','$row[1]','$row[2]');\">"._wstaw."</a><br>";
}
	
}

?>
</body>
</html>
