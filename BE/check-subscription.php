<?php
    include('connection.php');
    $device = $_GET['device'];
    
    $now = date('Y-m-d H:i:s', strtotime('-10 minutes'));


    $sql =  "SELECT * FROM `subscription` a where a.device = '$device' and sub_date > '$now'";
    $query = $conn -> query($sql);
    $row = $query-> fetch_array(MYSQLI_ASSOC);
    if($row) {
        header("HTTP/1.1 200 subscription");
    } else {
        header("HTTP/1.1 400 Not subscription");
    }
?>