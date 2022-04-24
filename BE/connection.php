<?php
session_start();
$host = "localhost";
$user = "id18623751_admin";
$password = "Aa@123456789";
$dbName = "id18623751_music";
$conn = mysqli_connect($host,$user,$password,$dbName);
mysqli_set_charset($conn,"utf8");

function getCurrentDate(){
	return date("Y-m-d H:i:s");
}
?>